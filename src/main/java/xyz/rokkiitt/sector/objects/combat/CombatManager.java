package xyz.rokkiitt.sector.objects.combat;

import java.util.*;
import cn.nukkit.*;
import java.util.concurrent.*;

public class CombatManager
{
    static final Map<String, Long> list;
    
    public static void addPlayer(final Player p, final long time) {
        CombatManager.list.put(p.getName(), time);
    }
    
    public static void removePlayer(final Player p) {
        CombatManager.list.remove(p.getName());
    }
    
    public static Long getTime(final Player p) {
        return CombatManager.list.get(p.getName());
    }
    
    public static boolean isContains(final String id) {
        return CombatManager.list.containsKey(id);
    }
    
    public static Map<String, Long> getCombats() {
        return CombatManager.list;
    }
    
    public static void clear() {
        CombatManager.list.clear();
    }
    
    static {
        list = new ConcurrentHashMap<String, Long>();
    }
}
