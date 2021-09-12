package veth.vetheon.survival.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class BlockManager {

	private final Survival plugin;
	private FileConfiguration data;
	private File data_file;
	private final Lang lang;

	private final int seconds;

	public BlockManager(Survival plugin) {
		this.plugin = plugin;
		this.lang = plugin.getLang();
		this.seconds = plugin.getSurvivalConfig().MECHANICS_BURNOUT_TORCH_TIME;
		loadDataFile(plugin.getServer().getConsoleSender());
		toBurnout();
	}

	/** Sets a torch to burn out after a preset time (in config)
	 * @param block The torch to burn out
	 */
	public void burnoutTorch(Block block) {
		burnoutTorch(block, seconds);
	}

	/**
	 * Sets a torch to burn out after x seconds
	 *
	 * @param seconds The time to wait for burnout in seconds
	 * @param block   The torch to burn out
	 */
	@SuppressWarnings("WeakerAccess")
	public void burnoutTorch(Block block, int seconds) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            BlockData data = block.getBlockData();
			if (data.getMaterial() == Material.TORCH)
				data = Material.REDSTONE_TORCH.createBlockData();
			else if (data.getMaterial() == Material.WALL_TORCH) {
				BlockFace face = ((Directional) data).getFacing();
				data = Material.REDSTONE_WALL_TORCH.createBlockData();
                ((Directional) data).setFacing(face);
			} else {
				if (isNonPersistent(block))
					unsetNonPersistent(block);
				return;
			}
            ((Lightable) data).setLit(false);
			block.setBlockData(data);
		}, 20 * seconds);
	}

	private void loadDataFile(CommandSender sender) {
		String loaded;
		data_file = new File(plugin.getDataFolder(), "data.yml");
		if (!data_file.exists()) {
			plugin.saveResource("data.yml", true);
			loaded = "&aNew data.yml created";
		} else {
			loaded = "&7data.yml &aloaded";
		}
		data = YamlConfiguration.loadConfiguration(data_file);
		Utils.sendColoredMsg(sender, lang.prefix + loaded);
	}

	/**
	 * Adds a non persistent torch to the data.yml file
	 *
	 * @param block The torch to make non persistent
	 */
	public void setNonPersistent(Block block) {
		List<String> list = data.getStringList("NonPersistent Torches");
		long time = System.currentTimeMillis();
		time = time + (1000 * seconds);
		list.add(locToString(block.getLocation()) + " time:" + time);
		data.set("NonPersistent Torches", list);
		try {
			data.save(data_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes a non persistent torch from the data.yml file
	 *
	 * @param block The torch to remove as non persistent
	 */
	public void unsetNonPersistent(Block block) {
		List<String> list = data.getStringList("NonPersistent Torches");
		for (Object string : list.toArray()) {
			if (stringMatchLoc(((String) string), block.getLocation())) {
				list.remove(string);
			}
		}
		data.set("NonPersistent Torches", list);
		try {
			data.save(data_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if a torch is non persistent
	 *
	 * @param block The torch to check
	 * @return Whether its persistent or not
	 */
	public boolean isNonPersistent(Block block) {
		return containsLoc(block.getLocation());
	}

	private String locToString(Location loc) {
		assert loc.getWorld() != null;
		return ("world:" + loc.getWorld().getName() + " x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ())
				.replace(".0", "");
	}

	private boolean stringMatchLoc(String string, Location location) {
		String[] loc = string.split(" ");
		assert location.getWorld() != null;
		String world = location.getWorld().getName();
		String x = String.valueOf(location.getX()).replace(".0", "");
		String y = String.valueOf(location.getY()).replace(".0", "");
		String z = String.valueOf(location.getZ()).replace(".0", "");
		if (loc[0].equalsIgnoreCase("world:" + world)) {
			if (loc[1].equalsIgnoreCase("x:" + x)) {
				if (loc[2].equalsIgnoreCase("y:" + y)) {
					return loc[3].equalsIgnoreCase("z:" + z);
				}
			}
		}
		return false;
	}

	private boolean containsLoc(Location loc) {
		List<String> list = data.getStringList("NonPersistent Torches");
		for (String torch : list) {
			if (stringMatchLoc(torch, loc))
				return true;
		}
		return false;
	}

	private void toBurnout() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			List<String> list = data.getStringList("NonPersistent Torches");
			for (String string : list) {
				String[] loc = string.split(" ");
				long time = Long.parseLong(loc[4].replace("time:", ""));
				if (time < System.currentTimeMillis()) {
					String world = loc[0].replace("world:", "");
					int x = Integer.parseInt(loc[1].replace("x:", ""));
					int y = Integer.parseInt(loc[2].replace("y:", ""));
					int z = Integer.parseInt(loc[3].replace("z:", ""));
					Block block = Objects.requireNonNull(Bukkit.getServer().getWorld(world)).getBlockAt(x, y, z);
					if (block.getType() == Material.TORCH || block.getType() == Material.WALL_TORCH) {
						burnoutTorch(block, 20);
					}
				}
			}
		}, 20 * 60, 20 * 60);
	}

}
