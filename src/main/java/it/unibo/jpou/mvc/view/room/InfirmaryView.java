package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the Infirmary.
 */
public final class InfirmaryView extends AbstractRoomView {

    private static final String ACTION_BTN_STYLE = "action-button";

    private final Label potionLabel;
    private final Button useButton;
    private final Button nextPotionButton;

    private List<Potion> availablePotions = new ArrayList<>();
    private Map<Potion, Integer> currentPotionMap = Collections.emptyMap();
    private int currentIndex = -1;
    private Consumer<Potion> onUseHandler;

    /**
     * Initializes the Infirmary layout.
     */
    public InfirmaryView() {
        super("Infirmary");
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/styleInfirmaryView.css")).toExternalForm());
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/defaultRoom.css")).toExternalForm());
        this.getStyleClass().add("infirmary-view");

        this.potionLabel = new Label("Empty");
        this.potionLabel.getStyleClass().add("selected-item-label");

        this.addHeaderContent(this.potionLabel);

        this.useButton = new Button("Use");
        this.useButton.getStyleClass().add(ACTION_BTN_STYLE);
        this.useButton.setOnAction(_ -> triggerUse());

        this.nextPotionButton = new Button("Next");
        this.nextPotionButton.getStyleClass().add(ACTION_BTN_STYLE);
        this.nextPotionButton.setOnAction(_ -> scrollNext());

        this.getActionBar().getChildren().addAll(this.nextPotionButton, this.useButton);
    }

    /**
     * Updates the potion list and quantities.
     *
     * @param potions the map of potions and their counts.
     */
    public void refreshPotions(final Map<Potion, Integer> potions) {
        this.currentPotionMap = Map.copyOf(potions);
        this.availablePotions = new ArrayList<>(potions.keySet());

        if (this.availablePotions.isEmpty()) {
            this.potionLabel.setText("Empty");
            this.currentIndex = -1;
            this.useButton.setDisable(true);
        } else {
            this.currentIndex = 0;
            this.useButton.setDisable(false);
            updateDisplay();
        }
    }

    /**
     * Sets the use potion handler.
     *
     * @param handler the consumer.
     */
    public void setOnUsePotion(final Consumer<Potion> handler) {
        this.onUseHandler = handler;
    }

    private void scrollNext() {
        if (!this.availablePotions.isEmpty()) {
            this.currentIndex = (this.currentIndex + 1) % this.availablePotions.size();
            updateDisplay();
        }
    }

    private void triggerUse() {
        if (this.currentIndex >= 0 && !this.availablePotions.isEmpty() && this.onUseHandler != null) {
            this.onUseHandler.accept(this.availablePotions.get(this.currentIndex));
        }
    }

    private void updateDisplay() {
        if (this.currentIndex >= 0 && this.currentIndex < this.availablePotions.size()) {
            final Potion current = this.availablePotions.get(this.currentIndex);
            final int quantity = this.currentPotionMap.getOrDefault(current, 0);

            this.potionLabel.setText(current.getName().toUpperCase(Locale.ROOT) + " (x" + quantity + ")");
        }
    }
}
