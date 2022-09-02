package net.pixelatedstudios.SurvivalPlus.data;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import net.pixelatedstudios.SurvivalPlus.util.Math;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Holder of data for player
 * <p>You can get an instance of PlayerData from <b>{@link PlayerManager}</b></p>
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "SameParameterValue"})
public class PlayerData implements ConfigurationSerializable {

    private final Config config = Survival.getInstance().getSurvivalConfig();
    private final int max_carbs = config.MECHANICS_FOOD_MAX_CARBS;
    private final int max_proteins = config.MECHANICS_FOOD_MAX_PROTEINS;
    private final int max_salts = config.MECHANICS_FOOD_MAX_SALTS;
    private final UUID uuid;
    private final int dualWieldMsg = 0;
    private int thirst;
    private Map<String, Location> compassMap = new HashMap<>();
    // Nutrients
    private int proteins;
    private int carbs;
    private int salts;
    private double energy;
    // Dunno yet
    private boolean localChat = false;
    // Stats
    private int charge = 0;
    private int charging = 0;
    private int spin = 0;
    private int dualWield = 0;
    private int healing = 0;
    private int healTimes = 0;
    private int recurveFiring = 0;
    private int recurveCooldown = 0;

    // TODO: Investigate if this is needed
    private double temperature = 0L;

    // Scoreboard info
    private boolean score_hunger = true;
    private boolean score_thirst = true;
    private boolean score_energy = true;
    private boolean score_nutrients = true;
    // TODO: Investigate warnings
    private boolean score_temperature = true;

    public PlayerData(OfflinePlayer player, int thirst, int proteins, int carbs, int salts, double energy) {
        this(player.getUniqueId(), thirst, proteins, carbs, salts, energy);
    }

    public PlayerData(UUID uuid, int thirst, int proteins, int carbs, int salts, double energy) {
        this(uuid, thirst, proteins, carbs, salts, energy, 0.0);
    }

    public PlayerData(UUID uuid, int thirst, int proteins, int carbs, int salts, double energy, double temperature) {
        this.uuid = uuid;
        this.thirst = thirst;
        this.proteins = proteins;
        this.carbs = carbs;
        this.salts = salts;
        this.energy = energy;
        this.temperature = temperature;
    }

    /**
     * Internal deserializer for yaml config
     *
     * @param args Args from yaml config
     * @return New PlayerData loaded from config
     */
    public static PlayerData deserialize(Map<String, Object> args) {
        UUID uuid = UUID.fromString(args.get("uuid").toString());
        int thirst = ((Integer) args.get("thirst"));
        double energy = getDouble(args, "energy", 20.0);
        int proteins = ((Integer) args.get("nutrients.proteins"));
        int carbs = ((Integer) args.get("nutrients.carbs"));
        int salts = ((Integer) args.get("nutrients.salts"));

        PlayerData data = new PlayerData(uuid, thirst, proteins, carbs, salts, energy);

        boolean localChat = getBool(args, "local-chat", false);
        data.setLocalChat(localChat);

        boolean score_hunger = getBool(args, "score.hunger", true);
        boolean score_thirst = getBool(args, "score.thirst", true);
        boolean score_energy = getBool(args, "score.energy", true);
        boolean score_nutrients = getBool(args, "score.nutrients", false);
        data.setInfoDisplayed(score_hunger, score_thirst, score_energy, score_nutrients);

        if (args.containsKey("compass")) {
            //noinspection unchecked
            data.compassMap = (Map<String, Location>) args.get("compass");
        }

        return data;
    }

    // Methods for grabbing sections from map, defaults if section isn't set
    private static int getInt(Map<String, Object> args, String val, int def) {
        if (args.containsKey(val))
            return ((int) args.get(val));
        return def;
    }

    private static double getDouble(Map<String, Object> args, String val, double def) {
        if (args.containsKey(val))
            return ((double) args.get(val));
        return def;
    }

    private static boolean getBool(Map<String, Object> args, String val, boolean def) {
        if (args.containsKey(val))
            return (boolean) args.get(val);
        return def;
    }

    /**
     * Get the player from this data
     *
     * @return Player from this data
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Get the UUID of the player from this data
     *
     * @return UUID of player from this data
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get the thirst of this data
     *
     * @return Thirst of this data
     */
    public int getThirst() {
        return thirst;
    }

    /**
     * Set the thirst for this data
     *
     * @param thirst Level of thirst to set
     */
    public void setThirst(int thirst) {
        this.thirst = Math.clamp(thirst, 0, 40);
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = Math.round(temperature * 10);
    }

    /**
     * Increase the thirst for this data
     *
     * @param thirst Level of thirst to add
     */
    public void increaseThirst(int thirst) {
        this.thirst = Math.clamp(this.thirst + thirst, 0, 40);
    }

