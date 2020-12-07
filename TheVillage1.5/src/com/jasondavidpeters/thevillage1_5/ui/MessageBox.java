package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class MessageBox extends Component {

	private static Font messageFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);

	private ArrayList<String> messages = new ArrayList<String>();

	private int messageSpacing = messageFont.getSize();

	private int scrollBarY, scrollbarX;
	private int scrollBarHeight;
	private int scrollbarWidth = 10;
	private int sizeInc;
	public int maxMessagesPerScreen = 20;
	public int minHeight = 50;
	public int startingPoint;
	private int messageHeight = 1;
	private boolean clear;

	public MessageBox(int x, int y, int width, int height) {
		super(x,y,width,height);
		scrollBarHeight = height;
		scrollBarY = y;
	}

	public void clear() {
		setClear(true);
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.setFont(messageFont);
		for (int i = startingPoint; i < messages.size(); i++) {
			if (startingPoint > 0) {
				g.drawString(messages.get(i).toString(), x, (messageHeight) * messageSpacing);
				messageHeight++;
			} else {
				g.drawString(messages.get(i).toString(), x, (i + 1) * messageSpacing);// calculation needs to change
			}
		}
		messageHeight = 1;
		// from startpoint to maxmessage limit, display messages
		if (messages.size() >= maxMessagesPerScreen) {
			addScrollbar(g);

		}
		if (clear) { // clear the screen
			g.clearRect(x, y, width, height);
			messages.clear();
			// potentially have to reset scrollbar and other variables here
			setClear(false);
		}
		// System.out.println("starting point: " + startingPoint);
		// System.out.println(scrollBarHeight);
	}

	public void tick() {
	}

	public void addScrollbar(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(width - scrollbarWidth, scrollBarY, scrollbarWidth, scrollBarHeight);
	}

	public void write(String message, boolean repeatable) {
		if (message.equalsIgnoreCase(""))
			return;
		if (!repeatable)
		if (messages.contains(message)) return;
		messages.add(message.trim());
	}

	public void setSizeInc(int c) {
		this.sizeInc = c;
	}

	public int getSizeInc() {
		return this.sizeInc;
	}

	public int getScrollbarHeight() {
		return this.scrollBarHeight;
	}

	public void setScrollbarHeight(int i) {
		this.scrollBarHeight = i;

	}

	public int getScrollbarY() {
		return this.scrollBarY;
	}

	public ArrayList<String> getMessages() {
		return this.messages;
	}

	public void setScrollbarY(int y) {
		this.scrollBarY = y;

	}

	public int getStartingPoint() {
		return this.startingPoint;
	}

	public void setStartingPoint(int i) {
		this.startingPoint = i;

	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public boolean getClear() {
		return this.clear;
	}

}
