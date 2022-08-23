package net.pixelatedstudios.SurvivalPlus.listeners.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.util.Utils;

public class Bow implements Listener {

	// TODO: Investigate warning
	private Lang lang;
	
	public Bow(Survival plugin) {
		this.lang = plugin.getLang();
	}

	@EventHandler
	private void onShootWithoutArrows(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			ItemStack mainHand = player.getInventory().getItemInMainHand();
			if (event.getBow() != null && mainHand.getType() == event.getBow().getType()) {
				if (Survival.getInstance().getPlayerManager().isArrowOffHand(player)) {
					event.setCancelled(false);
				} else {
					if (mainHand.getType() != Material.CROSSBOW) {
						event.setCancelled(true);
						Utils.sendColoredMsg(player, lang.arrows_off_hand);
						player.updateInventory();
					}
				}
			} else {
				event.setCancelled(true);
				Utils.sendColoredMsg(player, lang.bow_main_hand);
				player.updateInventory();
			}
		}
	}

	@EventHandler
	private void onLoadCrossbow(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if (mainHand.getType() == Material.CROSSBOW && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if (event.getHand() == EquipmentSlot.OFF_HAND) return;
			if (mainHand.getItemMeta() != null && ((CrossbowMeta) mainHand.getItemMeta()).hasChargedProjectiles()) return;
			if (!Survival.getInstance().getPlayerManager().isArrowOffHand(player)) {
				event.setCancelled(true);
				Utils.sendColoredMsg(player, lang.arrows_off_hand_crossbow);
			}
		} else if (offHand.getType() == Material.CROSSBOW) {
			if (event.getHand() == EquipmentSlot.HAND) return;
			event.setCancelled(true);
			Utils.sendColoredMsg(player, lang.bow_main_hand);
		}
	}

}
