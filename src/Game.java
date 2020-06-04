import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    private final Deck deck;
    private final Scanner scanner;
    private String[] playerNames;
    private Player[] players;
    private int numberOfPlayers;
    private int handStarter;
    private ArrayList<String> currentHand;
    private Player winner;

    public Game(Deck deck, Scanner sc) {
        this.deck = deck;
        this.scanner = sc;
        this.numberOfPlayers = this.deck.getNumberOfPlayers();
        players = new Player[numberOfPlayers];
        playerNames = new String[numberOfPlayers];
        handStarter = 0;
    }

    public void getNames() {
        for (int i = 0; i < deck.getNumberOfPlayers(); i++) {
            System.out.println("Enter your name: [leave blank to replace with bot]");
            String name = scanner.nextLine();
            if (name.equals("")) {
                playerNames[i] = ("Bot " + i);
            } else {
                playerNames[i] = (name);
            }
        }
    }

    public void startGame() {
        getNames();
        deck.deal();
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(deck.getPlayerDecks()[i], playerNames[i]);
        }
        currentHand = new ArrayList<>();
        handStarter = 0;
        startHand(handStarter);
    }

    public void startHand(int hStarter) {
        if (checkGameStats()) {
            int symbol = 4;
            for (int i = 0; i < numberOfPlayers; i++) {
                String play = promptUser(players[(hStarter + i) % 4]);
                if (symbol == 4) symbol = symbolToNum(play.charAt(0));
                else if (playValidity(players[(hStarter + i) % 4], play, symbol)) {

                } else {
                    System.out.println("Please play a valid card");
                    i--;
                }
            }
        } else {
            endGame();
        }
    }

    public boolean playValidity(Player player, String play, int symbol) {
        if (symbolToNum(play.charAt(0)) == symbol) {
            return true;
        }
        for (int i = 0; i < player.getPlayerCards().size(); i++) {
            if (symbolToNum(player.getPlayerCards().get(i).charAt(0)) == symbol) {
                return false;
            }
        }
        return true;
    }

    public int symbolToNum(char symbol) {
        return switch (symbol) {
            case 'h' -> 0;
            case 'd' -> 1;
            case 's' -> 2;
            case 'c' -> 3;
            default -> 4;
        };
    }

    public String promptUser(Player player) {
        System.out.println("The current hand is: " + currentHand);
        System.out.println("Your cards are: ");
        for (int i = 0; i < player.getPlayerCards().size(); i++) {
            System.out.print("1. " + player.getPlayerCards().get(i) + "\t");
        }
        System.out.println("Enter number corresponding to card you want to play: ");
        int cardSelected = scanner.nextInt();
        return player.getPlayerCards().get(cardSelected);
    }

    public boolean checkGameStats() {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (!checkPoints(players[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPoints(Player player) {
        return player.getPoints() < 100;
    }

    public void sortPlayersByPoints() {
        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = 0; j < numberOfPlayers - i - 1; j++) {
                if (players[j].getPoints() > players[j + 1].getPoints()) {
                    Player temp = players[j];
                    players[j] = players[j + 1];
                    players[j + 1] = temp;
                }
            }
        }
    }

    public void endGame() {
        sortPlayersByPoints();
        printScoreCard();
        deck.resetDeck();
    }

    public void printScoreCard() {
        boolean first = true;
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.print(i + ". ");
            if (first) {
                System.out.print(" (Winner)");
                first = false;
            }
            System.out.println("");
        }
    }
}
