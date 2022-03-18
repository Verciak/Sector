package xyz.rokkiitt.sector.objects.water;

import java.util.*;
import cn.nukkit.level.*;
import java.util.concurrent.*;

public class WaterManager
{
    private static Map<Long, FakeWater> waters;
    
    public static void createWater(final Position pos) {
        if (!pos.isValid()) {
            return;
        }
        final long hash = Level.blockHash(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());
        if (!WaterManager.waters.containsKey(hash)) {
            WaterManager.waters.put(hash, new FakeWater(pos.getX(), pos.getY(), pos.getZ(), pos.getLevel()));
        }
    }
    
    public static FakeWater getWater(final Position pos) {
        if (!pos.isValid()) {
            return null;
        }
        final long hash = Level.blockHash(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());
        if (WaterManager.waters.containsKey(hash)) {
            return WaterManager.waters.get(hash);
        }
        return null;
    }
    
    public static void removeWater(final Position pos) {
        final long hash = Level.blockHash(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());
        WaterManager.waters.remove(hash);
    }
    
    public static Map<Long, FakeWater> getWaters() {
        return WaterManager.waters;
    }
    
    static {
        WaterManager.waters = new ConcurrentHashMap<Long, FakeWater>();
    }
}
