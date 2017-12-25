import java.util.ArrayList;

public class Player extends Person {
	private String name;

	private int chips;

	private int bet;

	private ArrayList<Card> oneRoundCard=new ArrayList<Card>();

	public ArrayList<Card> oneRoundCard() {
		return oneRoundCard;
	}

	public player(String name, int chips) {
		this.name = name;
		this.chips = chips;
	}

	public String getName() {
		return name;
	}

	public int makeBet() {
		 bet = 0;
		if (chips != 0) {
			bet = 1;
		}
		return bet;
	}

	public void setOneRoundCard(ArrayList<Card> cards) {
		oneRoundCard = cards;
	}

	public boolean hitMe() { // ­n±Æ

		if (getTotalValue() > 16) {
			return false;
		} else {
			return true;
		}
	}

	public int getTotalValue() {
		int sum = 0;
		int value = 0;
		for (int i = 0; i < oneRoundCard.size(); i++) {
			Card a = oneRoundCard.get(i);
			value = a.getRank();
			if (value > 10 && value < 14) {
				value = 10;
			}
			sum = sum + value;
			if(value==1||sum<=11){
				sum+=10;
			}
			
		}

		return sum;
	}

	public int getCurrentChips() {
		return chips;
	}

	public void increaseChips(int diff) {
		chips += diff;

	}

	public void sayHello() {
		System.out.println("Hello, I am " + name + ".");
		System.out.println("I have " + chips + " chips.");

	}
}
