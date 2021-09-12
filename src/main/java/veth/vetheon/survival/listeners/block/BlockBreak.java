package veth.vetheon.survival.listeners.block;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.util.Utils;

import java.util.Random;

public class BlockBreak implements Listener {
	
	private Config settings;
	private Lang lang;
	
	public BlockBreak(Survival plugin) {
		this.lang = plugin.getLang();
		this.settings = plugin.getSurvivalConfig();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();

		ItemStack tool = player.getInventory().getItemInMainHand();

		Block block = event.getBlock();
		Material material = block.getType();

		if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
			if (!ItemManager.compare(tool, Item.QUARTZ_PICKAXE)) {
				if (settings.BREAK_ONLY_WITH_SHOVEL) {
					if (!Utils.isShovel(tool.getType())) {
						if (Utils.requiresShovel(material)) {
							event.setCancelled(true);
							player.updateInventory();
							player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.task_must_use_shovel));
						}
						//Flint
						if (material == Material.GRAVEL) {
							event.setDropItems(false);

							Random rand = new Random();
							double chance = rand.nextDouble();

							if (chance <= settings.DROP_RATE_FLINT)
								event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.FLINT));
						}
					} else {
						Block above = block.getRelative(BlockFace.UP);
						switch (block.getType()) {
							case GRASS_BLOCK:
							case DIRT:
							case PODZOL:
							case COARSE_DIRT:
							case FARMLAND:
								if (Utils.isFarmable(above.getType())) {
									above.setType(Material.AIR);
								}
						}
					}
				}

				if (settings.BREAK_ONLY_WITH_AXE && !Utils.isAxe(tool.getType())) {
					if (Utils.requiresAxe(material)) {
						event.setCancelled(true);
						player.updateInventory();
						player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.task_must_use_axe));
					}

					//Fix half door glitch
					if (Tag.DOORS.isTagged(material)) {
						if (block.getRelative(BlockFace.UP).getType() == material)
							block.getRelative(BlockFace.UP).getState().update(true);
						if (block.getRelative(BlockFace.DOWN).getType() == material)
							block.getRelative(BlockFace.DOWN).getState().update(true);
					}
				}
				if (settings.BREAK_ONLY_WITH_PICKAXE && !Utils.isPickaxe(tool.getType())) {
					if (Utils.requiresPickaxe(material)) {
						event.setCancelled(true);
						player.updateInventory();
						player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.task_must_use_pick));
					}
				}

				if (settings.BREAK_ONLY_WITH_SICKLE) {
					if (Utils.isFarmable(material)) {
						if (!Item.Tags.SICKLES.isTagged(tool)) {
							event.setCancelled(true);
							player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.task_must_use_sickle));
						} else {
							event.setDropItems(false);
							Location loc = event.getBlock().getLocation();
							int random = 1;
							int multiplier = 1;
							boolean grown = true;

							if (event.getBlock().getBlockData() instanceof Ageable) {
								Ageable crop = ((Ageable) event.getBlock().getBlockData());
								grown = crop.getAge() == crop.getMaximumAge();
							}

							// Flint/Stone sickles drop a chance of 0-1 items (not grown) or 1-2 (grown)
							if (ItemManager.compare(tool, Item.FLINT_SICKLE)) {
								multiplier = 4;
								random = grown ? new Random().nextInt(2) + 1 : new Random().nextInt(2);
							}
							if (ItemManager.compare(tool, Item.STONE_SICKLE)) {
								multiplier = 2;
								random = grown ? new Random().nextInt(2) + 1 : new Random().nextInt(2);
							}
							// Iron/Diamond sickles drop a chance of 1 (not grown) or 2-4 items (grown)
							if (ItemManager.compare(tool, Item.IRON_SICKLE, Item.DIAMOND_SICKLE)) {
								random = grown ? new Random().nextInt(2) + 3 : 1;
							}

							for (Material drop : Utils.getDrops(material, grown)) {
								if (drop != Material.AIR && random != 0) {
									assert loc.getWorld() != null;
									if (drop == Material.PUMPKIN) { // prevent duping pumpkins
									    random = 1;
                                    }
									loc.getWorld().dropItemNaturally(loc, new ItemStack(drop, random));
								}
							}
							if (tool.getType().getMaxDurability() < Utils.getDurability(tool) + multiplier) {
								player.getInventory().setItemInMainHand(null);
								player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
								return;
							}
							Utils.setDurability(tool, Utils.getDurability(tool) + multiplier);
							player.updateInventory();
						}
					}
				}

				if (!(tool.getType() == Material.SHEARS)) {
					if (settings.BREAK_ONLY_WITH_SHEARS) {
						if (Utils.requiresShears(material)) {
							event.setCancelled(true);
							player.updateInventory();
							player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.task_must_use_shear));
						}
					}

					//Sticks - Maybe this should be removed since 1.14+ leaves drop sticks?!?!?
					if (Tag.LEAVES.isTagged(material)) {
						Random rand = new Random();
						double chance = rand.nextDouble();

						if (chance <= settings.DROP_RATE_STICK)
							event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.STICK));
					}
				}
				if (settings.RECIPES_WORKBENCH && material == Material.CRAFTING_TABLE && !event.isCancelled()) {
					event.setDropItems(false);
					ItemStack workbench = ItemManager.get(Item.WORKBENCH);
					block.getWorld().dropItem(block.getLocation(), workbench);
				}
			} else {
				if (Utils.isOreBlock(material) || Utils.isNaturalOreBlock(material)) {
					event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(material));
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	private void onHarvest(PlayerInteractEvent e) {
		if (e.isCancelled()) return;
		if (!settings.BREAK_ONLY_WITH_SICKLE) return;
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR
				|| e.getAction() == Action.LEFT_CLICK_BLOCK) return;
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		ItemStack tool = player.getInventory().getItemInMainHand();
		assert block != null;
		if (block.getType() == Material.SWEET_BERRY_BUSH) {
			Ageable bush = ((Ageable) block.getBlockData());
			if (e.getItem() != null && e.getItem().getType() == Material.BONE_MEAL) {
				if (bush.getAge() == 3) {
					e.setCancelled(true);
					return;
				} else return;
			}
			if (!Item.Tags.SICKLES.isTagged(tool)) {
				e.setCancelled(true);
				player.sendMessage(ChatColor.RED + Utils.getColoredString(lang.task_must_use_sickle));
			} else {
				if (bush.getAge() >= 2) {
					int berries = 0;
					Location loc = block.getLocation();
					assert loc.getWorld() != null;
					int multiplier = 1;
					e.setCancelled(true);
					int random = new Random().nextInt(5) + 1;

					if (ItemManager.compare(tool, Item.FLINT_SICKLE)) {
						if (bush.getAge() == 3) {
							berries = 1;
						}
						multiplier = 4;
					} else if (ItemManager.compare(tool, Item.STONE_SICKLE)) {
						if (bush.getAge() == 2) {
							if (random <= 4)
								berries = 1;
						} else if (bush.getAge() == 3) {
							if (random <= 3)
								berries = 1;
							else
								berries = 2;
						}
						multiplier = 2;
					} else if (ItemManager.compare(tool, Item.IRON_SICKLE, Item.DIAMOND_SICKLE)) {
						if (bush.getAge() == 2) {
							if (random <= 3)
								berries = 1;
							else
								berries = 2;
						} else if (bush.getAge() == 3) {
							if (random <= 4)
								berries = 2;
							else
								berries = 4;
						}
					}
					if (berries != 0)
						loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.SWEET_BERRIES, berries));

					bush.setAge(1);
					block.setBlockData(bush);
					int durability = Utils.getDurability(tool) + multiplier;
					Utils.setDurability(tool, durability);
					player.playSound(loc, Sound.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, 1, 1);
					if (durability >= tool.getType().getMaxDurability()) {
                        player.getInventory().setItemInMainHand(null);
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                    }
				}
			}
		}
	}

	@EventHandler
	private void onWaterBreakCrops(BlockPhysicsEvent event) {
		if (!settings.BREAK_ONLY_WITH_SICKLE) return;
		if (event.getSourceBlock().getType() == Material.WATER) {
		    Material type = event.getBlock().getType();
			if (Utils.isFarmable(type)) {
			    if (type == Material.MELON || type == Material.PUMPKIN) return;
				event.getBlock().setType(Material.AIR);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onTrample(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		if (!settings.BREAK_ONLY_WITH_SICKLE) return;
		if (event.getAction() == Action.PHYSICAL) {
			if (event.getClickedBlock() == null) return;
			if (event.getClickedBlock().getType() == Material.FARMLAND) {
				Location loc = event.getClickedBlock().getLocation();
				assert loc.getWorld() != null;
				loc.getWorld().playEffect(loc, Effect.STEP_SOUND, event.getClickedBlock().getRelative(BlockFace.UP).getType());
				event.getClickedBlock().getRelative(BlockFace.UP).setType(Material.AIR);
			}
		}
	}

}

