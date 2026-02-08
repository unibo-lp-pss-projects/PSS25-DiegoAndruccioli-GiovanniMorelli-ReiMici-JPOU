package it.unibo.jpou.mvc.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unibo.jpou.mvc.model.PouCoins;
import it.unibo.jpou.mvc.model.PouState;
import it.unibo.jpou.mvc.model.statistics.PouStatistics;
import it.unibo.jpou.mvc.model.Room;
import it.unibo.jpou.mvc.model.items.durable.skin.DefaultSkin;
import it.unibo.jpou.mvc.model.save.PouSaveData;
import it.unibo.jpou.mvc.model.save.SavedInventory;
import it.unibo.jpou.mvc.model.save.SavedStatistics;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the saving and loading of game data.
 * Uses the Gson library to write and read data in Json format.
 */
public class PersistenceManager {

    private static final Logger LOGGER = Logger.getLogger(PersistenceManager.class.getName());

    private static final String SAVE_DIR = ".j-pou";
    private static final String SAVE_FILE = "j-pou-save.json";

    private final Path filePath;
    private final Gson gson;

    /**
     * Creates the manager for a specific file.
     *
     * @param filePath the path to the file where data will be saved
     */
    public PersistenceManager(final Path filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Takes the user home directory and returns the path to save file.
     *
     * @return the path to the save file
     */
    public static Path getDefaultSavePath() {
       return Paths.get(System.getProperty("user.home"), SAVE_DIR, SAVE_FILE);
    }

    /**
     * Saves the game data to the file, creating directories if needed.
     *
     * @param data the data object to save
     * @throws IOException  if there is an error writing the file
     */
    public void save(final PouSaveData data) throws IOException {
        final Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            gson.toJson(data, writer);
            LOGGER.info(() -> "Il salvataggio dei dati è avvenuto in " + filePath);
        }
    }

    /**
     * Loads the game data from file, returns default data if file is missing.
     *
     * @return the loaded data of default data if missing
     */
    public PouSaveData load() {
        if (!Files.exists(filePath)) {
            LOGGER.info("File di salvataggio non esistente, creo file con dati di default");
            return createDefaultData();
        }
        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            final PouSaveData data = gson.fromJson(reader, PouSaveData.class);

            if (data != null && isValid(data)) {
                LOGGER.info("Dati caricati correttamente");
                return data;
            } else {
                LOGGER.warning("Dati non validi, ripristino dati default " + filePath);
                return createDefaultData();
            }
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Errore nel caricamento, ripristino dati default", e);
            return createDefaultData();
        }
    }

    /**
     * Deletes the save file if it exists.
     */
    public void deleteSaveFile() {
        try {
            Files.deleteIfExists(filePath);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Impossibile eliminare il file di salvataggio", e);
        }
    }

    private boolean isValid(final PouSaveData data) {
        final SavedStatistics statistics = data.statistics();
        return statistics.hunger() >= PouStatistics.STATISTIC_MIN_VALUE
                && statistics.hunger() <= PouStatistics.STATISTIC_MAX_VALUE
                && statistics.coins() >= PouCoins.MIN_COINS
                && statistics.age() >= 0;
    }

    private PouSaveData createDefaultData() {
        final SavedStatistics defaultStatistics = new SavedStatistics(
                PouStatistics.STATISTIC_INITIAL_VALUE,
                PouStatistics.STATISTIC_INITIAL_VALUE,
                PouStatistics.STATISTIC_INITIAL_VALUE,
                PouStatistics.STATISTIC_INITIAL_VALUE,
                PouCoins.MIN_COINS,
                PouState.AWAKE.name(),
                0
        );

        final SavedInventory defaultInventory = new SavedInventory(
                Collections.emptyList(),
                List.of(DefaultSkin.DEFAULT_NAME), DefaultSkin.DEFAULT_NAME
        );

        return new PouSaveData(defaultStatistics, defaultInventory, Room.BEDROOM.name());
    }
}
