package veth.vetheon.survival.tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.item.Item;

public class WeatherTask extends BukkitRunnable {

    private final double baseSpeed;
    private final double rainSpeed;
    private final double stormSpeed;
    private final double snowSpeed;
    private final double snowstormSpeed;

    public WeatherTask(Survival plugin) {
        Config config = plugin.getSurvivalConfig();
        this.baseSpeed = config.MECHANICS_WEATHER_SPEED_BASE;
        this.rainSpeed = config.MECHANICS_WEATHER_SPEED_RAIN;
        this.stormSpeed = config.MECHANICS_WEATHER_SPEED_STORM;
        this.snowSpeed = config.MECHANICS_WEATHER_SPEED_SNOW;
        this.snowstormSpeed = config.MECHANICS_WEATHER_SPEED_SNOWSTORM;
        this.runTaskTimer(plugin, 20, 15);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            handleWeather(player);
        }
    }

    private void handleWeather(Player player) {
        World world = player.getWorld();
        GameMode mode = player.getGameMode();
        if (world.getEnvironment() == Environment.NORMAL && (mode == GameMode.SURVIVAL || mode == GameMode.ADVENTURE)) {
            if (isInSnowstorm(player) && !hasSnowBoots(player)) {
                setWalkSpeed(player, snowstormSpeed);
            } else if (isOnSnow(player) && !hasSnowBoots(player)) {
                setWalkSpeed(player, snowSpeed);
            } else if (isInStorm(player) && !hasRainBoots(player)) {
                setWalkSpeed(player, stormSpeed);
            } else if (isInRain(player) && !hasRainBoots(player)) {
                setWalkSpeed(player, rainSpeed);
            } else {
                setWalkSpeed(player, baseSpeed);
            }
        } else {
            setWalkSpeed(player, baseSpeed);
        }
    }

    private boolean isOnSnow(Player player) {
        Block block = player.getLocation().getBlock();
        Block blockBelow = block.getRelative(BlockFace.DOWN);
        Material at = block.getType();
        Material down = blockBelow.getType();
        Material down2 = blockBelow.getRelative(BlockFace.DOWN).getType();
        if (at == Material.SNOW) {
            return true;
        } else if (at == Material.AIR) {
            return down == Material.SNOW || down == Material.SNOW_BLOCK || down2 == Material.SNOW || down2 == Material.SNOW_BLOCK;
        }
        return false;
    }

    private boolean isInSnowstorm(Player player) {
        World world = player.getWorld();
        double temp = player.getLocation().getBlock().getTemperature();

        return world.hasStorm() && temp < 0.15 && isAtHighest(player);
    }

    private boolean isInRain(Player player) {
        World world = player.getWorld();
        double temp = player.getLocation().getBlock().getTemperature();

        // is raining (0.15 0.95 for rain)
        if (world.hasStorm() && temp >= 0.15 && temp <= 0.95) {
            // sky is above
            return isAtHighest(player);
        }
        return false;
    }

    private boolean isInStorm(Player player) {
        return isInRain(player) && player.getWorld().isThundering();
    }

    private boolean isAtHighest(Player player) {
        Location location = player.getLocation();
        World world = player.getWorld();
        return location.getY() > world.getHighestBlockAt(location).getY();
    }

    private void setWalkSpeed(Player player, double speed) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attribute != null) {
            attribute.setBaseValue(speed);
        }
    }

    // TODO: Investigate warning
    private boolean hasRainBoots(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        return boots != null && Item.RAIN_BOOTS.compare(boots);
    }

    // TODO: Investigate warning
    private boolean hasSnowBoots(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        return boots != null && Item.SNOW_BOOTS.compare(boots);
    }

}

