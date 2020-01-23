package com.example.batch.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class ProcessorTasklet implements Tasklet {

	private static final Logger logger = LoggerFactory.getLogger(ProcessorTasklet.class);

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
		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();

		logger.debug("Starting processor ");

		for (int i = 0; i < lines.size(); i++) {
			tasks.add(new ProcessingUnit(lines.get(i)));
		}
		ExecutorService executor = Executors.newFixedThreadPool(4);
		CompletionService<Integer> service = new ExecutorCompletionService<Integer>(executor);

		for (Callable<Integer> callables : tasks) {
			service.submit(callables);
		}

		long startProcessingTime = System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			Future<Integer> future = service.take();
			if (future.isDone()) {
				int result = future.get();
				logger.debug("Processed item " + result);
			}
		}
		long totalProcessingTime = System.currentTimeMillis() - startProcessingTime;

		awaitTerminationAfterShutdown(executor);
		logger.debug("Proccesing took " + totalProcessingTime + " ms");
		logger.debug("Ending processor ");
		return RepeatStatus.FINISHED;
	}

	private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
		threadPool.shutdown();
		try {
			if (!threadPool.awaitTermination(2, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();
			}
		} catch (InterruptedException ex) {
			threadPool.shutdownNow();
			Thread.currentThread().interrupt();
		}

	}

}
