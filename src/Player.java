import java.util.ArrayList;

public class Player {

    private ArrayList<String> playerCards;
    private String name;
    private int points;
    private int id;

    public Player(ArrayList<String> cards, String  name, int id) {
        this.playerCards = cards;
        this.points = 0;
        this.name = name;
        this.id = id;
    }

    public ArrayList<String> getPlayerCards() {
        return playerCards;
    }

    public void addPoint(){
        addPoints(1);
    }

    public void addPoints(int points){
        this.points += points;
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
