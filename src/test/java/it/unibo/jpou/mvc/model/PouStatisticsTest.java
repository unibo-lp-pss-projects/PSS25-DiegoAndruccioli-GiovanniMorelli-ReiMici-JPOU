package it.unibo.jpou.mvc.model;

import it.unibo.jpou.mvc.model.statistics.EnergyStatistic;
import it.unibo.jpou.mvc.model.statistics.FunStatistic;
import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import it.unibo.jpou.mvc.model.statistics.HungerStatistic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for PouStatistics.
 */
class PouStatisticsTest {

    private static final int STATISTIC_MAX_ERROR_VALUE = 150;
    private static final int STATISTIC_MIN_ERROR_VALUE = -50;
    private static final int STATISTIC_CHANGE_VALUE = 75;

    @Test
    void testClampingStatistics() {
        final PouStatistics hunger = new HungerStatistic();
        final PouStatistics energy = new EnergyStatistic();
        final PouStatistics fun = new FunStatistic();
        final PouStatistics health = new HealthStatistic();

        hunger.setValueStat(STATISTIC_MAX_ERROR_VALUE);
        energy.setValueStat(STATISTIC_MAX_ERROR_VALUE);
        fun.setValueStat(STATISTIC_MAX_ERROR_VALUE);
        health.setValueStat(STATISTIC_MAX_ERROR_VALUE);

        assertAll("Il valore MAX delle statistiche non deve superare 100",
                () -> assertEquals(PouStatistics.STATISTIC_MAX_VALUE, hunger.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_MAX_VALUE, energy.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_MAX_VALUE, fun.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_MAX_VALUE, health.getValueStat())
        );

        hunger.setValueStat(STATISTIC_MIN_ERROR_VALUE);
        energy.setValueStat(STATISTIC_MIN_ERROR_VALUE);
        fun.setValueStat(STATISTIC_MIN_ERROR_VALUE);
        health.setValueStat(STATISTIC_MIN_ERROR_VALUE);

        assertAll("Il valore MIN delle statistiche non deve essere negativo",
                () -> assertEquals(PouStatistics.STATISTIC_MIN_VALUE, hunger.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_MIN_VALUE, energy.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_MIN_VALUE, fun.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_MIN_VALUE, health.getValueStat())
        );
    }

    @Test
    void testInitialValues() {
        assertAll("Il valore iniziale delle statistiche dev'essere quello di default",
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, new HungerStatistic().getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, new EnergyStatistic().getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, new FunStatistic().getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, new HealthStatistic().getValueStat())
        );
    }

    @Test
    void testPropertiesUpdate() {
        final PouStatistics hunger = new HungerStatistic();
        final PouStatistics energy = new EnergyStatistic();
        final PouStatistics fun = new FunStatistic();
        final PouStatistics health = new HealthStatistic();

        assertAll("Le Property delle statistiche non devono essere vuote",
                () -> assertNotNull(hunger.valueProperty()),
                () -> assertNotNull(energy.valueProperty()),
                () -> assertNotNull(fun.valueProperty()),
                () -> assertNotNull(health.valueProperty())
                );

        hunger.setValueStat(STATISTIC_CHANGE_VALUE);
        energy.setValueStat(STATISTIC_CHANGE_VALUE);
        fun.setValueStat(STATISTIC_CHANGE_VALUE);
        health.setValueStat(STATISTIC_CHANGE_VALUE);

        assertAll("Le Property devono aggiornarsi quando cambiano",
                () -> assertEquals(STATISTIC_CHANGE_VALUE, hunger.valueProperty().get()),
                () -> assertEquals(STATISTIC_CHANGE_VALUE, energy.valueProperty().get()),
                () -> assertEquals(STATISTIC_CHANGE_VALUE, fun.valueProperty().get()),
                () -> assertEquals(STATISTIC_CHANGE_VALUE, health.valueProperty().get())
                );
    }
}
