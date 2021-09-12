package veth.vetheon.survival.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.item.Nutrition;
import veth.vetheon.survival.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class NutritionGUI implements InventoryHolder, Listener {

    private Inventory inv;
    private final Lang lang;
    private int page = 0;

    private final ItemStack LAST_PAGE_BUTTON;
    private final ItemStack NEXT_PAGE_BUTTON;

    public NutritionGUI(Survival plugin) {
        this.lang = plugin.getLang();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        LAST_PAGE_BUTTON = getButton(Material.PAPER, Utils.getColoredString(lang.nutrition_gui_last_page));
        NEXT_PAGE_BUTTON = getButton(Material.PAPER, Utils.getColoredString(lang.nutrition_gui_next_page));
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void openInventory(Player player, int page) {
        this.page = page;
        int p = page * 45;
        List<Nutrition> nutritions = Nutrition.getAllNutritions();
        int listSize = nutritions.size();
        boolean pages = listSize > 54;
        int rows;
        if (nutritions.size() <= 54) {
            rows = (int) Math.ceil((double) nutritions.size() / 9);
        } else {
            listSize = nutritions.size() - p;
            rows = listSize > 45 ? 6 : (int) Math.ceil((double) listSize / 9) + 1;
        }
        this.inv = Bukkit.createInventory(this, rows * 9, Utils.getColoredString(lang.nutrition_gui));

        for (int i = 0; i < (Math.min(listSize, pages ? 45 : 54)); i++) {
            Nutrition nutrition = nutritions.get(i + p);
            this.inv.setItem(i, getItemStack(nutrition));
        }
        if (pages && listSize > 45) {
            this.inv.setItem((9 * rows) - 1, NEXT_PAGE_BUTTON);
        }
        if (page > 0) {
            this.inv.setItem(9 * (rows - 1), LAST_PAGE_BUTTON);
        }
        player.openInventory(inv);
    }

    private ItemStack getButton(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.getColoredString(name));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private ItemStack getItemStack(Nutrition nutrition) {
        ItemStack item = nutrition.getItemStack();
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.add(" ");
        lore.add(Utils.getColoredString("&2" + lang.carbohydrates + ": &7" + nutrition.getCarbs()));
        lore.add(Utils.getColoredString("&4" + lang.protein + ": &7" + nutrition.getProteins()));
        lore.add(Utils.getColoredString("&5" + lang.vitamins + ": &7" + nutrition.getVitamins()));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if (inv.getHolder() != this) return;
        if (event.getInventory().getHolder() != inv.getHolder()) return;

        event.setCancelled(true);
        Player player = ((Player) event.getWhoClicked());

        int slot = event.getSlot();
        ItemStack item = event.getCurrentItem();

        if (slot < 0) {
            player.closeInventory();
            player.updateInventory();
        }
        if (event.getClickedInventory() != this.inv) {
            player.closeInventory();
            player.updateInventory();
        }
        if (item != null) {
            if (item.isSimilar(LAST_PAGE_BUTTON)) {
                openInventory(player, page - 1);
            } else if (item.isSimilar(NEXT_PAGE_BUTTON)) {
                openInventory(player, page + 1);
            }
        }
    }

}
