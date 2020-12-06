package com.jasondavidpeters.thevillage1_5.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	public static char key;
	public static boolean typing;

	public void keyPressed(KeyEvent e) {
		if (!typing) {
			key = e.getKeyChar();
			typing = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if (typing) {
			typing = false;
//			key = '\0';
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public static void resetKey() {
		Keyboard.key = '\0';
	}
}
