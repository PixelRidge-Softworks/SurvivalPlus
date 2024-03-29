package net.pixelatedstudios.SurvivalPlus.tasks;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.Nutrient;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class NutrientsDrain extends BukkitRunnable {

    private final PlayerManager playerManager;

    NutrientsDrain(Survival plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.runTaskTimer(plugin, -1, 1);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                if (player.getExhaustion() >= 4) {
                    PlayerData playerData = playerManager.getPlayerData(player);

                    playerData.increaseNutrient(Nutrient.CARBS, -8);
                    playerData.increaseNutrient(Nutrient.PROTEIN, -2);
                    playerData.increaseNutrient(Nutrient.SALTS, -3);
                }
            }
        }
    }

}

