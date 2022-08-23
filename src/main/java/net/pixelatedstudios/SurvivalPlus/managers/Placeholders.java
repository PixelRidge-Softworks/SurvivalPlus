package net.pixelatedstudios.SurvivalPlus.managers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.Nutrient;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Placeholders extends PlaceholderExpansion {

    private final Survival plugin;
    private final PlayerManager playerManager;

    public Placeholders(Survival plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    // TODO: Investigate warnings
    @Override
    public String getIdentifier() {
        return "survivalplus";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        PlayerData playerData = playerManager.getPlayerData(p);

        if (playerData == null) {
            return null;
        }

        // Shows player's health, kinda useless but here it is
        if (identifier.equalsIgnoreCase("player_health")) {
            return String.format("%.2f", p.getHealth());
        }
        // Shows a player's total hunger (including saturation)
        if (identifier.equalsIgnoreCase("player_hunger_total")) {
            return String.valueOf(p.getFoodLevel() + p.getSaturation());
        }
        // Shows player's hunger
        if (identifier.equalsIgnoreCase("player_hunger")) {
            return String.valueOf(p.getFoodLevel());
        }
        // Shows player's saturation
        if (identifier.equalsIgnoreCase("player_saturation")) {
            return String.valueOf(p.getSaturation());
        }
        // Shows player's hunger bar (hunger part)
        if (identifier.equalsIgnoreCase("player_hunger_bar_1")) {
            return playerManager.ShowHunger(p).get(1);
        }
        // Shows player's hunger bar (saturation part)
        if (identifier.equalsIgnoreCase("player_hunger_bar_2")) {
            return playerManager.ShowHunger(p).get(2);
        }
        // Shows player's thirst
        if (identifier.equalsIgnoreCase("player_thirst")) {
            return String.valueOf(playerData.getThirst());
        }
        // Shows player's thirst bar (top part - first half out of 40)
        if (identifier.equalsIgnoreCase("player_thirst_bar_1")) {
            return playerManager.ShowThirst(p).get(1);
        }
        // Shows player's thirst bar (bottom part - second half out of 40)
        if (identifier.equalsIgnoreCase("player_thirst_bar_2")) {
            return playerManager.ShowThirst(p).get(2);
        }
        // Shows player's fatigue // Deprecated
        if (identifier.equalsIgnoreCase("player_fatigue")) {
            return "0"; // removed
        }
        // Shows player's energy level (as a number)
        if (identifier.equalsIgnoreCase("player_energy")) {
            return String.format("%.2f", playerData.getEnergy());
        }
        // Shows player's energy level (as a colored bar)
        if (identifier.equalsIgnoreCase("player_energy_bar")) {
            return playerManager.showEnergy(p).get(1);
        }
        // Shows player's nutrients bars (<amount> <nutrient>)
        if (identifier.equalsIgnoreCase("player_nutrients_carbs_bar")) {
            return playerManager.ShowNutrients(p).get(0);
        }
        if (identifier.equalsIgnoreCase("player_nutrients_proteins_bar")) {
            return playerManager.ShowNutrients(p).get(1);
        }
        if (identifier.equalsIgnoreCase("player_nutrients_salts_bar")) {
            return playerManager.ShowNutrients(p).get(2);
        }
        // Shows player's nutrients (just the <amount>)
        if (identifier.equalsIgnoreCase("player_nutrients_carbs")) {
            return String.valueOf(playerData.getNutrient(Nutrient.CARBS));
        }
        if (identifier.equalsIgnoreCase("player_nutrients_proteins")) {
            return String.valueOf(playerData.getNutrient(Nutrient.PROTEIN));
        }
        if (identifier.equalsIgnoreCase("player_nutrients_salts")) {
            return String.valueOf(playerData.getNutrient(Nutrient.SALTS));
        }
        return null;
    }

}
