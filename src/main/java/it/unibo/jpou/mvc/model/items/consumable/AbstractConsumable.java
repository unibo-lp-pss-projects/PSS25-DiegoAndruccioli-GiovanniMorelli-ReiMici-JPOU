package it.unibo.jpou.mvc.model.items.consumable;

/**
 * Abstract base class for all consumable items.
 * This class handles the common state for consumables, such as name, price, and effect value,
 * reducing code duplication in concrete implementations like Food or Potions.
 */
public abstract class AbstractConsumable implements Consumable {

    private final String name;
    private final int price;
    private final int effectValue;

    /**
     * Constructs a new AbstractConsumable.
     *
     * @param name        the display name of the item.
     * @param price       the cost in the shop.
     * @param effectValue the magnitude of the item's benefit.
     */
    protected AbstractConsumable(final String name, final int price, final int effectValue) {
        this.name = name;
        this.price = price;
        this.effectValue = effectValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPrice() {
        return this.price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEffectValue() {
        return this.effectValue;
    }
}
