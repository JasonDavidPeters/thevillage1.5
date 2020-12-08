package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import com.jasondavidpeters.thevillage1_5.io.Mouse;

public class MessageBox extends Component {

//	private static Font messageFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

	private ArrayList<Label> messages = new ArrayList<Label>();
	private ArrayList<Label> buffer = new ArrayList<Label>();

	private int scrollBarY, scrollbarX;
	private int scrollBarHeight;
	private int scrollbarWidth = 10;
	private int sizeInc;
	public int maxMessagesPerScreen = 20;
	public int minHeight = 50;
	public int startingPoint;
	private boolean clear;
	private boolean retrieveFontMetrics;
	private FontMetrics fontMetrics;
	private boolean append;
	private boolean scrollUp;
	private boolean scrollDown;

	public MessageBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		scrollBarHeight = height;
		scrollBarY = y;
	}

	public void clear() {
		setClear(true);
	}

	public void render(Graphics g) {
		if (!retrieveFontMetrics) {
//			g.setFont(messageFont);
			fontMetrics = g.getFontMetrics();
			retrieveFontMetrics = true;
		}
		g.setColor(Color.black);

		g.drawRect(0, 0, width, height);

		for (int i = 0; i < messages.size(); i++) {
			Label drawLabel = messages.get(i);
			if (drawLabel.getVisible())
				g.drawString(drawLabel.getText().trim(), drawLabel.getX(), drawLabel.getY());
		}

		if (messages.size() >= getMaxMessagesPerScreen()) {
			// for every message
//			buffer.add(messages.get(0));
//			messages.remove(0);
			// last message = messages.size();
			// each message has to take the y position of the message before it
//			messages.remove(0);
			if (append || scrollUp || scrollDown) {
				for (Label l : messages) {
					if (scrollUp) {
						l.y += 1;
						if (l.getY() > height) {
							l.setVisible(false);
							continue;
						} else {
							l.setVisible(true);
							continue;
						}
					} else if (scrollDown) {
						l.y -= 1;
						if (l.getY() > height) {
							l.setVisible(false);
							continue;
						} else {
							l.setVisible(true);
							continue;
						}
					} else if (append && !scrollUp && !scrollDown) {
						l.y -= 30;
					}
				}
				scrollUp = false;
				scrollDown = false;
				append = false;
			}
			addScrollbar(g);

		}
		if (clear) { // clear the screen
			g.clearRect(x, y, width, height);
			messages.clear();
			// potentially have to reset scrollbar and other variables here
			setClear(false);
		}
	}

	public void tick() {
	}

	public void addScrollbar(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(width - scrollbarWidth, scrollBarY + 10, scrollbarWidth, scrollBarHeight - 20);
		g.setColor(Color.DARK_GRAY);
		// Top button
		g.fillRect(width - scrollbarWidth, 0, scrollbarWidth, 10);
		if (Mouse.mouseB == 1) {
			if (Mouse.mouseX >= width - scrollbarWidth && Mouse.mouseX <= (width - scrollbarWidth) + scrollbarWidth && Mouse.mouseY >= 0
					&& Mouse.mouseY <= 10) {
				// up mechanic
				scrollUp = true;
			}
		}
		// Bottom button
		g.fillRect(width - scrollbarWidth, height - 10, scrollbarWidth, 10);
		if (Mouse.mouseB == 1) {
			if (Mouse.mouseX >= width - scrollbarWidth && Mouse.mouseX <= (width - scrollbarWidth) + scrollbarWidth && Mouse.mouseY >= height - 10
					&& Mouse.mouseY <= height) {
				// down mechanic
				scrollDown = true;
			}
		}

		/*
		 * two buttons
		 */
	}

	public void write(String message, boolean repeatable) {
		if (message.equalsIgnoreCase(""))
			return;
		message.replaceAll("[\\n\\t ]", "");
		Label label = null;
		if (messages.size() > 0)
			label = new Label(message, x, y + messages.get(messages.size() - 1).getY() + 15);
		else
			label = new Label(message, x, y + 15);
		messages.add(label);

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

	public ArrayList<Label> getMessages() {
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

	public int getMaxMessagesPerScreen() {
		int fontHeight = fontMetrics.getHeight();
		int screenHeight = height;
		int maxMessages = screenHeight / fontHeight + 2;
		return maxMessages;
	}

	public void append(boolean append) {
		this.append = append;
	}

}
