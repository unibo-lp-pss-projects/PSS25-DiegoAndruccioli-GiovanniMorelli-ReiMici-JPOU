package it.unibo.jpou.mvc.model;

import it.unibo.jpou.mvc.model.items.consumable.food.Apple;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test for PouLogic orchestrator.
 */
class PouLogicTest {

    private static final int AWAKE_DECAY = 3;
    private static final int CUSTOM_STATISTICS = 60;
    private static final int CUSTOM_WALLET = 10_000;
    private static final int CUSTOM_AGE = 20;

    private PouLogic pou;

    @BeforeEach
    void setUp() {
        pou = new PouLogic();
        pou.setHunger(PouStatistics.STATISTIC_INITIAL_VALUE);
        pou.setHealth(PouStatistics.STATISTIC_INITIAL_VALUE);
        pou.setEnergy(PouStatistics.STATISTIC_INITIAL_VALUE);
        pou.setFun(PouStatistics.STATISTIC_INITIAL_VALUE);
    }

    @Test
    void testInitialization() {
        assertEquals(PouState.AWAKE, pou.getState());
        assertNotNull(pou.stateProperty());
    }

    @Test
    void testEatWithItem() {
        final Apple apple = new Apple();
        final int expected = PouStatistics.STATISTIC_INITIAL_VALUE + apple.getEffectValue();
        pou.eat(apple);
        assertEquals(expected, pou.getHunger());
    }

    @Test
    void testPotionWithItem() {
        final HealthPotion pot = new HealthPotion();
        final int expected = PouStatistics.STATISTIC_INITIAL_VALUE + pot.getEffectValue();
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

    @Test
    void testDecay() {
        pou.applyDecay();

        final int expected = PouStatistics.STATISTIC_INITIAL_VALUE - AWAKE_DECAY;

        assertAll("Le statistiche dovrebbero decrementare tutte di AWAKE_DECAY",
                () -> assertEquals(expected, pou.getHunger()),
                () -> assertEquals(expected, pou.getEnergy()),
                () -> assertEquals(expected, pou.getFun()),
                () -> assertEquals(expected, pou.getHealth())
        );
    }

    @Test
    void testReset() {
        pou.setHunger(CUSTOM_STATISTICS);
        pou.setEnergy(CUSTOM_STATISTICS);
        pou.setFun(CUSTOM_STATISTICS);
        pou.setHealth(CUSTOM_STATISTICS);
        pou.setCoins(CUSTOM_WALLET);
        pou.sleep();
        pou.setAge(CUSTOM_AGE);

        pou.reset();

        assertAll("Pou torna alle caratteristiche iniziali",
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getHunger()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getEnergy()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getFun()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getHealth()),
                () -> assertEquals(PouCoins.MIN_COINS, pou.getCoins()),
                () -> assertEquals(PouState.AWAKE, pou.getState()),
                () -> assertEquals(0, pou.getAge())
        );
    }
}
