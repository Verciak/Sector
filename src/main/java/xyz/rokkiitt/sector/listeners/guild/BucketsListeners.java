package xyz.rokkiitt.sector.listeners.guild;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.water.FakeWater;
import xyz.rokkiitt.sector.objects.water.WaterManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.level.*;
import cn.nukkit.item.*;
import cn.nukkit.event.*;
import cn.nukkit.event.player.*;

public class BucketsListeners implements Listener
{
    @EventHandler(priority = EventPriority.LOW)
    public void onBucketEmpty(final PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final Block b = e.getBlockClicked();
        if (e.getBlockClicked() != null && e.getBlockFace() != null && b != null) {
            final Location l = b.getLocation();
            if (p.hasPermission(Perms.BUCKETBYPASS.getPermission())) {
                return;
            }
            if (Config.isSpawn(p.getLocation())) {
                Util.sendMessage((CommandSender)p, Settings.getMessage("bucketemptyspawn"));
                e.setCancelled(true);
                return;
            }
            else {
                if (MeteoriteManager.isOpenedListener(l)) {
                    e.setCancelled(true);
                    return;
                }
                final Guild g = GuildManager.getGuild(l);
                if (g != null) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null) {
                        if (u.getTag().equalsIgnoreCase(g.getTag())) {
                            if (g.getHeart().isInHeart(e.getBlockClicked().getSide(e.getBlockFace()).getLocation())) {
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("bucketemptyheart"));
                                return;
                            }
                            final Item bucket = e.getBucket();
                            if (bucket.getName().toString().toUpperCase().contains("LAVA") && !u.hasPermission("6")) {
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "lavy"));
                                return;
                            }
                            if (bucket.getName().toString().toUpperCase().contains("WATER") && !u.hasPermission("5")) {
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "wody"));
                                return;
                            }
                            g.addLogblock(p.getName(), b.getLocationHash(), "wylal " + bucket.getName().replace("Bucket", "").toUpperCase(), "liquid", System.currentTimeMillis());
                        }
                        else if (!g.getHeart().isInHeart(e.getBlockClicked().getSide(e.getBlockFace()).getLocation())) {
                            if (e.getBucket().getName().toString().toUpperCase().contains("WATER")) {
                                WaterManager.createWater((Position)l);
                            }
                            else {
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("bucketemptyenemy"));
                            }
                        }
                    }
                    else {
                        e.setCancelled(true);
                        p.kick("&9 not properly loaded data - buckets");
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onBucketFill(final PlayerBucketFillEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final Block b = e.getBlockClicked().getSide(e.getBlockFace().getOpposite());
        if (b != null) {
            final Location l = b.getLocation();
            if (p.hasPermission(Perms.BUCKETBYPASS.getPermission())) {
                return;
            }
            if (Config.isSpawn(p.getLocation())) {
                Util.sendMessage((CommandSender)p, Settings.getMessage("bucketfillspawn"));
                e.setCancelled(true);
                return;
            }
            else {
                if (MeteoriteManager.isOpenedListener(l)) {
                    e.setCancelled(true);
                    return;
                }
                final Guild g = GuildManager.getGuild(l);
                if (g != null) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null) {
                        if (u.getTag().equalsIgnoreCase(g.getTag())) {
                            if (g.getHeart().isInHeart(l)) {
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("bucketfillheart"));
                                return;
                            }
                            g.addLogblock(p.getName(), b.getLocationHash(), "zabral " + b.getName().toUpperCase(), "liquid", System.currentTimeMillis());
                        }
                        else {
                            final FakeWater water = WaterManager.getWater((Position)l);
                            if (water == null) {
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("bucketfillenemy"));
                            }
                            else {
                                WaterManager.removeWater((Position)l);
                            }
                        }
                    }
                    else {
                        e.setCancelled(true);
                        p.kick("&9 not properly loaded data - buckets");
                    }
                }
            }
        }
    }
}
