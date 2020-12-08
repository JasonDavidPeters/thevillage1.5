package com.jasondavidpeters.thevillage1_5.entity;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.jasondavidpeters.thevillage1_5.Game;
import com.jasondavidpeters.thevillage1_5.Game.GameState;
import com.jasondavidpeters.thevillage1_5.io.PlayerSave;
import com.jasondavidpeters.thevillage1_5.ui.ChatBox;
import com.jasondavidpeters.thevillage1_5.ui.Component;
import com.jasondavidpeters.thevillage1_5.ui.InformationBox;
import com.jasondavidpeters.thevillage1_5.ui.MessageBox;
import com.jasondavidpeters.thevillage1_5.world.Mine;

public class Player extends Entity {
	private boolean messageSubmitted;

	/*
	 * Player should be created upon game creation Individual player should be in
	 * charge of reading and writing messages to the messagebox
	 * 
	 */
	private transient MessageBox m;
	private transient ChatBox c;
	private transient InformationBox info;
	private transient Random random = new Random();

	private boolean canType;
	private boolean preload;
	private boolean digging;
	private boolean transition;
	public int loginPhase;
	private int time;
	private int diggingCount;
	private boolean clearScreen;
	private boolean inventoryFullNotif;

	private String username;
	private String password;
	private final int MAX_INVENTORY_SLOTS = 15;
	private boolean playerExists;
	private int freeInventorySlots;
	private boolean displayingInventory;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();

	public Entity[] inventory = new Entity[MAX_INVENTORY_SLOTS];

	public Player(String username) {
		super();
		this.username = username;
	}

