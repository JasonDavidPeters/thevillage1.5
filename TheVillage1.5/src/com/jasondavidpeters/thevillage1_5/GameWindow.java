package com.jasondavidpeters.thevillage1_5;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.jasondavidpeters.thevillage1_5.input.Keyboard;
import com.jasondavidpeters.thevillage1_5.input.Mouse;

public class GameWindow extends Canvas {

	private static final long serialVersionUID = 6222230155513894185L;
	public final static int WIDTH = 680;
	public final static int HEIGHT = 540;
	public final static String GAME_TITLE = "The Village 1.5";
	private JFrame frame;
	
	private Mouse mouse;
	private Keyboard keyboard;
	
	public GameWindow() {
		mouse = new Mouse();
		keyboard = new Keyboard();
		frame = new JFrame(GAME_TITLE);
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.add(this);
		addKeyListener(keyboard);
		addMouseListener(mouse);
		requestFocus();
	}
	
	public JFrame getFrame() {
		return this.frame;
	}

}
