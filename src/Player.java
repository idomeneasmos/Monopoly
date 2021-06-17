import java.io.*;
import java.util.*;

public class Player {
	private String name;
	private static Scanner myobj= new Scanner(System.in);
	private static Random rand=new Random();
	private int money;
	private HashMap<String, Integer> cards = new HashMap<String, Integer>();
	private int place;
	
	public Player(String name) {
		this.name=name;
		this.money=1500;
		this.place=0;
	}
	
	public String getname() {
		return this.name;
	}
	
	public int getmoney() {
		return this.money;
	}
	
	public int getplace() {
		return this.place;
	}
	
	public HashMap<String, Integer> getcards(){
		return this.cards;
	}
	
	public String toString() {
		System.out.print("Player "+ this.name+" has "+ this.money+"$ and the following cards:\n");
		if (this.cards!=null) {
			for (String card:this.cards.keySet()) {
				System.out.print(card+" with "+ this.cards.get(card)+" house(s)\n");
			}
		}
		else {
			System.out.print("no cards\n");
		}
		return "\n";
	}

	public boolean isbroke() {
		if(this.money<=0)
			return true;
		else
			return false;
	}
	
	public void buycard() {
		Card thiscard=Maingame.Cards.get(this.place);
		if (thiscard.getmode()) {
			boolean flag=false;
			for(Player otherpl:Maingame.player) {
					if(otherpl.cards.containsKey(thiscard.getname())) {
						flag=true;
						break;
					}
			}
			if (!flag) {
				int cost=thiscard.getcost();
				if(this.money>cost) {
					this.cards.put(thiscard.getname(), 0);
					this.money-=cost;
					System.out.print(this.getname()+" bought this card!So now "+this.getname()+" have the following cards:\n"+this.cards.keySet()+"\n");
				}
				else {
					System.out.print(this.getname()+" don't have that much money. "+this.getname()+" can't buy this card!\n");
				}
				
			}
			else
				System.out.print(this.getname()+" can't buy this card! Someone else has it.\n");
		}
		else
			System.out.print("You can't buy "+thiscard.getname()+"\n");
	}
	
	public void buyhouse() {
		System.out.print("In which card will you buy a house?");
		String card=myobj.nextLine();
		String colorofbuy="";
		int housecost=0;
		for (Card cd:Maingame.Cards) {
			if (cd.getname().equals(card)) {
				colorofbuy=cd.getcolor();
				housecost=cd.gethousecost();
			}
		}
		HashMap<String,Integer> cardswiththiscolor= new HashMap<String,Integer>();
		for (Card cd:Maingame.Cards) {
			if(colorofbuy.equals(cd.getcolor())) {
				if (this.cards.containsKey(cd.getname())) {
					cardswiththiscolor.put(cd.getname(), this.cards.get(cd.getname()));
				}
				else
					cardswiththiscolor.put(cd.getname(), 0);
			}
		}
		int countallcards=0;
		boolean canbuildfromnumbofhouses=true;
		for (String cdcl:cardswiththiscolor.keySet()) {
			if (this.cards.containsKey(cdcl)) {
				countallcards++;
			}
			if(cardswiththiscolor.get(card)>cardswiththiscolor.get(cdcl)) {
				canbuildfromnumbofhouses=false;
			}
		}
		if (countallcards==cardswiththiscolor.size()) {
			if(canbuildfromnumbofhouses) {
				System.out.print("A house here costs "+housecost+"$.\n");
				int housesinthiscard = this.cards.get(card);
				if(housesinthiscard==5) {
					System.out.print(this.getname()+" can't buy any more houses in this card.\n");
				}
				else {
					if(this.money>housecost) {
						this.cards.put(card, housesinthiscard+1);
						this.money-=housecost;
						System.out.print(this.getname()+" bought a house in this card!So now you have "+this.cards.get(card)+" house(s) in here.\n");
					}
					else {
						System.out.print(this.getname()+" don't have that much money. "+this.getname()+" can't buy this card!\n");
					}
				}
			}
			else
				System.out.print("You have to build in the other cards of this color before you build in here an otherone house.\n");
		}
		else 
			System.out.print("You don't have all the cards of the color "+colorofbuy+". You need to have all the following cards:\n"+cardswiththiscolor.keySet()+"\n");
	}
	
