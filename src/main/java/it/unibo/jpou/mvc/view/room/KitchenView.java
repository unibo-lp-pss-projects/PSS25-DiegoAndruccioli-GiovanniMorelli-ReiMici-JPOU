package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the Kitchen.
 */
public final class KitchenView extends AbstractRoomView {

    private static final int TOP_PADDING = 50;
    private final Label feedbackLabel;
    private final Button eatButton;
    private final Button nextItemButton;

    private List<Food> availableFood = new ArrayList<>();
    private Map<Food, Integer> foodQuantities = new HashMap<>();
    private int currentIndex = -1;
    private Food currentSelectedFood;

    private Consumer<Food> onEatAction;

    /**
     * Initializes the kitchen layout.
     */
    public KitchenView() {
        super("Kitchen");

        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/styleKitchenView.css")).toExternalForm());
        this.getStylesheets().add(Objects.requireNonNull(getClass()
                .getResource("/style/room/defaultRoom.css")).toExternalForm());
        this.getStyleClass().add("kitchen-view");

        final VBox topContainer = new VBox();
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(TOP_PADDING, 0, 0, 0));

        this.feedbackLabel = new Label("Fridge is empty.");
        this.feedbackLabel.getStyleClass().add("selected-food-label");

        topContainer.getChildren().add(feedbackLabel);
        this.setTop(topContainer);

        this.eatButton = new Button("EAT");
        this.eatButton.getStyleClass().add("action-button");
        this.eatButton.setDisable(true);

        this.nextItemButton = new Button("NEXT ITEM");
        this.nextItemButton.getStyleClass().add("action-button");

        this.nextItemButton.setOnAction(_ -> scrollNextItem());
        this.eatButton.setOnAction(_ -> triggerEat());

        this.getActionBar().getChildren().addAll(this.eatButton, this.nextItemButton);
    }

    /**
     * Refresh food list.
     *
     * @param foods map of foods.
     */
    public void refreshFood(final Map<Food, Integer> foods) {
        this.foodQuantities = new HashMap<>(foods);
        this.availableFood = new ArrayList<>(foods.keySet());

        if (this.availableFood.isEmpty()) {
            this.currentIndex = -1;
            this.currentSelectedFood = null;
            this.feedbackLabel.setText("Fridge is empty.");
            this.eatButton.setDisable(true);
        } else {
            if (this.currentIndex < 0 || this.currentIndex >= this.availableFood.size()) {
                this.currentIndex = 0;
            }
            this.currentSelectedFood = this.availableFood.get(this.currentIndex);
            this.eatButton.setDisable(false);
            updateDisplay();
        }
    }

    private void scrollNextItem() {
        if (!this.availableFood.isEmpty()) {
            this.currentIndex = (this.currentIndex + 1) % this.availableFood.size();
            this.currentSelectedFood = this.availableFood.get(this.currentIndex);
            updateDisplay();
        }
    }

    private void updateDisplay() {
        if (this.currentSelectedFood != null) {
            final String name = this.currentSelectedFood.getName().toUpperCase(Locale.ROOT);
            final int qty = this.foodQuantities.get(this.currentSelectedFood);
            this.feedbackLabel.setText(name + " (x" + qty + ")");
        }
    }

    private void triggerEat() {
        if (this.currentSelectedFood != null && this.onEatAction != null) {
            this.onEatAction.accept(this.currentSelectedFood);
        }
    }

    /**
     * @param listener action on eat.
     */
    public void setOnEat(final Consumer<Food> listener) {
        this.onEatAction = listener;
    }
}
