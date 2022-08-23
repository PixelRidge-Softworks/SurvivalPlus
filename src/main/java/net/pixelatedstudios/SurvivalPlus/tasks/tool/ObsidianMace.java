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

public class ObsidianMace extends BukkitRunnable {

    private final Survival plugin;

    public ObsidianMace(Survival plugin) {
        this.plugin = plugin;
        this.runTaskTimer(plugin, 1, 10);
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (ItemManager.compare(player.getInventory().getItemInMainHand(), Item.OBSIDIAN_MACE)) {
                player.removePotionEffect(PotionEffectType.SLOW);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1, false));
                Location particleLoc = player.getLocation();
                particleLoc.setY(particleLoc.getY() + 1);
                assert particleLoc.getWorld() != null;
                particleLoc.getWorld().spawnParticle(Particle.CRIT, particleLoc, 10, 0.5, 0.5, 0.5);
                particleLoc.getWorld().spawnParticle(Particle.PORTAL, particleLoc, 20, 0.5, 0.5, 0.5);
            }
        }
    }

}
