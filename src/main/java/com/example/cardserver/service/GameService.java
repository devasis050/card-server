package com.example.cardserver.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import com.example.cardserver.model.Card;
import com.example.cardserver.model.Game;
import com.example.cardserver.model.Player;
import com.example.cardserver.model.Round;
import com.example.cardserver.model.Team;
import com.example.cardserver.store.GameStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GameService {

	@Autowired
	private GameStore store;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	public Game join(String playerName, int teamNumber) {

		Game game = store.getGame();
		if(!game.getPlayers().contains(playerName)) {
			Team team = teamNumber == 1 ? game.getTeam1() : game.getTeam2();
			
			if (team.getPlayer1() == null) {
				team.setPlayer1(playerName);
			} else if (team.getPlayer2() == null) {
				team.setPlayer2(playerName);
			}
		}
		

		return game;
	}

	public synchronized Game startGame(String playerName) {
		Game game = store.getGame();
		if (game.getGameStartedBy() == null) {
			game.setGameStartedBy(playerName);
			distributeCards();
			Round round = game.getRound();
			round.setRanga(getRanga(playerName));
			round.setNextTurn(playerName);
		}

		return game;
	}
	
	public synchronized Game finishGame() {
		Game game = store.getGame();
		if(game.getRound().getNumber() == 14) {
			Team team1 = game.getTeam1();
			Team team2 = game.getTeam2();
			
			//Reset
			resetTeam(team1);
			resetTeam(team2);
			resetGame(game);
			store.getPlayer(team1.getPlayer1()).setCall(-1);
			store.getPlayer(team1.getPlayer2()).setCall(-1);
			store.getPlayer(team2.getPlayer1()).setCall(-1);
			store.getPlayer(team2.getPlayer2()).setCall(-1);
		} else {
			throw new UnsupportedOperationException("Game not over yet");
		}
		
		return game;
	}
	
	private void resetGame(Game game) {
		game.setGameStartedBy(null);
		game.setRound(new Round());
	}
	
	private void resetTeam(Team team) {
		team.setCall(0);
		team.setScore(0);
	}

	private int calculateTeamFinalScore(Team team) {
		if(team.getScore() < team.getCall() || team.getScore() > team.getCall() + 3) {
			return -10 * team.getCall();
		} else {
			return 10 * team.getCall() + team.getScore() - team.getCall();
		}
	}
	
	private Card getRanga(String playerName) {
		int random = ThreadLocalRandom.current().nextInt(0, 13);
		return store.getPlayer(playerName).getCards().get(random);

	}

	private Card getCard(String shortFormat) {
		Character type = shortFormat.charAt(0);
		Integer number = Integer.parseInt(shortFormat.substring(1));
		return new Card(Card.Type.valueOf(type.toString()), number);
	}

	private void distributeCards() {
		List<String> cards = Arrays.asList("C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "C11", "C12", "C13",
				"C14", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "D11", "D12", "D13", "D14", "S2", "S3",
				"S4", "S5", "S6", "S7", "S8", "S9", "S10", "S11", "S12", "S13", "S14", "H2", "H3", "H4", "H5", "H6",
				"H7", "H8", "H9", "H10", "H11", "H12", "H13", "H14");
		List<String> remaining = new ArrayList<>(cards);

		Team team1 = store.getGame().getTeam1();
		Team team2 = store.getGame().getTeam2();

		List<Player> players = Arrays.asList(store.getPlayer(team1.getPlayer1()), store.getPlayer(team1.getPlayer2()),
				store.getPlayer(team2.getPlayer1()), store.getPlayer(team2.getPlayer2()));

		int max = 53;

		ArrayList<Card> playerCards = new ArrayList<>();
		for (Player player : players) {

			for (int i = 1; i <= 13; i++) {
				int random = ThreadLocalRandom.current().nextInt(1, max--);
				playerCards.add(getCard(remaining.remove(random - 1)));
			}
			Collections.sort(playerCards);
			player.setCards(playerCards);
			playerCards = new ArrayList<>();
		}
	}

	public void submitRound() {
		Game game = store.getGame();
		Round round = game.getRound();
		Round nextRound = new Round();
		nextRound.setRanga(round.getRanga());

		Team team1 = game.getTeam1();
		Team team2 = game.getTeam2();
		
		if (round.getNumber() == 0) {
			int team1Call = store.getPlayer(team1.getPlayer1()).getCall() 
					+ store.getPlayer(team1.getPlayer2()).getCall();
			
			int team2Call = store.getPlayer(team2.getPlayer1()).getCall() 
					+ store.getPlayer(team2.getPlayer2()).getCall();
			if( team1Call + team2Call == 9 || team1Call + team2Call == 10) {
				team1Call ++;
				team2Call ++;
			}
			team1.setCall(team1Call);
			team2.setCall(team2Call);
			
			nextRound.setNextTurn(game.getGameStartedBy());
			nextRound.setNumber(1);
		} else{
			calculateWinner(round);
			nextRound.setNumber(round.getNumber() + 1);
			nextRound.setNextTurn(round.getWinner());
		}
		
		new Thread(() -> {
			try {
				Thread.sleep(5000);
				game.setRound(nextRound);
				if(nextRound.getNumber() == 14) {
					game.getTeam1Score().add(calculateTeamFinalScore(team1));
					game.getTeam2Score().add(calculateTeamFinalScore(team2));
				}
				byte[] data = new ObjectMapper().writeValueAsBytes(game);
				Message<byte[]> message = new GenericMessage<>(data);
				simpMessagingTemplate.send("/game/nextRound", message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}).start();
	}

	String calculateWinner(Round round) {

		Entry<String, Card> winner = round.getMoves().entrySet().stream()
				.filter(entry -> entry.getValue().equals(round.getBase())).findFirst().get();

		for (Entry<String, Card> entry : round.getMoves().entrySet()) {
			Card currentCard = entry.getValue();
			Card winnerCard = winner.getValue();
			// Joker
			boolean isJoker = currentCard.equals(Card.JOKER) && !round.getBase().equals(Card.JOKER)
					&& round.getNumber() != 1 && round.getNumber() != 13;
			if (isJoker) {
				winner = entry;
				break;
			}

			if (currentCard.getType() == winnerCard.getType()) {
				if (currentCard.getNumber() > winnerCard.getNumber()) {
					winner = entry;
				}
			} else if (currentCard.getType() == round.getRanga().getType()) {
				winner = entry;
			}

		}
		round.setWinner(winner.getKey());
		Team team1 = store.getGame().getTeam1();
		Team team2 = store.getGame().getTeam2();
		if(winner.getKey().equals(team1.getPlayer1()) || winner.getKey().equals(team1.getPlayer2())) {
			team1.setScore(team1.getScore() +1);
		} else {
			team2.setScore(team2.getScore() +1);
		}
		
		return round.getWinner();
	}

	public Game submitCall(Player player) {
		store.getPlayer(player.getName()).setCall(player.getCall());
		Round round = store.getGame().getRound();
		round.getCalls().put(player.getName(), null);
		if (round.getCalls().size() == 4) {
			round.getCalls().entrySet().stream().forEach(entry -> {
				String playerName = entry.getKey();
				entry.setValue(store.getPlayer(playerName).getCall());
			});
			submitRound();
		}
		return store.getGame();
	}

	public Game submitMove(Map<String, Card> move) {
		Game game = store.getGame();
		Round round = game.getRound();
		
		List<String> players = Arrays.asList(game.getTeam1().getPlayer1(), game.getTeam2().getPlayer1(),
				game.getTeam1().getPlayer2(), game.getTeam2().getPlayer2());
		
		String playerName = move.entrySet().iterator().next().getKey();
		Card card = move.entrySet().iterator().next().getValue();
		
		int nextPlayerIndex = players.indexOf(playerName) < 3 ? players.indexOf(playerName) +1 : 0 ;
		
		round.getMoves().put(playerName, card);
		
		if(round.getMoves().size() == 1) {
			round.setBase(card);
		}
		else if(round.getMoves().size() == 4) {
			submitRound();
		} 
		round.setNextTurn(players.get(nextPlayerIndex));
		
		return store.getGame();
	}

}
