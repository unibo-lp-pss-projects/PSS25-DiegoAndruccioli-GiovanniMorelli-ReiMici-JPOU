package it.unibo.jpou.mvc.view.room;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import java.util.Objects;

/**
 * Specific view for the Game Room.
 * Provides controls to start minigames and enter the shop.
 */
public final class GameRoomView extends AbstractRoomView {

    private final Button fruitCatcherButton;
    private final Button shopButton;

    /**
     * Initializes the Game Room by extending the abstract room structure.
     */
    public GameRoomView() {
        super("Game Room");

        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style/room/styleGameRoomView.css"))
                        .toExternalForm());
        this.getStyleClass().add("game-room-view");

        this.fruitCatcherButton = new Button("Fruit Catcher");
        this.shopButton = new Button("Shop");

        this.getActionBar().getChildren().addAll(
                this.fruitCatcherButton,
                this.shopButton
        );
    }

    /**
     * Sets the handler for the Fruit Catcher minigame button.
     *
     * @param handler the action to perform when clicked.
     */
    public void setOnFruitCatcherAction(final EventHandler<ActionEvent> handler) {
        this.fruitCatcherButton.setOnAction(handler);
    }

    /**
     * Sets the handler for the Shop button.
     *
     * @param handler the action to perform when clicked.
     */
    public void setOnShopAction(final EventHandler<ActionEvent> handler) {
        this.shopButton.setOnAction(handler);
    }
}
