package xyz.rokkiitt.sector.tasks;

import xyz.rokkiitt.sector.objects.antigrief.AntiGrief;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.stonefarms.ExcavatedStone;
import xyz.rokkiitt.sector.objects.stonefarms.StoneManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.water.FakeWater;
import xyz.rokkiitt.sector.objects.water.WaterManager;
import cn.nukkit.math.*;
import cn.nukkit.level.*;

import java.util.concurrent.*;
import cn.nukkit.block.*;
import java.util.*;
import cn.nukkit.*;

public class ThreeTickTask implements Runnable
{
    @Override
    public void run() {
        for (final Map.Entry<Long, FakeWater> entry : WaterManager.getWaters().entrySet()) {
            if (!entry.getValue().canBeRemoved()) {
                continue;
            }
            if (entry.getValue().getLevel().getBlock((Vector3)entry.getValue()).getId() == 9 || entry.getValue().getLevel().getBlock((Vector3)entry.getValue()).getId() == 8) {
                entry.getValue().getLocation().getLevel().setBlockAt(entry.getValue().getLocation().getFloorX(), entry.getValue().getLocation().getFloorY(), entry.getValue().getLocation().getFloorZ(), 0);
            }
            WaterManager.removeWater(entry.getValue());
        }
        for (final Guild g : GuildManager.guilds) {
            if (g.isRegen()) {
                if (g.getRegenDoneSize() >= 50) {
                    final Guild guild = g;
                    g.setGuildBalance(g.getGuildBalance() - 1);
                    g.clearRegenDoneBlock();
                    g.getRegengui().refresh();
                }
                if (g.getGuildBalance() > 0) {
                    final int y = g.rgetY();
                    if (y <= 256) {
                        if (g.reSizeRegen() > 0) {
                            final Map<String, Integer> b = g.getblocks(y);
                            if (b != null) {
                                final Location loc = new Location((double)b.get("x"), (double)b.get("y"), (double)b.get("z"), g.getCuboid().getCenter().getLevel());
                                final Block v = loc.getLevelBlock();
                                if (v.getId() == 0 || !v.isSolid()) {
                                    loc.getLevel().addSound(v.getLocation(), Sound.RANDOM_ORB, 0.1f, 0.111f);
                                    v.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), (int)b.get("id"), (int)b.get("meta"));
                                    g.addRegenDone();
                                    g.rremoveBlock(y);
                                    for (final User u : UserManager.users) {
                                        if (u.getTag().equalsIgnoreCase(g.getTag())) {
                                            final Player pp = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
                                            if (pp == null) {
                                                continue;
                                            }
                                        }
                                    }
                                }
                                else {
                                    g.addRegenMissedBlock();
                                    g.rremoveBlock(y);
                                    for (final User u : UserManager.users) {
                                        if (u.getTag().equalsIgnoreCase(g.getTag())) {
                                            final Player pp = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
                                            if (pp == null) {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                g.removeKey(y);
                                g.regenSort(y + 1);
                                g.rsetY(y + 1);
                                for (final User u2 : UserManager.users) {
                                    if (u2.getTag().equalsIgnoreCase(g.getTag())) {
                                        final Player pp2 = Server.getInstance().getPlayerExact(u2.getNickname().toLowerCase());
                                        if (pp2 == null) {
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            for (final User u3 : UserManager.users) {
                                if (u3.getTag().equalsIgnoreCase(g.getTag())) {
                                    final Player pp3 = Server.getInstance().getPlayerExact(u3.getNickname().toLowerCase());
                                    if (pp3 == null) {
                                        continue;
                                    }
                                }
                            }
                            g.regenClearBlocks();
                            g.setRegen(false);
                            g.getRegengui().refresh();
                        }
                    }
                    else {
                        g.rsetY(0);
                    }
                }
                else {
                    for (final User u4 : UserManager.users) {
                        if (u4.getTag().equalsIgnoreCase(g.getTag())) {
                            final Player pp4 = Server.getInstance().getPlayerExact(u4.getNickname().toLowerCase());
                            if (pp4 == null) {
                                continue;
                            }
                        }
                    }
                    g.setRegen(false);
                    g.getRegengui().refresh();
                }
            }
        }
        for (final ExcavatedStone s : StoneManager.getExcavatedStones().values()) {
            if (System.currentTimeMillis() >= s.getTime() + 1500L) {
                s.getLocation().getLevel().setBlockAt(s.getLocation().getFloorX(), s.getLocation().getFloorY(), s.getLocation().getFloorZ(), 1);
                StoneManager.removeExcavatedStone(s.getHash());
            }
        }
        for (final ConcurrentHashMap<String, String> s2 : AntiGrief.getGrief().values()) {
            if (Long.parseLong(s2.get("time")) <= System.currentTimeMillis()) {
                final Location loc2 = new Location(Integer.parseInt(s2.get("x")), Integer.parseInt(s2.get("y")), Integer.parseInt(s2.get("z")), Server.getInstance().getLevelByName((String)s2.get("world")));
                final Block v2 = loc2.getLevelBlock();
                if (!AntiGrief.removeBlock(v2)) {
                    continue;
                }
                loc2.getLevel().setBlock(loc2, Block.get(0));
            }
        }
    }
}
