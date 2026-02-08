package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.statistics.PouStatistics;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;

/**
 * Logic for the Kitchen room.
 */
public final class KitchenLogic {

    /**
     * Updates statistics when Pou eats.
     *
     * @param hungerStat the hunger statistic.
     * @param food       the food to consume.
     */
    public void eat(final PouStatistics hungerStat, final Food food) {
        if (food != null && hungerStat != null) {
            final int newValue = hungerStat.getValueStat() + food.getEffectValue();
            hungerStat.setValueStat(newValue);
        }
    }
}
