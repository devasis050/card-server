package com.example.cardserver.model;

import java.io.Serializable;

public class Team implements Serializable{
	
	private static final long serialVersionUID = 6210714712890256057L;
	private String player1;
	private String player2;
	private int score;
	private int call;
	
	public String getPlayer1() {
		return player1;
	}
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	public String getPlayer2() {
		return player2;
	}
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getCall() {
		return call;
	}
	public void setCall(int call) {
		this.call = call;
	}
	
	
}
