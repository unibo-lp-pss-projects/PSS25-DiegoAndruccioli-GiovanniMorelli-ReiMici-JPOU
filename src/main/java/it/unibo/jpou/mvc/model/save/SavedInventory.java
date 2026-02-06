package it.unibo.jpou.mvc.model.save;

import java.util.Collections;
import java.util.List;

/**
 * Record representing the saved state of the inventory.
 *
 * @param items         the list of consumable items (food and potions) currently owned
 * @param unlockedSkins the list of names of the skins that been unlocked/purchased
 * @param equippedSkin  the name of the skin currently equipped
 */
public record SavedInventory(List<SavedItem> items, List<String> unlockedSkins, String equippedSkin) {

    /**
     * Compact constructor to ensure immutability by creating defensive copies.
     *
     * @param items         the list of consumable items (food and potions) currently owned
     * @param unlockedSkins the list of names of the skins that been unlocked/purchased
     * @param equippedSkin  the name of the skin currently equipped
     */
    public SavedInventory {
        /* If the list is null it returns an empty list */
        items = items != null ? List.copyOf(items) : Collections.emptyList();
        unlockedSkins = unlockedSkins != null ? List.copyOf(unlockedSkins) : Collections.emptyList();
    }

    @Override
    public List<SavedItem> items() {
        return List.copyOf(items);
    }

    @Override
    public List<String> unlockedSkins() {
        return List.copyOf(unlockedSkins);
    }
}
