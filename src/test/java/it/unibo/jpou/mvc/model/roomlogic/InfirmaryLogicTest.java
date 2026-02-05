package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.items.consumable.potion.EnergyPotion;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import it.unibo.jpou.mvc.model.statistics.EnergyStatistic;
import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InfirmaryLogicTest {

    private static final int INITIAL_VALUE = 50;
    private InfirmaryLogic infirmaryLogic;
    private EnergyStatistic energy;
    private HealthStatistic health;

    @BeforeEach
    void setUp() {
        infirmaryLogic = new InfirmaryLogic();
        energy = new EnergyStatistic();
        health = new HealthStatistic();
        energy.setValueStat(INITIAL_VALUE);
        health.setValueStat(INITIAL_VALUE);
    }

    @Test
    void testUseHealthPotion() {
        final HealthPotion potion = new HealthPotion();
        final int expectedHealth = INITIAL_VALUE + potion.getEffectValue();
        infirmaryLogic.usePotion(energy, health, potion);
        assertEquals(expectedHealth, health.getValueStat());
        assertEquals(INITIAL_VALUE, energy.getValueStat());
    }

    @Test
    void testUseEnergyPotion() {
        final EnergyPotion potion = new EnergyPotion();
        final int expectedEnergy = INITIAL_VALUE + potion.getEffectValue();
        infirmaryLogic.usePotion(energy, health, potion);
        assertEquals(expectedEnergy, energy.getValueStat());
        assertEquals(INITIAL_VALUE, health.getValueStat());
    }
}
