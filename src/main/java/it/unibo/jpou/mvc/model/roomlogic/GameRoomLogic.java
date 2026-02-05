package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Game Room, action play.
 */
public final class GameRoomLogic {

    private static final int PLAY_INCREMENT = 15;

    /**
     * Play action.
     *
     * @param fun the fun statistic to modify.
     */
    public void play(final PouStatistics fun) {
        fun.setValueStat(fun.getValueStat() + PLAY_INCREMENT);
    }
}
