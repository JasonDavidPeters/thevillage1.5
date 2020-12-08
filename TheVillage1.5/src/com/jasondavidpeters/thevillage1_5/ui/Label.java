package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Graphics;

import com.jasondavidpeters.thevillage1_5.Game;
import com.jasondavidpeters.thevillage1_5.io.Mouse;

public class Label extends Component {

	private String text;
	private double textWidth;
	private boolean checkBounds;
	private boolean pressed;
	private MessageBox messagebox;
	private ChatBox chatbox;
	private boolean doOnce;
	private boolean visible;

	public Label(String text, int x, int y) {
		super(x, y, 0, 0);
		this.text = text;
		visible = true;
	}

	public Label(String text) {
		super(0, 0, 0, 0);
		this.text = text;
		visible = true;
	}

	public void render(Graphics g) {
//		g.setColor(Color.red);
		g.drawRect(x, y - 15, g.getFontMetrics().stringWidth(text), 15);
		if (checkBounds)
			textWidth = g.getFontMetrics().stringWidth(text);
	}

	public void tick() {
		if (!doOnce) {
			for (Component c : Game.uimanager.getComponents()) {
				if (c instanceof MessageBox)
					messagebox = (MessageBox) c;
				if (c instanceof ChatBox)
					chatbox = (ChatBox) c;
			}
			doOnce = true;
		}
		if (Mouse.mouseB == 1)
			checkBounds = true;
		else
			checkBounds = false;
		if (!pressed) {
			if (Mouse.mouseB == 1) {
				if (Mouse.mouseX >= x && Mouse.mouseX <= x + textWidth && Mouse.mouseY >= y - 15 && Mouse.mouseY <= y) {
					pressed = true;
					// TODO when the player clicks on an option, send a message to the chat
					messagebox.write(getText(), false);
					chatbox.getPlayer().setMessageSubmitted(true);
				}
			}
		} else if (Mouse.mouseB != 1)
			pressed = false;

	}
	
	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getText() {
		return text;
	}
}
