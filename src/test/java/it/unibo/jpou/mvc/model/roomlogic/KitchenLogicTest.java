package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.statistics.HungerStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KitchenLogicTest {

    private PouStatistics hunger;
    private KitchenLogic kitchenLogic;

    @BeforeEach
    void setUp() {
        this.hunger = new HungerStatistic();
        this.kitchenLogic = new KitchenLogic();
    }

    @Test
    void testIncreasesHunger() {
        final int initialHunger = this.hunger.getValueStat();
        this.kitchenLogic.eat(this.hunger);

        assertEquals(initialHunger + KitchenLogic.INCREMENT_ACTION_EAT, this.hunger.getValueStat(),
                "Mangiare deve aumentare la fame");
    }

}
