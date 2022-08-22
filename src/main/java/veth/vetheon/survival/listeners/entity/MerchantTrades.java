package veth.vetheon.survival.listeners.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Merchant;
import veth.vetheon.survival.Survival;
import veth.vetheon.survival.managers.MerchantManager;

public class MerchantTrades implements Listener {

    // TODO: Investigate warning
    private MerchantManager merchantManager;

    public MerchantTrades(Survival plugin) {
        this.merchantManager = plugin.getMerchantManager();
    }

    @EventHandler
    private void onClickVillager(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Merchant) {
            this.merchantManager.updateRecipes(entity);
        }
    }

}
