package com.example.cardserver.model;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable{
	
	private static final long serialVersionUID = -5943190606852088263L;
	private int number;
	private Type type;
	public static Card JOKER = new Card(Type.S, 2);
	
	public Card() {
		
	}
	
	public Card(Type type, int number) {
		this.type = type;
		this.number = number;
	}
	
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public static enum Type {
		S, D, H, C 
	}
	@Override
	public String toString() {
		return "" + type + number;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number != other.number)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int compareTo(Card arg) {
		if(this.type.toString().compareTo(arg.type.toString()) ==0) {
			if(this.number == arg.number) {
				return 0;
			} else if(this.number >= arg.number) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return this.type.toString().compareTo(arg.type.toString());
		}
	}
}
