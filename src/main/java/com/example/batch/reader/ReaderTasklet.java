package com.example.batch.reader;

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

public class ReaderTasklet implements Tasklet {
	
	private static final Logger logger = LoggerFactory.getLogger(ReaderTasklet.class);
	
	private static int counter = 0;
	private static int start = 0;
	private int batchCount = 5;
	private List<Integer> lines;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		start = executionContext.getInt("start");
		lines = new ArrayList<Integer>();
		int end = start + batchCount;
		logger.debug("Starting reader ");
		logger.debug("READER 1 START " + start + " END " + end + " COUNTER " + counter);
		for (int i = 0; i < start; i++) {
			logger.debug("Reader skipping line " + (i+1));
		}
		for (int i = start; i < end; i++) {
			logger.debug("Reader reading line " + (i+1));
			lines.add(i+1);
			counter = counter + 1;
		}
		start = end;
		executionContext.putInt("start", start);
		executionContext.putInt("count", counter);
		executionContext.put("lines", lines);
		logger.debug("READER 2 START " + start + " END " + end + " COUNTER " + counter);
		logger.debug("Ending reader ");
		return RepeatStatus.FINISHED;
	}

}
