package veth.vetheon.survival.listeners.server;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.util.Utils;

public class Guide implements Listener {
    
    private Survival plugin;
    private Lang lang;
    private Config config;
    
    public Guide(Survival plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLang();
        this.config = plugin.getSurvivalConfig();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPlayedBefore() && config.WELCOME_GUIDE_NEW_PLAYERS) return;
        int delay = config.WELCOME_GUIDE_DELAY;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Player player = e.getPlayer();
            TextComponent msg = new TextComponent(Utils.getColoredString(lang.survival_guide_msg));
            TextComponent link = new TextComponent(Utils.getColoredString(lang.survival_guide_click_msg));
            link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, lang.survival_guide_link));
            link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(Utils.getColoredString(lang.survival_guide_hover_msg)).create()));
            player.spigot().sendMessage(msg, link);
        }, 20 * delay);
    }

}
