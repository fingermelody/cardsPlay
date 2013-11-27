package org.lp.forwardChaining;

import java.util.ArrayList;

public class Card implements Comparable{
	int id;
	String name;
	float value;
	
	ArrayList<ExternRelationship> relations;
	
	public Card(int id, String name, float value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}
	
	public int compareTo(Object o){
		return (int) (this.value - ((Card)o).value);
	}
}
