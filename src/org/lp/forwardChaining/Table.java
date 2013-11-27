package org.lp.forwardChaining;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Lock;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Table extends JFrame{
	
	private int computerCardsNum = 27;
	private int playerCardsNum  = 27;
	
	
	GlobalValue gv = GlobalValue.getInstance();
	
	public final Object lock;
	
	private Dealer dealer = new Dealer();
	private Player player = new Player("player");
	private Memory computerMem;
	
	public JTextField computerCardsField;
	JTextField playerCardsField;
	JTextArea cardsAreaField;
	JTextField cardsPlayField;
	JScrollPane scrollPane;
	
	public Table(Memory m,final Object lock) {
		this.computerMem = m;
		this.lock = lock;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(500,100);
        setSize(800,900);
        	
       JPanel complex = new JPanel();
//       complex.setLayout(new BoxLayout(complex,BoxLayout.Y_AXIS));
       complex.setLayout(new GridLayout(4,0));
       computerCardsField = new JTextField("computer's cards");
       playerCardsField = new JTextField("player's cards");
       cardsAreaField = new JTextArea();
       cardsPlayField = new JTextField(); 
       scrollPane = new JScrollPane(cardsAreaField);
       
       complex.setSize(800, 600);
       computerCardsField.setPreferredSize(new Dimension(200,200));
       computerCardsField.setEditable(false);
       
//       playerCardsField.setPreferredSize(new Dimension(700,200));
       playerCardsField.setEditable(false);
       
//       cardsAreaField.setPreferredSize(new Dimension(600,400));
       cardsAreaField.setEditable(false);
//       scrollPane.setPreferredSize(new Dimension(700,500));
       
       
       //area to enter cards player wanna play 
       cardsPlayField.setPreferredSize(new Dimension(700,20));
       cardsPlayField.addKeyListener(new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(!gv.isPlayerTurn()) return;
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				String cardsString = cardsPlayField.getText();
				if(cardsString.equals("")){
					gv.changeTurn();
					gv.setPlayerLastCards(null);
					synchronized (lock) {
						lock.notifyAll();
					}
					return;
				}
				Cards cards = checkLegal(cardsString, player.hands);
				if(gv.getComputerLastCards() == null){
					gv.setPlayerLastCards(cards);
					showCards(playerCardsField,player.hands);
					updateCardsArea(player, cards);
					gv.changeTurn();
					synchronized (lock) {
						lock.notifyAll();
					}
					return;
				}
				if((cards == null)||(cards.compareTo(gv.getComputerLastCards())<=0)){
					JOptionPane.showMessageDialog(null, "can not play like this.");
					return;
				}
				gv.setPlayerLastCards(cards);
				showCards(playerCardsField,player.hands);
				updateCardsArea(player, cards);
				cardsPlayField.setText("");
				gv.decreaseCardsNum(gv.playerTurn, cards);
				gv.checkwin();
				gv.changeTurn();
				synchronized (lock) {
					lock.notifyAll();
				}
				return;
			}
		}
	});
       
       
//       complex.add(Box.createVerticalStrut(10));
       complex.add(computerCardsField);
       complex.add(scrollPane);
       complex.add(playerCardsField);
       complex.add(cardsPlayField);
       
       this.add(complex);
       
       //Deal cards
       dealer.shuffle();
       dealer.deal(player.cards, computerMem.cards);
       
       player.analysisCards();
       computerMem.analysisCards();
       
       showCards(playerCardsField, player.cards);
       showCards(computerCardsField, computerMem.cards);
	}
	
	private void showCards(JTextField textField, ArrayList<Card> cards){
		Collections.sort(cards);
		String cardsString = "";
		for(Card c : cards){
			cardsString += c.name+" ";
		}
		textField.setText(cardsString);
	}
	
	public void showCards(JTextField textField, CardBox[] cboxes){
		ArrayList<Card> cards = new ArrayList<Card>();
		for(CardBox cb : cboxes){
			cards.addAll(cb.cards);
		}
		showCards(textField, cards);
	}
	
	public void updateCardsArea(Player p,Cards cards){
		String all = cardsAreaField.getText();
		all += p.name+":\n"+cards+"\n";
		cardsAreaField.setText(all);
		cardsAreaField.setSelectionStart(all.length());
	}
	

	
	private Cards checkLegal(String s, CardBox[] boxs){
		
		if(s.length() == 0)
			return null;
		Cards cards = new Cards();
		String[] sArray  = s.split(" ");
		//nomal check
		
		if(sArray.length == 1){
			cards.type = CardsType.single;
			CardBox box = boxs[getCardIDFromName(s)];
			if(box.count > 0){
				box.count --;
				cards.value = box.value;
				cards.cards.add(box.cards.remove(0));
				return cards;
			}
		}else if(sArray.length == 2){
			
			if(!sArray[0].equals(sArray[1])) return null;
			cards.type = CardsType.pair;
			CardBox box = boxs[getCardIDFromName(sArray[0])];
			if(box.count > 1){
				box.count -= 2;
				cards.value = box.value * 2;
				cards.cards.add(box.cards.remove(0));
				cards.cards.add(box.cards.remove(0));
				return cards;
			}
		}else if(sArray.length == 3){
			if(sArray[0].equals(sArray[1])&&(sArray[1].equals(sArray[2]))){
				cards.type = CardsType.three;
				CardBox box = boxs[getCardIDFromName(sArray[0])];
				if(box.count>2){
					box.count -= 3;
					cards.value = box.value * 3;
					cards.cards.add(box.cards.remove(0));
					cards.cards.add(box.cards.remove(0));
					cards.cards.add(box.cards.remove(0));
					return cards;
				}
			}
		}else if(sArray.length == 4){
			if(sArray[0].equals(sArray[1])&&(sArray[1].equals(sArray[2]))&&(sArray[2].equals(sArray[3]))){
				cards.type = CardsType.four;
				CardBox box = boxs[getCardIDFromName(sArray[0])];
				if(box.count>3){
					box.count -= 4;
					cards.setBombValue(box.cards.get(0).value);
					cards.cards.add(box.cards.remove(0));
					cards.cards.add(box.cards.remove(0));
					cards.cards.add(box.cards.remove(0));
					cards.cards.add(box.cards.remove(0));
					return cards;
				}
			}
		}
		
		return null;
	}
	
	private int getCardIDFromName(String s){
		switch(s){
		case "A":
			return 1;	
		case "J":
			return 11;
		case "Q":
			return 12;
		case "K":
			return 13;
		case "moon":
			return 14;
		case "sun" :
			return 15;
		default:
			return Integer.valueOf(s);
		
		}
	}
	
	private float getCardValueFromName(String s){
		switch(s){
		case "A":
			return 14;	
		case "J":
			return 11;
		case "Q":
			return 12;
		case "K":
			return 13;
		case "moon":
			return 15;
		case "sun" :
			return 16;
		default:
			return Integer.valueOf(s);
		
		}
	}
}
