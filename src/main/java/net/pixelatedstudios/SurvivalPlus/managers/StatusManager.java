package net.pixelatedstudios.SurvivalPlus.managers;

import org.bukkit.entity.Player;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.Nutrient;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;

// TODO: investigate if this is needed still or if it can be removed
/**
 * Manage a player's different status levels
 * @deprecated Use <b>{@link PlayerData}</b>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Deprecated
public class StatusManager {

    private static PlayerManager playerManager = Survival.getInstance().getPlayerManager();

    /**
     * Enums for Nutrient types
     */
    public enum Nutrients {
        CARBS("Carbs"),
        PROTEIN("Protein"),
        SALTS("Salts");

        private String name;

        Nutrients(String nutrient){
            name = nutrient;
        }

        public String getName() {
            return name;
        }

    }

    /** Set the thirst level of a player
     * @param player The player to set thirst for
     * @param level The level of thirst to set (Max = 40)
     */
    public static void setThirst(Player player, int level) {
        PlayerData pd = playerManager.getPlayerData(player);
        pd.setThirst(level);
    }

    /** Get the thirst level of a player
     * @param player The player to get a thirst level from
     * @return The thirst level of this player
     */
    public static int getThirst(Player player) {
        PlayerData pd = playerManager.getPlayerData(player);
        return pd.getThirst();
    }

    /** Add to the thirst level of a player
     * @param player The player to add thirst to
     * @param level The level of thirst to add
     */
    public static void addThirst(Player player, int level) {
        setThirst(player, getThirst(player) + level);
    }

    /** Remove from the thirst level of a player
     * @param player The player to remove thirst from
     * @param level The level of thirst to remove
     */
    public static void removeThirst(Player player, int level) {
        setThirst(player, getThirst(player) - level);
    }

    /** Set the nutrient levels of a player
     * @param player The player to set nutrients for
     * @param nutrient The nutrient to set
     * @param level The level to set
     */
    public static void setNutrients(Player player, Nutrients nutrient, int level) {
        PlayerData pd = playerManager.getPlayerData(player);
        switch (nutrient) {
            case SALTS:
                pd.setNutrient(Nutrient.SALTS, level);
                break;
            case CARBS:
                pd.setNutrient(Nutrient.CARBS, level);
                break;
            case PROTEIN:
                pd.setNutrient(Nutrient.PROTEIN, level);
        }
    }

    /** Get the nutrient levels of a player
     * @param player The player to get nutrient levels for
     * @param nutrient The nutrient to check
     * @return The level of this nutrient
     */
    public static int getNutrients(Player player, Nutrients nutrient) {
        PlayerData pd = playerManager.getPlayerData(player);
        switch (nutrient) {
            case SALTS:
                return pd.getNutrient(Nutrient.SALTS);
            case CARBS:
                return pd.getNutrient(Nutrient.CARBS);
            case PROTEIN:
                return pd.getNutrient(Nutrient.PROTEIN);
            default:
                return 0;
        }
    }

    /** Add to the nutrient levels of a player
     * @param player The player to add nutrients for
     * @param nutrient The nutrient to add to
     * @param level The level to add
     */
    public static void addNutrients(Player player, Nutrients nutrient, int level) {
        setNutrients(player, nutrient, getNutrients(player, nutrient) + level);
    }

    /** Remove from the nutrient levels of a player
     * @param player The player to remove nutrients for
     * @param nutrient The nutrient to remove from
     * @param level The level to remove
     */
    public static void removeNutrients(Player player, Nutrients nutrient, int level) {
        setNutrients(player, nutrient, getNutrients(player, nutrient) - level);
    }

    /** Set the hunger level of a player
     * <p>
     *     <b>NOTE:</b> This level is a mixture of the player's food/saturation levels
     * </p>
     * @param player The player to set hunger level for
     * @param level The level to set for the player
     */
    public static void setHunger(Player player, int level) {
        level = Math.min(level, 40);
        player.setFoodLevel(Math.min(level, 20));
        player.setSaturation(level >= 21 ? level - 20 : 0);
    }

    /** Get the hunger level of a player
     * <p>
     *     <b>NOTE:</b> This level is a mixture of the player's food/saturation levels
     * </p>
     * @param player The player to get hunger level for
     * @return The level of player's hunger
     */
    public static int getHunger(Player player) {
        return Math.round(player.getFoodLevel() + player.getSaturation());
    }

}
