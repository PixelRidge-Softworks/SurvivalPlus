package net.pixelatedstudios.SurvivalPlus.listeners.item;

import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;
import net.pixelatedstudios.SurvivalPlus.data.Stat;
import net.pixelatedstudios.SurvivalPlus.item.Item;
import net.pixelatedstudios.SurvivalPlus.managers.ItemManager;
import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import net.pixelatedstudios.SurvivalPlus.util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class MedicalKit implements Listener {

    private final Survival plugin;
    private final Lang lang;
    private final PlayerManager playerManager;

    public MedicalKit(Survival plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLang();
        this.playerManager = plugin.getPlayerManager();
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDamaged(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerData playerData = playerManager.getPlayerData(player);
            playerData.setStat(Stat.HEALING, 0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onClickEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;
        final Player player = event.getPlayer();
        PlayerData playerData = playerManager.getPlayerData(player);
        final ItemStack mainItem = player.getInventory().getItemInMainHand();

        if (ItemManager.compare(mainItem, Item.MEDIC_KIT)) {
            if (playerData.getStat(Stat.HEALING) <= 0) {
                if (!player.isSneaking()) {
                    if (event.getRightClicked() instanceof Player) {
                        final Player healed = (Player) event.getRightClicked();
                        PlayerData healedData = playerManager.getPlayerData(healed);

                        if (healedData.getStat(Stat.HEALING) <= 0) {
                            if (player.getLocation().distance(healed.getLocation()) <= 4) {
                                playerData.setStat(Stat.HEALING, 1);
                                healedData.setStat(Stat.HEALING, 1);
                                healed.teleport(playerManager.lookAt(healed.getLocation(), player.getLocation()));
                                player.sendMessage(Utils.getColoredString(lang.healing) + ChatColor.RESET + healed.getDisplayName() + Utils.getColoredString(lang.keep) + ChatColor.DARK_GREEN + Utils.getColoredString(lang.medical_kit) + Utils.getColoredString(lang.on_hand));
                                healed.sendMessage(Utils.getColoredString(lang.being_healed) + ChatColor.RESET + player.getDisplayName() + Utils.getColoredString(lang.stay_still));

                                playerData.setStat(Stat.HEAL_TIMES, 5);
                                final Runnable task = new Runnable() {
                                    public void run() {
                                        int times = playerData.getStat(Stat.HEAL_TIMES);
                                        if (player.getInventory().getItemInMainHand().getType() == Material.CLOCK && player.getLocation().distance(healed.getLocation()) <= 4 && playerData.getStat(Stat.HEALING) > 0 && healedData.getStat(Stat.HEALING) > 0) {
                                            if (times-- > 0) {
                                                player.teleport(playerManager.lookAt(player.getLocation(), healed.getLocation()));

                                                Random rand = new Random();

                                                player.removePotionEffect(PotionEffectType.SLOW);
                                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 6, true, false));
                                                player.removePotionEffect(PotionEffectType.JUMP);
                                                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 199, true, false));

                                                healed.getWorld().playSound(healed.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                                                healed.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));

                                                Location particleLoc = healed.getLocation();
                                                particleLoc.setY(particleLoc.getY() + 1);
                                                Utils.spawnParticle(particleLoc, Particle.VILLAGER_HAPPY, 10, 0.5, 0.5, 0.5);

                                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20L);
                                                playerData.setStat(Stat.HEAL_TIMES, times);
                                            } else {
                                                playerData.setStat(Stat.HEALING, 0);
                                                healedData.setStat(Stat.HEALING, 0);

                                                player.sendMessage(ChatColor.DARK_GREEN + Utils.getColoredString(lang.healing_complete));
                                                healed.sendMessage(ChatColor.DARK_GREEN + Utils.getColoredString(lang.healing_complete));

                                                player.getInventory().removeItem(ItemManager.get(Item.MEDIC_KIT));
                                            }
                                        } else {
                                            playerData.setStat(Stat.HEALING, 0);
                                            healedData.setStat(Stat.HEALING, 0);

                                            player.sendMessage(ChatColor.DARK_RED + Utils.getColoredString(lang.healing_interrupted));
                                            healed.sendMessage(ChatColor.DARK_RED + Utils.getColoredString(lang.healing_interrupted));

                                            player.getInventory().removeItem(ItemManager.get(Item.MEDIC_KIT));
                                        }
                                    }
                                };
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, -1L);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onSelfClick(PlayerInteractEvent event) {
        if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            final Player player = event.getPlayer();
            PlayerData playerData = playerManager.getPlayerData(player);
            ItemStack mainItem = player.getInventory().getItemInMainHand();
            if (ItemManager.compare(mainItem, Item.MEDIC_KIT)) {
                if (playerData.getStat(Stat.HEALING) <= 0) {
                    if (player.isSneaking()) {
                        playerData.setStat(Stat.HEALING, 1);
                        player.sendMessage(Utils.getColoredString(lang.healing_self) + Utils.getColoredString(lang.keep) + ChatColor.DARK_GREEN + Utils.getColoredString(lang.medical_kit) + Utils.getColoredString(lang.on_hand));

                        playerData.setStat(Stat.HEAL_TIMES, 5);
                        final Runnable task = new Runnable() {
                            public void run() {
                                int times = playerData.getStat(Stat.HEAL_TIMES);
                                if (ItemManager.compare(player.getInventory().getItemInMainHand(), Item.MEDIC_KIT) && playerData.getStat(Stat.HEALING) > 0) {
                                    if (times-- > 0) {
                                        Random rand = new Random();

                                        player.removePotionEffect(PotionEffectType.SLOW);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 6, true, false));
                                        player.removePotionEffect(PotionEffectType.JUMP);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 199, true, false));

                                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));

                                        Location particleLoc = player.getLocation();
                                        particleLoc.setY(particleLoc.getY() + 1);
                                        Utils.spawnParticle(particleLoc, Particle.VILLAGER_HAPPY, 10, 0.5, 0.5, 0.5);

                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 20L);
                                        playerData.setStat(Stat.HEAL_TIMES, times);
                                    } else {
                                        playerData.setStat(Stat.HEALING, 0);

                                        player.sendMessage(ChatColor.DARK_GREEN + Utils.getColoredString(lang.healing_complete));

                                        player.getInventory().removeItem(ItemManager.get(Item.MEDIC_KIT));
                                    }
                                } else {
                                    playerData.setStat(Stat.HEALING, 0);

                                    player.sendMessage(ChatColor.DARK_RED + Utils.getColoredString(lang.healing_interrupted));

                                    player.getInventory().removeItem(ItemManager.get(Item.MEDIC_KIT));
                                }
                            }
                        };

                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, -1L);
                    }
                }
            }
        }
    }


}
