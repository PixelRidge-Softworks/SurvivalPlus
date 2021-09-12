package veth.vetheon.survival.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.config.Config;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.data.PlayerData;
import veth.vetheon.survival.util.Utils;

public class Heal implements CommandExecutor {

    private final Survival plugin;
    private final Config config;
    private final Lang lang;

    public Heal(Survival plugin) {
        this.plugin = plugin;
        this.config = plugin.getSurvivalConfig();
        this.lang = plugin.getLang();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length > 0) {
            if (!sender.hasPermission("survivalplus.heal.others")) {
                sender.sendMessage(plugin.getLang().no_perm);
                return true;
            }
            Player healed = Bukkit.getPlayer(args[0]);
            if (healed == null) {
                Utils.sendColoredMsg(sender, lang.cmd_player_not_online.replace("<player>", args[0]));
                return true;
            }
            heal(healed);
            Utils.sendColoredMsg(sender, lang.cmd_heal_other.replace("<player>", args[0]));
            Utils.sendColoredMsg(healed, lang.cmd_heal_by.replace("<player>", sender.getName()));
        } else {
            if (sender instanceof Player) {
                heal(((Player) sender));
                Utils.sendColoredMsg(sender, lang.cmd_heal_self);
            } else {
                Utils.log("&cConsole can not heal itself!");
                return true;
            }
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private void heal(Player player) {
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(20);
        player.setSaturation(5);
        PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
        if (config.MECHANICS_THIRST_ENABLED) {
            playerData.setThirst(40);
        }
        if (config.MECHANICS_FOOD_DIVERSITY_ENABLED) {
            switch (player.getWorld().getDifficulty()) {
                case PEACEFUL:
                case EASY:
                    playerData.setNutrients( 960, 240, 360);
                    break;
                case NORMAL:
                    playerData.setNutrients( 480, 120, 180);
                    break;
                case HARD:
                    playerData.setNutrients( 96, 24, 36);
                    break;
            }
        }
        if (config.MECHANICS_ENERGY_ENABLED) {
            playerData.setEnergy(20.0);
        }
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

}
