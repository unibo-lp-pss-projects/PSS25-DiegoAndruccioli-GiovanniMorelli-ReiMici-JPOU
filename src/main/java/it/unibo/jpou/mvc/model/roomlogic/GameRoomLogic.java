package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Game room, action play.
 */
public final class GameRoomLogic {

    public static final int INCREMENT_ACTION_PLAY = 2;

    /**
     * Play action.
     *
     * @param fun the statistic to modify
     */
    public void play(final PouStatistics fun) {
        fun.setValueStat(fun.getValueStat() + INCREMENT_ACTION_PLAY);
    }

}
