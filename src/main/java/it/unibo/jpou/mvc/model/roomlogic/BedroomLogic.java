package it.unibo.jpou.mvc.model.roomlogic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouState;
import javafx.beans.property.ObjectProperty;

/**
 * Logic for Bedroom, actions sleep and wake-up.
 */
public final class BedroomLogic {

    private final ObjectProperty<PouState> state;

    /**
     * @param state the state property to modify
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Logic classes must modify the passed statistics")
    public BedroomLogic(final ObjectProperty<PouState> state) {
        this.state = state;
    }

    /**
     * Put Pou to sleep.
     */
    public void sleep() {
        this.state.set(PouState.SLEEPING);
    }

    /**
     * Wake Pou up.
     */
    public void wakeUp() {
        this.state.set(PouState.AWAKE);
    }
}
