package veth.vetheon.survival.listeners.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;

import java.util.ArrayList;
import java.util.List;

// TODO: Add remainder of stair and sign block types
public class Chairs implements Listener {

	private final Survival plugin;
	private final Config config;

	public Chairs(Survival plugin) {
		this.plugin = plugin;
		this.config = plugin.getSurvivalConfig();
	}

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();

			assert block != null;
			if (plugin.getChairBlocks().contains(block.getType())) {
				Player player = event.getPlayer();
                Stairs stairs = (Stairs) block.getBlockData();
				int chairwidth = 1;

				// Check if block beneath chair is solid.
				if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR)
					return;

				// Check if player is sitting.
				if (!player.isSneaking() && player.getVehicle() != null) {
					player.getVehicle().remove();
					return;
				}

				// Check for distance distance between player and chair.
				if (player.getLocation().distance(block.getLocation().add(0.5, 0, 0.5)) > 2)
					return;

				// Check for signs.
				boolean sign1 = false;
				boolean sign2 = false;

				if (stairs.getFacing() == BlockFace.NORTH || stairs.getFacing() == BlockFace.SOUTH) {
					sign1 = checkSign(block, BlockFace.EAST);
					sign2 = checkSign(block, BlockFace.WEST);
				} else if (stairs.getFacing() == BlockFace.EAST || stairs.getFacing() == BlockFace.WEST) {
					sign1 = checkSign(block, BlockFace.NORTH);
					sign2 = checkSign(block, BlockFace.SOUTH);
				}

				if (!(sign1 && sign2))
					return;

				// Check for maximal chair width.
				if (stairs.getFacing() == BlockFace.NORTH || stairs.getFacing() == BlockFace.SOUTH) {
					chairwidth += getChairWidth(block, BlockFace.EAST);
					chairwidth += getChairWidth(block, BlockFace.WEST);
				} else if (stairs.getFacing() == BlockFace.EAST || stairs.getFacing() == BlockFace.WEST) {
					chairwidth += getChairWidth(block, BlockFace.NORTH);
					chairwidth += getChairWidth(block, BlockFace.SOUTH);
				}

				if (chairwidth > config.MECHANICS_CHAIRS_MAX_WIDTH)
					return;

				// Sit-down process.
				if (player.getVehicle() != null)
					player.getVehicle().remove();

				ArmorStand drop = dropSeat(block, stairs);
				List<ArmorStand> drops = checkChair(drop);

				if (drops != null) {
					drop.remove();
					return;
				}

				// Save the location of player before sitting in a chair
				Location pastLoc = player.getLocation();
				// Rotate the player's view to the descending side of the block.
				Location plocation = player.getLocation();

				switch (stairs.getFacing()) {
					case SOUTH:
						plocation.setYaw(180);
						break;
					case WEST:
						plocation.setYaw(270);
						break;
					case NORTH:
						plocation.setYaw(0);
						break;
					case EAST:
						plocation.setYaw(90);
					default:
				}

				player.teleport(plocation);

				// Changing the drop material is only necessary for the item merge feature of CB++
				// The client won't update the material, though.
				drop.addPassenger(player);

				// Cancel BlockPlaceEvent Result, if player is rightclicking with a block in his hand.
				event.setUseInteractedBlock(Result.DENY);

				//A schedule that removes the ArmorStand once the player leaves from the chair
				final ArmorStand chair = drop;

