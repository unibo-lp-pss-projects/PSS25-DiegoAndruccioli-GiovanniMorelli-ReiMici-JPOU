package it.unibo.jpou.mvc.controller.room;

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

    /**
     * @param model the game model.
     * @param view the bedroom view.
     * @param mainView the main container view.
     * @param inventory the player inventory.
     */
    public BedroomController(final PouLogic model, final BedroomView view,
                             final MainView mainView, final Inventory inventory) {
        setupLogic(model, view, mainView, inventory);
    }

    private void setupLogic(final PouLogic model, final BedroomView view,
                            final MainView mainView, final Inventory inventory) {
        // ... (resto del codice invariato, assicurati che i parametri siano final) ...
        // Per brevità non incollo tutto il body, ma aggiungi 'final' a 'view' e 'inventory' se mancano
        model.stateProperty().addListener((_, _, newState) -> {
            Platform.runLater(() -> {
                view.updateView(newState);
                mainView.setPouSleeping(newState == PouState.SLEEPING);
            });
        });
        view.setOnActionHandler(_ -> {
            if (model.getState() == PouState.SLEEPING) {
                model.wakeUp();
            } else {
                model.sleep();
            }
        });
        refreshWardrobe(view, inventory);
        view.setOnSkinSelected(skin -> {
            model.setSkin(skin);
            LOGGER.info("Pou skin changed to: " + skin.getName());
        });
    }

    private void refreshWardrobe(final BedroomView view, final Inventory inventory) {
        final Map<Skin, Integer> ownedSkins = inventory.getDurables().stream()
                .filter(item -> item instanceof Skin)
                .map(item -> (Skin) item)
                .collect(Collectors.toMap(skin -> skin, _ -> 1));
        view.refreshSkins(ownedSkins);
    }
}
