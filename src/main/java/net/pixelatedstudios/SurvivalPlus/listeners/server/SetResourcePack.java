package net.pixelatedstudios.SurvivalPlus.listeners.server;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

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