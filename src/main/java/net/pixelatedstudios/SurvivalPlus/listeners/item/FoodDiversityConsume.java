package net.pixelatedstudios.SurvivalPlus.listeners.item;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.data.Nutrient;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;
import net.pixelatedstudios.SurvivalPlus.item.Nutrition;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class FoodDiversityConsume implements Listener {

    private final PlayerManager playerManager;
    private final int RESPAWN_PROTEIN, RESPAWN_CARBS, RESPAWN_SALTS;

    public FoodDiversityConsume(Survival plugin) {
        this.playerManager = plugin.getPlayerManager();
        Config config = plugin.getSurvivalConfig();
        RESPAWN_PROTEIN = config.MECHANICS_FOOD_RESPAWN_PROTEINS;
        RESPAWN_CARBS = config.MECHANICS_FOOD_RESPAWN_CARBS;
        RESPAWN_SALTS = config.MECHANICS_FOOD_RESPAWN_SALTS;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();

        Nutrition nutrition = Nutrition.getByItemStack(event.getItem());
        if (nutrition != null) {
            addStats(player, nutrition);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onConsumeCake(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block cake = event.getClickedBlock();
            assert cake != null;
            if (cake.getType().equals(Material.CAKE)) {
                if (player.getFoodLevel() < 20 && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)) {
                    addStats(player, Nutrition.CAKE);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDamage(EntityDamageEvent event) {
        DamageCause cause = event.getCause();
        if (cause == DamageCause.VOID || cause == DamageCause.CUSTOM) return;
        if (event.isCancelled()) return;

        Entity entity = event.getEntity();
        if (entity instanceof Player && !Utils.isCitizensNPC(entity)) {
            event.setDamage(event.getDamage() * addMultiplier((Player) entity));
        }
    }

    @EventHandler
    private void onRespawn(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (Utils.isCitizensNPC(player)) return;

        setStats(player, RESPAWN_CARBS, RESPAWN_PROTEIN, RESPAWN_SALTS);
    }

    private void addStats(Player player, Nutrient nutrient, int point) {
        PlayerData playerData = playerManager.getPlayerData(player);
        playerData.setNutrient(nutrient, playerData.getNutrient(nutrient) + point);
    }

    private void addStats(Player player, Nutrition nutrition) {
        addStats(player, Nutrient.CARBS, nutrition.getCarbs());
        addStats(player, Nutrient.PROTEIN, nutrition.getProteins());
        addStats(player, Nutrient.SALTS, nutrition.getVitamins());
    }

    private void setStats(Player player, int carbs, int proteins, int vitamins) {
        PlayerData playerData = playerManager.getPlayerData(player);
        playerData.setNutrient(Nutrient.CARBS, carbs);
        playerData.setNutrient(Nutrient.PROTEIN, proteins);
        playerData.setNutrient(Nutrient.SALTS, vitamins);
    }

    private double addMultiplier(Player player) {
        PlayerData playerData = playerManager.getPlayerData(player);
        double damageMultiplier = 1;

        if (playerData.getNutrient(Nutrient.PROTEIN) <= 75) {
            // TODO: replace switch with enhanced switch
            switch (player.getWorld().getDifficulty()) {
                case EASY:
                    damageMultiplier *= 1.25;
                    break;
                case NORMAL:
                    damageMultiplier *= 1.5;
                    break;
                case HARD:
                    damageMultiplier *= 2;
                    break;
                default:
            }
        }
        if (playerData.getNutrient(Nutrient.SALTS) <= 100) {
            // TODO: replace switch with enhanced switch
            switch (player.getWorld().getDifficulty()) {
                case EASY:
                    damageMultiplier *= 1.25;
                    break;
                case NORMAL:
                    damageMultiplier *= 1.5;
                    break;
                case HARD:
                    damageMultiplier *= 2;
                    break;
                default:
            }
        }
        return damageMultiplier;
    }

}
