package net.pixelatedstudios.SurvivalPlus.listeners.item;

import net.pixelatedstudios.SurvivalPlus.item.Item;
import org.bukkit.Keyed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RepairCrafting implements Listener {

    @EventHandler
    private void onCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe instanceof Keyed && ((Keyed) recipe).getKey().getNamespace().equalsIgnoreCase("survivalplus")) {
            // If this is a legit recipe, let's get outta here
            return;
        }
        CraftingInventory inventory = event.getInventory();

        List<ItemStack> items = new ArrayList<>();
        for (ItemStack itemStack : inventory.getMatrix()) {
            if (itemStack != null) {
                items.add(itemStack);
            }
        }

        if (items.size() == 2) {
            ItemStack iOne = items.get(0);
            ItemStack iTwo = items.get(1);
            Item itemOne = Item.getFromStack(iOne);
            Item itemTwo = Item.getFromStack(iTwo);
            if (itemOne != null && itemOne == itemTwo) {
                ItemStack result = repair(iOne, iTwo, itemOne);
                if (inventory.getType() == InventoryType.CRAFTING || itemOne.getRepairCostMultiplier() >= 0) {
                    // No repairing in player inventory
                    // Cost >= 0 signifies it requires an anvil
                    result = null;
                }
                // Since we're using a crafting table we're going to reset defaults
                // ie: remove enchantments
                result = resetItem(result);
                inventory.setResult(result);

            } else if ((itemOne != null && itemTwo == null) || (itemOne == null && itemTwo != null)) {
                // Prevent repairing custom items with vanilla items
                inventory.setResult(null);

            }
        }
        items.clear();
    }

    @EventHandler
    private void onAnvilRepair(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack iOne = inventory.getContents()[0];
        ItemStack iTwo = inventory.getContents()[1];
        if (iOne != null && iTwo != null) {
            if (iOne.getType() != iTwo.getType()) {
                // If two different items, lets get outta here
                // ie: enchanting
                return;
            }
            Item itemOne = Item.getFromStack(iOne);
            Item itemTwo = Item.getFromStack(iTwo);
            // Let's make sure we're joining two of the same item
            if (itemOne != null && itemOne == itemTwo) {
                ItemStack result = repair(iOne, iTwo, itemOne);
                event.setResult(result);
                double cost = itemOne.getRepairCostMultiplier();
                if (cost > 0) {
                    inventory.setRepairCost((int) Math.round(inventory.getRepairCost() * cost));
                }
            } else if ((itemOne != null && itemTwo == null) || (itemOne == null && itemTwo != null)) {
                // Prevent repairing custom items with vanilla items
                event.setResult(null);
            }
        }

    }

    private ItemStack repair(ItemStack itemStackOne, ItemStack itemStackTwo, Item item) {
        double repairPercent = item.getRepairPercent();
        if (repairPercent <= 0) {
            return null;
        }

        ItemStack result = itemStackOne.clone();
        double max = itemStackOne.getType().getMaxDurability();
        int dura1 = getRemainingDurability(itemStackOne);
        int dura2 = getRemainingDurability(itemStackTwo);
        int repair = (int) Math.min((dura1 + dura2 + Math.floor(max / 20)) * repairPercent, max);

        ItemMeta meta = result.getItemMeta();
        assert meta != null;
        ((Damageable) meta).setDamage((int) (max - repair));
        result.setItemMeta(meta);
        return result;
    }

    private ItemStack resetItem(ItemStack itemStack) {
        Item item = Item.getFromStack(itemStack);

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        int damage = ((Damageable) itemMeta).getDamage();

        assert item != null;
        ItemStack newItemStack = item.getItem();
        ItemMeta newItemMeta = newItemStack.getItemMeta();
        assert newItemMeta != null;
        ((Damageable) newItemMeta).setDamage(damage);
        newItemStack.setItemMeta(newItemMeta);

        return newItemStack;
    }

    private int getRemainingDurability(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof Damageable) {
            return itemStack.getType().getMaxDurability() - ((Damageable) meta).getDamage();
        }
        return 0;
    }

}
