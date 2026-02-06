package it.unibo.jpou.mvc.model.save;

/**
 * Root record acting as the container for the complete saved game state.
 *
 * @param statistics  the saved vital statistics, coins and state
 *                    (AWAKE/SLEEPING)
 * @param inventory   the saved inventory state
 * @param currentRoom the saved current room
 */
public record PouSaveData(SavedStatistics statistics, SavedInventory inventory, String currentRoom) {

}
