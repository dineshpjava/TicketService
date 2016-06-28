package com.ticketservices.services.impl;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ticketservices.threads.CacheCleanUpThread;
import com.ticketservices.threads.CustomerThread;

/**
 * This class is the starting point of execution and creates {@link CustomerThread} to serve the customers. 
 * @author Dinesh
 *
 */
public class MainService {

	private static int numOfThreads=0;

	/**
	 * 
	 * @param args number of threads to run is provided as  command line argument at start of execution.
	 */
	public static void main(String[] args) {
		
		Thread cacheCleanUpThread = new Thread( new CacheCleanUpThread());
		cacheCleanUpThread.setDaemon(true);
		cacheCleanUpThread.start();
		try{
			numOfThreads = Integer.parseInt(args[0]);
			Runnable[] runnable= new Runnable[numOfThreads];
			int numThreads=numOfThreads;
			
			ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
			
			for(int i = 0; i<numThreads;i++){
				runnable[i] = new CustomerThread();
				executorService.submit(runnable[i]);
			}
			executorService.shutdown();			
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please enter number of threads to run as a command line argument");
		}
	}
}