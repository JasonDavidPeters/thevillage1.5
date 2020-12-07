package com.jasondavidpeters.thevillage1_5.io;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {

	public static double mouseX, mouseY = -1;
	public static int mouseB = -1;
	public static boolean pressed;

	public void mousePressed(MouseEvent e) {
		if (!pressed) pressed = true;
		if (pressed) {
			mouseB = e.getButton();
			mouseX = e.getX();
			mouseY = e.getY();
		} else if (!pressed) {
			mouseB = -1;
			mouseX = -1;
			mouseY = -1;			
		}
	}

	public void mouseReleased(MouseEvent e) {
		pressed = false;
		mouseB = -1;
		mouseX = -1;
		mouseY = -1;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
