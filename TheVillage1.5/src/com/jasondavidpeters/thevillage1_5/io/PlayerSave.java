package com.jasondavidpeters.thevillage1_5.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import com.jasondavidpeters.thevillage1_5.entity.Player;

public class PlayerSave implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String username, password;
	public HashMap<String, Object> attributes;
	private boolean fileExists;

	public PlayerSave(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public PlayerSave load(Player p) {
		 // TODO: password checking
		PlayerSave ps = null;
		File f = null;
		ObjectInputStream istream = null;
		try {
			String path = System.getProperty("user.dir");
			f = new File(path + "\\res\\players\\" + getName() + ".txt");
			if (!f.exists()) {
				p.setPlayerExists(false);
			}else {
				p.setPlayerExists(true);
				istream = new ObjectInputStream(new FileInputStream(f));
				Object readObject = istream.readObject();
				if (readObject instanceof PlayerSave)
					ps= (PlayerSave) readObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ps;
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

	public boolean fileExists() {
		return fileExists;
	}

	public String getName() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes=attributes;
		
	}

	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

}
