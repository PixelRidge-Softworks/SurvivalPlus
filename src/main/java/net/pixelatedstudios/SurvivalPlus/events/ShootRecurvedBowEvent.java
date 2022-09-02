package net.pixelatedstudios.SurvivalPlus.events;

import net.pixelatedstudios.SurvivalPlus.item.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player shoots a recurved bow/crossbow
 */
@SuppressWarnings("unused")
public class ShootRecurvedBowEvent extends Event implements Cancellable {

    // TODO: Investigate warning
    private static HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack itemStack;
    private final Item item;
    private boolean isCancelled;

    public ShootRecurvedBowEvent(Player player, ItemStack itemStack, Item item) {
        this.player = player;
        this.itemStack = itemStack;
        this.item = item;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the player that shot the bow in this event
     *
     * @return The player that shot the bow
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the ItemStack the player shoot
     *
     * @return The player's main hand item (will be a bow or crossbow)
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Get the custom Items type the player shoot
     *
     * @return The player's main hand {@link Item} type
     */
    public Item getItem() {
        return this.item;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

}

