package veth.vetheon.survival.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.gui.NutritionGUI;
import veth.vetheon.survival.util.Utils;

public class Nutrition implements CommandExecutor {

    private final Survival plugin;

    public Nutrition(Survival plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            NutritionGUI gui = new NutritionGUI(this.plugin);
            gui.openInventory(player, 0);
        } else {
            if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
                itemTest();
                return true;
            }
            Utils.log("&cThis is a player only command!");
        }
        return true;
    }

    // Used for debugging edible items
    private void itemTest() {
        veth.vetheon.survival.item.Nutrition.getAllNutritions().forEach(nutrition -> {
            String key = nutrition.getKey().toString().replace(":", "&r:&a");
            String item = nutrition.getItemStack().toString().replace("{", "&r{&b").replace("}", "&r}&b");
            Utils.log("Nutrition%s:", nutrition.isCustom() ? "&r(&cCUSTOM&r)&7" : "");
            Utils.log(" - Key: &a%s", key);
            Utils.log(" - Item: &e%s", item);
        });
    }

}

