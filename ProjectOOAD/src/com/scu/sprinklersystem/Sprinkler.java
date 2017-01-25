package com.scu.sprinklersystem;

import java.awt.Color;

import javax.swing.JLabel;

public class Sprinkler {
	
	private String sprinklerId;
	private String location;
	private boolean status = true;
	private JLabel sprinkler;
	
	enum Group {EAST, WEST, NORTH, SOUTH};
	
	public String getSprinklerId() {
		return sprinklerId;
	}
	
	//Creating Sprinkler on the Map
	public JLabel createSprinkler() {
		
		sprinkler = new JLabel("•");
        sprinkler.setForeground(Color.blue);
	    sprinkler.setFont (sprinkler.getFont ().deriveFont (50.0f));
		
		return sprinkler;
	}
	public JLabel getSprinkler() {
		return sprinkler;
	}

	public void setSprinkler(JLabel sprinkler) {
		this.sprinkler = sprinkler;
	}

	public void setSprinklerId(String sprinklerId) {
		this.sprinklerId = sprinklerId;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
