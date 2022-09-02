package net.pixelatedstudios.SurvivalPlus.tasks.tool;


import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class BlazeSwordSound extends BukkitRunnable {

    private final Survival plugin;

    public BlazeSwordSound(Survival plugin) {
        this.plugin = plugin;
        this.runTaskTimer(plugin, 1, 50);
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (ItemManager.compare(player.getInventory().getItemInMainHand(), Item.BLAZE_SWORD)) {
                Random rand = new Random();
                assert player.getLocation().getWorld() != null;
                player.getLocation().getWorld().playSound(
                        player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            }
        }
    }

}

