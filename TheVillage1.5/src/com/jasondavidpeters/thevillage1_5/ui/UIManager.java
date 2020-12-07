package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

	private List<Component> components = new ArrayList<Component>();
	private ChatBox chatbox = null;
	private MessageBox messagebox = null;

	public void add(Component c) {
		if (c instanceof Component) {
			if (c instanceof ChatBox)
				chatbox = (ChatBox) c;
			if (c instanceof MessageBox)
				messagebox = (MessageBox) c;
			components.add(c);
		}
		else
			System.err.println("Component is not instanced");
	}

	public void render(Graphics g) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).render(g);
		}
	}

	public void tick() {
		for (int i = 0; i < components.size(); i++) 
			components.get(i).tick();
		
//		if (chatbox.getPlayer() != null)
//		if (chatbox.getPlayer().messageSubmitted()) { // if user presses enter
//			if (messagebox.getMessages().size() >= messagebox.maxMessagesPerScreen) { // add scrollbar
//				// System.out.println("scrollbar height: " + messagebox.getScrollbarHeight());
//				messagebox.setSizeInc(messagebox.getSizeInc() + 1);
//				messagebox.setScrollbarHeight(messagebox.getScrollbarHeight() - messagebox.getSizeInc());
//				messagebox.setScrollbarY(messagebox.getScrollbarY() + messagebox.getSizeInc());
//				messagebox.setStartingPoint(messagebox.getStartingPoint() + 1); // only increments to 25
//			}
//			chatbox.setMessage("");
//		}

	}
	public List<Component> getComponents() {
		return components;
	}
}
