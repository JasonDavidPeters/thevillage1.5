package com.jasondavidpeters.thevillage1_5;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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

	private BufferedImage gameBackground;

	private int time;

	public static UIManager uimanager = new UIManager();

	private static GameState currentState = GameState.WELCOME;

	public enum GameState {
		WELCOME, LOGIN, LOBBY, MINE, SHOP, INVENTORY;
	}

	public Game() {
		gw = new GameWindow();

		try {
			gameBackground = ImageIO.read(Game.class.getResource("/gfx/gamesprite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public void run() {
		messagebox = new MessageBox(0, 0, (GameWindow.WIDTH / 4) * 3, (GameWindow.HEIGHT / 3) * 2);
		chatbox = new ChatBox(0, (GameWindow.HEIGHT / 3) * 2, GameWindow.WIDTH, GameWindow.HEIGHT - ((GameWindow.HEIGHT / 3) * 2));
		informationBox = new InformationBox(messagebox.getWidth(), 0, GameWindow.WIDTH - messagebox.getWidth(),
				GameWindow.HEIGHT - chatbox.getHeight());
		uimanager.add(messagebox);
		uimanager.add(informationBox);
		uimanager.add(chatbox);
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
//			int messages = messagebox.getMessages().size();

			if (player.messageSubmitted() && player.loginPhase == 0) { // get the next message
				/*
				 * Login phase 0 == get username check if user exists
				 * 
				 * if user doesnt exist then login phase == 2 we get user to make a password,
				 * set player password and save
				 * 
				 * if user exists then login phase == 3 load player object and set player to
				 * that object
				 */
				String username = messagebox.getMessages().get(messagebox.getMessages().size() - 1);
				player.setMessageSubmitted(false);
				player.setUsername(username);
				if (player.load() == null) {
					player.loginPhase = 1;
//				messagebox.clear();
				} else {
					player = player.load();
					player.loginPhase=4;
//					Game.setGameState(GameState.LOBBY);
				}
				player.setCanType(false);
				// TODO check if player exists, if they do then perhaps prompt for password
			} else if (player.loginPhase == 1) {
				messagebox.write("Enter a password:", false);
				player.loginPhase = 2;
			}
			if (player.messageSubmitted() && player.loginPhase == 2) {
				String password = messagebox.getMessages().get(messagebox.getMessages().size() - 1);
				player.setPassword(password);
//				System.out.println("player saving");
				player.save();
				Game.setGameState(GameState.LOBBY);
				player.setMessageSubmitted(false);

			}
			if (player.loginPhase==4) {
				messagebox.write("Enter a password:", false);
				player.loginPhase=5;
			}
			if (player.messageSubmitted() && player.loginPhase == 5) {
				String password = messagebox.getMessages().get(messagebox.getMessages().size() - 1);
				if (player.getPassword().equals(password)) {
					Game.setGameState(GameState.LOBBY);
					messagebox.clear();
				} else {
					player.loginPhase=4;
				}
				player.setMessageSubmitted(false);
			}
//			if (player.getUsername() != null && !player.playerExists()) {
//				if (time % 60 == 0)
//					messagebox.write("Welcome, " + player.getUsername() + ", to the Village 1.5!", false);

//				if (time % 180 == 0) {
//					messagebox.clear();
//					Game.setGameState(GameState.LOBBY);
//				}
			break;
		case LOBBY:
//				messagebox.write("Hello, " + getName() + " welcome to the Village");
			messagebox.write("On your right-hand side you will", false);
			messagebox.write("see a list of keywords to choose from", false);
			if (time % 20 == 0) {
				messagebox.write("Either click an option,", false);
				messagebox.write("or type the keyword", false);
			}
			informationBox.write("Mine");
			informationBox.write("Shop");
			informationBox.write("Inventory");
			player.setCanType(true);
			if (player.messageSubmitted()) { // the user has entered a new message into the messagebox
				messageLoop: for (String m : messagebox.getMessages()) { // Check messages for keywords
					if (m.equalsIgnoreCase("mine")) {
						messagebox.write("We are off to the mines!", false);
						player.setTransition(true);
						break messageLoop;
					}
				}
			}
			player.setMessageSubmitted(false);
			if (player.getTransition())
				if (time % 60 == 0) {
					messagebox.clear();
					player.setTransition(false);
					informationBox.clear();
					currentState = GameState.MINE;
				}
			/*
			 * we dont want the user to type until prompted. Welcome to the Village You have
			 * a list of options on the right-hand side of your screen Type one of the
			 * keywords to find out more | MINE | SHOP | INVENTORY | EXIT
			 */
			break;
		case MINE:
			if (!player.isDigging()) {
				messagebox.write("Welcome to the mines", false);
				if (time % 30 == 0) {
					messagebox.write("On your right-hand side you will", false);
					messagebox.write("see a list of keywords to choose from", false);
				}
				if (time % 60 == 0) {
					messagebox.write("Either click an option,", false);
					messagebox.write("or type the keyword", false);
				}
			}
			informationBox.write("Dig");
			informationBox.write("Exit");

			if (player.messageSubmitted()) {
				messageLoop: for (String m : messagebox.getMessages()) {
					if (m.equalsIgnoreCase("dig")) {
						player.setDigging(true);
						player.setCanType(false);
						messagebox.clear();
						break messageLoop;
					}
				}
			}
			player.setMessageSubmitted(false);
			break;
		default:
			break;
		}

		if (Game.getGameState() == Game.GameState.WELCOME) {
			messagebox.write("Welcome to The Village 1.5!", false);
			if (time % (180) == 0) {
				messagebox.write("What is your name?", false);
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
		g.drawImage(gameBackground, 0, 0, gw.getWidth(), gw.getHeight(), null);
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
