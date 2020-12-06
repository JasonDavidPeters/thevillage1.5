package com.jasondavidpeters.thevillage1_5.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {

	public static double mouseX, mouseY = -1;
	public static int mouseB = -1;
	public boolean pressed;

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (!pressed) {
			mouseB = e.getButton();
			mouseX = e.getX();
			mouseY = e.getY();
			pressed = true;
		} else {
			mouseB = -1;
			mouseX = -1;
			mouseY = -1;
			pressed=false;
		}
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = -1;
		mouseX = -1;
		mouseY = -1;
		pressed=false;
	}
	

}
