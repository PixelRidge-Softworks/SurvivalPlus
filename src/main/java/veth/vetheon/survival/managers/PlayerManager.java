package veth.vetheon.survival.managers;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.config.PlayerDataConfig;
import veth.vetheon.survival.data.Nutrient;
import veth.vetheon.survival.data.PlayerData;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.util.Utils;

import java.util.*;

/**
 * Manager for players
 * <p>Get an instance of this class from <b>{@link Survival#getPlayerManager()}</b></p>
 */
public class PlayerManager implements Listener {

    private final String url;
    private final Lang lang;
    private final Survival plugin;
    private final PlayerDataConfig playerDataConfig;
    private final int THIRST;
    private final int HUNGER;
    private final double ENERGY;
    private final int PROTEIN;
    private final int CARBS;
    private final int SALTS;

    // Store all the active PlayerData
    private final Map<UUID, PlayerData> playerDataMap;

    public PlayerManager(Survival plugin, Map<UUID, PlayerData> playerDataMap) {
        this.plugin = plugin;
        this.playerDataMap = playerDataMap;
        this.lang = plugin.getLang();
        this.url = plugin.getSurvivalConfig().RESOURCE_PACK_URL;
        this.playerDataConfig = plugin.getPlayerDataConfig();
        Config config = plugin.getSurvivalConfig();
        THIRST = config.MECHANICS_THIRST_START_AMOUNT;
        HUNGER = config.MECHANICS_HUNGER_START_AMOUNT;
        ENERGY = config.MECHANICS_ENERGY_START;
        PROTEIN = config.MECHANICS_FOOD_START_PROTEINS;
        CARBS = config.MECHANICS_FOOD_START_CARBS;
        SALTS = config.MECHANICS_FOOD_START_SALTS;
        loadPlayerData();
    }

    /**
     * Get PlayerData for a player
     *
     * @param player Player to get data for
     * @return PlayerData for player
     */
    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    /**
     * Get a collection of all PlayerData
     *
     * @return Collection of all PlayerData
     */
    @SuppressWarnings("unused")
    public Collection<PlayerData> getAllPlayerData() {
        return playerDataMap.values();
    }

    /**
     * Create player data for a new player
     *
     * @param player Player to create data for
     * @return Newly created player data
     */
    public PlayerData createNewPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        setHunger(player, HUNGER);

