import java.util.*;
import java.io.*;

public class Maingame {
	private static Scanner myobj= new Scanner(System.in);
	public static ArrayList<Card> Cards;
	public static String[] listofplaces; 
	private static Random rand=new Random();
	public static Player[] player;
	public static int jailplace; 
	public static int gotojailplace; 
	
	
	public static int numberofplayers() {
		int num=0;
		while(num<2 || num>8) {
			try {
				System.out.print("The game has 2 to 8 players. How many players do you want?\n");
				num= Integer.parseInt(myobj.nextLine());
			}catch(NumberFormatException ex){
				System.out.println("Invalid input");
			}
		}
		return num;
	}
	
	public static ArrayList<Card> makethecards(){
		ArrayList<Card> cardlist= new ArrayList<Card>();
		String[] colors= {"red", "green", "blue", "velvet", "orange", "pink", "yellow", "brown"};
		int cl=0;
		//numbs is {cost, payment with no houses, with 1, with 2, 3, 4, 5}
		for (int i=0; i<listofplaces.length;i++) {
			if(listofplaces[i].equals("START") || listofplaces[i].equals("JAIL") || listofplaces[i].equals("GO TO JAIL") ) {
				int[] nullstats= {0, 0, 0, 0, 0, 0};
				cardlist.add(new Card(listofplaces[i], "no color", 0, nullstats, false));
				continue;
			}
			int[] numbs={i*5+10, i*10+20, i*15+30, i*20+40, i*25+50, i*30+100};
			cardlist.add(new Card(listofplaces[i], colors[cl], i*10+100, numbs, true));
			if (i%3==2) {
				cl++;
			}
		}
		return cardlist;
	}
	
	public static String[] makeplaces() {
		String[] list= {"START", "Thessaloniki", "Athens", "Volos", "Chania", "Xanthi", "JAIL", "Kilkis", "Aridea", "Corfu", "Ikaria", "Crete", "Andros", "Zagorochoria", "Meteora", "GO TO JAIL", "Larisa", "Peraia"};
		return list;
	}
	
	public static void whatmove() {
		System.out.print("What is your move?(buy this card, buy a house, sell a house, make a deal, players stats, card stats, end my turn)\n");
		String move=myobj.nextLine();
		while(!(player[0].whattodo(move))) {
			System.out.print("You are in "+listofplaces[player[0].getplace()]+". What is your move?(buy this card, buy a house, sell a house, make a deal, players stats, card stats, end my turn)\n");
			move=myobj.nextLine();
		}
	}
	
	public static Player[] makingcompplayers(int numofplayers) {
		List<String> names=new ArrayList<String>();
		String[] names2= {"Maria", "John", "George", "Tania", "Teo", "Jina", "Paris", "Lia", "Ron", "Harry", "Otto"}; 
		Collections.addAll(names, names2);
		Player[]  playerlist= new Player[numofplayers];
		for (int i=1;i<numofplayers;i++) {
			int thisname=rand.nextInt(names.size());
			playerlist[i]=new Player(names.get(thisname));
			names.remove(thisname);
			System.out.print(playerlist[i].toString());
		}
		return playerlist;
	}
	
	public static void main(String[] args) {
		int numofplayers=numberofplayers();
		player= makingcompplayers(numofplayers); 
		listofplaces = makeplaces();
		
		for (int i=0;i<listofplaces.length;i++) {
			if (listofplaces[i].equals("JAIL"))
				jailplace=i;
			if (listofplaces[i].equals("GO TO JAIL"))
				gotojailplace=i;
		}

		Cards=makethecards();
		System.out.print("What is your name?\n");
		String myname= myobj.nextLine();
		player[0]= new Player(myname);
		int countdeaths=0;
		while(true) {
			//show the table
			for(String table:listofplaces) {
				System.out.print(table+" ");
			}
			System.out.print("\n");
			player[0].throwthedices();
			//check if you pay anyone
			for (Player other:player) {
				other.payme(player[0], listofplaces[player[0].getplace()]);
			}
			whatmove();
			
			//computerplayers' turn
			for (int pl=1;pl<numofplayers;pl++) {
				player[pl].compmove();
			}
			
			//check for the win
			if(player[0].isbroke()) {
				System.out.print("\nYou are broke! Game over!");
				break;
			}
			for (int i=1;i<numofplayers;i++) {
				if (player[i].isbroke()) {
					countdeaths++;
					System.out.print("\n"+player[i].getname()+" is broke!\n");
					for (int j = i; j < player.length - 1; j++) {
						player[i] = player[i + 1];
					}
					numofplayers-=1;
				}
			}
			if (numofplayers==1) {
				System.out.print("\nYou are the last player! You WON!");
				break;
			}
			
		}
	}
}
