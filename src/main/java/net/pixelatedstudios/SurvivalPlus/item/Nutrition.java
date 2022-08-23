package net.pixelatedstudios.SurvivalPlus.item;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.pixelatedstudios.SurvivalPlus.Survival;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Nutritional values for foods
 */
@SuppressWarnings("unused")
public class Nutrition implements Keyed {

    static void setup() {
    }

    private static final Map<NamespacedKey, Nutrition> NUTRITION_MAP = new LinkedHashMap<>();

    // FRUITS AND VEGGIES
    public static final Nutrition POTATO = register(25, 0, 10, Material.POTATO);
    public static final Nutrition BAKED_POTATO = register(200, 0, 60, Material.BAKED_POTATO);
    public static final Nutrition POISONOUS_POTATO = register(50, 0, 8, Material.POISONOUS_POTATO);
    public static final Nutrition APPLE = register(50, 0, 70, Material.APPLE);
    public static final Nutrition GOLDEN_APPLE = register(50, 0, 70, Material.GOLDEN_APPLE);
    public static final Nutrition ENCHANTED_GOLDEN_APPLE = register(50, 0, 70, Material.ENCHANTED_GOLDEN_APPLE);
    public static final Nutrition CARROT = register(0, 0, 105, Material.CARROT);
    public static final Nutrition GOLDEN_CARROT = register(0, 0, 25, Material.GOLDEN_CARROT);
    public static final Nutrition CHORUS_FRUIT = register(25, 0, 35, Material.CHORUS_FRUIT);
    public static final Nutrition MELON_SLICE = register(25, 0, 35, Material.MELON_SLICE);
    public static final Nutrition BEETROOT = register(0, 0, 35, Material.BEETROOT);
    public static final Nutrition DRIED_KELP = register(15, 50, 50, Material.DRIED_KELP);
    public static final Nutrition SWEET_BERRIES = register(40, 0, 60, Material.SWEET_BERRIES);

    // PREPARED FOODS
    public static final Nutrition BREAD = register(300, 25, 12, Material.BREAD);
    public static final Nutrition PUMPKIN_PIE = register(300, 50, 60, Material.PUMPKIN_PIE);
    public static final Nutrition RABBIT_STEW = register(200, 225, 240, Material.RABBIT_STEW);
    public static final Nutrition MUSHROOM_STEW = register(0, 50, 200, Material.MUSHROOM_STEW);
    public static final Nutrition SUSPICIOUS_STEW = register(0, 50, 210, Material.SUSPICIOUS_STEW);
    public static final Nutrition BEETROOT_SOUP = register(0, 50, 200, Material.BEETROOT_SOUP);
    public static final Nutrition COOKIE = register(107, 11, 3, Material.COOKIE);
    public static final Nutrition CAKE = register(171, 114, 3, Material.CAKE);

    // MEATS
    public static final Nutrition BEEF = register(0, 50, 0, Material.BEEF);
    public static final Nutrition COOKED_BEEF = register(0, 200, 0, Material.COOKED_BEEF);
    public static final Nutrition CHICKEN = register(0, 50, 0, Material.CHICKEN);
    public static final Nutrition COOKED_CHICKEN = register(0, 200, 0, Material.COOKED_CHICKEN);
    public static final Nutrition MUTTON = register(0, 50, 0, Material.MUTTON);
    public static final Nutrition COOKED_MUTTON = register(0, 200, 0, Material.COOKED_MUTTON);
    public static final Nutrition PORKCHOP = register(0, 50, 0, Material.PORKCHOP);
    public static final Nutrition COOKED_PORKCHOP = register(0, 200, 0, Material.COOKED_PORKCHOP);
    public static final Nutrition RABBIT = register(0, 50, 0, Material.RABBIT);
    public static final Nutrition COOKED_RABBIT = register(0, 200, 0, Material.COOKED_RABBIT);
    public static final Nutrition COD = register(0, 75, 0, Material.COD);
    public static final Nutrition COOKED_COD = register(0, 225, 0, Material.COOKED_COD);
    public static final Nutrition SALMON = register(0, 75, 0, Material.SALMON);
    public static final Nutrition COOKED_SALMON = register(0, 225, 0, Material.COOKED_SALMON);
    public static final Nutrition PUFFERFISH = register(0, 75, 0, Material.PUFFERFISH);
    public static final Nutrition TROPICAL_FISH = register(0, 75, 0, Material.TROPICAL_FISH);

    // MISC
    public static final Nutrition SPIDER_EYE = register(0, 50, 0, Material.SPIDER_EYE);
    public static final Nutrition ROTTEN_FLESH = register(0, 25, 25, Material.ROTTEN_FLESH);
    public static final Nutrition MILK_BUCKET = register(0, 250, 0, Material.MILK_BUCKET);

    @NotNull
    private static Nutrition register(int carbs, int proteins, int vitamins, Material material) {
        String key = material.toString().toLowerCase(Locale.ROOT);
        int[] nutritions = ItemConfig.INSTANCE.getNutritionValues(key, carbs, proteins, vitamins);
        NamespacedKey namespacedKey = new NamespacedKey(Survival.getInstance(), "nutrition_" + key);
        ItemStack itemStack = new ItemStack(material);
        return register(namespacedKey, false, itemStack, nutritions[0], nutritions[1], nutritions[2]);
    }

