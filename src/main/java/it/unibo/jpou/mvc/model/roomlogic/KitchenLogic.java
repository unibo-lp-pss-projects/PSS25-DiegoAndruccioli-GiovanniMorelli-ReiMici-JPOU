package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;

/**
 * Logic for Kitchen, action eat.
 */
public final class KitchenLogic {

    /**
     * Make Pou eat a specific food.
     *
     * @param hunger the hunger statistic to modify.
     * @param food the food item being consumed.
     */
    public void eat(final PouStatistics hunger, final Food food) {
        if (food != null) {
            hunger.setValueStat(hunger.getValueStat() + food.getEffectValue());
        }
    }
}
