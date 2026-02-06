package it.unibo.jpou.mvc.controller.room;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import it.unibo.jpou.mvc.view.room.InfirmaryView;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller dedicated to the Infirmary logic.
 * Handles the potion usage and updates the potion inventory view.
 */
public final class InfirmaryController {

    private static final Logger LOGGER = Logger.getLogger(InfirmaryController.class.getName());

    private final PouLogic model;
    private final Inventory inventory;
    private final InfirmaryView view;
    private final Runnable globalStatsUpdater;

    /**
     * Constructs the Infirmary Controller.
     *
     * @param model              The game logic model.
     * @param inventory          The player's inventory.
     * @param view               The view specific to the infirmary.
     * @param globalStatsUpdater A callback to update the top bar statistics.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Dependency Injection requires storing mutable references")
    public InfirmaryController(final PouLogic model,
                               final Inventory inventory,
                               final InfirmaryView view,
                               final Runnable globalStatsUpdater) {
        this.model = model;
        this.inventory = inventory;
        this.view = view;
        this.globalStatsUpdater = globalStatsUpdater;

        setupLogic();
    }

    private void setupLogic() {
        this.view.setOnUsePotion(potion -> {
            try {
                this.model.usePotion(potion);

                this.inventory.consumeItem(potion);

                refreshView();
                this.globalStatsUpdater.run();

                LOGGER.info("Pou used potion: " + potion.getName());
            } catch (final IllegalArgumentException e) {
                LOGGER.warning("Potion action failed: " + e.getMessage());
            }
        });
    }

    /**
     * Refreshes the available potions in the view based on current inventory.
     * Filters the inventory to find only Potion items.
     */
    public void refreshView() {
        final Map<Potion, Integer> potionMap = this.inventory.getConsumables().entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Potion)
                .collect(Collectors.toMap(e -> (Potion) e.getKey(), Map.Entry::getValue));

        this.view.refreshPotions(potionMap);
    }
}
