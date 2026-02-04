package it.unibo.jpou.mvc.model;

/**
 * Defines the accessible rooms in the game.
 * Used as a unique source of truth for navigation and logic.
 */
public enum Room {
    /** Bedroom room. */
    BEDROOM("Bedroom"),
    /** Bathroom room. */
    BATHROOM("Bathroom"),
    /** Game Room. */
    GAME_ROOM("Game Room"),
    /** Infirmary room. */
    INFIRMARY("Infirmary"),
    /** Kitchen room. */
    KITCHEN("Kitchen"),
    /** Shop for purchasing items. */
    SHOP("Shop");

    private final String label;

    /**
     * @param label the display name of the room.
     */
    Room(final String label) {
        this.label = label;
    }

    /**
     * @return the human-readable label of the room.
     */
    public String getLabel() {
        return this.label;
    }
}
