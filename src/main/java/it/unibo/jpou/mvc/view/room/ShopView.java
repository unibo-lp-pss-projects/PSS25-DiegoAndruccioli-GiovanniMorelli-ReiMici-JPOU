package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.Item;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the Shop.
 */
public final class ShopView extends AbstractRoomView {

    private final Label feedbackLabel;
    private final Button browseButton;
    private final Button buyButton;

    private List<Item> availableItems;
    private Map<Item, Integer> priceMap;
    private int currentIndex = -1;
    private Item currentSelectedItem;
    private Consumer<Item> onBuyAction;

    /**
     * Initializes the Shop View layout.
     */
    public ShopView() {
        super("Shop");

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleShopView.css"))
                        .toExternalForm());
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/defaultRoom.css"))
                        .toExternalForm());
        this.getStyleClass().add("shop-view");

        final StackPane centerLayout = new StackPane();

        this.feedbackLabel = new Label("Welcome to Shop");
        this.feedbackLabel.getStyleClass().add("feedback-label");

        centerLayout.getChildren().add(feedbackLabel);
        this.getChildren().add(centerLayout);

        this.browseButton = new Button("Browse");
        this.browseButton.getStyleClass().add("action-button");

        this.buyButton = new Button("Buy");
        this.buyButton.getStyleClass().add("action-button");

        this.browseButton.setOnAction(_ -> scrollNextItem());
        this.buyButton.setOnAction(_ -> triggerBuy());

        this.getActionBar().getChildren().addAll(this.browseButton, this.buyButton);

        this.availableItems = new ArrayList<>();
        this.priceMap = new HashMap<>();
    }

    /**
     * Receives the catalog data from the Controller.
     *
     * @param catalog The map of Items and Prices.
     * @param onBuy The action to execute when buying.
     */
    public void populateShop(final Map<Item, Integer> catalog, final Consumer<Item> onBuy) {
        this.priceMap = new HashMap<>(catalog);
        this.availableItems = new ArrayList<>(catalog.keySet());
        this.onBuyAction = onBuy;

        this.currentIndex = -1;
        this.currentSelectedItem = null;
        this.feedbackLabel.setText("Click 'Browse' to see items");
    }

    private void scrollNextItem() {
        if (this.availableItems.isEmpty()) {
            this.feedbackLabel.setText("Shop Empty");
            return;
        }

        this.currentIndex = (this.currentIndex + 1) % this.availableItems.size();

        this.currentSelectedItem = this.availableItems.get(this.currentIndex);
        final int price = this.priceMap.get(this.currentSelectedItem);

        final String name = this.currentSelectedItem.getClass().getSimpleName().toUpperCase(Locale.ROOT);
        this.feedbackLabel.setText(name + " (" + price + "$)");
    }

    private void triggerBuy() {
        if (this.currentSelectedItem == null) {
            this.feedbackLabel.setText("Select an item first!");
            return;
        }

        if (this.onBuyAction != null) {
            this.onBuyAction.accept(this.currentSelectedItem);
        }
    }

    /**
     * Updates the central label.
     *
     * @param message text to show.
     */
    public void setFeedbackText(final String message) {
        this.feedbackLabel.setText(message);
    }
}
