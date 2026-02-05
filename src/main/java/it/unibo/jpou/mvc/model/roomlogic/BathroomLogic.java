package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Bathroom, action wash.
 */
public final class BathroomLogic {

    private static final int WASH_INCREMENT = 20;

    /**
     * Washing action.
     *
     * @param health the health statistic to modify (washing improves hygiene/health).
     */
    public void wash(final PouStatistics health) {
        health.setValueStat(health.getValueStat() + WASH_INCREMENT);
    }
}
