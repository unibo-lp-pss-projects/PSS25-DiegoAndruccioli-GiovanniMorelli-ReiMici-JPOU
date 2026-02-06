package it.unibo.jpou.mvc.controller.room;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.room.BedroomView;
import javafx.application.Platform;

import java.util.logging.Logger;

/**
 * Controller specifically managing the interactions within the Bedroom.
 * Handles sleeping logic and visual updates relevant to the sleeping state.
 */
public final class BedroomController {

    private static final Logger LOGGER = Logger.getLogger(BedroomController.class.getName());

    /**
     * Creates a new BedroomController.
     *
     * @param model    the main game logical model
     * @param view     the specific view for the bedroom
     * @param mainView the main container view, used to update global character
     *                 visuals (eye)
     */
    public BedroomController(final PouLogic model, final BedroomView view, final MainView mainView) {
        setupLogic(model, view, mainView);
    }

    private void setupLogic(final PouLogic model, final BedroomView view, final MainView mainView) {
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

        view.setOnInventoryHandler(_ -> {
            LOGGER.info("Open wardrobe devi mettere l'azione di guardarobe");
        });
    }
}
