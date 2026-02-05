package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.statistics.EnergyStatistic;
import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InfirmaryLogicTest {

    private PouStatistics energy;
    private PouStatistics health;
    private InfirmaryLogic infirmaryLogic;

    @BeforeEach
    void setUp() {
        this.energy = new EnergyStatistic();
        this.health = new HealthStatistic();
        this.infirmaryLogic = new InfirmaryLogic();
    }

    @Test
    void testUseEnergyPotion() {
        final int initialEnergy = this.energy.getValueStat();

        this.infirmaryLogic.usePotion("EnergyPotion", this.energy, this.health);

        assertAll("La pozione aumenta solo la statistica dell'energia",
                () -> assertEquals(initialEnergy + InfirmaryLogic.INCREMENT_ENERGY_POTION,
                        this.energy.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, this.health.getValueStat()));
    }

    @Test
    void testUseHealthPotion() {
        final int initialHealth = this.health.getValueStat();

        this.infirmaryLogic.usePotion("HealthPotion", this.energy, this.health);

        assertAll("La pozione aumenta solo la statistica della salute",
                () -> assertEquals(initialHealth + InfirmaryLogic.INCREMENT_HEALTH_POTION,
                        this.health.getValueStat()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, this.energy.getValueStat()));
    }
}
