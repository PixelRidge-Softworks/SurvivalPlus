package Pixelated.Studios.survival.listeners.item;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import Pixelated.Studios.survival.util.Utils;

public class StarBattleaxeWither implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onAttack(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity && event.getCause() == DamageCause.ENTITY_ATTACK) {
			Player player = (Player) event.getDamager();
			if (Utils.isCitizensNPC(player)) return;
			ItemStack mainItem = player.getInventory().getItemInMainHand();
			LivingEntity enemy = (LivingEntity) event.getEntity();

			Random rand = new Random();

			if (mainItem.getType() == Material.GOLDEN_AXE) {
				enemy.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 480, 2, false));
				enemy.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 480, 0, false));
				enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
			}
		}
	}

}
