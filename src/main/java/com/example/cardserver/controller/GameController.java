package com.example.cardserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cardserver.model.Game;
import com.example.cardserver.service.GameService;
import com.example.cardserver.store.GameStore;

@RestController
public class GameController {
	
	@Autowired
	private GameStore store;
	
	@Autowired
	private GameService gameService;
	
	@GetMapping("/game")
	@CrossOrigin(origins = "http://localhost:3000")
	public Game getGame() {
		return store.getGame();
	}
	
	@PostMapping("/game/reset")
	public String resetGame() {
		store.hardResetGame();
		return "Success";
	}
	
	@PostMapping("/game/finishGame")
	public Game finishGame() {
		return gameService.finishGame();
	}
	
	@PostMapping("/game/resetMatch")
	public String resetMatch() {
		gameService.resetMatch();
		return "Success";
		
	}

}
