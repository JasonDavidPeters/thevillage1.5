package com.jasondavidpeters.thevillage1_5.world;

import java.util.ArrayList;
import java.util.List;

import com.jasondavidpeters.thevillage1_5.entity.Ore;

public class Mine extends Location {
	
	private List<Ore> availableOres = new ArrayList<Ore>();
	
	public Mine(String name) {
		super(name);
	}
	
	public void add(Ore ore) {
		availableOres.add(ore);
	}
	
	/*
	 * TODO: List of entities that can be harvested
	 * at the mine
	 */
	public List<Ore> getAvailableOres() {
		return availableOres;
	}

}
