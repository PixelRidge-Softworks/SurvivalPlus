package net.pixelatedstudios.SurvivalPlus.listeners.server;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Guide implements Listener {

    private final Survival plugin;
    private final Lang lang;
    private final Config config;

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
            TextComponent msg = Utils.getColoredString(lang.survival_guide_msg);

            TextComponent link = Utils.getColoredString(lang.survival_guide_click_msg);
            link.clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, lang.survival_guide_link));
            link.hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    Utils.getColoredString(lang.survival_guide_hover_msg));
            player.spigot().sendMessage(msg, link);
        }, 20 * delay);
    }

}
