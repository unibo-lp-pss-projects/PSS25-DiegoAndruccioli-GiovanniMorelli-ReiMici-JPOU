package it.unibo.jpou.mvc.model.roomlogic;

import it.unibo.jpou.mvc.model.items.consumable.food.Apple;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import it.unibo.jpou.mvc.model.statistics.HungerStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KitchenLogicTest {

    private static final int INITIAL_VALUE = 50;
    private KitchenLogic kitchenLogic;
    private HungerStatistic hunger;

    @BeforeEach
    void setUp() {
        kitchenLogic = new KitchenLogic();
        hunger = new HungerStatistic();
        hunger.setValueStat(INITIAL_VALUE);
    }

    @Test
    void testEat() {
        final Food apple = new Apple();
        final int expectedValue = INITIAL_VALUE + apple.getEffectValue();
        kitchenLogic.eat(hunger, apple);
        assertEquals(expectedValue, hunger.getValueStat());
    }

    @Test
    void testEatNull() {
        kitchenLogic.eat(hunger, null);
        assertEquals(INITIAL_VALUE, hunger.getValueStat());
    }
}
