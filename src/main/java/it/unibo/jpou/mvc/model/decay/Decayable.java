package it.unibo.jpou.mvc.model.decay;

/**
 * Represents an automatically updated game entity, managed by GameEngine.
 */
public interface Decayable {

    /**
     * Updates the object's state with each game tick.
     */
    void performDecayTick();
}
