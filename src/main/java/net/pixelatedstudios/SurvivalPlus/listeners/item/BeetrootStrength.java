package net.pixelatedstudios.SurvivalPlus.listeners.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BeetrootStrength implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.BEETROOT) {
            int amp = 0;
            int dur = 200;
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                    dur += effect.getDuration();
                    if (dur > 600) dur = 600;
                    player.removePotionEffect(effect.getType());
                }
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, dur, amp));
        }
    }

}
