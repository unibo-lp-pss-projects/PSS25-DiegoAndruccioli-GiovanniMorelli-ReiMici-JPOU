package it.unibo.jpou.mvc.view.component;

import it.unibo.jpou.mvc.model.Room;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.EnumMap;
import java.util.Map;

/**
 * Navigation bar using EnumMap for efficiency.
 */
public final class BottomNavBarComponent extends HBox {

    private static final double BUTTON_SPACING = 5.0;
    private final Map<Room, Button> roomButtons;

    /**
     * @param rooms the available rooms.
     */
    public BottomNavBarComponent(final Room[] rooms) {
        super(BUTTON_SPACING);
        this.roomButtons = new EnumMap<>(Room.class);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("bottom-nav-bar");

        for (final Room room : rooms) {
            final Button btn = new Button(room.getLabel());
            btn.getStyleClass().add("room-button");
            setHgrow(btn, Priority.ALWAYS);
            btn.setMaxWidth(Double.MAX_VALUE);

            this.roomButtons.put(room, btn);
            this.getChildren().add(btn);
        }
    }

    /**
     * @param room the room constant.
     * @param handler the action.
     */
    public void setOnRoomChange(final Room room, final EventHandler<ActionEvent> handler) {
        if (this.roomButtons.containsKey(room)) {
            this.roomButtons.get(room).setOnAction(handler);
        }
    }
}
