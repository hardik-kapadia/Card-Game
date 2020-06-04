import java.util.*;

public class Deck {

    private String[] allCards;
    private int[] allCardsNum;
    private ArrayList<String>[] playerDecks;
    private int numberOfPlayers;
    private int cardsLeft;

    public Deck(int numberOfPlayers) {
        allCards = new String[52];
        this.numberOfPlayers = numberOfPlayers;
        playerDecks = new ArrayList[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            playerDecks[i] = new ArrayList<>();
        }
        createDeck();
    }

    public void createDeck() {
        for (int i = 0; i < 52; i++) {
            int symbolAssigner, valueAssigner;
            symbolAssigner = i / 13;
            valueAssigner = i % 13;
            switch (symbolAssigner) {
                case 0:
                    allCards[i] = "h";
                    break;
                case 1:
                    allCards[i] = "d";
                    break;
                case 2:
                    allCards[i] = "s";
                    break;
                case 3:
                    allCards[i] = "c";
                    break;
                default:
                    continue;
            }
            allCards[i] += valueAssigner + 2;
        }
        cardsLeft = 52;
    }

    public void deal() {
        Random r = new Random();
        for (int i = 0; i < 52; i++) {
            int player = r.nextInt(4);
            if (playerDecks[player].size() < 13) {
                playerDecks[player].add(allCards[i]);
            } else {
                i--;
            }
        }
    }

    public void deal(int numberOfCards) {
        if (numberOfCards * numberOfPlayers > 52) {
            System.out.println("Not enough cards");
        } else {
            Random r = new Random();
            for (int i = 0; i < numberOfCards * numberOfPlayers; i++) {
                while (true) {
                    int card = r.nextInt(cardsLeft);
                    int player = r.nextInt(numberOfPlayers);
                    if (playerDecks[player].size() < numberOfCards) {
                        playerDecks[player].add(allCards[card]);
                        remove(card);
                        break;
                    }
                }
            }
        }
    }

    public ArrayList<String>[] getPlayerDecks() {
        return playerDecks;
    }

    public void resetDeck() {
        createDeck();
        for (int i = 0; i < numberOfPlayers; i++) {
            playerDecks[i].clear();
        }
    }

    private void remove(int index) {
        if (cardsLeft - 1 - index >= 0)
            System.arraycopy(this.allCards, index + 1, this.allCards, index, cardsLeft - 1 - index);
        this.allCards[cardsLeft - 1] = null;
        this.cardsLeft--;
    }

    public String indexToCard(int index) {
        return allCards[index];
    }

    public int cardToIndex(String card) {
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String[] returnFullDeck() {
        return allCards;
    }
}
