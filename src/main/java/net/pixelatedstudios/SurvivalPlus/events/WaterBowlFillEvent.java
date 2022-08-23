package net.pixelatedstudios.SurvivalPlus.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player fills a water bowl
 */
@SuppressWarnings("unused")
public class WaterBowlFillEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final ItemStack item;
	private boolean isCancelled;

	public WaterBowlFillEvent(ItemStack item) {
		this.item = item;
		this.isCancelled = false;
	}

	/** Get the bowl the player filled
	 * @return The bowl
	 */
	public ItemStack getWaterBowl() {
		return this.item;
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}

}
