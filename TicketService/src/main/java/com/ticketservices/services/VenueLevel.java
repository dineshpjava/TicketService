package com.ticketservices.services;

import java.util.concurrent.locks.ReentrantLock;

import com.ticketservices.models.SeatHold;
/**
 * Provides different different services of Venue Level.
 * @author Dinesh
 *
 */
public interface VenueLevel {

	/**
	 * Acquires the lock on particular seat to confirm it's availability.
	 * 
	 * @return number of seats available i.e., neither held or reserved.
	 */
	public int getAvailableSeats();
	
	/**
	 * checks for best available seats in this level and adds it to SeatHold object.
	 * 
	 * @param numSeats number of seats to find and hold.
	 * @param seatHold reference of seatHold object, to add seats.
	 * @return SeatHold object identifying the specific seats and related information.
	 */
	public SeatHold findAndHoldSeats(int numSeats, SeatHold seatHold);
		
	/**
	 * calculates the cost of reservation.
	 * 
	 * @param seatHold object, to calculate the price of reservation.
	 * @return amount to be paid for reservation.
	 */
	public int calculateCost(SeatHold seatHold);
	
	/**
	 * Provides the lock of the class.
	 * 
	 * @return lock of that class.
	 */
	public ReentrantLock getLock();
}
