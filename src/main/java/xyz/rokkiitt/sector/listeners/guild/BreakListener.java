package xyz.rokkiitt.sector.listeners.guild;

import cn.nukkit.event.block.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.antigrief.AntiGrief;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.entity.blockentity.ChestTile;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.stonefarms.Stone;
import xyz.rokkiitt.sector.objects.stonefarms.StoneManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.item.*;
import cn.nukkit.math.*;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.level.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.event.*;

public class BreakListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(final BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (Settings.FREEZE_TIME >= System.currentTimeMillis()) {
            e.setCancelled(true);
            return;
        }
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        final int m = b.getId();
        final Location l = b.getLocation();
        final Item item = p.getInventory().getItemInHand();
        if (p.hasPermission(Perms.BREAKBYPASS.getPermission())) {
            if (m == 1) {
                final Stone s = StoneManager.checkStone(b.getLocationHash());
                if (s != null) {
                    if (item.getId() == 285) {
                        Util.giveItem(p, PItemsGUI.stoniarka);
                        StoneManager.removeStone(b.getLocationHash());
                    }
                    else {
                        StoneManager.createExcavatedStone(b.getLocationHash(), l);
                    }
                }
            }
            else if (m == 68) {
                final int[] faces = { 3, 2, 5, 4 };
                final Block behind = b.getLevel().getBlock((Vector3)b.getSide(BlockFace.fromIndex(faces[b.getDamage() - 2])));
                if (behind.getId() != 54) {
                    return;
                }
                final BlockEntity tile = b.getLevel().getBlockEntity((Vector3)behind);
                if (!(tile instanceof ChestTile)) {
                    return;
                }
                if (!((ChestTile)tile).isLocked()) {
                    return;
                }
                if (!((ChestTile)tile).getSign().equals((Object)b)) {
                    return;
                }
                ((ChestTile)tile).unlock();
                Util.sendMessage((CommandSender)p, Settings.getMessage("locksucces"));
            }
            else {
            }
        }
        else if (Config.isSpawn(p.getLocation())) {
            if (m == 1) {
                final Stone s = StoneManager.checkStone(b.getLocationHash());
                if (s != null) {
                    StoneManager.createExcavatedStone(b.getLocationHash(), l);
                }
                else {
                    Util.sendMessage((CommandSender)p, Settings.getMessage("breakspawn"));
                    e.setCancelled(true);
                }
            }
            else {
                Util.sendMessage((CommandSender)p, Settings.getMessage("breakspawn"));
                e.setCancelled(true);
            }
        }
        else if (MeteoriteManager.isOpenedListener(l)) {
            e.setCancelled(true);
        }
        else {
            final Guild g = GuildManager.getGuild(l);
            if (g != null) {
                final User u = UserManager.getUser(p.getName());
                if (u != null) {
                    if (u.getTag().equalsIgnoreCase(g.getTag())) {
                        if (g.getHeart().isInHeart(b.getLocation())) {
                            Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("breakheart"));
                            e.setCancelled(true);
                            return;
                        }
                        if (!u.hasPermission("0")) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "niszczenia blokow"));
                            return;
                        }
                        if (m == 138 && !u.hasPermission("15")) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "niszczenia beacona"));
                            return;
                        }
                        if (m == 152 && !u.hasPermission("14")) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "niszczenia blokow redstone"));
                            return;
                        }
                        if (m == 49 && !u.hasPermission("27")) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "niszczenia obsydianu"));
                            return;
                        }
                        if ((m == 54 && !u.hasPermission("3")) || (m == 146 && !u.hasPermission("3"))) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "niszczenia skrzynek"));
                            return;
                        }
                        if (b.canBeActivated() && !u.hasPermission("7")) {
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "interakcji"));
                            return;
                        }
                        if (m == 54 || m == 146) {
                            final BlockEntity tile2 = b.getLevel().getBlockEntity((Vector3)b);
                            if (!(tile2 instanceof ChestTile)) {
                                g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "break", System.currentTimeMillis());
                                return;
                            }
                            if (!((ChestTile)tile2).isLocked()) {
                                g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "break", System.currentTimeMillis());
                                return;
                            }
                            if (!((ChestTile)tile2).getOwner().equalsIgnoreCase(p.getName())) {
                                Util.sendMessage((CommandSender)p, Settings.getMessage("lockcantbreak"));
                                e.setCancelled();
                                return;
                            }
                        }
                        else if (m == 68) {
                            final int[] faces2 = { 3, 2, 5, 4 };
                            final Block behind2 = b.getLevel().getBlock((Vector3)b.getSide(BlockFace.fromIndex(faces2[b.getDamage() - 2])));
                            if (behind2.getId() != 54) {
                                g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "break", System.currentTimeMillis());
                                return;
                            }
                            if (g.getCuboid().isInCuboid(behind2.getLocation())) {
                                final BlockEntity tile3 = b.getLevel().getBlockEntity((Vector3)behind2);
                                if (!(tile3 instanceof ChestTile)) {
                                    g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "break", System.currentTimeMillis());
                                    return;
                                }
                                if (!((ChestTile)tile3).isLocked()) {
                                    g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "break", System.currentTimeMillis());
                                    return;
                                }
                                if (!((ChestTile)tile3).getSign().equals((Object)b)) {
                                    g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "break", System.currentTimeMillis());
                                    return;
                                }
                                if (!((ChestTile)tile3).getOwner().equalsIgnoreCase(p.getName()) && !u.hasPermission("17")) {
                                    Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermissions").replace("{TYPE}", "niszczenia cudzych lockow"));
                                    e.setCancelled();
                                    return;
                                }
                                ((ChestTile)tile3).unlock();
                                Util.sendMessage((CommandSender)p, Settings.getMessage("locksucces"));
                            }
                        }
                        else if (m == 1) {
                            final Stone s2 = StoneManager.checkStone(b.getLocationHash());
                            if (s2 != null) {
                                if (item.getId() == 285) {
                                    Util.giveItem(p, PItemsGUI.stoniarka);
                                    StoneManager.removeStone(b.getLocationHash());
                                }
                                else {
                                    StoneManager.createExcavatedStone(b.getLocationHash(), l);
                                }
                            }
                        }
                        g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "break", System.currentTimeMillis());
                    }
                    else {
                        e.setCancelled(true);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildenemy"));
                    }
                }
                else {
                    e.setCancelled(true);
                    p.kick(Util.fixColor("&9 not properly loaded user data - BlockBreak"));
                }
            }
            else {
                final Stone s3 = StoneManager.checkStone(b);
                if (s3 != null) {
                    if (item.getId() == 285) {
                        Util.giveItem(p, PItemsGUI.stoniarka);
                        StoneManager.removeStone(b.getLocationHash());
                    }
                    else {
                        StoneManager.createExcavatedStone(b.getLocationHash(), l);
                    }
                }
                else {
                    if (l.getFloorY() > 70) {
                        AntiGrief.removeBlock(b);
                    }
                }
            }
        }
    }
}
