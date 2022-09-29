package net.pixelatedstudios.SurvivalPlus.listeners.block;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

public class SnowballThrow implements Listener {

    @EventHandler
    private void onThrowingSnowball(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            Snowball snowball = (Snowball) e.getEntity();

            BlockIterator iterator = new BlockIterator(snowball.getWorld(), snowball.getLocation().toVector(),
                    snowball.getVelocity().normalize(), 0.0D, 4);
            Block actual = null;
            while (iterator.hasNext()) {
                actual = iterator.next();

                if (actual.getState().getType() != Material.AIR)
                    break;
            }

            assert actual != null;
            if (actual.getType() == Material.SNOW) {
                Snow snow = ((Snow) actual.getBlockData());
                if (snow.getLayers() == 7)
                    actual.setType(Material.SNOW_BLOCK);
                else {
                    snow.setLayers(snow.getLayers() + 1);
                    actual.setBlockData(snow);
                }
                return;
            }
            if (actual.getType() == Material.TALL_GRASS && actual.getType() == Material.DANDELION && actual.getType() == Material.DEAD_BUSH && actual.getType() == Material.ROSE_BUSH) {
                actual.setType(Material.SNOW);
                return;
            }

            if (actual.getType().isSolid()) {
                Block aboveHit = actual.getRelative(BlockFace.UP);
                if (aboveHit.getType() == Material.AIR) {
                    aboveHit.setType(Material.SNOW);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onBreakSnow(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            ItemStack mainItem = player.getInventory().getItemInMainHand();
            if (Tag.MINEABLE_SHOVEL.isTagged(mainItem.getType())) {
                Block block = e.getBlock();
                ItemMeta meta = mainItem.getItemMeta();
                assert meta != null;
                if (block.getType() == Material.SNOW) {
                    e.setCancelled(true);

                    ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                    mainItem.setItemMeta(meta);
                    if (((Damageable) meta).getDamage() >= mainItem.getType().getMaxDurability() + 1)
                        player.getInventory().setItemInMainHand(null);
                    player.updateInventory();
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL,
                            (((Snow) block.getBlockData()).getLayers())));
                    block.setType(Material.AIR);
                    return;
                }

                if (block.getType() == Material.SNOW_BLOCK) {
                    e.setCancelled(true);
                    ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                    mainItem.setItemMeta(meta);
                    if (((Damageable) meta).getDamage() >= mainItem.getType().getMaxDurability() + 1)
                        player.getInventory().setItemInMainHand(null);
                    player.updateInventory();
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL, 8));
                    block.setType(Material.AIR);
                    return;
                }
            }
        }
    }

}
