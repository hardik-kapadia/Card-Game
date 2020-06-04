import org.junit.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game g;
    private Deck d;
    private Scanner sc;

    @Before
    public void setUp() {
        d = new Deck(4);
        sc = new Scanner("hk\n"+"ab\n"+"amk\n"+"pkap\n");
        g = new Game(d,sc);
    }

    @Test
    public void checkPlayerName(){
        g.startGame();
        Player[] p = g.getPlayers();
        assertEquals(4,p.length);
        assertEquals(0,p[0].getPoints());
        p[0].addPoint();
        assertEquals(1,p[0].getPoints());
        p[0].addPoints(3);
        assertEquals(4,p[0].getPoints());

        // added a comment to test branches
    }
}