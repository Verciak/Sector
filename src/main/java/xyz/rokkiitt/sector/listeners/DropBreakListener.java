package xyz.rokkiitt.sector.listeners;

import xyz.rokkiitt.sector.objects.drop.DropManager;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.event.*;
import cn.nukkit.event.block.*;
import java.util.*;

public class DropBreakListener implements Listener
{
    static List<Integer> ore;
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(final BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        if (p.getGamemode() == 0) {
            final User u = UserManager.getUser(p.getName());
            if (u == null) {
                e.setCancelled(true);
                p.kick(Util.fixColor("&9not properly loaded user data - DropBreak"));
                return;
            }
            if (b.getId() == 1) {
                e.setDrops(new Item[0]);
                DropManager.getDrop(p, u, p.getInventory().getItemInHand());
                PandoraManager.getDrop(p, u);
                final User user = u;
                user.setBroken(u.getBroken() + 1);
                p.addExperience(3);
            }
            else if (DropBreakListener.ore.contains(b.getId())) {
                final User user2 = u;
                user2.setBroken(u.getBroken() + 1);
                e.setDrops(new Item[0]);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(final BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        if (p.getGamemode() == 0) {
            final User u = UserManager.getUser(p.getName());
            if (u == null) {
                e.setCancelled(true);
                p.kick(Util.fixColor("&9not properly loaded user data - BlockPlaceStat"));
                return;
            }
            final User user = u;
            user.setPlaced(u.getPlaced() + 1);
        }
    }
    
    static {
        DropBreakListener.ore = Arrays.asList(16, 56, 129, 74, 14, 15, 21, 73);
    }
}
