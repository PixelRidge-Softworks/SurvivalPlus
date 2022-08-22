package veth.vetheon.survival.util;

import com.google.common.collect.ImmutableSet;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.Metadatable;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Utils {

	// TODO: Investigate if HEX_PATTERN can be simplified
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]){6}>");
    private static final ImmutableSet<Material> CONCRETE_BLOCKS;
	private static final ImmutableSet<Material> CONCRETE_POWDER;
	private final static ImmutableSet<Material> GLAZED_TERRACOTTA;
	private final static ImmutableSet<Material> TERRACOTTA;
	private final static ImmutableSet<Material> NATURAL_ORE_BLOCK;
	private final static ImmutableSet<Material> ORE_BLOCK;
	private final static ImmutableSet<Material> COOKING_BLOCK;
	private final static ImmutableSet<Material> UTILITY_BLOCK;
	private final static ImmutableSet<Material> STORAGE_BLOCK;
	private final static ImmutableSet<Material> STONE_TYPE_BLOCK;
	private final static ImmutableSet<Material> FARMABLE;
	private final static ImmutableSet<Material> SHOVEL;
	private final static ImmutableSet<Material> REQUIRES_SHOVEL;
	private final static ImmutableSet<Material> PICKAXE;
	private final static ImmutableSet<Material> REQUIRES_PICKAXE;
	private final static ImmutableSet<Material> AXE;
	private final static ImmutableSet<Material> REQUIRES_AXE;
	private final static ImmutableSet<Material> REQUIRES_SHEARS;
	private final static ImmutableSet<Material> REQUIRES_HAMMER;

    static {
    	CONCRETE_BLOCKS = ImmutableSet.<Material>builder()
				.add(Material.CYAN_CONCRETE)
				.add(Material.BLACK_CONCRETE)
				.add(Material.BLUE_CONCRETE)
				.add(Material.BROWN_CONCRETE)
				.add(Material.GRAY_CONCRETE)
				.add(Material.GREEN_CONCRETE)
				.add(Material.LIGHT_BLUE_CONCRETE)
				.add(Material.LIGHT_GRAY_CONCRETE)
				.add(Material.LIME_CONCRETE)
				.add(Material.MAGENTA_CONCRETE)
				.add(Material.ORANGE_CONCRETE)
				.add(Material.PINK_CONCRETE)
				.add(Material.PURPLE_CONCRETE)
				.add(Material.RED_CONCRETE)
				.add(Material.WHITE_CONCRETE)
				.add(Material.YELLOW_CONCRETE)
				.build();

		CONCRETE_POWDER = ImmutableSet.<Material>builder()
				.add(Material.BLACK_CONCRETE_POWDER)
				.add(Material.BLUE_CONCRETE_POWDER)
				.add(Material.BROWN_CONCRETE_POWDER)
				.add(Material.CYAN_CONCRETE_POWDER)
				.add(Material.GRAY_CONCRETE_POWDER)
				.add(Material.GREEN_CONCRETE_POWDER)
				.add(Material.LIME_CONCRETE_POWDER)
				.add(Material.MAGENTA_CONCRETE_POWDER)
				.add(Material.ORANGE_CONCRETE_POWDER)
				.add(Material.PINK_CONCRETE_POWDER)
				.add(Material.PURPLE_CONCRETE_POWDER)
				.add(Material.RED_CONCRETE_POWDER)
				.add(Material.WHITE_CONCRETE_POWDER)
				.add(Material.YELLOW_CONCRETE_POWDER)
				.add(Material.LIGHT_BLUE_CONCRETE_POWDER)
				.add(Material.LIGHT_GRAY_CONCRETE_POWDER)
				.build();

		GLAZED_TERRACOTTA = ImmutableSet.<Material>builder()
				.add(Material.BLACK_GLAZED_TERRACOTTA)
				.add(Material.BLUE_GLAZED_TERRACOTTA)
				.add(Material.BROWN_GLAZED_TERRACOTTA)
				.add(Material.CYAN_GLAZED_TERRACOTTA)
				.add(Material.GRAY_GLAZED_TERRACOTTA)
				.add(Material.GREEN_GLAZED_TERRACOTTA)
				.add(Material.LIGHT_BLUE_GLAZED_TERRACOTTA)
				.add(Material.LIME_GLAZED_TERRACOTTA)
				.add(Material.MAGENTA_GLAZED_TERRACOTTA)
				.add(Material.ORANGE_GLAZED_TERRACOTTA)
				.add(Material.PINK_GLAZED_TERRACOTTA)
				.add(Material.PURPLE_GLAZED_TERRACOTTA)
				.add(Material.RED_GLAZED_TERRACOTTA)
				.add(Material.LIGHT_GRAY_GLAZED_TERRACOTTA)
				.add(Material.WHITE_GLAZED_TERRACOTTA)
				.add(Material.YELLOW_GLAZED_TERRACOTTA)
				.build();

		TERRACOTTA = ImmutableSet.<Material>builder()
				.add(Material.BLACK_TERRACOTTA)
				.add(Material.BLUE_TERRACOTTA)
				.add(Material.BROWN_TERRACOTTA)
				.add(Material.CYAN_TERRACOTTA)
				.add(Material.GRAY_TERRACOTTA)
				.add(Material.GREEN_TERRACOTTA)
				.add(Material.LIGHT_BLUE_TERRACOTTA)
				.add(Material.LIME_TERRACOTTA)
				.add(Material.MAGENTA_TERRACOTTA)
				.add(Material.ORANGE_TERRACOTTA)
				.add(Material.PINK_TERRACOTTA)
				.add(Material.PURPLE_TERRACOTTA)
				.add(Material.RED_TERRACOTTA)
				.add(Material.LIGHT_GRAY_TERRACOTTA)
				.add(Material.WHITE_TERRACOTTA)
				.add(Material.YELLOW_TERRACOTTA)
				.build();

        NATURAL_ORE_BLOCK = ImmutableSet.<Material>builder()
                .add(Material.COAL_ORE)
                .add(Material.DIAMOND_ORE)
                .add(Material.EMERALD_ORE)
                .add(Material.GOLD_ORE)
                .add(Material.IRON_ORE)
                .add(Material.LAPIS_ORE)
                .add(Material.NETHER_QUARTZ_ORE)
                .add(Material.REDSTONE_ORE)
                .add(Material.NETHER_GOLD_ORE)
                .add(Material.ANCIENT_DEBRIS)
                .add(Material.GILDED_BLACKSTONE)
                .build();

        ORE_BLOCK = ImmutableSet.<Material>builder()
                .add(Material.COAL_BLOCK)
                .add(Material.DIAMOND_BLOCK)
                .add(Material.EMERALD_BLOCK)
                .add(Material.GOLD_BLOCK)
                .add(Material.IRON_BLOCK)
                .add(Material.LAPIS_BLOCK)
                .add(Material.QUARTZ_BLOCK)
                .add(Material.REDSTONE_BLOCK)
                .add(Material.NETHERITE_BLOCK)
                .build();

		COOKING_BLOCK = ImmutableSet.<Material>builder()
				.add(Material.FURNACE)
				.add(Material.BLAST_FURNACE)
				.add(Material.SMOKER)
				.build();

        UTILITY_BLOCK = ImmutableSet.<Material>builder()
                .add(Material.CARTOGRAPHY_TABLE)
                .add(Material.FLETCHING_TABLE)
                .add(Material.LECTERN)
                .add(Material.LOOM)
                .add(Material.STONECUTTER)
                .add(Material.GRINDSTONE)
                .add(Material.SMITHING_TABLE)
                .add(Material.ANVIL)
                .add(Material.ENCHANTING_TABLE)
                .add(Material.JUKEBOX)
                .add(Material.NOTE_BLOCK)
                .add(Material.BREWING_STAND)
                .add(Material.CAULDRON)
                .add(Material.COMPOSTER)
                .add(Material.RESPAWN_ANCHOR)
                .add(Material.LODESTONE)
                .build();

		STORAGE_BLOCK = ImmutableSet.<Material>builder()
				.add(Material.CHEST)
				.add(Material.ENDER_CHEST)
				.add(Material.TRAPPED_CHEST)
				.add(Material.BARREL)
				.build();

        STONE_TYPE_BLOCK = ImmutableSet.<Material>builder()
                .add(Material.STONE)
                .add(Material.COBBLESTONE)
                .add(Material.MOSSY_COBBLESTONE)
                .add(Material.INFESTED_COBBLESTONE)
                .add(Material.ANDESITE)
                .add(Material.POLISHED_ANDESITE)
                .add(Material.DIORITE)
                .add(Material.POLISHED_DIORITE)
                .add(Material.GRANITE)
                .add(Material.POLISHED_GRANITE)
                .add(Material.BRICKS)
                .add(Material.NETHER_BRICKS)
                .add(Material.SANDSTONE)
                .add(Material.CHISELED_SANDSTONE)
                .add(Material.SMOOTH_SANDSTONE)
                .add(Material.CUT_SANDSTONE)
                .add(Material.RED_SANDSTONE)
                .add(Material.CHISELED_RED_SANDSTONE)
                .add(Material.CUT_RED_SANDSTONE)
                .add(Material.SMOOTH_RED_SANDSTONE)
                .add(Material.PRISMARINE)
                .add(Material.PRISMARINE_BRICKS)
                .add(Material.DARK_PRISMARINE)
                .add(Material.NETHERRACK)
                .add(Material.END_STONE)
                .add(Material.END_STONE_BRICKS)
                .add(Material.PURPUR_BLOCK)
                .add(Material.PURPUR_PILLAR)
                // Nether update blocks
                .add(Material.BASALT)
                .add(Material.POLISHED_BASALT)
                .add(Material.BLACKSTONE)
                .add(Material.POLISHED_BLACKSTONE)
                .add(Material.CHISELED_POLISHED_BLACKSTONE)
                .add(Material.CHISELED_NETHER_BRICKS)
                .add(Material.CRACKED_NETHER_BRICKS)
                .add(Material.QUARTZ_BRICKS)
                .build();

		FARMABLE = ImmutableSet.<Material>builder()
				.add(Material.MELON)
				.add(Material.MELON_STEM)
				.add(Material.PUMPKIN)
				.add(Material.PUMPKIN_STEM)
				.add(Material.CHORUS_FLOWER)
				.add(Material.CHORUS_PLANT)
				.add(Material.CARROTS)
				.add(Material.POTATOES)
				.add(Material.BEETROOTS)
				.add(Material.WHEAT)
				.add(Material.SWEET_BERRY_BUSH)
				.add(Material.COCOA)
				.build();

        SHOVEL = ImmutableSet.<Material>builder()
                .add(Material.STONE_SHOVEL)
                .add(Material.IRON_SHOVEL)
                .add(Material.DIAMOND_SHOVEL)
                .add(Material.GOLDEN_SHOVEL)
                .add(Material.WOODEN_SHOVEL)
                .add(Material.NETHERITE_SHOVEL)
                .build();

        REQUIRES_SHOVEL = ImmutableSet.<Material>builder()
                .add(Material.GRASS_BLOCK)
                .add(Material.DIRT)
                .add(Material.PODZOL)
                .add(Material.COARSE_DIRT)
                .add(Material.FARMLAND)
                .add(Material.SOUL_SAND)
                .add(Material.SAND)
                .add(Material.RED_SAND)
                .add(Material.CLAY)
                .add(Material.MYCELIUM)
                .add(Material.SNOW)
                .add(Material.SNOW_BLOCK)
                .add(Material.SOUL_SOIL)
                .build();

        PICKAXE = ImmutableSet.<Material>builder()
                .add(Material.GOLDEN_PICKAXE)
                .add(Material.WOODEN_PICKAXE)
                .add(Material.DIAMOND_PICKAXE)
                .add(Material.IRON_PICKAXE)
                .add(Material.STONE_PICKAXE)
                .add(Material.NETHERITE_PICKAXE)
                .build();

		REQUIRES_PICKAXE = ImmutableSet.<Material>builder()
				.add(Material.NETHER_BRICK_FENCE)
				.add(Material.NETHER_BRICKS)
				.add(Material.RED_NETHER_BRICKS)
				.add(Material.SPAWNER)
				.add(Material.SEA_LANTERN)
				.add(Material.GLOWSTONE)
				.add(Material.END_ROD)
				.add(Material.DISPENSER)
				.add(Material.DROPPER)
				.add(Material.OBSERVER)
				.add(Material.PISTON)
				.add(Material.PISTON_HEAD)
				.add(Material.STICKY_PISTON)
				.add(Material.MOVING_PISTON)
				.add(Material.ENCHANTING_TABLE)
				.add(Material.ANVIL)
				.add(Material.GRINDSTONE)
				.add(Material.STONECUTTER)
				.add(Material.ENDER_CHEST)
				.add(Material.HOPPER)
				.add(Material.CAULDRON)
				.add(Material.BREWING_STAND)
				.add(Material.STONE_PRESSURE_PLATE)
				.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
				.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
				.add(Material.BEACON)
				.add(Material.OBSIDIAN)
                .add(Material.IRON_TRAPDOOR)
				.build();

        AXE = ImmutableSet.<Material>builder()
                .add(Material.WOODEN_AXE)
                .add(Material.STONE_AXE)
                .add(Material.GOLDEN_AXE)
                .add(Material.IRON_AXE)
                .add(Material.DIAMOND_AXE)
                .add(Material.NETHERITE_AXE)
                .build();

		REQUIRES_AXE = ImmutableSet.<Material>builder()
				.add(Material.CHEST)
				.add(Material.TRAPPED_CHEST)
				.add(Material.BARREL)
				.add(Material.CRAFTING_TABLE)
				.add(Material.CARTOGRAPHY_TABLE)
				.add(Material.FLETCHING_TABLE)
				.add(Material.SMITHING_TABLE)
				.add(Material.LOOM)
				.add(Material.LECTERN)
				.add(Material.CAMPFIRE)
				.add(Material.COMPOSTER)
				.add(Material.BOOKSHELF)
				.add(Material.LADDER)
				.add(Material.JUKEBOX)
				.add(Material.NOTE_BLOCK)
				.add(Material.DAYLIGHT_DETECTOR)
				.add(Material.SCAFFOLDING)
				.build();

		REQUIRES_SHEARS = ImmutableSet.<Material>builder()
				.add(Material.COBWEB)
				.add(Material.TRIPWIRE)
				.add(Material.TNT)
				.add(Material.MUSHROOM_STEM)
				.build();

		REQUIRES_HAMMER = ImmutableSet.<Material>builder()
				.add(Material.BOOKSHELF)
				.add(Material.LADDER)
				.add(Material.SEA_LANTERN)
				.add(Material.GLOWSTONE)
				.add(Material.END_ROD)
				.add(Material.DISPENSER)
				.add(Material.DROPPER)
				.add(Material.HOPPER)
				.add(Material.STONE_PRESSURE_PLATE)
				.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)
				.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
				.add(Material.DAYLIGHT_DETECTOR)
				.add(Material.PISTON)
				.add(Material.STICKY_PISTON)
				.add(Material.REDSTONE_LAMP)
				.add(Material.REPEATER)
				.add(Material.COMPARATOR)
				.add(Material.TRIPWIRE_HOOK)
				.add(Material.BEACON)
				.add(Material.IRON_BARS)
				.add(Material.SCAFFOLDING)
				.build();
	}

	/** Check if a material is concrete
	 * @param material Material to check
	 * @return True if material is concrete
	 */
    public static boolean isConcrete(Material material) {
        return CONCRETE_BLOCKS.contains(material);
    }

	/** Check if a material is concrete powder
	 * @param material Material to check
	 * @return True if material is concrete powder
	 */
    public static boolean isConcretePowder(Material material) {
        return CONCRETE_POWDER.contains(material);
    }

	/** Check if a material is glazed terracotta
	 * @param material Material to check
	 * @return True if material is glazed terracotta
	 */
    public static boolean isGlazedTerracotta(Material material) {
        return GLAZED_TERRACOTTA.contains(material);
    }

	/** Check if a material is terracotta
	 * @param material Material to check
	 * @return True if material is terracotta
	 */
    public static boolean isTerracotta(Material material) {
        return TERRACOTTA.contains(material);
    }

	/** Check if a material is a natural ore block
	 * <p>ie: coal ore, diamond ore, iron ore</p>
	 * @param material Material to check
	 * @return True if material is natural ore block
	 */
    public static boolean isNaturalOreBlock(Material material) {
        return NATURAL_ORE_BLOCK.contains(material);
    }

	/** Check if a material is an ore block
	 * <p>ie: coal block, diamond block, iron block</p>
	 * @param material Material to check
	 * @return True if material is ore block
	 */
    public static boolean isOreBlock(Material material) {
        return ORE_BLOCK.contains(material);
    }

	/** Check if a material is a cooking block
	 * <p>ie: furnace, blast furnace, smoker</p>
	 * @param material Material to check
	 * @return True if material is cooking block
	 */
    public static boolean isCookingBlock(Material material) {
        return COOKING_BLOCK.contains(material);
    }

	/** Check if a material is a utility block
	 * <p>ie: cartography table, grindstone, anvil</p>
	 * @param material Material to check
	 * @return True if material is utility block
	 */
    public static boolean isUtilityBlock(Material material) {
        return UTILITY_BLOCK.contains(material);
    }

	/** Check if a material is a shulker box
	 * @param material Material to check
	 * @return True if material is shulker box
	 */
    public static boolean isShulkerBox(Material material) {
        return Tag.SHULKER_BOXES.isTagged(material);
    }

	/** Check if a material is a storage block
	 * <p>ie: chest, ender chest, barrel</p>
	 * @param material Material to check
	 * @return True if material is storage block
	 */
    public static boolean isStorageBlock(Material material) {
        return STORAGE_BLOCK.contains(material);
    }

	/** Check if a material is stone block type
	 * @param material Material to check
	 * @return True if material is stone block type
	 */
    public static boolean isStoneTypeBlock(Material material) {
        if (STONE_TYPE_BLOCK.contains(material)) return true;
        if (isNonWoodSlab(material)) return true;
        if (isNonWoodStairs(material)) return true;
        return Tag.STONE_BRICKS.isTagged(material) || Tag.WALLS.isTagged(material);
    }

	/** Check if a material is a non wood door
	 * @param material Material to check
	 * @return True if material is non wood door
	 */
    public static boolean isNonWoodDoor(Material material) {
        return (Tag.DOORS.isTagged(material) && !Tag.WOODEN_DOORS.isTagged(material));
    }

	/** Check if a material is a non wood slab
	 * @param material Material to check
	 * @return True if material is non wood slab
	 */
    public static boolean isNonWoodSlab(Material material) {
        return (Tag.SLABS.isTagged(material) && !Tag.WOODEN_SLABS.isTagged(material));
    }

	/** Check if a material is a non wood stair
	 * @param material Material to check
	 * @return True if material is non wood stair
	 */
    public static boolean isNonWoodStairs(Material material) {
        return (Tag.STAIRS.isTagged(material) && !Tag.WOODEN_STAIRS.isTagged(material));
    }

	/** Check if a material is a wood gate
	 * @param material Material to check
	 * @return True if material is wood gate
	 */
    public static boolean isWoodGate(Material material) {
        return Tag.FENCE_GATES.isTagged(material);
    }

	/** Check if a material is a farmable block
	 * <p>ie: melon, potatoes, wheat</p>
	 * @param material Material to check
	 * @return True if material is farmable block
	 */
    public static boolean isFarmable(Material material) {
        return FARMABLE.contains(material);
    }

	/** Check if a material is a shove
	 * @param material Material to check
	 * @return True if material is shovel
	 */
    public static boolean isShovel(Material material) {
        return SHOVEL.contains(material);
    }

	/** Check if a material requires a shovel to dig it
	 * @param material Material to check
	 * @return True if material requires a shovel
	 */
    public static boolean requiresShovel(Material material) {
		return REQUIRES_SHOVEL.contains(material) || isConcretePowder(material);
	}

	/** Check if a material is a pickaxe
	 * @param material Material to check
	 * @return True if material is pickaxe
	 */
    public static boolean isPickaxe(Material material) {
        return PICKAXE.contains(material);
    }

	/** Check if a material requires a pickaxe to mine it
	 * <p>ie: ores, non-wood doors/slabs/stairs, concrete</p>
	 * @param material Material to check
	 * @return True if material requires pickaxe
	 */
    public static boolean requiresPickaxe(Material material) {
        if (REQUIRES_PICKAXE.contains(material)) return true;
        if (Utils.isStoneTypeBlock(material)) return true;
        if (Utils.isOreBlock(material)) return true;
        if (Utils.isNaturalOreBlock(material)) return true;
        if (Utils.isNonWoodDoor(material)) return true;
        if (Utils.isTerracotta(material)) return true;
        if (Utils.isGlazedTerracotta(material)) return true;
        if (Utils.isConcrete(material)) return true;
        if (Utils.isCookingBlock(material)) return true;
        if (Tag.WALLS.isTagged(material)) return true;
        if (Tag.ICE.isTagged(material)) return true;
        if (Tag.CORAL_BLOCKS.isTagged(material)) return true;
        return Tag.RAILS.isTagged(material);
    }

	/** Check if a material is an axe
	 * @param material Material to check
	 * @return True if material is axe
	 */
    public static boolean isAxe(Material material) {
        return AXE.contains(material);
    }

	/** Check if a material requires an axe to break
	 * @param material Material to check
	 * @return True if material requires axe
	 */
    public static boolean requiresAxe(Material material) {
        if (REQUIRES_AXE.contains(material)) return true;
        if (Tag.WOODEN_DOORS.isTagged(material)) return true;
        if (Tag.WOODEN_TRAPDOORS.isTagged(material)) return true;
        if (Tag.PLANKS.isTagged(material)) return true;
        if (Tag.LOGS.isTagged(material)) return true;
        if (Tag.WOODEN_STAIRS.isTagged(material)) return true;
        if (Tag.WOODEN_SLABS.isTagged(material)) return true;
        if (Tag.WOODEN_FENCES.isTagged(material)) return true;
        if (Tag.WOODEN_PRESSURE_PLATES.isTagged(material)) return true;
        if (Tag.BANNERS.isTagged(material)) return true;
        if (Tag.SIGNS.isTagged(material)) return true;
        return Utils.isWoodGate(material);
    }

	/** Check if a material requires shears to break
	 * <p>ie: cobweb, tripwire, tnt</p>
	 * @param material Material to check
	 * @return True if material requires shears
	 */
    public static boolean requiresShears(Material material) {
        return REQUIRES_SHEARS.contains(material);
    }

	/** Check if a material requires a hammer (in offhand) to place
	 * @param material Material to check
	 * @return True if material requires hammer
	 */
    public static boolean requiresHammer(Material material) {
        if (REQUIRES_HAMMER.contains(material)) return true;
        return Tag.DOORS.isTagged(material)

                || isWoodGate(material)
                || isTerracotta(material)
                || isGlazedTerracotta(material)
                || isConcrete(material)
                || isStoneTypeBlock(material)
                || isCookingBlock(material)
                || isStorageBlock(material)
                || isUtilityBlock(material)
                || isShulkerBox(material)
                || isOreBlock(material)

                || Tag.BEDS.isTagged(material)
                || Tag.LOGS.isTagged(material)
                || Tag.STAIRS.isTagged(material)
                || Tag.SLABS.isTagged(material)
                || Tag.PLANKS.isTagged(material)
                || Tag.WOODEN_PRESSURE_PLATES.isTagged(material)
                || Tag.WOODEN_FENCES.isTagged(material)
                || Tag.RAILS.isTagged(material)
                || Tag.BANNERS.isTagged(material)
                || Tag.FENCES.isTagged(material)
                || Tag.SIGNS.isTagged(material);
    }

	/** Get the drops for a certain material
	 * @param material Material that will be broken
	 * @param grown If the block is grown
	 * @return List of materials this material will drop
	 */
    public static List<Material> getDrops(Material material, Boolean grown) {
        List<Material> mat = new ArrayList<>();
		// TODO: Replace switch with ehanced switch
        switch (material) {
            case PUMPKIN:
                mat.add(Material.PUMPKIN);
                break;
            case MELON_STEM:
                mat.add(Material.MELON_SEEDS);
                break;
            case MELON:
                mat.add(Material.MELON_SLICE);
                break;
            case PUMPKIN_STEM:
                mat.add(Material.PUMPKIN_SEEDS);
                break;
            case CHORUS_FLOWER:
                mat.add(Material.CHORUS_FLOWER);
                break;
            case CARROTS:
                mat.add(Material.CARROT);
                break;
            case POTATOES:
                mat.add(Material.POTATO);
                break;
            case BEETROOTS:
                if (grown) {
                    mat.add(Material.BEETROOT);
                }
                mat.add(Material.BEETROOT_SEEDS);
                break;
            case WHEAT:
                if (grown) {
                    mat.add(Material.WHEAT);
                }
                mat.add(Material.WHEAT_SEEDS);
                break;
            case SWEET_BERRY_BUSH:
                mat.add(Material.SWEET_BERRIES);
                break;
            case COCOA:
                mat.add(Material.COCOA_BEANS);
                break;
            default:
                mat.add(Material.AIR);
        }
        return mat;
    }

    /** Send a colored string to a Player
     * <p>
     *     Does not require ChatColor methods
     * </p>
     * @param player The player to send a colored string to
     * @param msg The string to send including color codes
     */
    public static void sendColoredMsg(CommandSender player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

	/** Send a colored console message
     * <p>This will NOT include plugin prefix</p>
	 * @param msg Message to send
	 */
    public static void sendColoredConsoleMsg(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    /** Log a message to console
     * <p>This will include plugin prefix</p>
     * @param msg Message to log to console
     */
	public static void log(String msg) {
        Lang lang = Survival.getInstance().getLang();
        String prefix = "&7[&bSurvival&3Plus&7] ";
        if (lang != null) {
            prefix = lang.prefix;
        }
		sendColoredConsoleMsg(prefix + msg);
	}

    /** Log a formatted message to console
     * <p>Formatted in the same style as {@link String#format(String, Object...)}
     * <br>This will include plugin prefix</p>
     * @param format Message format
     * @param objects Objects in format
     */
	public static void log(String format, Object... objects) {
	    log(String.format(format, objects));
    }

    /** Gets a colored string
     * @param string The string including color codes/HEX color codes
     * @return Returns a formatted string
     */
    public static String getColoredString(String string) {
        if (isRunningMinecraft(1, 16)) {
            Matcher matcher = HEX_PATTERN.matcher(string);
            while (matcher.find()) {
				// TODO: Replace Deprecation
                final net.md_5.bungee.api.ChatColor hexColor = net.md_5.bungee.api.ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
                final String before = string.substring(0, matcher.start());
                final String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        }
		// TODO: Replace Deprecation
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', string);
    }

    /** Spawn a particle at a location for all players
     * @param location The location to spawn a particle at
     * @param particle The particle to spawn
     * @param amount The amount of particles
     * @param offsetX Offset by x
     * @param offsetY Offset by y
     * @param offsetZ Offset by z
     */
    public static void spawnParticle(Location location, Particle particle, int amount, double offsetX, double offsetY, double offsetZ) {
        assert location.getWorld() != null;
        location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ);
    }

    /** Spawn a particle at a location for a player
     * @param location The location to spawn a particle at
     * @param particle The particle to spawn
     * @param amount The amount of particles
     * @param offsetX Offset by x
     * @param offsetY Offset by y
     * @param offsetZ Offset by z
     * @param player The player to spawn a particle for
     */
    public static void spawnParticle(Location location, Particle particle, int amount, double offsetX, double offsetY, double offsetZ, Player player) {
        player.spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ);
    }

    /** Set the durability of an ItemStack
     * @param item The ItemStack to set
     * @param durability The durability to set
     */
    public static void setDurability(ItemStack item, int durability) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        ((Damageable) meta).setDamage(durability);
        item.setItemMeta(meta);
    }

    /** Check the durability of an ItemStack
     * @param item The ItemStack to check
     * @return The durability of the ItemStack
     */
    public static int getDurability(ItemStack item) {
        assert item.getItemMeta() != null;
        return ((Damageable) item.getItemMeta()).getDamage();
    }

    public static List<ItemStack> getItemStackDura(Item item, int maxDurability) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i < maxDurability; i++) {
            ItemStack stack = ItemManager.get(item);
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            ((Damageable) meta).setDamage(i);
            stack.setItemMeta(meta);
            itemStacks.add(stack);
        }
        return itemStacks;
    }

    /** Gets the minutes a player has played on the server
     * @param player The player to check
     * @return The number of minutes they have played on the server
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
	public static int getMinutesPlayed(Player player) {
        int played = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        return Math.round(played / 1200);
    }

    /** Check if server is running a minimum Minecraft version
     * @param major Major version to check (Most likely just going to be 1)
     * @param minor Minor version to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor) {
        return isRunningMinecraft(major, minor, 0);
    }

    /** Check if server is running a minimum Minecraft version
     * @param major Major version to check (Most likely just going to be 1)
     * @param minor Minor version to check
     * @param revision Revision to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor, int revision) {
        String[] version = Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.");
        int maj = Integer.parseInt(version[0]);
        int min = Integer.parseInt(version[1]);
        int rev;
        try {
            rev = Integer.parseInt(version[2]);
        } catch (Exception ignore) {
            rev = 0;
        }
        return maj > major || min > minor || (min == minor && rev >= revision);
    }

    public static boolean isRunningSpigot() {
        return classExists("org.spigotmc.CustomTimingsHandler");
    }

	/** Check if a class exists
	 * @param className The {@link Class#getCanonicalName() canonical name} of the class
	 * @return True if the class exists
	 */
    public static boolean classExists(final String className) {
    	try {
    		Class.forName(className);
    		return true;
		} catch (ClassNotFoundException ex) {
    		return false;
		}
	}

	/** Check if a method exists
	 * @param c Class the method belongs to
	 * @param methodName Name of method
	 * @param parameterTypes Parameter types for this method
	 * @return True if the method exists
	 */
	public static boolean methodExists(final Class<?> c, final String methodName, final Class<?>... parameterTypes) {
    	try {
    		c.getDeclaredMethod(methodName, parameterTypes);
			return true;
		} catch (NoSuchMethodException ex) {
			return false;
		}
	}

    /** Check if this entity is a Citizens NPC
     * @param entity Entity to check
     * @return True if entity is an NPC
     */
	public static boolean isCitizensNPC(Metadatable entity) {
	    return entity.hasMetadata("NPC");
    }

    /** Get a {@link NamespacedKey} linked to this plugin
     * @param key Key to create
     * @return New NamespacedKey linked to this plugin
     */
    public static NamespacedKey getNamespacedKey(String key) {
	    return new NamespacedKey(Survival.getInstance(), key);
    }

    /** Check if the player is at the highest block (exposed to sun)
     * @param player Player to check
     * @return True if player is exposed to sun
     */
    public static boolean isAtHighest(Player player) {
        Location location = player.getLocation();
        World world = player.getWorld();
        return location.getY() > world.getHighestBlockAt(location).getY();
    }

}

