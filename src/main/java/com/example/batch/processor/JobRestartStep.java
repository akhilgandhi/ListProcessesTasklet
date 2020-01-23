package com.example.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class JobRestartStep implements Tasklet {
	
	private static final Logger logger = LoggerFactory.getLogger(JobRestartStep.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.debug("Restarting the previous job");
		return RepeatStatus.FINISHED;
	}

}
