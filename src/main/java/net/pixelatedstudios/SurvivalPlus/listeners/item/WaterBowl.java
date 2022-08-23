package net.pixelatedstudios.SurvivalPlus.listeners.item;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.events.WaterBowlFillEvent;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class WaterBowl implements Listener {

    private final Survival plugin;
    private final boolean THIRST_ENABLED;
    private final boolean CLAY_ENABLED;

    public WaterBowl(Survival plugin) {
        this.plugin = plugin;
        this.THIRST_ENABLED = plugin.getSurvivalConfig().MECHANICS_THIRST_ENABLED;
        this.CLAY_ENABLED = plugin.getSurvivalConfig().RECIPES_CLAY;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onConsume(PlayerItemConsumeEvent event) {
        if (!THIRST_ENABLED) {
            if (event.isCancelled()) return;
            if (ItemManager.compare(event.getItem(), Item.WATER_BOWL)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onDrop(ItemSpawnEvent event) {
        if (event.isCancelled()) return;
        if (THIRST_ENABLED || CLAY_ENABLED) {
            final org.bukkit.entity.Item itemDrop = event.getEntity();
            if (itemDrop.getItemStack().getType() == Material.BOWL) {
                final Runnable task = () -> {
                    Location itemLocation = itemDrop.getLocation();
                    if (itemLocation.getBlock().getType() == Material.WATER) {
                        WaterBowlFillEvent bowlFillEvent = new WaterBowlFillEvent(itemDrop.getItemStack());
                        Bukkit.getPluginManager().callEvent(bowlFillEvent);
                        if (bowlFillEvent.isCancelled()) return;
                        int amount = itemDrop.getItemStack().getAmount();
                        itemDrop.remove();
                        for (int i = 0; i < amount; i++) {
                            itemDrop.getWorld().dropItem(itemLocation, Item.WATER_BOWL.getItem());
                        }
                    }
                };
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, 20L);
            }
        }
    }

    // Prevent water bowls turning into glass bottles
    @EventHandler
    private void onCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe instanceof Keyed) {
            String key = ((Keyed) recipe).getKey().getKey();
            if (key.equalsIgnoreCase("glass_bottle")) {
                CraftingInventory inventory = event.getInventory();
                for (ItemStack itemStack : inventory.getMatrix()) {
                    if (itemStack != null && Item.WATER_BOWL.compare(itemStack)) {
                        inventory.setResult(null);
                    }
                }
            }
        }

    }

}
