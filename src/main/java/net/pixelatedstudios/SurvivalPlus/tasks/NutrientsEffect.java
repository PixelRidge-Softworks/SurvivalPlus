package net.pixelatedstudios.SurvivalPlus.tasks;

import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.Nutrient;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;

class NutrientsEffect extends BukkitRunnable {

	private final Config config;
	private final PlayerManager playerManager;
	private PotionEffect SALTS_NORMAL = null;
	private PotionEffect SALTS_HARD = null;
	private PotionEffect PROTEIN_NORMAL = null;
	private PotionEffect PROTEIN_HARD = null;

	NutrientsEffect(Survival plugin) {
		this.config = plugin.getSurvivalConfig();
		this.playerManager = plugin.getPlayerManager();
		loadEffects();
		this.runTaskTimer(plugin, -1, 320);
	}

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				PlayerData playerData = playerManager.getPlayerData(player);

				if (playerData.getNutrient(Nutrient.CARBS) <= 0) {
					// TODO: Replace switch with enhanced switch
					switch (player.getWorld().getDifficulty()) {
						case EASY:
							player.setExhaustion(player.getExhaustion() + Math.max(config.MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_EASY, 0));
							break;
						case NORMAL:
							player.setExhaustion(player.getExhaustion() + Math.max(config.MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_MEDIUM, 0));
							break;
						case HARD:
							player.setExhaustion(player.getExhaustion() + Math.max(config.MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_HARD, 0));
							break;
						default:
					}
				}

				if (playerData.getNutrient(Nutrient.SALTS) <= 0) {
					player.setExhaustion(player.getExhaustion() + Math.max(config.MECHANICS_FOOD_EFFECTS_SALTS_EX_AMP, 0));
					switch (player.getWorld().getDifficulty()) {
						case NORMAL:
							//update from deprecated
							if (SALTS_NORMAL != null) {
								// TODO: Replace deprecation
								player.addPotionEffect(SALTS_NORMAL, true);
							}
							break;
						case HARD:
							if (SALTS_HARD != null) {
								// TODO: Replace deprecation
								player.addPotionEffect(SALTS_HARD, true);
							}
							break;
						default:
					}
				}

				if (playerData.getNutrient(Nutrient.PROTEIN) <= 0) {
					player.setExhaustion(player.getExhaustion() + Math.max(config.MECHANICS_FOOD_EFFECTS_PROTEIN_EX_AMP, 0));
					switch (player.getWorld().getDifficulty()) {
						case NORMAL:
							if (PROTEIN_NORMAL != null) {
								// TODO: Replace deprecation
								player.addPotionEffect(PROTEIN_NORMAL, true);
							}
							break;
						case HARD:
							if (PROTEIN_HARD != null) {
								// TODO: Replace deprecation
								player.addPotionEffect(PROTEIN_HARD, true);
							}
							break;
						default:
					}
				}
			}
		}
	}

	private void loadEffects() {
		PotionEffectType s_normal_type = getType(config.MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_EFFECT);
		if (s_normal_type != null) {
			int s_normal_amp = config.MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_AMP;
			int s_normal_dur = config.MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_DURATION;
			SALTS_NORMAL = new PotionEffect(s_normal_type, s_normal_dur * 20, s_normal_amp);
		}
		PotionEffectType s_hard_type = getType(config.MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_EFFECT);
		if (s_hard_type != null) {
			int s_hard_amp = config.MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_AMP;
			int s_hard_dur = config.MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_DURATION;
			SALTS_HARD = new PotionEffect(s_hard_type, s_hard_dur * 20, s_hard_amp);
		}

		PotionEffectType p_normal_type = getType(config.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_EFFECT);
		if (p_normal_type != null) {
			int p_normal_amp = config.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_AMP;
			int p_normal_dur = config.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_DURATION;
			PROTEIN_NORMAL = new PotionEffect(p_normal_type, p_normal_dur * 20, p_normal_amp);
		}
		PotionEffectType p_hard_type = getType(config.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_EFFECT);
		if (p_hard_type != null) {
			int p_hard_amp = config.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_AMP;
			int p_hard_dur = config.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_DURATION;
			PROTEIN_HARD = new PotionEffect(p_hard_type, p_hard_dur * 20, p_hard_amp);
		}
	}

	private PotionEffectType getType(String potionType) {
		try {
			return PotionEffectType.getByName(potionType);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

}
