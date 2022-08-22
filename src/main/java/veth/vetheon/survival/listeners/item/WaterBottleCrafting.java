package veth.vetheon.survival.listeners.item;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.config.Config;

import java.util.Objects;


public class WaterBottleCrafting implements Listener {

	//  TODO: Investigate warnings
	private Survival plugin;
	private Config config;

	public WaterBottleCrafting(Survival plugin) {
		this.plugin = plugin;
		this.config = plugin.getSurvivalConfig();
	}

	@EventHandler
	private void onCraft(CraftItemEvent e) {
		final Player player = (Player) e.getWhoClicked();
		final CraftingInventory inv = e.getInventory();

		ItemStack[] bottles = inv.getMatrix();
		ItemStack result = inv.getResult();

		if (result != null && result.getType() == Material.CLAY) {
			for (int i = 0; i < bottles.length; i++) {
				if (bottles[i] == null) continue;
				if (bottles[i].getType() == Material.POTION) {
					final int slot = i + 1;
					Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
						inv.setItem(slot, new ItemStack(Material.BOWL));
						player.updateInventory();
					}, 1);
				}
			}
		}
	}

	@EventHandler
	private void onFillWaterBottle(PlayerInteractEvent e) {
		if (!config.MECHANICS_THIRST_PURIFY_WATER) return;
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		if (item != null && item.getType() == Material.GLASS_BOTTLE) {
            Block targetBlock = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
            if (targetBlock == null) return;
            if (isWaterBlock(targetBlock)) {
				e.setCancelled(true);
				if (item.getAmount() > 1) {
				    if (player.getInventory().addItem(ItemManager.get(Item.DIRTY_WATER)).size() > 0) {
				        player.getWorld().dropItem(player.getLocation(), Item.DIRTY_WATER.getItem());
                    }
					if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
						item.setAmount(item.getAmount() - 1);
				} else {
					if (player.getInventory().getItemInMainHand().getType() == item.getType())
						player.getInventory().setItemInMainHand(ItemManager.get(Item.DIRTY_WATER));
					else if (player.getInventory().getItemInOffHand().getType() == item.getType())
						player.getInventory().setItemInOffHand(ItemManager.get(Item.DIRTY_WATER));
				}
			}
		}
	}

	private boolean isWaterBlock(Block block) {
	    if (block.getType() == Material.WATER) {
	        return true;
        }
        BlockData data = block.getBlockData();
        return data instanceof Waterlogged && ((Waterlogged) data).isWaterlogged();
    }

	// TODO: Investigate if this is needed
	private boolean checkWaterBottle(ItemStack bottle) {

		return ((PotionMeta) Objects.requireNonNull(bottle.getItemMeta())).getBasePotionData().getType() == PotionType.WATER;

	}

}
