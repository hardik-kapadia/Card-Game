import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private final Deck deck;
    private final Scanner scanner;
    private final int numberOfPlayers;
    private String[] playerNames;
    private Player[] players;
    private String[] currentHand; // contains all the cards of the playing hand
    private int handCount; // number of hands played
    private int roundCount; // number of rounds played
    private boolean canPlayHearts; // can They play a heart's card yet?
    private boolean isFirstCard; // Is it the first card for comparing

    public Game(Deck deck, Scanner sc) { // initializing all necessary variables
        this.deck = deck;
        this.scanner = sc;
        this.numberOfPlayers = this.deck.getNumberOfPlayers();
        players = new Player[numberOfPlayers];
        playerNames = new String[numberOfPlayers];
        currentHand = new String[numberOfPlayers];
    }

    public void getNames() { // gets names of all players and whether they are bots or not
        while (true) {
            int numberOfBots = 0;
            for (int i = 0; i < deck.getNumberOfPlayers(); i++) {
                System.out.println("Enter your name: [leave blank to replace with bot]");
                String name = scanner.nextLine();
                if (name.equals("")) {
                    playerNames[i] = ("Bot-" + (i + 1));
                    numberOfBots++;
                } else {
                    playerNames[i] = (name);
                }
            }
            if (numberOfBots < 4) {
                break;
            } else {
                System.out.println("You have to add at least one player!");
            }
        }
    }

    public void startGame() { // The main program which is called in UserInterface
        getNames();
        for (int i = 0; i < numberOfPlayers; i++) { // assigns bot status based on name of player
            if (playerNames[i].equals("Bot-" + (i + 1))) {
                players[i] = new Player(deck.getPlayerDecks()[i], playerNames[i], i, true);
            } else {
                players[i] = new Player(deck.getPlayerDecks()[i], playerNames[i], i, false);
            }
        }
        this.roundCount = 1; // sets roundcounter to 1
        startRound(); // starts the round
    }

    public void startRound() {
        System.out.println("\n-------------------------------------------------------");
        System.out.print("-------------------------------------------------------");
        if (checkGameStats()) { // check if any of the players have a score above 100, if not continues to start round or ends game
            System.out.println("\nRound no. " + roundCount + " started");
            roundCount++; // increments roundCounter
            deck.resetDeck(); // resetting the deck
            deck.deal(); // deals the cards to the players
            for (int i = 0; i < numberOfPlayers; i++) { // assigned to new decks to all players
                players[i].setPlayerCards(deck.getPlayerDecks()[i]);
            }
            this.canPlayHearts = false; // basic initializations for starting the round fresh
            this.isFirstCard = true;
            Player handStarter = firstStarter(); // decides who will start the round based on the 2 of clubs
            handCount = 1; // initializes the handCount
            startHand(handStarter); // starts the hand
        } else {
            endGame(); // ends the game
        }
    }

    private Player firstStarter() { // goes through all the player's cards and returns which player has the 2 of clubs
        for (Player p : players) {
            if (p.getPlayerCards().contains("c2")) {
                return p;
            }
        }
        return players[0]; // Impossible scenario: If nobody has the two of spades, returns the first player
    }

    public void startHand(Player handStarter) { // starts the hand
        if (handCount <= 13) { // There can be only 13 hands in one round
            emptyArray(currentHand); // sets the currentHand to null
            System.out.println("-------------------------------------------------------");
            System.out.println("\nStarting hand number: " + handCount);
            int symbol = 4; // symbol is 4 when starting the hand and then assumes the values 0 to 3 based on first card played
            for (int i = 0; i < numberOfPlayers; i++) { // loops through all the players
                Player currentPlayer = players[(handStarter.getId() + i) % 4]; //sets current player for this loop by adding i to handStarter
                int playNum = promptUser(currentPlayer, symbol); // gets the index of the card the user wants to play
                String play = currentPlayer.getPlayerCards().get(playNum); // gets the actual card based on th index of card
                System.out.println("\n" + currentPlayer.getName() + " Played: " + getPrintableCard(play));
                if (symbol == 4) { // if this is the first card of the hand, then assigns the symbol
                    symbol = getSymbolAndValue(play)[0]; // sets symbol to the symbol of the first card played
                    if (symbol == 0) { // if the symbol of the first card is a heart
                        if (!this.canPlayHearts) { // check if the player can play a Heart card based on MS Heart rules
                            symbol = 4; // reverts value of symbol back to 4 as from 0
                            i--; // decrements i by one so the player gets to play again
                            System.out.println("You Cannot play a Heart Card right now!");
                            continue;
                        }
                    }
                    System.out.println("New hand started, symbol assigned: " + numToString(getSymbolAndValue(play)[0]));
                }
                if (playValidity(currentPlayer, play, symbol)) { // check validity of card and if valid,
                    currentPlayer.removeCard(playNum); // removes card from player's deck
                    currentHand[currentPlayer.getId()] = play; // adds card to current hand
                } else {
                    System.out.println("\n Please play a valid card\n");
                    i--;
                }
            }
            System.out.println("\nFinal hand: " + Arrays.toString(getPrintableHand(currentHand)));
            Player nextHandStarter = getHandWinner(currentHand, symbol, handStarter); // gets the winner of the hand
            System.out.println(nextHandStarter.getName() + " got the hand");
            addHandPoints(currentHand, nextHandStarter); // adds points to the winner
            handCount++;
            System.out.println("\n-------------------------------------------------------\n");
            System.out.print("ScoreCard: \n\n");
            printScoreCard();
            startHand(nextHandStarter); // starts recursive function but with this hand's winner as next hand's starter
        } else {
            startRound(); // starts new round if all 13 cards have been played
        }
    }

    public int promptUser(@NotNull Player player, int symbol) { // takes input from player or automatically returns bot's card or the
        // default card, also prints information to help player choose card
        System.out.println("\n|\tIts is " + player.getName() + "'s turn\t|\n");
        System.out.println("The current hand is: ");
        String[] printingHand = getPrintableHand(currentHand); // stores current hand in a beautified format
        for (int j = 0; j < numberOfPlayers; j++) { // prints the current hand with each player and his/ her card
            System.out.print(players[j].getName() + ": " + printingHand[j] + "\t");
        }
        System.out.println();
        if (symbol != 4) System.out.println("Ongoing Symbol is: " + numToString(symbol) + "\n");
        System.out.println("Your cards are: ");
        for (int i = 0; i < player.getPlayerCards().size(); i++) { // prints the player's deck with each cards index number
            System.out.print("(" + i + ") " + getPrintableCard(player.getPlayerCards().get(i)) + "\t");
            if (i == 7) { // prints a newline after the 7th card for visual betterment
                System.out.println();
            }
        }
        if (isFirstCard) { // automatically returns the 2 of clubs if this is the first card of the first hand of the rounf
            System.out.println("\n\nThis is the first play, so the 2 of clubs is automatically selected for you.");
            isFirstCard = false;
            for (int i = 0; i < player.getPlayerCards().size(); i++) {
                if (player.getPlayerCards().get(i).equals("c2")) {
                    return i;
                }
            }
        }
        if (player.isBot()) { // if the player is a bot, returns a card automatically based on pre-decided algorithm
            System.out.println();
            return autoPlay(player, symbol);
        }
        System.out.println("\n");
        while (true) { // takes input from player and returns it if the input is valid
            System.out.print("Enter number corresponding to card you want to play: ");
            int cardToPlay = Integer.parseInt(scanner.next());
            if (cardToPlay < player.getPlayerCards().size()) {
                return cardToPlay;
            } else {
                System.out.println("Please Enter a valid card\n");
            }
        }
    }

    public int autoPlay(Player player, int symbol) { // pre-decided algorithm to decide the bot's play
        int autoPlayCard = 0;
        if (symbol == 4) { // if this is the first card,
            while (true) {
                autoPlayCard = (int) (Math.random() * player.getPlayerCards().size()); // randomly select a card from the deck and
                if (this.canPlayHearts) { // play the card directly if playing hearts has been allowed by the game
                    return autoPlayCard;
                } else if (symbolToNum(player.getPlayerCards().get(autoPlayCard).charAt(0)) != 0) { // check whether the card is no a heart
                    // returns the card if not else start again
                    return autoPlayCard;
                }
            }
        }
        boolean setFirst = true; // setFirst card of the same symbol or so as the default card
        boolean cardOfSymbolFound = false; // self-explanatory
        boolean heartsCardFound = false; // self-explanatory
        for (int i = 0; i < player.getPlayerCards().size(); i++) { // goes through each card of player's deck
            String s = player.getPlayerCards().get(i);
            int[] symbolAndValue = getSymbolAndValue(s); // gets the symbol and value numerically
            if (symbolAndValue[0] == symbol) { // if the card is of same symbol as ongoing card
                cardOfSymbolFound = true;
                if (setFirst) { // if this is the first card of symbol found, set it as the card to play
                    autoPlayCard = i;
                    setFirst = false;
                } else if (symbolAndValue[1] < cardToValue(player.getPlayerCards().get(autoPlayCard))) { // compare with current card set to play
                    // and sets the lower card to play
                    autoPlayCard = i;
                }
            }
        }
        if (cardOfSymbolFound) { // returns if same symbol's card found
            return autoPlayCard;
        } // If same symbol's card not found the...
        if (player.getPlayerCards().contains("s12")) { // ...check if the player has the queen of spades and returns it
            return player.getPlayerCards().indexOf("s12");
        } // if player does not have the queen of spades
        for (int i = 0; i < player.getPlayerCards().size(); i++) { // goes through each card of player's deck and sets the card to play as the highest card of hearts
            String s = player.getPlayerCards().get(i);
            int[] symbolAndValue = getSymbolAndValue(s);
            if (symbolAndValue[0] == 0) {
                heartsCardFound = true;
                if (setFirst) {
                    autoPlayCard = i;
                    setFirst = false;
                } else if (symbolAndValue[1] > cardToValue(player.getPlayerCards().get(autoPlayCard))) {
                    autoPlayCard = i;
                }
            }
        }
        if (heartsCardFound) { // returns if the player has a heart card
            return autoPlayCard;
        } // if he doesn't...
        for (int i = 0; i < player.getPlayerCards().size(); i++) { // ... sets card to play as the highest valued card
            String s = player.getPlayerCards().get(i);
            int[] symbolAndValue = getSymbolAndValue(s);
            if (symbolAndValue[1] > cardToValue(player.getPlayerCards().get(autoPlayCard))) {
                autoPlayCard = i;
            }
        }
        return autoPlayCard; // returns highest values card's index in player's deck
    }

    public boolean playValidity(Player player, String play, int symbol) { // check's the player's card's validity in that hand
        if (symbolToNum(play.charAt(0)) == symbol) { // if the symbols are same, returns true
            return true;
        } // if the card has a different symbol...
        for (int i = 0; i < player.getPlayerCards().size(); i++) { // ...check if the player has any card of sme symbol in his deck
            if (symbolToNum(player.getPlayerCards().get(i).charAt(0)) == symbol) {
                return false;
            }
        }

        if (symbolToNum(play.charAt(0)) == 0) this.canPlayHearts = true;

        return true;
    }


    public Player getHandWinner(String[] handAtPlay, int symbol, @NotNull Player starter) { // gets the winner of the hand
        Player winner = starter; // assume that the winner is the person who played the first card in the hand
        for (Player p : players) { // cycles through each player's card in the hand and...
            int[] symbolAndValue = getSymbolAndValue(handAtPlay[p.getId()]);
            if (symbolAndValue[0] != symbol) { // ... skips to next iteration if their card's symbol is not the same as the symbol of the hand
                continue;
            }
            if (symbolAndValue[1] < Integer.parseInt(handAtPlay[winner.getId()].substring(1))) { // ...skips to nex iteration if the card's value is
                // lower than the current winner's card
                continue;
            }
            winner = p; // if the player passed both of the above tests, he is the new winner
        }
        return winner;
    }

    public void addHandPoints(String[] handAtPlay, Player winner) { // adds points to the winner
        int totalPointsGiveToWinner = 0;
        for (String s : handAtPlay) { // adds one point for each Heart's card and 13 points for the queen of spades
            int[] symbolAndValue = getSymbolAndValue(s);
            if (symbolAndValue[0] == 0) {
                totalPointsGiveToWinner++;
            } else if (symbolAndValue[0] == 2 && symbolAndValue[1] == 12) {
                totalPointsGiveToWinner += 13;
            }
        }
        if (totalPointsGiveToWinner == 26) { // If the winner has all 13 heart's card and the queen of spades, everyone but the player is awarded 26 points
            for (Player player : players) {
                if (!winner.equals(player)) {
                    player.addPoints(26);
                }
            }
        } else {
            winner.addPoints(totalPointsGiveToWinner); // adds points to the winner
        }
        System.out.println(totalPointsGiveToWinner + " points awarded to " + winner.getName() + "\n");
    }

    private int[] getSymbolAndValue(@NotNull String card) { // returns an array [symbol, value] according to specified legend
        return new int[]{symbolToNum(card.charAt(0)), Integer.parseInt(card.substring(1))};
    }

    private void emptyArray(String[] arrayToEmpty) { // replaces all the values in specified array to null, therefore emptying the array
        Arrays.fill(arrayToEmpty, null);
    }

    private int cardToValue(String card) { // takes String as input and returns the card's value (h12 -> 12)
        return getSymbolAndValue(card)[1];
    }

    public int symbolToNum(char symbol) { // returns number corresponding to given symbol
        return switch (symbol) {
            case 'h' -> 0;
            case 'd' -> 1;
            case 's' -> 2;
            case 'c' -> 3;
            default -> 4;
        };
    }

    public boolean checkGameStats() { // check whether the game should go on or not based on the player's points
        for (int i = 0; i < numberOfPlayers; i++) {
            if (!checkPoints(players[i])) { // checks if any player's points are above 50, if so: return false
                return false;
            }
        }
        return true; // else, return true
    }

    public boolean checkPoints(Player player) { // check whether given player's points are below 50...
        return player.getPoints() < 12; // ... or not
    }

    private String numToString(int symbol) { // returns the string for the symbol corresponding to given number
        return switch (symbol) {
            case 0 -> "Hearts";
            case 1 -> "Diamonds";
            case 2 -> "Spades";
            case 3 -> "Clubs";
            default -> "Invalid";
        };
    }

    private String getPrintableCard(String card) { // returns a String with a beautified format of card
        // h10 becomes 10 of Hearts & s14 becomes Ace of Spades (More info in legend)
        String properCard;
        if (card == null) { // if card is not yet played, returns so
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

    public String[] getPrintableHand(String[] cards) { // Accepts a String array of cards and returns a String array of said cards in beautifies format
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

    public void endGame() { // ends the Game and returns control to UserInterface
        System.out.println("\n------------------------------------------------\n");
        System.out.println("Game has ended!");
        System.out.println(getWinner().getName() + " is the Winner!!!\n");
        System.out.println("\n------------------------------------------------");
        System.out.println("\n------------------------------------------------");
        printScoreCard();
        deck.resetDeck();
        System.out.println("\n------------------------------------------------");
        System.out.println("\n------------------------------------------------");
        System.out.println("\n");

    }

    public Player getWinner() { // returns winner of the game (the player with the least points)
        Player winner;
        winner = players[0];
        for (Player player : players) {
            if (player.getPoints() < winner.getPoints()) {
                winner = player;
            }
        }
        return winner;
    }

    public void printScoreCard() { // prints the player's name and their scores
        System.out.println("\tScores\n");
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println(i + ". " + players[i].getName() + "\t" + players[i].getPoints());
        }
    }
}