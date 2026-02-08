package it.unibo.jpou.mvc.controller;

import it.unibo.jpou.mvc.controller.persistence.PersistenceController;
import it.unibo.jpou.mvc.controller.persistence.PersistenceControllerImpl;
import it.unibo.jpou.mvc.model.PouCoins;
import it.unibo.jpou.mvc.model.PouLogic;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.statistics.PouStatistics;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.inventory.Inventory;
import it.unibo.jpou.mvc.model.inventory.InventoryImpl;
import it.unibo.jpou.mvc.persistence.PersistenceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceControllerTest {

    private static final int CUSTOM_STATISTICS = 60;
    private static final int CUSTOM_WALLET = 10_000;
    private static final int SECOND_CUSTOM_WALLET = 10;

    @TempDir
    private Path tempDir;

    private PouLogic model;
    private PersistenceController controller;

    @BeforeEach
    void setUp() {
        this.model = new PouLogic();
        final Inventory inventory = new InventoryImpl();

        final Path testSaveFile = tempDir.resolve("test-save.json");
        final PersistenceManager testManager = new PersistenceManager(testSaveFile);

        /* Use the constructor for the test */
        controller = new PersistenceControllerImpl(model, inventory, testManager);
    }

    @Test
    void testSaveAndLoadStandardGame() {
        model.setHunger(CUSTOM_STATISTICS);
        model.setEnergy(CUSTOM_STATISTICS);
        model.setFun(CUSTOM_STATISTICS);
        model.setHealth(CUSTOM_STATISTICS);
        model.setCoins(CUSTOM_WALLET);

        controller.saveGame(Room.GAME_ROOM);

        model.setHunger(PouStatistics.STATISTIC_INITIAL_VALUE);
        model.setEnergy(PouStatistics.STATISTIC_INITIAL_VALUE);
        model.setFun(PouStatistics.STATISTIC_INITIAL_VALUE);
        model.setHealth(PouStatistics.STATISTIC_INITIAL_VALUE);
        model.setCoins(SECOND_CUSTOM_WALLET);

        final String room = controller.loadGame();

        assertAll("Controllo se le statistiche sono quelle corrette al ricaricamento del gioco",
                () -> assertEquals(CUSTOM_STATISTICS, model.getHunger()),
                () -> assertEquals(CUSTOM_STATISTICS, model.getEnergy()),
                () -> assertEquals(CUSTOM_STATISTICS, model.getFun()),
                () -> assertEquals(CUSTOM_STATISTICS, model.getHealth()),
                () -> assertEquals(CUSTOM_WALLET, model.getCoins()),
                () -> assertEquals(Room.GAME_ROOM.name(), room)
        );
    }

    @Test
    void testDeathReset() {
        model.setHealth(PouStatistics.STATISTIC_MIN_VALUE);
        assertEquals(PouState.DEAD, model.getState(),
                "Pou è morto");

        controller.saveGame(Room.GAME_ROOM);

        final PouLogic newModel = new PouLogic();
        final Inventory newInventory = new InventoryImpl();
        final PersistenceManager sameManager = new PersistenceManager(tempDir.resolve("test-save.json"));
        final PersistenceController newController = new PersistenceControllerImpl(newModel, newInventory, sameManager);

        newController.loadGame();

        assertAll("Inizia una nuova partita, dati di default",
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, newModel.getHunger()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, newModel.getEnergy()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, newModel.getFun()),
                () -> assertEquals(PouStatistics.STATISTIC_INITIAL_VALUE, newModel.getHealth()),
                () -> assertEquals(PouCoins.MIN_COINS, newModel.getCoins()),
                () -> assertEquals(PouState.AWAKE, newModel.getState())
                );
    }
}
