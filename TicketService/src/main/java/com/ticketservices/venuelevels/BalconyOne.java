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
public class BalconyOne implements VenueLevel{
	private static Integer levelValue;
	private static Integer price;
	private static Integer rows;
	private static Integer seatsPerRow;
	private static BalconyOne venueLevel = null;
	private static Vector<Seat> balconyOneSeatsList;
	private ReentrantLock lock = null;

	/**
	 * private constructor initializes the class fields 
	 */
	private BalconyOne() {
		levelValue = 3;
		price = 50;
		rows = 15;
		seatsPerRow = 100;
		balconyOneSeatsList = new Vector<Seat>();
		lock = new ReentrantLock(true);
		createBalconyOneSeatsList();
	}
	
	/**
	 * creates singleton object for this class and initiates setup, for the first time 
	 * 
	 * @return BalconyOne's singleton object
	 */
	public static BalconyOne getVenueLevel(){
		synchronized (BalconyOne.class) {
			if(venueLevel == null){
				venueLevel = new BalconyOne();
			}
		}
		return venueLevel;
	}
	
	/**
	 *  Initializes, collection of seats in BalconyOne class 
	 */
	private static void createBalconyOneSeatsList(){
		for(Character c = 'A' ; c < 'A'+rows; c++){
			for(int i=1 ; i<= seatsPerRow ; i++){
				Seat seat = new Seat(c + "#" + i, "white");
				balconyOneSeatsList.add(seat);
			}
		}
	}
	
	/**
	 * Provides all the seats of the BalconyOne class.
	 * @return a collection of all {@link Seat}
	 */
	private Vector<Seat> getAllSeats(){
		return balconyOneSeatsList;
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
	 * @param seatHold reference of seatHold object, to add seats.
	 * @return seatHold object identifying the specific seats and related information.
	 */
	public synchronized SeatHold findAndHoldSeats(int numSeats, SeatHold seatHold) {
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
	 * @param seatHold object, to calculate the price of reservation
	 * @return amount to be paid for reservation.
	 */
	public int calculateCost(SeatHold seatHold){
		return ((seatHold.getHeldSeats().size())*price);
	}
	
	public ReentrantLock getLock(){
		return lock;
	}
}
