package net.pixelatedstudios.SurvivalPlus.listeners.item;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Random;

public class Clownfish implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.TROPICAL_FISH) {
            Random rand = new Random();
            Location originLoc = player.getLocation();
            originLoc.getWorld().spawnParticle(Particle.PORTAL, originLoc, 200, 0.5, 0.5, 0.5);
            player.getLocation().getWorld().playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, rand.nextFloat() * 0.4F + 0.8F);

            // TODO: Investigate warning
            if (player.getCompassTarget() != null) {
                Location teleportLoc = player.getCompassTarget();
                player.teleport(teleportLoc);
                teleportLoc.getWorld().spawnParticle(Particle.PORTAL, teleportLoc, 200, 0.5, 0.5, 0.5);
                player.getLocation().getWorld().playSound(teleportLoc, Sound.BLOCK_PORTAL_TRAVEL, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            } else {
                Location teleportLoc = player.getWorld().getSpawnLocation();
                player.teleport(teleportLoc);
                teleportLoc.getWorld().spawnParticle(Particle.PORTAL, teleportLoc, 200, 0.5, 0.5, 0.5);
                player.getLocation().getWorld().playSound(teleportLoc, Sound.BLOCK_PORTAL_TRAVEL, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            }
        }
    }

}
