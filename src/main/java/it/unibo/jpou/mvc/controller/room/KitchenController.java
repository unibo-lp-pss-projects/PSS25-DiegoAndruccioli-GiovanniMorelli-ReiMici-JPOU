package it.unibo.jpou.mvc.controller.room;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import it.unibo.jpou.mvc.view.room.KitchenView;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller dedicated to the Kitchen logic.
 * Handles the eating action and updates the food inventory view.
 */
public final class KitchenController {

    private static final Logger LOGGER = Logger.getLogger(KitchenController.class.getName());

    private final PouLogic model;
    private final Inventory inventory;
    private final KitchenView view;
    private final Runnable globalStatsUpdater;

    /**
     * Constructs the Kitchen Controller.
     *
     * @param model              The game logic model.
     * @param inventory          The player's inventory.
     * @param view               The view specific to the kitchen.
     * @param globalStatsUpdater A callback to update the top bar statistics in MainView.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Dependency Injection requires storing mutable references")
    public KitchenController(final PouLogic model,
                             final Inventory inventory,
                             final KitchenView view,
                             final Runnable globalStatsUpdater) {
        this.model = model;
        this.inventory = inventory;
        this.view = view;
        this.globalStatsUpdater = globalStatsUpdater;

        setupLogic();
    }

    private void setupLogic() {
        this.view.setOnEat(food -> {
            try {
                this.model.eat(food);

                this.inventory.consumeItem(food);

                refreshView();

                this.globalStatsUpdater.run();

                LOGGER.info("Pou ate: " + food.getName());
            } catch (final IllegalArgumentException e) {
                LOGGER.warning("Eat action failed: " + e.getMessage());
            }
        });
    }

    /**
     * Refreshes the available food in the view based on current inventory.
     * Filters the inventory to find only Food items.
     */
    public void refreshView() {
        final Map<Food, Integer> foodMap = this.inventory.getConsumables().entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Food)
                .collect(Collectors.toMap(e -> (Food) e.getKey(), Map.Entry::getValue));

        this.view.refreshFood(foodMap);
    }
}