    /**
     * Get the nutrients from this data
     *
     * @param nutrient Nutrient to get
     * @return Level of the nutrient
     */
    // TODO: replace switch statement with enhanced switch statement
    public int getNutrient(Nutrient nutrient) {
        switch (nutrient) {
            case PROTEIN:
                return proteins;
            case CARBS:
                return carbs;
            case SALTS:
                return salts;
            default:
                throw new IllegalArgumentException("Unexpected value: " + nutrient);
        }
    }

    /**
     * Set the nutrients for this data
     *
     * @param nutrient Nutrient to set
     * @param value    Level to set
     */
    // TODO: replace switch statement with enhanced switch statement
    public void setNutrient(Nutrient nutrient, int value) {
        switch (nutrient) {
            case PROTEIN:
                this.proteins = Math.clamp(value, 0, this.max_proteins);
                break;
            case CARBS:
                this.carbs = Math.clamp(value, 0, this.max_carbs);
                break;
            case SALTS:
                this.salts = Math.clamp(value, 0, this.max_salts);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + nutrient);
        }
    }

    /**
     * Set all the nutrients for this data
     *
     * @param carbs    Level of carbs to set
     * @param proteins Level of proteins to set
     * @param salts    Level of salts to set
     */
    public void setNutrients(int carbs, int proteins, int salts) {
        setNutrient(Nutrient.CARBS, carbs);
        setNutrient(Nutrient.PROTEIN, proteins);
        setNutrient(Nutrient.SALTS, salts);
    }

    /**
     * Increase a nutrient for this data
     *
     * @param nutrient Nutrient to increase
     * @param value    Level of increase
     */
    // TODO: replace switch statement with enhanced switch statement
    public void increaseNutrient(Nutrient nutrient, int value) {
        switch (nutrient) {
            case PROTEIN:
                this.proteins = Math.clamp(this.proteins + value, 0, this.max_proteins);
                break;
            case CARBS:
                this.carbs = Math.clamp(this.carbs + value, 0, this.max_carbs);
                break;
            case SALTS:
                this.salts = Math.clamp(this.salts + value, 0, this.max_salts);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + nutrient);
        }
    }

    /**
     * Get the energy of this data
     *
     * @return Energy of this data
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Set the energy level for this data
     * <p>Value must be between 0.0 and 20.0</p>
     *
     * @param energy Energy level to set
     */
    public void setEnergy(double energy) {
        this.energy = Math.clamp(energy, 0.0, 20.0);
    }

    /**
     * Increase the energy level for this data
     *
     * @param energy Energy amount to increase
     */
    public void increaseEnergy(double energy) {
        setEnergy(this.energy + energy);
    }

    /**
     * Set a stat for this data
     *
     * @param stat  Stat to set
     * @param value Value of stat to set
     */
    // TODO: replace switch statement with enhanced switch statement
    public void setStat(Stat stat, int value) {
        switch (stat) {
            case CHARGE:
                this.charge = value;
                break;
            case CHARGING:
                this.charging = value;
                break;
            case SPIN:
                this.spin = value;
                break;
            case DUAL_WIELD:
                this.dualWield = value;
                break;
            case HEALING:
                this.healing = value;
                break;
            case HEAL_TIMES:
                this.healTimes = value;
                break;
            case RECURVE_FIRING:
                this.recurveFiring = value;
                break;
            case RECURVE_COOLDOWN:
                this.recurveCooldown = value;
        }
    }

    /**
     * Get a stat from this data
     *
     * @param stat Stat to retrieve
     * @return Value of stat
     */
    // TODO: replace switch statement with enhanced switch statement
    public int getStat(Stat stat) {
        switch (stat) {
            case CHARGE:
                return this.charge;
            case CHARGING:
                return this.charging;
            case SPIN:
                return this.spin;
            case DUAL_WIELD:
                return this.dualWield;
            case HEALING:
                return this.healing;
            case HEAL_TIMES:
                return this.healTimes;
            case RECURVE_FIRING:
                return this.recurveFiring;
            case RECURVE_COOLDOWN:
                return this.recurveCooldown;
            default:
                throw new IllegalArgumentException("Unexpected value: " + stat);
        }
    }

    /**
     * Check if the player is using local chat
     *
     * @return True if local chat is activated
     */
    public boolean isLocalChat() {
        return localChat;
    }

    /**
     * Set whether the player is using local chat
     *
     * @param localChat Whether the player is using local chat
     */
    public void setLocalChat(boolean localChat) {
        this.localChat = localChat;
    }

    /**
     * Get the visibility of a specific healthboard info
     *
     * @param info Healthboard info
     * @return True if this info is displayed on the player's scoreboard
     */
    // TODO: replace switch statement with enhanced switch statement
    public boolean isInfoDisplayed(Info info) {
        switch (info) {
            case HUNGER:
                return score_hunger;
            case THIRST:
                return score_thirst;
            case ENERGY:
                return score_energy;
            case NUTRIENTS:
                return score_nutrients;
            case TEMPERATURE:
                return score_temperature;
            default:
                throw new IllegalArgumentException("Unexpected value: " + info);
        }
    }

    /**
     * Set the visibility of a specific healthboard info
     *
     * @param info    Healthboard info to display
     * @param visible Whether the info should be displayed or not
     */
    // TODO: replace switch statement with enhanced switch statement
    public void setInfoDisplayed(Info info, boolean visible) {
        switch (info) {
            case HUNGER:
                this.score_hunger = visible;
                break;
            case THIRST:
                this.score_thirst = visible;
                break;
            case ENERGY:
                this.score_energy = visible;
                break;
            case NUTRIENTS:
                this.score_nutrients = visible;
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + info);
        }
    }

    /**
     * Set the visibility of all healthboard info
     * <p>This is mainly used internally for data transfers</p>
     *
     * @param hunger    Whether hunger should be displayed on the player's healthboard
     * @param thirst    Whether thirst should be displayed on the player's healthboard
     * @param energy    Whether energy should be displayed on the player's healthboard
     * @param nutrients Whether nutrients should be displayed on the player's healthboard
     */
    public void setInfoDisplayed(boolean hunger, boolean thirst, boolean energy, boolean nutrients) {
        this.score_hunger = hunger;
        this.score_thirst = thirst;
        this.score_energy = energy;
        this.score_nutrients = nutrients;
    }

    /**
     * Set a compass waypoint for this player data
     * <p>Will also update the player's compass target</p>
     *
     * @param location Location of waypoint
     */
    public void setCompassWaypoint(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        if (!config.MECHANICS_COMPASS_WAYPOINT_WORLDS) {
            this.compassMap.clear();
        }
        this.compassMap.put(world.getName(), location);
        getPlayer().setCompassTarget(location);
    }

    /**
     * Get the compass waypoint for this player data
     *
     * @param world World to grab waypoint from
     * @return Location of waypoint
     */
    @NotNull
    public Location getCompassWaypoint(World world) {
        if (this.compassMap.containsKey(world.getName())) {
            return this.compassMap.get(world.getName());
        }
        return world.getSpawnLocation();
    }

    /**
     * Internal serializer for yaml config
     *
     * @return Map for config
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("uuid", uuid.toString());
        result.put("thirst", thirst);
        result.put("energy", energy);
        result.put("nutrients.proteins", proteins);
        result.put("nutrients.carbs", carbs);
        result.put("nutrients.salts", salts);
        result.put("local-chat", localChat);
        result.put("score.hunger", score_hunger);
        result.put("score.thirst", score_thirst);
        result.put("score.energy", score_energy);
        result.put("score.nutrients", score_nutrients);
        result.put("compass", compassMap);
        return result;
    }

    /**
     * Get the hunger level of this player data
     * <p>This is a combination of food level + saturation level</p>
     *
     * @return Hunger level from this player data
     */
    public double getHunger() {
        Player player = getPlayer();
        return player.getFoodLevel() + player.getSaturation();
    }

    /**
     * Set the hunger level of this player data
     * <p>This is a combination of food level + saturation level</p>
     *
     * @param hunger Hunger level to set
     */
    public void setHunger(double hunger) {
        double sat = 0.0;
        double hun = 0.0;
        if (hunger > 40) {
            hun = 20;
            sat = 20;
        } else if (hunger > 20) {
            hun = 20;
            sat = hunger - 20;
        } else if (hunger >= 0) {
            hun = hunger;
        }
        Player player = getPlayer();
        player.setFoodLevel((int) hun);
        player.setSaturation((float) sat);
    }

    /**
     * Set a data value for this player data
     *
     * @param type  DataType to set
     * @param value Value to set
     */
    // TODO: replace switch statement with enhanced switch statement
    public void setData(DataType type, Number value) {
        switch (type) {
            case THIRST:
                setThirst(value.intValue());
                break;
            case ENERGY:
                setEnergy(value.doubleValue());
                break;
            case PROTEINS:
                setNutrient(Nutrient.PROTEIN, value.intValue());
                break;
            case CARBS:
                setNutrient(Nutrient.CARBS, value.intValue());
                break;
            case SALTS:
                setNutrient(Nutrient.SALTS, value.intValue());
                break;
            case HUNGER:
                setHunger(value.doubleValue());
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    /**
     * Get a data value from this player data
     *
     * @param type DataType to get
     * @return Value from this player data
     */
    // TODO: replace switch statement with enhanced switch statement
    public double getData(DataType type) {
        switch (type) {
            case THIRST:
                return getThirst();
            case ENERGY:
                return getEnergy();
            case PROTEINS:
                return getNutrient(Nutrient.PROTEIN);
            case CARBS:
                return getNutrient(Nutrient.CARBS);
            case SALTS:
                return getNutrient(Nutrient.SALTS);
            case HUNGER:
                return getHunger();
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public enum DataType {
        THIRST,
        ENERGY,
        PROTEINS,
        SALTS,
        CARBS,
        HUNGER;

        public static DataType getByName(String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (Exception ignore) {
                return null;
            }
        }

        public static String[] getNames() {
            String[] names = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                names[i] = values()[i].toString().toLowerCase(Locale.ROOT);
            }
            return names;
        }
    }

}
