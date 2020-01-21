package elements;

import java.util.Comparator;

import elements.fruit;

public class Fruit_Comperator implements Comparator<fruit> {

	public Fruit_Comperator() {;}
	


	@Override
	public int compare(fruit o1, fruit o2) {
		double dp = o2.getValue() - o1.getValue();
		return (int) dp;
			
	}


}