				Runnable run = new Runnable() {
					public void run() {
						if (chair.getPassengers().isEmpty()) {
							chair.remove();
							player.teleport(pastLoc);
						}
						else
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 10L);
					}
				};

				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run, -1);
			}
		}
	}

	// TODO: Remove deprecations from this class and replace with the new Paper Adventure Library
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) return;
		if (plugin.getChairBlocks().contains(event.getBlock().getType())) {
			ArmorStand drop = dropSeat(event.getBlock(), ((Stairs) event.getBlock().getBlockData()));

			for (Entity e : drop.getNearbyEntities(0.5, 0.5, 0.5)) {
				if (e instanceof ArmorStand && e.getCustomName() != null && e.getCustomName().equals("Chair"))
					e.remove();
			}

			drop.remove();
		}
	}

	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event) {
		Entity vehicle = event.getPlayer().getVehicle();

		// Let players stand up when leaving the server.
		if (vehicle instanceof ArmorStand && vehicle.getCustomName() != null && vehicle.getCustomName().equals("Chair"))
			vehicle.remove();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onHit(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		Entity hitTarget = event.getEntity();
		if (hitTarget instanceof ArmorStand && hitTarget.getCustomName() != null && hitTarget.getCustomName().equals("Chair"))
			// Chair entity is immune to damage.
			event.setCancelled(true);
		else if (hitTarget instanceof Player && hitTarget.getVehicle() != null) {
			// Let players stand up if receiving damage.
			Entity vehicle = hitTarget.getVehicle();
			if (vehicle instanceof ArmorStand && vehicle.getCustomName() != null && vehicle.getCustomName().equals("Chair"))
				vehicle.remove();
		}
	}

	private ArmorStand dropSeat(Block chair, Stairs stairs) {
		Location location = chair.getLocation().add(0.5, (-0.7 - 0.5), 0.5);

		switch (stairs.getFacing()) {
            case SOUTH:
				location.setYaw(180);
				break;
			case WEST:
				location.setYaw(270);
				break;
			case NORTH:
				location.setYaw(0);
				break;
			case EAST:
				location.setYaw(90);
			default:
		}

		assert location.getWorld() != null;
		ArmorStand drop = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		drop.setCustomName("Chair");
		drop.setVelocity(new Vector(0, 0, 0));
		drop.setGravity(false);
		drop.setVisible(false);

		return drop;
	}

	private List<ArmorStand> checkChair(ArmorStand drop) {
		List<ArmorStand> drops = new ArrayList<>();

		// Check for already existing chair items.
		for (Entity e : drop.getNearbyEntities(0.5, 0.5, 0.5)) {
			if (e instanceof ArmorStand && e.getCustomName() != null && e.getCustomName().equals("Chair")) {
				if (e.getPassengers().isEmpty())
					e.remove();
				else
					drops.add(drop);
			}
		}

		if (!drops.isEmpty())
			return drops;

		return null;
	}

	private int getChairWidth(Block block, BlockFace face) {
		int width = 0;

		// Go through the blocks next to the clicked block and check if there are any further stairs.
		for (int i = 1; i <= config.MECHANICS_CHAIRS_MAX_WIDTH; i++) {
			Block relative = block.getRelative(face, i);

			if (plugin.getChairBlocks().contains(relative.getType()) && ((Stairs) relative.getBlockData()).getFacing() == ((Stairs) block.getBlockData()).getFacing())
				width++;
			else
				break;
		}

		return width;
	}

	// TODO: Replace Switch Statement with a enhanced 'switch'.
	private boolean checkSign(Block block, BlockFace face) {
		// Go through the blocks next to the clicked block and check if are signs on the end.
		for (int i = 1; true; i++) {
			Block relative = block.getRelative(face, i);
			if (!(plugin.getChairBlocks().contains(relative.getType())) || (block.getBlockData() instanceof Stairs && ((Stairs) relative.getBlockData()).getFacing() != ((Stairs) block.getBlockData()).getFacing())) {
				if (Tag.SIGNS.isTagged(relative.getType())) return true;
				switch (relative.getType()) {
					case ITEM_FRAME:
					case PAINTING:
					case ACACIA_TRAPDOOR:
					case BIRCH_TRAPDOOR:
					case JUNGLE_TRAPDOOR:
					case OAK_TRAPDOOR:
					case SPRUCE_TRAPDOOR:
					case DARK_OAK_TRAPDOOR:
					case IRON_TRAPDOOR:
						return true;
					default:
						return false;
				}
			}
		}
	}

}
