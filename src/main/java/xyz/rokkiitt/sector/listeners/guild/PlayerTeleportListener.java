package xyz.rokkiitt.sector.listeners.guild;

import java.util.*;
import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.guild.entity.EntityHead;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.*;
import cn.nukkit.event.*;
import java.util.concurrent.*;

public class PlayerTeleportListener implements Listener
{
    private static final Map<Guild, Long> times;
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
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
            if (Config.isSpawn(p.getLocation())) {
                return;
            }
            final Guild from = GuildManager.getGuild(e.getFrom());
            final Guild to = GuildManager.getGuild(e.getTo());
            if (from != null) {
                if (from.getHeart().isInHeart(e.getFrom()) && from.getHead() != null) {
                    from.getHead().despawnFrom(p);
                    if (from.getHead().getViewers().isEmpty()) {
                        from.getHead().close();
                        from.setHead(null);
                    }
                }
                if (to == null) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null) {
                        if (from.getTag().equalsIgnoreCase(u.getTag())) {
                            p.sendTip(Util.fixColor("&aOpuszczasz teren swojej gildii &3" + from.getTag().toUpperCase() + " &a!"));
                        }
                        else if (u.getAlliances().contains(from.getTag().toLowerCase())) {
                            p.sendTip(Util.fixColor("&bOpuszczasz teren sojuszniczej gildii &3" + from.getTag().toUpperCase() + " &b!"));
                        }
                        else {
                            p.sendTip(Util.fixColor("&cOpuszczasz teren wrogiej gildii &3" + from.getTag().toUpperCase() + " &c!"));
                        }
                    }
                    else {
                        p.kick(Util.fixColor("&9 not properly loaded data - MoveGuild"));
                    }
                }
            }
            if (to != null) {
                if (to.getHeart().isInHeart(e.getTo())) {
                    if (to.getHead() == null) {
                        to.setHead(new EntityHead(to));
                    }
                    to.getHead().spawnTo(p);
                }
                if (to != from) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null) {
                        if (to.getTag().equalsIgnoreCase(u.getTag())) {
                            p.sendTip(Util.fixColor("&aWkroczyles na teren swojej gildii &3" + to.getTag().toUpperCase() + " &a!"));
                        }
                        else if (u.getAlliances().contains(to.getTag().toLowerCase())) {
                            p.sendTip(Util.fixColor("&bWkroczyles na teren sojuszniczej gildii &3" + to.getTag().toUpperCase() + " &b!"));
                        }
                        else {
                            p.sendTip(Util.fixColor("&cWkroczyles na teren wrogiej gildii &3" + to.getTag().toUpperCase() + " &c!"));
                            if (!p.hasPermission("core.admin.gnotify")) {
                                final Long time = PlayerTeleportListener.times.get(to);
                                if (time == null || System.currentTimeMillis() - time >= Time.SECOND.getTime(10)) {
                                    Util.sendInformation("GUILDCHAT||" + to.getTag() + "|^|" + Settings.getMessage("guildenemyjoin"));
                                    PlayerTeleportListener.times.put(to, System.currentTimeMillis());
                                }
                            }
                        }
                    }
                    else {
                        p.kick(Util.fixColor("&9 not properly loaded data - MoveGuild"));
                    }
                }
            }
        }
    }
    
    static {
        times = new ConcurrentHashMap<Guild, Long>();
    }
}
