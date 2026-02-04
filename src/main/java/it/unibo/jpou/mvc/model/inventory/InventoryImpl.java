package it.unibo.jpou.mvc.model.inventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.Consumable;
import it.unibo.jpou.mvc.model.items.durable.Durable;

/**
 * Concrete implementation of the player's inventory.
 * Uses a Map for consumables to track quantities and a Set for durables
 * to ensure uniqueness.
 */
public class InventoryImpl implements Inventory {

    private final Map<Consumable, Integer> consumables = new HashMap<>();
    private final Set<Durable> durables = new HashSet<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addItem(final Item item) {
        if (item instanceof Consumable) {
            final Consumable c = (Consumable) item;
            this.consumables.put(c, this.consumables.getOrDefault(c, 0) + 1);
        } else if (item instanceof Durable) {
            this.durables.add((Durable) item);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void consumeItem(final Consumable consumable) {
        if (!this.consumables.containsKey(consumable)) {
            throw new IllegalArgumentException("Item not present in inventory: " + consumable.getName());
        }
        final int quantity = this.consumables.get(consumable);
        if (quantity > 1) {
            this.consumables.put(consumable, quantity - 1);
        } else {
            this.consumables.remove(consumable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isOwned(final Durable durable) {
        return this.durables.contains(durable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Map<Consumable, Integer> getConsumables() {
        return Collections.unmodifiableMap(this.consumables);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<Durable> getDurables() {
        return Collections.unmodifiableSet(this.durables);
    }
}
