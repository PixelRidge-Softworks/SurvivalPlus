package veth.vetheon.survival.listeners.entity;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.util.Utils;

public class BeeKeeperSuit implements Listener {

	@EventHandler
	private void onSting(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
		if (entity instanceof Player && damager instanceof Bee && !Utils.isCitizensNPC(entity)) {
			if (hasBeekeeperSuit((Player) entity)) {
				Bee bee = (Bee) damager;
				event.setCancelled(true);
				bee.setTarget(null);
				bee.setAnger(0);
			}
		}
	}

	@EventHandler
	private void onPoison(EntityPotionEffectEvent event) {
	    Entity entity = event.getEntity();
		if (entity instanceof Player && !Utils.isCitizensNPC(entity)) {
		    if (event.getCause() != Cause.ATTACK) return;
		    if (event.getModifiedType() != PotionEffectType.POISON) return;
		    if (event.getAction() != Action.ADDED) return;
			if (hasBeekeeperSuit((Player) entity)) {
			    event.setCancelled(true);
            }
		}
	}

	@EventHandler
	private void onTarget(EntityTargetLivingEntityEvent event) {
	    Entity target = event.getTarget();
	    Entity entity = event.getEntity();
		if (target instanceof Player && entity instanceof Bee && !Utils.isCitizensNPC(target)) {
			if (hasBeekeeperSuit((Player) target)) {
                event.setCancelled(true);
            }
		}
	}

	private boolean hasBeekeeperSuit(Player player) {
		PlayerInventory inv = player.getInventory();
		if (inv.getHelmet() == null || inv.getChestplate() == null || inv.getLeggings() == null || inv.getBoots() == null) {
			return false;
		}
		return ItemManager.compare(inv.getHelmet(), Item.BEEKEEPER_HELMET) && ItemManager.compare(inv.getChestplate(), Item.BEEKEEPER_CHESTPLATE) &&
				ItemManager.compare(inv.getLeggings(), Item.BEEKEEPER_LEGGINGS) && ItemManager.compare(inv.getBoots(), Item.BEEKEEPER_BOOTS);
	}

}

