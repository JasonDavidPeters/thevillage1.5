package com.jasondavidpeters.thevillage1_5.entity;

import java.awt.Graphics;

import com.jasondavidpeters.thevillage1_5.Game;
import com.jasondavidpeters.thevillage1_5.Game.GameState;
import com.jasondavidpeters.thevillage1_5.ui.ChatBox;
import com.jasondavidpeters.thevillage1_5.ui.InformationBox;
import com.jasondavidpeters.thevillage1_5.ui.MessageBox;

public class Player {
	
	private MessageBox m;
	private ChatBox c;
	private InformationBox info;
	
	/*
	 * Player should be created upon game creation
	 * Individual player should be in charge of reading
	 * and writing messages to the messagebox
	 * 
	 */
	
	private String name;
	private String password;
	private boolean canType;
	private int time;
	
	public Player() {
	
	}
	public Player(String name) {
		super();
		this.name=name;
	}
	public Player(String name, String password) {
		super();
		this.name=name;
		this.password=password;
	}
	
	public void render(Graphics g) {
		
	}
	public void tick() {
		time++;
		if (time == Integer.MAX_VALUE-1)time=0;
		if (Game.getGameState() == Game.GameState.WELCOME) canType=false;
		
		if (canType()) {
			c.enableKeyboard();
		} else {
			c.disableKeyboard();
		}
	}
	
	public void init(MessageBox m, ChatBox c, InformationBox info) {
		this.m=m;
		this.c=c;
		this.info=info;
	}
	
	public String getName() {
		return name;
	}
	public void setUsername(String name) {
		this.name=name;
	}
	public boolean canType() {
		return canType;
	}
	public void setCanType(boolean canType) {
		this.canType=canType;
	}

}
