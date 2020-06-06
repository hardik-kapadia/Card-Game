import java.util.ArrayList;

public class Player {

    private ArrayList<String> playerCards; // player's deck
    private final String name;
    private int points;
    private final int id;
    private boolean isBot; // whether player is a bot or person

    public Player(ArrayList<String> cards, String name, int id, boolean isBot) { // Initialization
        this.playerCards = cards;
        this.points = 0;
        this.name = name;
        this.id = id;
        this.isBot = isBot;
    }

    public int getId() { // returns player's id
        return id;
    }

    public boolean isBot() { // returns whether the player is a bot or not
        return isBot;
    }

    public void setPlayerCards(ArrayList<String> playerCards) { // gives the player a fresh deck of cards (implemented after every round)
        this.playerCards = playerCards;
    }

    public void removeCard(int index){ // removes said card from player's deck
        playerCards.remove(index);
    }

    public ArrayList<String> getPlayerCards() { // returns an ArrayList of player's deck of cards
        return playerCards;
    }

    @Override
    public boolean equals(Object o) { // self-explanatory
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return points == player.points &&
                id == player.id &&
                playerCards.equals(player.playerCards) &&
                name.equals(player.name);
    }

    public void addPoints(int points) { // adds points for the player
        this.points += points;
    }

    public String getName() { // returns player's name
        return name;
    }

    public int getPoints() { // returns player's points
        return points;
    }

    @Override
    public String toString() { // default method
        return "Player{" +
                "playerCards=" + playerCards +
                ", name='" + name + '\'' +
                ", points=" + points +
                ", id=" + id +
                '}';
    }
}
