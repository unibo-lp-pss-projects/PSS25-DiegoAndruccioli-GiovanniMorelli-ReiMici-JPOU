package it.unibo.jpou.mvc.model.minigames.fruitcatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the FruitCatcherGame model.
 */
class FruitCatcherGameTest {

    private static final double GAME_WIDTH = 400.0;
    private static final double PLAYER_SIZE = 60.0;

    private static final double INITIAL_TIME = 60.0;
    private static final double TOLERANCE = 0.1;
    private static final double MOVEMENT_DELTA = 50.0;
    private static final double NEGATIVE_POSITION = -100.0;
    private static final double OUT_OF_BOUNDS_POSITION = 1000.0;

    private FruitCatcherGame game;

    @BeforeEach
    void setUp() {
        this.game = new FruitCatcherGame();
        this.game.startGame();
    }

    @Test
    @DisplayName("Test Initial State")
    void testInitialState() {
        assertEquals(0, game.getScore(), "Il punteggio iniziale deve essere 0");
        assertFalse(game.isGameOver(), "Il gioco non deve iniziare in Game Over");

        final double expectedInitialX = (GAME_WIDTH - PLAYER_SIZE) / 2.0;
        assertEquals(expectedInitialX, game.getPlayerX(), "Pou deve iniziare centrato (considerando la sua larghezza)");

        assertEquals(INITIAL_TIME, game.getTimeLeft(), TOLERANCE, "Il timer deve partire da 60 secondi");
    }

    @Test
    @DisplayName("Test Player Position Update (Movement)")
    void testPlayerPositionUpdate() {
        final double startX = game.getPlayerX();
        final double newX = startX + MOVEMENT_DELTA;

        game.setPlayerPosition(newX);

        assertEquals(newX, game.getPlayerX(), "La posizione del giocatore deve aggiornarsi correttamente");
    }

    @Test
    @DisplayName("Test Boundary Left (0)")
    void testBoundaryLeft() {
        game.setPlayerPosition(NEGATIVE_POSITION);

        assertEquals(0.0, game.getPlayerX(), "Pou non deve superare il bordo sinistro (0)");
    }

    @Test
    @DisplayName("Test Boundary Right (Max Width)")
    void testBoundaryRight() {
        game.setPlayerPosition(OUT_OF_BOUNDS_POSITION);

        final double expectedMaxX = GAME_WIDTH - PLAYER_SIZE;

        assertEquals(expectedMaxX, game.getPlayerX(), "Pou non deve superare il bordo destro");
    }

    @Test
    @DisplayName("Test Game Loop Timer Decrement")
    void testTimerDecrement() {
        final double initialTime = game.getTimeLeft();

        game.gameLoop(System.nanoTime());

        assertTrue(game.getTimeLeft() < initialTime, "Il tempo deve diminuire dopo un ciclo di gioco");
    }

    @Test
    @DisplayName("Test Coin Calculation")
    void testEconomyMath() {
        assertEquals(0, game.calculateCoins());
    }
}
