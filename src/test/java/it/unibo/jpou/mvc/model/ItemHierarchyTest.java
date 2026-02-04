package it.unibo.jpou.mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import it.unibo.jpou.mvc.model.items.consumable.food.Apple;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;
import it.unibo.jpou.mvc.model.items.consumable.Consumable;
import it.unibo.jpou.mvc.model.items.durable.Durable;

/**
 * Automated tests for verifying the Item hierarchy and properties within the MVC model.
 */
class ItemHierarchyTest {

    private static final Logger LOGGER = Logger.getLogger(ItemHierarchyTest.class.getName());

    private static final int EXPECTED_APPLE_PRICE = 5;
    private static final int EXPECTED_HEALTH_POTION_PRICE = 30;

    @BeforeAll
    static void setup() {
        LOGGER.log(Level.INFO, "--- Initializing Item Hierarchy Tests ---");
    }

    @Test
    @DisplayName("Verify Food: Apple properties and type safety")
    void testApple() {
        final Apple apple = new Apple();
        LOGGER.log(Level.INFO, "Testing: {0}", apple.getName());

        assertNotNull(apple.getName());
        assertEquals(EXPECTED_APPLE_PRICE, apple.getPrice());
        assertInstanceOf(Consumable.class, apple);
    }

    @Test
    @DisplayName("Verify Skin: RedSkin properties and type safety")
    void testRedSkin() {
        final RedSkin redSkin = new RedSkin();
        LOGGER.log(Level.INFO, "Testing: {0} [Color: {1}]",
                new Object[]{redSkin.getName(), redSkin.getColorHex()});

        assertEquals("#FF0000", redSkin.getColorHex());
        assertInstanceOf(Durable.class, redSkin);
    }

    @Test
    @DisplayName("Verify Potion: HealthPotion properties and type safety")
    void testHealthPotion() {
        final HealthPotion potion = new HealthPotion();
        LOGGER.log(Level.INFO, "Testing: {0}", potion.getName());

        assertEquals(EXPECTED_HEALTH_POTION_PRICE, potion.getPrice());
        assertInstanceOf(Consumable.class, potion);
    }
}