	public void makedeal() {
		System.out.print("With whom do you want to make a deal?");
		String input=myobj.nextLine();
		Player dealplayer=Maingame.player[0];
		for(Player pl:Maingame.player) {
			if (pl.getname().equals(input)) {
				dealplayer=pl;
			}
		}
		if (dealplayer.equals(Maingame.player[0])) {
			System.out.print("There is no player with this name.\n");
			return;
		}
		//what will you give
		HashSet<String> givencards= new HashSet<String>();
		int wantedgivenmoney=0;
		int givenmoney=0;
		while(true) {
			System.out.print(this.toString()+"\n");
			System.out.print("What do you want to exchange?( cards , money or change))\n");
			String whattogive=myobj.nextLine();
			if(whattogive.equals("cards")) {
				System.out.print("Which card of yours do you want?\n");
				String card=myobj.nextLine();
				if (this.cards.containsKey(card)) {
					if(this.cards.get(card)==0) {
						givencards.add(card);
					}
					else 
						System.out.print("You can't exchange a card with houses. Choose an other one.\n");
				}
				else
					System.out.print("You don't have this card.\n");
			}
			else if (whattogive.equals("money")) {
				System.out.print("How much money do you want to exchange?\n");
				wantedgivenmoney=Integer.parseInt(myobj.nextLine());
				if(wantedgivenmoney<=dealplayer.getmoney())
					givenmoney=wantedgivenmoney;
				else
					System.out.print("You don't have that much money.\n");			}
			else if (whattogive.equals("change")) {
				break;
			}
		}
		
		//what will you take
		HashSet<String> takencards= new HashSet<String>();
		int wantedmoney=0;
		int takenmoney=0;
		while(true) {
			System.out.print(dealplayer.toString()+"\n");
			System.out.print("What do you want to take?( cards , money or change))\n");
			String whattotake=myobj.nextLine();
			if(whattotake.equals("cards")) {
				System.out.print("Which card do you want to take?\n");
				String card=myobj.nextLine();
				if (dealplayer.cards.containsKey(card)) {
					if(dealplayer.cards.get(card)==0) {
						takencards.add(card);
					}
					else {
						System.out.print("You can't exchange a card with houses. Choose an other one.\n");
					}	
				}
				else
					System.out.print(dealplayer.getname()+" doesn't have this card.\n");
			}
			else if (whattotake.equals("money")) {
				System.out.print("How much money do you want to exchange?\n");
				wantedmoney=Integer.parseInt(myobj.nextLine());
				if(wantedmoney<=dealplayer.getmoney())
					takenmoney=wantedmoney;
				else
					System.out.print(dealplayer.getname()+" doesn't have that much money.\n");
			}
			else if (whattotake.equals("change")) {
				break;
			}
		}
		
		//compute if there is a deal
		double givencost=computecost(givenmoney, givencards);
		double takencost=computecost(takenmoney, takencards);
		double result=givencost/takencost;
		if (result>1.1) {
			acceptthedeal(dealplayer, givenmoney, givencards, takenmoney, takencards);
			System.out.print("The deal was made. You took "+takenmoney+"$ and the cards:\n"+takencards.toString());
			System.out.print("\nAnd you gave "+givenmoney+"$ and the cards:\n"+givencards.toString());
		}
		else
			System.out.print(dealplayer.getname()+" didn't accept your deal.\n");
	}
	
	public void acceptthedeal(Player dealplayer,int givenmoney,HashSet<String> givencards,int takenmoney,HashSet<String> takencards) {
		this.money+=(takenmoney- givenmoney);
		dealplayer.money+=(givenmoney- takenmoney);
		for (String card:givencards) {
			dealplayer.cards.put(card, 0);
			this.cards.remove(card);
		}
		for (String card:takencards) {
			this.cards.put(card, 0);
			dealplayer.cards.remove(card);
		}		
	}
	
	public double computecost(int money, HashSet<String> cards) {
		double cost=money;
		if (cards!=null) {
			for (String cd:cards) {
				for (Card everycd:Maingame.Cards) {
					if(everycd.getname().equals(cd))
						cost+=(everycd.getcost()*1.2);
				}
			}
		}
		return cost;
	}
	
	public void sellhouse() {
		System.out.print("In which card will you sell a house?\n");
		String card=myobj.nextLine();
		if (!this.cards.containsKey(card)) {
			System.out.print("You don't have this card.\n");
			return;
		}
		int housecost=0;
		for (Card cd:Maingame.Cards) {
			if (cd.getname().equals(card)) {
				housecost=cd.gethousecost();
			}
		}
		int housesinthiscard = this.cards.get(card);
		if(housesinthiscard==0) {
			System.out.print("There are no houses in here to sell.\n");
		}
		else {
			System.out.print("A house here costs "+housecost+"$.\n");
			this.cards.put(card, housesinthiscard-1);
			this.money+=housecost;
			System.out.print(this.getname()+" sold a house in this card!So now you have "+this.cards.get(card)+" house(s) in here.\n");
		}
	}
	
