package it.unibo.jpou.mvc.model.roomlogic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Infirmary, action use potion.
 */
public final class InfirmaryLogic {

    public static final int INCREMENT_ENERGY_POTION = 10;
    public static final int INCREMENT_HEALTH_POTION = 20;
    private final PouStatistics energy;
    private final PouStatistics health;

    /**
     * @param energy the energy statistic to modify
     * @param health the health statistic to modify
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Logic classes must modify the passed statistics")
    public InfirmaryLogic(final PouStatistics energy, final PouStatistics health) {
        this.energy = energy;
        this.health = health;
    }

    /**
     * @param potionName name of potion
     */
    public void usePotion(final String potionName) {
        if ("HealthPotion".equals(potionName)) {
            this.health.setValueStat(this.health.getValueStat() + INCREMENT_HEALTH_POTION);
        } else if ("EnergyPotion".equals(potionName)) {
            this.energy.setValueStat(this.energy.getValueStat() + INCREMENT_ENERGY_POTION);
        }
    }
}
