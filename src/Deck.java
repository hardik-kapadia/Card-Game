import java.util.*;

public class Deck {

    private String[] allCards;
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
            allCards[i] += valueAssigner + 1;
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
            for(int i=0;i<numberOfCards*numberOfPlayers;i++){
                while(true){
                    int card = r.nextInt(cardsLeft);
                    int player = r.nextInt(numberOfPlayers);
                    if(playerDecks[player].size()<numberOfCards){
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

    public void resetDeck(){
        createDeck();
        for(int i=0;i<numberOfPlayers;i++){
            playerDecks[i].clear();
        }
    }

    private void remove(int index){
        for(int i = index;i<cardsLeft-1;i++){
            this.allCards[i] = this.allCards[i+1];
        }
        this.allCards[cardsLeft-1] = null;
        this.cardsLeft--;
    }

    public String indexToCard(int index){
        return allCards[index];
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String[] returnFullDeck() {
        return allCards;
    }
}
