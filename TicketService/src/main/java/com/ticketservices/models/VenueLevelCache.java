package com.ticketservices.models;

import java.util.Hashtable;

import com.ticketservices.services.VenueLevel;
import com.ticketservices.venuelevels.BalconyOne;
import com.ticketservices.venuelevels.BalconyTwo;
import com.ticketservices.venuelevels.Main;
import com.ticketservices.venuelevels.Orchestra;
/**
 * 
 * Theater class maintains and holds all the Venue Levels as a unit
 *
 */
public class VenueLevelCache {
	private Hashtable<Integer,VenueLevel> levelsInTheater;
	private static VenueLevelCache venueLevelCache;
	
	private VenueLevelCache(){
		levelsInTheater = new Hashtable<Integer,VenueLevel>();
		levelsInTheater.put(1, Orchestra.getVenueLevel());
		levelsInTheater.put(2, Main.getVenueLevel());
		levelsInTheater.put(3, BalconyOne.getVenueLevel());
		levelsInTheater.put(4, BalconyTwo.getVenueLevel());
	}
	
	public static VenueLevelCache getVenueLevelCache(){
		synchronized (VenueLevelCache.class) {
			if(venueLevelCache == null){
				venueLevelCache = new VenueLevelCache();
			}
		}
		return venueLevelCache;
	}
	
	public VenueLevel getVenueLevel(Integer index){
		return levelsInTheater.get(index);
	}
}
