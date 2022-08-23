package net.pixelatedstudios.SurvivalPlus.listeners.block;

import org.bukkit.GameMode;
import org.bukkit.Material;
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
			switch (actual.getType()) {
				case SNOW:
					Snow snow = ((Snow) actual.getBlockData());
					if (snow.getLayers() == 7)
						actual.setType(Material.SNOW_BLOCK);
					else {
						snow.setLayers(snow.getLayers() + 1);
						actual.setBlockData(snow);
					}
					break;
				case TALL_GRASS:
				case DANDELION:
				case ROSE_BUSH:
				case DEAD_BUSH:
					actual.setType(Material.SNOW);
					break;
				default:
					if (actual.getType().isSolid()) {
						Block aboveHit = actual.getRelative(BlockFace.UP);
						if (aboveHit.getType() == Material.AIR) {
							aboveHit.setType(Material.SNOW);
						}
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
			// TODO: Switch statements to be switched to enhanced "switch" statements
			switch (mainItem.getType()) {
				case WOODEN_SHOVEL:
				case GOLDEN_SHOVEL:
				case STONE_SHOVEL:
				case DIAMOND_SHOVEL:
				case IRON_SHOVEL:
					Block block = e.getBlock();
					ItemMeta meta = mainItem.getItemMeta();
					assert meta != null;
					switch (block.getType()) {
						case SNOW:
							e.setCancelled(true);


							((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
							mainItem.setItemMeta(meta);
							if (((Damageable) meta).getDamage() >= mainItem.getType().getMaxDurability() + 1)
								player.getInventory().setItemInMainHand(null);
							player.updateInventory();
							block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL,
									(((Snow) block.getBlockData()).getLayers())));
							block.setType(Material.AIR);
							break;
						case SNOW_BLOCK:
							e.setCancelled(true);
							((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
							mainItem.setItemMeta(meta);
							if (((Damageable) meta).getDamage() >= mainItem.getType().getMaxDurability() + 1)
								player.getInventory().setItemInMainHand(null);
							player.updateInventory();
							block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL, 8));
							block.setType(Material.AIR);
							break;
						default:
					}
					break;
				default:
			}
		}
	}

}
