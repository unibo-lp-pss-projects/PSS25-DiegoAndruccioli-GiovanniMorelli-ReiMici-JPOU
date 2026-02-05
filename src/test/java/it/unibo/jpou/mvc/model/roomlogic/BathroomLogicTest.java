package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BathroomLogicTest {

    private static final int INITIAL_VALUE = 50;
    private BathroomLogic bathroomLogic;
    private HealthStatistic health;

    @BeforeEach
    void setUp() {
        bathroomLogic = new BathroomLogic();
        health = new HealthStatistic();
        health.setValueStat(INITIAL_VALUE);
    }

    @Test
    void testWash() {
        final int expectedIncrement = 20;
        bathroomLogic.wash(health);
        assertEquals(INITIAL_VALUE + expectedIncrement, health.getValueStat());
    }
}
