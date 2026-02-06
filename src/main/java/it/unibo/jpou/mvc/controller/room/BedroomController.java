package it.unibo.jpou.mvc.controller.room;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.room.BedroomView;
import javafx.application.Platform;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.durable.skin.Skin;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controller specifically managing the interactions within the Bedroom.
 */
public final class BedroomController {

    private static final Logger LOGGER = Logger.getLogger(BedroomController.class.getName());

    private final PouLogic model;
    private final BedroomView view;
    private final MainView mainView;
    private final Inventory inventory;

    /**
     * @param model     the game model.
     * @param view      the bedroom view.
     * @param mainView  the main container view.
     * @param inventory the player inventory.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Standard Dependency Injection pattern")
    public BedroomController(final PouLogic model, final BedroomView view,
                             final MainView mainView, final Inventory inventory) {
        this.model = model;
        this.view = view;
        this.mainView = mainView;
        this.inventory = inventory;

        setupLogic();
        refreshView();
    }

    private void setupLogic() {
        this.model.stateProperty().addListener((_, _, newState) -> {
            Platform.runLater(() -> {
                this.view.updateView(newState);
                this.mainView.setPouSleeping(newState == PouState.SLEEPING);
            });
        });

        this.view.setOnActionHandler(_ -> {
            if (this.model.getState() == PouState.SLEEPING) {
                this.model.wakeUp();
            } else {
                this.model.sleep();
            }
        });

        refreshView();

        this.view.setOnSkinSelected(skin -> {
            this.model.setSkin(skin);
            LOGGER.info("Pou skin changed to: " + skin.getName());
        });
    }

    /**
     * Refreshes the wardrobe content based on the current inventory.
     * Use this method to update the view when inventory changes.
     */
    public void refreshView() {
        final Map<Skin, Integer> ownedSkins = this.inventory.getDurables().stream()
                .filter(item -> item instanceof Skin)
                .map(item -> (Skin) item)
                .collect(Collectors.toMap(skin -> skin, _ -> 1));

        this.view.refreshSkins(ownedSkins);
    }
}
