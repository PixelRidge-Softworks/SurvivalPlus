package Pixelated.Studios.survival.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import Pixelated.Studios.survival.util.Utils;

public class Recipes implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String prefix = "&7[&3SurvivalPlus&7] ";
		if (!(sender instanceof Player)) {
			Utils.sendColoredConsoleMsg(prefix + "&cPlayer only command");
			return true;
		}
		Player player = (Player) sender;
		Utils.sendColoredMsg(player, prefix + "&6Recipes");
		Utils.sendColoredMsg(player, "  &7Recipes can be found in your crafting guide in your inventory/crafting table");
		return true;
	}

}