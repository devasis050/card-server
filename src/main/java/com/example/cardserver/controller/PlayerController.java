package com.example.cardserver.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.cardserver.model.Card;
import com.example.cardserver.model.Player;
import com.example.cardserver.service.PlayerService;
import com.example.cardserver.store.GameStore;

@RestController
public class PlayerController {
	
	private static final String USER_ID_COOKIE_NAME = "rangaPlayer";
	
	@Autowired
	private GameStore store;
	
	@Autowired
	private PlayerService playerService;
	
	
	@PostMapping("/player")
	public Player createPlayer(@RequestBody Player player, HttpServletResponse response) {
		String id = UUID.randomUUID().toString();
		Cookie cookie = new Cookie(USER_ID_COOKIE_NAME, id);
		cookie.setMaxAge(60*60*60*12);
		response.addCookie(cookie);
		player.setId(id);
		return store.addPlayer(player);
	}
	
	@GetMapping("/player")
	public Player getPlayer(@RequestHeader Map<String, String> headers) {
		return store.getPlayerById(headers.get("playerid"));
	}
	
	@PostMapping("/player/removecard")
	public Player removePlayerCard(@RequestHeader Map<String, String> headers, @RequestBody Card card) {
		Player player = store.getPlayerById(headers.get("playerid"));
		return playerService.removeCard(player.getName(), card);
	}
	
	@PostMapping("/player/clear")
	public String clearPlayer() {
		store.clearPlayer();
		return "Success";
	}
}
