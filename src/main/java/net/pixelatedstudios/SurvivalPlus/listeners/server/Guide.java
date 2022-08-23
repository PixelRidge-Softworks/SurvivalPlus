package net.pixelatedstudios.SurvivalPlus.listeners.server;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.pixelatedstudios.SurvivalPlus.Survival;

public class Guide implements Listener {

    // TODO: Investigate Warnings
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
            // TODO: Replace deprecation
            TextComponent msg = new TextComponent(Utils.getColoredString(lang.survival_guide_msg));
            // TODO: Replace deprecation
            TextComponent link = new TextComponent(Utils.getColoredString(lang.survival_guide_click_msg));
            // TODO: Replace deprecation
            link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, lang.survival_guide_link));
            // TODO: Replace deprecation
            link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    // TODO: Replace deprecations
                    new ComponentBuilder(Utils.getColoredString(lang.survival_guide_hover_msg)).create()));
            // TODO: Replace deprecation
            player.spigot().sendMessage(msg, link);
            // TODO: Investigate warning
        }, 20 * delay);
    }

}
