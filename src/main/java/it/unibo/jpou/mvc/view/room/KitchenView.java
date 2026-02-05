package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Advanced View for the Kitchen.
 */
public final class KitchenView extends AbstractRoomView {

    private final HBox foodShelf;
    private final Button shopButton;
    private final Label feedbackLabel;

    private Consumer<Food> onEatAction;

    public KitchenView() {
        super("/style/room/styleKitchenView.css");
        this.getStyleClass().add("kitchen-view");

        final BorderPane layout = new BorderPane();

        final VBox topContainer = new VBox(5);
        topContainer.setAlignment(Pos.CENTER);

        final Label titleLabel = new Label("KITCHEN");
        titleLabel.getStyleClass().add("room-title");

        this.feedbackLabel = new Label("Select food to eat");
        this.feedbackLabel.getStyleClass().add("feedback-label");

        topContainer.getChildren().addAll(titleLabel, feedbackLabel);
        layout.setTop(topContainer);

        final VBox bottomContainer = new VBox(10);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.getStyleClass().add("bottom-panel");

        this.foodShelf = new HBox(15);
        this.foodShelf.setAlignment(Pos.CENTER_LEFT);
        this.foodShelf.getStyleClass().add("food-shelf");

        final ScrollPane scrollPane = new ScrollPane(foodShelf);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("food-scroll-pane");
        scrollPane.setMinHeight(120);

        this.shopButton = new Button("Go to Market");
        this.shopButton.getStyleClass().add("shop-link-button");

        bottomContainer.getChildren().addAll(scrollPane, shopButton);
        layout.setBottom(bottomContainer);

        this.getChildren().add(layout);
    }

    /**
     * Updates the shelf displaying text cards for available food.
     * @param inventory The current inventory state.
     */
    public void refreshItems(final Map<Item, Integer> inventory) {
        this.foodShelf.getChildren().clear();
        boolean hasFood = false;

        for (final Map.Entry<Item, Integer> entry : inventory.entrySet()) {
            final Item item = entry.getKey();
            final int quantity = entry.getValue();

            if (item instanceof Food && quantity > 0) {
                hasFood = true;
                final StackPane itemCard = createTextCard((Food) item, quantity);
                this.foodShelf.getChildren().add(itemCard);
            }
        }

        if (!hasFood) {
            final Label emptyMsg = new Label("Empty Fridge");
            emptyMsg.getStyleClass().add("empty-msg");
            this.foodShelf.getChildren().add(emptyMsg);
            this.feedbackLabel.setText("You need to buy groceries!");
        } else {
            this.feedbackLabel.setText("What should Pou eat?");
        }
    }

    /**
     * Creates a rectangular card with the Food Name and Quantity.
     */
    private StackPane createTextCard(final Food food, final int quantity) {
        final StackPane card = new StackPane();
        card.getStyleClass().add("food-card");

        final VBox cardContent = new VBox(5);
        cardContent.setAlignment(Pos.CENTER);

        final String foodName = food.getClass().getSimpleName().toUpperCase();
        final Label nameLabel = new Label(foodName);
        nameLabel.getStyleClass().add("food-name");

        final Label qtyLabel = new Label("x" + quantity);
        qtyLabel.getStyleClass().add("food-qty");

        cardContent.getChildren().addAll(nameLabel, qtyLabel);
        card.getChildren().add(cardContent);

        card.setOnMouseClicked(_ -> {
            if (this.onEatAction != null) {
                this.feedbackLabel.setText("Yum! " + foodName + " is delicious.");
                this.onEatAction.accept(food);
            }
        });

        return card;
    }

    public void setOnEat(final Consumer<Food> listener) {
        this.onEatAction = listener;
    }

    public Button getShopButton() {
        return this.shopButton;
    }
}
