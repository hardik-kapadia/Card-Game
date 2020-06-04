import java.util.ArrayList;

public class Player {

    private ArrayList<String> playerCards;
    private String name;
    private int points;

    public Player(ArrayList<String> cards, String  name) {
        this.playerCards = cards;
        this.points = 0;
        this.name = name;
    }

    public ArrayList<String> getPlayerCards() {
        return playerCards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }
}
