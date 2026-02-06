package it.unibo.jpou.mvc.model;

import it.unibo.jpou.mvc.model.decay.PouStatisticsDecay;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import it.unibo.jpou.mvc.model.items.durable.skin.DefaultSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.Skin;
import it.unibo.jpou.mvc.model.roomlogic.BathroomLogic;
import it.unibo.jpou.mvc.model.roomlogic.BedroomLogic;
import it.unibo.jpou.mvc.model.roomlogic.GameRoomLogic;
import it.unibo.jpou.mvc.model.roomlogic.InfirmaryLogic;
import it.unibo.jpou.mvc.model.roomlogic.KitchenLogic;
import it.unibo.jpou.mvc.model.statistics.EnergyStatistic;
import it.unibo.jpou.mvc.model.statistics.FunStatistic;
import it.unibo.jpou.mvc.model.statistics.HealthStatistic;
import it.unibo.jpou.mvc.model.statistics.HungerStatistic;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Main logic class for Pou.
 * Orchestrates interactions between Statistics, Logic components, and Items.
 */
public final class PouLogic {

    private final PouStatistics hunger;
    private final PouStatistics energy;
    private final PouStatistics fun;
    private final PouStatistics health;
    private final PouCoins coins;

    private final ReadOnlyObjectWrapper<PouState> state;
    private final ReadOnlyObjectWrapper<Skin> currentSkin;

    private final BathroomLogic bathroomLogic;
    private final BedroomLogic bedroomLogic;
    private final GameRoomLogic gameRoomLogic;
    private final InfirmaryLogic infirmaryLogic;
    private final KitchenLogic kitchenLogic;

    private final PouStatisticsDecay decayLogic;

    private final IntegerProperty age;

    /**
     * Initializes Pou with default statistics and state.
     */
    public PouLogic() {
        this.hunger = new HungerStatistic();
        this.energy = new EnergyStatistic();
        this.fun = new FunStatistic();
        this.health = new HealthStatistic();
        this.coins = new PouCoins();

        this.state = new ReadOnlyObjectWrapper<>(PouState.AWAKE);
        this.currentSkin = new ReadOnlyObjectWrapper<>(new DefaultSkin());

        this.bathroomLogic = new BathroomLogic();
        this.bedroomLogic = new BedroomLogic();
        this.gameRoomLogic = new GameRoomLogic();
        this.infirmaryLogic = new InfirmaryLogic();
        this.kitchenLogic = new KitchenLogic();

        this.decayLogic = new PouStatisticsDecay();

        this.health.valueProperty().addListener((_, _, newValue) -> {
            if (newValue.intValue() <= PouStatistics.STATISTIC_MIN_VALUE) {
                handleDeath();
            }
        });

        this.age = new SimpleIntegerProperty(0);
    }

    /**
     * Eat a specific food item.
     *
     * @param food the food to consume.
     */
    public void eat(final Food food) {
        if (canModify() && food != null) {
            this.kitchenLogic.eat(this.hunger, food);
        }
    }

    /**
     * Use a specific potion.
     *
     * @param potion the potion to drink.
     */
    public void usePotion(final Potion potion) {
        if (canModify() && potion != null) {
            this.infirmaryLogic.usePotion(this.energy, this.health, potion);
        }
    }

    /**
     * Wear a new skin.
     *
     * @param skin the skin to wear.
     */
    public void setSkin(final Skin skin) {
        if (canModify() && skin != null) {
            this.currentSkin.set(skin);
        }
    }

    /**
     * Put Pou to sleep.
     */
    public void sleep() {
        if (this.state.get() != PouState.DEAD) {

            this.bedroomLogic.sleep(this.state);
        }
    }

    /**
     * Wakes Pou up.
     */
    public void wakeUp() {
        if (this.state.get() != PouState.DEAD) {
            this.bedroomLogic.wakeUp(this.state);
        }
    }

    /**
     * Wash Pou.
     */
    public void wash() {
        if (canModify()) {
            this.bathroomLogic.wash(this.health);
        }
    }

