package com.jasondavidpeters.thevillage1_5.util;

import java.util.ArrayList;

import com.jasondavidpeters.thevillage1_5.ui.Label;

public class Utilities {
	
	public static ArrayList<Label> rearrange(ArrayList<Label> a) {
		ArrayList<Label> b = new ArrayList<Label>();
		for (int i = a.size()-1; i >=0; i--)
			b.add(a.get(i));
		return b;
		
	}

}
