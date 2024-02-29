package card.game;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private final int numberOfPlayers;
    private final String[] allCards;
    private final ArrayList<String>[] playerDecks;
    private int cardsLeft;

    public Deck(int numberOfPlayers) { // Initializations
        allCards = new String[52];
        this.numberOfPlayers = numberOfPlayers;
        playerDecks = new ArrayList[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            playerDecks[i] = new ArrayList<>();
        }
        createDeck(); // creating a deck
    }

    public void createDeck() { // creates a deck of 52 cards in the format h3, d9, s14, c6
        for (int i = 0; i < 52; i++) {
            int symbolAssigner, valueAssigner;
            symbolAssigner = i / 13; // first 13 cards will be assigned 0 (h), the next will be assigned 1 (d)
            valueAssigner = i % 13; // first card is 2, second is 3 till 13th card is 14(A) and then reset back to 2 for next symbol
            allCards[i] = switch (symbolAssigner) {
                case 0 -> "h";
                case 1 -> "d";
                case 2 -> "s";
                case 3 -> "c";
                default -> "n";
            };
            allCards[i] += valueAssigner + 2;
        }
        cardsLeft = 52; // cardsLeft in deck (initially 52)
    }

    public void deal() { //deals all cards among the numberOfPlayers
        Random r = new Random();
        for (int i = 0; i < 52; i++) {
            int player = r.nextInt(numberOfPlayers); // randomly selects a player
            if (playerDecks[player].size() < 13) {
                playerDecks[player].add(allCards[i]); // assigned card to said player
            } else {
                i--;
            }
        }
    }

    /* not being used in current game
    public void deal(int numberOfCards) { // distributes specified number of cards among the people
        if (numberOfCards * numberOfPlayers > 52) {
            System.out.println("Not enough cards");
        } else {
            Random r = new Random();
            for (int i = 0; i < numberOfCards * numberOfPlayers; i++) {
                while (true) {
                    int card = r.nextInt(cardsLeft); // randomly selects a card from the card left
                    int player = r.nextInt(numberOfPlayers); // randomly selects a player
                    if (playerDecks[player].size() < numberOfCards) { // adds card to player's deck if player has less than maximum cards
                        playerDecks[player].add(allCards[card]);
                        remove(card); // remove card from full deck
                        break;
                    }
                }
            }
        }
    }
     */

    public ArrayList<String>[] getPlayerDecks() {
        return playerDecks;
    } // returns an Array of ArrayLists containing each Player's cards

    public void resetDeck() { // resets the full deck to its original 52 cards and empties all the player's decks
        createDeck();
        for (int i = 0; i < numberOfPlayers; i++) {
            playerDecks[i].clear();
        }
    }

    /* Not being used currently
    private void remove(int index) { // removes card from the full deck
        if (cardsLeft - 1 - index >= 0)
            System.arraycopy(this.allCards, index + 1, this.allCards, index, cardsLeft - 1 - index);
        this.allCards[cardsLeft - 1] = null;
        this.cardsLeft--;
    }
    */

    /*

    public String indexToCard(int index) {
        return allCards[index];
    } // return card corresponding to given index

    public int cardToIndex(String card) { // returns index corresponding to given card
        int cardNum = switch (card.charAt(0)) {
            case 'h' -> 0;
            case 'd' -> 13;
            case 's' -> 26;
            case 'c' -> 39;
            default -> 53;
        };
        int cardVal = Integer.parseInt(card.substring(1)) - 2;
        cardNum += cardVal;
        return cardNum;
    }
    */

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    } // returns number of players

   /* public String[] returnFullDeck() {
        return allCards;
    }
     // returns all the cards
    */
}
