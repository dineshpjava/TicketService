package com.ticketservices.venuelevels;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import com.ticketservices.models.Seat;
import com.ticketservices.models.SeatHold;
import com.ticketservices.services.VenueLevel;

/** 
 * This class is an implementation of Venue Level. Provides different Orchestra Venue Level services.
 * @author Dinesh
 *  
 */
public class Orchestra implements VenueLevel{
	private static Integer levelValue;
	private static Integer price;
	private static Integer rows;
	private static Integer seatsPerRow;
	private static Orchestra venueLevel = null;
	private static Vector<Seat> orchestraSeatsList;
	private ReentrantLock lock = null;
	
	/**
	 * private constructor initializes the class fields. 
	 */
	private Orchestra() {
		super();
		levelValue = 1;
		price = 100;
		rows = 25;
		seatsPerRow = 50;
		orchestraSeatsList = new Vector<Seat>();
		lock = new ReentrantLock(true);
		createOrchestraSeatsList();
		
	}
	
	/**
	 * Creates singleton object for this class and initiates setup, for the first time. 
	 * 
	 * @return Orchestra singleton object
	 */
	public static Orchestra getVenueLevel(){
		synchronized (Orchestra.class) {
			if(venueLevel == null){
				venueLevel = new Orchestra();
			}
		}
		return venueLevel;
	}
		
	/**
	 *  Initializes, collection of seats in Orchestra class 
	 */
	private static void createOrchestraSeatsList(){
		for(Character c = 'A' ; c < 'A'+rows; c++){
			for(int i=1 ; i<= seatsPerRow ; i++){
				Seat seat = new Seat(c + "#" + i, "white");
				orchestraSeatsList.add(seat);
			}
		}
	}

	/**
	 * Provides all the seats of the Orchestra class.
	 * @return a collection of all {@link Seat}
	 */
	private Vector<Seat> getAllSeats(){
		return orchestraSeatsList;
	}

	/**
	 * Provides information about all the seats that are neither held nor reserved.
	 * 
	 * @return number of seats available i.e., neither held or reserved.
	 */
	public int getAvailableSeats() {
		Vector <Seat> allSeats = getAllSeats();
		Vector<Seat> allAvailableSeats = new Vector<Seat>();
		lock.lock();
		for (Seat seat : allSeats) {
			if(seat.getColor()=="white"){
				allAvailableSeats.add(seat);
			}	
		}
		int count=allAvailableSeats.size();
		lock.unlock();
		return count;
	}

	/**
	 * Provides the best available seats in this level to SeatHold object.
	 * 
	 * @param numSeats number of seats to find and hold.
	 * @param SeatHold reference of seatHold object, to add seats.
	 * @return SeatHold object identifying the specific seats and related information.
	 */
	public SeatHold findAndHoldSeats(int numSeats, SeatHold seatHold) {
		Vector<Seat> allSeats = getAllSeats();
		int count=0,index=0;	
		while(count < numSeats ){
			Seat seat = allSeats.get(index);
			if(seat.getColor()=="white"){
				seatHold.holdSeat(seat);
				seat.setColor("grey");
				count++;
			}
			index++;
		}
		seatHold.setVenueLevel(levelValue);
		return seatHold;
	}
	
	/**
	 * Calculates the cost of reservation.
	 * 
	 * @param SeatHold object, to calculate the price of reservation
	 * @return amount to be paid for reservation.
	 */
	public int calculateCost(SeatHold seatHold){
		return ((seatHold.getHeldSeats().size())*price);
	}
	
	public ReentrantLock getLock(){
		return lock;
	}
}
