package com.example.cardserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cardserver.model.Card;
import com.example.cardserver.model.Player;
import com.example.cardserver.service.PlayerService;
import com.example.cardserver.store.GameStore;

@RestController
public class PlayerController {
	
	@Autowired
	private GameStore store;
	
	@Autowired
	private PlayerService playerService;
	
	@PostMapping("/player")
	public Player createPlayer(@RequestBody Player player) {
		return store.addPlayer(player);
	}
	
	@PostMapping("/player/{playerName}")
	public Player getPlayer(@PathVariable String playerName) {
		return store.getPlayer(playerName);
	}
	
	@PostMapping("/player/{playerName}/removecard")
	public Player removePlayerCard(@PathVariable String playerName, @RequestBody Card card) {
		return playerService.removeCard(playerName, card);
	}
}
