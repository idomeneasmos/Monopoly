import java.io.*;
import java.util.*;

public class Card {
	private String name;
	private int cost;
	private int[] payments;
	private boolean mode;
	private String color;
	private int housecost;
	
	public Card(String name, String color, int cost, int[] payments, boolean mode) {
		this.name=name;
		this.cost=cost;
		this.payments=payments;
		this.mode=mode;
		this.color=color;
		this.housecost=findhousecost();
	}
	
	public String getname() {
		return this.name;
	}
	
	public int getcost() {
		return this.cost;
	}
	
	public int[] getpayment() {
		return this.payments;
	}
	
	public boolean getmode() {
		return this.mode;
	}
	
	public String getcolor() {
		return this.color;
	}
	
	public int gethousecost() {
		return this.housecost;
	}
	
	public String toString() {
		return "The card "+this.name+", has "+ this.color+" color and costs "+this.cost+"$.\n";
	}
	
	public int findhousecost() {
		ArrayList<String> colors= new ArrayList<String>();
		String[] tempar={"red", "green", "blue", "velvet", "orange", "pink", "yellow", "brown"};
		colors.addAll(Arrays.asList(tempar));
		int index=colors.indexOf(this.color);
		int cost=index*25+50;
		return cost;
	}
	
	public void getstats() {
		if (!this.mode) {
			System.out.print("The card "+this.name+", has "+ this.color+" and you can't buy it.\n");
		}
		else {
			System.out.print("The card "+this.name+", has "+ this.color+" color and costs "+this.cost+"$.\n");
			for (int i=0;i<payments.length;i++)
				System.out.print("With "+i+" house(s), the payment is "+this.payments[i]+"$.\n");
		}
	}
}
