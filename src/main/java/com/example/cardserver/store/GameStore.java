package com.example.cardserver.store;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.cardserver.model.Game;
import com.example.cardserver.model.Player;

@Repository
public class GameStore {

	
	Game game = new Game();
	Map<String, Player> players = new HashMap<>(4);
	
	public Game getGame() {
		return game;
	}
	
	public void resetGame() {
		game = new Game();
	}
	
	public Player addPlayer(Player player) {
		if(players.get(player.getName()) == null) {
			players.put(player.getName(), player);
		}
		
		return players.get(player.getName());
	}
	
	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}

	public Player getPlayerById(String id) {
		return players.entrySet().stream().filter(entry->
			entry.getValue().getId().equals(id)).findFirst().orElseThrow(()->new RuntimeException("Player not found")).getValue();
	}
	
	public void clearPlayer() {
		players = new HashMap<>(4);
	}

}
