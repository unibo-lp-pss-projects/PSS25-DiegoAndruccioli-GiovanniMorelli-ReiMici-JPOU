package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouState;
import javafx.beans.property.ObjectProperty;

/**
 * Logic for Bedroom, actions sleep and wake-up.
 */
public final class BedroomLogic {

    /**
     * Put Pou to sleep.
     *
     * @param state  the state property to modify
     */
    public void sleep(final ObjectProperty<PouState> state) {
        state.set(PouState.SLEEPING);
    }

    /**
     * Wake Pou up.
     *
     * @param state  the state property to modify
     */
    public void wakeUp(final ObjectProperty<PouState> state) {
        state.set(PouState.AWAKE);
    }
}
