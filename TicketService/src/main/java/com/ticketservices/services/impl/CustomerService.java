package com.ticketservices.services.impl;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import com.ticketservices.models.SeatHold;
import com.ticketservices.services.TicketService;

/**
 * This class provides service to every customer thread, by providing options to choose from {@link TicketServiceImpl}.
 * @author Dinesh
 * 
 */
public class CustomerService {

	private static CustomerService customerService;
	private static Scanner scanner;
	private static TicketService ticketService;
    private static ReentrantLock lock = null;

    private CustomerService(){
    	scanner = new Scanner(System.in);
    	ticketService = new TicketServiceImpl();
        lock = new ReentrantLock(true);
    }
    
    /**
     * This method provides the singleton CustomerService object.
     * 
     * @return CustomerService class singleton object.
     */
    public static CustomerService getCustomerService(){
    	synchronized (CustomerService.class) {
			if(customerService==null){
				customerService = new CustomerService();
			}
		}
    	return customerService;
    }
    
    /**
     * This method provides service to the customer to view, hold and reserve tickets from TicketService.
     */
    public void provideService(){
		boolean quit = false;
		int index=0;
		SeatHold seatHold=null;
		while(!quit){
			try{	
				lock.lock();
				displayOptions();
				index = scanner.nextInt();
				scanner.nextLine();			
				switch(index){
					case 1: getNumberOfSeats();
							break;
					case 2: seatHold = findAndHoldSeats();		
							break;
					case 3: reserveSeats(seatHold);
							break;
					case 4: quit=true;
							lock.unlock();
							break;
					default:quit=true;
							lock.unlock();
							break;		
				}
			}catch(InputMismatchException e){
				scanner.nextLine();
				if(lock.isLocked()){
					lock.unlock();
				}
				System.out.println(Thread.currentThread().getName() + " ...Please enter only integers");
			}catch(RuntimeException e){
				System.out.println("Error - " + Thread.currentThread().getName() + e.getMessage());
			}catch(Exception e){
				System.out.println("Error - " + Thread.currentThread().getName() + e.getMessage());
			}
		}
    }
	
	/**
	 * This method requests the Venue Level as an input from customer, to find the number of seats available in the particular Venue.
	 * 
	 * @return the number of tickets available on the provided level.
	 */
	private void getNumberOfSeats()throws RuntimeException{

		int index;
		System.out.println("Please enter 1, 2, 3, 4 to check number of seat in Orchestera, Main, Balcony-1, Balcony-2 respectively");
		index = scanner.nextInt();
		scanner.nextLine();			
		lock.unlock();
		if(index<=4 && index>=1){
			System.out.println( Thread.currentThread().getName() + " Tickets available are "+ ticketService.numSeatsAvailable(index));
		}
		else{
			System.out.println( Thread.currentThread().getName()+ " The option entered is invalid... Please enter a valid option");
		}
	}
	
	/**
	 * This method requests customer details and specifics as an input from the customer to hold seats.
	 *
	 * @return a SeatHold object identifying the specific seats and related.
	 * 
	 */
	public SeatHold findAndHoldSeats()throws RuntimeException{
		SeatHold seatHold;
		System.out.println("Please enter your emailId");
		String customerEmail = scanner.nextLine();
		System.out.println("Please enter number of seats to be reserved");
		int numSeats = scanner.nextInt();
		System.out.println("Please choose minimum desired level between 1 to 4");
		int minLevel = scanner.nextInt();
		System.out.println("Please choose maximum desired level between 1 to 4");
		int maxLevel = scanner.nextInt();
		lock.unlock();
		if(validate(numSeats,minLevel, maxLevel, customerEmail)){
			System.out.println("Checking for availability....");
			seatHold = ticketService.findAndHoldSeats(numSeats,minLevel, maxLevel, customerEmail);
		}
		else{
			throw new RuntimeException("data not valid");
		}
		if(seatHold!=null){
			int venueLevel = seatHold.getVenueLevel();
			String venueName = getVenueName(venueLevel);
			System.out.println( Thread.currentThread().getName()+ " ....The seats are held in : "+ venueName + "; and the id for SeatHold refernce is " + seatHold.getSeatHoldId());
		
			return seatHold;
		}
		return null;
	}
	
	/**
	 * This method reserves the Seats held in SeatHold object.   
	 *
	 * @param seatHold object on which reservation has to be made.
	 */
	public void reserveSeats(SeatHold seatHold)throws RuntimeException{
		lock.unlock();
		if(seatHold==null){
			throw new RuntimeException(" First find and Hold the seats ");		 
		}
		String confirmation = ticketService.reserveSeats(seatHold.getSeatHoldId(), seatHold.getCustomerEmail());
		if(confirmation==null){
			System.out.println( Thread.currentThread().getName()+ " ...The hold on the seats has expired");
		}
		else{
			System.out.println( Thread.currentThread().getName()+ " ...Ticket confirmation number is " +confirmation);
		}
	}
	
	/**
	 * This method displays relative options for the customer to make choice.
	 */
	private void displayOptions(){
		System.out.println( Thread.currentThread().getName()+ " .... Please choose options from below options");
		System.out.println("1 - To view number of tickets available");
		System.out.println("2 - To find and hold seats available");
		System.out.println("3 - To reserve the tickets ");
		System.out.println("4 - To quit the application ");		
	}

	/**
	 * Validates the customer's details and specifics provided. 
	 * 
	 * @param numSeats the number of seats to find and hold.
	 * @param minLevel the minimum venue level.
	 * @param maxLevel the maximum venue level.
	 * @param customerEmail unique identifier for the customer.
	 * @return true if the inputs are valid.
	 */
	public boolean validate(int numSeats, Integer minLevel, Integer maxLevel, String customerEmail){
		if((customerEmail.indexOf('@')>0)&&(customerEmail.length()>1)){
		}else{
			throw new RuntimeException(" invalid emailId... please check the emailId");			 
		}
		if((numSeats <= 0) || (numSeats > 2000)){
		 throw new RuntimeException(" Minimum number of seats to be reserved is 1 and maximum number of seats available in any level is 2000");
		}
		if(minLevel > maxLevel){
			throw new RuntimeException(" Minimum desired level can't be greater than maximum desired level");
		}
		if((minLevel < 1) || (maxLevel> 4)){
			throw new RuntimeException(" Minimum Venue-Level number can be 1 and maximum Venue-Level can be 4");			 
		}
		return true;
	}
	
	/**
	 * This method provides relevant Venue Level. 
	 * 
	 * @param venueLevel the Id of Venue Level.
	 * @return venueName the name of the Venue Level.
	 */
	public String getVenueName(int venueLevel){
		if(venueLevel==1)
			return "Orchestra Venue Level";
		else if(venueLevel==1)
			return "Main Venue Level";
		else if(venueLevel==1)
			return "Balcony-1 Venue Level";
		else
			return "Balcony-2 Venue Level";
	}
}
