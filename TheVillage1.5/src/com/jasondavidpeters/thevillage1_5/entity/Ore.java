package com.jasondavidpeters.thevillage1_5.entity;

public class Ore extends Entity {
	
	public static Ore coalOre = new CoalOre("Coal");
	public static Ore ironOre = new IronOre("Iron");

	/*
	 * Types of ore
	 */
	protected int minQuantity;
	protected int maxQuantity;
	protected int minHarvestTime;
	protected int maxHarvestTime;
	protected int cost;
	protected int chance; // out of 100?
	
	protected enum HARVEST_TOOL {
		WOODEN, STONE, IRON, STEEL, DIAMOND
	};
	
	public Ore() { 
		
	}
	
	public Ore(String name) {
		this.name=name;
	}
	public int getChance() {
		return chance;
	}
	public int getMinHarvestTime() {
		return minHarvestTime;
	}
	public int getMaxHarvestTime() {
		return maxHarvestTime;
	}
	public int getMaxQuantity() {
		return maxQuantity;
	}
	public int getMinQuantity() { 
		return minQuantity;
	}
	public int getCost() {
		return cost;
	}
}
