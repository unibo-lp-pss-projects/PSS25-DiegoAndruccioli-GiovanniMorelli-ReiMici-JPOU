package it.unibo.jpou.mvc.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.roomlogic.BathroomLogic;
import it.unibo.jpou.mvc.model.roomlogic.BedroomLogic;
import it.unibo.jpou.mvc.model.roomlogic.GameRoomLogic;
import it.unibo.jpou.mvc.model.roomlogic.InfirmaryLogic;
import it.unibo.jpou.mvc.model.roomlogic.KitchenLogic;
import it.unibo.jpou.mvc.model.statistics.HungerStatistic;
import it.unibo.jpou.mvc.model.statistics.EnergyStatistic;
import it.unibo.jpou.mvc.model.statistics.FunStatistic;
import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Main logic class for Pou, aggregating statistics and state.
 */
public final class PouLogic {

    private final PouStatistics hunger;
    private final PouStatistics energy;
    private final PouStatistics fun;
    private final PouStatistics health;
    private final PouCoins coins;
    private final ObjectProperty<PouState> state;

    private final BathroomLogic bathroomLogic;
    private final BedroomLogic bedroomLogic;
    private final GameRoomLogic gameRoomLogic;
    private final InfirmaryLogic infirmaryLogic;
    private final KitchenLogic kitchenLogic;

    /**
     * Initializes Pou with default statistics and state.
     */
    public PouLogic() {
        this.hunger = new HungerStatistic();
        this.energy = new EnergyStatistic();
        this.fun = new FunStatistic();
        this.health = new HealthStatistic();
        this.coins = new PouCoins();
        this.state = new SimpleObjectProperty<>(PouState.AWAKE);

        this.bathroomLogic = new BathroomLogic(this.health);
        this.bedroomLogic = new BedroomLogic(this.state);
        this.gameRoomLogic = new GameRoomLogic(this.fun);
        this.infirmaryLogic = new InfirmaryLogic(this.energy, this.health);
        this.kitchenLogic = new KitchenLogic(this.hunger);

        this.health.valueProperty().addListener(
                (_, _, newValue) -> {
                    if (newValue.intValue() <= PouStatistics.STATISTIC_MIN_VALUE) {
                        handleDeath();
                    }
                });
    }

    /**
     * Puts Pou to sleep in BedroomLogic.
     */
    public void sleep() {
        if (this.state.get() != PouState.DEAD) {
            this.bedroomLogic.sleep();
        }
    }

    /**
     * Wakes Pou up in BedroomLogic.
     */
    public void wakeUp() {
        if (this.state.get() != PouState.DEAD) {
            this.bedroomLogic.wakeUp();
        }
    }

    /**
     * Wash Pou in BathroomLogic.
     */
    public void wash() {
        if (canModify()) {
            this.bathroomLogic.wash();
        }
    }

    /**
     * Play with Pou in GameRoomLogic.
     */
    public void play() {
        if (canModify()) {
            this.gameRoomLogic.play();
        }
    }

    /**
     * Use potion in Infirmary.
     * 
     * @param potionName the potion to use
     */
    public void usePotion(final String potionName) {
        if (canModify()) {
            this.infirmaryLogic.usePotion(potionName);
        }
    }

    /**
     * Feed Pou in KitchenLogic.
     */
    public void eat() {
        if (canModify()) {
            this.kitchenLogic.eat();
        }
    }

    /**
     * @return the current state of Pou
     */
    public PouState getState() {
        return this.state.get();
    }

    /**
     * @return current hunger value
     */
    public int getHunger() {
        return this.hunger.getValueStat();
    }

    /**
     * @param value new hunger value
     */
    public void setHunger(final int value) {
        if (canModify()) {
            this.hunger.setValueStat(value);
        }
    }

    /**
     * @return current energy value
     */
    public int getEnergy() {
        return this.energy.getValueStat();
    }

    /**
     * @param value new energy value
     */
    public void setEnergy(final int value) {
        if (canModify()) {
            this.energy.setValueStat(value);
        }
    }

    /**
     * @return current fun value
     */
    public int getFun() {
        return this.fun.getValueStat();
    }

    /**
     * @param value new fun value
     */
    public void setFun(final int value) {
        if (canModify()) {
            this.fun.setValueStat(value);
        }
    }

    /**
     * @return current health value
     */
    public int getHealth() {
        return this.health.getValueStat();
    }

    /**
     * @param value new health value
     */
    public void setHealth(final int value) {
        if (canModify() || value <= PouStatistics.STATISTIC_MIN_VALUE && this.state.get() != PouState.DEAD) {
            this.health.setValueStat(value);
        }
    }

    /**
     * @return current wallet amount
     */
    public int getCoins() {
        return this.coins.getCoins();
    }

    /**
     * @param value new energy value
     */
    public void setCoins(final int value) {
        if (canModify()) {
            this.coins.setCoins(value);
        }
    }

    /**
     * @return the Observable State Property
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Standard JavaFX pattern implies returning the property object")
    public ObjectProperty<PouState> stateProperty() {
        return this.state;
    }

    private boolean canModify() {
        return this.state.get() == PouState.AWAKE;
    }

    private void handleDeath() {
        this.state.set(PouState.DEAD);

        final int deadValue = PouStatistics.STATISTIC_MIN_VALUE;

        this.hunger.setValueStat(deadValue);
        this.energy.setValueStat(deadValue);
        this.fun.setValueStat(deadValue);
        this.health.setValueStat(deadValue);
        this.coins.setCoins(deadValue);
    }
}
