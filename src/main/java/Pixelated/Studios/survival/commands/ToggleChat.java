package Pixelated.Studios.survival.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import Pixelated.Studios.survival.Survival;
import Pixelated.Studios.survival.data.PlayerData;
import Pixelated.Studios.survival.managers.PlayerManager;
import Pixelated.Studios.survival.config.Lang;
import Pixelated.Studios.survival.util.Utils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class ToggleChat implements CommandExecutor, TabCompleter {

	private final Lang lang;
	private final PlayerManager playerManager;
	private final int LOCAL_CHAT_DIST;
	
	public ToggleChat(Survival plugin) {
		this.lang = plugin.getLang();
		this.playerManager = plugin.getPlayerManager();
		this.LOCAL_CHAT_DIST = plugin.getSurvivalConfig().LOCAL_CHAT_DISTANCE;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("togglechat")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.getColoredString(lang.players_only));
				return true;
			}
			Player player = (Player) sender;
			PlayerData playerData = playerManager.getPlayerData(player);

			if (LOCAL_CHAT_DIST <= -1) {
				player.sendMessage(Utils.getColoredString(lang.toggle_chat_disabled));
				return true;
			}

			if (args.length == 1) {

				switch (args[0]) {
					case "local":
					case "l":
						player.sendMessage(Utils.getColoredString(lang.toggle_chat_local));
						playerData.setLocalChat(true);
						break;
					case "global":
					case "g":
						player.sendMessage(Utils.getColoredString(lang.toggle_chat_global));
						playerData.setLocalChat(false);
						break;
					default:
						return false;
				}
			} else if (args.length == 0) {
				if (playerData.isLocalChat()) {
					player.sendMessage(Utils.getColoredString(lang.toggle_chat_global));
					playerData.setLocalChat(false);
				} else {
					player.sendMessage(Utils.getColoredString(lang.toggle_chat_local));
					playerData.setLocalChat(true);
				}
			} else {
				sender.sendMessage(ChatColor.RED + Utils.getColoredString(lang.invalid_arg));
				return false;
			}

			return true;
		} else {
			sender.sendMessage("Command: " + command.getName());
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		StringBuilder builder = new StringBuilder();
		for (String arg : args) {
			builder.append(arg).append(" ");
		}
		String[] list = {"local", "global"};
		String[] list2 = {""};
		String arg = builder.toString().trim();
		ArrayList<String> matches = new ArrayList<>();
		for (String name : (LOCAL_CHAT_DIST > -1) ? list : list2) {
			if (StringUtil.startsWithIgnoreCase(name, arg)) {
				matches.add(name);
			}
		}
		return matches;
	}

}

