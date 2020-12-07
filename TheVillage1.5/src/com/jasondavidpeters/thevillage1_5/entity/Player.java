package com.jasondavidpeters.thevillage1_5.entity;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import com.jasondavidpeters.thevillage1_5.Game;
import com.jasondavidpeters.thevillage1_5.ui.ChatBox;
import com.jasondavidpeters.thevillage1_5.ui.Component;
import com.jasondavidpeters.thevillage1_5.ui.InformationBox;
import com.jasondavidpeters.thevillage1_5.ui.MessageBox;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

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

	private String username;
	private String password;
	private final int INVENTORY_SLOTS = 5;
	private boolean playerExists;

	public Player() {

	}

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

		if (isDigging() && diggingCount < INVENTORY_SLOTS) {
			if (time % (random.nextInt(120) + 30) == 0) {
				m.write(new String("You have found something."), true);
				diggingCount++;
			}
		} else if (diggingCount >= INVENTORY_SLOTS) {
			setDigging(false);
			diggingCount = 0;
			m.clear();
		}
//		messageSubmitted = c.submitted;
		/*
		 * if the user has submitted a message then we will set messageSubmitted to true
		 * UNTIL it has been set to false
		 */
	}

	public void save() {
		File file = null;
		ObjectOutputStream ostream = null;
		try {
			String path = System.getProperty("user.dir");
			file = new File(path + "\\res\\players\\" + getName() + ".txt");
			if (!file.exists())
				file.createNewFile();
			ostream = new ObjectOutputStream(new FileOutputStream(file));
			ostream.writeObject(this);
		} catch (Exception e) {
			if (e instanceof NotSerializableException)
				System.err.println(e.getCause() + " is not serializable");
			e.printStackTrace();
		}
	}

	public Player load() {
		Player p = null;
		File f = null;
		ObjectInputStream istream = null;
		try {
			String path = System.getProperty("user.dir");
			f = new File(path + "\\res\\players\\" + getName() + ".txt");
			if (!f.exists())
				playerExists = false;
			else {
				playerExists = true;
				istream = new ObjectInputStream(new FileInputStream(f));
				Object readObject = istream.readObject();
				if (readObject instanceof Player)
					p = (Player) readObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		preload = false;
		return p;
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

	public boolean playerExists() {
		return playerExists;
	}

	public void setPassword(String password) {
		this.password = password;

	}

	public String getPassword() {
		return password;
	}
}
