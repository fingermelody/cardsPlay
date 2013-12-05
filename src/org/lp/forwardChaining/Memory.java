package org.lp.forwardChaining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Lock;

public class Memory extends Player implements Runnable{
	
	Table table;
	Object lock;
	GlobalValue gv = GlobalValue.getInstance();
	
	ArrayList<Cards> straights;
	
	public Memory(String name,Object lock) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.lock = lock;
	}
	
	public void initialize(Table table){
		this.table = table;
	}
	
	public void analysisStraight(){
		straights = new ArrayList<Cards>();
		
		for(int i=3;i<10;i++){
			int num=0;
			int j=i;
			while(hands[j].count != 0 && j<14){
				num++;
				j++;
			}
			if(num > 4){
				Cards cards = new Cards();
				cards.straightLenght = num;
				cards.straightStart = i;
				cards.value = num*i + num*(num-1)/2;
				int t = i;
				for(int m=0;m<num;m++){
					cards.cards.add(hands[t++].cards.get(0));
				}
				straights.add(cards);
			}
		}
	}
	
	public Cards searchLargerStraight(Cards cards){
		if(straights.isEmpty()) return null;
		for(Cards cs : straights){
			if((cs.straightLenght==cards.straightLenght)&&(cs.straightStart>cards.straightStart))
				return cs;
		}
		return null;
	}
	
	
	public ArrayList<Cards> searchSingle() {
		ArrayList<Cards> singleCards = new ArrayList<Cards>();

		for (CardBox cb : this.hands) {
			if (cb.count == 1){
				Cards single = new Cards();
				single.type = CardsType.single;
				single.cards.addAll(cb.cards);
				single.value = cb.value;
				singleCards.add(single);
			}
		}
		Collections.sort(singleCards);
		return singleCards;
	}

	public ArrayList<Cards> searchPair() {
		ArrayList<Cards> pairCards = new ArrayList<Cards>();

		for (CardBox cb : this.hands) {
			if (cb.count == 2) {
				Cards pair = new Cards();
				pair.type = CardsType.pair;
				pair.cards.addAll(cb.cards);
				pair.value = cb.value*2;
				pairCards.add(pair);
			}

		}
		Collections.sort(pairCards);
		return pairCards;
	}

	public ArrayList<Cards> searchThree() {
		ArrayList<Cards> threeCards = new ArrayList<Cards>();

		for (CardBox cb : this.hands) {
			if (cb.count == 3) {
				Cards three = new Cards();
				three.type = CardsType.three;
				three.cards.addAll(cb.cards);
				three.value = cb.value*3;
				threeCards.add(three);
			}

		}
		Collections.sort(threeCards);
		return threeCards;
	}

	public ArrayList<Cards> searchFour() {
		ArrayList<Cards> fourCards = new ArrayList<Cards>();

		for (CardBox cb : this.hands) {
			if (cb.count == 4) {
				Cards four = new Cards();
				four.type = CardsType.four;
				four.cards.addAll(cb.cards);
				four.setBombValue(cb.value);
				fourCards.add(four);
			}

		}
		Collections.sort(fourCards);
		return fourCards;
	}
	
	public Cards searchLarger(ArrayList<Cards> cards, Cards c){
		for(Cards cs : cards){
			if(cs.compareTo(c)>0)
				return cs;
		}
			
		return null;
	}
	
	
	
	private Cards searchMin(){
		Cards zero = new Cards();
		Cards single = searchLarger(searchSingle(),zero);
		Cards pair = searchLarger(searchPair(),zero);
		Cards three = searchLarger(searchThree(), zero);
		Cards four = searchLarger(searchFour(),zero);
		if((three!=null)&&((three.compareTo(pair)<0)||(three.compareTo(single)<0))) return three;
		if((pair!=null)&&(pair.compareTo(single)<0)) return pair;
		if(single!=null) return single;
		if(pair!=null) return pair;
		if(three!=null) return three;
		if(four != null) return four;
		return null;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(!gv.isComputerTurn()){
				try {
					synchronized (lock) {
						lock.notifyAll();
						lock.wait();
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			Cards candidate = null;
//			System.out.println(gv.getPlayerLastCards());
			if(gv.getPlayerLastCards() == null){
				candidate = searchMin();
			}else{
				Cards playerCards = gv.getPlayerLastCards();
				switch(playerCards.type){
					case single:{
						candidate = searchLarger(searchSingle(),playerCards);
						if(playerCards.value == 17)
							candidate = searchLarger(searchFour(),playerCards);
						break;
					}
					case pair:{
						candidate = searchLarger(searchPair(),playerCards);
						break;
					}
					case three:{
						candidate = searchLarger(searchThree(), playerCards);
						if(candidate == null)
							candidate = searchLarger(searchFour(),playerCards);
						break;
					}
					case four:{
						candidate = searchLarger(searchFour(),playerCards);
						break;
					}
					case straight:{
						candidate = searchLargerStraight(playerCards);
					}
				}
			}
			gv.setComputerLastCards(candidate);
			
			try {
				removeCardsFromBox(candidate);
				table.updateCardsArea(this, candidate);
				table.showCards(table.computerCardsField, this.hands);
				gv.decreaseCardsNum(gv.computerTurn, candidate);
				gv.checkwin();
				analysisStraight();
				gv.changeTurn();
				synchronized (lock) {
					lock.notifyAll();
					lock.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
}
