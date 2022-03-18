package xyz.rokkiitt.sector.objects.antigrief;

import java.util.concurrent.*;
import cn.nukkit.block.*;
import xyz.rokkiitt.sector.utils.Time;

import java.util.*;

public class AntiGrief
{
    private static Map<String, ConcurrentHashMap<String, String>> grief;
    private static List<Integer> ignored;
    
    public static Map<String, ConcurrentHashMap<String, String>> getGrief() {
        return AntiGrief.grief;
    }
    
    public static boolean removeBlock(final Block b) {
        final ConcurrentHashMap<String, String> h = AntiGrief.grief.get(b.getLocationHash());
        if (h != null) {
            AntiGrief.grief.remove(b.getLocationHash());
        }
        return !AntiGrief.ignored.contains(b.getId());
    }
    
    public static boolean AddBlock(final Block b) {
        if (!AntiGrief.ignored.contains(b.getId()) && !(b instanceof BlockCactus)) {
            final ConcurrentHashMap<String, String> id = new ConcurrentHashMap<String, String>();
            id.put("world", b.getLevel().getName());
            id.put("x", String.valueOf(b.getLocation().getFloorX()));
            id.put("y", String.valueOf(b.getLocation().getFloorY()));
            id.put("z", String.valueOf(b.getLocation().getFloorZ()));
            id.put("time", String.valueOf(System.currentTimeMillis() + Time.MINUTE.getTime(5)));
            final ConcurrentHashMap<String, String> h = AntiGrief.grief.get(b.getLocationHash());
            if (h != null) {
                AntiGrief.grief.replace(b.getLocationHash(), id);
            }
            else {
                AntiGrief.grief.put(b.getLocationHash(), id);
            }
            return true;
        }
        return false;
    }
    
    static {
        AntiGrief.grief = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
        AntiGrief.ignored = Arrays.asList(116, 130, 47, 58, 54, 146);
    }
}
