package org.lp.forwardChaining;

import java.util.ArrayList;

public class Player {
	
	public CardBox[] hands;
	public ArrayList<Card> cards;
	public String name;
	
	public Player() {
		// TODO Auto-generated constructor stub
		cards = new ArrayList<Card>();
		hands = new CardBox[16];
		for(int i=0; i<16; i++){
			hands[i] = new CardBox();
		}
	}
	
	public Player(String name){
		cards = new ArrayList<Card>();
		hands = new CardBox[16];
		for(int i=0; i<16; i++){
			hands[i] = new CardBox();
		}
		this.name = name;
	}
	
	public void analysisCards() {
		for (Card c : this.cards) {
			int id = c.id;
			hands[id].count++;
			hands[id].value = c.value;
			hands[id].cards.add(c);
		}
	}
	
	public void removeCardsFromBox(Cards cards){
		if(cards == null) return;
		for(Card c : cards.cards){
			int id = c.id;
			hands[id].count--;
			hands[id].cards.remove(0);
		}
	}
}
