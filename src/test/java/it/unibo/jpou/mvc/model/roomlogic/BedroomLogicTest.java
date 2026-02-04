package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BedroomLogicTest {

    private ObjectProperty<PouState> state;
    private BedroomLogic bedroomLogic;

    @BeforeEach
    void setUp() {
        this.state = new SimpleObjectProperty<>(PouState.AWAKE);
        this.bedroomLogic = new BedroomLogic(this.state);
    }

    @Test
    void testSleepIncreasesEnergy() {

        this.bedroomLogic.sleep();
        assertEquals(PouState.SLEEPING, this.state.get(),
                "Pou deve dormire");
    }

    @Test
    void testWakeUp() {
        this.state.set(PouState.SLEEPING);

        this.bedroomLogic.wakeUp();
        assertEquals(PouState.AWAKE, this.state.get(),
                "Pou deve svegliarsi");
    }
}
