package org.lp.forwardChaining;

import javax.swing.JOptionPane;

public class GlobalValue {
	public static int turn =1;
	public static GlobalValue gv;
	public final int computerTurn = 0;
	public final int playerTurn = 1;
	private static Cards ComputerLastCards;
	private static Cards playerLastCards;
	
	private int computerCardsLeft = 27;
	private int playerCardsLeft = 27;
	
	private GlobalValue(){
		ComputerLastCards = new Cards();
		playerLastCards = new Cards();
	}
	
	public static GlobalValue getInstance(){
		if(gv == null) gv = new GlobalValue();
		return gv;
	}
	
	public void changeTurn(){
		turn = (turn + 1)%2;
	}
	
	public boolean isPlayerTurn(){
		return (turn == playerTurn);
	}
	
	public boolean isComputerTurn(){
		return (turn == computerTurn);
	}

	public Cards getComputerLastCards() {
		return ComputerLastCards;
	}

	public void setComputerLastCards(Cards computerLastCards) {
		ComputerLastCards = computerLastCards;
	}

	public Cards getPlayerLastCards() {
		return playerLastCards;
	}

	public void setPlayerLastCards(Cards playerLastCards) {
		this.playerLastCards = playerLastCards;
	}
	
	public void decreaseCardsNum(int memOrPlayer, Cards cards){
		int num;
		if(cards == null)
			num=0;
		else 
			num = cards.cards.size();
		
		if(memOrPlayer == computerTurn)
			computerCardsLeft -= num;
		else if(memOrPlayer == playerTurn)
			playerCardsLeft -= num;
		
	}
	
	public void checkwin(){
		if(computerCardsLeft == 0)
			JOptionPane.showMessageDialog(null, "computer win!");
		else if(playerCardsLeft == 0)
			JOptionPane.showMessageDialog(null, "player win!");
	}
	 
}
