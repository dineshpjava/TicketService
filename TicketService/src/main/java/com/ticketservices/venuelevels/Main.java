package com.ticketservices.venuelevels;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import com.ticketservices.models.Seat;
import com.ticketservices.models.SeatHold;
import com.ticketservices.services.VenueLevel;

/** 
 * This class is an implementation of Venue Level. Provides different Main Venue Level services.
 * @author Dinesh
 *   
 */
public class Main implements VenueLevel {
	private static Integer levelValue;
	private static Integer price;
	private static Integer rows;
	private static Integer seatsPerRow;
	private static Main venueLevel = null;
	private static Vector<Seat> mainSeatsList;
	private ReentrantLock lock = null;

	/**
	 * private constructor initializes the class fields 
	 */
	private Main() {
		super();
		levelValue  = 2;
		price = 75;
		rows = 20;
		seatsPerRow = 100;
		mainSeatsList = new Vector<Seat>();
		lock = new ReentrantLock(true);
		createMainSeatsList();

	}
	
	/**
	 * Creates singleton object for this class and initiates setup, for the first time. 
	 * 
	 * @return Main's singleton object.
	 */
	public static Main getVenueLevel(){
		synchronized (Orchestra.class) {
			if(venueLevel == null){
				venueLevel = new Main();
			}
		}
		return venueLevel;
	}
		
	/**
	 *  Initializes, collection of seats in Main class. 
	 */
	private static void createMainSeatsList(){
		for(Character c = 'A' ; c < 'A'+rows; c++){
			for(int i=1 ; i<= seatsPerRow ; i++){
				Seat seat = new Seat(c + "#" + i, "white");
				mainSeatsList.add(seat);
			}
		}
	}
	
	/**
	 * Provides all the seats of the Main class.
	 * @return a collection of all {@link Seat}
	 * 
	 */
	private Vector<Seat> getAllSeats(){
		return mainSeatsList;
	}

	/**
	 * Provides information about all the seats that are neither held nor reserved.
	 * 
	 * @return number of seats available i.e., neither held nor reserved.
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
		int count = allAvailableSeats.size();
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
			synchronized (venueLevel) {
				if(seat.getColor()=="white"){
					seatHold.holdSeat(seat);
					seat.setColor("grey");
					count++;
				}
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