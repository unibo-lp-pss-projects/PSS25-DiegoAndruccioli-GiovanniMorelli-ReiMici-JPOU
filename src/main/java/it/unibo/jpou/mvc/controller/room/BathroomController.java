package it.unibo.jpou.mvc.controller.room;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.view.room.BathroomView;

/**
 * Controller specifically managing the interactions within the Bathroom.
 * Handles the washing logic.
 */
public final class BathroomController {

    /**
     * Creates a new BathroomController.
     *
     * @param model                   the main game logical model
     * @param view                    the specific view for the bathroom
     * @param globalStatisticsUpdater a callback to trigger the update of the global
     *                                UI statistics bar
     */
    public BathroomController(final PouLogic model, final BathroomView view, final Runnable globalStatisticsUpdater) {
        setupLogic(model, view, globalStatisticsUpdater);
    }

    private void setupLogic(final PouLogic model, final BathroomView view, final Runnable globalStatisticsUpdater) {
        view.setOnWashHandler(_ -> {
            model.wash();
            globalStatisticsUpdater.run();
        });
    }

}
