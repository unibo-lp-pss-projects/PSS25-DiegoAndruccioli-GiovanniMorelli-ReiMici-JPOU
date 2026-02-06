package it.unibo.jpou.mvc.model.save;

import java.util.List;

/**
 * Record representing the saved state of the inventory.
 *
 * @param items         the list of consumable items (food and potions) currently owned
 * @param unlockedSkins the list of names of the skins that been unlocked/purchased
 * @param equippedSkin  the name of the skin currently equipped
 */
public record SavedInventory(List<SavedItem> items, List<String> unlockedSkins, String equippedSkin) {

}
