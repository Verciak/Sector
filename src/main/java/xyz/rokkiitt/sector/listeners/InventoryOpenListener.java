package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.inventory.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.inventory.*;
import cn.nukkit.*;
import cn.nukkit.event.*;

public class InventoryOpenListener implements Listener
{
    @EventHandler
    public void onOpen(final InventoryOpenEvent e) {
        final Inventory inv = e.getInventory();
        final Player p = e.getPlayer();
        if (Cooldown.getInstance().has(p, "restart")) {
            e.setCancelled(true);
            return;
        }
        if (inv instanceof ChestInventory || inv instanceof DoubleChestInventory) {
            final User u = UserManager.getUser(p.getName());
            if (u != null && !p.hasPermission(Perms.CHESTSEE.getPermission())) {
                final Guild g = GuildManager.getGuild(p.getLocation());
                if (g != null && !g.getTag().equalsIgnoreCase(u.getTag()) && u.getOnlineoverall() / 2 < 1800) {
                    Util.sendMessage(p, Settings.getMessage("openonline"));
                    e.setCancelled(true);
                }
            }
        }
    }
}
