package it.unibo.jpou.mvc.model.minigames.fruitcatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

/**
 * Unit tests for the FruitCatcherGame logic.
 */
class FruitCatcherGameTest {

    private static final Logger LOGGER = Logger.getLogger(FruitCatcherGameTest.class.getName());
    private static final double GAME_WIDTH = 800.0;
    private static final double EXPECTED_CENTER_X = GAME_WIDTH / 2.0;

    private FruitCatcherGame game;

    @BeforeEach
    void setUp() {
        this.game = new FruitCatcherGame();
        this.game.startGame();
    }

    @Test
    @DisplayName("Test initial game state")
    void testInitialState() {
        LOGGER.info("Testing initial game state");
        assertEquals(0, this.game.getScore(), "Score should start at 0");
        assertFalse(this.game.isGameOver(), "Game should not start in Game Over state");
        assertEquals(EXPECTED_CENTER_X, this.game.getPlayerX(), "Player should start at center screen");
        assertTrue(this.game.getFallingObjects().isEmpty(), "No objects should be present at very start");
    }

    @Test
    @DisplayName("Test player position update")
    void testPlayerMovement() {
        LOGGER.info("Testing player position update");
        final double newPosition = 150.0;

        this.game.setPlayerPosition(newPosition);

        assertEquals(newPosition, this.game.getPlayerX(), "Player X should update to the set value");
    }

    @Test
    @DisplayName("Test game loop execution (basic sanity check)")
    void testGameLoopSanity() {
        LOGGER.info("Testing game loop execution");
        // run gameloop per pochi frame
        for (int i = 0; i < 100; i++) {
            this.game.gameLoop(0);
        }

        assertFalse(this.game.isGameOver(), "Game should continue if no bombs hit");
    }

    @Test
    @DisplayName("Test coin calculation at start")
    void testInitialCoins() {
        LOGGER.info("Testing initial coin calculation");
        assertEquals(0, this.game.calculateCoins(), "Should yield 0 coins with 0 score");
    }
}
