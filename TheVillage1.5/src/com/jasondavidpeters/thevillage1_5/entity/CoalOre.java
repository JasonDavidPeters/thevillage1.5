package com.jasondavidpeters.thevillage1_5.entity;

import com.jasondavidpeters.thevillage1_5.world.Location;
import com.jasondavidpeters.thevillage1_5.world.Mine;

public class CoalOre extends Ore {

	
	public CoalOre(String name) {
		super(name);
		chance = 50;
		minQuantity = 1;
		maxQuantity = 3;
		minHarvestTime = 40;
		maxHarvestTime = 120;
		cost= 5;
		((Mine)Location.mine).add(this);
	}
}
