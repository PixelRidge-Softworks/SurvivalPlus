package Pixelated.Studios.survival.listeners.entity;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import Pixelated.Studios.survival.Survival;
import Pixelated.Studios.survival.config.Config;
import Pixelated.Studios.survival.item.Item;

import java.util.List;
import java.util.Random;

public class EntityDeath implements Listener {

    private final Config config;
    private final int SUSPICIOUS_MEAT_CHANCE;

    public EntityDeath(Survival plugin) {
        this.config = plugin.getSurvivalConfig();
        this.SUSPICIOUS_MEAT_CHANCE = Math.max(0, this.config.ENTITY_MECHANICS_SUSPICIOUS_MEAT_CHANCE);
    }

    @EventHandler
    private void onEntityDeath(EntityDeathEvent event) {
        if (!this.config.ENTITY_MECHANICS_SUSPICIOUS_MEAT_ENABLED) return; // May need to move if we add more items to drop in the future
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer != null) {
            int random = new Random().nextInt(100) + 1;
            if (random > this.SUSPICIOUS_MEAT_CHANCE) return;
            // TODO: Replace switch with enhanced switch
            switch (entity.getType()) {
                case ZOMBIE:
                case DROWNED:
                case HUSK:
                case ZOMBIE_VILLAGER:
                case ZOMBIE_HORSE:
                    replaceDrops(event.getDrops());
            }
        }
    }

    private void replaceDrops(List<ItemStack> items) {
        items.removeIf(item -> item.getType() == Material.ROTTEN_FLESH);
        items.add(Item.SUSPICIOUS_MEAT.getItem());
    }

}
