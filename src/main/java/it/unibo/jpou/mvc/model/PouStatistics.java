package it.unibo.jpou.mvc.model;

import javafx.beans.property.ReadOnlyIntegerProperty;

/**
 * Represents the numeric vital statistics of Pou, managing constraints for each
 * statistic through automatic clamping.
 */
public interface PouStatistics {

    /**
     * Maximum value for any statistic.
     */
    int STATISTIC_MAX_VALUE = 100;

    /**
     * Minimum value for any statistic.
     */
    int STATISTIC_MIN_VALUE = 0;

    /**
     * Initial value for any statistic.
     */
    int STATISTIC_INITIAL_VALUE = 50;

    /**
     * @return the current value of the statistic
     */
    int getValueStat();

    /**
     * Sets the value of the statistic, clamping it within the defined MAX - MIN.
     * 
     * @param valueStat the new value to set
     */
    void setValueStat(int valueStat);

    /**
     * @return the read only property to observe changes
     */
    ReadOnlyIntegerProperty valueProperty();

}
