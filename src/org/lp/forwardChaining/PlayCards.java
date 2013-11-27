package org.lp.forwardChaining;

public class PlayCards {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final Object lock = new Object();
		Memory computerMem = new Memory("computer", lock);
		Table table  = new Table(computerMem,lock);
		table.setVisible(true);
		
		computerMem.initialize(table);
		computerMem.run();
		
	}

}
