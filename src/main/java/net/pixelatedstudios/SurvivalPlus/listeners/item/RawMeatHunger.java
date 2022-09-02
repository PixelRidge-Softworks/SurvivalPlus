package net.pixelatedstudios.SurvivalPlus.listeners.item;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class RawMeatHunger implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) return;
        Random rand = new Random();
        Player player = event.getPlayer();
        // TODO: replace switch with enhanced switch
        switch (event.getItem().getType()) {
            case BEEF:
            case PORKCHOP:
            case MUTTON:
            case RABBIT:
            case SALMON:
            case COD:
            case CHICKEN:
            case ROTTEN_FLESH:
                int hungerChance = rand.nextInt(10) + 1;
                // TODO: Investigate warning
                if (hungerChance >= 1 && hungerChance <= 8) {
                    int dur = 600;
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        if (effect.getType().equals(PotionEffectType.HUNGER)) {
                            dur += effect.getDuration();
                            player.removePotionEffect(effect.getType());
                        }
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, dur, 0, false));
                }
                break;
            default:
        }
    }

}
