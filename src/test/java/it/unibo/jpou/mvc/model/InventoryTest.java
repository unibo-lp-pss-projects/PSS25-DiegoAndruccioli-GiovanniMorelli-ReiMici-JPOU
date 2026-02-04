package it.unibo.jpou.mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.model.items.consumable.food.Apple;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;

/**
 * Unit tests for the Inventory implementation.
 */
class InventoryTest {

    private static final Logger LOGGER = Logger.getLogger(InventoryTest.class.getName());
    private static final int INITIAL_QUANTITY = 2;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        this.inventory = new InventoryImpl();
    }

    @Test
    @DisplayName("Test adding and consuming food items")
    void testConsumablesManagement() {
        final Apple apple = new Apple();
        LOGGER.info("Testing consumables management for: " + apple.getName());

        this.inventory.addItem(apple);
        this.inventory.addItem(apple);

        assertEquals(INITIAL_QUANTITY, this.inventory.getConsumables().get(apple),
                "Should have 2 apples");

        this.inventory.consumeItem(apple);
        assertEquals(1, this.inventory.getConsumables().get(apple));

        this.inventory.consumeItem(apple);
        assertFalse(this.inventory.getConsumables().containsKey(apple));
    }

    @Test
    @DisplayName("Test adding durable items (uniqueness)")
    void testDurableManagement() {
        final RedSkin skin = new RedSkin();
        LOGGER.info("Testing durable management for: " + skin.getName());

        this.inventory.addItem(skin);
        this.inventory.addItem(skin);

        assertTrue(this.inventory.isOwned(skin));
        assertEquals(1, this.inventory.getDurables().size());
    }

    @Test
    @DisplayName("Test consuming non-existent item throws exception")
    void testInvalidConsumption() {
        final Apple apple = new Apple();
        LOGGER.info("Testing exception handling for missing item: " + apple.getName());

        assertThrows(IllegalArgumentException.class, () -> {
            this.inventory.consumeItem(apple);
        });
    }
}
