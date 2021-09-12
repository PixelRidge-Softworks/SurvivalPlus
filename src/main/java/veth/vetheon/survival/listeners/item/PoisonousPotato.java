package veth.vetheon.survival.listeners.item;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonousPotato implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onConsume(PlayerItemConsumeEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		if (event.getItem().getType() == Material.POISONOUS_POTATO) {
			for (PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());

			Random rand = new Random();
			if (rand.nextInt(10) + 1 <= 6) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0), true);
			}
		}
	}

}