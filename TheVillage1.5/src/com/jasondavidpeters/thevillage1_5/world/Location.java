package com.jasondavidpeters.thevillage1_5.world;

import java.util.ArrayList;
import java.util.List;

import com.jasondavidpeters.thevillage1_5.entity.Entity;

public class Location {
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	public static Location mine = new Mine("Mines");
	public static Location lobby = new Lobby("Lobby");
	public static Location shop = new Shop("Shop");
	
	protected String name;
	
	public Location() {
		
	}
	
	public Location(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
}
