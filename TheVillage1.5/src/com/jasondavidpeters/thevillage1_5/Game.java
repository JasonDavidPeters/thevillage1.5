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
	public void run() {
		messagebox = new MessageBox(0, 0, (GameWindow.WIDTH / 4) * 3, (GameWindow.HEIGHT / 3) * 2);
		chatbox = new ChatBox(0, (GameWindow.HEIGHT / 3) * 2, GameWindow.WIDTH,
				GameWindow.HEIGHT - ((GameWindow.HEIGHT / 3) * 2));
		informationBox = new InformationBox(messagebox.getWidth(), 0, GameWindow.WIDTH - messagebox.getWidth(),
				GameWindow.HEIGHT - chatbox.getHeight());
		uimanager.add(messagebox);
		uimanager.add(informationBox);
		uimanager.add(chatbox);
		player = new Player();
		player.init(messagebox, chatbox, informationBox);
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
				tick();
				delta--;
				ticks++;
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

	public void tick() {
		time++;
		if (time == Integer.MAX_VALUE - 1)
			time = 0;
		// messagebox.write(chatbox.getMessage());
		uimanager.tick();
		player.tick();
		switch (Game.getGameState()) { // Check for the current game state
		case LOGIN:
			player.setCanType(true);
			if (messagebox.getMessages().size() > 2) { // get the next message
				String username = messagebox.getMessages().get(2);
				player.setUsername(username);
				if (time % 30 == 0)
					messagebox.write("Welcome, " + username + ", to the Village 1.5!");
				player.setCanType(false);
				if (time % 180 == 0) {
					messagebox.clear();
					Game.setGameState(GameState.LOBBY);
				}
			}
			break;
		case LOBBY:
//				messagebox.write("Hello, " + getName() + " welcome to the Village");
			messagebox.write("On your right-hand side you will");
			messagebox.write("see a list of keywords to choose from");
			player.setCanType(true);
			
			informationBox.write("Mine");
			informationBox.write("Shop");
			informationBox.write("Inventory");
			/*
			 * we dont want the user to type until prompted. Welcome to the Village You have
			 * a list of options on the right-hand side of your screen Type one of the
			 * keywords to find out more | MINE | SHOP | INVENTORY | EXIT
			 */
			break;
		default:
			break;
		}

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
		bs.show();
		g.dispose();
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
