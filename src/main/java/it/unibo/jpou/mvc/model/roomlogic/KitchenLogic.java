package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Kitchen, action eat.
 */
public final class KitchenLogic {

    public static final int INCREMENT_ACTION_EAT = 10;

    /**
     * Meke Pou eat.
     *
     * @param hunger the statistic to modify
     */
    public void eat(final PouStatistics hunger) {
        hunger.setValueStat(hunger.getValueStat() + INCREMENT_ACTION_EAT);
    }
}
