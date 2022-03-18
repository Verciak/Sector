package xyz.rokkiitt.sector.objects.home;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import xyz.rokkiitt.sector.objects.user.User;

import java.util.concurrent.*;
import java.util.*;
import java.util.regex.*;

public class PlayerHomeData
{
    public double x;
    public double y;
    public double z;
    public String name;
    
    public static String serialize(final User u) {
        final Set<String> wp = ConcurrentHashMap.newKeySet();
        for (final PlayerHomeData wd : u.getHomes()) {
            wp.add(wd.name + "|&|" + wd.x + "|&|" + wd.y + "|&|" + wd.z);
        }
        return wp.isEmpty() ? "brak" : JsonStream.serialize(wp);
    }
    
    public static Set<PlayerHomeData> deserialize(final String s) {
        final Set<PlayerHomeData> list = ConcurrentHashMap.newKeySet();
        if (!s.equalsIgnoreCase("brak")) {
            final List<String> deswd = JsonIterator.deserialize(s, List.class);
            for (final String wp : deswd) {
                final String[] split = wp.split(Pattern.quote("|&|"));
                if (split.length >= 5) {
                    final PlayerHomeData wd = new PlayerHomeData();
                    wd.name = split[0];
                    wd.x = Double.parseDouble(split[1]);
                    wd.y = Double.parseDouble(split[2]);
                    wd.z = Double.parseDouble(split[3]);
                    list.add(wd);
                }
            }
        }
        return list;
    }
}
