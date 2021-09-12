package veth.vetheon.survival.listeners.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CookieHealthBoost implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onConsume(PlayerItemConsumeEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		if (event.getItem().getType() == Material.COOKIE) {
			int amp = -1;
			int dur = 600;
			for (PotionEffect effect : player.getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.HEALTH_BOOST)) {
					dur += effect.getDuration();
					if (effect.getDuration() >= 300)
						amp++;
					if (effect.getDuration() >= 1200)
						amp++;
					if (effect.getDuration() >= 3600)
						amp++;
					player.removePotionEffect(effect.getType());
				}
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, dur, amp));
			player.setSaturation(player.getSaturation() + 4.6f);
		}
	}

}
