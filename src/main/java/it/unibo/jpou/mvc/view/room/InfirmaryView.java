package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
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
 * Advanced View for the Infirmary (Text-Only Edition).
 * Displays available potions on a dynamic medical shelf.
 */
public final class InfirmaryView extends AbstractRoomView {

    private final HBox potionShelf;
    private final Button shopButton;
    private final Label feedbackLabel;

    private Consumer<Potion> onUsePotionAction;

    public InfirmaryView() {
        super("/style/room/styleInfirmaryView.css");
        this.getStyleClass().add("infirmary-view");

        final BorderPane layout = new BorderPane();

        final VBox topContainer = new VBox(5);
        topContainer.setAlignment(Pos.CENTER);

        final Label titleLabel = new Label("INFIRMARY");
        titleLabel.getStyleClass().add("room-title");

        this.feedbackLabel = new Label("Pou feels sick?");
        this.feedbackLabel.getStyleClass().add("feedback-label");

        topContainer.getChildren().addAll(titleLabel, feedbackLabel);
        layout.setTop(topContainer);

        final VBox bottomContainer = new VBox(10);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.getStyleClass().add("bottom-panel");

        this.potionShelf = new HBox(15);
        this.potionShelf.setAlignment(Pos.CENTER_LEFT);
        this.potionShelf.getStyleClass().add("potion-shelf");

        final ScrollPane scrollPane = new ScrollPane(potionShelf);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("potion-scroll-pane");
        scrollPane.setMinHeight(120);

        this.shopButton = new Button("Buy Medicine");
        this.shopButton.getStyleClass().add("shop-link-button");

        bottomContainer.getChildren().addAll(scrollPane, shopButton);
        layout.setBottom(bottomContainer);

        this.getChildren().add(layout);
    }

    /**
     * Updates the shelf displaying only Potions.
     *
     * @param inventory The current inventory state.
     */
    public void refreshItems(final Map<Item, Integer> inventory) {
        this.potionShelf.getChildren().clear();
        boolean hasPotions = false;

        for (final Map.Entry<Item, Integer> entry : inventory.entrySet()) {
            final Item item = entry.getKey();
            final int quantity = entry.getValue();

            if (item instanceof Potion && quantity > 0) {
                hasPotions = true;
                final StackPane potionCard = createTextCard((Potion) item, quantity);
                this.potionShelf.getChildren().add(potionCard);
            }
        }

        if (!hasPotions) {
            final Label emptyMsg = new Label("No Medicine Left");
            emptyMsg.getStyleClass().add("empty-msg");
            this.potionShelf.getChildren().add(emptyMsg);
            this.feedbackLabel.setText("Visit the Pharmacy!");
        } else {
            this.feedbackLabel.setText("Select a potion to heal.");
        }
    }

    /**
     * Creates a medical-style text card.
     */
    private StackPane createTextCard(final Potion potion, final int quantity) {
        final StackPane card = new StackPane();
        card.getStyleClass().add("potion-card");

        final VBox cardContent = new VBox(5);
        cardContent.setAlignment(Pos.CENTER);

        final String computedName = potion.getClass().getSimpleName()
                .replace("Potion", "")
                .toUpperCase();

        final String rawName = computedName.isEmpty() ? "POTION" : computedName;

        final Label nameLabel = new Label(rawName);
        nameLabel.getStyleClass().add("potion-name");

        final Label qtyLabel = new Label("x" + quantity);
        qtyLabel.getStyleClass().add("potion-qty");

        cardContent.getChildren().addAll(nameLabel, qtyLabel);
        card.getChildren().add(cardContent);

        // Interaction
        card.setOnMouseClicked(_ -> {
            if (this.onUsePotionAction != null) {
                // Ora rawName è sicuro da usare qui
                this.feedbackLabel.setText("Using " + rawName + "...");
                this.onUsePotionAction.accept(potion);
            }
        });

        return card;
    }

    public void setOnUsePotion(final Consumer<Potion> listener) {
        this.onUsePotionAction = listener;
    }

    public Button getShopButton() {
        return this.shopButton;
    }
}
