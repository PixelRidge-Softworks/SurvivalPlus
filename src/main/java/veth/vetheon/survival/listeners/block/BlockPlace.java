package veth.vetheon.survival.listeners.block;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.util.Utils;

import java.util.Random;

public class BlockPlace implements Listener {

	private Config config;
	private Lang lang;

	public BlockPlace(Survival plugin) {
		this.config = plugin.getSurvivalConfig();
		this.lang = plugin.getLang();
	}

	@SuppressWarnings("ConstantConditions")
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();

		ItemStack mainTool = player.getInventory().getItemInMainHand();
		ItemStack offTool = player.getInventory().getItemInOffHand();

		Block block = event.getBlock();

		if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
			if (config.PLACE_ONLY_WITH_HAMMER) {
				if (Utils.requiresHammer(block.getType())) {
					if (ItemManager.compare(offTool, Item.HAMMER)) {
						Random rand = new Random();
						int chance_reduceDur = rand.nextInt(10) + 1;
						if (chance_reduceDur == 1) {
							Utils.setDurability(offTool, Utils.getDurability(offTool) + 1);
						}

						if (Utils.getDurability(offTool) >= offTool.getType().getMaxDurability()) {
							player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
							player.getInventory().setItemInOffHand(null);
						}
					} else if (ItemManager.compare(mainTool, Item.HAMMER)) {
						Random rand = new Random();
						int chance_reduceDur = rand.nextInt(10) + 1;
						if (chance_reduceDur == 1) {
							Utils.setDurability(mainTool, ((Damageable) mainTool.getItemMeta()).getDamage() + 1);
						}

						if (Utils.getDurability(mainTool) >= mainTool.getType().getMaxDurability()) {
							player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
							player.getInventory().setItemInMainHand(null);
						}
					} else {
						event.setCancelled(true);
						player.updateInventory();
						player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.task_must_use_hammer));
					}
				}
			}
		}
	}

}