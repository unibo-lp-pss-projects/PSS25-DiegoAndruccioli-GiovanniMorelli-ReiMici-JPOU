package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the Kitchen.
 */
public final class KitchenView extends AbstractRoomView {

    private static final int TOP_PADDING = 30;
    private static final double SPACING = 10.0;
    private static final String ACTION_BTN_STYLE = "action-button";

    private final Label foodLabel;
    private final Button eatButton;
    private final Button nextFoodButton;

    private List<Food> availableFood = new ArrayList<>();
    private Map<Food, Integer> currentFoodMap = Collections.emptyMap();
    private int currentIndex = -1;
    private Consumer<Food> onEatHandler;

    /**
     * Initializes the Kitchen layout.
     */
    public KitchenView() {
        super("Kitchen");
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/styleKitchenView.css")).toExternalForm());
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/defaultRoom.css")).toExternalForm());
        this.getStyleClass().add("kitchen-view");

        final VBox topContainer = new VBox(SPACING);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(TOP_PADDING, 0, 0, 0));

        final Label titleLabel = new Label("KITCHEN");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        this.foodLabel = new Label("Empty");
        this.foodLabel.getStyleClass().add("selected-item-label");

        topContainer.getChildren().addAll(titleLabel, this.foodLabel);
        this.setTop(topContainer);

        this.eatButton = new Button("Eat");
        this.eatButton.getStyleClass().add(ACTION_BTN_STYLE);
        this.eatButton.setOnAction(_ -> triggerEat());

        this.nextFoodButton = new Button("Next");
        this.nextFoodButton.getStyleClass().add(ACTION_BTN_STYLE);
        this.nextFoodButton.setOnAction(_ -> scrollNext());

        this.getActionBar().getChildren().addAll(this.nextFoodButton, this.eatButton);
    }

    /**
     * Updates the food list and quantities.
     *
     * @param food the map of food items and their counts.
     */
    public void refreshFood(final Map<Food, Integer> food) {
        this.currentFoodMap = food;
        this.availableFood = new ArrayList<>(food.keySet());

        if (this.availableFood.isEmpty()) {
            this.foodLabel.setText("Empty");
            this.currentIndex = -1;
            this.eatButton.setDisable(true);
        } else {
            this.currentIndex = 0;
            this.eatButton.setDisable(false);
            updateDisplay();
        }
    }

    /**
     * Sets the eat action handler.
     *
     * @param handler the consumer.
     */
    public void setOnEat(final Consumer<Food> handler) {
        this.onEatHandler = handler;
    }

    private void scrollNext() {
        if (!this.availableFood.isEmpty()) {
            this.currentIndex = (this.currentIndex + 1) % this.availableFood.size();
            updateDisplay();
        }
    }

    private void triggerEat() {
        if (this.currentIndex >= 0 && !this.availableFood.isEmpty() && this.onEatHandler != null) {
            this.onEatHandler.accept(this.availableFood.get(this.currentIndex));
        }
    }

    private void updateDisplay() {
        if (this.currentIndex >= 0 && this.currentIndex < this.availableFood.size()) {
            final Food current = this.availableFood.get(this.currentIndex);
            final int quantity = this.currentFoodMap.getOrDefault(current, 0);

            this.foodLabel.setText(current.getName().toUpperCase(Locale.ROOT) + " (x" + quantity + ")");
        }
    }
}
