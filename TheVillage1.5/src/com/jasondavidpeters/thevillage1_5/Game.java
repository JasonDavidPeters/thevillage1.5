package com.jasondavidpeters.thevillage1_5;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jasondavidpeters.thevillage1_5.entity.Ore;
import com.jasondavidpeters.thevillage1_5.entity.Player;
import com.jasondavidpeters.thevillage1_5.ui.ChatBox;
import com.jasondavidpeters.thevillage1_5.ui.InformationBox;
import com.jasondavidpeters.thevillage1_5.ui.Label;
import com.jasondavidpeters.thevillage1_5.ui.MessageBox;
import com.jasondavidpeters.thevillage1_5.ui.UIManager;
import com.jasondavidpeters.thevillage1_5.world.Location;

public class Game implements Runnable {

	private boolean running;
	private Thread thread;

	private GameWindow gw;
	private ChatBox chatbox;
	private MessageBox messagebox;
	private InformationBox informationBox;
	private Player player;
	private Ore ore;
	private Location location;

	private static boolean preloadPhase;

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
		player = new Player(null);
		ore = new Ore();
		location = new Location();
		long timer = System.currentTimeMillis();
		long before = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int ticks = 0;

		if (Game.getGameState() == Game.GameState.WELCOME) {
			messagebox.write("Welcome to The Village 1.5!", false);
			if (time % (180) == 0) {
				messagebox.write("What is your name?", false);
				Game.setGameState(GameState.LOGIN);
			}
		}
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
				 * 
				 * if game sends write message then render that message
				 */
				String username = messagebox.getMessages().get(messagebox.getMessages().size() - 1).getText();
				player.setMessageSubmitted(false);
				player.setUsername(username);
				player.load();
				if (!player.getPlayerExists()) {
					player.loginPhase = 1;
				} else {
					player.loginPhase = 4;
				}
				player.setCanType(false);
			} else if (player.loginPhase == 1) {
				messagebox.write("Enter a password:", false);
				player.loginPhase = 2;
			}
			if (player.messageSubmitted() && player.loginPhase == 2) {
				String password = messagebox.getMessages().get(messagebox.getMessages().size() - 1).getText();
				player.setPassword(password);
				player.save();
				Game.setGameState(GameState.LOBBY);
				player.setLocation(Location.lobby);
				player.setMessageSubmitted(false);

			}
			if (player.loginPhase == 4) {
				messagebox.write("Enter a password:", false);
				player.loginPhase = 5;
			}
			if (player.messageSubmitted() && player.loginPhase == 5) {
				String password = messagebox.getMessages().get(messagebox.getMessages().size() - 1).getText();
				if (player.getPassword().equals(password)) {
					player.setLocation(Location.lobby);
					Game.setGameState(GameState.LOBBY);
					messagebox.clear();
				} else {
					player.loginPhase = 4;
				}
				player.setMessageSubmitted(false);
			}
			break;
		case LOBBY:
			if (preloadPhase) {
				messagebox.write("Welcome to " + player.getLocation().getName(), false);
				messagebox.write("On your right-hand side you will", false);
				messagebox.write("see a list of keywords to choose from", false);
				if (time % 20 == 0) {
					messagebox.write("Either click an option,", false);
					messagebox.write("or type the keyword", false);
				}
				informationBox.write("Mine");
				informationBox.write("Shop");
				informationBox.write("Inventory");
				preloadPhase = false;
			}
			player.setCanType(true);
			if (player.messageSubmitted()) { // the user has entered a new message into the messagebox
				messageLoop: for (Label l : messagebox.getMessages()) { // Check messages for keywords
					String m = l.getText().trim();
					if (m.equalsIgnoreCase("mine")) {
						System.out.println(true);
						messagebox.write("We are off to the mines!", false);
						player.setTransition(true);
						break messageLoop;
					} else if (m.equalsIgnoreCase("shop")) {
						messagebox.write("We are off to shops!", false);
						player.setLocation(Location.shop);
						setGameState(GameState.SHOP);
						informationBox.clear();
						player.setMessageSubmitted(false);
						break messageLoop;
					} else if (m.equalsIgnoreCase("inventory")) {
						setGameState(GameState.INVENTORY);
						informationBox.clear();
						player.setMessageSubmitted(false);
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
					setGameState(GameState.MINE);
					player.setLocation(Location.mine);
				}
			break;
		case MINE:
			if (preloadPhase) {
				if (time % 120 == 0) {
					messagebox.write("Welcome to the " + player.getLocation().getName(), false);
					if (time % 60 == 0) {
						messagebox.write("On your right-hand side you will", false);
						messagebox.write("see a list of keywords to choose from", false);
						if (time % 30== 0) {
							messagebox.write("Either click an option,", false);
							messagebox.write("or type the keyword", false);
							informationBox.write("Dig");
							informationBox.write("Inventory");
							informationBox.write("Exit");
						}
					}
					preloadPhase = false;
				}
			}
			if (!player.isDigging()) {
				player.setCanType(true);
				if (player.messageSubmitted()) {
//					System.out.println("message submitted");
					messageLoop: for (Label l : messagebox.getMessages()) { // Check messages for keywords
						String m = l.getText().trim();
						if (m.equalsIgnoreCase("dig")) {
							player.setDigging(true);
							player.setCanType(false);
							messagebox.clear();
							break messageLoop;
						} else if (m.equalsIgnoreCase("clear")) {
							player.clearInventory();
							messagebox.clear();
							break messageLoop;
						} else if (m.equalsIgnoreCase("inventory")) {
							setGameState(GameState.INVENTORY);
							messagebox.clear();
							break messageLoop;
						} else if (m.equalsIgnoreCase("exit")) {
							messagebox.clear();
							setGameState(GameState.LOBBY);
							player.setLocation(Location.lobby);
							informationBox.clear();
							break messageLoop;
						}
					}
				}
			}
			player.setMessageSubmitted(false);
			break;
		case INVENTORY:
			informationBox.write("Exit");
			player.displayInventory();
			if (player.messageSubmitted()) {
				messageLoop: for (Label l : messagebox.getMessages()) { // Check messages for keywords
					String m = l.getText();
					if (m.equalsIgnoreCase("exit")) {
						messagebox.clear();
						setGameState(GameState.MINE);
						player.setLocation(Location.mine);
						break messageLoop;
					}
				}
				player.setMessageSubmitted(false);
			}
			break;
		case SHOP:
			if (preloadPhase) {
				player.setCanType(true);
				messagebox.write("Welcome to the " + player.getLocation().getName(), false);
				if (time % 30 == 0) {
					messagebox.write("On your right-hand side you will", false);
					messagebox.write("see a list of keywords to choose from", false);
				}
				if (time % 60 == 0) {
					messagebox.write("Either click an option,", false);
					messagebox.write("or type the keyword", false);
				}
				informationBox.write("Sell");
				informationBox.write("Inventory");
				informationBox.write("Exit");
			}
			if (player.messageSubmitted()) {
				messageLoop: for (Label l : messagebox.getMessages()) { // Check messages for keywords
					String m = l.getText().trim();
					if (m.equalsIgnoreCase("inventory")) {
						setGameState(GameState.INVENTORY);
						messagebox.clear();
						break messageLoop;
					} else if (m.equalsIgnoreCase("exit")) {
						messagebox.clear();
						setGameState(GameState.LOBBY);
						player.setLocation(Location.lobby);
						informationBox.clear();
						break messageLoop;
					} else if (m.equalsIgnoreCase("sell")) {
//						messagebox.clear();
						player.sellAll();
						player.setMessageSubmitted(false);
						break messageLoop;
					}
				}
			}
			player.setMessageSubmitted(false);
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
		preloadPhase = true;
		currentState = c;
	}

}
