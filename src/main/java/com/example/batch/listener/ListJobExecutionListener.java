package com.example.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;

public class ListJobExecutionListener implements JobExecutionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ListJobExecutionListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		
		logger.debug("Doing BEFORE JOB tasks ");
		jobExecution.getExecutionContext().putInt("start", 0);
		jobExecution.getExecutionContext().putInt("totalRecords", 10);

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		ExecutionContext executionContext = jobExecution.getExecutionContext();
		logger.debug("JOB STATS ");
		logger.debug("JOB BATCH STATUS " + jobExecution.getStatus() + " JOB EXIT STATUS " + jobExecution.getExitStatus());
		logger.debug("START " + executionContext.getInt("start") + " COUNTER " + executionContext.getInt("count"));
		logger.debug("LIST " + executionContext.get("lines"));
	}

}
