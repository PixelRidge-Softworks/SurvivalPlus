package veth.vetheon.survival.listeners.item;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.managers.EffectManager;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.util.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class ObsidianMaceWeakness implements Listener {

	// TODO: Investigate warning
	private EffectManager effectManager;
	private Config config;

	public ObsidianMaceWeakness(Survival plugin) {
		this.effectManager = plugin.getEffectManager();
		this.config = plugin.getSurvivalConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onAttack(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity && event.getCause() == DamageCause.ENTITY_ATTACK) {
			Player player = (Player) event.getDamager();
            if (Utils.isCitizensNPC(player)) return;
			ItemStack mainItem = player.getInventory().getItemInMainHand();
			LivingEntity enemy = (LivingEntity) event.getEntity();

			if (ItemManager.compare(mainItem, Item.OBSIDIAN_MACE)) {
				effectManager.applyObsidianMaceEffects(player, enemy);
			}
		}
	}

	// Prevent obsidian mace turning dirt/grass block into farmland
    @EventHandler
    private void onInteractBlock(PlayerInteractEvent event) {
	    if (!this.config.LEGENDARY_OBSIDIAN_MACE) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack tool = event.getItem();
            if (event.getClickedBlock() == null || tool == null) return;

            Material clickedBlock = event.getClickedBlock().getType();

            if (ItemManager.compare(tool, Item.OBSIDIAN_MACE)) {
                if (clickedBlock == Material.GRASS_BLOCK || clickedBlock == Material.DIRT) {
                    event.setCancelled(true);
                }
            }
        }
    }

}