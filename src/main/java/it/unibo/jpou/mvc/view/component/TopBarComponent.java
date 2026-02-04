package it.unibo.jpou.mvc.view.component;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

/**
 * Top bar component that displays coins, statistics progress bars, and settings.
 */
public final class TopBarComponent extends HBox {

    private static final double DEFAULT_SPACING = 20.0;
    private static final double STATS_SPACING = 15.0;
    private static final double BOX_SPACING = 3.0;
    private static final double INITIAL_PROGRESS = 0.5;

    private final Map<String, ProgressBar> progressBars = new HashMap<>();
    private final Map<String, Label> valueLabels = new HashMap<>();
    private final Label coinsLabel = new Label();
    private final Button settingsButton = new Button("Settings");

    /**
     * Constructs the top bar with dynamic statistics.
     *
     * @param statsKeys an array of strings representing the statistics to track.
     */
    public TopBarComponent(final String[] statsKeys) {
        super(DEFAULT_SPACING);
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().add("top-nav-bar");

        this.coinsLabel.getStyleClass().add("coins-label");

        final HBox statsBox = new HBox(STATS_SPACING);
        statsBox.setAlignment(Pos.CENTER);
        for (final String key : statsKeys) {
            createStatItem(statsBox, key);
        }

        final Region leftSpacer = new Region();
        final Region rightSpacer = new Region();
        setHgrow(leftSpacer, Priority.ALWAYS);
        setHgrow(rightSpacer, Priority.ALWAYS);

        this.getChildren().addAll(this.coinsLabel, leftSpacer, statsBox, rightSpacer, this.settingsButton);
    }

    private void createStatItem(final HBox container, final String key) {
        final VBox box = new VBox(BOX_SPACING);
        box.setAlignment(Pos.CENTER);

        final String formattedLabel = key.substring(0, 1).toUpperCase(Locale.ROOT) + key.substring(1);
        final Label label = new Label(formattedLabel);
        final ProgressBar bar = new ProgressBar(INITIAL_PROGRESS);
        final Label val = new Label("50/100");

        this.progressBars.put(key, bar);
        this.valueLabels.put(key, val);

        box.getChildren().addAll(label, bar, val);
        container.getChildren().add(box);
    }

    /**
     * Updates the UI for a specific statistic.
     *
     * @param key      the name of the statistic.
     * @param progress the progress value (0.0 to 1.0).
     * @param text     the textual representation of the value.
     */
    public void updateStat(final String key, final double progress, final String text) {
        if (this.progressBars.containsKey(key)) {
            this.progressBars.get(key).setProgress(progress);
            this.valueLabels.get(key).setText(text);
        }
    }

    /**
     * Updates the displayed coin balance.
     *
     * @param text the text to display (e.g., "Coins: 100").
     */
    public void updateCoins(final String text) {
        this.coinsLabel.setText(text);
    }

    /**
     * Sets the action for the settings button.
     *
     * @param handler the event handler to be triggered.
     */
    public void setOnSettingsAction(final EventHandler<ActionEvent> handler) {
        this.settingsButton.setOnAction(handler);
    }
}
