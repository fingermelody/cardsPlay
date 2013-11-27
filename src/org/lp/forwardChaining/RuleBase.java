package org.lp.forwardChaining;

import java.util.ArrayList;


public class RuleBase {
	
	private Cards analysis(Cards cards, Memory m){
		
		Cards candidates = new Cards();
		
		CardsType t = cards.type;
		
		switch(t){
			case single:{
				ArrayList<Cards> singles = m.searchSingle();
				if(!singles.isEmpty()){
					return m.searchLarger(singles, cards);
				}
				break;
			}
			case pair:{
				ArrayList<Cards> pairs = m.searchSingle();
				if(!pairs.isEmpty()){
					return m.searchLarger(pairs, cards);
				}
				break;
			}
			case three:{
				ArrayList<Cards> three = m.searchSingle();
				if(!three.isEmpty()){
					return m.searchLarger(three, cards);
				}
				break;
			}
			case four:{
				ArrayList<Cards> four = m.searchSingle();
				if(!four.isEmpty()){
					return m.searchLarger(four, cards);
				}
				break;
			}
		}
		
		return null;
	}
	
	
}
