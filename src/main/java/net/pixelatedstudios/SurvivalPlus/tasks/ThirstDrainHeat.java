package net.pixelatedstudios.SurvivalPlus.tasks;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;
import net.pixelatedstudios.SurvivalPlus.events.ThirstLevelChangeEvent;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

class ThirstDrainHeat extends BukkitRunnable {

    private final PlayerManager playerManager;

    ThirstDrainHeat(Survival plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.runTaskTimer(plugin, 0, 20L * plugin.getSurvivalConfig().MECHANICS_THIRST_DRAIN_HEAT);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) continue;
            if (player.getWorld().getEnvironment() != Environment.NORMAL) continue;
            if (player.getLocation().getBlock().getTemperature() < 1.0) continue;
            if (!Utils.isAtHighest(player)) continue;

            PlayerData playerData = playerManager.getPlayerData(player);
            int change = 1;
            // Call thirst level change event
            ThirstLevelChangeEvent event = new ThirstLevelChangeEvent(player, change, playerData.getThirst() - change);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                playerData.increaseThirst(-change);
            }
        }
    }

}

