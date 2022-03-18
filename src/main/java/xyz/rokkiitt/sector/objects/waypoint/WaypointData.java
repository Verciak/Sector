package xyz.rokkiitt.sector.objects.waypoint;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import xyz.rokkiitt.sector.objects.user.User;

import java.util.concurrent.*;
import java.util.*;
import java.util.regex.*;

public class WaypointData
{
    public int x;
    public int y;
    public int z;
    public String name;
    public boolean active;
    
    public static String serialize(final User u) {
        final Set<String> wp = ConcurrentHashMap.newKeySet();
        for (final WaypointData wd : u.getWaypoints()) {
            wp.add(wd.name + "|&|" + wd.x + "|&|" + wd.y + "|&|" + wd.z + "|&|" + wd.active);
        }
        return wp.isEmpty() ? "brak" : JsonStream.serialize(wp);
    }
    
    public static Set<WaypointData> deserialize(final String s) {
        final Set<WaypointData> list = ConcurrentHashMap.newKeySet();
        if (!s.equalsIgnoreCase("brak")) {
            final List<String> deswd = JsonIterator.deserialize(s, List.class);
            for (final String wp : deswd) {
                final String[] split = wp.split(Pattern.quote("|&|"));
                if (split.length >= 5) {
                    final WaypointData wd = new WaypointData();
                    wd.name = split[0];
                    wd.x = Integer.parseInt(split[1]);
                    wd.y = Integer.parseInt(split[2]);
                    wd.z = Integer.parseInt(split[3]);
                    wd.active = Boolean.parseBoolean(split[4]);
                    list.add(wd);
                }
            }
        }
        return list;
    }
}
