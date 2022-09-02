package net.pixelatedstudios.SurvivalPlus.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player's thirst level changes
 */
@SuppressWarnings("unused")
public class ThirstLevelChangeEvent extends Event implements Cancellable {

    private final static HandlerList handlers = new HandlerList();
    private final Player player;
    private final int thirst;
    private final int changed;
    private boolean isCancelled;

    public ThirstLevelChangeEvent(Player player, int changed, int thirst) {
        this.player = player;
        this.changed = changed;
        this.thirst = thirst;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the player involved in this event
     *
     * @return The player involved in this event
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the new thirst level from the event
     *
     * @return The new thirst level from the event
     */
    public int getThirst() {
        return this.thirst;
    }

    /**
     * Get the level of thirst that was changed
     *
     * @return The level that was changed
     */
    public int getChanged() {
        return this.changed;
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
