package com.test.services;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ticketservices.services.TicketService;
import com.ticketservices.services.impl.TicketServiceImpl;

public class TicketServiceTest {

	private TicketService ticketService;
	private int numSeats, minLevel, maxLevel;
	private String customerEmail;
	
	@Before
	 public void init(){
		numSeats=1000;
		minLevel=1;
		maxLevel=1;
		customerEmail="dinesh@java";
		
		ticketService = new TicketServiceImpl();
	}

	@Test (expected = RuntimeException.class) 
	public void numSeatsAvailableTest() {

		//true case 
		assertEquals(2000, ticketService.numSeatsAvailable(2));

		//false case : doesn't return same value
		assertNotSame(1,ticketService.numSeatsAvailable(1));

		//false case : throws exception
		assertNull(ticketService.numSeatsAvailable(0));
		
	}

	@Test (expected = RuntimeException.class)
	public void findAndHoldSeats(){
		//seatHoldTest = orchestra.findAndHoldSeats(numSeats, seatHold);
		assertNotNull(ticketService.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail));
		

		//false case : throws exception
		assertNull(ticketService.findAndHoldSeats(numSeats, minLevel, maxLevel, ""));

	}
	
	@Test 
	public void reserveSeats(){		
		int seatHoldId = ticketService.findAndHoldSeats(1,3,4,customerEmail).getSeatHoldId();
		String str = ticketService.reserveSeats(seatHoldId, customerEmail);
		String strTest = seatHoldId+"-dine-50";
		
		//true case:
		assertTrue(strTest.equals(str)); 
	
		//false case: null string returned the SaetHold 0 doesn't exist
		String test = ticketService.reserveSeats(0, customerEmail);
		assertNull(test); 
	}
}