    /**
     * Play with Pou.
     */
    public void play() {
        if (canModify()) {
            this.gameRoomLogic.play(this.fun);
        }
    }

    /**
     * @return current state.
     */
    public PouState getState() {
        return this.state.get();
    }

    /**
     * @return state property (read-only).
     */
    public ReadOnlyObjectProperty<PouState> stateProperty() {

        return this.state.getReadOnlyProperty();
    }

    /**
     * @return current skin.
     */
    public Skin getSkin() {
        return this.currentSkin.get();
    }

    /**
     * @return skin property (read-only).
     */
    public ReadOnlyObjectProperty<Skin> skinProperty() {
        // SOLUZIONE SPOTBUGS
        return this.currentSkin.getReadOnlyProperty();
    }

    /**
     * @return hunger value.
     */
    public int getHunger() {
        return this.hunger.getValueStat();
    }

    /**
     * @param v new hunger value.
     */
    public void setHunger(final int v) {
        if (canModify()) {
            this.hunger.setValueStat(v);
        }
    }

    /**
     * @return energy value.
     */
    public int getEnergy() {
        return this.energy.getValueStat();
    }

    /**
     * @param v new energy value.
     */
    public void setEnergy(final int v) {
        if (canModify()) {
            this.energy.setValueStat(v);
        }
    }

    /**
     * @return fun value.
     */
    public int getFun() {
        return this.fun.getValueStat();
    }

    /**
     * @param v new fun value.
     */
    public void setFun(final int v) {
        if (canModify()) {
            this.fun.setValueStat(v);
        }
    }

    /**
     * @return health value.
     */
    public int getHealth() {
        return this.health.getValueStat();
    }

    /**
     * @param v new health value.
     */
    public void setHealth(final int v) {
        if (canModify()
                || v <= PouStatistics.STATISTIC_MIN_VALUE && this.state.get() != PouState.DEAD) {
            this.health.setValueStat(v);
        }
    }

    /**
     * @return coins amount.
     */
    public int getCoins() {
        return this.coins.getCoins();
    }

    /**
     * @param v new coins amount.
     */
    public void setCoins(final int v) {
        if (canModify()) {
            this.coins.setCoins(v);
        }
    }

    /**
     * Applies the decay to the statistics based on the current state.
     */
    public void applyDecay() {
        if (canModify() || this.state.get() == PouState.SLEEPING) {
            this.decayLogic.performDecay(this.hunger, this.energy, this.fun, this.health, this.state, this.age);
        }
    }

    private boolean canModify() {
        return this.state.get() == PouState.AWAKE;
    }

    private void handleDeath() {
        this.state.set(PouState.DEAD);
        final int min = PouStatistics.STATISTIC_MIN_VALUE;
        this.hunger.setValueStat(min);
        this.energy.setValueStat(min);
        this.fun.setValueStat(min);
        this.health.setValueStat(min);
        this.coins.setCoins(min);
    }

    /**
     * Adds coins to the Pou entity.
     *
     * @param amount the amount of coins to add.
     */
    public void addCoins(final int amount) {
        // Prendo le monete attuali, aggiungo amount, e uso il setter.
        // Il setter si occupa già di controllare canModify().
        setCoins(getCoins() + amount);
    }

    /**
     * @return current age.
     */
    public int getAge() {
        return this.age.get();
    }

    /**
     * @return age property.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Property exposure is required for JavaFX binding")
    public IntegerProperty ageProperty() {
        return this.age;
    }

    /**
     * Sets the age of Pou.
     * Useful for loading saved data.
     *
     * @param v the age value.
     */
    public void setAge(final int v) {
        this.age.set(Math.max(0, v));
    }

    /**
     * Resets Pou to initial conditions.
     */
    public void reset() {
        this.hunger.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        this.energy.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        this.fun.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);
        this.health.setValueStat(PouStatistics.STATISTIC_INITIAL_VALUE);

        this.coins.setCoins(PouCoins.MIN_COINS);

        this.state.set(PouState.AWAKE);

        this.age.set(0);
    }
}
