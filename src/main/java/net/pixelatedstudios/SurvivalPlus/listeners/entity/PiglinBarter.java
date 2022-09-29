package net.pixelatedstudios.SurvivalPlus.listeners.entity;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.Random;

public class PiglinBarter implements Listener {

    private final boolean SLOW_ARMOR;
    private final boolean THIRST_ENABLED;
    private final boolean DROP_WATER;
    private final boolean ALT_DROPS;
    private final Random RANDOM;

    public PiglinBarter(Survival plugin) {
        Config config = plugin.getSurvivalConfig();
        this.SLOW_ARMOR = config.MECHANICS_SLOW_ARMOR;
        this.THIRST_ENABLED = config.MECHANICS_THIRST_ENABLED;
        this.DROP_WATER = config.ENTITY_MECHANICS_PIGLIN_DROP_WATER;
        this.ALT_DROPS = config.ENTITY_MECHANICS_PIGLIN_ALT_DROP;
        this.RANDOM = new Random();
    }

    @EventHandler
    private void onPiglinDrop(EntityDropItemEvent event) {
        if (event.getEntityType() != EntityType.PIGLIN) return;

        org.bukkit.entity.Item itemDrop = event.getItemDrop();
        ItemStack itemDropStack = itemDrop.getItemStack();
        Material itemDropMaterial = itemDropStack.getType();

        // If water bottle is dropped, let's change it
        if (itemDropMaterial == Material.POTION && THIRST_ENABLED && DROP_WATER) {
            PotionMeta meta = ((PotionMeta) itemDropStack.getItemMeta());
            assert meta != null;
            if (meta.getBasePotionData().getType() == PotionType.WATER) {
                if (RANDOM.nextFloat() < 0.25f) {
                    itemDrop.setItemStack(Item.PURIFIED_WATER.getItem());
                } else {
                    itemDrop.setItemStack(Item.CLEAN_WATER.getItem());
                }
                return;
            }
        }

        // If alt drops are disabled let's get outta here
        if (!ALT_DROPS) return;

        // If slow armor is enabled let's always drop custom iron boots
        if (itemDropMaterial == Material.IRON_BOOTS && SLOW_ARMOR) {
            ItemStack boots = Item.IRON_BOOTS.getItem();
            boots.addEnchantment(Enchantment.SOUL_SPEED, RANDOM.nextInt(3) + 1);
            itemDrop.setItemStack(boots);
            return;
        }

        // If anything else we have some random drops
        ItemStack altItem = switch (itemDropMaterial) {
            case LEATHER -> Item.SUSPICIOUS_MEAT.getItem();
            case NETHER_BRICK -> Item.COFFEE_BEAN.getItem(RANDOM.nextInt(4) + 1);
            case GRAVEL -> Item.FIRESTRIKER.getItem();
            case SOUL_SAND -> Item.CAMPFIRE.getItem();
            case POTION -> Item.MEDIC_KIT.getItem();
            case SPLASH_POTION -> Item.GRAPPLING_HOOK.getItem();
            case ENCHANTED_BOOK -> Item.RECURVE_CROSSBOW.getItem();
            default -> null;
        };
        if (altItem != null && RANDOM.nextFloat() > 0.5f) {
            itemDrop.setItemStack(altItem);
        }
    }

}

