package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Graphics;

public class Component {
	
	protected int x, y, width, height;
	protected int time;
	
	
	public Component(int x, int y, int width, int height) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	public void tick() {
		
	}
	public void render(Graphics g) {
		
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

}