        PlayerData playerData = new PlayerData(uuid, THIRST, PROTEIN, CARBS, SALTS, ENERGY);
        playerDataMap.put(uuid, playerData);
        savePlayerData(playerData);
        return playerData;
    }

    private void setHunger(Player player, int value) {
        value = Math.min(value, 40);
        int hunger = Math.min(value, 20);
        int saturation = value > 20 ? value - 20 : 0;
        player.setFoodLevel(hunger);
        player.setSaturation(saturation);
    }

    /**
     * Save PlayerData to file
     *
     * @param data PlayerData to save
     */
    private void savePlayerData(PlayerData data) {
        playerDataConfig.savePlayerDataToFile(data);
    }

    /**
     * Load PlayerData from file into map
     *
     * @param player Player to load data for
     * @return Loaded player data
     */
    public PlayerData loadPlayerData(Player player) {
        PlayerData playerData = playerDataConfig.getPlayerDataFromFile(player);
        playerDataMap.put(player.getUniqueId(), playerData);
        return playerData;
    }

    /**
     * Save/Unload player data
     * <p>This will mainly be used internally for when a player leaves the server,
     * their data will be saved to file then removed from the PlayerData map</p>
     *
     * @param player Player to save/unload data for
     */
    public void unloadPlayerData(Player player) {
        PlayerData playerData = getPlayerData(player);
        playerDataConfig.savePlayerDataToFile(playerData);
        playerDataMap.remove(player.getUniqueId());
    }

    /**
     * Set the waypoint of a player's compass to their location
     *
     * @param player   The player to set a waypoint for
     * @param particle If particles should show at the location a waypoint is set
     */
    @SuppressWarnings("unused")
    public void setWaypoint(Player player, boolean particle) {
        setWaypoint(player, player.getLocation(), particle);
    }

    /**
     * Set the waypoint of a player's compass
     *
     * @param player   The player to set a waypoint for
     * @param location The location of the waypoint
     * @param particle If the particles should show at the location a waypoint is set
     */
    public void setWaypoint(Player player, Location location, boolean particle) {
        PlayerData playerData = getPlayerData(player);
        playerData.setCompassWaypoint(location);
        if (particle)
            Utils.spawnParticle(location, Particle.CLOUD, 25, 0.5, 0.5, 0.5, player);
        savePlayerData(playerData);
    }

    /**
     * Apply SurvivalPlus' resource pack to a player
     *
     * @param player The player to apply the resource pack to
     * @param delay  A delay in ticks
     */
    public void applyResourcePack(Player player, int delay) {
        if (url != null) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                try {
                    player.setResourcePack(url);
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("ResourcePackURL is null or URL is too long! Plugin disabled.");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    return;
                }
                plugin.getUsingPlayers().add(player);
            }, delay);
        }
    }

    public Location lookAt(Location loc, Location lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0)
                loc.setYaw((float) (1.5 * Math.PI));
            else
                loc.setYaw((float) (0.5 * Math.PI));

            loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0)
            loc.setYaw((float) Math.PI);

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }

    public List<String> ShowThirst(Player player) {
    	if (player == null)
    	{
    	return null;
    	}
        StringBuilder thirstBar = new StringBuilder();
        PlayerData data = getPlayerData(player);
        int thirst = data.getThirst();

        for (int i = 0; i < thirst; i++) {
            thirstBar.append("|");
        }
        for (int i = thirst; i < 20; i++) {
            thirstBar.append(".");
        }

        if (thirst >= 40)
            thirstBar.insert(0, ChatColor.GREEN);
        else if (thirst <= 6)
            thirstBar.insert(0, ChatColor.RED);
        else
            thirstBar.insert(0, ChatColor.AQUA);

        return Arrays.asList(ChatColor.AQUA + lang.thirst, (thirstBar.length() <= 22 ? thirstBar.toString() : thirstBar.substring(0, 22)),
                thirstBar.substring(0, 2) + (thirstBar.length() > 22 ? thirstBar.substring(22) : "") + ChatColor.RESET + ChatColor.RESET);
    }

    public List<String> ShowHunger(Player player) {
    	if (player == null)
    	{
    	return null;
    	}
        int hunger = player.getFoodLevel();
        int saturation = Math.round(player.getSaturation());
        StringBuilder hungerBar = new StringBuilder();
        StringBuilder saturationBar = new StringBuilder(ChatColor.YELLOW + "");
        for (int i = 0; i < hunger; i++) {
            hungerBar.append("|");
        }
        for (int i = hunger; i < 20; i++) {
            hungerBar.append(".");
        }
        for (int i = 0; i < saturation; i++) {
            saturationBar.append("|");
        }

        if (hunger >= 20)
            hungerBar.insert(0, ChatColor.GREEN);
        else if (hunger <= 6)
            hungerBar.insert(0, ChatColor.RED);
        else
            hungerBar.insert(0, ChatColor.GOLD);

        return Arrays.asList(ChatColor.GOLD + lang.hunger, hungerBar.toString() + ChatColor.RESET, saturationBar.toString());
    }

    public List<String> ShowNutrients(Player player) {
    	if (player == null)
    	{
    	return null;
    	}
        List<String> nutrients = new ArrayList<>();
        PlayerData data = getPlayerData(player);

        int carbon = data.getNutrient(Nutrient.CARBS);
        int protein = data.getNutrient(Nutrient.PROTEIN);
        int salts = data.getNutrient(Nutrient.SALTS);

        String showCarbon = Integer.toString(carbon);
        if (carbon >= 480)
            showCarbon = ChatColor.GREEN + showCarbon;
        else
            showCarbon = ChatColor.RED + showCarbon;
        nutrients.add(showCarbon + " " + ChatColor.DARK_GREEN + lang.carbohydrates);

        String showProtein = Integer.toString(protein);
        if (protein >= 120)
            showProtein = ChatColor.GREEN + showProtein;
        else
            showProtein = ChatColor.RED + showProtein;
        nutrients.add(showProtein + " " + ChatColor.DARK_RED + lang.protein);

        String showSalts = Integer.toString(salts);
        if (salts >= 180)
            showSalts = ChatColor.GREEN + showSalts;
        else
            showSalts = ChatColor.RED + showSalts;
        nutrients.add(showSalts + " " + ChatColor.BLUE + lang.vitamins);

        return nutrients;
    }

    public List<String> showEnergy(Player player) {
    	if (player == null) 
    	{
    		return null;
    	}
        PlayerData playerData = getPlayerData(player);
        double energy = Math.floor(playerData.getEnergy());
        StringBuilder energyBar = new StringBuilder();
        for (int i = 0; i < energy; i++) {
            energyBar.append("|");
        }
        for (int i = ((int) energy); i < 20; i++) {
            energyBar.append(".");
        }
        if (energy >= 16) {
            energyBar.insert(0, ChatColor.GREEN);
        } else if (energy <= 3) {
            energyBar.insert(0, ChatColor.RED);
        } else {
            energyBar.insert(0, ChatColor.GOLD);
        }
        return Arrays.asList(Utils.getColoredString(lang.energy), energyBar.toString());
    }

    /**
     * Check if player is holding arrows in their offhand
     *
     * @param player The player to check
     * @return Whether or not the player has arrows in their offhand
     */
    public boolean isArrowOffHand(Player player) {
        Material mainHand = player.getInventory().getItemInMainHand().getType();
        Material offHand = player.getInventory().getItemInOffHand().getType();
        if (mainHand == Material.CROSSBOW)
            return offHand == Material.ARROW || offHand == Material.SPECTRAL_ARROW
                    || offHand == Material.TIPPED_ARROW || offHand == Material.FIREWORK_ROCKET;
        return offHand == Material.ARROW || offHand == Material.SPECTRAL_ARROW || offHand == Material.TIPPED_ARROW;
    }

    @SuppressWarnings("ConstantConditions")
    private void loadPlayerData() {
        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        Scoreboard scoreboard = plugin.getMainBoard();

        // Convert previous player data
        if (playerDataConfig.needsConversion()) {
            int c = 0;
            if (scoreboard.getObjective("Thirst") != null) {
                Utils.log("&bConverting player data!");
                long time = System.currentTimeMillis();

                for (OfflinePlayer player : players) {
                    UUID uuid = player.getUniqueId();
                    assert player.getName() != null;
                    int thirst = scoreboard.getObjective("Thirst").getScore(player.getName()).getScore();
                    int proteins = scoreboard.getObjective("Protein").getScore(player.getName()).getScore();
                    int carbs = scoreboard.getObjective("Carbs").getScore(player.getName()).getScore();
                    int salts = scoreboard.getObjective("Salts").getScore(player.getName()).getScore();

                    boolean s_hunger = scoreboard.getObjective("BoardHunger").getScore(player.getName()).getScore() == 0;
                    boolean s_thirst = scoreboard.getObjective("BoardThirst").getScore(player.getName()).getScore() == 0;
                    boolean s_energy = scoreboard.getObjective("BoardEnergy").getScore(player.getName()).getScore() == 0;
                    boolean s_nutrients = scoreboard.getObjective("BoardNutrients").getScore(player.getName()).getScore() == 0;

                    if (thirst > 0 || proteins > 0 || carbs > 0 || salts > 0) {
                        PlayerData data = new PlayerData(uuid, thirst, proteins, carbs, salts, 20.0);
                        data.setInfoDisplayed(s_hunger, s_thirst, s_energy, s_nutrients);
                        savePlayerData(data);
                        c++;
                    }
                }
                Utils.log("Converted players: &b" + c);
                Utils.log("&aPlayer data conversion completed in " + (System.currentTimeMillis() - time) + " milliseconds!");
            }
            playerDataConfig.createConvertedFile(c);
        }
    }

}
