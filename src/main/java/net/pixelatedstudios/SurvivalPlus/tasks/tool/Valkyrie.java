package net.pixelatedstudios.SurvivalPlus.tasks.tool;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Valkyrie extends BukkitRunnable {

    private final Survival plugin;

    public Valkyrie(Survival plugin) {
        this.plugin = plugin;
        this.runTaskTimer(plugin, 1, 10);
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (ItemManager.compare(player.getInventory().getItemInMainHand(), Item.VALKYRIES_AXE)) {
                Location particleLoc = player.getLocation();
                particleLoc.setY(particleLoc.getY() + 1);
                assert particleLoc.getWorld() != null;
                particleLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, particleLoc, 10, 0.5, 0.5, 0.5);
            }
        }
    }

}
