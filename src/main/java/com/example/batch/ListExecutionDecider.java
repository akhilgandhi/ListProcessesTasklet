package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class ListExecutionDecider implements JobExecutionDecider {
	
	private static final Logger logger = LoggerFactory.getLogger(ListExecutionDecider.class);
	
	public static final String READ_DONE = "READ_DONE";
	public static final String READ_AGAIN = "READ_AGAIN";

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		int totalRecords = jobExecution.getExecutionContext().getInt("totalRecords");
		int count = jobExecution.getExecutionContext().getInt("count");
		String status;
		if(count == totalRecords) {
			logger.debug("No more records to read ");
			status = READ_DONE;
		}
		else {
			logger.debug("There are more records to read ");
			status = READ_AGAIN;
		}
		return new FlowExecutionStatus(status);
	}

}
