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

    private FruitCatcherGame game;

    private static final double GAME_WIDTH = 800.0;
    private static final double PLAYER_BOUNDARY_OFFSET = 50.0;
    private static final double INITIAL_TIME = 60.0;

    @BeforeEach
    void setUp() {
        this.game = new FruitCatcherGame();
        this.game.startGame();
    }

    @Test
    @DisplayName("Test Initial State")
    void testInitialState() {
        System.out.println("[TEST] Verifica Stato Iniziale...");

        assertEquals(0, game.getScore(), "Il punteggio iniziale deve essere 0");
        assertFalse(game.isGameOver(), "Il gioco non deve iniziare in Game Over");

        assertEquals(GAME_WIDTH / 2, game.getPlayerX(), "Pou deve iniziare al centro dello schermo");

        assertEquals(INITIAL_TIME, game.getTimeLeft(), 0.1, "Il timer deve partire da 60 secondi");

        System.out.println(" -> OK! Punteggio a 0, Timer a 60s e Pou al centro.");
    }

    @Test
    @DisplayName("Test Player Position Update (Movement)")
    void testPlayerPositionUpdate() {
        System.out.println("[TEST] Verifica Aggiornamento Posizione...");

        double startX = game.getPlayerX();
        double newX = startX + 50;

        game.setPlayerPosition(newX);

        System.out.println(" -> Posizione X: da " + startX + " a " + game.getPlayerX());

        assertEquals(newX, game.getPlayerX(), "La posizione del giocatore deve aggiornarsi correttamente");
    }

    @Test
    @DisplayName("Test Boundary Left (0)")
    void testBoundaryLeft() {
        System.out.println("[TEST] Verifica Muro Sinistro...");

        game.setPlayerPosition(-100.0);

        System.out.println(" -> Posizione Finale X: " + game.getPlayerX() + " (Atteso: 0.0)");

        assertEquals(0.0, game.getPlayerX(), "Pou non deve superare il bordo sinistro (0)");
    }

    @Test
    @DisplayName("Test Boundary Right (Max Width)")
    void testBoundaryRight() {
        System.out.println("[TEST] Verifica Muro Destro...");

        game.setPlayerPosition(GAME_WIDTH + 200.0);

        double expectedMaxX = GAME_WIDTH - PLAYER_BOUNDARY_OFFSET;

        System.out.println(" -> Posizione Finale X: " + game.getPlayerX() + " (Atteso: " + expectedMaxX + ")");

        assertEquals(expectedMaxX, game.getPlayerX(), "Pou non deve superare il bordo destro");
    }

    @Test
    @DisplayName("Test Game Loop Timer Decrement")
    void testTimerDecrement() {
        System.out.println("[TEST] Verifica Decremento Timer...");

        double initialTime = game.getTimeLeft();

        game.gameLoop(System.nanoTime());

        assertTrue(game.getTimeLeft() < initialTime, "Il tempo deve diminuire dopo un ciclo di gioco");
        System.out.println(" -> Tempo rimanente: " + game.getTimeLeft());
    }

    @Test
    @DisplayName("Test Coin Calculation")
    void testEconomyMath() {
        System.out.println("[TEST] Verifica Calcolo Monete...");

        assertEquals(0, game.calculateCoins());

    }
}
