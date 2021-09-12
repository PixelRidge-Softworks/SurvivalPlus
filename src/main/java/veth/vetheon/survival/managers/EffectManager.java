package veth.vetheon.survival.managers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.tasks.tool.*;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.util.Utils;

public class EffectManager {
	
	private final Survival plugin;
	private final Config config;

	// Effect Tasks
	private BlazeSwordEffects blazeSwordEffects = null;
	private BlazeSwordSound blazeSwordSound = null;
	private GiantBlade giantBlade = null;
	private ObsidianMace obsidianMace = null;
	private QuartzPickaxe quartzPickaxe = null;
	private Valkyrie valkyrie = null;
	
	public EffectManager(Survival plugin) {
		this.plugin = plugin;
		this.config = plugin.getSurvivalConfig();
		loadEffects();
	}

	private void loadEffects() {
		if (config.LEGENDARY_BLAZESWORD) {
			this.blazeSwordEffects = new BlazeSwordEffects(plugin);
			this.blazeSwordSound = new BlazeSwordSound(plugin);
		}
		if (config.LEGENDARY_GIANTBLADE)
			this.giantBlade = new GiantBlade(plugin);
		if (config.LEGENDARY_OBSIDIAN_MACE)
			this.obsidianMace = new ObsidianMace(plugin);
		if (config.LEGENDARY_VALKYRIE)
			this.valkyrie = new Valkyrie(plugin);
		if (config.LEGENDARY_QUARTZPICKAXE)
			this.quartzPickaxe = new QuartzPickaxe(plugin);
	}

	/**
	 * Stop all effect tasks
	 */
	@SuppressWarnings("unused")
	public void cancelTasks() {
		if (blazeSwordEffects != null)
			blazeSwordEffects.cancel();
		if (blazeSwordSound != null)
			blazeSwordSound.cancel();
		if (giantBlade != null)
			giantBlade.cancel();
		if (obsidianMace != null)
			obsidianMace.cancel();
		if (quartzPickaxe != null)
			quartzPickaxe.cancel();
		if (valkyrie != null)
			valkyrie.cancel();
	}

	/** Apply obsidian mace effects to player and enemy
	 * @param player Player to apply Regeneration to
	 * @param enemy Enemy to apply weakness and slowness to
	 */
	public void applyObsidianMaceEffects(Player player, LivingEntity enemy) {
		enemy.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0, false));
		enemy.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 48, 2, true));
		Location particleLoc = player.getLocation();
		particleLoc.setY(particleLoc.getY() + 2);
		Utils.spawnParticle(particleLoc, Particle.HEART, 2, 0.5, 0.5, 0.5);
	}

}
