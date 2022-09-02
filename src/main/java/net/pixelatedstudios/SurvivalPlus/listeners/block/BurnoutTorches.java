package net.pixelatedstudios.SurvivalPlus.listeners.block;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.BlockManager;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class BurnoutTorches implements Listener {

    // TODO Finish this

    private final boolean RELIGHTABLE;
    private final boolean PERSISTENT_TORCHES;
    private final boolean DROP_TORCH;

    private final BlockManager torchManager;

    public BurnoutTorches(Survival plugin) {
        this.torchManager = plugin.getBlockManager();
        this.RELIGHTABLE = plugin.getSurvivalConfig().MECHANICS_BURNOUT_TORCH_RELIGHT;
        this.PERSISTENT_TORCHES = plugin.getSurvivalConfig().MECHANICS_BURNOUT_TORCH_PERSIST;
        this.DROP_TORCH = plugin.getSurvivalConfig().MECHANICS_BURNOUT_TORCH_DROP;
    }

    @EventHandler
    private void onBlockUpdate(BlockPhysicsEvent e) {
        Block block = e.getBlock();
        if (block.getType() == Material.REDSTONE_TORCH || block.getType() == Material.REDSTONE_WALL_TORCH) {
            if (torchManager.isNonPersistent(block) && !((Lightable) block.getBlockData()).isLit()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onRelight(PlayerInteractEvent e) {
        if (!RELIGHTABLE) return;
        Player player = e.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = e.getClickedBlock();
        if (block == null || (block.getType() != Material.REDSTONE_TORCH && block.getType() != Material.REDSTONE_WALL_TORCH))
            return;
        if (!torchManager.isNonPersistent(block)) return;
        if (tool.getType() != Material.FLINT_AND_STEEL && !ItemManager.compare(tool, Item.FIRESTRIKER)) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        e.setCancelled(true);
        if (block.getType() == Material.REDSTONE_WALL_TORCH) {
            BlockFace face = ((Directional) block.getBlockData()).getFacing();
            block.setType(Material.WALL_TORCH);
            Directional dir = ((Directional) block.getBlockData());
            dir.setFacing(face);
            block.setBlockData(dir);
        } else {
            block.setType(Material.TORCH);
        }
        Random rand = new Random();
        Location loc = block.getLocation();
        assert loc.getWorld() != null;
        loc.getWorld().playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);

        if ((Utils.getDurability(tool) + 1) < tool.getType().getMaxDurability())
            Utils.setDurability(tool, Utils.getDurability(tool) + 1);
        else {
            player.getInventory().setItemInMainHand(null);
            assert player.getLocation().getWorld() != null;
            player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
        }
        torchManager.setNonPersistent(block);
        torchManager.burnoutTorch(block);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlaceTorch(BlockPlaceEvent e) {
        if (e.isCancelled()) { // If another plugin cancels these, lets get outta here
            return;
        }
        Block block = e.getBlock();
        ItemStack mainHand = e.getItemInHand();
        GameMode mode = e.getPlayer().getGameMode();
        if (mode != GameMode.SURVIVAL && mode != GameMode.ADVENTURE) {
            return;
        }
        if (block.getType() == Material.TORCH || block.getType() == Material.WALL_TORCH) {
            if (!ItemManager.compare(mainHand, Item.PERSISTENT_TORCH)) {
                torchManager.burnoutTorch(block);
                torchManager.setNonPersistent(block);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onBreakTorch(BlockBreakEvent e) {
        if (e.isCancelled()) { // If another plugin cancels these, lets get outta here
            return;
        }
        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation();
        GameMode mode = e.getPlayer().getGameMode();
        assert loc.getWorld() != null;
        /*
        if (block.getType() == Material.REDSTONE_WALL_TORCH || block.getType() == Material.REDSTONE_TORCH) {
            if (((Lightable) block.getBlockData()).isLit()) return;
            e.setDropItems(false);
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.STICK));
            torchManager.unsetNonPersistent(block);
        } else if (block.getType() == Material.TORCH || block.getType() == Material.WALL_TORCH) {
            if (PERSISTENT_TORCHES && !torchManager.isNonPersistent(block)) {
                e.setDropItems(false);
                loc.getWorld().dropItemNaturally(loc, ItemManager.get(Items.PERSISTENT_TORCH));
            } else {
                torchManager.unsetNonPersistent(block);
                if (!DROP_TORCH) {
                    e.setDropItems(false);
                    loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.STICK));
                }
            }
        } else {
            for (BlockFace blockFace : BlockFace.values()) {
                Block relative = block.getRelative(blockFace);
                if (torchManager.isNonPersistent(relative)) {
                    relative.setType(Material.AIR);
                    dropTorch(relative);
                }
            }
        }
         */
        switch (block.getType()) {
            case TORCH:
            case WALL_TORCH:
            case REDSTONE_TORCH:
            case REDSTONE_WALL_TORCH:
                if (mode != GameMode.SURVIVAL && mode != GameMode.ADVENTURE) {
                    if (torchManager.isNonPersistent(block)) {
                        torchManager.unsetNonPersistent(block);
                    }
                    return;
                }
                if (dropTorch(block)) {
                    e.setDropItems(false);
                }
                return;
            default:
                for (BlockFace blockFace : BlockFace.values()) {
                    Block relative = block.getRelative(blockFace);
                    if (torchManager.isNonPersistent(relative)) {
                        if (mode != GameMode.SURVIVAL && mode != GameMode.ADVENTURE) {
                            torchManager.unsetNonPersistent(relative);
                            continue;
                        }
                        if (dropTorch(relative)) {
                            relative.setType(Material.AIR);
                        }
                    }
                }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void pistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()) {
            return;
        }
        for (Block block : event.getBlocks()) {
            if (dropTorch(block)) {
                block.setType(Material.AIR);
            }
        }
    }

    private boolean dropTorch(Block block) {
        Location loc = block.getLocation();
        World world = loc.getWorld();
        if (world == null) return false;
        Material mat = block.getType();
        if (mat == Material.TORCH || mat == Material.WALL_TORCH) {
            if (PERSISTENT_TORCHES && !torchManager.isNonPersistent(block)) {
                world.dropItemNaturally(loc, Item.PERSISTENT_TORCH.getItem());
            } else if (DROP_TORCH) {
                return false;
            } else {
                world.dropItemNaturally(loc, new ItemStack(Material.STICK));
            }
        } else if (mat == Material.REDSTONE_TORCH || mat == Material.REDSTONE_WALL_TORCH) {
            if (torchManager.isNonPersistent(block)) {
                world.dropItemNaturally(loc, new ItemStack(Material.STICK));
            } else {
                return false;
            }
        } else {
            return false;
        }
        if (torchManager.isNonPersistent(block)) {
            torchManager.unsetNonPersistent(block);
        }
        return true;
    }

}

