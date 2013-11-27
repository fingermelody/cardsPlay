package org.lp.forwardChaining;

import java.util.ArrayList;
import java.util.Collections;

public class Dealer {

	private ArrayList<Card> cards;
	private int countOfCards;
	
	public Dealer() {
		countOfCards = 54;
		cards = new ArrayList<Card>();
		
	//create cards jokers, sun's id is 15 and value is 17, moon's id is 14 and value is 16
			Card sun = new Card(15,"sun",17);
			Card moon = new Card(14, "moon", 16);
			this.cards.add(sun);
			this.cards.add(moon);
			
			//create cards Ace, JACK, QUEEN, KING, 2
			for(int i=0;i<4;i++){
				this.cards.add(new Card(1,"A",14));
				this.cards.add(new Card(2, "2", 15));
				this.cards.add(new Card(11, "J", 11));
				this.cards.add(new Card(12, "Q", 12));
				this.cards.add(new Card(13, "K", 13));
			}
			
			//create cards from 3 to 10
			for(int i=3;i<=10;i++)
				for(int j=0;j<4;j++)
					this.cards.add(new Card(i, i+"", i));
		
	}
	
	public ArrayList<Card> shuffle(){
		
		//shuffle cards
		Collections.shuffle(this.cards);
		return this.cards;
	}
	
	public void deal(ArrayList<Card> playerCards, ArrayList<Card> computerCards){
		if(playerCards.isEmpty() && computerCards.isEmpty()){
			for(int i=0;i<54;i++){
				if(i<27)
					playerCards.add(this.cards.get(i));
				else
					computerCards.add(this.cards.get(i));
			}
			
			return ;
		}
		System.out.println("deal cards fails");
	}
}
