import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;
    private Deck d;

    public UserInterface(Scanner sc) {
        this.scanner = sc;
    }

    public void start() {
        char choice;
        do {
            System.out.println("Please select the Game you want to play");
            System.out.println("1. M.S. Hearts\n2. 7-8\nX. exits");
            System.out.println("Enter your Choice: [1/2/X]");
            choice = scanner.next().charAt(0);
            switch (choice) {
                case '1':
                    Deck allCards = new Deck(4);
                    Game mshearts = new Game(allCards,scanner);
                    break;
                case '2':
                    break;
                default:
                    System.out.println("Invalid Option");
            }
        } while (choice != 'X' && choice != 'x');
    }
}
