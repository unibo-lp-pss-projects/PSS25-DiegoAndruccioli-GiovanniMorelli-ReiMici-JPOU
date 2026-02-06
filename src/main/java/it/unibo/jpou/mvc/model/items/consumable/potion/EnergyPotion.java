package it.unibo.jpou.mvc.model.items.consumable.potion;

import it.unibo.jpou.mvc.model.items.consumable.AbstractConsumable;

/**
 * A potion that restores the character's energy levels, allowing for more activities.
 */
public final class EnergyPotion extends AbstractConsumable implements Potion {

    public static final String POTION_NAME = "Energy Potion";
    private static final int POTION_PRICE = 20;
    private static final int POTION_EFFECT = 30;

    /**
     * Constructs a new EnergyPotion.
     */
    public EnergyPotion() {
        super(POTION_NAME, POTION_PRICE, POTION_EFFECT);
    }
}
