import java.util.ArrayList;
import java.util.Objects;

public class Player {

    private ArrayList<String> playerCards;
    private String name;
    private int points;
    private int id;

    public Player(ArrayList<String> cards, String name, int id) {
        this.playerCards = cards;
        this.points = 0;
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPlayerCards(ArrayList<String> playerCards) {
        this.playerCards = playerCards;
    }

    public void removeCard(int index){
        playerCards.remove(index);
    }

    public String returnAndRemoveCard(int index){
        String temp = playerCards.get(index);
        playerCards.remove(index);
        return temp;
    }

    public ArrayList<String> getPlayerCards() {
        return playerCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return points == player.points &&
                id == player.id &&
                playerCards.equals(player.playerCards) &&
                name.equals(player.name);
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerCards=" + playerCards +
                ", name='" + name + '\'' +
                ", points=" + points +
                ", id=" + id +
                '}';
    }
}
