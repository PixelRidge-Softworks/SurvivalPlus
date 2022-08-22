package Pixelated.Studios.survival.listeners.item;

import java.util.Collection;
import java.util.Random;

import Pixelated.Studios.survival.data.PlayerData;
import Pixelated.Studios.survival.data.Stat;
import Pixelated.Studios.survival.managers.ItemManager;
import Pixelated.Studios.survival.item.Item;
import Pixelated.Studios.survival.config.Lang;
import Pixelated.Studios.survival.util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import Pixelated.Studios.survival.Survival;

public class Valkyrie implements Listener {

	// TODO: Investigate warning
	private Survival plugin;
	private Lang lang;

	public Valkyrie(Survival plugin) {
		this.plugin = plugin;
		this.lang = plugin.getLang();
	}

	@EventHandler
	private void onItemClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
		ItemStack mainItem = player.getInventory().getItemInMainHand();
		ItemMeta mainItemMeta = mainItem.getItemMeta();
		assert mainItemMeta != null;

		if (ItemManager.compare(mainItem, Item.VALKYRIES_AXE)) {
			if (playerData.getStat(Stat.DUAL_WIELD) == 0) {
				if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
					if (playerData.getStat(Stat.SPIN) == 0) {
						if (player.getFoodLevel() > 6) {
							Random rand = new Random();

							spin(player);

							if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
								player.setFoodLevel(player.getFoodLevel() - 1);

							int chance_reduceDur = rand.nextInt(10) + 1;
							if (chance_reduceDur == 1) {
								((Damageable) mainItemMeta).setDamage(((Damageable) mainItemMeta).getDamage() + 1);
								mainItem.setItemMeta(mainItemMeta);
							}

							if (((Damageable) mainItemMeta).getDamage() >= mainItem.getType().getMaxDurability()) {
								player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
								player.getInventory().setItemInMainHand(null);
							}
							player.updateInventory();
						} else {
							player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.lack_of_energy));
						}
					}
				}
			} else {
				if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
					playerData.setStat(Stat.DUAL_WIELD_MSG, playerData.getStat(Stat.DUAL_WIELD_MSG) + 1);
				else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
					playerData.setStat(Stat.DUAL_WIELD_MSG, playerData.getStat(Stat.DUAL_WIELD_MSG) + 2);
				if (playerData.getStat(Stat.DUAL_WIELD_MSG) == 2) {
					player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.valkyrie_axe_unable_dual));
				}
			}
		}
		playerData.setStat(Stat.DUAL_WIELD_MSG, 0);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onAttack(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (Utils.isCitizensNPC(player)) return;
			PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
			ItemStack mainItem = player.getInventory().getItemInMainHand();
			ItemMeta mainItemMeta = mainItem.getItemMeta();
			assert mainItemMeta != null;

			if (playerData.getStat(Stat.DUAL_WIELD) == 0) {
				if (ItemManager.compare(mainItem, Item.VALKYRIES_AXE)) {
					if (playerData.getStat(Stat.SPIN) == 0) {
						if (player.getFoodLevel() > 6) {
							Random rand = new Random();

							spin(player);

							if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
								player.setFoodLevel(player.getFoodLevel() - 1);

							int chance_reduceDur = rand.nextInt(10) + 1;
							if (chance_reduceDur == 1) {
								((Damageable) mainItemMeta).setDamage(((Damageable) mainItemMeta).getDamage() + 1);
								mainItem.setItemMeta(mainItemMeta);
							}

							if (((Damageable) mainItem.getItemMeta()).getDamage() >= mainItem.getType().getMaxDurability()) {
								assert player.getLocation().getWorld() != null;
								player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
								player.getInventory().setItemInMainHand(null);
							}
							player.updateInventory();
						} else {
							player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.lack_of_energy));
						}
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	private void spin(final Player player) {
		PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
		playerData.setStat(Stat.SPIN, 1);

		particleCircle(player, 50, 2.5f, Particle.CRIT);
		particleCircle(player, 25, 2f, Particle.CRIT);
		particleCircle(player, 10, 2.5f, Particle.CRIT_MAGIC);

		Random rand = new Random();
		assert player.getLocation().getWorld()  != null;
		player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, rand.nextFloat() * 0.4F + 0.8F);

		damageNearbyEnemies(player, 8);

		// TODO: replace statement lambda with expression lambda
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			playerData.setStat(Stat.SPIN, 0);
		}, 20L);
	}

	private void particleCircle(Player player, int particles, float radius, Particle particle) {
		Location location = player.getEyeLocation();
		for (int i = 0; i < particles; i++) {
			double angle, x, z;
			angle = 2 * Math.PI * i / particles;
			x = Math.cos(angle) * radius;
			z = Math.sin(angle) * radius;
			location.add(x, -0.3, z);
			Utils.spawnParticle(location, particle, 1, 0.5, 0.5, 0.5);
			location.subtract(x, -0.3, z);
		}
	}

	// TODO: Investigate warning
	private void damageNearbyEnemies(Player player, int dmg) {
		assert player.getLocation().getWorld()  != null;
		Collection<Entity> enemies = player.getLocation().getWorld().getNearbyEntities(player.getLocation().add(0, 0.5, 0), 3.5f, 1.5f, 3.5f);
		for (Entity e : enemies) {
			if (e instanceof LivingEntity && e != player) {
				LivingEntity enemy = (LivingEntity) e;
				enemy.damage(dmg, player);
			}
		}
	}

}
