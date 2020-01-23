package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class ListExecutionRestartDecider implements JobExecutionDecider {
	
	private static final Logger logger = LoggerFactory.getLogger(ListExecutionRestartDecider.class);
	
	public static final String ON_RESTART = "ON_RESTART";
	public static final String NOT_ON_RESTART = "NOT_ON_RESTART";

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		
		String status;
		if("true".equals(jobExecution.getExecutionContext().getString("restart"))) {
			logger.debug("Restart is required ");
			status = ON_RESTART;
		}
		else {
			logger.debug("No restart is required ");
			status = NOT_ON_RESTART;
		}
		return new FlowExecutionStatus(status);
	}

	

}
