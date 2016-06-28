package com.ticketservices.models;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import com.ticketservices.threads.CacheCleanUpThread;
/**
 * 
 * seatHoldCache class acts as cache and maintains the SeatHold objects and it's expire.
 */
public class SeatHoldCache {

	private static Hashtable<Integer, SeatHold> cachedSeats;
	private static SeatHoldCache seatHoldCache=null;
	
	private SeatHoldCache(){
		cachedSeats = new Hashtable<Integer, SeatHold>();	
	}
	
	/**
	 * Provides the singleton object of the SeatHoldCache.
	 * @return seatHoldCache singleton object of the SeatHoldCache.
	 */
	public static SeatHoldCache getSeatHoldCache(){
		synchronized (SeatHoldCache.class) {
			if(seatHoldCache==null){
				seatHoldCache = new SeatHoldCache();
			}
		}
		return seatHoldCache;
	}
	
	/**
	 * It adds the SeatHold object to it's seatHoldCache when ever a seatHold is generated.
	 * @param seatHold object which has to be added.
	 */
	public void addToSeatHoldCache(SeatHold seatHold){
		cachedSeats.put(seatHold.getSeatHoldId(), seatHold);
	}

	/**
	 * Checks if a specific {@link SeatHold} is present in it's seatHoldCache or not
	 * @param key unique {@link SeatHold} Id.
	 * @return {@link SeatHold} object if present or null if not present.
	 */
	public SeatHold getSeatHeld(int key){
		if(cachedSeats.contains(key))
			return cachedSeats.get(key);
		return null;
	}
	
	/**
	 * It provides the Hashtable of all the cached {@link SeatHold} objects
	 * @return cachedSeats which is the Hashtable of all the cached SeatHold. 
	 */
	public Hashtable<Integer, SeatHold> getCachedSeats(){
		return cachedSeats;
	}
	
	/**
	 * This method checks for the expire of the SeatHold by the daemon thread {@link CacheCleanUpThread} for every 20 seconds and every SeatHold object expires in 60 seconds.
	 */
	public void checkExpiryOfSeatHold(){
		Collection<SeatHold> seatHoldList = cachedSeats.values();
		for (SeatHold seatHold : seatHoldList) {
			if(seatHold.getExpireTime()<System.currentTimeMillis()){
				System.out.println("************* The expired SeatHold ID is : " + seatHold.getSeatHoldId() +" *************");
				cachedSeats.remove(seatHold.getSeatHoldId());
				Vector<Seat> seats = seatHold.getHeldSeats();
				for (Seat seat : seats)
					seat.setColor("white");
				System.out.println("******************* Cleaned up *******************");
			}
		}
	}
}
