package veth.vetheon.survival.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.managers.ItemManager;
import veth.vetheon.survival.item.Item;
import veth.vetheon.survival.config.Lang;
import veth.vetheon.survival.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class GiveItem implements CommandExecutor, TabCompleter {

    private final Lang lang;

    public GiveItem(Survival plugin) {
        this.lang = plugin.getLang();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String prefix = Utils.getColoredString(lang.prefix);
        if (args.length < 2) return true;
        Player player = Bukkit.getPlayer(args[0]);
        if (player != null) {
            Item item;
            int amount = 1;
            try {
                if (args.length == 3) {
                    amount = Integer.parseInt(args[2]);
                }
            } catch (IllegalArgumentException ignore) {}
            try {
                item = Item.valueOf(args[1].toUpperCase());
                ItemStack itemStack = ItemManager.get(item);
                itemStack.setAmount(amount);

                Location loc = player.getLocation();
                loc.setY(loc.getY() + 1);

                if (player.getInventory().addItem(itemStack).size() != 0) {
                    player.getWorld().dropItem(loc, itemStack);
                }
                if (item != null) {
                    String itemName = item.getKey().replace("_", " ");
                    if (sender instanceof Player) {
                        Utils.sendColoredMsg(sender, prefix + "&6You gave &b" + itemName + " &6to &b" + player.getName());
                    } else {
                        Utils.sendColoredMsg(sender, prefix + "&6CONSOLE gave &b" + itemName + " &6to &b" + player.getName());
                    }
                }
            } catch (IllegalArgumentException ignore) {
                Utils.sendColoredMsg(sender, prefix + "&b" + args[1] + "&c is not an item");
            }
        } else {
            Utils.sendColoredMsg(sender, prefix + "&b" + args[0] + " &cis not online");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0 || args.length >= 4) {
            return ImmutableList.of();
        }
        if (args.length <= 1) return null;
        if (args.length == 2) {
            ArrayList<String> matches = new ArrayList<>();
            for (Item item : Item.values()) {
                String name = item.getKey().toUpperCase();
                if (StringUtil.startsWithIgnoreCase(name, args[1])) {
                    matches.add(name);
                }
            }
            return matches;
        } else {
            return Collections.singletonList("<amount>");
        }
    }

}
