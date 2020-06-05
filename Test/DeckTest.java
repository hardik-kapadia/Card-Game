import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTest {

    private Deck d, d1, d2;

    @Before
    public void setUp() {
        d = new Deck(4);
        d1 = new Deck(6);
        d2 = new Deck(2);
    }

    @Test
    public void checkFullDeck() {
        d.createDeck();
        String[] temp = d.returnFullDeck();
        assertEquals(52, temp.length);
        for (int i = 0; i < 52; i++) {
            assertEquals((Integer.parseInt(temp[i].substring(1)) / 10) + 2, temp[i].length());
        }
    }

    @Test
    public void playerCheck() {
        d.deal();
        ArrayList<String>[] temp = d.getPlayerDecks();
        for (int i = 0; i < 4; i++) {
            assertEquals(13, temp[i].size());
        }
    }

    @Test
    public void playerCheckForVariableCards() {
        d.deal(15);
        ArrayList<String>[] temp = d.getPlayerDecks();
        for (int i = 0; i < 4; i++) {
            assertEquals(0, temp[i].size());
        }
        d.deal(5);
        temp = d.getPlayerDecks();
        for (int i = 0; i < 4; i++) {
            assertEquals(5, temp[i].size());
        }
        d1.deal(9);
        temp = d1.getPlayerDecks();
        for (int i = 0; i < 4; i++) {
            assertEquals(0, temp[i].size());
        }
        d2.deal(20);
        temp = d1.getPlayerDecks();
        for (int i = 0; i < 4; i++) {
            assertEquals(0, temp[i].size());
        }
    }

    @Test
    public void checkConversion() {
        assertEquals(0, d.cardToIndex("h2"));
        assertEquals(51, d.cardToIndex("c14"));
        assertEquals(23, d.cardToIndex(d.indexToCard(23)));
    }

    @Test
    public void checkHands() {

    }


}