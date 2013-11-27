package org.lp.forwardChaining;

import java.util.ArrayList;


public class Cards implements Comparable{

	private float bombValue = 100;
	
	 CardsType type; 
	 ArrayList<Card> cards;
	 float value;
	 
	 public Cards() {
		 type = CardsType.single;
		 cards = new ArrayList<Card>();
		 value = 0;
	 }
	 
	 public int compareTo(Object o){
		 if(o == null) return 1;
			return (int) (this.value - ((Cards)o).value);
	}
	 
	 public String toString(){
		 String cards = "";
		 for(Card c : this.cards)
			 cards += c.name+" ";
		 return cards;
	 }
	 
	 public void setBombValue(float offset){
		 this.value = this.bombValue + offset;
	 }
}
