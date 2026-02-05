package it.unibo.jpou.mvc.model;

import it.unibo.jpou.mvc.model.items.consumable.food.Apple;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test for PouLogic orchestrator.
 */
class PouLogicTest {

    private static final int INITIAL_VALUE = 50;
    private PouLogic pou;

    @BeforeEach
    void setUp() {
        pou = new PouLogic();
        pou.setHunger(INITIAL_VALUE);
        pou.setHealth(INITIAL_VALUE);
        pou.setEnergy(INITIAL_VALUE);
        pou.setFun(INITIAL_VALUE);
    }

    @Test
    void testInitialization() {
        assertEquals(PouState.AWAKE, pou.getState());
        assertNotNull(pou.stateProperty());
    }

    @Test
    void testEatWithItem() {
        final Apple apple = new Apple();
        final int expected = INITIAL_VALUE + apple.getEffectValue();
        pou.eat(apple);
        assertEquals(expected, pou.getHunger());
    }

    @Test
    void testPotionWithItem() {
        final HealthPotion pot = new HealthPotion();
        final int expected = INITIAL_VALUE + pot.getEffectValue();
        pou.usePotion(pot);
        assertEquals(expected, pou.getHealth());
    }

    @Test
    void testSleepWake() {
        pou.sleep();
        assertEquals(PouState.SLEEPING, pou.getState());
        pou.wakeUp();
        assertEquals(PouState.AWAKE, pou.getState());
    }
}
