package net.pixelatedstudios.SurvivalPlus.listeners.block;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class NoAnvil implements Listener {

    private Lang lang;

    public NoAnvil(Survival plugin) {
        this.lang = plugin.getLang();
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();

        if (inv instanceof AnvilInventory) {
            AnvilInventory anvil = (AnvilInventory) inv;
            InventoryView view = e.getView();
            int rawSlot = e.getRawSlot();

            // compare raw slot to the inventory view to make sure we are in the upper inventory
            if (rawSlot == view.convertSlot(rawSlot)) {
                // 2 = result slot
                if (rawSlot == 2) {
                    // item in the left slot
                    ItemStack item = anvil.getContents()[0];

                    if (item != null) {
                        if (ItemManager.compare(item, Item.VALKYRIES_AXE)
                                || ItemManager.compare(item, Item.QUARTZ_PICKAXE) || ItemManager.compare(item, Item.OBSIDIAN_MACE)
                                || ItemManager.compare(item, Item.ENDER_GIANT_BLADE) || ItemManager.compare(item, Item.BLAZE_SWORD)
                                || ItemManager.compare(item, Item.HATCHET) || ItemManager.compare(item, Item.MATTOCK)
                                || ItemManager.compare(item, Item.FIRESTRIKER) || ItemManager.compare(item, Item.SHIV)
                                || ItemManager.compare(item, Item.HAMMER)) {
                            e.setCancelled(true);
                            e.getWhoClicked().closeInventory();
                            // TODO: Remove getDisplayName() and replace with new Paper Adventure Library one
                            e.getWhoClicked().sendMessage(ChatColor.RED + Utils.getColoredString(lang.no_rename) + item.getItemMeta().getDisplayName() + ChatColor.RED + Utils.getColoredString(lang.period));
                        }
                    }
                }
            }
        }
    }

}