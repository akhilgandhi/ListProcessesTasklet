package com.example.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class JobEndingTasklet implements Tasklet {
	
	private static final Logger logger = LoggerFactory.getLogger(JobEndingTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		logger.debug("This is the end of \"listJob\"");
		return RepeatStatus.FINISHED;
	}

}
