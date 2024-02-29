package card.game;

import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;

    public UserInterface(Scanner sc) { // Initializations
        this.scanner = sc;
    }

    public void start() { // starts the game

        Deck deck;
        Game game;

        while (true) { // Infinite loop till player chooses to exit
            char choice;
            System.out.println("Please select the Game you want to play");
            System.out.println("1. M.S. Hearts\nX. exit"); //Game 2 is not yet developed
            System.out.print("\nEnter your Choice: [1/X]");
            choice = scanner.nextLine().charAt(0);
            if (choice == '1') { // if player chooses to play
                deck = new Deck(4); // initiate deck
                game = new Game(deck, scanner); // initiate the game
                game.startGame(); // start the game
            } else {
                System.out.println("Exiting");
                break;
            }
            if(scanner.hasNextLine()){
                scanner.nextLine();
            }
        }
    }
}
