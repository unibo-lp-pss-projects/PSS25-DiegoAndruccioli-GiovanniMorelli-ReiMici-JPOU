package it.unibo.jpou.mvc.model.roomlogic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Game room, action play.
 */
public final class GameRoomLogic {

    public static final int INCREMENT_ACTION_PLAY = 2;
    private final PouStatistics fun;

    /**
     * @param fun the fun statistic to modify
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Logic classes must modify the passed statistics")
    public GameRoomLogic(final PouStatistics fun) {
        this.fun = fun;
    }

    /**
     * Play action.
     */
    public void play() {
        this.fun.setValueStat(this.fun.getValueStat() + INCREMENT_ACTION_PLAY);
    }

}
