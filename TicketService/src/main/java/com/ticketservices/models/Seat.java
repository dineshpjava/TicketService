package com.ticketservices.models;

/**
 * This class represents seats available in all the Venues.
 * @author Dinesh
 *
 */
public class Seat {

	private String seatId;
	private String color;
	
	/**
	 * Constructor by default initializes a seat to a unique value and sets it as available.
	 * 
	 * @param seatId is unique for every seat in a VenueLevel. 
	 * @param color used to indicate the seat status as available or held or reserved. 
	 */
	public Seat(String seatId, String color) {
		this.seatId = seatId;
		this.color = color;
	}

	/**
	 * Default Constructor
	 */
	public Seat() {
		super();
		//no-op
	}



	/**
	 * Provides the unique id of the seat.
	 * @return seatId which is unique for every seat in a VenueLevel.
	 */
	public String getSeatId() {
			return seatId;
	}

	/**
	 * Provides the status of the seat.
	 * @return color which is the status of the seat.
	 */
	public String getColor() {
		return color;
	}	
	
	/**
	 * To set the set the status of the seat.
	 * @param color which is status of the seat.
	 */
	public void setColor(String color) {
		synchronized (this) {
			this.color = color;
		}
	}	
}