package it.unibo.jpou.mvc.controller.persistence;

import it.unibo.jpou.mvc.model.Room;

/**
 * Interface for the controller responsible for data persistence.
 * It handles the conversion between the game model and the saved data format.
 */
public interface PersistenceController {

    /**
     * Load the game data and update the model, if the loaded state is DEAD, it resets the game to default.
     *
     * @return the name of the room where the game should start
     */
    String loadGame();

    /**
     * Save the current game state.
     *
     * @param currentRoom the room the user is currently in
     */
    void saveGame(Room currentRoom);
}
