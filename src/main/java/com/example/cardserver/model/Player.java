package com.example.cardserver.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	
	private String name;
	private String id;
	private int call = -1;
	private ArrayList<Card> cards = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
	public int getCall() {
		return call;
	}
	public void setCall(int call) {
		this.call = call;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
