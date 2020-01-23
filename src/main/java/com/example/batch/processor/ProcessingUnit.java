package com.example.batch.processor;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessingUnit implements Callable<Integer> {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessingUnit.class);
	
	private Integer lines;

	public ProcessingUnit(Integer lines) {
		this.lines = lines;
	}

	@Override
	public Integer call() throws Exception {
		
		Integer task = 0;
		
		logger.debug("Executing task ..." + lines);
		task = lines;
		Thread.sleep(200);
		
		return task;
	}

}
