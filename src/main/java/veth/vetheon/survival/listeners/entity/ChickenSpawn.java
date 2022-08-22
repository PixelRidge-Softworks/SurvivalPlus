package veth.vetheon.survival.listeners.entity;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.item.Item;

import java.util.Random;

public class ChickenSpawn implements Listener {

    private final Random rand = new Random();
    private final NamespacedKey key;
    private final int maxEggs;
    private final boolean alwaysBaby;
    private final int babyTicks;

    public ChickenSpawn(Survival plugin) {
        Config config = plugin.getSurvivalConfig();
        this.key = new NamespacedKey(plugin, "fromBreeding");
        this.maxEggs = config.ENTITY_MECHANICS_CHICKEN_BREEDING_MAX_EGGS;
        this.alwaysBaby = config.ENTITY_MECHANICS_CHICKEN_BREEDING_ALWAYS_BABY;
        this.babyTicks = config.ENTITY_MECHANICS_CHICKEN_BREEDING_BABY_TICKS;
    }

    @EventHandler
    private void onChickenSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() == EntityType.CHICKEN) {
            SpawnReason reason = e.getSpawnReason();
            if (reason == SpawnReason.BREEDING) {
                e.setCancelled(true);
                Location loc = e.getLocation();
                World world = loc.getWorld();
                assert world != null;
                world.dropItem(loc, getEgg());
                world.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            } else if (reason == SpawnReason.EGG) {
                Chicken chicken = ((Chicken) e.getEntity());
                if (alwaysBaby) {
                    chicken.setBaby();
                    chicken.setAge(-babyTicks);
                } else if (!chicken.isAdult()) {
                    chicken.setAge(-babyTicks);
                }
            }
        }
    }

    @EventHandler
    private void onEggThrown(PlayerEggThrowEvent e) {
        if (isFromBreeding(e.getEgg())) {
            e.setHatching(true);
            e.setNumHatches((byte) 1);
        }
    }

    private ItemStack getEgg() {
        int ran = maxEggs > 1 ? rand.nextInt(maxEggs) + 1 : 1;
        return Item.BREEDING_EGG.getItem(ran);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isFromBreeding(Egg egg) {
        ItemStack itemStack = egg.getItem();
        ItemMeta meta = itemStack.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.BYTE)) {
            // Old egg method (changed on sept 4/2020)
            // Will keep for a while incase players have old eggs
            return container.get(key, PersistentDataType.BYTE) == (byte) 1;
            // TODO: Investigate if this statement can be simplified
        } else if (Item.BREEDING_EGG.compare(itemStack)) {
            return true;
        }
        return false;
    }

}

