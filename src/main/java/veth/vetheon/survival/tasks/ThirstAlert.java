package veth.vetheon.survival.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.data.PlayerData;
import veth.vetheon.survival.managers.PlayerManager;
import veth.vetheon.survival.config.Lang;

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
