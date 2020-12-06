package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Graphics;

import com.jasondavidpeters.thevillage1_5.input.Mouse;

public class Label extends Component {

	private String text;
	private double textWidth;
	private boolean checkBounds;

	/*
	 * what are the dimensions of the label?
	 */

	public Label(String text, int x, int y, int width, int height) {
		super(x, y, 0, 0);
		this.text = text;
	}

	public Label(String text) {
		super(0, 0, 0, 0);
		this.text = text;
	}

	public void render(Graphics g) {
//		g.setColor(Color.red);
		g.drawRect(x, y - 15, g.getFontMetrics().stringWidth(text), 15);
		if (checkBounds)
			textWidth = g.getFontMetrics().stringWidth(text);
	}

	public void tick() {
		if (Mouse.mouseB == 1)
			checkBounds = true;
		else
			checkBounds = false;

		if (Mouse.mouseB == 1)
//			System.out.println(Mouse.mouseB);
			if (Mouse.mouseX >= x && Mouse.mouseX <= x + textWidth && Mouse.mouseY >= y - 15 && Mouse.mouseY <= y) {
				System.out.println(Mouse.mouseB + " " + Mouse.mouseX);
//				System.out.println(getText());
			}

	}

	public String getText() {
		return text;
	}
}
