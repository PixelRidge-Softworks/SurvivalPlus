package veth.vetheon.survival.listeners.item;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.util.Utils;

import java.util.Random;

public class ShivPoison implements Listener {

	// TODO: Investigate warning
    private Config config;

    public ShivPoison(Survival plugin) {
        this.config = plugin.getSurvivalConfig();
    }

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onAttack(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity && event.getCause() == DamageCause.ENTITY_ATTACK) {
			Player player = (Player) event.getDamager();
            if (Utils.isCitizensNPC(player)) return;
			ItemStack mainItem = player.getInventory().getItemInMainHand();
			ItemStack offItem = player.getInventory().getItemInOffHand();
			LivingEntity enemy = (LivingEntity) event.getEntity();

			Random rand = new Random();

			if (ItemManager.compare(mainItem, Item.SHIV)) {
                ItemMeta mainItemMeta = mainItem.getItemMeta();
				enemy.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0, false));
				assert mainItemMeta != null;
				if (((Damageable) mainItemMeta).getDamage() >= mainItem.getType().getMaxDurability()) {
					player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
					player.getInventory().setItemInMainHand(null);
				}
			}

			if (ItemManager.compare(offItem, Item.SHIV)) {
				int chance_poison = rand.nextInt(4) + 1;
				// TODO: Replace switch with enhanced switch
				switch (chance_poison) {
					case 1:
					case 2:
						enemy.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0, false));
						break;
					default:
				}
				ItemMeta offItemMeta = offItem.getItemMeta();
				assert offItemMeta != null;
				((Damageable) offItemMeta).setDamage(((Damageable) offItemMeta).getDamage() + 1);
				offItem.setItemMeta(offItemMeta);
				if (((Damageable) offItem.getItemMeta()).getDamage() >= offItem.getType().getMaxDurability()) {
					player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
					player.getInventory().setItemInOffHand(null);
				}
			}
		}
	}

	// Prevent shiv from turning dirt/grass block into farmland
    @EventHandler
    private void onInteractBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack tool = event.getItem();
            if (event.getClickedBlock() == null || tool == null) return;

            if (config.SURVIVAL_ENABLED && ItemManager.compare(tool, Item.SHIV)) {
				// TODO: Replace switch with enhanced switch
                switch (event.getClickedBlock().getType()) {
                    case DIRT:
                    case GRASS_BLOCK:
                        event.setCancelled(true);
                }
            }
        }
    }

}

