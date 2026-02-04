package it.unibo.jpou.mvc.model.items.durable;

import it.unibo.jpou.mvc.model.items.Item;

/**
 * Marker interface for items that are purchased once and persist in the inventory.
 * These items are not consumed upon use but serve as permanent customizations
 */
public interface Durable extends Item {
}
