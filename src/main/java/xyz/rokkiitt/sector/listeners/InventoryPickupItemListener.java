package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.inventory.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.inventory.*;
import cn.nukkit.event.*;

public class InventoryPickupItemListener implements Listener
{
    @EventHandler
    public void onPickupItem(final InventoryPickupItemEvent e) {
        final Inventory inv = e.getInventory();
        if (inv instanceof PlayerInventory && e.getItem() != null) {
            final InventoryHolder pp = inv.getHolder();
            if (pp instanceof Player) {
                if (Cooldown.getInstance().has(((Player)pp).getName(), "dead")) {
                    e.setCancelled();
                    return;
                }
                final User u = UserManager.getUser(((Player)pp).getName());
                if (u == null) {
                    e.setCancelled();
                    ((Player)pp).kick("pickup item null data");
                    return;
                }
                if (u.isVanish()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