	public Player(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public void render(Graphics g) {
	}

	public void tick() {
		if (!preload) {
			for (int i = 0; i < Game.uimanager.getComponents().size(); i++) {
				Component comp = Game.uimanager.getComponents().get(i);
				if (comp instanceof MessageBox)
					m = (MessageBox) comp;
				if (comp instanceof InformationBox)
					info = (InformationBox) comp;
				if (comp instanceof ChatBox)
					c = (ChatBox) comp;
			}
			preload = true;
		}
		time++;
		if (time == Integer.MAX_VALUE - 1)
			time = 0;
		if (Game.getGameState() == Game.GameState.WELCOME)
			canType = false;

		if (canType()) {
			c.enableKeyboard();
		} else {
			c.disableKeyboard();
		}
		c.chat(this);

		if (isDigging() && getFreeInventorySlots() > 0) {
			List<Ore> potentialOres = ((Mine) getLocation()).getAvailableOres();
			Ore randomOre = null;
			int bestChance = 0;
			int sum = 0;
			for (Ore o : potentialOres) {
				int chance = o.getChance();
				if ((sum = (random.nextInt(chance)) + 1) > bestChance) {
//					System.out.println(sum + " " + bestChance);
//					System.out.println(o.getName() + " " + sum + " " + chance + " " + bestChance);
					bestChance = sum;
					randomOre = o;

				}
			}
			if (time % (random.nextInt(randomOre.getMaxHarvestTime()) + randomOre.getMinHarvestTime()) == 0) {
				int quantity = random.nextInt((randomOre.getMaxQuantity())) + 1;
				m.write("You have found " + quantity + "x " + randomOre.getName(), true);
				qty: for (int j = 0; j < quantity; j++) {
					for (int i = 0; i < inventory.length; i++) {
						if (inventory[i] == null) {
							/*
							 * TODO: each inventory slot holds 1 item so for each quantity of ore, take up
							 * that many slots
							 */
							inventory[i] = randomOre;
							continue qty;
						}
					}
				}
				if (!inventoryFullNotif)
					inventoryFullNotif = true;

			}
		} else if (inventoryFullNotif && getFreeInventorySlots() <= 0) {
			setDigging(false);
			m.write("Your inventory is full", false);
			inventoryFullNotif = false;
			Game.setGameState(GameState.MINE);
			m.clear();
		}

		if (time % (60 * 3) == 0) { // For now every 3 minutes (60 ticks a second) update attributes
			addAttribute("inventory", inventory);
		}
//		messageSubmitted = c.submitted;
		/*
		 * if the user has submitted a message then we will set messageSubmitted to true
		 * UNTIL it has been set to false
		 */
	}

	public void addAttribute(String k, Object v) {
		// TODO: check for key and update value if exists
		attributes.put(k, v);
	}

	public void save() {
		PlayerSave saveFile = new PlayerSave(getName(), getPassword());
		// key, v
		saveFile.setAttributes(attributes);
		saveFile.save(); // send attributes
	}

	public void load() {
		PlayerSave loadFile = new PlayerSave(getName(), getPassword());
		loadFile = loadFile.load(this);
		if (playerExists) {
			setPassword(loadFile.getPassword());
			attributes = loadFile.getAttributes();
			/*
			 * UNLOAD ATTRIBUTES HERE
			 */
			inventory = (Entity[]) attributes.get("inventory");
			System.out.println("Loaded file for: " + loadFile.getName());
		}
	}

	public String getName() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public boolean canType() {
		return canType;
	}

	public void setCanType(boolean canType) {
		this.canType = canType;
	}

	public boolean messageSubmitted() {
		return messageSubmitted;
	}

	public void setMessageSubmitted(boolean submitted) {
//		System.out.println("called 1");
		this.messageSubmitted = submitted;
	}

	public String getUsername() {
		return username;
	}

	public boolean getTransition() {
		return transition;
	}

	public void setTransition(boolean transition) {
		this.transition = transition;
	}

	public boolean isDigging() {
		return digging;
	}

	public void setDigging(boolean digging) {
		this.digging = digging;
	}

	public boolean getPlayerExists() {
		return playerExists;
	}

	public void setPlayerExists(boolean playerExists) {
		this.playerExists = playerExists;
	}

	public void setPassword(String password) {
		this.password = password;

	}

	public int getFreeInventorySlots() {
		int count = 0;
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null)
				count++;
		}
		return freeInventorySlots = count;
		/*
		 * inventory[0] = ore inventory[1] = ore inventory[2] = null inventory[3] = ore;
		 */
	}

	public String getPassword() {
		return password;
	}

	public void clearInventory() {
		for (int i = 0; i < inventory.length; i++) {
			inventory[i] = null;
		}
		m.write("You clear your inventory.", false);

	}

	public void displayInventory() {
		if (Game.getGameState() == GameState.INVENTORY && getFreeInventorySlots() == inventory.length) {
			for (Component c: Game.uimanager.getComponents())
				if (c instanceof InformationBox) ((InformationBox) c).clear();
			m.write("Your inventory is empty", false);
			return;
		}
		HashMap<Entity, Integer> inv = new HashMap<Entity, Integer>();
		for (int i = 0; i < inventory.length; i++) {
			Entity e = inventory[i]; // coal
			if (inv.containsKey(e)) {
				inv.replace(e, inv.get(e) + 1);
			} else {
				inv.put(e, 1);
			}
		}

		// loop through hashmap inv and display key x value
		if (!displayingInventory) {
			for (Entity e : inv.keySet()) {
				m.write(e.getName() + " " + inv.get(e), false);
			}
			displayingInventory = true;
		}
		/*
		 * Get count of unique entities quantity of each entity
		 * 
		 */

	}

	public void sellAll() {
		if (Game.getGameState() == GameState.SHOP
				&&getFreeInventorySlots() == inventory.length) {
			m.write("Your inventory is empty", false);
			info.clear();
			Game.setGameState(GameState.LOBBY);
			return;
		}
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] instanceof Ore) {
				Ore o = (Ore) inventory[i];
				m.write("You have sold, " + o.getName() + " for: " + o.getCost(), false);
				inventory[i] = null;
			} else {
				m.write("You have nothing to sell", false);
			}
		}
	}

	public boolean isDisplayingInventory() {
		return displayingInventory;
	}
}
