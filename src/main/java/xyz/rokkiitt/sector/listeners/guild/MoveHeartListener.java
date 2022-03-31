package xyz.rokkiitt.sector.listeners.guild;

import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.guild.entity.EntityHead;
import xyz.rokkiitt.sector.objects.teleport.TeleportManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import cn.nukkit.*;
import cn.nukkit.event.*;

public class MoveHeartListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(final PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final int xfrom = e.getFrom().getFloorX();
        final int zfrom = e.getFrom().getFloorZ();
        final int yfrom = e.getFrom().getFloorY();
        final int xto = e.getTo().getFloorX();
        final int yto = e.getTo().getFloorY();
        final int zto = e.getTo().getFloorZ();
        if (xfrom != xto || zfrom != zto || yfrom != yto) {
            if (Settings.FREEZE_TIME >= System.currentTimeMillis()) {
                e.setCancelled();
                return;
            }
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                u.updateWaypoints(p);
            }
            TeleportManager.removePlayer(p);
            if (Config.isSpawn(p.getLocation())) {
                return;
            }
            final Guild from = GuildManager.getHeart(e.getFrom());
            final Guild to = GuildManager.getHeart(e.getTo());
            if (from == null && to != null) {
                if (to.getHead() == null) {
                    to.setHead(new EntityHead(to));
                }
                to.getHead().spawnTo(p);
            }
        }
    }
}
