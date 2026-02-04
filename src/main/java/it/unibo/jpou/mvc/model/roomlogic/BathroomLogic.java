package it.unibo.jpou.mvc.model.roomlogic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Bathroom, action wash.
 */
public final class BathroomLogic {

    public static final int INCREMENT_ACTION_WASH = 2;
    private final PouStatistics health;

    /**
     * @param health the health statistic to modify
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Logic classes must modify the passed statistics")
    public BathroomLogic(final PouStatistics health) {
        this.health = health;
    }

    /**
     * Washing action.
     */
    public void wash() {
        this.health.setValueStat(this.health.getValueStat() + INCREMENT_ACTION_WASH);
    }

}
