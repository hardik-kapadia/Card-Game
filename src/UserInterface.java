import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;
    private Deck deck;
    private Game game;

    public UserInterface(Scanner sc) {
        this.scanner = sc;
    }

    public void start() {
        char choice;
        System.out.println("Please select the Game you want to play");
        System.out.println("1. M.S. Hearts\n2. 7-8\nX. exits"); //Game 2 is not yet developed
        System.out.println("\nEnter your Choice: [1/2/X]");
        choice = scanner.nextLine().charAt(0);
        switch (choice) {
            case '1':
                deck = new Deck(4);
                game = new Game(deck, scanner);
                game.startGame();
                break;
            default:
                System.out.println("Invalid Option");
        }
    }
}
