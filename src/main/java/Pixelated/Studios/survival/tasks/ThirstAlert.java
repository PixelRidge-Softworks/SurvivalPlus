package Pixelated.Studios.survival.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import Pixelated.Studios.survival.Survival;
import Pixelated.Studios.survival.data.PlayerData;
import Pixelated.Studios.survival.managers.PlayerManager;
import Pixelated.Studios.survival.config.Lang;

class ThirstAlert extends BukkitRunnable {

	private final PlayerManager playerManager;
	private final Lang lang;

	ThirstAlert(Survival plugin) {
		this.playerManager = plugin.getPlayerManager();
		this.lang = plugin.getLang();
		final int ALERT_INTERVAL = plugin.getSurvivalConfig().MECHANICS_ALERT_INTERVAL;
		// TODO: Investigate warning
		this.runTaskTimer(plugin, -1, ALERT_INTERVAL * 20);
	}
	@Override
	public void run() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				PlayerData playerData = playerManager.getPlayerData(player);
				int hunger = player.getFoodLevel();
				if (hunger <= 6) {
					player.sendMessage(ChatColor.GOLD + lang.starved_eat);
				}
				if (playerData.getThirst() <= 6) {
					player.sendMessage(ChatColor.AQUA + lang.dehydrated_drink);
				}
			}
		}
	}

}
