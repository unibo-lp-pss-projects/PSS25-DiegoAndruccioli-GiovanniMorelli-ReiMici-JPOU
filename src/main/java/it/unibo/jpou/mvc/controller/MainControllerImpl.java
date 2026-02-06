package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.PouStatistics;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.model.items.consumable.food.Food;
import it.unibo.jpou.mvc.model.items.consumable.potion.Potion;
import it.unibo.jpou.mvc.view.MainView;
import it.unibo.jpou.mvc.view.room.AbstractRoomView;
import it.unibo.jpou.mvc.view.room.BathroomView;
import it.unibo.jpou.mvc.view.room.BedroomView;
import it.unibo.jpou.mvc.view.room.InfirmaryView;
import it.unibo.jpou.mvc.view.room.KitchenView;
import it.unibo.jpou.mvc.view.room.ShopView;
import javafx.application.Platform;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of the Main Controller acting as the Logic Orchestrator.
 */
public final class MainControllerImpl implements MainController {

    private static final Logger LOGGER = Logger.getLogger(MainControllerImpl.class.getName());

    private final PouLogic model;
    private final Inventory inventory;

    private final Supplier<ShopController> shopControllerSupplier;
    private final Supplier<InventoryController> inventoryControllerSupplier;

    private final GameLoop gameLoop;

    private final MainView mainView;
    private final BedroomView bedroomView;
    private final BathroomView bathroomView;
    private final KitchenView kitchenView;
    private final InfirmaryView infirmaryView;
    private final ShopView shopView;

    /**
     * Constructs the logical system and wires dependencies.
     *
     * @param view The main view instance.
     */
    public MainControllerImpl(final MainView view) {
        this.mainView = Objects.requireNonNull(view, "View cannot be null");

        this.model = new PouLogic();
        this.model.setCoins(1000); // monete iniziali x debug

        this.inventory = new InventoryImpl();
        this.gameLoop = new PouGameLoop();

        this.bedroomView = new BedroomView();
        this.bathroomView = new BathroomView();
        this.kitchenView = new KitchenView();
        this.infirmaryView = new InfirmaryView();
        this.shopView = new ShopView();

        final ShopController shopCtrl = new ShopControllerImpl(this.model, this.inventory);
        final InventoryController invCtrl = new InventoryControllerImpl(this.model, this.inventory);

        this.shopControllerSupplier = () -> shopCtrl;
        this.inventoryControllerSupplier = () -> invCtrl;

        setupNavigation();
        setupGameLoop();
        setupConsumableHandlers();

        this.mainView.setRoom(this.bedroomView);
        LOGGER.info("[MainController] Logic System initialized.");
    }

    private void setupNavigation() {
        this.mainView.setOnRoomChange(Room.BEDROOM, _ -> changeRoom(this.bedroomView));
        this.mainView.setOnRoomChange(Room.BATHROOM, _ -> changeRoom(this.bathroomView));
        this.mainView.setOnRoomChange(Room.KITCHEN, _ -> changeRoom(this.kitchenView));
        this.mainView.setOnRoomChange(Room.INFIRMARY, _ -> changeRoom(this.infirmaryView));
        this.mainView.setOnRoomChange(Room.SHOP, _ -> changeRoom(this.shopView));
    }

    private void setupGameLoop() {
        if (this.gameLoop instanceof PouGameLoop) {
            ((PouGameLoop) this.gameLoop).addTickListener(() -> {
                this.model.applyDecay();
                Platform.runLater(this::updateGlobalStatistics);
            });
        }
    }

    private void updateGlobalStatistics() {
        this.mainView.updateStat("hunger",
                (double) this.model.getHunger() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getHunger()));
        this.mainView.updateStat("energy",
                (double) this.model.getEnergy() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getEnergy()));
        this.mainView.updateStat("fun",
                (double) this.model.getFun() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getFun()));
        this.mainView.updateStat("health",
                (double) this.model.getHealth() / PouStatistics.STATISTIC_MAX_VALUE,
                String.valueOf(this.model.getHealth()));
    }

    private void setupConsumableHandlers() {
        this.kitchenView.setOnEat(food -> {
            try {
                this.model.eat(food);
                this.inventory.consumeItem(food);

                updateRoomData(this.kitchenView);
                updateGlobalStatistics();

                LOGGER.info("Pou ate: " + food.getName());
            } catch (final IllegalArgumentException e) {
                LOGGER.warning("Eat action failed: " + e.getMessage());
            }
        });

        this.infirmaryView.setOnUsePotion(potion -> {
            try {
                this.model.usePotion(potion);
                this.inventory.consumeItem(potion);

                updateRoomData(this.infirmaryView);
                updateGlobalStatistics();

                LOGGER.info("Pou used potion: " + potion.getName());
            } catch (final IllegalArgumentException e) {
                LOGGER.warning("Potion action failed: " + e.getMessage());
            }
        });
    }

    /**
     * Handles the transition between rooms.
     *
     * @param newRoomView The view to switch to.
     */
    private void changeRoom(final AbstractRoomView newRoomView) {
        if (!(newRoomView instanceof BedroomView) && this.model.getState() == PouState.SLEEPING) {
            this.model.wakeUp();
        }

        this.mainView.setCharacterVisible(!(newRoomView instanceof ShopView));

        if (newRoomView instanceof ShopView) {
            this.shopControllerSupplier.get().populateShop((ShopView) newRoomView);
        } else {
            updateRoomData(newRoomView);
        }

        this.mainView.setRoom(newRoomView);
    }

    /**
     * Synchronizes room views with the current inventory state.
     *
     * @param currentView The view to update.
     */
    private void updateRoomData(final AbstractRoomView currentView) {
        if (currentView instanceof KitchenView) {
            final Map<Food, Integer> foodMap = this.inventory.getConsumables().entrySet().stream()
                    .filter(entry -> entry.getKey() instanceof Food)
                    .collect(Collectors.toMap(e -> (Food) e.getKey(), Map.Entry::getValue));
            ((KitchenView) currentView).refreshFood(foodMap);
        } else if (currentView instanceof InfirmaryView) {
            final Map<Potion, Integer> potionMap = this.inventory.getConsumables().entrySet().stream()
                    .filter(entry -> entry.getKey() instanceof Potion)
                    .collect(Collectors.toMap(e -> (Potion) e.getKey(), Map.Entry::getValue));
            ((InfirmaryView) currentView).refreshPotions(potionMap);
        }
    }

    @Override
    public void start() {
        this.gameLoop.start();
        LOGGER.info("[MainController] GameLoop started.");
    }

    @Override
    public void stop() {
        this.gameLoop.shutdown();
        LOGGER.info("[MainController] GameLoop stopped.");
    }

    @Override
    public ShopController getShopController() {
        return this.shopControllerSupplier.get();
    }

    @Override
    public InventoryController getInventoryController() {
        return this.inventoryControllerSupplier.get();
    }
}
