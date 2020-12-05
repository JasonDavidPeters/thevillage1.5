package com.jasondavidpeters.thevillage1_5.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jasondavidpeters.thevillage1_5.GameWindow;

public class ChatBox implements KeyListener {

	private int x, y, width, height;

	BufferedImage img;

	private static Font chatFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
	private StringBuilder sb = new StringBuilder();
	private int stringWidth;
	public boolean pressedEnter;
	private boolean canType;

	public ChatBox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		try {
			img = ImageIO.read(ChatBox.class.getResource("/gfx/chatbox.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		g.drawImage(img, x, y, width, height, null);
		g.setColor(Color.BLACK);
		g.setFont(chatFont);
		g.drawString(sb.toString(), x, y + 50);
		stringWidth = g.getFontMetrics().stringWidth(sb.toString());

	}

	public void tick() {

	}

	public void keyTyped(KeyEvent e) {
		if (canType) {
			if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
				if (sb.length() > 0) { // string must be at least 1 character long to delete a character with backspace
					sb.deleteCharAt(sb.length() - 1); // if you press backspace then delete character at the end of the string
				} else {
					return;
				}
			} else if (stringWidth >= GameWindow.WIDTH - chatFont.getSize()) {
				sb.deleteCharAt(sb.lastIndexOf(sb.toString())); // if the string is at the end of the window then delete the last character
			} else if (!pressedEnter && e.getKeyChar() == KeyEvent.VK_ENTER && (sb.toString().trim()).length() > 0) { // if you press enter + check for empty string
			while ((sb.toString().trim()).length() > 0) // huh
					pressedEnter = true;
			pressedEnter=false;
			} else {
				sb.append(e.getKeyChar());
			}
		} else {
			
		}

	}
	
	public void enableKeyboard() {
		setCanType(true);
	}
	public void disableKeyboard() {
		setCanType(false);
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public String getMessage() {
		return sb.toString();
	}

	public void setMessage(String message) {
		sb.replace(0, sb.length(), message);
	}
	public void setCanType(boolean canType) {
		this.canType=canType;
	}

}
