package com.example.cardserver.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Round implements Serializable {
	
	private static final long serialVersionUID = 6893708099282428756L;
	private int number = 0;
	private Map<String, Integer> calls = new HashMap<>();
	private Map<String, Card> moves = new HashMap<>();
	private Card ranga;
	private String nextTurn;
	private Card base;
	private String winner;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Map<String, Integer> getCalls() {
		return calls;
	}
	public void setCalls(Map<String, Integer> calls) {
		this.calls = calls;
	}
	public Map<String, Card> getMoves() {
		return moves;
	}
	public void setMoves(Map<String, Card> moves) {
		this.moves = moves;
	}
	public Card getRanga() {
		return ranga;
	}
	public void setRanga(Card ranga) {
		this.ranga = ranga;
	}
	public String getNextTurn() {
		return nextTurn;
	}
	public void setNextTurn(String nextTurn) {
		this.nextTurn = nextTurn;
	}
	public Card getBase() {
		return base;
	}
	public void setBase(Card base) {
		this.base = base;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	
	

}
