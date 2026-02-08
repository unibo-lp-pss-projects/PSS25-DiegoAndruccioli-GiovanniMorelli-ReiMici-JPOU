package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.statistics.EnergyStatistic;
import it.unibo.jpou.mvc.model.statistics.FunStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameRoomLogicTest {

    private static final int INITIAL_VALUE = 50;
    private GameRoomLogic gameRoomLogic;
    private FunStatistic fun;
    private EnergyStatistic energy;

    @BeforeEach
    void setUp() {
        gameRoomLogic = new GameRoomLogic();
        fun = new FunStatistic();
        fun.setValueStat(INITIAL_VALUE);

        energy = new EnergyStatistic();
        energy.setValueStat(INITIAL_VALUE);
    }

    @Test
    void testPlay() {
        final int expectedIncrement = 15;
        final int expectedDecrement = 10;
        gameRoomLogic.play(fun, energy);
        assertEquals(INITIAL_VALUE + expectedIncrement, fun.getValueStat(), "Fun should increase");
        assertEquals(INITIAL_VALUE - expectedDecrement, energy.getValueStat(), "Energy should decrease");
    }
}
