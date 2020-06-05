import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private final Deck deck;
    private final Scanner scanner;
    private String[] playerNames;
    private Player[] players;
    private int numberOfPlayers;
    private int handStarter;
    private String[] currentHand;
    private Player winner;

    public Game(Deck deck, Scanner sc) {
        this.deck = deck;
        this.scanner = sc;
        this.numberOfPlayers = this.deck.getNumberOfPlayers();
        players = new Player[numberOfPlayers];
        playerNames = new String[numberOfPlayers];
        handStarter = 0;
        currentHand = new String[numberOfPlayers];
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
            players[i] = new Player(deck.getPlayerDecks()[i], playerNames[i], i);
        }
        handStarter = 0;
        emptyArray(currentHand);
        startHand(handStarter);
    }

    public Player[] getPlayers() {
        return players;
    }

    public void startHand(int hStarter) {
        if (checkGameStats()) {
            int symbol = 4;
            for (int i = 0; i < numberOfPlayers; i++) {
                int currentPlayer = (hStarter + i) % 4;
                String play = promptUser(players[currentPlayer]);
                if (symbol == 4) symbol = getSymbolAndValue(play)[0];
                else if (playValidity(players[(hStarter + i) % 4], play, symbol)) {
                    currentHand[players[currentPlayer].getId()] = play;
                } else {
                    System.out.println("Please play a valid card");
                    i--;
                }
            }
            addHandPoints(currentHand, getHandWinner(currentHand, symbol));
        } else {
            endGame();
        }

    }

    public Player getHandWinner(String[] handAtPlay, int symbol) {
        int winner = 0;
        for (int i = 0; i < numberOfPlayers; i++) {
            int[] symbolAndValue = getSymbolAndValue(handAtPlay[i]);
            if (symbolAndValue[0] == symbol) {
                if (symbolAndValue[1] > getSymbolAndValue(currentHand[winner])[1]) {
                    winner = i;
                }
            }
        }
        return players[winner];
    }

    public void addHandPoints(String[] handAtPlay, Player winner) {
        for (String s : handAtPlay) {
            int[] symbolAndValue = getSymbolAndValue(s);
            if (symbolAndValue[0] == 0) {
                winner.addPoint();
            } else if (symbolAndValue[0] == 2 && symbolAndValue[1] == 12) {
                winner.addPoints(13);
            }
        }
    }

    private int[] getSymbolAndValue(String card) {
        // returns an array [symbol, value] according to specified legend
        return new int[]{symbolToNum(card.charAt(0)), Integer.parseInt(card.substring(1))};
    }

    private void emptyArray(String[] arrayToEmpty) {
        // replaces all the values in specified array to null, therefore emptying the array
        Arrays.fill(arrayToEmpty, null);
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

    public String promptUser(@NotNull Player player) {
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

    /*
    public ArrayList<String> properHand(ArrayList<String> cards){
        for(String s: cards){
            int symbol = getSymbolAndValue(s)[0];
            int value = getSymbolAndValue(s)[1];
            String card = "";

        }
    }
    */

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