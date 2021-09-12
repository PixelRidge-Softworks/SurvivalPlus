package veth.vetheon.survival.listeners.server;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;

@SuppressWarnings("deprecation")
public class InventoryUpdate implements Listener {

    @EventHandler
    private void onJoinUpdate(PlayerJoinEvent e) { // Update old items to new items
        Inventory inv = e.getPlayer().getInventory();
        if (needsUpdate(inv)) {
            itemCheck(inv);
        }
    }

    @EventHandler
    private void onInventoryOpenUpdate(InventoryOpenEvent e) { // Update old items to new items
        Inventory inv = e.getInventory();
        if (needsUpdate(inv)) {
            itemCheck(inv);
        }
    }

    private void itemCheck(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) continue;
        }
    }

    private void itemUpdate(Inventory inv, int slot, ItemStack oldItem, Item newItem) {
        assert oldItem.getItemMeta() != null;
        int damage = ((Damageable) oldItem.getItemMeta()).getDamage();
        ItemStack item = ItemManager.get(newItem);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        ((Damageable) meta).setDamage(damage);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    private boolean needsUpdate(Inventory inv) {
        return inv.contains(Material.WOODEN_HOE) || inv.contains(Material.GOLDEN_PICKAXE) || inv.contains(Material.GOLDEN_AXE) ||
                inv.contains(Material.GOLDEN_SHOVEL) || inv.contains(Material.GOLDEN_HOE) || inv.contains(Material.GOLDEN_SWORD);
    }

}
