package veth.vetheon.survival.listeners.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.item.items.FireStriker;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.util.Utils;

import java.util.Random;

public class FirestrikerClick implements Listener {

    private final Lang lang;

    public FirestrikerClick(Survival plugin) {
        this.lang = plugin.getLang();
    }

    @EventHandler
    private void onItemClick(PlayerInteractEvent event) {
        if (event.hasItem()) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            ItemStack tool = event.getItem();
            Action action = event.getAction();
            if (clickedBlock == null || tool == null) return;

            Material clickedBlockType = clickedBlock.getType();
            Material toolType = tool.getType();
            if (ItemManager.compare(tool, Item.FIRESTRIKER)) {
                if (player.isSneaking()) {
                    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                        Random rand = new Random();
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ITEM_SHOVEL_FLATTEN, 1.0F, rand.nextFloat() * 0.4F + 0.8F);

                        event.setCancelled(true);
                        FireStriker fireStriker = new FireStriker(player, tool.clone());
                        fireStriker.open();
                        tool.setAmount(0);
                        player.updateInventory();
                    }
                } else {
                    if (action == Action.RIGHT_CLICK_BLOCK) {
                        switch (clickedBlockType) {
                            case ENCHANTING_TABLE:
                            case ANVIL:
                            case BREWING_STAND:
                            case SPRUCE_DOOR:
                            case BIRCH_DOOR:
                            case OAK_DOOR:
                            case JUNGLE_DOOR:
                            case ACACIA_DOOR:
                            case DARK_OAK_DOOR:
                            case IRON_DOOR:
                            case TRAPPED_CHEST:
                            case CHEST:
                            case NOTE_BLOCK:
                            case IRON_TRAPDOOR:
                            case FURNACE:
                            case HOPPER:
                            case CRAFTING_TABLE:
                            case DROPPER:
                            case DISPENSER:
                            case REDSTONE_WALL_TORCH:
                            case REDSTONE_TORCH:
                                return;
                            default:
                        }
                        if (Tag.BEDS.isTagged(clickedBlockType)) return;
                        if (Utils.isWoodGate(clickedBlockType)) return;
                        if (Tag.TRAPDOORS.isTagged(clickedBlockType)) return;
                        if (clickedBlockType == Material.CAMPFIRE) {
                            Lightable camp = ((Lightable) clickedBlock.getBlockData());
                            if (camp.isLit()) return;
                            camp.setLit(true);
                            clickedBlock.setBlockData(camp);
                        }
                        // Cancel turning grass block into grass path
                        if (clickedBlockType == Material.GRASS_BLOCK) {
                            event.setCancelled(true);
                        }
                        Location loc = clickedBlock.getRelative(event.getBlockFace()).getLocation();
                        if (ignite(player, loc)) {
                            Random rand = new Random();
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.ITEM_SHOVEL_FLATTEN, 1.0F, rand.nextFloat() * 0.4F + 0.8F);

                            ItemMeta meta = tool.getItemMeta();
                            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 7);
                            tool.setItemMeta(meta);
                            if (((Damageable) tool.getItemMeta()).getDamage() >= 56) {
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                                if (player.getInventory().getItemInMainHand().getType() == toolType)
                                    player.getInventory().setItemInMainHand(null);
                                else if (player.getInventory().getItemInOffHand().getType() == toolType)
                                    player.getInventory().setItemInOffHand(null);
                            }
                            player.updateInventory();
                        }
                    }
                }
            }
        }
    }

    private boolean ignite(Player igniter, Location loc) {
        Random rand = new Random();

        loc.add(0.5, 0.5, 0.5);

        BlockIgniteEvent igniteEvent = new BlockIgniteEvent(loc.getBlock(),
                IgniteCause.FLINT_AND_STEEL, igniter);
        Bukkit.getServer().getPluginManager().callEvent(igniteEvent);
        if (igniteEvent.isCancelled()) {
            return false;
        }

        BlockState blockState = loc.getBlock().getState();

        BlockPlaceEvent placeEvent = new BlockPlaceEvent(loc.getBlock(),
                blockState, loc.getBlock(), igniter.getInventory().getItemInMainHand(), igniter, true, EquipmentSlot.HAND);
        Bukkit.getServer().getPluginManager().callEvent(placeEvent);

        if (placeEvent.isCancelled() || !placeEvent.canBuild()) {
            placeEvent.getBlockPlaced().getState().setType(Material.AIR);
            return false;
        }


        loc.getWorld().playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
        loc.getBlock().setType(Material.FIRE);

        return true;
    }

    @EventHandler
    private void onCloseInventory(InventoryCloseEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Utils.getColoredString(lang.firestriker))) {
            Inventory inv = event.getInventory();
            if (inv.getHolder() instanceof FireStriker) {
                ((FireStriker) inv.getHolder()).close();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onAttack(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity && event.getCause() == DamageCause.ENTITY_ATTACK) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (ItemManager.compare(item, Item.FIRESTRIKER)) {
                ItemMeta meta = item.getItemMeta();
                assert meta != null;
                ((Damageable) meta).setDamage(((Damageable) meta).getDamage() - 2);
                item.setItemMeta(meta);
            }
        }
    }

}
