import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private final Deck deck;
    private final Scanner scanner;
    private String[] playerNames;
    private Player[] players;
    private int numberOfPlayers;
    private Player handStarter;
    private String[] currentHand;
    private int handCount;
    private int roundCount;
    private boolean canPlayHearts;
    private boolean isFirstCard;
    private int positionOfFirstCard;

    public Game(Deck deck, Scanner sc) {
        this.deck = deck;
        this.scanner = sc;
        this.numberOfPlayers = this.deck.getNumberOfPlayers();
        players = new Player[numberOfPlayers];
        playerNames = new String[numberOfPlayers];
        currentHand = new String[numberOfPlayers];
    }

    public void getNames() {
        for (int i = 0; i < deck.getNumberOfPlayers(); i++) {
            System.out.println("Enter your name: [leave blank to replace with bot]");
            String name = scanner.nextLine();
            if (name.equals("")) {
                playerNames[i] = ("Bot-" + (i + 1));
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
            // System.out.println("Deck assigned to "+players[i].getName());
        }
        this.roundCount = 1;
        startRound();
    }

    public void startRound() {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("-------------------------------------------------------");
        System.out.println("\nRound no. " + roundCount + " started");
        if (checkGameStats()) {
            // System.out.println("checkGameStats() Passed");
            deck.resetDeck();
            deck.deal();
            for (int i = 0; i < numberOfPlayers; i++) {
                players[i].setPlayerCards(deck.getPlayerDecks()[i]);
            }
            this.canPlayHearts = false;
            this.isFirstCard = true;
            this.handStarter = firstStarter();
            this.positionOfFirstCard = 0;
            emptyArray(currentHand);
            handCount = 1;
            startHand(handStarter);
        } else {
            endGame();
        }
    }

    private Player firstStarter() {
        for (Player p : players) {
            if (p.getPlayerCards().contains("c2")) {
                return p;
            }
        }
        return players[0];
    }

    public void startHand(Player hStarter) {
        if (handCount <= 13) {
            emptyArray(currentHand);
            System.out.println("\n-------------------------------------------------------");
            System.out.println("\nStarting hand number: " + handCount);
            int symbol = 4;
            for (int i = 0; i < numberOfPlayers; i++) {
                Player currentPlayer = players[(hStarter.getId() + i) % 4];
                int playNum = promptUser(currentPlayer, symbol);
                String play = currentPlayer.getPlayerCards().get(playNum);
                System.out.println("The card you have selected is: " + getPrintableCard(play));
                if (symbol == 4) {
                    symbol = getSymbolAndValue(play)[0];
                    if (symbol == 0) {
                        if (!this.canPlayHearts) {
                            symbol = 4;
                            i--;
                            System.out.println("You Cannot play a Heart Card till you play it as a foke first");
                            continue;
                        }
                    }
                    System.out.println("New hand started, symbol assigned: " + numToString(getSymbolAndValue(play)[0]));
                }
                if (playValidity(currentPlayer, play, symbol)) {
                    currentPlayer.removeCard(playNum);
                    currentHand[currentPlayer.getId()] = play;
                } else {
                    System.out.println("\n Please play a valid card\n");
                    i--;
                }
            }
            System.out.println("\nFinal hand: " + Arrays.toString(getPrintableHand(currentHand)));
            Player nextHandStarter = getHandWinner(currentHand, symbol, hStarter);
            System.out.println(nextHandStarter.getName() + " got the hand");
            addHandPoints(currentHand, nextHandStarter);
            handCount++;
            System.out.println("\n-------------------------------------------------------\n");
            System.out.print("ScoreCard: \n\n");
            printScoreCard();
            startHand(nextHandStarter);
        } else {
            startRound();
        }
    }

    public int promptUser(@NotNull Player player, int symbol) {
        System.out.println("\n|\tIts is " + player.getName() + "'s turn\t|\n");
        System.out.println("The current hand is: ");
        String[] printingHand = getPrintableHand(currentHand);
        for(int j =0;j<numberOfPlayers;j++){
            System.out.print(players[j].getName()+": "+printingHand[j]+"\t");
        }
        System.out.println();
        // + Arrays.toString());
        if (symbol != 4) System.out.println("Ongoing Symbol is: " + numToString(symbol)+"\n");
        System.out.println("Your cards are: ");
        for (int i = 0; i < player.getPlayerCards().size(); i++) {
            System.out.print("(" + i + ") " + getPrintableCard(player.getPlayerCards().get(i)) + "\t");
            if(i==7){
                System.out.println();
            }
        }
        if (isFirstCard) {
            System.out.println("\nThis is the first play, so the 2 of clubs is automatically selected for you.");
            isFirstCard = false;
            for (int i = 0; i < player.getPlayerCards().size(); i++) {
                if (player.getPlayerCards().get(i).equals("c2")) {
                    return i;
                }
            }
        }
        System.out.print("\n\nEnter number corresponding to card you want to play: ");
        return scanner.nextInt();
    }



    public Player getHandWinner(String[] handAtPlay, int symbol, @NotNull Player starter) {
        Player winner = starter;
        for (Player p : players) {
            int[] symbolAndValue = getSymbolAndValue(handAtPlay[p.getId()]);
            if (symbolAndValue[0] != symbol) {
                continue;
            }
            if (symbolAndValue[1] < Integer.parseInt(handAtPlay[winner.getId()].substring(1))) {
                continue;
            }
            winner = p;
        }
        return winner;
    }

    public void addHandPoints(String[] handAtPlay, Player winner) {
        int totalPointsGiveToWinner = 0;
        for (String s : handAtPlay) {
            int[] symbolAndValue = getSymbolAndValue(s);
            if (symbolAndValue[0] == 0) {
                totalPointsGiveToWinner++;
            } else if (symbolAndValue[0] == 2 && symbolAndValue[1] == 12) {
                totalPointsGiveToWinner += 13;
            }
        }
        if (totalPointsGiveToWinner == 26) {
            for (Player player : players) {
                if (!winner.equals(player)) {
                    player.addPoints(26);
                }
            }
        } else {
            winner.addPoints(totalPointsGiveToWinner);
        }
        System.out.println(totalPointsGiveToWinner + " points awarded to " + winner.getName() + "\n");
    }

    private int[] getSymbolAndValue(@NotNull String card) {
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

        if (symbol == 0) this.canPlayHearts = true;

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

    private String numToString(int symbol) {
        return switch (symbol) {
            case 0 -> "Hearts";
            case 1 -> "Diamonds";
            case 2 -> "Spades";
            case 3 -> "Clubs";
            default -> "Invalid";
        };
    }

    private String getPrintableCard(String card) {
        String properCard;
        if (card == null) {
            return "Not yet played";
        }
        int[] symbolsAndValues = getSymbolAndValue(card);
        int symbol = symbolsAndValues[0];
        int value = symbolsAndValues[1];
        properCard = switch (value) {
            case 2, 3, 4, 5, 6, 7, 8, 9, 10 -> value + " of ";
            case 11 -> "Jack of ";
            case 12 -> "Queen of ";
            case 13 -> "King of ";
            case 14 -> "Ace of ";
            default -> "Invalid card of ";
        } +
                switch (symbol) {
                    case 0 -> "Hearts";
                    case 1 -> "Diamonds";
                    case 2 -> "Spades";
                    case 3 -> "Clubs";
                    default -> "Invalid";
                };
        return properCard;

    }

    public String[] getPrintableHand(String[] cards) {
        String[] properHand = new String[cards.length];
        int i = 0;
        String card;
        for (String s : cards) {
            card = getPrintableCard(s);
            properHand[i] = card;
            i++;
        }
        return properHand;
    }

    public void endGame() {
        System.out.println("Game has ended!");
        System.out.println(getWinner().getName() + " is the Winner!!!\n\n");
        System.out.println("\n------------------------------------------------");
        System.out.println("\n------------------------------------------------");
        printScoreCard();
        deck.resetDeck();
    }

    public Player getWinner() {
        Player winner;
        winner = players[0];
        for (Player player : players) {
            if (player.getPoints() < winner.getPoints()) {
                winner = player;
            }
        }
        return winner;
    }

    public void printScoreCard() {
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println(i + ". " + players[i].getName() + "\t" + players[i].getPoints());
        }
    }
}