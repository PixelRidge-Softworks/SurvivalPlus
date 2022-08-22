package veth.vetheon.survival.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player's energy level changes
 */
@SuppressWarnings("unused")
public class EnergyLevelChangeEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final double changed;
	private final double level;
	private boolean isCancelled;

	public EnergyLevelChangeEvent(Player player, double changed, double level) {
		this.player = player;
		this.changed = changed;
		this.level = level;
		this.isCancelled = false;
	}

	/** Get the player involved in this event
	 * @return The player involved in this event
	 */
	public Player getPlayer() {
		return this.player;
	}

	/** Get the amount that changed in this event
	 * @return The amount that changed
	 */
	public double getChanged() {
		return this.changed;
	}

	/** Get the new energy level of the player
	 * @return The energy level of the player
	 */
	public double getEnergyLevel() {
		return this.level;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	// TODO: Investigate warning
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}

}
