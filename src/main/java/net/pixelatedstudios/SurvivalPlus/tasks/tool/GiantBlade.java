package net.pixelatedstudios.SurvivalPlus.tasks.tool;

import com.google.common.collect.ImmutableSet;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;
import net.pixelatedstudios.SurvivalPlus.data.Stat;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GiantBlade extends BukkitRunnable {

    private final Survival plugin;
    private final PlayerManager playerManager;
    private final ImmutableSet<Material> MAIN_SET;
    private final ImmutableSet<Material> OFF_SET;
    private final PotionEffect DAMAGE;
    private final PotionEffect SLOW;
    private final PotionEffect JUMP;

    public GiantBlade(Survival plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.MAIN_SET = ImmutableSet.<Material>builder()
                .add(Material.GOLDEN_HOE).add(Material.GOLDEN_AXE).build();
        this.OFF_SET = ImmutableSet.<Material>builder()
                .add(Material.WOODEN_AXE).add(Material.WOODEN_SWORD).add(Material.WOODEN_PICKAXE)
                .add(Material.WOODEN_SHOVEL).add(Material.WOODEN_HOE).add(Material.STONE_AXE)
                .add(Material.STONE_SWORD).add(Material.STONE_PICKAXE).add(Material.STONE_SHOVEL)
                .add(Material.STONE_HOE).add(Material.IRON_AXE).add(Material.IRON_SWORD)
                .add(Material.IRON_PICKAXE).add(Material.IRON_SHOVEL).add(Material.IRON_HOE)
                .add(Material.GOLDEN_AXE).add(Material.GOLDEN_SWORD).add(Material.GOLDEN_PICKAXE)
                .add(Material.GOLDEN_SHOVEL).add(Material.GOLDEN_HOE).add(Material.DIAMOND_AXE)
                .add(Material.DIAMOND_SWORD).add(Material.DIAMOND_PICKAXE).add(Material.DIAMOND_SHOVEL)
                .add(Material.DIAMOND_HOE).add(Material.BOW).build();

        this.DAMAGE = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1, false);
        this.SLOW = new PotionEffect(PotionEffectType.SLOW, 20, 6, true);
        this.JUMP = new PotionEffect(PotionEffectType.JUMP, 20, 199, true);

        this.runTaskTimer(plugin, 1, 10);
    }

    @Override
    public void run() { //TODO Fix this garbage spaghetti code
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ItemStack mainItem = player.getInventory().getItemInMainHand();
            ItemStack offItem = player.getInventory().getItemInOffHand();
            Material mainType = mainItem.getType();
            Material offType = offItem.getType();

            if (ItemManager.compare(mainItem, Item.ENDER_GIANT_BLADE)) {
                Location particleLoc = player.getLocation();
                particleLoc.setY(particleLoc.getY() + 1);
                assert particleLoc.getWorld() != null;
                particleLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, particleLoc, 10, 0.5, 0.5, 0.5);
            }

            if (ItemManager.compare(offItem, Item.ENDER_GIANT_BLADE)) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.addPotionEffect(this.DAMAGE);
                Location particleLoc = player.getLocation();
                particleLoc.setY(particleLoc.getY() + 1);
                assert particleLoc.getWorld() != null;
                particleLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, particleLoc, 10, 0.5, 0.5, 0.5);
            }

            PlayerData playerData = playerManager.getPlayerData(player);
            if ((MAIN_SET.contains(mainType) && OFF_SET.contains(offType)) || (MAIN_SET.contains(offType) && OFF_SET.contains(mainType))) {
                player.removePotionEffect(PotionEffectType.SLOW);
                player.addPotionEffect(this.SLOW);
                player.removePotionEffect(PotionEffectType.JUMP);
                player.addPotionEffect(this.JUMP);
                playerData.setStat(Stat.DUAL_WIELD, 1);
            } else {
                playerData.setStat(Stat.DUAL_WIELD, 0);
            }
        }
    }

}
