
import java.io.PrintStream;
import java.util.ArrayList;

public class Table
{
  public static final int MAXPLAYER = 4;
  private Deck deck;
  private Player[] players;
  private Dealer dealer;
  private int[] pos_betArray;
  private int nDecks;
  
  public Table(int nDecks)
  {
    this.nDecks = nDecks;
    this.deck = new Deck(nDecks);
    this.players = new Player[4];
  }
  
  public void play()
  {
    ask_each_player_about_bets();
    distribute_cards_to_dealer_and_players();
    ask_each_player_about_hits();
    ask_dealer_about_hits();
    calculate_chips();
  }
  
  public void set_player(int pos, Player p)
  {
    if ((pos >= 0) && (pos <= this.players.length)) {
      this.players[pos] = p;
    }
  }
  
  public Player[] get_player()
  {
    return this.players;
  }
  
  public void set_dealer(Dealer d)
  {
    this.dealer = d;
  }
  
  public Card get_face_up_card_of_dealer()
  {
    if (this.dealer != null)
    {
      Card dealersFaceUpCard = (Card)this.dealer.getOneRoundCard().get(0);
      return dealersFaceUpCard;
    }
    System.out.println("Sorry!! There is no Dealer!");
    return null;
  }
  
  public ArrayList<Card> getOpenedCards()
  {
    return this.deck.getOpenedCard();
  }
  
  public int getNumberOfDeck()
  {
    return this.nDecks;
  }
  
  private void ask_each_player_about_bets()
  {
    this.pos_betArray = new int[this.players.length];
    for (int i = 0; i < this.players.length; i++) {
      if (this.players[i] != null)
      {
        this.players[i].say_hello();
        int bet = this.players[i].make_bet();
        if (bet > this.players[i].get_current_chips())
        {
          this.players[i].setBet(0);
          this.pos_betArray[i] = 0;
        }
        else
        {
          this.pos_betArray[i] = this.players[i].make_bet();
        }
      }
    }
  }
  
  private void distribute_cards_to_dealer_and_players()
  {
    for (int i = 0; i < this.players.length; i++) {
      if ((this.players[i] != null) && (this.pos_betArray[i] != 0))
      {
        ArrayList<Card> playersCard = new ArrayList();
        playersCard.add(this.deck.getOneCard(true));
        playersCard.add(this.deck.getOneCard(true));
        this.players[i].setOneRoundCard(playersCard);
      }
    }
    if (this.dealer != null)
    {
      ArrayList<Card> dealersCard = new ArrayList();
      dealersCard.add(this.deck.getOneCard(true));
      dealersCard.add(this.deck.getOneCard(false));
      this.dealer.setOneRoundCard(dealersCard);
      System.out.print("Dealer's face up card is ");
      Card dealers_face_up_card = get_face_up_card_of_dealer();
      dealers_face_up_card.printCard();
    }
  }
  
  private void ask_each_player_about_hits()
  {
    for (int i = 0; i < this.players.length; i++) {
      if ((this.players[i] != null) && (this.pos_betArray[i] != 0))
      {
        System.out.print(this.players[i].get_name() + "'s Cards now:");
        this.players[i].printAllCard();
        hit_process(i, this.players[i].getOneRoundCard());
        System.out.println(this.players[i].get_name() + "'s hit is over!");
      }
    }
  }
  
  private void hit_process(int pos, ArrayList<Card> cards)
  {
    boolean hit;
    do
    {
      hit = this.players[pos].hit_me(this);
      if (hit)
      {
        cards.add(this.deck.getOneCard(true));
        this.players[pos].setOneRoundCard(cards);
        System.out.print("Hit! ");
        System.out.print(this.players[pos].get_name() + "'s Cards now:");
        this.players[pos].printAllCard();
        if (this.players[pos].getTotalValue() > 21) {
          hit = false;
        }
      }
      else
      {
        System.out.println("Pass hit!");
      }
    } while (hit);
  }
  
