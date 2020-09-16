package com.example.cardserver.ws;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.cardserver.model.Card;
import com.example.cardserver.model.Game;
import com.example.cardserver.model.JoinRequest;
import com.example.cardserver.model.Player;
import com.example.cardserver.service.GameService;

@Controller
public class WebSocketController {

	@Autowired
	private GameService service;
	
	@MessageMapping("/join")
	@SendTo("/game/playerJoined")
	public Game join(@Payload JoinRequest request) {
		return service.join(request.getPlayerName(), request.getTeamNumber());
	}
	
	@MessageMapping("/start")
	@SendTo("/game/started")
	public Game startGame(@Payload String playerName) {
		return service.startGame(playerName);
	}
	
//	@MessageMapping("/submitRound")
//	@SendTo("/game/nextRound")
//	public Game submitRound() {
//		return service.submitRound();
//	}
	
	@MessageMapping("/call")
	@SendTo("/game/called")
	public Game submitCall(@Payload Player player) {
		return service.submitCall(player);
	}
	
	@MessageMapping("/submitMove")
	@SendTo("/game/moveAdded")
	public Game submitMove(@Payload Map<String, Card> move) {
		return service.submitMove(move);
	}
	
}
