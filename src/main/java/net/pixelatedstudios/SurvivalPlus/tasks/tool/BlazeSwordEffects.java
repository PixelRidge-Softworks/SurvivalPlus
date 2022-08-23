package net.pixelatedstudios.SurvivalPlus.tasks.tool;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BlazeSwordEffects extends BukkitRunnable {

    private final Survival plugin;
    private final PotionEffect FLAME;

    public BlazeSwordEffects(Survival plugin) {
        this.plugin = plugin;
        this.FLAME = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20, 0, false);
        this.runTaskTimer(plugin, 1, 10);
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (ItemManager.compare(player.getInventory().getItemInMainHand(), Item.BLAZE_SWORD)) {
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                player.addPotionEffect(this.FLAME);
                Location particleLoc = player.getLocation();
                particleLoc.setY(particleLoc.getY() + 1);
                assert particleLoc.getWorld() != null;
                particleLoc.getWorld().spawnParticle(Particle.FLAME, particleLoc, 10, 0.5, 0.5, 0.5);

                player.setFireTicks(20);
                if (player.getHealth() > 14)
                    player.setHealth(14);
            }
        }
    }

}