  private void ask_dealer_about_hits()
  {
    ArrayList<Card> cards = this.dealer.getOneRoundCard();
    boolean hit;
    do
    {
      hit = this.dealer.hit_me(this);
      if (hit)
      {
        cards.add(this.deck.getOneCard(true));
        this.dealer.setOneRoundCard(cards);
      }
      if (this.dealer.getTotalValue() > 21) {
        hit = false;
      }
    } while (hit);
    System.out.println("Dealer's hit is over!");
  }
  
  private void calculate_chips()
  {
    int dealersCradValue = this.dealer.getTotalValue();
    System.out.print("Dealer's card value is " + dealersCradValue + " ,Cards:");
    this.dealer.printAllCard();
    for (int i = 0; i < this.players.length; i++) {
      if ((this.players[i] != null) && (this.pos_betArray[i] != 0))
      {
        System.out.print(this.players[i].get_name() + "'s Cards: ");
        this.players[i].printAllCard();
        caculate_process(i);
      }
    }
  }
  
  private void caculate_process(int pos)
  {
    System.out.print(this.players[pos].get_name() + " card value is " + this.players[pos].getTotalValue());
    if (this.players[pos].getTotalValue() > 21)
    {
      if (this.dealer.getTotalValue() > 21)
      {
        System.out.println(", chips have no change!, the Chips now is: " + this.players[pos].get_current_chips());
      }
      else
      {
        this.players[pos].increase_chips(-this.pos_betArray[pos]);
        System.out.println(", Loss " + this.pos_betArray[pos] + " Chips, the Chips now is: " + this.players[pos].get_current_chips());
      }
    }
    else if (this.players[pos].getTotalValue() == 21)
    {
      if ((this.players[pos].getOneRoundCard().size() == 2) && (this.players[pos].hasAce()))
      {
        if (this.dealer.getTotalValue() != 21)
        {
          this.players[pos].increase_chips(this.pos_betArray[pos] * 2);
          System.out.println(",Black jack!!! Get " + this.pos_betArray[pos] + " Chips, the Chips now is: " + this.players[pos].get_current_chips());
        }
        else if ((this.dealer.getOneRoundCard().size() == 2) && (this.dealer.hasAce()))
        {
          System.out.println(",Black Jack!!!! But chips have no change!, the Chips now is: " + this.players[pos].get_current_chips());
        }
        else
        {
          this.players[pos].increase_chips(this.pos_betArray[pos] * 2);
          System.out.println(",Black jack!!! Get " + this.pos_betArray[pos] + " Chips, the Chips now is: " + this.players[pos].get_current_chips());
        }
      }
      else if (this.dealer.getTotalValue() != 21)
      {
        this.players[pos].increase_chips(this.pos_betArray[pos] * 2);
        System.out.println(",Get " + this.pos_betArray[pos] + " Chips, the Chips now is: " + this.players[pos].get_current_chips());
      }
      else
      {
        System.out.println(",chips have no change!The Chips now is: " + this.players[pos].get_current_chips());
      }
    }
    else if (this.dealer.getTotalValue() > 21)
    {
      this.players[pos].increase_chips(this.pos_betArray[pos]);
      System.out.println(", Get " + this.pos_betArray[pos] + " Chips, the Chips now is: " + this.players[pos].get_current_chips());
    }
    else if (this.dealer.getTotalValue() < this.players[pos].getTotalValue())
    {
      this.players[pos].increase_chips(this.pos_betArray[pos]);
      System.out.println(", Get " + this.pos_betArray[pos] + " Chips, the Chips now is: " + this.players[pos].get_current_chips());
    }
    else if (this.dealer.getTotalValue() > this.players[pos].getTotalValue())
    {
      this.players[pos].increase_chips(-this.pos_betArray[pos]);
      System.out.println(", Loss " + this.pos_betArray[pos] + " Chips, the Chips now is: " + this.players[pos].get_current_chips());
    }
    else
    {
      System.out.println(", chips have no change! The Chips now is: " + this.players[pos].get_current_chips());
    }
  }
  
  public int[] get_palyers_bet()
  {
    return this.pos_betArray;
  }
  
  public double getPercentofUsedCard()
  {
    return this.deck.nUsed / this.deck.getAllCards().size();
  }
}


