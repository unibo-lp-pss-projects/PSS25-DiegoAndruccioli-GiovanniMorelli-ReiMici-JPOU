package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.statistics.FunStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameRoomLogicTest {

    private static final int INITIAL_VALUE = 50;
    private GameRoomLogic gameRoomLogic;
    private FunStatistic fun;

    @BeforeEach
    void setUp() {
        gameRoomLogic = new GameRoomLogic();
        fun = new FunStatistic();
        fun.setValueStat(INITIAL_VALUE);
    }

    @Test
    void testPlay() {
        final int expectedIncrement = 15;
        gameRoomLogic.play(fun);
        assertEquals(INITIAL_VALUE + expectedIncrement, fun.getValueStat());
    }
}
