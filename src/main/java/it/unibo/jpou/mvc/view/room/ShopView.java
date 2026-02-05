package it.unibo.jpou.mvc.view.room;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * View responsible for displaying the in-game Shop.
 */
public final class ShopView extends AbstractRoomView {

    private static final double GAP = 15.0;

    // Components
    private final FlowPane itemsContainer;
    private final Button backButton;
    private final Label feedbackLabel;
    private final Label titleLabel;

    /**
     * Initializes the shop interface.
     */
    public ShopView() {
        super("/style/room/styleShopView.css");
        this.getStyleClass().add("shop-view");

        final BorderPane layout = new BorderPane();

        final VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10));

        this.titleLabel = new Label("MARKETPLACE");
        this.titleLabel.getStyleClass().add("shop-title");

        this.feedbackLabel = new Label("Welcome! Spend your coins wisely.");
        this.feedbackLabel.getStyleClass().add("shop-feedback-label");

        headerBox.getChildren().addAll(this.titleLabel, this.feedbackLabel);
        layout.setTop(headerBox);

        this.itemsContainer = new FlowPane();
        this.itemsContainer.setAlignment(Pos.TOP_CENTER);
        this.itemsContainer.setHgap(GAP);
        this.itemsContainer.setVgap(GAP);
        this.itemsContainer.setPadding(new Insets(10));
        this.itemsContainer.getStyleClass().add("items-container");
        this.itemsContainer.prefWidthProperty().bind(layout.widthProperty());

        final ScrollPane scrollPane = new ScrollPane(itemsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("shop-scroll-pane");

        layout.setCenter(scrollPane);

        final VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        bottomBox.getStyleClass().add("shop-bottom-panel");

        this.backButton = new Button("Back to Room");
        this.backButton.getStyleClass().add("shop-back-button");

        bottomBox.getChildren().add(this.backButton);
        layout.setBottom(bottomBox);

        this.getChildren().add(layout);
    }

    /**
     * Adds a new item component to the shop display.
     *
     * @param itemNode the graphical node representing the shop item (Product Card).
     */
    public void addItem(final Node itemNode) {
        this.itemsContainer.getChildren().add(itemNode);
    }

    /**
     * Clears all items from the shop display.
     */
    public void clearItems() {
        this.itemsContainer.getChildren().clear();
    }

    /**
     * Updates the feedback message for the user.
     *
     * @param message the text to show.
     */
    public void setFeedbackText(final String message) {
        this.feedbackLabel.setText(message);
    }

    /**
     * Sets the action for the back button.
     *
     * @param handler the event handler.
     */
    public void setOnBackAction(final EventHandler<ActionEvent> handler) {
        this.backButton.setOnAction(handler);
    }

    public Button getBackButton() {
        return this.backButton;
    }
}