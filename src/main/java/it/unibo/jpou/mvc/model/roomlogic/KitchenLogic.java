package it.unibo.jpou.mvc.model.roomlogic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Kitchen, action eat.
 */
public final class KitchenLogic {

    public static final int INCREMENT_ACTION_EAT = 10;
    private final PouStatistics hunger;

    /**
     * @param hunger the hunger statistic to modify
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Logic classes must modify the passed statistics")
    public KitchenLogic(final PouStatistics hunger) {
        this.hunger = hunger;
    }

    /**
     * Meke Pou eat.
     */
    public void eat() {
        this.hunger.setValueStat(this.hunger.getValueStat() + INCREMENT_ACTION_EAT);
    }
}
