package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Game Room, action play.
 */
public final class GameRoomLogic {

    private static final int PLAY_INCREMENT = 15;
    private static final int ENERGY_DECREMENT = 10;

    /**
     * Play action.
     *
     * @param fun the fun statistic to modify.
     * @param energy the energy statistic to modify.
     */
    public void play(final PouStatistics fun, final PouStatistics energy) {
        fun.setValueStat(fun.getValueStat() + PLAY_INCREMENT);
        energy.setValueStat(energy.getValueStat() - ENERGY_DECREMENT);
    }
}
