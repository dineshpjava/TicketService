package com.ticketservices.threads;

import com.ticketservices.services.impl.CustomerService;

/**
 * 
 * Every customer is served by providing one thread instance of this class which is served by {@link CustomerService}.
 */
public class CustomerThread implements Runnable{
	
    private static CustomerService customerService;
    private static int threadCount = 0;
    private String customerName;
    
    public CustomerThread(){
    	customerService = CustomerService.getCustomerService();
    	threadCount++;
    	customerName = "Customer#"+ threadCount;
    }
	
    /**
     * This is a overridden run method of Thread Class.
     */
	public void run() {
    	Thread.currentThread().setName(customerName);
    	System.out.println( Thread.currentThread().getName() + " with Thread Id : " + Thread.currentThread().getId()+ " is created");
		customerService.provideService();
		System.out.println( Thread.currentThread().getName() + " exiting the service");
	}
}
