package veth.vetheon.survival.listeners.server;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.managers.PlayerManager;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.util.Utils;

public class SetResourcePack implements Listener {

	// TODO: Investigate warnings
	private Survival plugin;
	private Config config;
	private Lang lang;
	private PlayerManager playerManager;
	private String prefix;

	public SetResourcePack(Survival plugin) {
		this.plugin = plugin;
		this.config = plugin.getSurvivalConfig();
		this.lang = plugin.getLang();
		this.playerManager = plugin.getPlayerManager();
		this.prefix = lang.prefix;
	}



	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		if (config.RESOURCE_PACK_ENABLED)
			playerManager.applyResourcePack(event.getPlayer(), 20);
	}

	/* Not sure why this was added, leaving here for now just in case its actually needed
	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		if (resourcePack)
			applyResourcePack(event.getPlayer());
	}
	 */

	@EventHandler
	private void onPlayerLeave(PlayerQuitEvent event) {
		plugin.getUsingPlayers().remove(event.getPlayer());
	}

	@EventHandler
	private void resourcePackEvent(PlayerResourcePackStatusEvent e) {
		Player player = e.getPlayer();
		if (config.RESOURCE_PACK_ENABLED && config.RESOURCE_PACK_NOTIFY)
			if (e.getStatus() == Status.DECLINED) {
				Utils.sendColoredMsg(player, " ");
				Utils.sendColoredMsg(player, prefix + "&c" + lang.resource_pack_declined);
				Utils.sendColoredMsg(player, "   &6" + lang.resource_pack_apply);
				Utils.sendColoredMsg(player, "   &6" + lang.resource_pack_required);
			} else if (e.getStatus() == Status.ACCEPTED) {
				Utils.sendColoredMsg(player, prefix + "&a" + lang.resource_pack_accepted);
			}
	}

}