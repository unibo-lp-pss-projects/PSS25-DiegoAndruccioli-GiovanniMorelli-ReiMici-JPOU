package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Bathroom, action wash.
 */
public final class BathroomLogic {

    public static final int INCREMENT_ACTION_WASH = 2;

    /**
     * Washing action.
     *
     * @param health the statistic to modify
     */
    public void wash(final PouStatistics health) {
        health.setValueStat(health.getValueStat() + INCREMENT_ACTION_WASH);
    }

}