    /**
     * Register a new Nutrition
     * <p>
     * The plugin will check if the item matches (with the exception for stack size)
     * and apply the appropriate nutrients to the player
     *
     * @param key       NamespacedKey to register
     * @param itemStack Itemstack to register
     * @param carbs     Carb value for this Nutrition
     * @param proteins  Protein value for this Nutrition
     * @param vitamins  Vitamin value for this Nutrition
     * @return Newly registered nutrition
     */
    @SuppressWarnings({"UnusedReturnValue"})
    @NotNull
    public static Nutrition register(@NotNull NamespacedKey key, @NotNull ItemStack itemStack, int carbs, int proteins, int vitamins) {
        return register(key, true, itemStack, carbs, proteins, vitamins);
    }

    @SuppressWarnings("ConstantConditions")
    private static Nutrition register(@NotNull NamespacedKey key, boolean custom, @NotNull ItemStack itemStack, int carbs, int proteins, int vitamins) {
        Preconditions.checkArgument(key != null, "Registering Nutrition, NamespacedKey cannot be null");
        Preconditions.checkArgument(itemStack != null, "Registering Nutrition '%s': ItemStack cannot be null", key);
        String nutritionFormat = "Registering Nutrition '%s': Nutrition value must be >= 0, found '%s' = '%s'";
        Preconditions.checkArgument(carbs >= 0, nutritionFormat, key, "carbs", carbs);
        Preconditions.checkArgument(proteins >= 0, nutritionFormat, key, "proteins", proteins);
        Preconditions.checkArgument(vitamins >= 0, nutritionFormat, key, "vitamins", vitamins);
        Nutrition nutrition = new Nutrition(key, custom, carbs, proteins, vitamins, itemStack);
        if (!NUTRITION_MAP.containsKey(key)) {
            NUTRITION_MAP.put(key, nutrition);
            return nutrition;
        }
        throw new IllegalArgumentException(String.format("There is already a Nutrition registered for '%s'", key));
    }

    /**
     * Unregister a Nutrition
     *
     * @param key NamespacedKey of Nutrition to unregister
     * @return True if successfully unregistered, else false
     */
    @SuppressWarnings("ConstantConditions")
    public static boolean unregister(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "Unregistering Nutrition, NamespacedKey cannot be null");
        if (!NUTRITION_MAP.containsKey(key)) {
            NUTRITION_MAP.remove(key);
            return true;
        }
        return false;
    }

    private final NamespacedKey key;
    private final boolean custom;
    private final int carbs;
    private final int proteins;
    private final int vitamins;
    private final ItemStack itemStack;
    private Item item = null;

    Nutrition(NamespacedKey key, boolean custom, int carbs, int proteins, int vitamins, ItemStack itemStack) {
        this.key = key;
        this.custom = custom;
        this.carbs = carbs;
        this.proteins = proteins;
        this.vitamins = vitamins;
        this.itemStack = itemStack;
    }

    // Will be used in the future for custom food items
    Nutrition(NamespacedKey key, boolean custom, int carbs, int proteins, int vitamins, Item item) {
        this.key = key;
        this.custom = custom;
        this.carbs = carbs;
        this.proteins = proteins;
        this.vitamins = vitamins;
        this.item = item;
        this.itemStack = item.getItem();
    }

    /**
     * Get the key for this nutritional item
     *
     * @return Key for this nutritional item
     */
    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Checks if this Nutrition is custom
     * <p>
     * Will be true if another plugin has registered
     * a custom Nutrition
     *
     * @return True if custom
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * Get the carbs for this nutritional item
     *
     * @return Carbs for this nutritional item
     */
    public int getCarbs() {
        return carbs;
    }

    /**
     * Get the proteins for this nutritional item
     *
     * @return Proteins for this nutritional item
     */
    public int getProteins() {
        return proteins;
    }

    /**
     * Get the vitamins for this nutritional item
     *
     * @return Vitamins for this nutritional item
     */
    public int getVitamins() {
        return vitamins;
    }

    /**
     * Get the ItemStack of this nutritional item
     *
     * @return ItemStack of this nutritional item
     */
    @NotNull
    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    /**
     * Get the item of this nutritional item
     *
     * @return Item of this nutritional item
     * @deprecated Currently unused
     */
    @Deprecated
    public Item getItem() {
        return item;
    }

    /**
     * Get from an ItemStack
     *
     * @param itemStack ItemStack to match
     * @return Nutrition for material (null if nutrition does not exist for this material)
     */
    @Nullable
    public static Nutrition getByItemStack(ItemStack itemStack) {
        // First check if an ItemStack matches a registered Nutrition
        for (Nutrition nutrition : NUTRITION_MAP.values()) {
            if (nutrition.itemStack.isSimilar(itemStack)) {
                return nutrition;
            }
        }
        // Fallback to default items when no registrations match
        // This is in case a player/plugin renames/modifies an item without registering,
        // then we can still give the player the base nutrients
        for (Nutrition nutrition : NUTRITION_MAP.values()) {
            if (!nutrition.custom && nutrition.itemStack.getType() == itemStack.getType()) {
                return nutrition;
            }
        }
        return null;
    }

    /**
     * Get all registered Nutritions
     *
     * @return List of all registered nutritions
     */
    public static List<Nutrition> getAllNutritions() {
        return new ArrayList<>(NUTRITION_MAP.values());
    }

}
