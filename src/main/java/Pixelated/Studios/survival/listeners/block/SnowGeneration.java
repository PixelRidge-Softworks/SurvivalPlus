package Pixelated.Studios.survival.listeners.block;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import Pixelated.Studios.survival.Survival;

// TODO: This entire feature is being removed
public class SnowGeneration implements Listener {
	
	private Survival plugin;
	
	public SnowGeneration(Survival plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	private void chunkLoad(final ChunkLoadEvent event) {
		if (plugin.isSnowGenOption()) {
			if (event.isNewChunk()) {
				Bukkit.getScheduler().runTask(plugin, () -> checkChunk(event.getChunk()));
			}
		}
	}

	/**
	 * Internal usage, do not use
	 *
	 * @param chunk A chunk
	 */
	public void checkChunk(final Chunk chunk) {
		final ChunkSnapshot chunkSnap = chunk.getChunkSnapshot(true, false, false);

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				final int y = chunkSnap.getHighestBlockYAt(x, z);

				if (chunkSnap.getBlockType(x, y, z) == Material.SNOW)

					placeSnow(chunk, chunkSnap, x, y, z);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void snowForm(final BlockFormEvent event) {
		if (plugin.isSnowGenOption()) {
			if (event.getNewState().getType() != Material.SNOW)
				return;

			Bukkit.getScheduler().runTask(plugin, () -> placeSnow(event.getBlock()));
		}
	}

	private void placeSnow(final Block block) {
		final Location loc = block.getLocation();
		final Chunk chunk = block.getChunk();

		placeSnow(chunk, chunk.getChunkSnapshot(true, false, false), Math.abs((chunk.getX() * 16) - loc.getBlockX()), loc.getBlockY(), Math.abs((chunk.getZ() * 16) - loc.getBlockZ()));
	}

	private void placeSnow(final Chunk chunk, final ChunkSnapshot chunkSnap, final int x, int y, final int z) {
		if (y <= 1)
			return;

		Material type = chunkSnap.getBlockType(x, --y, z);

		if (!(Tag.LEAVES.isTagged(type)))
			return;

		Material lastType = type;

		while (true) {
			type = chunkSnap.getBlockType(x, --y, z);

			switch (type) {
				case AIR: // ignore air and snow
				case SNOW:
					break;
				case OAK_LEAVES:
				case BIRCH_LEAVES:
				case JUNGLE_LEAVES:
				case DARK_OAK_LEAVES:
				case ACACIA_LEAVES:
				case SPRUCE_LEAVES: // check leaves if they have air above them to place snow
				{
					if (lastType == Material.AIR) {
						try {
							chunk.getBlock(x, y + 1, z).setType(Material.SNOW);
						} catch (Exception ignore) {
						}
					}

					break;
				}

				// snowable blocks and the stop
				case STONE:
				case GRASS:
				case DIRT:
				case COBBLESTONE:
				case BEDROCK:
				case SAND:
				case GRAVEL:
				case GOLD_ORE:
				case IRON_ORE:
				case COAL_ORE:
				case SPONGE:
				case GLASS:
				case LAPIS_ORE:
				case LAPIS_BLOCK:
				case DISPENSER:
				case SANDSTONE:
				case NOTE_BLOCK:
//				case PISTON_BASE:
//				case PISTON_STICKY_BASE:
//				case PISTON_MOVING_PIECE:
//				case PISTON_EXTENSION:
				case GOLD_BLOCK:
				case IRON_BLOCK:
				case BRICK:
				case TNT:
				case BOOKSHELF:
				case MOSSY_COBBLESTONE:
				case OBSIDIAN:
				case SPAWNER:
				case DIAMOND_ORE:
				case DIAMOND_BLOCK:
				case CRAFTING_TABLE:
				case FURNACE:
//				case BURNING_FURNACE:
				case REDSTONE_ORE:
//				case ICE:
				case SNOW_BLOCK:
				case CLAY:
				case JUKEBOX:
				case PUMPKIN:
				case NETHERRACK:
				case SOUL_SAND:
				case GLOWSTONE:
//				case JACK_O_LANTERN:
//				case SMOOTH_BRICK:
				case MELON:
				case NETHER_BRICK:
				case END_STONE:
				case REDSTONE_LAMP:
				case EMERALD_BLOCK:
				case EMERALD_ORE: {
					setSnow(lastType, chunk, x, y, z);
					return;
				}

				default: // everything else stops
					if (Tag.LOGS.isTagged(type) ||
							Tag.WOOL.isTagged(type) ||
							Tag.PLANKS.isTagged(type) ||
							Tag.SLABS.isTagged(type)) setSnow(lastType, chunk, x, y, z);
					return;
			}

			lastType = type;
		}
	}

	private void setSnow(Material mat, Chunk chunk, final int x, int y, final int z) {

		if (mat == Material.AIR) {
			try {
				chunk.getBlock(x, y + 1, z).setType(Material.SNOW);
			} catch (Exception ignore) {
			}
		}

	}

}
