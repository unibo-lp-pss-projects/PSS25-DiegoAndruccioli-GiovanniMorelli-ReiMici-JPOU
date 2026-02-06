package it.unibo.jpou.mvc.model.save;

/**
 * Record representing the saved vital statistics and coins of Pou.
 *
 * @param hunger current hunger level
 * @param energy current energy level
 * @param fun    current fun level
 * @param health current health level
 * @param coins  current amount of coins
 * @param state  current state of Pou (AWAKE/SLEEPING)
 */
public record SavedStatistics(int hunger, int energy, int fun, int health, int coins, String state) {

}