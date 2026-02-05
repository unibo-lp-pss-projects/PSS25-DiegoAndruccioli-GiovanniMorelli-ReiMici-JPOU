package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;

/**
 * Logic for Infirmary, action use potion.
 */
public final class InfirmaryLogic {

    public static final int INCREMENT_ENERGY_POTION = 10;
    public static final int INCREMENT_HEALTH_POTION = 20;

    /**
     * @param potionName name of potion
     * @param energy the statistic to modify
     * @param health the statistic to modify
     */
    public void usePotion(final String potionName, final PouStatistics energy, final PouStatistics health) {
        if ("HealthPotion".equals(potionName)) {
            health.setValueStat(health.getValueStat() + INCREMENT_HEALTH_POTION);
        } else if ("EnergyPotion".equals(potionName)) {
            energy.setValueStat(energy.getValueStat() + INCREMENT_ENERGY_POTION);
        }
    }
}
