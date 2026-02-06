package it.unibo.jpou.mvc.model.items.consumable.potion;

import it.unibo.jpou.mvc.model.items.consumable.AbstractConsumable;

/**
 * A potion that restores the character's health points.
 */
public final class HealthPotion extends AbstractConsumable implements Potion {

    public static final String POTION_NAME = "Health Potion";
    private static final int POTION_PRICE = 30;
    private static final int POTION_EFFECT = 50;

    /**
     * Constructs a new HealthPotion.
     */
    public HealthPotion() {
        super(POTION_NAME, POTION_PRICE, POTION_EFFECT);
    }
}
