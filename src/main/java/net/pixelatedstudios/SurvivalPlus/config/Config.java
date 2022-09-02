package net.pixelatedstudios.SurvivalPlus.config;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Config {

    private final Survival plugin;
    private final String prefix;
    public String LANG;
    public String RESOURCE_PACK_URL;
    public boolean RESOURCE_PACK_ENABLED;
    public boolean RESOURCE_PACK_NOTIFY;
    public int LOCAL_CHAT_DISTANCE;
    public boolean NO_POS;
    public boolean WELCOME_GUIDE_ENABLED;
    public boolean WELCOME_GUIDE_NEW_PLAYERS;
    public int WELCOME_GUIDE_DELAY;
    // SURVIVAL
    public boolean SURVIVAL_ENABLED;
    public boolean SURVIVAL_LIMITED_CRAFTING;
    public boolean SURVIVAL_UNLOCK_ALL_RECIPES;
    public boolean SURVIVAL_REMOVE_WOOD_TOOLS;
    public boolean SURVIVAL_TORCH;
    public boolean SURVIVAL_UPDATE_MERCHANT_TRADES;
    public boolean BREAK_ONLY_WITH_SICKLE;
    public boolean BREAK_ONLY_WITH_SHOVEL;
    public boolean BREAK_ONLY_WITH_AXE;
    public boolean BREAK_ONLY_WITH_PICKAXE;
    public boolean BREAK_ONLY_WITH_SHEARS;
    public boolean PLACE_ONLY_WITH_HAMMER;
    public boolean SURVIVAL_SICKLE_FLINT;
    public boolean SURVIVAL_SICKLE_STONE;
    public boolean SURVIVAL_SICKLE_IRON;
    public boolean SURVIVAL_SICKLE_DIAMOND;
    public double DROP_RATE_STICK;
    public double DROP_RATE_FLINT;
    // MECHANICS
    public boolean MECHANICS_SHARED_WORKBENCH;
    public boolean MECHANICS_PREVENT_NIGHT_SKIP;
    // ENERGY
    public boolean MECHANICS_ENERGY_ENABLED;
    public double MECHANICS_ENERGY_START;
    public double MECHANICS_ENERGY_RESPAWN;
    public boolean MECHANICS_ENERGY_WARNING;
    public double MECHANICS_ENERGY_DRAIN_RATE;
    public double MECHANICS_ENERGY_DRAIN_COLD_RATE;
    public double MECHANICS_ENERGY_REFRESH_RATE_BED;
    public double MECHANICS_ENERGY_REFRESH_RATE_CHAIR;
    public double MECHANICS_ENERGY_EXHAUSTION;
    public boolean MECHANICS_ENERGY_COFFEE_ENABLED;
    public boolean MECHANICS_ENERGY_ABSORPTION;
    public boolean MECHANICS_ENERGY_HASTE;
    public boolean MECHANICS_SLOW_ARMOR;
    public boolean MECHANICS_REINFORCED_ARMOR;
    public boolean MECHANICS_BOW;
    public boolean MECHANICS_RECURVED_BOW;
    public boolean MECHANICS_GRAPPLING_HOOK;
    public boolean MECHANICS_MEDIC_KIT;
    public boolean MECHANICS_REDUCED_IRON_NUGGET;
    public boolean MECHANICS_REDUCED_GOLD_NUGGET;
    public boolean MECHANICS_STATUS_SCOREBOARD;
    public int MECHANICS_ALERT_INTERVAL;
    public boolean MECHANICS_RAW_MEAT_HUNGER;
    public boolean MECHANICS_EMPTY_POTION;
    public boolean MECHANICS_POISON_POTATO;
    public boolean MECHANICS_COOKIE_BOOST;
    public boolean MECHANICS_BEET_STRENGTH;
    public boolean MECHANICS_FOOD_DIVERSITY_ENABLED;
    public int MECHANICS_FOOD_MAX_PROTEINS;
    public int MECHANICS_FOOD_MAX_SALTS;
    public int MECHANICS_FOOD_MAX_CARBS;
    public int MECHANICS_FOOD_START_PROTEINS;
    public int MECHANICS_FOOD_START_SALTS;
    public int MECHANICS_FOOD_START_CARBS;
    public int MECHANICS_FOOD_RESPAWN_PROTEINS;
    public int MECHANICS_FOOD_RESPAWN_SALTS;
    public int MECHANICS_FOOD_RESPAWN_CARBS;
    public int MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_EASY;
    public int MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_MEDIUM;
    public int MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_HARD;
    public int MECHANICS_FOOD_EFFECTS_SALTS_EX_AMP;
    public String MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_EFFECT;
    public int MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_AMP;
    public int MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_DURATION;
    public String MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_EFFECT;
    public int MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_AMP;
    public int MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_DURATION;
    public int MECHANICS_FOOD_EFFECTS_PROTEIN_EX_AMP;
    public String MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_EFFECT;
    public int MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_AMP;
    public int MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_DURATION;
    public String MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_EFFECT;
    public int MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_AMP;
    public int MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_DURATION;
    // THIRST
    public boolean MECHANICS_THIRST_ENABLED;
    public int MECHANICS_THIRST_START_AMOUNT;
    public int MECHANICS_THIRST_RESPAWN_AMOUNT;
    public boolean MECHANICS_THIRST_PURIFY_WATER;
    public boolean MECHANICS_THIRST_MELT_SNOW;
    public double MECHANICS_THIRST_DRAIN_RATE;
    public int MECHANICS_THIRST_DRAIN_HEAT;
    public int MECHANICS_THIRST_DRAIN_NETHER;
    public double MECHANICS_THIRST_DAMAGE_RATE;
    public int MECHANICS_THIRST_REP_BEET_SOUP;
    public int MECHANICS_THIRST_REP_MELON_SLICE;
    public int MECHANICS_THIRST_REP_MUSH_STEW;
    public int MECHANICS_THIRST_REP_WATER_BOWL;
    public int MECHANICS_THIRST_REP_DIRTY_WATER;
    public int MECHANICS_THIRST_REP_CLEAN_WATER;
    public int MECHANICS_THIRST_REP_PURE_WATER;
    public int MECHANICS_THIRST_REP_COFFEE;
    public int MECHANICS_THIRST_REP_COLD_MILK;
    public int MECHANICS_THIRST_REP_HOT_MILK;
    public int MECHANICS_THIRST_REP_MILK_BUCKET;
    public int MECHANICS_THIRST_REP_WATER;
    public int MECHANICS_THIRST_REP_HONEY_BOTTLE;
    public int MECHANICS_THIRST_REP_OTHER_WATER;
    public int MECHANICS_HUNGER_START_AMOUNT;
    public int MECHANICS_HUNGER_RESPAWN_AMOUNT;
    public boolean MECHANICS_COMPASS_WAYPOINT;
    public boolean MECHANICS_COMPASS_WAYPOINT_WORLDS;
    public boolean MECHANICS_CLOWN_FISH;
    public boolean MECHANICS_FERMENTED_SKIN;
    public boolean MECHANICS_LIVING_SLIME;
    public boolean MECHANICS_SNOWBALL_REVAMP;
    public boolean MECHANICS_SNOW_GEN_REVAMP;
    public boolean MECHANICS_FARMING_PRODUCTS_COOKIE;
    public boolean MECHANICS_FARMING_PRODUCTS_BREAD;
    public boolean MECHANICS_CHAIRS_ENABLED;
    public int MECHANICS_CHAIRS_MAX_WIDTH;
    public List<String> MECHANICS_CHAIRS_BLOCKS;
    public boolean MECHANICS_BURNOUT_TORCH_ENABLED;
    public int MECHANICS_BURNOUT_TORCH_TIME;
    public boolean MECHANICS_BURNOUT_TORCH_RELIGHT;
    public boolean MECHANICS_BURNOUT_TORCH_DROP;
    public boolean MECHANICS_BURNOUT_TORCH_PERSIST;
    public boolean MECHANICS_WEATHER_ENABLED;
    public double MECHANICS_WEATHER_SPEED_BASE;
    public double MECHANICS_WEATHER_SPEED_RAIN;
    public double MECHANICS_WEATHER_SPEED_STORM;
    public double MECHANICS_WEATHER_SPEED_SNOW;
    public double MECHANICS_WEATHER_SPEED_SNOWSTORM;
    // ITEM MECHANICS
    public int ITEM_FIRESTRIKER_COOK_TIME;
    // ENTITY MECHANICS
    public boolean ENTITY_MECHANICS_PIGMEN_CHEST_ENABLED;
    public int ENTITY_MECHANICS_PIGMEN_CHEST_RADIUS;
    public double ENTITY_MECHANICS_PIGMEN_CHEST_SPEED;
    public boolean ENTITY_MECHANICS_BEEKEEPER_SUIT_ENABLED;
    public boolean ENTITY_MECHANICS_SUSPICIOUS_MEAT_ENABLED;
    public int ENTITY_MECHANICS_SUSPICIOUS_MEAT_CHANCE;
    public boolean ENTITY_MECHANICS_CHICKEN_BREEDING_ENABLED;
    public int ENTITY_MECHANICS_CHICKEN_BREEDING_MAX_EGGS;
    public boolean ENTITY_MECHANICS_CHICKEN_BREEDING_ALWAYS_BABY;
    public int ENTITY_MECHANICS_CHICKEN_BREEDING_BABY_TICKS;
    public boolean ENTITY_MECHANICS_PIGLIN_DROP_WATER;
    public boolean ENTITY_MECHANICS_PIGLIN_ALT_DROP;
    // RECIPES
    public boolean RECIPES_SADDLE;
    public boolean RECIPES_NAME_TAG;
    public boolean RECIPES_PACKED_ICE;
    public boolean RECIPES_LEATHER_BARD;
    public boolean RECIPES_IRON_BARD;
    public boolean RECIPES_GOLD_BARD;
    public boolean RECIPES_DIAMOND_BARD;
    public boolean RECIPES_CLAY_BRICK;
    public boolean RECIPES_QUARTZ_BLOCK;
    public boolean RECIPES_WOOL_STRING;
    public boolean RECIPES_WEB_STRING;
    public boolean RECIPES_ICE;
    public boolean RECIPES_CLAY;
    public boolean RECIPES_DIORITE;
    public boolean RECIPES_GRANITE;
    public boolean RECIPES_ANDESITE;
    public boolean RECIPES_GRAVEL;
    public boolean RECIPES_SLIMEBALL;
    public boolean RECIPES_COBWEB;
    public boolean RECIPES_SAPLING_STICK;
    public boolean RECIPES_FISHING_ROD;
    public boolean RECIPES_FURNACE;
    public boolean RECIPES_WORKBENCH;
    public boolean RECIPES_HATCHET;
    public boolean RECIPES_MATTOCK;
    public boolean RECIPES_SHIV;
    public boolean RECIPES_HAMMER;
    public boolean RECIPES_FIRESTRIKER;
    public boolean RECIPES_CHEST;
    public boolean RECIPES_FLINT;
    public boolean RECIPES_UNLITCAMFIRE;
    // LEGENDARY TOOLS
    public boolean LEGENDARY_VALKYRIE;
    public boolean LEGENDARY_QUARTZPICKAXE;
    public boolean LEGENDARY_OBSIDIAN_MACE;
    public boolean LEGENDARY_GIANTBLADE;
    public boolean LEGENDARY_BLAZESWORD;
    public boolean LEGENDARY_NOTCH_APPLE;
    public boolean LEGENDARY_GOLDARMORBUFF;
    // HIDDEN CONFIG
    public int RECIPE_DELAY;
    private FileConfiguration settings;
    private File configFile;

    public Config(Survival plugin) {
        this.plugin = plugin;
        this.prefix = "&7[&3Survival&bPlus&7] "; //temp prefix used before lang.yml loads
        loadDefaultSettings();
    }

    private void loadDefaultSettings() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
            settings = YamlConfiguration.loadConfiguration(configFile);
            Utils.sendColoredConsoleMsg(prefix + "new config.yml created");
        } else {
            settings = YamlConfiguration.loadConfiguration(configFile);
        }
        matchConfig(settings, configFile);
        loadSettings();
        Utils.sendColoredConsoleMsg(prefix + "&7config.yml &aloaded");
    }

    // Used to update config
    @SuppressWarnings("ConstantConditions")
    private void matchConfig(FileConfiguration config, File file) {
        try {
            boolean hasUpdated = false;
            InputStream is = plugin.getResource(file.getName());
            assert is != null;
            InputStreamReader isr = new InputStreamReader(is);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(isr);
            for (String key : defConfig.getConfigurationSection("").getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, defConfig.get(key));
                    hasUpdated = true;
                }
            }
            for (String key : config.getConfigurationSection("").getKeys(true)) {
                if (!defConfig.contains(key) && !key.equalsIgnoreCase("recipe-delay")) {
                    config.set(key, null);
                    hasUpdated = true;
                }
            }
            if (hasUpdated)
                config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public FileConfiguration getSettings() {
        return this.settings;
    }

    private void loadSettings() {

        this.LANG = settings.getString("Language");

        // MULTIWORLD
        this.RESOURCE_PACK_URL = settings.getString("MultiWorld.ResourcePackURL");
        this.RESOURCE_PACK_ENABLED = settings.getBoolean("MultiWorld.EnableResourcePack");
        this.RESOURCE_PACK_NOTIFY = settings.getBoolean("MultiWorld.NotifyMessage");

        this.LOCAL_CHAT_DISTANCE = settings.getInt("LocalChatDist");
        this.NO_POS = settings.getBoolean("NoPos");

        // WELCOME GUIDE
        this.WELCOME_GUIDE_ENABLED = settings.getBoolean("WelcomeGuide.Enabled");
        this.WELCOME_GUIDE_NEW_PLAYERS = settings.getBoolean("WelcomeGuide.NewPlayersOnly");
        this.WELCOME_GUIDE_DELAY = settings.getInt("WelcomeGuide.Delay");

        // SURVIVAL
        this.SURVIVAL_ENABLED = settings.getBoolean("Survival.Enabled");
        this.SURVIVAL_LIMITED_CRAFTING = settings.getBoolean("Survival.LimitedCrafting");
        this.SURVIVAL_UNLOCK_ALL_RECIPES = settings.getBoolean("Survival.Unlock-all-recipes-on-join");
        this.SURVIVAL_REMOVE_WOOD_TOOLS = settings.getBoolean("Survival.Remove-Wooden-Tools");
        this.SURVIVAL_TORCH = settings.getBoolean("Survival.Torch");
        this.SURVIVAL_UPDATE_MERCHANT_TRADES = settings.getBoolean("Survival.UpdateMerchantTrades");

        this.BREAK_ONLY_WITH_SICKLE = settings.getBoolean("Survival.BreakOnlyWith.Sickle");
        this.BREAK_ONLY_WITH_SHOVEL = settings.getBoolean("Survival.BreakOnlyWith.Shovel");
        this.BREAK_ONLY_WITH_AXE = settings.getBoolean("Survival.BreakOnlyWith.Axe");
        this.BREAK_ONLY_WITH_PICKAXE = settings.getBoolean("Survival.BreakOnlyWith.Pickaxe");
        this.BREAK_ONLY_WITH_SHEARS = settings.getBoolean("Survival.BreakOnlyWith.Shears");
        this.PLACE_ONLY_WITH_HAMMER = settings.getBoolean("Survival.PlaceOnlyWith.Hammer");

        this.SURVIVAL_SICKLE_FLINT = settings.getBoolean("Survival.Sickles.Flint");
        this.SURVIVAL_SICKLE_STONE = settings.getBoolean("Survival.Sickles.Stone");
        this.SURVIVAL_SICKLE_IRON = settings.getBoolean("Survival.Sickles.Iron");
        this.SURVIVAL_SICKLE_DIAMOND = settings.getBoolean("Survival.Sickles.Diamond");

        this.DROP_RATE_STICK = settings.getDouble("Survival.DropRate.Stick");
        this.DROP_RATE_FLINT = settings.getDouble("Survival.DropRate.Flint");

        // MECHANICS
        this.MECHANICS_SHARED_WORKBENCH = settings.getBoolean("Mechanics.SharedWorkbench");
        this.MECHANICS_PREVENT_NIGHT_SKIP = settings.getBoolean("Mechanics.Prevent-Night-Skip");
        this.MECHANICS_ENERGY_ENABLED = settings.getBoolean("Mechanics.Energy.enabled");
        this.MECHANICS_ENERGY_START = settings.getDouble("Mechanics.Energy.start-level");
        this.MECHANICS_ENERGY_RESPAWN = settings.getDouble("Mechanics.Energy.respawn-level");
        this.MECHANICS_ENERGY_WARNING = settings.getBoolean("Mechanics.Energy.warning");
        this.MECHANICS_ENERGY_DRAIN_RATE = settings.getDouble("Mechanics.Energy.drain-rate");
        this.MECHANICS_ENERGY_DRAIN_COLD_RATE = settings.getDouble("Mechanics.Energy.cold-drain-rate");
        this.MECHANICS_ENERGY_REFRESH_RATE_BED = settings.getDouble("Mechanics.Energy.sleeping-refresh-rate");
        this.MECHANICS_ENERGY_REFRESH_RATE_CHAIR = settings.getDouble("Mechanics.Energy.chair-refresh-rate");
        this.MECHANICS_ENERGY_EXHAUSTION = settings.getDouble("Mechanics.Energy.exhaustion");
        this.MECHANICS_ENERGY_COFFEE_ENABLED = settings.getBoolean("Mechanics.Energy.coffee");
        this.MECHANICS_ENERGY_ABSORPTION = settings.getBoolean("Mechanics.Energy.absorption");
        this.MECHANICS_ENERGY_HASTE = settings.getBoolean("Mechanics.Energy.haste");

        this.MECHANICS_SLOW_ARMOR = settings.getBoolean("Mechanics.SlowArmor");
        this.MECHANICS_REINFORCED_ARMOR = settings.getBoolean("Mechanics.ReinforcedLeatherArmor");
        this.MECHANICS_BOW = settings.getBoolean("Mechanics.Bow");
        this.MECHANICS_RECURVED_BOW = settings.getBoolean("Mechanics.RecurveBow");
        this.MECHANICS_GRAPPLING_HOOK = settings.getBoolean("Mechanics.GrapplingHook");
        this.MECHANICS_MEDIC_KIT = settings.getBoolean("Mechanics.MedicalKit");
        this.MECHANICS_REDUCED_IRON_NUGGET = settings.getBoolean("Mechanics.ReducedIronNugget");
        this.MECHANICS_REDUCED_GOLD_NUGGET = settings.getBoolean("Mechanics.ReducedGoldNugget");

        this.MECHANICS_STATUS_SCOREBOARD = settings.getBoolean("Mechanics.StatusScoreboard");
        this.MECHANICS_ALERT_INTERVAL = settings.getInt("Mechanics.AlertInterval");

        this.MECHANICS_RAW_MEAT_HUNGER = settings.getBoolean("Mechanics.RawMeatHunger");
        this.MECHANICS_EMPTY_POTION = settings.getBoolean("Mechanics.EmptyPotions");
        this.MECHANICS_POISON_POTATO = settings.getBoolean("Mechanics.PoisonousPotato");
        this.MECHANICS_COOKIE_BOOST = settings.getBoolean("Mechanics.CookieHealthBoost");
        this.MECHANICS_BEET_STRENGTH = settings.getBoolean("Mechanics.BeetrootStrength");

        this.MECHANICS_FOOD_DIVERSITY_ENABLED = settings.getBoolean("Mechanics.FoodDiversity.enabled");
        this.MECHANICS_FOOD_MAX_CARBS = settings.getInt("Mechanics.FoodDiversity.max-level.carbs");
        this.MECHANICS_FOOD_MAX_SALTS = settings.getInt("Mechanics.FoodDiversity.max-level.salts");
        this.MECHANICS_FOOD_MAX_PROTEINS = settings.getInt("Mechanics.FoodDiversity.max-level.proteins");
        this.MECHANICS_FOOD_DIVERSITY_ENABLED = settings.getBoolean("Mechanics.FoodDiversity.enabled");
        this.MECHANICS_FOOD_START_CARBS = settings.getInt("Mechanics.FoodDiversity.start-level.carbs");
        this.MECHANICS_FOOD_START_SALTS = settings.getInt("Mechanics.FoodDiversity.start-level.salts");
        this.MECHANICS_FOOD_START_PROTEINS = settings.getInt("Mechanics.FoodDiversity.start-level.proteins");
        this.MECHANICS_FOOD_DIVERSITY_ENABLED = settings.getBoolean("Mechanics.FoodDiversity.enabled");
        this.MECHANICS_FOOD_RESPAWN_CARBS = settings.getInt("Mechanics.FoodDiversity.respawn-level.carbs");
        this.MECHANICS_FOOD_RESPAWN_SALTS = settings.getInt("Mechanics.FoodDiversity.respawn-level.salts");
        this.MECHANICS_FOOD_RESPAWN_PROTEINS = settings.getInt("Mechanics.FoodDiversity.respawn-level.proteins");
        this.MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_EASY = settings.getInt("Mechanics.FoodDiversity.effects.carbs.exhaustion-amplifier.easy");
        this.MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_MEDIUM = settings.getInt("Mechanics.FoodDiversity.effects.carbs.exhaustion-amplifier.normal");
        this.MECHANICS_FOOD_EFFECTS_CARBS_EX_AMP_HARD = settings.getInt("Mechanics.FoodDiversity.effects.carbs.exhaustion-amplifier.hard");
        this.MECHANICS_FOOD_EFFECTS_SALTS_EX_AMP = settings.getInt("Mechanics.FoodDiversity.effects.salts.exhaustion-amplifier");
        this.MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_EFFECT = settings.getString("Mechanics.FoodDiversity.effects.salts.status-effects.normal.effect");
        this.MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_AMP = settings.getInt("Mechanics.FoodDiversity.effects.salts.status-effects.normal.amplifier");
        this.MECHANICS_FOOD_EFFECTS_SALTS_SE_NORMAL_DURATION = settings.getInt("Mechanics.FoodDiversity.effects.salts.status-effects.normal.duration");
        this.MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_EFFECT = settings.getString("Mechanics.FoodDiversity.effects.salts.status-effects.hard.effect");
        this.MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_AMP = settings.getInt("Mechanics.FoodDiversity.effects.salts.status-effects.hard.amplifier");
        this.MECHANICS_FOOD_EFFECTS_SALTS_SE_HARD_DURATION = settings.getInt("Mechanics.FoodDiversity.effects.salts.status-effects.hard.duration");

        this.MECHANICS_FOOD_EFFECTS_PROTEIN_EX_AMP = settings.getInt("Mechanics.FoodDiversity.effects.proteins.exhaustion-amplifier");
        this.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_EFFECT = settings.getString("Mechanics.FoodDiversity.effects.proteins.status-effects.normal.effect");
        this.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_AMP = settings.getInt("Mechanics.FoodDiversity.effects.proteins.status-effects.normal.amplifier");
        this.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_NORMAL_DURATION = settings.getInt("Mechanics.FoodDiversity.effects.proteins.status-effects.normal.duration");
        this.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_EFFECT = settings.getString("Mechanics.FoodDiversity.effects.proteins.status-effects.hard.effect");
        this.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_AMP = settings.getInt("Mechanics.FoodDiversity.effects.proteins.status-effects.hard.amplifier");
        this.MECHANICS_FOOD_EFFECTS_PROTEIN_SE_HARD_DURATION = settings.getInt("Mechanics.FoodDiversity.effects.proteins.status-effects.hard.duration");

        this.MECHANICS_THIRST_ENABLED = settings.getBoolean("Mechanics.Thirst.Enabled");
        this.MECHANICS_THIRST_START_AMOUNT = settings.getInt("Mechanics.Thirst.Starting-Amount");
        this.MECHANICS_THIRST_RESPAWN_AMOUNT = settings.getInt("Mechanics.Thirst.Respawn-Amount");
        this.MECHANICS_THIRST_PURIFY_WATER = settings.getBoolean("Mechanics.Thirst.PurifyWater");
        this.MECHANICS_THIRST_MELT_SNOW = settings.getBoolean("Mechanics.Thirst.MeltSnow");
        this.MECHANICS_THIRST_DRAIN_RATE = settings.getDouble("Mechanics.Thirst.DrainRate");
        this.MECHANICS_THIRST_DRAIN_HEAT = settings.getInt("Mechanics.Thirst.HeatDrain");
        this.MECHANICS_THIRST_DRAIN_NETHER = settings.getInt("Mechanics.Thirst.NetherDrain");
        this.MECHANICS_THIRST_DAMAGE_RATE = settings.getDouble("Mechanics.Thirst.DamageRate");

        this.MECHANICS_THIRST_REP_BEET_SOUP = settings.getInt("Mechanics.Thirst.Replenish-Level.beetroot-soup");
        this.MECHANICS_THIRST_REP_MELON_SLICE = settings.getInt("Mechanics.Thirst.Replenish-Level.melon-slice");
        this.MECHANICS_THIRST_REP_MUSH_STEW = settings.getInt("Mechanics.Thirst.Replenish-Level.mushroom-stew");
        this.MECHANICS_THIRST_REP_WATER_BOWL = settings.getInt("Mechanics.Thirst.Replenish-Level.water-bowl");
        this.MECHANICS_THIRST_REP_DIRTY_WATER = settings.getInt("Mechanics.Thirst.Replenish-Level.dirty-water");
        this.MECHANICS_THIRST_REP_CLEAN_WATER = settings.getInt("Mechanics.Thirst.Replenish-Level.clean-water");
        this.MECHANICS_THIRST_REP_PURE_WATER = settings.getInt("Mechanics.Thirst.Replenish-Level.purified-water");
        this.MECHANICS_THIRST_REP_COFFEE = settings.getInt("Mechanics.Thirst.Replenish-Level.coffee");
        this.MECHANICS_THIRST_REP_COLD_MILK = settings.getInt("Mechanics.Thirst.Replenish-Level.cold-milk");
        this.MECHANICS_THIRST_REP_HOT_MILK = settings.getInt("Mechanics.Thirst.Replenish-Level.hot-milk");
        this.MECHANICS_THIRST_REP_MILK_BUCKET = settings.getInt("Mechanics.Thirst.Replenish-Level.milk-bucket");
        this.MECHANICS_THIRST_REP_WATER = settings.getInt("Mechanics.Thirst.Replenish-Level.water");
        this.MECHANICS_THIRST_REP_HONEY_BOTTLE = settings.getInt("Mechanics.Thirst.Replenish-Level.honey-bottle");
        this.MECHANICS_THIRST_REP_OTHER_WATER = settings.getInt("Mechanics.Thirst.Replenish-Level.other-water");

        this.MECHANICS_HUNGER_START_AMOUNT = settings.getInt("Mechanics.Hunger.Starting-Amount");
        this.MECHANICS_HUNGER_RESPAWN_AMOUNT = settings.getInt("Mechanics.Hunger.Respawn-Amount");

        this.MECHANICS_COMPASS_WAYPOINT = settings.getBoolean("Mechanics.CompassWaypoint.enabled");
        this.MECHANICS_COMPASS_WAYPOINT_WORLDS = settings.getBoolean("Mechanics.CompassWaypoint.per-world");
        this.MECHANICS_CLOWN_FISH = settings.getBoolean("Mechanics.Clownfish");
        this.MECHANICS_FERMENTED_SKIN = settings.getBoolean("Mechanics.FermentedSkin");
        this.MECHANICS_LIVING_SLIME = settings.getBoolean("Mechanics.LivingSlime");

        this.MECHANICS_SNOWBALL_REVAMP = settings.getBoolean("Mechanics.SnowballRevamp");
        this.MECHANICS_SNOW_GEN_REVAMP = settings.getBoolean("Mechanics.SnowGenerationRevamp");

        this.MECHANICS_FARMING_PRODUCTS_COOKIE = settings.getBoolean("Mechanics.FarmingProducts.Cookie");
        this.MECHANICS_FARMING_PRODUCTS_BREAD = settings.getBoolean("Mechanics.FarmingProducts.Bread");

        this.MECHANICS_CHAIRS_ENABLED = settings.getBoolean("Mechanics.Chairs.Enabled");
        this.MECHANICS_CHAIRS_MAX_WIDTH = settings.getInt("Mechanics.Chairs.MaxChairWidth");
        this.MECHANICS_CHAIRS_BLOCKS = settings.getStringList("Mechanics.Chairs.AllowedBlocks");

        this.MECHANICS_BURNOUT_TORCH_ENABLED = settings.getBoolean("Mechanics.BurnoutTorches.Enabled");
        this.MECHANICS_BURNOUT_TORCH_TIME = settings.getInt("Mechanics.BurnoutTorches.BurnoutTime");
        this.MECHANICS_BURNOUT_TORCH_RELIGHT = settings.getBoolean("Mechanics.BurnoutTorches.Relightable");
        this.MECHANICS_BURNOUT_TORCH_DROP = settings.getBoolean("Mechanics.BurnoutTorches.DropTorch");
        this.MECHANICS_BURNOUT_TORCH_PERSIST = settings.getBoolean("Mechanics.BurnoutTorches.PersistentTorches");

        this.MECHANICS_WEATHER_ENABLED = settings.getBoolean("Mechanics.Weather.Enabled");
        this.MECHANICS_WEATHER_SPEED_BASE = settings.getDouble("Mechanics.Weather.speed.base");
        this.MECHANICS_WEATHER_SPEED_RAIN = settings.getDouble("Mechanics.Weather.speed.rain");
        this.MECHANICS_WEATHER_SPEED_STORM = settings.getDouble("Mechanics.Weather.speed.storm");
        this.MECHANICS_WEATHER_SPEED_SNOW = settings.getDouble("Mechanics.Weather.speed.snow");
        this.MECHANICS_WEATHER_SPEED_SNOWSTORM = settings.getDouble("Mechanics.Weather.speed.snowstorm");

        // ITEM MECHANICS
        this.ITEM_FIRESTRIKER_COOK_TIME = settings.getInt("Item-Mechanics.firestriker.cook-time");

        // ENTITY MECHANICS
        this.ENTITY_MECHANICS_PIGMEN_CHEST_ENABLED = settings.getBoolean("Entity-Mechanics.pigmen-chests.enabled");
        this.ENTITY_MECHANICS_PIGMEN_CHEST_RADIUS = settings.getInt("Entity-Mechanics.pigmen-chests.distance");
        this.ENTITY_MECHANICS_PIGMEN_CHEST_SPEED = settings.getDouble("Entity-Mechanics.pigmen-chests.speed-modifier");
        this.ENTITY_MECHANICS_BEEKEEPER_SUIT_ENABLED = settings.getBoolean("Entity-Mechanics.beekeeper-suit.enabled");
        this.ENTITY_MECHANICS_SUSPICIOUS_MEAT_ENABLED = settings.getBoolean("Entity-Mechanics.suspicious-meat.enabled");
        this.ENTITY_MECHANICS_SUSPICIOUS_MEAT_CHANCE = settings.getInt("Entity-Mechanics.suspicious-meat.chance");
        this.ENTITY_MECHANICS_CHICKEN_BREEDING_ENABLED = settings.getBoolean("Entity-Mechanics.chicken-breeding.enabled");
        this.ENTITY_MECHANICS_CHICKEN_BREEDING_MAX_EGGS = settings.getInt("Entity-Mechanics.chicken-breeding.max-eggs");
        this.ENTITY_MECHANICS_CHICKEN_BREEDING_ALWAYS_BABY = settings.getBoolean("Entity-Mechanics.chicken-breeding.always-baby");
        this.ENTITY_MECHANICS_CHICKEN_BREEDING_BABY_TICKS = settings.getInt("Entity-Mechanics.chicken-breeding.baby-ticks");
        this.ENTITY_MECHANICS_PIGLIN_DROP_WATER = settings.getBoolean("Entity-Mechanics.piglin-barter.drop-purified-water");
        this.ENTITY_MECHANICS_PIGLIN_ALT_DROP = settings.getBoolean("Entity-Mechanics.piglin-barter.alternate-bartering");

        // RECIPES
        this.RECIPES_SADDLE = settings.getBoolean("Recipes.Saddle");
        this.RECIPES_NAME_TAG = settings.getBoolean("Recipes.Nametag");
        this.RECIPES_PACKED_ICE = settings.getBoolean("Recipes.PackedIce");
        this.RECIPES_LEATHER_BARD = settings.getBoolean("Recipes.LeatherBard");
        this.RECIPES_IRON_BARD = settings.getBoolean("Recipes.IronBard");
        this.RECIPES_GOLD_BARD = settings.getBoolean("Recipes.GoldBard");
        this.RECIPES_DIAMOND_BARD = settings.getBoolean("Recipes.DiamondBard");
        this.RECIPES_CLAY_BRICK = settings.getBoolean("Recipes.ClayBrick");
        this.RECIPES_QUARTZ_BLOCK = settings.getBoolean("Recipes.QuartzBlock");
        this.RECIPES_WOOL_STRING = settings.getBoolean("Recipes.WoolString");
        this.RECIPES_WEB_STRING = settings.getBoolean("Recipes.WebString");
        this.RECIPES_ICE = settings.getBoolean("Recipes.Ice");
        this.RECIPES_CLAY = settings.getBoolean("Recipes.Clay");
        this.RECIPES_DIORITE = settings.getBoolean("Recipes.Diorite");
        this.RECIPES_GRANITE = settings.getBoolean("Recipes.Granite");
        this.RECIPES_ANDESITE = settings.getBoolean("Recipes.Andesite");
        this.RECIPES_GRAVEL = settings.getBoolean("Recipes.Gravel");
        this.RECIPES_SLIMEBALL = settings.getBoolean("Recipes.Slimeball");
        this.RECIPES_COBWEB = settings.getBoolean("Recipes.Cobweb");
        this.RECIPES_SAPLING_STICK = settings.getBoolean("Recipes.SaplingToSticks");
        this.RECIPES_FISHING_ROD = settings.getBoolean("Recipes.FishingRod");
        this.RECIPES_FURNACE = settings.getBoolean("Recipes.Furnace");
        this.RECIPES_WORKBENCH = settings.getBoolean("Recipes.Workbench");
        this.RECIPES_HATCHET = settings.getBoolean("Recipes.Hatchet");
        this.RECIPES_MATTOCK = settings.getBoolean("Recipes.Mattock");
        this.RECIPES_SHIV = settings.getBoolean("Recipes.Shiv");
        this.RECIPES_HAMMER = settings.getBoolean("Recipes.Hammer");
        this.RECIPES_FIRESTRIKER = settings.getBoolean("Recipes.Firestriker");
        this.RECIPES_CHEST = settings.getBoolean("Recipes.Chest");
        this.RECIPES_FLINT = settings.getBoolean("Recipes.Flint");
        this.RECIPES_UNLITCAMFIRE = settings.getBoolean("Recipes.Unlit_Camfire");

        // LEGENDARY ITEMS
        this.LEGENDARY_VALKYRIE = settings.getBoolean("LegendaryItems.ValkyrieAxe");
        this.LEGENDARY_QUARTZPICKAXE = settings.getBoolean("LegendaryItems.QuartzPickaxe");
        this.LEGENDARY_OBSIDIAN_MACE = settings.getBoolean("LegendaryItems.ObsidianMace");
        this.LEGENDARY_GIANTBLADE = settings.getBoolean("LegendaryItems.GiantBlade");
        this.LEGENDARY_BLAZESWORD = settings.getBoolean("LegendaryItems.BlazeSword");
        this.LEGENDARY_NOTCH_APPLE = settings.getBoolean("LegendaryItems.NotchApple");
        this.LEGENDARY_GOLDARMORBUFF = settings.getBoolean("LegendaryItems.GoldArmorBuff");

        // HIDDEN CONFIG
        this.RECIPE_DELAY = settings.getInt("recipe-delay", 0);
    }

}