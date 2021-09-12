package veth.vetheon.survival.managers;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager for Merchant Recipes
 */
public class MerchantManager {

    private final Config config;

    public MerchantManager(Survival plugin) {
        this.config = plugin.getSurvivalConfig();
    }

    /**
     * Update a merchants recipes
     * <p>Replaces existing MerchantRecipes with ones that use custom {@link Item}</p>
     *
     * @param entity Merchant to update
     */
    public void updateRecipes(Entity entity) {
        if (entity instanceof Merchant) {
            Merchant merchant = ((Merchant) entity);

            for (int i = 0; i < merchant.getRecipes().size(); i++) {
                MerchantRecipe merchantRecipe = merchant.getRecipe(i);
                Material result = merchantRecipe.getResult().getType();
                Recipe recipe = Recipe.getByMaterial(result);
                if (recipe != null && canUpdate(result)) {
                    merchant.setRecipe(i, recipe.updateRecipe(merchantRecipe));
                }
            }
        }
    }

    private boolean canUpdate(Material material) {
        switch (material) {
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
                return this.config.MECHANICS_REINFORCED_ARMOR;
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
                return this.config.MECHANICS_SLOW_ARMOR;
            case STONE_HOE:
                return this.config.SURVIVAL_SICKLE_STONE;
            case DIAMOND_HOE:
                return this.config.SURVIVAL_SICKLE_DIAMOND;
        }
        return false;
    }

    /**
     * Merchant recipes overrides
     * <p>These will take vanilla recipes and replace them with custom {@link Item}s</p>
     */
    public enum Recipe {
        IRON_HELMET(Material.IRON_HELMET, Item.IRON_HELMET),
        IRON_CHESTPLATE(Material.IRON_CHESTPLATE, Item.IRON_CHESTPLATE),
        IRON_LEGGINGS(Material.IRON_LEGGINGS, Item.IRON_LEGGINGS),
        IRON_BOOTS(Material.IRON_BOOTS, Item.IRON_BOOTS),
        DIAMOND_HELMET(Material.DIAMOND_HELMET, Item.DIAMOND_HELMET),
        DIAMOND_CHESTPLATE(Material.DIAMOND_CHESTPLATE, Item.DIAMOND_CHESTPLATE),
        DIAMOND_LEGGINGS(Material.DIAMOND_LEGGINGS, Item.DIAMOND_LEGGINGS),
        DIAMOND_BOOTS(Material.DIAMOND_BOOTS, Item.DIAMOND_BOOTS),
        REINFORCED_LEATHER_HELMET(Material.CHAINMAIL_HELMET, Item.REINFORCED_LEATHER_HELMET),
        REINFORCED_LEATHER_TUNIC(Material.CHAINMAIL_CHESTPLATE, Item.REINFORCED_LEATHER_TUNIC),
        REINFORCED_LEATHER_TROUSERS(Material.CHAINMAIL_LEGGINGS, Item.REINFORCED_LEATHER_TROUSERS),
        REINFORCED_LEATHER_BOOTS(Material.CHAINMAIL_BOOTS, Item.REINFORCED_LEATHER_BOOTS),
        STONE_SICKLE(Material.STONE_HOE, Item.STONE_SICKLE),
        DIAMOND_SICKLE(Material.DIAMOND_HOE, Item.DIAMOND_SICKLE);

        private final Material material;
        private final Item item;
        private static final Map<Material, Recipe> recipeByMaterialMap;

        static {
            recipeByMaterialMap = new HashMap<>();
            for (Recipe recipe : values()) {
                recipeByMaterialMap.put(recipe.material, recipe);
            }
        }

        Recipe(Material material, Item item) {
            this.material = material;
            this.item = item;
        }

        /**
         * Get an updated MerchantRecipe based on an existing MerchantRecipe
         *
         * @param oldRecipe Old MerchantRecipe to replace
         * @return Updated MerchantRecipe using custom items
         */
        public MerchantRecipe updateRecipe(MerchantRecipe oldRecipe) {
            ItemStack old = oldRecipe.getResult().clone();

            ItemManager.applyAttribute(old, this.item);
            MerchantRecipe recipe = new MerchantRecipe(old, oldRecipe.getUses(), oldRecipe.getMaxUses(),
                    oldRecipe.hasExperienceReward(), oldRecipe.getVillagerExperience(),
                    oldRecipe.getPriceMultiplier());
            recipe.setIngredients(oldRecipe.getIngredients());
            return recipe;
        }

        /**
         * Get a Recipe by material
         *
         * @param material Material to get recipe from
         * @return Recipe based on material
         */
        public static Recipe getByMaterial(Material material) {
            if (recipeByMaterialMap.containsKey(material)) {
                return recipeByMaterialMap.get(material);
            }
            return null;
        }

    }

}

