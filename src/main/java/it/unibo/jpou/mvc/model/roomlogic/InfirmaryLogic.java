package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.items.consumable.potion.EnergyPotion;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;

/**
 * Logic for Infirmary, action use potion.
 */
public final class InfirmaryLogic {

    /**
     * Uses a specific potion.
     *
     * @param energy the energy statistic to modify.
     * @param health the health statistic to modify.
     * @param potion the potion item to use.
     */
    public void usePotion(final PouStatistics energy, final PouStatistics health, final Potion potion) {
        if (potion == null) {
            return;
        }

        if (potion instanceof HealthPotion) {
            health.setValueStat(health.getValueStat() + potion.getEffectValue());
        } else if (potion instanceof EnergyPotion) {
            energy.setValueStat(energy.getValueStat() + potion.getEffectValue());
        }
    }
}
