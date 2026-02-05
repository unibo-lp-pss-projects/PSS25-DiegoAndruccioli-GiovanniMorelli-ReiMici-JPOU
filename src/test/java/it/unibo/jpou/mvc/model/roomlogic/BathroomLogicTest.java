package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import it.unibo.jpou.mvc.model.PouStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BathroomLogicTest {

    private static final int SET_HEALTH_VALUE = 99;
    private PouStatistics health;
    private BathroomLogic bathroomLogic;

    @BeforeEach
    void setUp() {
        this.health = new HealthStatistic();
        this.bathroomLogic = new BathroomLogic();
    }

    @Test
    void testWashIncreasesHealth() {
        final int initialHealth = this.health.getValueStat();

        this.bathroomLogic.wash(this.health);
        assertEquals(initialHealth + BathroomLogic.INCREMENT_ACTION_WASH, this.health.getValueStat(),
                "La salute è aumentata");
    }

    @Test
    void testWashClamping() {
        this.health.setValueStat(SET_HEALTH_VALUE);

        this.bathroomLogic.wash(this.health);
        assertEquals(PouStatistics.STATISTIC_MAX_VALUE, this.health.getValueStat(),
                "L'azione wash non deve far aumentare la statistica sopra al limite");
    }
}
