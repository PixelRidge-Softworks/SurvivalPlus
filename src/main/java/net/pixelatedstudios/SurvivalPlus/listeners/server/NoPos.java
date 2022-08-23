package net.pixelatedstudios.SurvivalPlus.listeners.server;


import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NoPos implements Listener {
	@EventHandler
	private void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
			disableF3(player);
	}

	@EventHandler
	private void onGamemodeChange(PlayerGameModeChangeEvent e) {
		Player player = e.getPlayer();
		if (e.getNewGameMode() == GameMode.ADVENTURE || e.getNewGameMode() == GameMode.SURVIVAL) {
			disableF3(player);
		} else {
			enableF3(player);
		}
	}

	@EventHandler
	private void onWorldChange(PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) {
			disableF3(player);
		}
	}

	/**
	 * Disable a player's coordinates in their Minecraft Debug screen
	 *
	 * @param player The player to disable coords for
	 */
	@SuppressWarnings("WeakerAccess")
	public static void disableF3(Player player) {
		try {
			EntityPlayer player1 = (EntityPlayer) getHandle(player);
			PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(player1, (byte) 22);
			player1.b.a(packet);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("[SurvivalPlus] " + ChatColor.RED + e.getMessage());
		}
	}

	/**
	 * Enable a player's coordinates in their Minecraft Debug screen
	 *
	 * @param player The player to enable coords for
	 */
	@SuppressWarnings("WeakerAccess")
	public static void enableF3(Player player) {
		try {
			EntityPlayer player1 = (EntityPlayer) getHandle(player);
			PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(player1, (byte) 23);
			player1.b.a(packet);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("[SurvivalPlus] " + ChatColor.RED + e.getMessage());
		}
	}

	private static Object getHandle(Player player)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method getHandle = player.getClass().getMethod("getHandle");
		return getHandle.invoke(player);
	}
}
