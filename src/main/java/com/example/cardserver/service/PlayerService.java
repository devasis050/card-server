package com.example.cardserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.cardserver.model.Card;
import com.example.cardserver.model.Player;
import com.example.cardserver.store.GameStore;

@Component
public class PlayerService {
	
	@Autowired
	private GameStore store;

	public Player removeCard(String playerName, Card card) {
		Player player = store.getPlayer(playerName);
		player.getCards().remove(card);
		
		return player;
	}
	
	
}
