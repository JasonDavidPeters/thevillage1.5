package com.jasondavidpeters.thevillage1_5.entity;

import com.jasondavidpeters.thevillage1_5.world.Location;

public class Entity {
	
	protected String name;
	protected Location location;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}

}
