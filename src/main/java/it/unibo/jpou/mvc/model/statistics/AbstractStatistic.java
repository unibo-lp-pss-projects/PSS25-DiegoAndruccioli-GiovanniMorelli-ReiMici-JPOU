package it.unibo.jpou.mvc.model.statistics;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Abstract base class for Pou statistics implementing common clamping logic.
 */
public abstract class AbstractStatistic implements PouStatistics {

    private final IntegerProperty value;

    /**
     * Initializes the statistic with the default initial value.
     */
    protected AbstractStatistic() {
        this.value = new SimpleIntegerProperty(STATISTIC_INITIAL_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getValueStat() {
        return this.value.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueStat(final int valueStat) {
        final int newValueStat;
        if (valueStat > STATISTIC_MAX_VALUE) {
            newValueStat = STATISTIC_MAX_VALUE;
        } else {
            newValueStat = Math.max(valueStat, STATISTIC_MIN_VALUE);
        }
        this.value.set(newValueStat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
            justification = "Standard JavaFX pattern implies returning the property object")
    public ReadOnlyIntegerProperty valueProperty() {
        return this.value;
    }
}
