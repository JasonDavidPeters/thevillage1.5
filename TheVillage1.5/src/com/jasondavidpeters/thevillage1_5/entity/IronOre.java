package com.jasondavidpeters.thevillage1_5.entity;

import com.jasondavidpeters.thevillage1_5.world.Location;
import com.jasondavidpeters.thevillage1_5.world.Mine;

public class IronOre extends Ore {

	public IronOre(String name) {
		super(name);
		chance = 20;
		minQuantity = 1;
		maxQuantity = 2;
		minHarvestTime = 40;
		maxHarvestTime = 120;
		cost = 20;
		((Mine)Location.mine).add(this);
	}
}
