package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.jasondavidpeters.thevillage1_5.Game;
import com.jasondavidpeters.thevillage1_5.entity.Player;
import com.jasondavidpeters.thevillage1_5.io.Keyboard;

public class ChatBox extends Component {
	BufferedImage img;

	private static Font chatFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
	private StringBuilder sb = new StringBuilder();
	private int stringWidth;
	public boolean pressedEnter;
	private boolean canType;
	private int timer;
	private boolean appended;
	public boolean submitted;
	private Player p;
	private MessageBox m;

	public ChatBox(int x, int y, int width, int height) {
		super(x + 5, y, width, height);
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(chatFont);
		g.drawString(sb.toString(), x, y + 50);
		stringWidth = g.getFontMetrics().stringWidth(sb.toString());

	}

	public void tick() {
		timer++;
//		if (timer %4 ==0)
	}

	public void chat(Player player) {
		p = player;
		if (canType) {
			if (Keyboard.key == KeyEvent.VK_BACK_SPACE) {
				if (sb.length() > 0) { // string must be at least 1 character long to delete a character with backspace
					sb.deleteCharAt(sb.length() - 1); // if you press backspace then delete character at the end of the
														// string
					System.out.println(sb.length());
					appended = false;
					Keyboard.resetKey();
				}
			} else if (stringWidth >= 500) {
				sb.deleteCharAt(sb.lastIndexOf(sb.toString())); // if the string is at the end of the window then delete
																// the last character
			} else if (!pressedEnter && Keyboard.key == KeyEvent.VK_ENTER && (sb.toString().trim()).length() > 0) { // string
				
				if ((sb.toString().trim()).length() > 0)
					// update messagebox first
					for (Component c: Game.uimanager.getComponents())
						if (c instanceof MessageBox) m = (MessageBox) c;
					m.write(getMessage(),false);
					player.setMessageSubmitted(true);
					setMessage("");

			} else if (!appended && Keyboard.key != '\0') {
//				System.out.println("appending: " + Keyboard.key);
				sb.append(Keyboard.key);
				appended = true;
			} else {
				Keyboard.resetKey();
				appended = false;
			}
		}

	}

	public void enableKeyboard() {
		setCanType(true);
	}

	public void disableKeyboard() {
		setCanType(false);
	}

	public String getMessage() {
		return sb.toString();
	}
	public Player getPlayer() {
		return p;
	}

	public void setMessage(String message) {
		sb.replace(0, sb.length(), message);
	}

	public void setCanType(boolean canType) {
		this.canType = canType;
	}

}
