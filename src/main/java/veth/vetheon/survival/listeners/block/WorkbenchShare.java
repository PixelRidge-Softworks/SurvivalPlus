package veth.vetheon.survival.listeners.block;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import veth.vetheon.survival.Survival;

import java.util.*;

public class WorkbenchShare implements Listener {

	// TODO: Investigate warning
	private Survival plugin;
	
	public WorkbenchShare(Survival plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	@SuppressWarnings("deprecation")
	private void onPlayerInteract(PlayerInteractEvent e) {
		if (e.isCancelled()) return;
		final Player p = e.getPlayer();
		final Block block = e.getClickedBlock();

		if (block == null || block.getType() != Material.CRAFTING_TABLE) return;

		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		Bukkit.getServer().getScheduler().runTask(plugin, () -> {
			if (!p.isOnline())
				return;

			if (!block.hasMetadata("shared_players"))
				block.setMetadata("shared_players", new FixedMetadataValue(plugin, new ArrayList<UUID>()));

			// TODO: Investigate warning about unchecked cast
			final List<UUID> list = (block.getMetadata("shared_players").get(0).value() instanceof List<?>) ? (List<UUID>) block.getMetadata("shared_players").get(0).value() : new ArrayList<>();

			final Inventory open = p.getOpenInventory().getTopInventory();

			if (open.getType() != InventoryType.WORKBENCH)
				return;

			// Workaround to get the accessed WorkBench
			final Block workbench = p.getTargetBlock(ImmutableSet.of(Material.GRASS, Material.SNOW, Material.AIR), 8);

			if (workbench.getType() != Material.CRAFTING_TABLE) {
				// Close Inventory if player managed to access the workbench without actually use one.
				p.closeInventory();
				return;
			}

			assert list != null;
			list.add(p.getUniqueId());
			p.setMetadata("shared_workbench", new FixedMetadataValue(plugin, block));

			Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
				if (list.isEmpty())
					return;
				Player first = Bukkit.getPlayer(list.get(0));
				assert first != null;
				Inventory pInv = first.getOpenInventory().getTopInventory();
				if (pInv.getType() != InventoryType.WORKBENCH)
					return;
				open.setContents(pInv.getContents());
				Bukkit.getServer().getScheduler().runTaskLater(plugin, p::updateInventory, 1);
			}, 1);
		});

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInventoryClick(InventoryClickEvent e) {
		onInventoryInteract(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInventoryDrag(InventoryDragEvent e) {
		onInventoryInteract(e);
	}

	private void onInventoryInteract(InventoryInteractEvent e) {
		if (e.isCancelled()) return;
		if (!(e.getWhoClicked() instanceof Player))
			return;

		final Player p = (Player) e.getWhoClicked();

		if (!p.hasMetadata("shared_workbench"))
			return;

		if (e.getInventory().getType() == InventoryType.WORKBENCH) {
			// Workaround to get the accessed WorkBench
			final Block workbench = (p.getMetadata("shared_workbench").get(0).value() instanceof Block) ? (Block) p.getMetadata("shared_workbench").get(0).value() : null;

			assert workbench != null;
			if (!workbench.hasMetadata("shared_players") || workbench.getType() != Material.CRAFTING_TABLE) {
				// TODO: Investigate warning about getTopInventory()
				if (p.getOpenInventory().getTopInventory() != null)
					p.getOpenInventory().getTopInventory().clear();
				p.closeInventory();
				p.removeMetadata("shared_workbench", plugin);
				return;
			}

			// TODO: Investigate warning about unchecked cast
			List<UUID> list = (workbench.getMetadata("shared_players").get(0).value() instanceof List<?>) ? (List<UUID>) workbench.getMetadata("shared_players").get(0).value() : new ArrayList<UUID>();

			final Inventory pInv = p.getOpenInventory().getTopInventory();
			if (pInv.getType() != InventoryType.WORKBENCH) {
				workbench.removeMetadata("shared_players", plugin);
				return;
			}

			assert list != null;
			Iterator<UUID> iterator = list.iterator();
			while (iterator.hasNext()) {
				UUID next = iterator.next();

				if (p.getUniqueId().equals(next))
					continue;

				final Player idPlayer = Bukkit.getPlayer(next);

				if (idPlayer == null || !idPlayer.isOnline()) {
					iterator.remove();
					continue;
				}

				final Inventory open = idPlayer.getOpenInventory().getTopInventory();

				if (open.getType() != InventoryType.WORKBENCH) {
					// Close Inventory if player managed to access the workbench without actually use one.
					iterator.remove();
					p.closeInventory();
					continue;
				}

				Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
					open.setContents(pInv.getContents());
					Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
						p.updateInventory();
						idPlayer.updateInventory();
					}, 1);
				}, 1);
			}
		}
	}

	@EventHandler
	private void onInventoryClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player))
			return;
		final Player p = (Player) e.getPlayer();

		if (!p.hasMetadata("shared_workbench"))
			return;
		if (e.getInventory().getType() == InventoryType.WORKBENCH) {
			// Workaround to get the accessed WorkBench
			// TODO: Investigate warning
			final Block workbench = p.getTargetBlock((Set<Material>) null, 8);

			if (!workbench.hasMetadata("shared_players") || workbench.getType() != Material.CRAFTING_TABLE) {
				p.getOpenInventory().getTopInventory();
				p.getOpenInventory().getTopInventory().clear();
				p.removeMetadata("shared_workbench", plugin);

				return;
			}

			// TODO: Investigate warning about unchecked cast
			List<UUID> list = (workbench.getMetadata("shared_players").get(0).value() instanceof List<?>) ? (List<UUID>) workbench.getMetadata("shared_players").get(0).value() : new ArrayList<UUID>();

			assert list != null;
			list.remove(p.getUniqueId());

			if (list.isEmpty())
				workbench.removeMetadata("shared_players", plugin);
			else {
				e.getInventory().clear();
				workbench.setMetadata("shared_players", new FixedMetadataValue(plugin, list));
			}
		}
	}

	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent e) {
		final Player p = e.getPlayer();

		if (!p.hasMetadata("shared_workbench"))
			return;

		Block workbench = (p.getMetadata("shared_workbench").get(0).value() instanceof Block) ? (Block) p.getMetadata("shared_workbench").get(0).value() : null;

		if (workbench != null && workbench.hasMetadata("shared_players") && workbench.getType() == Material.CRAFTING_TABLE) {
			// TODO: Investigate warning about unchecked cast
			List<UUID> list = (workbench.getMetadata("shared_players").get(0).value() instanceof List<?>) ? (List<UUID>) workbench.getMetadata("shared_players").get(0).value() : new ArrayList<UUID>();

			assert list != null;
			list.remove(p.getUniqueId());

			if (list.isEmpty())
				workbench.removeMetadata("shared_players", plugin);
			else
				workbench.setMetadata("shared_players", new FixedMetadataValue(plugin, list));
		}

		p.removeMetadata("shared_workbench", plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBreakWorkbench(BlockBreakEvent e) {
		if (e.isCancelled()) return;
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		Block workbench = e.getBlock();

		if (!workbench.hasMetadata("shared_players") || workbench.getType() != Material.CRAFTING_TABLE)
			return;

		// TODO: Investigate warning about unchecked cast
		List<UUID> list = (workbench.getMetadata("shared_players").get(0).value() instanceof List<?>) ? (List<UUID>) workbench.getMetadata("shared_players").get(0).value() : new ArrayList<UUID>();

		assert list != null;
		Iterator<UUID> iterator = list.iterator();

		Inventory sharedInventory = Bukkit.createInventory(null, InventoryType.WORKBENCH);

		while (iterator.hasNext()) {
			UUID next = iterator.next();

			iterator.remove();

			final Player idPlayer = Bukkit.getPlayer(next);

			if (idPlayer != null) {
				idPlayer.removeMetadata("shared_inv", plugin);

				if (idPlayer.isOnline()) {
					final Inventory open = idPlayer.getOpenInventory().getTopInventory();

					if (open.getType() == InventoryType.WORKBENCH) {
						sharedInventory.setContents(open.getContents());
						open.clear();
						idPlayer.closeInventory();
					}
				}
			}
		}

		for (int i = 1; i < sharedInventory.getSize(); i++) {
			ItemStack item = sharedInventory.getItem(i);
			if (item != null)
				workbench.getWorld().dropItem(workbench.getLocation(), item);
		}


		workbench.removeMetadata("shared_players", plugin);
	}
}