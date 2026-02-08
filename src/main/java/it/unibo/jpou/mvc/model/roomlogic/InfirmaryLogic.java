package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.statistics.PouStatistics;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import it.unibo.jpou.mvc.model.items.consumable.potion.EnergyPotion;

/**
 * Logic for the Infirmary.
 */
public final class InfirmaryLogic {

    /**
     * Applies potion effects selectively based on potion type.
     *
     * @param energyStat the energy statistic.
     * @param healthStat the health statistic.
     * @param potion     the potion to use.
     */
    public void usePotion(final PouStatistics energyStat, final PouStatistics healthStat, final Potion potion) {
        if (potion == null || energyStat == null || healthStat == null) {
            return;
        }

        final int effect = potion.getEffectValue();

        if (potion instanceof HealthPotion) {
            healthStat.setValueStat(healthStat.getValueStat() + effect);
        } else if (potion instanceof EnergyPotion) {
            energyStat.setValueStat(energyStat.getValueStat() + effect);
        }
    }
}
