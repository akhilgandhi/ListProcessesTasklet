package com.example.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batch.listener.ListJobExecutionListener;
import com.example.batch.processor.JobRestartStep;
import com.example.batch.processor.ProcessorTasklet;
import com.example.batch.reader.ReaderTasklet;
import com.example.batch.writer.JobEndingTasklet;
import com.example.batch.writer.WriterTasklet;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public JobExecutionDecider decider() {
		return new ListExecutionDecider();
	}
	
	@Bean JobExecutionDecider restartDecider() {
		return new ListExecutionRestartDecider();
	}
	
	@Bean
	public Step reader() {
		return stepBuilderFactory.get("reader")
				.tasklet(new ReaderTasklet())
				.build();
	}
	
	@Bean
	public Step processor() {
		return stepBuilderFactory.get("processor")
				.tasklet(new ProcessorTasklet())
				.build();
	}
	
	@Bean
	public Step writer() {
		return stepBuilderFactory.get("writer")
				.tasklet(new WriterTasklet())
				.build();
	}
	
	@Bean
	public Step jobEndingStep() {
		return stepBuilderFactory.get("jobEndingStep")
				.tasklet(new JobEndingTasklet())
				.build();
	}
	
	@Bean
	public Step jobRestartStep() {
		return stepBuilderFactory.get("jobRestartStep")
				.tasklet(new JobRestartStep())
				.build();
	}
	
	@Bean
	public Job listJob() {
		
		Flow flow1 = new FlowBuilder<Flow>("flow1")
				.start(reader())
				.next(processor())
				.next(writer())
				.next(decider()).on(ListExecutionDecider.READ_AGAIN).to(reader())
				.from(decider()).on(ListExecutionDecider.READ_DONE).to(jobEndingStep())
				.end();
		
		Flow flow2 = new FlowBuilder<Flow>("flow2")
				.start(restartDecider()).on(ListExecutionRestartDecider.ON_RESTART).to(jobRestartStep())
				.from(restartDecider()).on(ListExecutionRestartDecider.NOT_ON_RESTART).to(flow1)
				.end();
		
		return jobBuilderFactory.get("listJob")
				.incrementer(new RunIdIncrementer())
				.listener(new ListJobExecutionListener())
				.start(flow2)
				.end()
				.build();
	}
}
