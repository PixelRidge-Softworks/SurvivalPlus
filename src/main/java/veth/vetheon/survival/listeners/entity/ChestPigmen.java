package veth.vetheon.survival.listeners.entity;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Chest;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import veth.vetheon.survival.Survival;

import java.util.ArrayList;
import java.util.List;

public class ChestPigmen implements Listener {

	private final List<Material> GOLD_ITEMS;
	private final int RADIUS;
	// TODO: Investigate warning
	private double SPEED;

	public ChestPigmen(Survival plugin) {
		GOLD_ITEMS = new ArrayList<>();
		GOLD_ITEMS.add(Material.GOLDEN_SWORD);
		GOLD_ITEMS.add(Material.GOLDEN_SHOVEL);
		GOLD_ITEMS.add(Material.GOLDEN_PICKAXE);
		GOLD_ITEMS.add(Material.GOLDEN_AXE);
		GOLD_ITEMS.add(Material.GOLDEN_HOE);
		GOLD_ITEMS.add(Material.GOLDEN_HELMET);
		GOLD_ITEMS.add(Material.GOLDEN_CHESTPLATE);
		GOLD_ITEMS.add(Material.GOLDEN_LEGGINGS);
		GOLD_ITEMS.add(Material.GOLDEN_BOOTS);
		GOLD_ITEMS.add(Material.GOLD_BLOCK);
		GOLD_ITEMS.add(Material.GOLD_INGOT);
		GOLD_ITEMS.add(Material.GOLD_NUGGET);
		RADIUS = plugin.getSurvivalConfig().ENTITY_MECHANICS_PIGMEN_CHEST_RADIUS;
		SPEED = plugin.getSurvivalConfig().ENTITY_MECHANICS_PIGMEN_CHEST_SPEED;
	}

	@EventHandler
	private void onOpenChest(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld().getEnvironment() != World.Environment.NETHER) return;
		if (event.getClickedBlock() == null) return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock().getType() != Material.CHEST) return;
		Chest chest = ((Chest) event.getClickedBlock().getState());
		if (chestContainsGold(chest)) {
			player.getNearbyEntities(RADIUS, RADIUS, RADIUS).forEach(entity -> {
				if (entity instanceof PigZombie) {
					((PigZombie) entity).setTarget(player);
					moveFaster((Attributable) entity, SPEED);
				}
			});
		}
	}

	private boolean chestContainsGold(Chest block) {
		for (ItemStack item : block.getInventory().getContents()) {
			if (item == null) continue;
			if (GOLD_ITEMS.contains(item.getType())) return true;
		}
		return false;
	}

	private void moveFaster(Attributable entity, double modifier) {
		if (entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
			// TODO: Investigate warning
			double speed = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
			// TODO: Investigate warning
			entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed * modifier);
		}
	}

}

