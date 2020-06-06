import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;
    private Deck deck;
    private Game game;

    public UserInterface(Scanner sc) {
        this.scanner = sc;
    }

    public void start() {
        while (true) {
            char choice;
            System.out.println("Please select the Game you want to play");
            System.out.println("1. M.S. Hearts\nX. exit"); //Game 2 is not yet developed
            System.out.print("\nEnter your Choice: [1/X]");
            choice = scanner.nextLine().charAt(0);
            if (choice == '1') {
                deck = new Deck(4);
                game = new Game(deck, scanner);
                game.startGame();
            } else {
                System.out.println("Invalid Option");
                break;
            }
        }
    }
}
