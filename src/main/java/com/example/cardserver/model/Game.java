package com.example.cardserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game implements Serializable {
	
	private static final long serialVersionUID = 4031123803614633216L;
	private Team team1 = new Team();
	private Team team2 = new Team();
	private String gameStartedBy;
	private Round round = new Round();
	private List<Integer> team1Score = new ArrayList<Integer>();
	private List<Integer> team2Score = new ArrayList<Integer>();
	
	public Team getTeam1() {
		return team1;
	}
	public void setTeam1(Team team1) {
		this.team1 = team1;
	}
	public Team getTeam2() {
		return team2;
	}
	public void setTeam2(Team team2) {
		this.team2 = team2;
	}
	public String getGameStartedBy() {
		return gameStartedBy;
	}
	public void setGameStartedBy(String gameStartedBy) {
		this.gameStartedBy = gameStartedBy;
	}
	public Round getRound() {
		return round;
	}
	public void setRound(Round round) {
		this.round = round;
	}
	public List<Integer> getTeam1Score() {
		return team1Score;
	}
	public void setTeam1Score(List<Integer> team1Score) {
		this.team1Score = team1Score;
	}
	public List<Integer> getTeam2Score() {
		return team2Score;
	}
	public void setTeam2Score(List<Integer> team2Score) {
		this.team2Score = team2Score;
	}
	
	public List<String> getPlayers() {
		return Arrays.asList(team1.getPlayer1(), team2.getPlayer1(), team1.getPlayer2(), team2.getPlayer2());
	}
	
	
}
