package veth.vetheon.survival.listeners.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.util.Utils;

public class BlazeSword implements Listener {

	@EventHandler
	private void onItemClick(PlayerInteractEvent event) {
		if (event.hasItem()) {
			Player player = event.getPlayer();
			ItemStack mainItem = player.getInventory().getItemInMainHand();
			if (ItemManager.compare(mainItem, Item.BLAZE_SWORD)) {
				if (player.isSneaking()) {
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
						if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							// TODO: Investigate warning
							Material mat = event.getClickedBlock().getType();
							if (Tag.BEDS.isTagged(mat) || Tag.DOORS.isTagged(mat) || Tag.TRAPDOORS.isTagged(mat) || Utils.isWoodGate(mat)) {
								return;
							}
							switch (event.getClickedBlock().getType()) {
								case ENCHANTING_TABLE:
								case ANVIL:
								case BREWING_STAND:
								case TRAPPED_CHEST:
								case CHEST:
								case BARREL:
								case NOTE_BLOCK:
								case FURNACE:
								case BLAST_FURNACE:
								case SMOKER:
								case HOPPER:
								case CRAFTING_TABLE:
								case SMITHING_TABLE:
								case FLETCHING_TABLE:
								case GRINDSTONE:
								case CARTOGRAPHY_TABLE:
								case COMPOSTER:
								case LECTERN:
								case LOOM:
								case DROPPER:
								case DISPENSER:
									return;
								default:
							}
							Location loc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
							ignite(player, loc);
						}

						if (event.getAction() == Action.RIGHT_CLICK_AIR) {
							Location loc = player.getLocation();
							loc.add(-0.5, -0.5, -0.5);
							ignite(player, loc);
						}

						ItemMeta meta = mainItem.getItemMeta();
						assert meta != null;
						((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
						mainItem.setItemMeta(meta);
						if (((Damageable) meta).getDamage() >= mainItem.getType().getMaxDurability()) {
							Random rand = new Random();
							player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
							player.getInventory().setItemInMainHand(null);
						}
						player.updateInventory();
					}
				}
			}
		}
	}

	private void ignite(Player igniter, Location loc) {
		Random rand = new Random();

		loc.add(0.5, 0.5, 0.5);

		BlockIgniteEvent igniteEvent = new BlockIgniteEvent(loc.getBlock(),
				IgniteCause.FLINT_AND_STEEL, igniter);
		Bukkit.getServer().getPluginManager().callEvent(igniteEvent);
		if (igniteEvent.isCancelled()) {
			return;
		}

		List<Location> locations = new ArrayList<>();

		for (double x = loc.getX() - 2; x <= loc.getX() + 2; x++) {
			for (double y = loc.getY() - 1; y <= loc.getY() + 1; y++) {
				for (double z = loc.getZ() - 2; z <= loc.getZ() + 2; z++) {
					locations.add(new Location(loc.getWorld(), x, y, z));
				}
			}
		}

		for (Location l : locations) {
			BlockIgniteEvent igniteEvent2 = new BlockIgniteEvent(l.getBlock(),
					IgniteCause.FLINT_AND_STEEL, igniter);
			Bukkit.getServer().getPluginManager().callEvent(igniteEvent2);
			if (igniteEvent2.isCancelled()) {
				continue;
			}

			BlockState blockState = l.getBlock().getState();
			BlockPlaceEvent placeEvent = new BlockPlaceEvent(l.getBlock(), blockState, l.getBlock(),
					igniter.getInventory().getItemInMainHand(), igniter, true, EquipmentSlot.HAND);
			Bukkit.getServer().getPluginManager().callEvent(placeEvent);

			if (placeEvent.isCancelled() || !placeEvent.canBuild()) {
				continue;
			}

			if (l.getBlock().getType() == Material.AIR)
				l.getBlock().setType(Material.FIRE);
		}

		loc.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);

	}

}

