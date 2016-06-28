package com.ticketservices.models;

import java.util.Vector;

import com.ticketservices.services.VenueLevel;


/**
 * Seat Hold class has details of customer and seats held along with the expire time of SeatHold object.
 *@author Dinesh
 *
 */
public class SeatHold {

	private static final long timeToExpire = 1*60*1000 ;
	private static int holdCount=0;
	private int seatHoldId;
	private String customerEmail;
	private int venueLevel;
	private long expireTime;
	private Vector<Seat> seatsHeld;
	
	/**
	 * Constructor initializes all the fields of SeatHold class.
	 * @param customerEmail unique identifier for the customer.
	 */
	public SeatHold(String customerEmail) {
		this.customerEmail = customerEmail;
		seatsHeld = new Vector<Seat>();
		holdCount++;
		seatHoldId = holdCount;
		expireTime = + System.currentTimeMillis() + timeToExpire;
	}
	
	/**
	 * Default Constructor
	 */
	public SeatHold() {
		super();
		//no-op
	}
	
	/**
	 * Adds the {@link Seat} to the SeatHold {@link Object}.
	 * @param seat that has to be added as held seat in SeatHold object.
	 */
	public void holdSeat(Seat seat){
		seatsHeld.add(seat);
	}
	
	/**
	 * Provides the list of all the held seats.
	 * @return seatsHeld by the SeatHold objects.
	 */
	public Vector<Seat> getHeldSeats(){
		return seatsHeld;
	}
	/**
	 * Provides the customer's email Id related to the SeatHold.
	 * @return customerEmail which is unique for a customer.
	 */
	public String getCustomerEmail(){
		return customerEmail;
	}
	
	/**
	 * Provides the SeatHold's unique Id.
	 * @return seatHoldId which is unique for every SeatHold. 
	 */
	public int getSeatHoldId(){
		return seatHoldId;
	}

	/**
	 * Provides the {@link VenueLevel} of the corresponding SeatHold.
	 * @return venueLevel which is an integer value uniquely representing the {@link VenueLevel} associated with this SeatHold. 
	 */
	public int getVenueLevel() {
		return venueLevel;
	}

	/**
	 * Set's the unique integer value of the Venue to SeatHold object.
	 * @param venueLevel an unique integer value representing the {@link VenueLevel}.
	 */
	public void setVenueLevel(int venueLevel) {
		this.venueLevel = venueLevel;
	}
	
	/**
	 * Provides the time at which this SeatHold object expires. 
	 * @return expireTime of the SatHold object.
	 */
	public long getExpireTime(){
		return expireTime;
	}
}
