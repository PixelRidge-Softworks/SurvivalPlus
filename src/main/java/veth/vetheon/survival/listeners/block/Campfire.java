package veth.vetheon.survival.listeners.block;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;

import java.util.Random;

public class Campfire implements Listener {

    private Survival plugin;

    public Campfire(Survival plugin) {
        this.plugin = plugin;
    }

    // When placing a campfire, turn it off (Requiring a player to light it manually)
    @EventHandler
    private void onPlaceCampfire(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() != Material.CAMPFIRE) return;
        if (ItemManager.compare(e.getItemInHand(), Item.CAMPFIRE)) {
            Lightable camp = ((Lightable) e.getBlock().getBlockData());
            camp.setLit(false);
            e.getBlock().setBlockData(camp);

        } else {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            e.setCancelled(true);
        }
    }

    // Hit an unlit campfire with a stick to light it
    @EventHandler
    private void lightFire(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() == Material.CAMPFIRE) {
            if (e.getItem() != null && e.getItem().getType() == Material.STICK) {
                Block block = e.getClickedBlock();
                Lightable camp = ((Lightable) block.getBlockData());
                if (camp.isLit()) return;
                e.setCancelled(true);
                int i = new Random().nextInt(20);
                if (i == 10) {
                    camp.setLit(true);
                    block.setBlockData(camp);
                    ItemStack tool = e.getItem();
                    tool.setAmount(tool.getAmount() - 1);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GENERIC_BURN, 1, 1), 1);

                }
            } else if (e.getItem() != null && e.getItem().getType() == Material.POTION) {
                if (!e.getPlayer().isSneaking()) return;
                Lightable camp = ((Lightable) e.getClickedBlock().getBlockData());
                if (!camp.isLit()) return;
                camp.setLit(false);
                e.getClickedBlock().setBlockData(camp);
                Player p = e.getPlayer();
                if (e.getHand() == EquipmentSlot.HAND) p.getInventory().setItemInMainHand(new ItemStack(Material.GLASS_BOTTLE));
                if (e.getHand() == EquipmentSlot.OFF_HAND) p.getInventory().setItemInOffHand(new ItemStack(Material.GLASS_BOTTLE));
                p.playSound(e.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);

            }
        }
    }

    // Randomly put out the fire when cooking food
    @EventHandler
    private void fireFinishedCooking(BlockCookEvent e) {
        if (e.getBlock().getType() != Material.CAMPFIRE) return;
        int i = new Random().nextInt(8);

        if (i == 5) {
            Block block = e.getBlock();
            Lightable camp = ((Lightable) block.getBlockData());
            camp.setLit(false);
            block.setBlockData(camp);
            block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        }
    }

}
