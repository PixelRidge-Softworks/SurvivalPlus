package veth.vetheon.survival.listeners.item;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.data.PlayerData;
import veth.vetheon.survival.managers.PlayerManager;
import veth.vetheon.survival.util.Utils;

// TODO: Fix me
public class CompassWaypoint implements Listener {

    private final Lang lang;
    private final PlayerManager playerManager;

    public CompassWaypoint(Survival plugin) {
        this.lang = plugin.getLang();
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    private void onItemClick(PlayerInteractEvent event) {
        if (event.hasItem()) {
            Player player = event.getPlayer();
            ItemStack mainItem = player.getInventory().getItemInMainHand();
            ItemStack offItem = player.getInventory().getItemInOffHand();
            if (mainItem.getType() == Material.COMPASS || offItem.getType() == Material.COMPASS) {

                // Prevent the event firing twice
                if (mainItem.getType() == Material.COMPASS && event.getHand() == EquipmentSlot.OFF_HAND) return;
                else if (offItem.getType() == Material.COMPASS && event.getHand() == EquipmentSlot.HAND) return;

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!player.isSneaking())
                        return; // To prevent accidentally resetting waypoint, player needs to sneak
                    assert event.getClickedBlock() != null;
                    switch (event.getClickedBlock().getType()) {
                        case HOPPER:
                        case CRAFTING_TABLE:
                        case DROPPER:
                        case DISPENSER:
                            return;
                        default:
                    }
                    if (Tag.BEDS.isTagged(event.getClickedBlock().getType())) return;
                    if (Utils.isWoodGate(event.getClickedBlock().getType())) return;
                    if (Tag.DOORS.isTagged(event.getClickedBlock().getType())) return;
                    if (Utils.isCookingBlock(event.getClickedBlock().getType())) return;
                    if (Utils.isStorageBlock(event.getClickedBlock().getType())) return;
                    if (Utils.isUtilityBlock(event.getClickedBlock().getType())) return;

                    Location loc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
                    player.sendMessage(Utils.getColoredString("&d" + lang.compass_pointed + locToString(loc)));
                    loc.add(0.5, 0.5, 0.5);
                    playerManager.setWaypoint(player, loc, true);
                }

                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Location loc = player.getLocation().getBlock().getLocation();
                    player.sendMessage(Utils.getColoredString("&d" + lang.compass_coords + locToString(loc)));
                }
            }
        }
    }

    @EventHandler
    private void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = this.playerManager.getPlayerData(player);
        player.setCompassTarget(playerData.getCompassWaypoint(player.getWorld()));
    }

    private String locToString(Location loc) {
        return " (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")";
    }

}
