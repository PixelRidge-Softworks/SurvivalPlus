package net.pixelatedstudios.SurvivalPlus.commands;

import net.pixelatedstudios.SurvivalPlus.managers.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import net.pixelatedstudios.SurvivalPlus.Survival;
import net.pixelatedstudios.SurvivalPlus.data.Info;
import net.pixelatedstudios.SurvivalPlus.data.PlayerData;
import net.pixelatedstudios.SurvivalPlus.config.Config;
import net.pixelatedstudios.SurvivalPlus.config.Lang;
import net.pixelatedstudios.SurvivalPlus.util.Utils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class Status implements CommandExecutor, TabCompleter {

	private final Config config;
	private final Lang lang;
	private final PlayerManager playerManager;

	public Status(Survival plugin) {
		this.config = plugin.getSurvivalConfig();
		this.lang = plugin.getLang();
		this.playerManager = plugin.getPlayerManager();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("status")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Utils.getColoredString(lang.players_only));
				return false;
			}

			Player player = (Player) sender;
			PlayerData playerData = playerManager.getPlayerData(player);

			if (args.length == 0) {
				if (!config.MECHANICS_STATUS_SCOREBOARD) {
					player.sendMessage(playerManager.ShowHunger(player).get(1) +
							playerManager.ShowHunger(player).get(2) + " " +
							playerManager.ShowHunger(player).get(0).toUpperCase());
					if (config.MECHANICS_THIRST_ENABLED)
						player.sendMessage(playerManager.ShowThirst(player).get(1) +
								playerManager.ShowThirst(player).get(2) + " " +
								playerManager.ShowThirst(player).get(0).toUpperCase());
				} else {
					sendHelp(player);
				}
			}

			if (args.length == 1) {
				switch (args[0]) {
					case "all":
						if (!config.MECHANICS_STATUS_SCOREBOARD) {
							player.sendMessage(playerManager.ShowHunger(player).get(1) +
									playerManager.ShowHunger(player).get(2) + " " +
									playerManager.ShowHunger(player).get(0).toUpperCase());
							if (config.MECHANICS_THIRST_ENABLED)
								player.sendMessage(playerManager.ShowThirst(player).get(1) +
										playerManager.ShowThirst(player).get(2) + " " +
										playerManager.ShowThirst(player).get(0).toUpperCase());
							if (config.MECHANICS_ENERGY_ENABLED)
							    player.sendMessage(playerManager.showEnergy(player).get(1) +
                                        " " + playerManager.showEnergy(player).get(0).toUpperCase());
							if (config.MECHANICS_FOOD_DIVERSITY_ENABLED) {
								for (String s : playerManager.ShowNutrients(player))
									player.sendMessage(s);
							}
						} else {
							playerData.setInfoDisplayed(Info.HUNGER, true);
							if (config.MECHANICS_THIRST_ENABLED)
								playerData.setInfoDisplayed(Info.THIRST, true);
							if (config.MECHANICS_ENERGY_ENABLED)
								playerData.setInfoDisplayed(Info.ENERGY, true);
							if (config.MECHANICS_FOOD_DIVERSITY_ENABLED)
								playerData.setInfoDisplayed(Info.NUTRIENTS, true);
						}
						break;
					case "none":
					case "off":
						playerData.setInfoDisplayed(Info.HUNGER, false);
						playerData.setInfoDisplayed(Info.THIRST, false);
						playerData.setInfoDisplayed(Info.ENERGY, false);
						playerData.setInfoDisplayed(Info.NUTRIENTS, false);
						break;
					case "hunger":
					case "h":
						if (!config.MECHANICS_STATUS_SCOREBOARD) {
							player.sendMessage(playerManager.ShowHunger(player).get(1) +
									playerManager.ShowHunger(player).get(2) + " " +
									playerManager.ShowHunger(player).get(0).toUpperCase());
						} else
							playerData.setInfoDisplayed(Info.HUNGER, !playerData.isInfoDisplayed(Info.HUNGER));
						break;
					case "thirst":
					case "t":
						if (!config.MECHANICS_STATUS_SCOREBOARD) {
							if (config.MECHANICS_THIRST_ENABLED)
								player.sendMessage(playerManager.ShowThirst(player).get(1) +
										playerManager.ShowThirst(player).get(2) + " " +
										playerManager.ShowThirst(player).get(0).toUpperCase());
						} else
							playerData.setInfoDisplayed(Info.THIRST, !playerData.isInfoDisplayed(Info.THIRST));
						break;
					case "fatigue":
					case "f":
                    case "energy":
                    case "e":
						if (!config.MECHANICS_STATUS_SCOREBOARD) {
							if (config.MECHANICS_ENERGY_ENABLED)
                                player.sendMessage(playerManager.showEnergy(player).get(1) +
                                        " " + playerManager.showEnergy(player).get(0).toUpperCase());
						} else
							playerData.setInfoDisplayed(Info.ENERGY, !playerData.isInfoDisplayed(Info.ENERGY));
						break;
					case "nutrients":
					case "n":
						if (!config.MECHANICS_STATUS_SCOREBOARD) {
							if (config.MECHANICS_FOOD_DIVERSITY_ENABLED) {
								for (String s : playerManager.ShowNutrients(player))
									player.sendMessage(s);
							}
						} else
							playerData.setInfoDisplayed(Info.NUTRIENTS, !playerData.isInfoDisplayed(Info.NUTRIENTS));
						break;
					default:
						sendHelp(player);
				}
			}
			return true;
		} else
			return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		StringBuilder builder = new StringBuilder();
		for (String arg : args) {
			builder.append(arg).append(" ");
		}
		String[] list = {"all", "hunger", "thirst", "energy", "nutrients", "none", "help"};

		String arg = builder.toString().trim();
		ArrayList<String> matches = new ArrayList<>();
		for (String name : list) {
			if (StringUtil.startsWithIgnoreCase(name, arg)) {
				matches.add(name);
			}
		}
		return matches;
	}

	private void sendHelp(Player player) {
        Utils.sendColoredMsg(player, lang.prefix + "&6HealthBoard");
        Utils.sendColoredMsg(player, "  &b/stat all &7- Show your entire health board");
        Utils.sendColoredMsg(player, "  &b/stat none &7- Turn off your entire health board");
        Utils.sendColoredMsg(player, "  &b/stat hunger &7- Turn on/off hunger");
        Utils.sendColoredMsg(player, "  &b/stat thirst &7- Turn on/off thirst");
        Utils.sendColoredMsg(player, "  &b/stat energy &7- Turn on/off energy");
        Utils.sendColoredMsg(player, "  &b/stat nutrients &7- Turn on/off nutrients");
    }

}
