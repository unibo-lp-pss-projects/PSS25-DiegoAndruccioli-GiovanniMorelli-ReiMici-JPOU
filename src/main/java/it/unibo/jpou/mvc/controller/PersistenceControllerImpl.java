package it.unibo.jpou.mvc.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.items.Item;
import it.unibo.jpou.mvc.model.items.consumable.food.Apple;
import it.unibo.jpou.mvc.model.items.consumable.food.Sushi;
import it.unibo.jpou.mvc.model.items.consumable.potion.EnergyPotion;
import it.unibo.jpou.mvc.model.items.consumable.potion.HealthPotion;
import it.unibo.jpou.mvc.model.items.durable.skin.DefaultSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.GreenSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.RedSkin;
import it.unibo.jpou.mvc.model.items.durable.skin.Skin;
import it.unibo.jpou.mvc.model.save.PouSaveData;
import it.unibo.jpou.mvc.model.save.SavedInventory;
import it.unibo.jpou.mvc.model.save.SavedItem;
import it.unibo.jpou.mvc.model.save.SavedStatistics;
import it.unibo.jpou.mvc.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of the PersistenceController,connect the inventory to the saved file.
 */
public class PersistenceControllerImpl implements PersistenceController {

    private static final Logger LOGGER = Logger.getLogger(PersistenceControllerImpl.class.getName());

    private final PersistenceManager persistenceManager;
    private final PouLogic model;
    private final Inventory inventory;

    /**
     * Default constructor for the application, uses the default save path.
     *
     * @param model the game logic
     * @param inventory the game inventory
     */
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "MVC Pattern: Controller must store mutable references to Model/Inventory components."
    )
    public PersistenceControllerImpl(final PouLogic model, final Inventory inventory) {
        this.model = model;
        this.inventory = inventory;
        this.persistenceManager = new PersistenceManager(PersistenceManager.getDefaultSavePath());
    }

    /**
     * Constructor for the text.
     *
     * @param model the game logic
     * @param inventory the game inventory
     * @param persistenceManager the specific persistence manager to use
     */
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "MVC Pattern: Controller must store mutable references to Model/Inventory components."
    )
    public PersistenceControllerImpl(final PouLogic model,
                                     final Inventory inventory,
                                     final PersistenceManager persistenceManager) {
        this.model = model;
        this.inventory = inventory;
        this.persistenceManager = persistenceManager;
    }

    /**
     * @return the name of the room when the game should resume
     */
    @Override
    public String loadGame() {
        PouSaveData data = this.persistenceManager.load();

        if (PouState.DEAD.name().equals(data.statistics().state())) {
            LOGGER.info("Pou è morto nel ultimo chiusura del gioco, reset per nuova partita");
            this.persistenceManager.deleteSaveFile();
            /* create new default data for new start */
            data = this.persistenceManager.load();
        }
        applyDataToModel(data);
        return data.currentRoom();
    }

    /**
     * @param currentRoom the room the user is currently in
     */
    @Override
    public void saveGame(final Room currentRoom) {
        final SavedStatistics statistics = new SavedStatistics(
                this.model.getHunger(),
                this.model.getEnergy(),
                this.model.getFun(),
                this.model.getHealth(),
                this.model.getCoins(),
                this.model.getState().name(),
                0
        );

        /* saving consumable data */
        final List<SavedItem> savedItems = new ArrayList<>();
        this.inventory.getConsumables().forEach((consumable, quantity) ->
                savedItems.add(new SavedItem(consumable.getName(), quantity))
        );

        /* saving durable data */
        final List<String> unlockedSkins = new ArrayList<>();
        this.inventory.getDurables().forEach(durable ->
                unlockedSkins.add(durable.getName())
        );

        final SavedInventory savedInventory = new SavedInventory(
                savedItems,
                unlockedSkins,
                this.model.getSkin().getName()
        );

        final PouSaveData data = new PouSaveData(statistics, savedInventory, currentRoom.name());

        try {
            this.persistenceManager.save(data);
        } catch (final IOException e) {
            LOGGER.severe("Errore nel salvataggio: " + e.getMessage());
        }
    }

    private void applyDataToModel(final PouSaveData data) {
        /* Statistics recovery */
        this.model.setHunger(data.statistics().hunger());
        this.model.setEnergy(data.statistics().energy());
        this.model.setFun(data.statistics().fun());
        this.model.setHealth(data.statistics().health());
        this.model.setCoins(data.statistics().coins());

        /* State recovery */
        try {
            final PouState targetState = PouState.valueOf(data.statistics().state());

            if (targetState == PouState.AWAKE && this.model.getState() == PouState.SLEEPING) {
                this.model.wakeUp();
            } else if (targetState == PouState.SLEEPING && this.model.getState() == PouState.AWAKE) {
                this.model.sleep();
            }

        } catch (final IllegalArgumentException e) {
            LOGGER.warning("Mantengo lo stato di default perchè lo stato nel file salvato è sconosciuto");
        }

        /* Inventory recovery */
        for (final SavedItem savedItem : data.inventory().items()) {
            final Item item = createItemFromName(savedItem.name());
            for (int i = 0; i < savedItem.quantity(); i++) {
                this.inventory.addItem(item);
            }
        }

        /* Unlocked skin recovery */
        for (final String skinName :data.inventory().unlockedSkins()) {
            final Item skin = createItemFromName(skinName);
            this.inventory.addItem(skin);
        }

        /* Equipped skin recovery */
        final Item equippedSkin = createItemFromName(data.inventory().equippedSkin());
        if (equippedSkin instanceof Skin) {
            this.model.setSkin((Skin) equippedSkin);
        }
    }

    private Item createItemFromName(final String name) {
        return switch (name) {
            case Apple.APPLE_NAME -> new Apple();
            case Sushi.SUSHI_NAME -> new Sushi();
            case EnergyPotion.POTION_NAME -> new EnergyPotion();
            case HealthPotion.POTION_NAME -> new HealthPotion();
            case DefaultSkin.DEFAULT_NAME -> new DefaultSkin();
            case GreenSkin.SKIN_NAME -> new GreenSkin();
            case RedSkin.SKIN_NAME -> new RedSkin();
            default -> null;
        };
    }
}
