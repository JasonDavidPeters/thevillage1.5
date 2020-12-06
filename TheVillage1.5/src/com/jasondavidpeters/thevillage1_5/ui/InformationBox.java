package com.jasondavidpeters.thevillage1_5.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class InformationBox extends Component {

	private List<Label> labels = new ArrayList<Label>();
	private int fontSize;

	public InformationBox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public void write(String text) {
		for (Label l : labels)
			if (l.getText().contains(text))
				return;
		if (text.equalsIgnoreCase(""))
			return;
		labels.add(new Label(text,x,(y+15+fontSize)*(labels.size()+1),0,0));
	}

	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		for (int i = 0; i < labels.size(); i++) {
			g.drawString(labels.get(i).getText(), x, labels.get(i).getY());
			labels.get(i).render(g);
		}
	}

	public void tick() {
		for (Label l: labels)
			l.tick();
	}
}
