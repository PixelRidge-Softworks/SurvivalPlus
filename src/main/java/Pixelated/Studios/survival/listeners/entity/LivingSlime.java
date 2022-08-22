package Pixelated.Studios.survival.listeners.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Pixelated.Studios.survival.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import Pixelated.Studios.survival.Survival;

public class LivingSlime implements Listener {

	// TODO: Investigate warning
	private Survival plugin;

	public LivingSlime(Survival plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	private void onGhastTearSlimeBlock(ItemSpawnEvent e) {
		if (e.getEntityType() == EntityType.DROPPED_ITEM) {
			Item i = e.getEntity();
			if (i.getItemStack().getType() == Material.GHAST_TEAR) {
				Bukkit.getScheduler().runTaskLater(plugin, initRunnable(i), 20);
			}
		}
	}

	private Runnable initRunnable(Item i) {
		final Item f_i = i;
		return () -> {
			List<Block> slimeBlocks = new ArrayList<>();
			slimeBlocks.add(f_i.getLocation().add(0, -1, 0).getBlock());
			slimeBlocks.add(f_i.getLocation().add(0, -1, 1).getBlock());
			slimeBlocks.add(f_i.getLocation().add(0, -1, -1).getBlock());
			slimeBlocks.add(f_i.getLocation().add(1, -1, 0).getBlock());
			slimeBlocks.add(f_i.getLocation().add(-1, -1, 0).getBlock());
			slimeBlocks.add(f_i.getLocation().add(0, 0, 1).getBlock());
			slimeBlocks.add(f_i.getLocation().add(0, 0, -1).getBlock());
			slimeBlocks.add(f_i.getLocation().add(1, 0, 0).getBlock());
			slimeBlocks.add(f_i.getLocation().add(-1, 0, 0).getBlock());
			slimeBlocks.add(f_i.getLocation().add(1, 0, 1).getBlock());
			slimeBlocks.add(f_i.getLocation().add(1, 0, -1).getBlock());
			slimeBlocks.add(f_i.getLocation().add(-1, 0, 1).getBlock());
			slimeBlocks.add(f_i.getLocation().add(-1, 0, -1).getBlock());

			ItemStack i_f_i = f_i.getItemStack();
			Iterator<Block> it = slimeBlocks.iterator();
			Block slimeBlock;
			while (it.hasNext()) {
				slimeBlock = it.next();
				if (slimeBlock != null && slimeBlock.getType() == Material.SLIME_BLOCK && f_i.isOnGround()) {
					if (i_f_i.getAmount() > 1)
						i_f_i.setAmount(i_f_i.getAmount() - 1);
					else
						f_i.remove();

					if (i_f_i.getAmount() <= 0)
						f_i.remove();

					slimeBlock.setType(Material.AIR);

					Slime slime = (Slime) f_i.getWorld().spawnEntity(slimeBlock.getLocation(), EntityType.SLIME);
					slime.setSize(2);

					Utils.spawnParticle(slimeBlock.getLocation().add(0.5, 0.5, 0.5), Particle.CLOUD, 20, 0.5, 0.5, 0.5);
					break;
				}
			}
		};
	}

}
