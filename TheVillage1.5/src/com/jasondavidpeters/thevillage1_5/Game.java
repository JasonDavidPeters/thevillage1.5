package com.jasondavidpeters.thevillage1_5;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.jasondavidpeters.thevillage1_5.graphics.ChatBox;
import com.jasondavidpeters.thevillage1_5.graphics.MessageBox;

public class Game implements Runnable {

	private boolean running;
	private Thread thread;

	private GameWindow gw;
	private ChatBox chatbox;
	private MessageBox messagebox;

	private long t = System.currentTimeMillis();
	private boolean doOnce;

	private GameState currentState = GameState.WELCOME;

	enum GameState {
		WELCOME, LOGIN;
	}

	public Game() {
		gw = new GameWindow();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public void tick() {
		// messagebox.write(chatbox.getMessage());
		chatbox.tick();
	}

	public void render() {
		BufferStrategy bs = gw.getBufferStrategy();
		if (bs == null) {
			gw.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		chatbox.render(g);
		messagebox.render(g);

		// use gamestates
		// if gamestate == login then ask user for input and wait

		switch (currentState) {
		case WELCOME:
			if (!doOnce) {
				messagebox.write("Welcome to The Village 1.5!");
				doOnce=true;
			}
			if (System.currentTimeMillis() - t >= 5000) {
				messagebox.write("What is your name?");
				t = System.currentTimeMillis();
				setGameState(GameState.LOGIN);
			}
			break;
		case LOGIN:
			break;
		}

		if (chatbox.pressedEnter) { // if user presses enter
			messagebox.write(chatbox.getMessage()); // submit message to messagebox
			if (messagebox.getMessages().size() >= messagebox.maxMessagesPerScreen) { // add scrollbar
				// System.out.println("scrollbar height: " + messagebox.getScrollbarHeight());
				messagebox.setSizeInc(messagebox.getSizeInc() + 1);
				messagebox.setScrollbarHeight(messagebox.getScrollbarHeight() - messagebox.getSizeInc());
				messagebox.setScrollbarY(messagebox.getScrollbarY() + messagebox.getSizeInc());
				messagebox.setStartingPoint(messagebox.getStartingPoint() + 1); // only increments to 25
			}
			chatbox.setMessage("");
		}
		bs.show();
		g.dispose();
	}

	public void run() {
		chatbox = new ChatBox(0, (GameWindow.HEIGHT / 3) * 2, GameWindow.WIDTH,
				GameWindow.HEIGHT - ((GameWindow.HEIGHT / 3) * 2));
		gw.addKeyListener(chatbox);
		messagebox = new MessageBox(0, 0, (GameWindow.WIDTH / 4) * 3, (GameWindow.HEIGHT / 3) * 2);
		long timer = System.currentTimeMillis();
		long before = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int ticks = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - before) / ns;
			before = now;
			while (delta >= 1) {
				delta--;
				ticks++;
				tick();
			}
			render();
			frames++;
			while (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("ticks: " + ticks + " frames: " + frames);
				timer = System.currentTimeMillis();
				frames = 0;
				ticks = 0;
			}
		}
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.run();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setGameState(GameState gs) {
		this.currentState = gs;
	}

	public GameState getGameState() {
		return currentState;
	}

}
