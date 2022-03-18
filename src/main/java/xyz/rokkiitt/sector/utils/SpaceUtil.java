package xyz.rokkiitt.sector.utils;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.guild.Guild;

import java.util.*;
import cn.nukkit.level.*;
import cn.nukkit.block.*;
import cn.nukkit.*;

public class SpaceUtil
{
    public static void CarpetkColor(final Guild g, final int id) {
        final Location c = g.getCuboid().getCenter();
        c.setComponents(c.getX(), (double)(Settings.GUILD_Y - 1), c.getZ());
        for (final Location loc : getSquare(c, 3, 4)) {
            if (loc.getLevelBlock().getId() == 171) {
                loc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 171, id);
            }
        }
    }
    
    public static void HeartDesignStairs(final Guild g, final int m) {
        final Location c = g.getCuboid().getCenter();
        for (final Location loc : getSquare(c, 4, 5)) {
            if (loc.getLevelBlock().getName().toLowerCase().contains("stairs")) {
                loc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), m, loc.getLevelBlock().getDamage());
            }
        }
    }
    
    public static void HeartDesignBlocks(final Guild g, final int m, final int id) {
        final Location c = g.getCuboid().getCenter();
        c.setComponents(c.getX(), (double)(Settings.GUILD_Y + 4), c.getZ());
        for (final Location loc : getCorners(c, 3, 0)) {
            loc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), m, id);
        }
    }
    
    public static void HeartDesignSlab(final Guild g, final int m, final int id1, final int id2) {
        final Location c = g.getCuboid().getCenter();
        final Location c2 = g.getCuboid().getCenter();
        c.setComponents(c.getX(), Settings.GUILD_Y - 1.0, c.getZ());
        c2.setComponents(c2.getX(), Settings.GUILD_Y + 3.0, c2.getZ());
        for (final Location loc : getSquare(c, 3, 3)) {
            if (loc.getLevelBlock().getName().toLowerCase().contains("slab")) {
                loc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), m, id1);
            }
        }
        for (final Location loc : getSquare(c2, 3, 3)) {
            if (loc.getLevelBlock().getName().toLowerCase().contains("slab")) {
                loc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), m, id2);
            }
        }
    }
    
    public static List<Location> getSquare(final Location center, final int radius) {
        final List<Location> locs = new ArrayList<Location>();
        final int cX = center.getFloorX();
        final int cZ = center.getFloorZ();
        final int minX = Math.min(cX + radius, cX - radius);
        final int maxX = Math.max(cX + radius, cX - radius);
        final int minZ = Math.min(cZ + radius, cZ - radius);
        final int maxZ = Math.max(cZ + radius, cZ - radius);
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                locs.add(new Location((double)x, (double)center.getFloorY(), (double)z, center.getLevel()));
            }
        }
        locs.add(center);
        return locs;
    }
    
    private static List<Location> getCorners(final Location center, final int radius) {
        final List<Location> locs = new ArrayList<Location>();
        final int cX = center.getFloorX();
        final int cZ = center.getFloorZ();
        final int minX = Math.min(cX + radius, cX - radius);
        final int maxX = Math.max(cX + radius, cX - radius);
        final int minZ = Math.min(cZ + radius, cZ - radius);
        final int maxZ = Math.max(cZ + radius, cZ - radius);
        locs.add(new Location((double)minX, (double)center.getFloorY(), (double)minZ, center.getLevel()));
        locs.add(new Location((double)maxX, (double)center.getFloorY(), (double)minZ, center.getLevel()));
        locs.add(new Location((double)minX, (double)center.getFloorY(), (double)maxZ, center.getLevel()));
        locs.add(new Location((double)maxX, (double)center.getFloorY(), (double)maxZ, center.getLevel()));
        return locs;
    }
    
    public static Location getCornerX(final Location center, final int radius) {
        final int cX = center.getFloorX();
        final int cZ = center.getFloorZ();
        final int minX = Math.min(cX + radius, cX - radius);
        final int minZ = Math.min(cZ + radius, cZ - radius);
        return new Location((double)minX, 0.0, (double)minZ, center.getLevel());
    }
    
    public static Location getCornerZ(final Location center, final int radius) {
        final int cX = center.getFloorX();
        final int cZ = center.getFloorZ();
        final int minX = Math.min(cX - radius, cX + radius);
        final int minZ = Math.min(cZ - radius, cZ + radius);
        return new Location((double)minX, 256.0, (double)minZ, center.getLevel());
    }
    
    public static List<Location> getWalls(final Location center, final int radius) {
        final List<Location> locs = getSquare(center, radius);
        locs.removeAll(getSquare(center, radius - 1));
        return locs;
    }
    
    public static void setBlock(final Level loc, final int material, final int xdd, final int d, final int y, final int e) {
        final Location xd = new Location((double)d, (double)y, (double)e, loc);
        xd.getLevel().setBlockAt(d, y, e, material, xdd);
    }
    
    public static Block getBlock(final Location loc, final int d, final int y, final int e) {
        final Location xd = new Location((double)d, (double)y, (double)e, Server.getInstance().getLevelByName("world"));
        return xd.getLevelBlock();
    }
    
    public static List<Location> getSquare(final Location center, final int radius, final int height) {
        final List<Location> locs = getSquare(center, radius);
        for (int i = 1; i <= height; ++i) {
            locs.addAll(getSquare(new Location((double)center.getFloorX(), (double)(center.getFloorY() + i), (double)center.getFloorZ(), center.getLevel()), radius));
        }
        return locs;
    }
    
    public static List<Location> getCorners(final Location center, final int radius, final int height) {
        final List<Location> locs = getCorners(center, radius);
        for (int i = 1; i <= height; ++i) {
            locs.addAll(getCorners(new Location((double)center.getFloorX(), (double)(center.getFloorY() + i), (double)center.getFloorZ(), center.getLevel()), radius));
        }
        return locs;
    }
    
    public static List<Location> getWallsOnGround(final Location center, final int radius) {
        final List<Location> locs = new ArrayList<Location>();
        for (final Location l : getWalls(center, radius)) {
            final Block b = l.getLevel().getBlock(l.getFloorX(), l.getLevel().getHighestBlockAt(l.getFloorX(), l.getFloorZ()), l.getFloorZ());
            locs.add(b.getLocation().add(0.0, 1.0, 0.0));
        }
        return locs;
    }
    
    public static List<Location> getWalls(final Location center, final int radius, final int height) {
        final List<Location> locs = getWalls(center, radius);
        for (int i = 1; i <= height; ++i) {
            locs.addAll(getWalls(new Location((double)center.getFloorX(), (double)(center.getFloorY() + i), (double)center.getFloorZ(), center.getLevel()), radius));
        }
        return locs;
    }
}
