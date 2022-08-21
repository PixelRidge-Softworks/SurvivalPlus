package veth.vetheon.survival.commands;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.data.PlayerData;
import veth.vetheon.survival.data.PlayerData.DataType;
import veth.vetheon.survival.managers.PlayerManager;
import veth.vetheon.survival.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerDataCmd implements TabExecutor {

    private final PlayerManager playerManager;

    public PlayerDataCmd(Survival plugin) {
        this.playerManager = plugin.getPlayerManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // command player (add/set/remove) stat amount
        if (args.length != 4) {
            return false;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            Utils.sendColoredMsg(sender, "&cPlayer &b" + args[0] + "&c is not online!");
            return true;
        }
        PlayerData playerData = playerManager.getPlayerData(player);
        if (playerData == null) {
            Utils.sendColoredMsg(sender, "&cInvalid player data for &b" + args[0]);
            return true;
        }

        String changer = args[1];
        if (!changer.equalsIgnoreCase("set") && !changer.equalsIgnoreCase("add") && !changer.equalsIgnoreCase("remove")) {
            return false;
        }

        if (!NumberUtils.isNumber(args[3])) {
            return false;
        }
        double value = Double.parseDouble(args[3]);
        DataType dataType = DataType.getByName(args[2]);
        if (dataType == null) {
            return false;
        }

        // TODO: Replace Switch statement with enhanced "switch" statement
        switch (args[1]) {
            case "add":
                value = value + playerData.getData(dataType);
                break;
            case "remove":
                value = playerData.getData(dataType) - value;
                break;
        }
        playerData.setData(dataType, value);

        return true;
    }

    private final String[] DATA_TYPES = PlayerData.DataType.getNames();
    private final String[] CHANGE = new String[]{"add", "set", "remove"};

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return null; // return player names
        } else if (args.length == 2) {
            List<String> matches = new ArrayList<>();
            for (String name : CHANGE) {
                if (StringUtil.startsWithIgnoreCase(name, args[1])) {
                    matches.add(name);
                }
            }
            return matches;
        } else if (args.length == 3) {
            List<String> matches = new ArrayList<>();
            for (String name : DATA_TYPES) {
                if (StringUtil.startsWithIgnoreCase(name, args[2])) {
                    matches.add(name);
                }
            }
            return matches;
        } else if (args.length == 4) {
            return Collections.singletonList("<amount>");
        }
        return ImmutableList.of(); // Return nothing
    }

}

