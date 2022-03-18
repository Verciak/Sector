package xyz.rokkiitt.sector.objects.entity.utils;

import java.util.*;
import cn.nukkit.entity.*;
import cn.nukkit.level.*;
import cn.nukkit.math.*;
import cn.nukkit.block.*;

public class Utils
{
    public static final SplittableRandom random;
    public static final int ACCORDING_X_OBTAIN_Y = 0;
    public static final int ACCORDING_Y_OBTAIN_X = 1;
    
    public static int rand(final int min, final int max) {
        if (min == max) {
            return max;
        }
        return Utils.random.nextInt(max + 1 - min) + min;
    }
    
    public static double rand(final double min, final double max) {
        if (min == max) {
            return max;
        }
        return min + Math.random() * (max - min);
    }
    
    public static float rand(final float min, final float max) {
        if (min == max) {
            return max;
        }
        return min + (float)Math.random() * (max - min);
    }
    
    public static boolean rand() {
        return Utils.random.nextBoolean();
    }
    
    public static double calLinearFunction(final Vector3 pos1, final Vector3 pos2, final double element, final int type) {
        if (pos1.getFloorY() != pos2.getFloorY()) {
            return Double.MAX_VALUE;
        }
        if (pos1.getX() == pos2.getX()) {
            if (type == 1) {
                return pos1.getX();
            }
            return Double.MAX_VALUE;
        }
        else if (pos1.getZ() == pos2.getZ()) {
            if (type == 0) {
                return pos1.getZ();
            }
            return Double.MAX_VALUE;
        }
        else {
            if (type == 0) {
                return (element - pos1.getX()) * (pos1.getZ() - pos2.getZ()) / (pos1.getX() - pos2.getX()) + pos1.getZ();
            }
            return (element - pos1.getZ()) * (pos1.getX() - pos2.getX()) / (pos1.getZ() - pos2.getZ()) + pos1.getX();
        }
    }
    
    public static boolean entityInsideWaterFast(final Entity ent) {
        final double y = ent.y + ent.getEyeHeight();
        final int b = ent.level.getBlockIdAt(NukkitMath.floorDouble(ent.x), NukkitMath.floorDouble(y), NukkitMath.floorDouble(ent.z));
        return b == 8 || b == 9;
    }
    
    public static boolean hasCollisionBlocks(final Level level, final AxisAlignedBB bb) {
        final int minX = NukkitMath.floorDouble(bb.getMinX());
        final int minY = NukkitMath.floorDouble(bb.getMinY());
        final int minZ = NukkitMath.floorDouble(bb.getMinZ());
        final int maxX = NukkitMath.ceilDouble(bb.getMaxX());
        final int maxY = NukkitMath.ceilDouble(bb.getMaxY());
        for (int maxZ = NukkitMath.ceilDouble(bb.getMaxZ()), z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    final Block block = level.getBlock(x, y, z, false);
                    if (block != null && block.getId() != 0 && block.collidesWithBB(bb)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static {
        random = new SplittableRandom();
    }
}
