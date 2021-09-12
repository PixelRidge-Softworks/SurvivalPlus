package veth.vetheon.survival.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.inventory.ItemStack;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.data.PlayerData;
import veth.vetheon.survival.events.EnergyLevelChangeEvent;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.managers.PlayerManager;
import veth.vetheon.survival.util.Utils;

public class EnergyChange implements Listener {

	private final Survival plugin;
	private final PlayerManager playerManager;
	private final Config config;
	private final Lang lang;
	private final double ENERGY_RESPAWN;

	public EnergyChange(Survival plugin) {
		this.plugin = plugin;
		this.playerManager = plugin.getPlayerManager();
		this.config = plugin.getSurvivalConfig();
		this.lang = plugin.getLang();
		this.ENERGY_RESPAWN = config.MECHANICS_ENERGY_RESPAWN;
	}

	@EventHandler
	private void onRespawn(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (Utils.isCitizensNPC(player)) return;
		PlayerData playerData = playerManager.getPlayerData(player);

		double change = ENERGY_RESPAWN - playerData.getEnergy();
		EnergyLevelChangeEvent energyEvent = new EnergyLevelChangeEvent(player, change, ENERGY_RESPAWN);
		Bukkit.getPluginManager().callEvent(energyEvent);
		if (energyEvent.isCancelled()) return;
        playerData.setEnergy(ENERGY_RESPAWN);
	}

	@EventHandler
	private void onDrinkCoffee(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		Player player = e.getPlayer();
		PlayerData playerData = playerManager.getPlayerData(player);

		if (ItemManager.compare(item, Item.COFFEE)) {
			EnergyLevelChangeEvent energyEvent = new EnergyLevelChangeEvent(player, 20.0 - playerData.getEnergy(), 20.0);
			Bukkit.getPluginManager().callEvent(energyEvent);
			if (energyEvent.isCancelled()) return;
            playerData.setEnergy(20);
		}
	}

	// Removes empty water bottles from crafting grid when brewing coffee
	@EventHandler
	private void onCraftCoffee(CraftItemEvent e) {
		if (ItemManager.compare(e.getRecipe().getResult(), Item.COFFEE)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> e.getInventory().remove(Material.GLASS_BOTTLE), 2);
		}
	}

	// Decrease energy when player does exhaustive tasks
	@EventHandler
    private void onSaturationReached(FoodLevelChangeEvent event) {
        double modifier = config.MECHANICS_ENERGY_EXHAUSTION;
        if (modifier <= 0) return;
	    if (!(event.getEntity() instanceof Player)) return;
	    Player player = (Player) event.getEntity();
	    if (event.getFoodLevel() > player.getFoodLevel()) return;
        GameMode mode = player.getGameMode();
	    if (mode == GameMode.SURVIVAL || mode == GameMode.ADVENTURE) {
            PlayerData playerData = playerManager.getPlayerData(player);
            EnergyLevelChangeEvent energyEvent = new EnergyLevelChangeEvent(player, -modifier, playerData.getEnergy() - modifier);
            Bukkit.getPluginManager().callEvent(energyEvent);
            if (energyEvent.isCancelled()) return;
            playerData.increaseEnergy(-modifier);
        }
    }

	// Send messages when energy level decreases
    @EventHandler
    private void onEnergyDrop(EnergyLevelChangeEvent event) {
	    if (!config.MECHANICS_ENERGY_WARNING) return;
	    if (event.getChanged() < 0) {
            Player player = event.getPlayer();
            PlayerData playerData = playerManager.getPlayerData(player);
            double level = event.getEnergyLevel();
            double newLevel = playerData.getEnergy();

	        if (targetMatch(10.0, level, newLevel)) {
                Utils.sendColoredMsg(player, lang.energy_level_10);
            } else if (targetMatch(6.5, level, newLevel)) {
                Utils.sendColoredMsg(player, lang.energy_level_6_5);
            } else if (targetMatch(3.5, level, newLevel)) {
                Utils.sendColoredMsg(player, lang.energy_level_3_5);
            } else if (targetMatch(2, level, newLevel)) {
                Utils.sendColoredMsg(player, lang.energy_level_2);
            } else if (targetMatch(1, level, newLevel)) {
                Utils.sendColoredMsg(player, lang.energy_level_1);
            }
        }
    }

    // Check if the change passed a certain amount
    private boolean targetMatch(double target, double level, double newLevel) {
	    return level <= target && newLevel > target;
    }

    // Increase players energy when they wakeup after the night skips
    // Or cancel the night skip event if true in config (forcing players to lay in bed and let energy increase)
    @EventHandler
    private void onSkipNight(TimeSkipEvent event) {
	    if (config.MECHANICS_PREVENT_NIGHT_SKIP) {
            if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
                event.setCancelled(true);
            }
        } else {
	        for (Player player : event.getWorld().getPlayers()) {
	            if (!player.isSleeping()) continue;
                PlayerData playerData = playerManager.getPlayerData(player);

                EnergyLevelChangeEvent energyEvent = new EnergyLevelChangeEvent(player, 20.0 - playerData.getEnergy(), 20.0);
                Bukkit.getPluginManager().callEvent(energyEvent);
                if (energyEvent.isCancelled()) return;
                playerData.setEnergy(20);
            }
        }
    }

}
