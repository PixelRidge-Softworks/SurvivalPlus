package veth.vetheon.survival.item.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.util.Utils;

public class FireStriker implements Runnable, InventoryHolder {

    private final int id;
    private final Inventory inv;
    private final Player player;
    private final ItemStack item;

    private final int MAX_COOK_TIME;
    private int cookTime;
    private int burnTime;

    public FireStriker(Player player, ItemStack item) {
        Survival plugin = Survival.getInstance();
        Lang lang = plugin.getLang();
        this.inv = Bukkit.createInventory(this, InventoryType.FURNACE, Utils.getColoredString(lang.firestriker));
        this.player = player;
        this.item = item;
        this.MAX_COOK_TIME = plugin.getSurvivalConfig().ITEM_FIRESTRIKER_COOK_TIME;
        this.cookTime = 0;

        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        this.burnTime = 8 - (((Damageable) itemMeta).getDamage() / 7);
        this.id = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 1).getTaskId();
    }

    @Override
    public void run() {
        tick();
    }

    private void tick() {
        if (canCook() && canBurn()) {
            if (cookTime < MAX_COOK_TIME) {
                cookTime++;
            } else {
                cook();
                burn();
                cookTime = 0;
            }
        } else {
            if (cookTime > 1) {
                cookTime -= 2;
            } else {
                cookTime = 0;
            }
            if (!canBurn()) {
                burnTime = 0;
                updateFuel();
            }
        }
        updateView();
    }

    private void updateFuel() {
        ItemStack fuel = inv.getItem(1);
        if (fuel != null && Item.FIRESTRIKER.compare(fuel)) {
            Damageable meta = ((Damageable) fuel.getItemMeta());
            assert meta != null;
            burnTime = 8 - (meta.getDamage() / 7);
        }
    }

    private boolean canBurn() {
        ItemStack fuel = inv.getItem(1);
        return fuel != null && Item.FIRESTRIKER.compare(fuel) && burnTime > 0;
    }

    private void burn() {
        ItemStack fuel = inv.getItem(1);
        assert fuel != null;
        ItemMeta itemMeta = fuel.getItemMeta();
        assert itemMeta != null;
        int damage = ((Damageable) itemMeta).getDamage();
        damage += 7;
        if (damage <= 52) {
            ((Damageable) itemMeta).setDamage(damage);
            fuel.setItemMeta(itemMeta);
            inv.setItem(1, fuel);
            burnTime--;
        } else {
            inv.setItem(1, null);
            burnTime = 0;
        }
    }

    private boolean canCook() {
        ItemStack input = inv.getItem(0);
        ItemStack output = inv.getItem(2);

        if (input == null) return false; // nothing to smelt

        Material possibleOutput = getOutput(input.getType());
        if (possibleOutput == null) return false; // The input can not be smelted

        if (output == null) { //output is empty
            return true;
        }
        Material out = output.getType();
        if (output.getAmount() >= out.getMaxStackSize()) {
            return false;
        }
        return out == Material.AIR || out == possibleOutput;
    }

    private void cook() {
        ItemStack input = inv.getItem(0);
        assert input != null;
        ItemStack output = inv.getItem(2);

        Material in = input.getType();
        Material out = getOutput(in);

        if (output == null || output.getType() == Material.AIR) {
            assert out != null;
            output = new ItemStack(out);
        } else {
            output.setAmount(output.getAmount() + 1);
        }
        inv.setItem(2, output);

        int inputAmount = input.getAmount();
        if (inputAmount > 1) {
            input.setAmount(inputAmount - 1);
        } else {
            input = null;
        }
        inv.setItem(0, input);
    }

    private void updateView() {
        InventoryView view = player.getOpenInventory();
        view.setProperty(Property.COOK_TIME, cookTime);
        view.setProperty(Property.TICKS_FOR_CURRENT_SMELTING, MAX_COOK_TIME);
        view.setProperty(Property.BURN_TIME, burnTime);
        view.setProperty(Property.TICKS_FOR_CURRENT_FUEL, 8);
    }

    public void open() {
        inv.setItem(1, this.item);
        player.openInventory(inv);
    }

    public void close() {
        Bukkit.getScheduler().cancelTask(this.id);
        Location location = player.getEyeLocation().clone().add(0.0, -0.7, 0.0);
        drop(location, inv.getItem(0));
        drop(location, inv.getItem(1));
        drop(location, inv.getItem(2));
        inv.clear();
    }

    private void drop(Location location, ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            World world = location.getWorld();
            assert world != null;
            org.bukkit.entity.Item drop = world.dropItem(location, itemStack);
            drop.setVelocity(new Vector(0, 0, 0));
        }
    }

    private Material getOutput(Material material) {
        switch (material) {
            case PORKCHOP:
                return Material.COOKED_PORKCHOP;
            case BEEF:
                return Material.COOKED_BEEF;
            case CHICKEN:
                return Material.COOKED_CHICKEN;
            case SALMON:
                return Material.COOKED_SALMON;
            case COD:
                return Material.COOKED_COD;
            case POTATO:
                return Material.BAKED_POTATO;
            case MUTTON:
                return Material.COOKED_MUTTON;
            case RABBIT:
                return Material.COOKED_RABBIT;
            case SAND:
                return Material.GLASS;
            case CLAY_BALL:
                return Material.BRICK;
        }
        if (Tag.LOGS.isTagged(material)) {
            return Material.CHARCOAL;
        }
        return null;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

}
