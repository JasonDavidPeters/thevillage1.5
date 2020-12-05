package com.jasondavidpeters.thevillage1_5;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.jasondavidpeters.thevillage1_5.entity.Player;
import com.jasondavidpeters.thevillage1_5.graphics.ChatBox;
import com.jasondavidpeters.thevillage1_5.graphics.MessageBox;

public class Game implements Runnable {

	private boolean running;
	private Thread thread;

	private GameWindow gw;
	private ChatBox chatbox;
	private MessageBox messagebox;
	private Player player;

	private int time;

	private long timer = System.currentTimeMillis();
	private boolean doOnce;

	private static GameState currentState = GameState.WELCOME;

	public enum GameState {
		WELCOME, LOGIN, LOBBY, MINE, SHOP;
	}

	public Game() {
		gw = new GameWindow();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public void tick() {
		time++;
		// messagebox.write(chatbox.getMessage());
		if (player.canType()) {
			chatbox.enableKeyboard();
		} else {
			chatbox.disableKeyboard();
		}
		chatbox.tick();
		messagebox.tick();

		switch (currentState) { // Check for the current game state
		case LOGIN:
			player.setCanType(true);
			if (messagebox.getMessages().size() > 2) { // get the next message
				String username = messagebox.getMessages().get(2);
				player = new Player(username);
				if (time %30 ==0)
				messagebox.write("Welcome, " + username + ", to the Village 1.5!");
				player.setCanType(false);
				if (time % 180 == 0) {
					messagebox.clear();
					setGameState(GameState.LOBBY);
				}
			}
			break;
		case LOBBY:
			if (doOnce) {
				messagebox.write("Hello, " + player.getName() + " welcome to the Village");
				doOnce = false;
				timer = System.currentTimeMillis();
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				messagebox.write("On your right-hand side you will");
				messagebox.write("see a list of keywords to choose from");
				player.setCanType(true);
				timer = System.currentTimeMillis();
			}
			/*
			 * we dont want the user to type until prompted. Welcome to the Village You have
			 * a list of options on the right-hand side of your screen Type one of the
			 * keywords to find out more | MINE | SHOP | INVENTORY | EXIT
			 */
			break;
		default:
			break;
		}
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
		player = new Player();
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

	public static GameState getGameState() {
		return currentState;
	}

	public static void setGameState(GameState c) {
		currentState = c;
	}

}
