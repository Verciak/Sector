package xyz.rokkiitt.sector.listeners.guild;

import cn.nukkit.event.entity.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.entity.blockentity.ChestTile;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.stonefarms.Stone;
import xyz.rokkiitt.sector.objects.stonefarms.StoneManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.block.*;
import cn.nukkit.math.*;

import java.util.*;
import cn.nukkit.level.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.*;
import cn.nukkit.event.*;
import java.util.concurrent.*;

public class ExplodeListener implements Listener
{
    private static final Map<Guild, Long> times;
    final List<Integer> dissalowed;
    
    public ExplodeListener() {
        this.dissalowed = Arrays.asList(10, 11, 9, 8, 46, 12, 13, 122, 51, 138, 0, 65, 63, 106, 57, 56, 129, 133, 14, 41, 42, 15, 47, 26, 68, 116, 167, 96);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void EntityExplodeEvent(final EntityExplodeEvent e) {
        if (Config.isSpawn(e.getEntity().getLocation())) {
            e.setCancelled();
            return;
        }
        final List<Block> copy = new ArrayList<Block>(e.getBlockList());
        for (final Block block : e.getBlockList()) {
            final Location loc = block.getLocation();
            final Guild g = GuildManager.getGuild(loc);
            if (g != null) {
                if (loc.getY() > 70.0) {
                    copy.remove(block);
                }
                else {
                    if (block.getId() == 1) {
                        final Stone s = StoneManager.checkStone(block.getLocationHash());
                        if (s != null) {
                            StoneManager.removeStone(block.getLocationHash());
                        }
                    }
                    if (block instanceof BlockWallSign) {
                        final int[] faces = { 3, 2, 5, 4 };
                        final Block behind = block.getLevel().getBlock((Vector3)block.getSide(BlockFace.fromIndex(faces[block.getDamage() - 2])));
                        if (behind.getId() != 54) {
                            continue;
                        }
                        if (g.getCuboid().isInCuboid(behind.getLocation())) {
                            final BlockEntity tile = block.getLevel().getBlockEntity((Vector3)behind);
                            if (!(tile instanceof ChestTile)) {
                                continue;
                            }
                            if (!((ChestTile)tile).isLocked()) {
                                continue;
                            }
                            if (!((ChestTile)tile).getSign().equals((Object)block)) {
                                continue;
                            }
                            ((ChestTile)tile).unlock();
                        }
                    }
                    if (!g.getHeart().isInHeart(loc)) {
                        if ((g.getGuildProtectionTime() <= System.currentTimeMillis() && Settings.ENABLE_TNT) || g.getPenaltytnt() >= System.currentTimeMillis()) {
                            g.setExplodeTime(System.currentTimeMillis() + Time.SECOND.getTime(60));
                            g.setRegenExplodeTime(System.currentTimeMillis() + Time.MINUTE.getTime(30));
                            if (this.dissalowed.contains(block.getId())) {
                                continue;
                            }
                            g.rAddBlock(block);
                        }
                        else {
                            copy.remove(block);
                        }
                    }
                    else {
                        copy.remove(block);
                    }
                }
            }
            else {
                copy.remove(block);
            }
        }
        e.setBlockList((List)copy);
        final Guild gg = GuildManager.getGuild(e.getPosition().getLocation());
        if (gg != null && ((gg.getGuildProtectionTime() <= System.currentTimeMillis() && Settings.ENABLE_TNT) || gg.getPenaltytnt() >= System.currentTimeMillis())) {
            final Long time = ExplodeListener.times.get(gg);
            if (time == null || System.currentTimeMillis() - time >= Time.SECOND.getTime(10)) {
                Util.sendInformation("GUILDCHAT||" + gg.getTag() + "|^|" + Settings.getMessage("guildexplode"));
                ExplodeListener.times.put(gg, System.currentTimeMillis());
            }
            if (gg.isRegen()) {
                for (final User u : UserManager.users) {
                    if (u.getTag().equalsIgnoreCase(gg.getTag())) {
                        final Player pp = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
                        if (pp == null) {
                            continue;
                        }
                    }
                }
                gg.setRegen(false);
            }
            gg.reSizeRegen();
            gg.getRegengui().refresh();
        }
    }
    
    static {
        times = new ConcurrentHashMap<Guild, Long>();
    }
}
