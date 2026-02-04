package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.statistics.FunStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameRoomLogicTest {

    private static final int SET_FUN_VALUE = 99;

    private PouStatistics fun;
    private GameRoomLogic gameRoomLogic;

    @BeforeEach
    void setUp() {
        this.fun = new FunStatistic();
        this.gameRoomLogic = new GameRoomLogic(this.fun);
    }

    @Test
    void testPlayIncreasesFun() {
        final int initialFun = this.fun.getValueStat();

        this.gameRoomLogic.play();

        assertEquals(initialFun + GameRoomLogic.INCREMENT_ACTION_PLAY, this.fun.getValueStat(),
                "Giocare deve aumentare il divertimento");
    }

    @Test
    void testPlayClamping() {
        this.fun.setValueStat(SET_FUN_VALUE);

        this.gameRoomLogic.play();

        assertEquals(PouStatistics.STATISTIC_MAX_VALUE, this.fun.getValueStat(),
                "L'azione play non deve far aumentare la statistica sopra al limite");
    }
}
