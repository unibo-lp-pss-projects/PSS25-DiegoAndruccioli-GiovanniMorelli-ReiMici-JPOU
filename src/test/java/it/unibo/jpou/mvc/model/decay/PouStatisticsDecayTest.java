package it.unibo.jpou.mvc.model.decay;

import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.statistics.EnergyStatistic;
import it.unibo.jpou.mvc.model.statistics.FunStatistic;
import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import it.unibo.jpou.mvc.model.statistics.HungerStatistic;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PouStatisticsDecayTest {

    private HungerStatistic hunger;
    private EnergyStatistic energy;
    private FunStatistic fun;
    private HealthStatistic health;
    private ObjectProperty<PouState> state;
    private IntegerProperty age;
    private PouStatisticsDecay statisticsDecay;

    @BeforeEach
    void setUp() {
        hunger = new HungerStatistic();
        energy = new EnergyStatistic();
        fun = new FunStatistic();
        health = new HealthStatistic();
        state = new SimpleObjectProperty<>(PouState.AWAKE);
        age = new SimpleIntegerProperty(0);
        statisticsDecay = new PouStatisticsDecay();
    }

    @Test
    void testDecayWhileAwake() {
        hunger.setValueStat(PouStatistics.STATISTIC_MAX_VALUE);
        energy.setValueStat(PouStatistics.STATISTIC_MAX_VALUE);
        fun.setValueStat(PouStatistics.STATISTIC_MAX_VALUE);
        health.setValueStat(PouStatistics.STATISTIC_MAX_VALUE);
        state.set(PouState.AWAKE);

        statisticsDecay.performDecay(this.hunger, this.energy, this.fun, this.health, this.state, this.age);

        assertAll("Tutte le statistiche devono decadere",
                () -> assertTrue(hunger.getValueStat() < PouStatistics.STATISTIC_MAX_VALUE),
                () -> assertTrue(energy.getValueStat() < PouStatistics.STATISTIC_MAX_VALUE),
                () -> assertTrue(fun.getValueStat() < PouStatistics.STATISTIC_MAX_VALUE),
                () -> assertTrue(health.getValueStat() < PouStatistics.STATISTIC_MAX_VALUE));
    }

    @Test
    void testDecayWhileSleeping() {
        hunger.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        energy.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        fun.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        health.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        state.set(PouState.SLEEPING);

        statisticsDecay.performDecay(this.hunger, this.energy, this.fun, this.health, this.state, this.age);

        assertAll("Il decadimento delle statistiche mentre dorme",
                () -> assertTrue(hunger.getValueStat() < PouStatistics.STATISTIC_INITIAL_VALUE),
                () -> assertTrue(energy.getValueStat() > PouStatistics.STATISTIC_INITIAL_VALUE),
                () -> assertTrue(fun.getValueStat() < PouStatistics.STATISTIC_INITIAL_VALUE),
                () -> assertTrue(health.getValueStat() > PouStatistics.STATISTIC_INITIAL_VALUE));
    }

    @Test
    void testHealthPenalityWhenStatisticsAreZero() {
        hunger.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        energy.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        fun.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        health.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        state.set(PouState.AWAKE);

        hunger.setValueStat(PouStatistics.STATISTIC_MIN_VALUE);

        final int initialHealth = health.getValueStat();

        statisticsDecay.performDecay(this.hunger, this.energy, this.fun, this.health, this.state, this.age);

        assertTrue(health.getValueStat() < initialHealth,
                "La salute deve diminuire se una delle altre statistiche è a 0");
    }
}
