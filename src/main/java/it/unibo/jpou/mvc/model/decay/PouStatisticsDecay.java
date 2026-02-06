package it.unibo.jpou.mvc.model.decay;

import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.IntegerProperty;

/**
 * Handles the automatic decay of Pou's statistics over time.
 */
public final class PouStatisticsDecay {

    private static final int AWAKE_DECAY = 3;
    private static final int SLEEPING_DECAY = 1;
    private static final int REGENERATION_ENERGY = 3;
    private static final int REGENERATION_HEALTH = 3;
    private static final int HEALTH_PENALITY = 10;

    private static final int MAX_AGE = 720;

    /**
     * Performs decay logic on the provided statistics.
     *
     * @param hunger statistic to modify
     * @param energy statistic to modify
     * @param fun    statistic to modify
     * @param health statistic to modify
     * @param state  statistic to modify
     * @param age    property to increment
     */
    public void performDecay(final PouStatistics hunger,
            final PouStatistics energy,
            final PouStatistics fun,
            final PouStatistics health,
            final ReadOnlyObjectProperty<PouState> state,
            final IntegerProperty age) {

        if (state.get() == PouState.DEAD) {
            return;
        }

        final int currentAge = age.get();
        age.set(currentAge + 1);

        if (age.get() >= MAX_AGE) {
            health.setValueStat(0);
            return;
        }

        if (state.get() == PouState.AWAKE) {
            hunger.setValueStat(hunger.getValueStat() - AWAKE_DECAY);
            energy.setValueStat(energy.getValueStat() - AWAKE_DECAY);
            fun.setValueStat(fun.getValueStat() - AWAKE_DECAY);
            health.setValueStat(health.getValueStat() - AWAKE_DECAY);
        } else if (state.get() == PouState.SLEEPING) {
            hunger.setValueStat(hunger.getValueStat() - SLEEPING_DECAY);
            energy.setValueStat(energy.getValueStat() + REGENERATION_ENERGY);
            fun.setValueStat(fun.getValueStat() - SLEEPING_DECAY);
            health.setValueStat(health.getValueStat() + REGENERATION_HEALTH);
        }

        if (hunger.getValueStat() <= PouStatistics.STATISTIC_MIN_VALUE
                || energy.getValueStat() <= PouStatistics.STATISTIC_MIN_VALUE
                || fun.getValueStat() <= PouStatistics.STATISTIC_MIN_VALUE) {

            health.setValueStat(health.getValueStat() - HEALTH_PENALITY);
        }
    }
}
