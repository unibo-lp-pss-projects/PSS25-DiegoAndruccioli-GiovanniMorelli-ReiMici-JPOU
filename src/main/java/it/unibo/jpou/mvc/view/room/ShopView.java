package it.unibo.jpou.mvc.view.room;

import it.unibo.jpou.mvc.model.items.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.Objects;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Ultra-simple Grid View with rounded cards.
 */
public final class ShopView extends AbstractRoomView {

    private static final int MAIN_PADDING = 20;
    private static final int HGAP = 15;
    private static final int VGAP = 15;

    private final FlowPane cardsContainer;
    private final Label feedbackLabel;

    /**
     * Constructor.
     */
    public ShopView() {
        super("Shop");

        this.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/style/room/styleShopView.css")).toExternalForm());
        this.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/style/room/defaultRoom.css")).toExternalForm());
        this.getStyleClass().add("shop-view-minimal");

        this.feedbackLabel = new Label("");
        this.feedbackLabel.getStyleClass().add("simple-feedback");

        this.cardsContainer = new FlowPane();
        this.cardsContainer.setPadding(new Insets(MAIN_PADDING));
        this.cardsContainer.setHgap(HGAP);
        this.cardsContainer.setVgap(VGAP);
        this.cardsContainer.setAlignment(Pos.CENTER);

        final VBox mainLayout = new VBox(MAIN_PADDING);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(feedbackLabel, cardsContainer);

        this.setCenter(mainLayout);
        this.getActionBar().getChildren().clear();
    }

    /**
     * Populates shop.
     *
     * @param catalog items.
     * @param onBuy buy action.
     */
    public void populateShop(final Map<Item, Integer> catalog, final Consumer<Item> onBuy) {
        this.cardsContainer.getChildren().clear();
        catalog.forEach((item, price) -> {
            this.cardsContainer.getChildren().add(createRoundedCard(item, price, onBuy));
        });
    }

    private VBox createRoundedCard(final Item item, final int price, final Consumer<Item> onBuy) {
        final VBox card = new VBox(5);
        card.getStyleClass().add("rounded-card");
        card.setAlignment(Pos.CENTER);

        final Label nameLabel = new Label(item.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        nameLabel.getStyleClass().add("card-name");

        final Label priceLabel = new Label(price + "$");
        priceLabel.getStyleClass().add("card-price");

        card.getChildren().addAll(nameLabel, priceLabel);
        card.setOnMouseClicked(e -> onBuy.accept(item));

        return card;
    }

    /**
     * @param message feedback message.
     */
    public void setFeedbackText(final String message) {
        this.feedbackLabel.setText(message.toUpperCase(Locale.ROOT));
    }
}
