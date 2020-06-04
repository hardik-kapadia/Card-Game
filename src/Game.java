import java.util.Scanner;

public class Game {

    private String[] players;
    private Deck allCards;
    private Scanner scanner;

    public Game(Deck deck, Scanner sc){
        this.allCards = deck;
        players = new String[allCards.getNumberOfPlayers()];
        this.scanner = sc;
    }

    public void getNames(){
        for(int i=0;i<allCards.getNumberOfPlayers();i++){
            System.out.println("Enter your name: [leave blank to replace with bot]");
            String name = scanner.nextLine();
            if(name.equals("")){
                players[i] = "Bot" +i;
            } else {
                players[i] = name;
            }
        }
    }

    public void start(){

    }
}
