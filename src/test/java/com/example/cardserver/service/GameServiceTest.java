package com.example.cardserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.cardserver.model.Card;
import com.example.cardserver.model.Round;
import com.example.cardserver.store.GameStore;

public class GameServiceTest {
	
	GameService target = new GameService(new GameStore());
	
	
	public void setUp() {
	}
	
	@Test
	public void calculateWinner() {
		String winnner = target.calculateWinner(getRound());
		assertEquals("p4", winnner);
	}
	
	@Test
	public void calculateWinner_ranga() {
		
		Round round = getRound();
		round.getMoves().put("p3", new Card(Card.Type.S, 5));
		String winnner = target.calculateWinner(round);
		assertEquals("p3", winnner);
	}
	
	@Test
	public void calculateWinner_2ranga() {
		
		Round round = getRound();
		round.getMoves().put("p2", new Card(Card.Type.S, 5));
		round.getMoves().put("p4", new Card(Card.Type.S, 7));
		String winnner = target.calculateWinner(round);
		assertEquals("p4", winnner);
	}
	
	@Test
	public void calculateWinner_alRanga() {
		
		Round round = getRound();
		round.setRanga(new Card(Card.Type.C, 5));
		String winnner = target.calculateWinner(round);
		assertEquals("p4", winnner);
	}
	
	@Test
	public void calculateWinner_baseRangaAndOneNonRanga() {
		
		Round round = getRound();
		round.setRanga(new Card(Card.Type.C, 5));
		round.getMoves().put("p4", new Card(Card.Type.S, 7));
		String winnner = target.calculateWinner(round);
		assertEquals("p3", winnner);
	}
	
	@Test
	public void calculateWinner_joker() {
		Round round = getRound();
		round.getMoves().put("p2", Card.JOKER);
		String winnner = target.calculateWinner(round);
		assertEquals("p2", winnner);
	}
	
	@Test
	public void calculateWinner_jokerFirst() {
		Round round = getRound();
		round.getMoves().put("p2", Card.JOKER);
		round.setRanga(new Card(Card.Type.H, 5));
		round.setNumber(1);
		String winnner = target.calculateWinner(round);
		assertEquals("p4", winnner);
	}
	
	@Test
	public void calculateWinner_jokerFirstAndRangaSpade() {
		Round round = getRound();
		round.getMoves().put("p2", Card.JOKER);
		round.setNumber(1);
		String winnner = target.calculateWinner(round);
		assertEquals("p2", winnner);
	}
	
	@Test
	public void calculateWinner_jokerLirst() {
		Round round = getRound();
		round.getMoves().put("p2", Card.JOKER);
		round.setRanga(new Card(Card.Type.H, 5));
		round.setNumber(13);
		String winnner = target.calculateWinner(round);
		assertEquals("p4", winnner);
	}
	
	@Test
	public void calculateWinner_jokerLastAndRangaSpade() {
		Round round = getRound();
		round.getMoves().put("p2", Card.JOKER);
		round.setNumber(13);
		String winnner = target.calculateWinner(round);
		assertEquals("p2", winnner);
	}
	
	@Test
	public void calculateWinner_jokerIsRanga() {
		Round round = getRound();
		round.setRanga(Card.JOKER);
		round.getMoves().put("p2", new Card(Card.Type.S, 7));
		String winnner = target.calculateWinner(round);
		assertEquals("p4", winnner);
	}
	
	@Test
	public void calculateWinner_jokerIsRangaJokerRound() {
		Round round = getRound();
		round.setRanga(Card.JOKER);
		round.getMoves().put("p2", Card.JOKER);
		String winnner = target.calculateWinner(round);
		assertEquals("p2", winnner);
	}
	
	private Round getRound() {
		Round round = new Round();
		round.setNumber(2);
		round.setBase(new Card(Card.Type.C, 5));
		round.setRanga(new Card(Card.Type.S, 5));
		
		Map<String, Card> moves = new HashMap<String, Card>();
		moves.put("p1", new Card(Card.Type.C, 5));
		moves.put("p2", new Card(Card.Type.C, 7));
		moves.put("p3", new Card(Card.Type.C, 10));
		moves.put("p4", new Card(Card.Type.C, 14));
		
		round.setMoves(moves);
		return round;
	}

}
