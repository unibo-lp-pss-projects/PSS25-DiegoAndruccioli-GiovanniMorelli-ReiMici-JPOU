package it.unibo.jpou.mvc.model.minigames.fruitcatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

/**
 * Unit tests for the FallingObject class.
 */
class FallingObjectTest {

    private static final Logger LOGGER = Logger.getLogger(FallingObjectTest.class.getName());
    private static final double START_X = 100.0;
    private static final double START_Y = 0.0;
    private static final int OBJ_WIDTH = 40;
    private static final int OBJ_HEIGHT = 40;
    private static final double GRAVITY = 0.5;

    @Test
    @DisplayName("Test creation and properties of a Fruit (Apple)")
    void testFruitCreation() {
        LOGGER.info("Testing Fruit creation (APPLE)");
        final FallingObject apple = new FallingObject(START_X, START_Y, FallingObject.Type.APPLE, OBJ_WIDTH, OBJ_HEIGHT);

        assertEquals(FallingObject.Type.APPLE, apple.getType());
        assertEquals(1, apple.getValue(), "Apple should be worth 1 point");
        assertFalse(apple.isBomb(), "Apple should not be a bomb");
        assertEquals(START_X, apple.getX());
        assertEquals(START_Y, apple.getY());
    }

    @Test
    @DisplayName("Test creation and properties of a Bomb")
    void testBombCreation() {
        LOGGER.info("Testing Bomb creation");
        final FallingObject bomb = new FallingObject(START_X, START_Y, FallingObject.Type.BOMB, OBJ_WIDTH, OBJ_HEIGHT);

        assertEquals(FallingObject.Type.BOMB, bomb.getType());
        assertEquals(0, bomb.getValue(), "Bomb should be worth 0 points");
        assertTrue(bomb.isBomb(), "Bomb should be identified as dangerous");
    }

    @Test
    @DisplayName("Test falling physics")
    void testFallMovement() {
        LOGGER.info("Testing fall physics");
        final FallingObject obj = new FallingObject(START_X, START_Y, FallingObject.Type.BANANA, OBJ_WIDTH, OBJ_HEIGHT);
        // y iniziale è 0.0
        obj.fall(GRAVITY);

        // dopo la caduta y deve essere > 0
        assertTrue(obj.getY() > START_Y, "Object should fall downwards (positive Y)");
        LOGGER.info("New Y position after fall: " + obj.getY());
    }
}
