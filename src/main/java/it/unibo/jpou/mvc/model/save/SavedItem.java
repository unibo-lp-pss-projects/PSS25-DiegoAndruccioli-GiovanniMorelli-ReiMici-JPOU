package it.unibo.jpou.mvc.model.save;

/**
 * Record representing a saved item stack.
 *
 * @param name the unique name of the item
 * @param quantity the quantity possessed
 */
public record SavedItem(String name, int quantity) {

}
