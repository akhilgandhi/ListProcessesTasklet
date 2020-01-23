package com.example.batch.writer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class WriterTasklet implements Tasklet {
	
	private static final Logger logger = LoggerFactory.getLogger(WriterTasklet.class);
	
	private List<Integer> lines;

	public List<Integer> getLines() {
		return lines;
	}

	public void setLines(List<Integer> lines) {
		this.lines = lines;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		lines = new ArrayList<Integer>();
		setLines((List<Integer>) executionContext.get("lines")); 
		logger.debug("Starting writer ");
		for(int i = 0; i < lines.size(); i++) {
			logger.debug("Writing line " + lines.get(i));
		}
		logger.debug("Ending writer ");
		return RepeatStatus.FINISHED;
	}

}
