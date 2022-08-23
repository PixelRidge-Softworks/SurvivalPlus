package net.pixelatedstudios.SurvivalPlus.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.listeners.block.SnowGeneration;

// TODO: Remove this entire feature
public class SnowGen implements CommandExecutor {

	private final Survival plugin;
	private final String prefix = ChatColor.translateAlternateColorCodes('&', "&7[&3SurvivalPlus&7] ");

	public SnowGen(Survival plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			switch (args[0].toLowerCase()) {
				case "on":
					plugin.setSnowGenOption(true);
					break;
				case "off":
					plugin.setSnowGenOption(false);
					break;
				default:
					return false;
			}
			if (sender instanceof Player) {
				sender.sendMessage(ChatColor.AQUA + prefix + ChatColor.YELLOW + "Snow Generation is " + args[0].toLowerCase());
				sender.getServer().getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + "Snow Generation is " + args[0].toLowerCase());
			} else if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage(prefix + ChatColor.YELLOW + "Snow Generation is " + args[0].toLowerCase());
			}
			return true;
		} else if (args.length == 0) {
			if (Bukkit.getServer().getOnlinePlayers().size() <= 1) {
				if (sender instanceof Player) {
					sender.sendMessage(ChatColor.AQUA + prefix + ChatColor.RED + "WARNING!" + ChatColor.YELLOW + " Snow Generation is running for generated chunks!");
					sender.getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + "WARNING!" + ChatColor.YELLOW + " Snow Generation is running for generated chunks!");
				} else if (sender instanceof ConsoleCommandSender) {
					sender.sendMessage(prefix + ChatColor.RED + "WARNING!" + ChatColor.YELLOW + " Snow Generation is running for generated chunks!");
				}

				SnowGeneration snowGen = new SnowGeneration(plugin);

				for (final World world : Bukkit.getServer().getWorlds()) {
					for (final Chunk chunk : world.getLoadedChunks()) {
						snowGen.checkChunk(chunk);
					}
				}

				if (sender instanceof Player) {
					sender.sendMessage(ChatColor.AQUA + prefix + ChatColor.GREEN + "Snow Generation is completed.");
					sender.getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Snow Generation is completed.");
				} else if (sender instanceof ConsoleCommandSender) {
					sender.sendMessage(prefix + ChatColor.GREEN + "Snow Generation is completed.");
				}

				return true;
			} else {
				if (sender instanceof Player) {
					sender.sendMessage(ChatColor.AQUA + prefix + ChatColor.RED + "WARNING!" + ChatColor.YELLOW + " Snow Generation will run through all generated chunks, lag spikes may occur!");
					sender.sendMessage(ChatColor.AQUA + prefix + ChatColor.YELLOW + "Run this command while nobody is in the server.");
				} else if (sender instanceof ConsoleCommandSender) {
					sender.sendMessage(prefix + ChatColor.RED + "WARNING!" + ChatColor.YELLOW + " Snow Generation will run through all generated chunks, lag spikes may occur!");
					sender.sendMessage(prefix + ChatColor.YELLOW + "Run this command while nobody is in the server.");
				}

				return false;
			}
		}
		return false;
	}

}

