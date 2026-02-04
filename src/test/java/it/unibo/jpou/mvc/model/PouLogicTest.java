package it.unibo.jpou.mvc.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PouLogicTest {

        private static final int POU_DEAD = 0;
        private static final int INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD = 10;
        private static final int INCREMENT_STATISTIC_ACTION = 20;

        private PouLogic pou;

        @BeforeEach
        void setUp() {
                this.pou = new PouLogic();
        }

        @Test
        void testInitialState() {
                assertAll("Controllo stato iniziale",
                                () -> assertEquals(PouState.AWAKE, pou.getState(),
                                                "Stato iniziale SVEGLIO"),
                                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getHunger(),
                                                "Hunger deve avere il valore iniziale"),
                                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getEnergy(),
                                                "Energy deve avere il valore iniziale"),
                                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getFun(),
                                                "Fun deve avere il valore iniziale"),
                                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, pou.getHealth(),
                                                "Health deve avere il valore iniziale"),
                                () -> assertEquals(PouCoins.MIN_COINS, pou.getCoins(),
                                                "Il wallet deve avere il valore iniziale"));
        }

        @Test
        void testDeathLogic() {

                pou.setHealth(POU_DEAD);

                assertEquals(PouState.DEAD, pou.getState(), "Se la salute è 0, Pou deve essere morto");
                assertAll("Da morto le statistiche e il wallet devono essere a 0",
                                () -> assertEquals(POU_DEAD, pou.getHunger()),
                                () -> assertEquals(POU_DEAD, pou.getEnergy()),
                                () -> assertEquals(POU_DEAD, pou.getFun()),
                                () -> assertEquals(POU_DEAD, pou.getCoins()));
        }

        @Test
        void testActionsBlockWhenSleeping() {

                pou.sleep();

                assertEquals(PouState.SLEEPING, pou.getState(),
                                "Pou deve dormire");

                final int initialHunger = pou.getHunger();
                final int initialEnergy = pou.getEnergy();
                final int initialFun = pou.getFun();
                final int initialHealth = pou.getHealth();
                final int initialCoins = pou.getCoins();

                pou.setHunger(initialHunger + INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setEnergy(initialEnergy + INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setFun(initialFun + INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setHealth(initialHealth + INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setCoins(initialCoins + INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);

                assertAll("Quando dorme le statistiche e il wallet non devono cambiare",
                                () -> assertEquals(initialHunger, pou.getHunger()),
                                () -> assertEquals(initialEnergy, pou.getEnergy()),
                                () -> assertEquals(initialFun, pou.getFun()),
                                () -> assertEquals(initialHealth, pou.getHealth()),
                                () -> assertEquals(initialCoins, pou.getCoins()));
        }

        @Test
        void testActionsBlockWhenDead() {

                pou.setHealth(POU_DEAD);

                assertEquals(PouState.DEAD, pou.getState(),
                                "Dev'essere morto");

                pou.setHunger(INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setEnergy(INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setFun(INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setHealth(INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);
                pou.setCoins(INCREMENT_STATISTIC_WHEN_POU_SLEEPING_DEAD);

                assertAll("Quando è morto le statistiche e il wallet devono essere 0",
                                () -> assertEquals(POU_DEAD, pou.getHunger()),
                                () -> assertEquals(POU_DEAD, pou.getEnergy()),
                                () -> assertEquals(POU_DEAD, pou.getFun()),
                                () -> assertEquals(POU_DEAD, pou.getHealth()),
                                () -> assertEquals(POU_DEAD, pou.getCoins()));
        }

        @Test
        void testWashActionDelegation() {
                pou.wash();
                assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE + INCREMENT_STATISTIC_ACTION, pou.getHealth(),
                                "Wash delega a BathroomLogic");
        }

        @Test
        void testSleepActionDelegation() {
                pou.sleep();
                assertEquals(PouState.SLEEPING, pou.getState(),
                                "Sleep delega a BedroomLogic");
        }

        @Test
        void testWakeUpActionDelegation() {
                pou.sleep();
                pou.wakeUp();
                assertEquals(PouState.AWAKE, pou.getState(),
                                "WakeUp delega a BedroomLogic");
        }

        @Test
        void testPlayActionDelegation() {
                pou.play();
                assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE + INCREMENT_STATISTIC_ACTION, pou.getFun(),
                                "Play delega a GameRoomLogic");
        }

        @Test
        void testEatActionDelegation() {
                pou.eat();
                assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE + INCREMENT_STATISTIC_ACTION, pou.getHunger(),
                                "Eat delega a KitchenLogic");
        }
}
