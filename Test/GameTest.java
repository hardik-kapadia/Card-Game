import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

    private Game g;
    private Deck d;
    private Scanner sc;

    @Before
    public void setUp() {
        d = new Deck(4);
        sc = new Scanner("hk\n" + "ab\n" + "amk\n" + "pkap\n");
        g = new Game(d, sc);
    }
}