	public boolean whattodo(String move) {
		if (move.equals("buy this card")) {
			buycard();
		}
		else if (move.equals("buy a house")) {
			buyhouse();
		}
		else if (move.equals("sell a house")){
			sellhouse();
		}
		else if (move.equals("make a deal")){
			makedeal();
		}
		else if (move.equals("players stats")) {
			System.out.print("Whose stats do you want to see?");
			String wantedplayer=myobj.nextLine();
			for (Player pl:Maingame.player) {
				if (pl.getname().equals(wantedplayer))
					System.out.print(pl.toString());
			}
		}
		else if(move.equals("card stats")) {
			System.out.print("Which card's stats do you want to see?\n");
			String card=myobj.nextLine();
			int wantedindex=Arrays.asList(Maingame.listofplaces).indexOf(card);
			if(wantedindex!=-1) {
				Card wantedcard=Maingame.Cards.get(wantedindex);
				wantedcard.getstats();
			}
			else
				System.out.print("There is no card with that name.\n");
		}
		else if (move.equals("end my turn")) {
			return true;
		}
		return false;
	}

	
	public void jailplace(int jailpl, int gotojailpl) {
		if(this.place==gotojailpl) {
			this.place=jailpl;
			System.out.print(this.name+" is going to Jail and pays 50$ to get out of it.\n");
			this.money-=50;
		}
		
	}
	
	public void throwthedices() {
		int dice1=rand.nextInt(6)+1;
		int dice2=rand.nextInt(6)+1;
		int dices=dice1+dice2;
		if (this.place+dices<Maingame.listofplaces.length) {
			this.place+=dices;
		}
		else {
			this.place=this.place+dices-Maingame.listofplaces.length;
			//after you pass the START
			System.out.print(this.name + " passed the start and got 200$.\n");
			this.money+=200;
		}
		System.out.print(this.name + "'s dices showed [ "+dice1+" , "+dice2+" ] and move(s) on "+ Maingame.listofplaces[this.place]+". It costs "+Maingame.Cards.get(this.place).getcost()+"$.\n");
		jailplace(Maingame.jailplace,Maingame.gotojailplace);
		
	}
	
	public void payme(Player other, String card) {
		Card thiscard=Maingame.Cards.get(other.getplace());
		int[] stats=thiscard.getpayment();
		if (this.name!=other.getname()) {
			if(this.cards.containsKey(card)) {
				int payment=stats[this.cards.get(card)];
				System.out.print(other.name + " paid "+ this.name + " "+ payment +"$.\n");
				other.money-=payment;
				this.money+=payment;
			}
		}
	}


	public void compmove() {
		this.throwthedices();
		for (Player other:Maingame.player) {
			other.payme(this, Maingame.listofplaces[this.getplace()]);
		}
		//buy the card
		Card thiscard=Maingame.Cards.get(this.place);
		if (thiscard.getmode()) {
			boolean flag=false;
			for(Player otherpl:Maingame.player) {
					if(otherpl.cards.containsKey(thiscard.getname())) {
						flag=true;
						break;
					}
			}
			if (!flag) {
				int cost=thiscard.getcost();
				if(this.money>cost) {
					this.cards.put(thiscard.getname(), 0);
					this.money-=cost;
					System.out.print(this.getname()+" bought this card!So now "+this.getname()+" has the following cards:\n"+this.cards.keySet()+"\n");
				}
			}
		}
		
		
		//buy house
		for (String owncards:this.cards.keySet()) {
			String wantedcolor="";
			int housecost=0;
			for(Card eachcard:Maingame.Cards) {
				if(owncards.equals(eachcard.getname())) {
					wantedcolor=eachcard.getcolor();
					housecost=eachcard.gethousecost();
				}
			}
			HashMap<String,Integer> cardswiththiscolor= new HashMap<String,Integer>();
			for (Card cd:Maingame.Cards) {
				if(wantedcolor.equals(cd.getcolor())) {
					if (this.cards.containsKey(cd.getname())) {
						cardswiththiscolor.put(cd.getname(), this.cards.get(cd.getname()));
					}
					else
						cardswiththiscolor.put(cd.getname(), 0);
				}
			}
			int countallcards=0;
			for (String cdcl:cardswiththiscolor.keySet()) {
				if (this.cards.containsKey(cdcl)) {
					countallcards++;
				}

				
			}
			if (countallcards==cardswiththiscolor.size()) {
				System.out.print("A house here costs "+housecost+"$.\n");
				int count5houses=0;
				while(true) {
					for (String colcards:cardswiththiscolor.keySet()) {
						int housesinthiscard = this.cards.get(colcards);
						boolean canbuildfromnumbofhouses=true;
						for (String cdcl:cardswiththiscolor.keySet()) {
							if(cardswiththiscolor.get(colcards)>cardswiththiscolor.get(cdcl)) {
								canbuildfromnumbofhouses=false;
							}
						}
						if(!canbuildfromnumbofhouses) {
							continue;
						}
						
						if(this.money>housecost && housesinthiscard<5) {
							
							this.cards.put(colcards, housesinthiscard+1);
							this.money-=housecost;
							System.out.print(this.getname()+" bought a house in "+colcards+ "!So now this player has "+this.cards.get(colcards)+" house(s) in here.\n");
						}
						if(housesinthiscard==5)
							count5houses++;
					}
					if(count5houses==countallcards || this.money<housecost)
						break;
				}
			}
		}
	}
}
