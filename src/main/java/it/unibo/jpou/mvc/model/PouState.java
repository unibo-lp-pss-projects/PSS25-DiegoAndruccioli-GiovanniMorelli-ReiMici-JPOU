package it.unibo.jpou.mvc.model;

/**
 * Represents the fundamental vital states of Pou, determining which are allowed at a given time.
 */
public enum PouState {
    /**
     * Normal state: Pou is awake and can interact.
     */
    AWAKE,
    /**
     * Resting state: Pou is sleeping and can only perform 'sleep' actions.
     */
    SLEEPING,
    /**
     * Terminal state: Pou is dead and all interactions are disabled and the game ends.
     */
    DEAD
}
