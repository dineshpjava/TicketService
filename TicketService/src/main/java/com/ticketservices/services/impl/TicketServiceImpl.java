package com.ticketservices.services.impl;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import com.ticketservices.models.Seat;
import com.ticketservices.models.SeatHold;
import com.ticketservices.models.SeatHoldCache;
import com.ticketservices.models.VenueLevelCache;
import com.ticketservices.services.TicketService;
import com.ticketservices.services.VenueLevel;

/**
 * This class is an implementation {@link TicketService}. It provides different {@link VenueLevel} ticket related services to customers. 
 * @author Dinesh
 *
 */
public class TicketServiceImpl implements TicketService{

	private VenueLevel venueLevel;
	private VenueLevelCache venueLevelCache;
	private Hashtable<String, SeatHold> confirmedTicets;
	private SeatHoldCache seatHoldCache;
	private ReentrantLock lock = null;

	
	public TicketServiceImpl() {
		venueLevelCache = VenueLevelCache.getVenueLevelCache();
		confirmedTicets = new Hashtable<String, SeatHold>();
		seatHoldCache = SeatHoldCache.getSeatHoldCache();
	}
	
	/**
	 * This method provides the number of seats in the requested level that are neither held nor reserved.
	 * 
	 * @param venueLevelIndex a numeric venue level identifier to limit the search.
	 * @return the number of tickets available on the provided level	 .
	 */
	public int numSeatsAvailable(Integer venueLevelIndex) {
		venueLevel = venueLevelCache.getVenueLevel(venueLevelIndex);
		int count = venueLevel.getAvailableSeats();
		return count;
	}

	/**
	 * Allocates best available seats in, as minimum level as possible in the provided Venue Level range. It holds the seats and return the SeatHold object for further reservation if the desired level has enough number of seats. Else, if not able to accommodate the seats in desired levels due to insufficient seats in the level range, returns a message, to choose different level options.
	 *
	 * @param numSeats the number of seats to find and hold.
	 * @param minLevel the minimum venue level.
	 * @param maxLevel the maximum venue level.
	 * @param customerEmail unique identifier for the customer.
	 * @return a SeatHold object identifying the specific seats and related.
	 * 
	 */
	public SeatHold findAndHoldSeats(int numSeats, Integer minLevel, Integer maxLevel, String customerEmail) throws RuntimeException{
		int actualMinLevel=minLevel;
		SeatHold seatHold = new SeatHold(customerEmail);
		while(minLevel<=maxLevel){
			venueLevel = venueLevelCache.getVenueLevel(minLevel);
			lock = venueLevel.getLock();
			lock.lock();
			if(venueLevel.getAvailableSeats()>=numSeats){
				seatHold = venueLevel.findAndHoldSeats(numSeats, seatHold);
				lock.unlock();
				break;
			}
			else{
				minLevel++;
				lock.unlock();
			}
		}
		if(minLevel>maxLevel){
			if((actualMinLevel==1)&&(maxLevel==4)){
				throw new RuntimeException(" The number of seats requested are not available as a whole in any Venue Level, please try booking in installments");
			}
			throw new RuntimeException(" Sorry... The seats requested are not available in your desired Venue Level, please try with different Venue Levels");
		}
		seatHoldCache.addToSeatHoldCache(seatHold);
		return seatHold;
	}
	
	/**
	 * This method reserves held seats, if the SeatHold object isn't expired, and calculates the cost and returns confirmation. If SeatHold object is expired, return null as an indication of SeatHold expire.
	 * 
	 * @param seatHoldId the seat hold Identifier.
	 * @param customerEmail the email address of the customer to which the seat hold is assigned.
	 * @return a reservation confirmation code.
	 */
	public String reserveSeats(int seatHoldId, String customerEmail) {
		int cost=0;
		SeatHold seatHold=null;
		synchronized (seatHoldCache) {
			if(seatHoldCache.getCachedSeats().containsKey(seatHoldId)){
				seatHold = seatHoldCache.getCachedSeats().get(seatHoldId);
				seatHoldCache.getCachedSeats().remove(seatHoldId);
				Vector<Seat> seats = seatHold.getHeldSeats();
				for (Seat seat : seats) {
					seat.setColor("black");
				}
			}
		}
		if(seatHold!=null){
			venueLevel = venueLevelCache.getVenueLevel(seatHold.getVenueLevel());
			cost = venueLevel.calculateCost(seatHold);
			String confirmation = "" + seatHoldId + "-" + customerEmail.substring(0,4) + "-" + cost;
			confirmedTicets.put(confirmation,seatHold);
			return confirmation;
		}
		return null;
	}
}
