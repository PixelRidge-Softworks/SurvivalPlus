package veth.vetheon.survival.listeners.server;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.managers.RecipeManager.Recipes;

public class RecipeDiscovery implements Listener {

    private final Survival plugin;
    private final boolean UNLOCK_ALL;

    public RecipeDiscovery(Survival plugin) {
        this.plugin = plugin;
        this.UNLOCK_ALL = plugin.getSurvivalConfig().SURVIVAL_UNLOCK_ALL_RECIPES;
    }

    // When a player first joins, give them a few recipes after 10 seconds
    @EventHandler
    private void onFirstJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (this.UNLOCK_ALL) {
                this.plugin.getRecipeManager().unlockAllRecipes(player);
            } else {
                player.discoverRecipes(Recipes.HATCHET.getKeys());
                player.discoverRecipes(Recipes.MATTOCK.getKeys());
                player.discoverRecipes(Recipes.SHIV.getKeys());
                player.discoverRecipes(Recipes.HAMMER.getKeys());
                player.discoverRecipes(Recipes.GLASS_BOTTLE.getKeys());
                player.discoverRecipes(Recipes.STICK.getKeys());
                player.discoverRecipes(Recipes.BREAD.getKeys());
                player.discoverRecipes(Recipes.STRING.getKeys());
                player.discoverRecipes(Recipes.WATER_BOTTLES.getKeys());
            }
            player.discoverRecipe(NamespacedKey.minecraft("bowl"));
        }, 200);
    }

    // When a player picks up items, unlock different item based recipes
    @EventHandler
    private void onPickupItems(EntityPickupItemEvent e) {
        if (this.UNLOCK_ALL) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player player = ((Player) e.getEntity());
        Material item = e.getItem().getItemStack().getType();
        if (item == Material.DIAMOND) {
            player.discoverRecipes(Recipes.DIAMOND_BOOTS.getKeys());
            player.discoverRecipes(Recipes.DIAMOND_CHESTPLATE.getKeys());
            player.discoverRecipes(Recipes.DIAMOND_LEGGINGS.getKeys());
            player.discoverRecipes(Recipes.DIAMOND_HELMET.getKeys());
            player.discoverRecipes(Recipes.DIAMOND_HORSE_ARMOR.getKeys());
            player.discoverRecipes(Recipes.VALKYRIES_AXE.getKeys());
            player.discoverRecipes(Recipes.QUARTZ_PICKAXE.getKeys());
            player.discoverRecipes(Recipes.ENDER_GIANT_BLADE.getKeys());
            player.discoverRecipes(Recipes.DIAMOND_SICKLE.getKeys());
        } else if (item == Material.FLINT) {
            player.discoverRecipes(Recipes.FIRESTRIKER.getKeys());
            player.discoverRecipes(Recipes.GRAVEL.getKeys());
            player.discoverRecipes(Recipes.FLINT_SICKLE.getKeys());
        } else if (item == Material.FEATHER) {
            player.discoverRecipes(Recipes.MEDIC_KIT.getKeys());
            player.discoverRecipes(Recipes.FISHING_ROD.getKeys());
        } else if (item == Material.BLAZE_POWDER || item == Material.BLAZE_ROD) {
            player.discoverRecipes(Recipes.BLAZE_SWORD.getKeys());
        } else if (item == Material.LEATHER) {
            player.discoverRecipes(Recipes.SADDLE.getKeys());
            player.discoverRecipes(Recipes.LEATHER_HORSE_ARMOR.getKeys());
        } else if (item == Material.GRAVEL) {
            player.discoverRecipes(Recipes.FLINT.getKeys());
        } else if (item == Material.ROTTEN_FLESH) {
            player.discoverRecipes(Recipes.FERMENTED_SKIN.getKeys());
        } else if (item == Material.STRING) {
            player.discoverRecipes(Recipes.COBWEB.getKeys());
            player.discoverRecipes(Recipes.RECURVED_BOW.getKeys());
        } else if (item == Material.SPIDER_EYE) {
            player.discoverRecipes(Recipes.FERMENTED_SPIDER_EYE.getKeys());
        } else if (item == Material.POTATO) {
            player.discoverRecipes(Recipes.POISONOUS_POTATO.getKeys());
        } else if (item == Material.COBBLESTONE) {
            player.discoverRecipes(Recipes.ANDESITE.getKeys());
            player.discoverRecipes(Recipes.DIORITE.getKeys());
            player.discoverRecipes(Recipes.GRANITE.getKeys());
            player.discoverRecipes(Recipes.STONE_SICKLE.getKeys());
        } else if (item == Material.QUARTZ) {
            player.discoverRecipes(Recipes.QUARTZ.getKeys());
        } else if (item == Material.DIRT) {
            player.discoverRecipes(Recipes.CLAY.getKeys());
        } else if (item == Material.EGG) {
            player.discoverRecipes(Recipes.COOKIE.getKeys());
        } else if (ItemManager.compare(e.getItem().getItemStack(), Item.WATER_BOWL)) {
            player.discoverRecipes(Recipes.BOWL.getKeys());
        } else if (item == Material.VINE) {
            player.discoverRecipes(Recipes.SLIMEBALL.getKeys());
        } else if (item == Material.REDSTONE) {
            player.discoverRecipes(Recipes.COMPASS.getKeys());
        } else if (item == Material.HONEYCOMB) {
            player.discoverRecipes(Recipes.BEEKEEPER_SUIT.getKeys());
        }
    }

    // When a player smelts items, unlock different item based recipes
    @EventHandler
    private void onFurnaceExtract(FurnaceExtractEvent event) {
        if (this.UNLOCK_ALL) return;
        Player player = event.getPlayer();
        if (event.getItemType() == Material.IRON_INGOT) {
            player.discoverRecipes(Recipes.IRON_BOOTS.getKeys());
            player.discoverRecipes(Recipes.IRON_CHESTPLATE.getKeys());
            player.discoverRecipes(Recipes.IRON_HELMET.getKeys());
            player.discoverRecipes(Recipes.IRON_LEGGINGS.getKeys());
            player.discoverRecipes(Recipes.IRON_HORSE_ARMOR.getKeys());
            player.discoverRecipes(Recipes.IRON_INGOT.getKeys());
            player.discoverRecipes(Recipes.IRON_SICKLE.getKeys());
            player.discoverRecipes(Recipes.IRON_NUGGET.getKeys());
        } else if (event.getItemType() == Material.GOLD_INGOT) {
            player.discoverRecipes(Recipes.GOLD_NUGGET.getKeys());
            player.discoverRecipes(Recipes.GOLD_INGOT.getKeys());
            player.discoverRecipes(Recipes.GOLD_CROWN.getKeys());
            player.discoverRecipes(Recipes.GOLD_GREAVES.getKeys());
            player.discoverRecipes(Recipes.GOLD_GUARD.getKeys());
            player.discoverRecipes(Recipes.GOLD_SABATONS.getKeys());
            player.discoverRecipes(Recipes.GOLD_HORSE_ARMOR.getKeys());
            player.discoverRecipes(Recipes.ENCHANTED_GOLDEN_APPLE.getKeys());
        }
    }

    // When a player breaks a block, unlock different item based recipes
    @EventHandler
    private void onPlayerBreakBlock(BlockBreakEvent e) {
        if (this.UNLOCK_ALL) return;
        Player player = e.getPlayer();
        Material item = e.getBlock().getType();
        if (e.isCancelled()) return;
        if (Tag.LOGS.isTagged(item)) {
            player.discoverRecipes(Recipes.WORKBENCH.getKeys());
            player.discoverRecipe(NamespacedKey.minecraft("crafting_table")); //unlocks vanilla recipe if custom workbench recipe is set to false
            player.discoverRecipes(Recipes.CHEST.getKeys());
            player.discoverRecipes(Recipes.UNLIT_CAMPFIRE.getKeys());
        } else if (item == Material.OBSIDIAN) {
            player.discoverRecipes(Recipes.OBSIDIAN_MACE.getKeys());
        } else if (item == Material.ICE || item == Material.BLUE_ICE || item == Material.FROSTED_ICE || item == Material.PACKED_ICE) {
            player.discoverRecipes(Recipes.ICE.getKeys());
            player.discoverRecipes(Recipes.PACKED_ICE.getKeys());
        } else if (item == Material.STONE) {
            player.discoverRecipe(NamespacedKey.minecraft("furnace")); //unlocks vanilla recipe if custom furnace recipe is set to false
        }
    }

    // When a player crafts an item, unlock different item based recipes
    @EventHandler
    private void onCraft(CraftItemEvent e) {
        if (this.UNLOCK_ALL) return;
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = ((Player) e.getWhoClicked());
        ItemStack result = e.getRecipe().getResult();
        if (ItemManager.compare(result, Item.FIRESTRIKER)) {
            player.discoverRecipes(Recipes.TORCH.getKeys());
            player.discoverRecipes(Recipes.FURNACE.getKeys());
        } else if (result.getType() == Material.FURNACE) {
            player.discoverRecipes(Recipes.FURNACE_GOLD_INGOT.getKeys());
            player.discoverRecipes(Recipes.FURNACE_IRON_INGOT.getKeys());
        } else if (result.getType() == Material.BLAST_FURNACE) {
            player.discoverRecipes(Recipes.BLAST_GOLD_INGOT.getKeys());
            player.discoverRecipes(Recipes.BLAST_IRON_INGOT.getKeys());
        } else if (result.getType() == Material.CROSSBOW) {
            player.discoverRecipes(Recipes.RECURVED_CROSSBOW.getKeys());
        } else if (result.getType() == Material.LEATHER_HELMET || result.getType() == Material.LEATHER_CHESTPLATE
                || result.getType() == Material.LEATHER_LEGGINGS || result.getType() == Material.LEATHER_BOOTS) {
            player.discoverRecipes(Recipes.REINFORCED_LEATHER_HELMET.getKeys());
            player.discoverRecipes(Recipes.REINFORCED_LEATHER_CHESTPLATE.getKeys());
            player.discoverRecipes(Recipes.REINFORCED_LEATHER_LEGGINGS.getKeys());
            player.discoverRecipes(Recipes.REINFORCED_LEATHER_BOOTS.getKeys());
        } else if (result.getType() == Material.PAPER) {
            player.discoverRecipes(Recipes.NAMETAG.getKeys());
            player.discoverRecipes(Recipes.MEDIC_KIT.getKeys());
        } else if (result.getType() == Material.STRING) {
            player.discoverRecipes(Recipes.COBWEB.getKeys());
            player.discoverRecipes(Recipes.RECURVED_BOW.getKeys());
        } else if (result.getType() == Material.BRICK || result.getType() == Material.BRICKS) {
            player.discoverRecipes(Recipes.CLAY_BRICK.getKeys());
        } else if (result.getType() == Material.FISHING_ROD) {
            player.discoverRecipes(Recipes.GRAPPLING_HOOK.getKeys());
        } else if (result.getType() == Material.GLASS_BOTTLE) {
            player.discoverRecipes(Recipes.COFFEE.getKeys());
            player.discoverRecipes(Recipes.COFFEE_BEAN.getKeys());
            player.discoverRecipes(Recipes.HOT_MILK.getKeys());
            player.discoverRecipes(Recipes.COLD_MILK.getKeys());
        }
    }

}
