package com.ticketservices.threads;

import com.ticketservices.models.SeatHold;
import com.ticketservices.models.SeatHoldCache;

/**
 * This is a daemon thread used to do the clean up, when ever a {@link SeatHold} expires.
 * @author Dinesh
 *
 */
public class CacheCleanUpThread implements Runnable{

	private SeatHoldCache seatHoldCache;
	private static final long sleepTime=20000; 
	/**
	 * CacheCleanUpThreadTime is set to 20 seconds i.e., clean up is performed for every 20 seconds
	 */
	public CacheCleanUpThread(){
		seatHoldCache = SeatHoldCache.getSeatHoldCache();
	}
	public void run() {
		while(true){
			synchronized (seatHoldCache) {
				seatHoldCache.checkExpiryOfSeatHold();				
			}
			try{
				Thread.sleep(sleepTime);
				Thread.yield();
			}catch(InterruptedException e){
				System.out.println("CacheCleanUpThread is interrupted - " + e.getMessage());
			}
		}
	}
}
