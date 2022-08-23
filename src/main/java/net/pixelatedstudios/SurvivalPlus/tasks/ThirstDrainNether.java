package net.pixelatedstudios.SurvivalPlus.tasks;

import net.pixelatedstudios.SurvivalPlus.events.ThirstLevelChangeEvent;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;

class ThirstDrainNether extends BukkitRunnable {

    private final PlayerManager playerManager;

    ThirstDrainNether(Survival plugin) {
        this.playerManager = plugin.getPlayerManager();
        // TODO: Investigate warning
        this.runTaskTimer(plugin, 0, 20 * plugin.getSurvivalConfig().MECHANICS_THIRST_DRAIN_NETHER);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) continue;
            if (player.getWorld().getEnvironment() != Environment.NETHER) continue;

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

