package com.jasondavidpeters.thevillage1_5;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.jasondavidpeters.thevillage1_5.entity.Player;
import com.jasondavidpeters.thevillage1_5.ui.ChatBox;
import com.jasondavidpeters.thevillage1_5.ui.InformationBox;
import com.jasondavidpeters.thevillage1_5.ui.MessageBox;
import com.jasondavidpeters.thevillage1_5.ui.UIManager;

public class Game implements Runnable {

	private boolean running;
	private Thread thread;

	private GameWindow gw;
	private ChatBox chatbox;
	private MessageBox messagebox;
	private InformationBox informationBox;
	private Player player;

	private int time;
	
	public static UIManager uimanager = new UIManager();

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
		if (time == Integer.MAX_VALUE-1) time=0;
		// messagebox.write(chatbox.getMessage());
		if (player.canType()) {
			chatbox.enableKeyboard();
		} else {
			chatbox.disableKeyboard();
		}
		uimanager.tick();
		player.tick();
		
		if (Game.getGameState() == Game.GameState.WELCOME) {
			messagebox.write("Welcome to The Village 1.5!");
			if (time % (180) == 0) {
				messagebox.write("What is your name?");
				Game.setGameState(GameState.LOGIN);
			}
		}
	}

	public void render() {
		BufferStrategy bs = gw.getBufferStrategy();
		if (bs == null) {
			gw.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		uimanager.render(g);

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
		messagebox = new MessageBox(0, 0, (GameWindow.WIDTH / 4) * 3, (GameWindow.HEIGHT / 3) * 2);
		chatbox = new ChatBox(0, (GameWindow.HEIGHT / 3) * 2, GameWindow.WIDTH,
				GameWindow.HEIGHT - ((GameWindow.HEIGHT / 3) * 2));
		informationBox = new InformationBox(messagebox.getWidth(), 0, GameWindow.WIDTH-messagebox.getWidth(),GameWindow.HEIGHT - chatbox.getHeight());
		gw.addKeyListener(chatbox);
		uimanager.add(messagebox);
		uimanager.add(informationBox);
		uimanager.add(chatbox);
		player = new Player();
		player.init(messagebox, chatbox);
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
