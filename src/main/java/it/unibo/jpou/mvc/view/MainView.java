package it.unibo.jpou.mvc.view;

import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.view.component.BottomNavBarComponent;
import it.unibo.jpou.mvc.view.component.CenterContainerComponent;
import it.unibo.jpou.mvc.view.component.TopBarComponent;
import it.unibo.jpou.mvc.view.overlay.GameOverOverlayView;
import it.unibo.jpou.mvc.view.overlay.PauseOverlayView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 * The root container of the UI.
 * Refactored to avoid exposing internal representation (SpotBugs EI_EXPOSE_REP).
 */
public final class MainView extends StackPane {

    private static final String[] STATS = {"hunger", "energy", "fun", "health"};

    private final TopBarComponent topBar;
    private final BottomNavBarComponent bottomBar;
    private final CenterContainerComponent centerArea;
    private final PauseOverlayView pauseOverlay;
    private final GameOverOverlayView gameOverOverlay;

    /**
     * Assemblies the entire game interface.
     */
    public MainView() {
        this.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/style/styleMainView.css")).toExternalForm());

        this.topBar = new TopBarComponent(STATS);
        this.bottomBar = new BottomNavBarComponent(Room.values());
        this.centerArea = new CenterContainerComponent();
        this.pauseOverlay = new PauseOverlayView();
        this.gameOverOverlay = new GameOverOverlayView();

        final BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(this.topBar);
        mainLayout.setCenter(this.centerArea);
        mainLayout.setBottom(this.bottomBar);

        this.pauseOverlay.setVisible(false);
        this.gameOverOverlay.setVisible(false);

        this.getChildren().addAll(mainLayout, this.pauseOverlay, this.gameOverOverlay);
    }

    /**
     * Updates a statistic in the top bar.
     *
     * @param key the stat name.
     * @param val the value.
     * @param txt the label.
     */
    public void updateStat(final String key, final double val, final String txt) {
        this.topBar.updateStat(key, val, txt);
    }

    /**
     * Sets the room change handler for a specific room.
     *
     * @param room the room.
     * @param handler the action.
     */
    public void setOnRoomChange(final Room room, final EventHandler<ActionEvent> handler) {
        this.bottomBar.setOnRoomChange(room, handler);
    }

    /**
     * Sets the action for the pause resume button.
     *
     * @param handler the action.
     */
    public void setOnResumeAction(final EventHandler<ActionEvent> handler) {
        this.pauseOverlay.setOnResume(handler);
    }

    /**
     * Sets the action for the game over restart button.
     *
     * @param handler the action.
     */
    public void setOnRestartAction(final EventHandler<ActionEvent> handler) {
        this.gameOverOverlay.setOnRestart(handler);
    }

    /**
     * Switches the current room.
     *
     * @param room the room view.
     */
    public void setRoom(final AbstractRoomView room) {
        this.centerArea.setRoom(room);
    }

    /**
     * Toggles pause overlay visibility.
     *
     * @param visible true to show.
     */
    public void setPauseVisible(final boolean visible) {
        this.pauseOverlay.setVisible(visible);
    }

    /**
     * Toggles game over overlay visibility.
     *
     * @param visible true to show.
     */
    public void setGameOverVisible(final boolean visible) {
        this.gameOverOverlay.setVisible(visible);
    }

    /**
     * Toggles the character's visibility in the center area.
     *
     * @param visible true to show, false to hide.
     */
    public void setCharacterVisible(final boolean visible) {
        this.centerArea.setCharacterVisible(visible);
    }
}
