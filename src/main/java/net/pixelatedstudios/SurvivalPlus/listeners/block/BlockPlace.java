package net.pixelatedstudios.SurvivalPlus.listeners.block;

import net.kyori.adventure.text.format.TextColor;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Random;

public class BlockPlace implements Listener {

    private final Config config;
    private final Lang lang;

    public BlockPlace(Survival plugin) {
        this.config = plugin.getSurvivalConfig();
        this.lang = plugin.getLang();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();

        ItemStack mainTool = player.getInventory().getItemInMainHand();
        ItemStack offTool = player.getInventory().getItemInOffHand();

        Block block = event.getBlock();

        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            if (config.PLACE_ONLY_WITH_HAMMER) {
                if (Utils.requiresHammer(block.getType())) {
                    if (ItemManager.compare(offTool, Item.HAMMER)) {
                        Random rand = new Random();
                        int chance_reduceDur = rand.nextInt(10) + 1;
                        if (chance_reduceDur == 1) {
                            Utils.setDurability(offTool, Utils.getDurability(offTool) + 1);
                        }

                        if (Utils.getDurability(offTool) >= offTool.getType().getMaxDurability()) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                            player.getInventory().setItemInOffHand(null);
                        }
                    } else if (ItemManager.compare(mainTool, Item.HAMMER)) {
                        Random rand = new Random();
                        int chance_reduceDur = rand.nextInt(10) + 1;
                        if (chance_reduceDur == 1) {
                            Utils.setDurability(mainTool, ((Damageable) mainTool.getItemMeta()).getDamage() + 1);
                        }

                        if (Utils.getDurability(mainTool) >= mainTool.getType().getMaxDurability()) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                            player.getInventory().setItemInMainHand(null);
                        }
                    } else {
                        event.setCancelled(true);
                        player.updateInventory();
                        player.sendMessage(Utils.getColoredString(lang.task_must_use_hammer).color(TextColor.color(255, 0, 0)));
                    }
                }
            }
        }
    }

}