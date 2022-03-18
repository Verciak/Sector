package xyz.rokkiitt.sector.objects.teleport;

import java.util.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.level.*;

import java.util.concurrent.*;

public class TeleportManager
{
    static final Map<String, HashMap<String, String>> list;
    
    public static HashMap<String, String> getMap(final Player p) {
        return TeleportManager.list.get(p.getName());
    }
    
    public static void initTeleport(final Player p) {
        if (TeleportManager.list.containsKey(p.getName())) {
            final HashMap<String, String> data = TeleportManager.list.get(p.getName());
                p.teleport(new Location(Integer.parseInt(data.get("x")), Integer.parseInt(data.get("y")), Integer.parseInt(data.get("z"))));
                TeleportManager.list.remove(p.getName());
        }
    }
    
    public static void teleport(final Player p, final int time, final Location loc) {
        if (!CombatManager.isContains(p.getName())) {
            addPlayer(p, System.currentTimeMillis() + Time.SECOND.getTime(time), loc);
            if (p.hasPermission(Perms.TPBYPASS.getPermission())) {
                initTeleport(p);
                removePlayer(p);
            }
        }
        else {
            Util.sendMessage(p, Settings.getMessage("teleportcombat"));
        }
    }
    
    public static void addPlayer(final Player p, final long time, final Location loc) {
        if (isContains(p.getName())) {
            removePlayer(p);
        }
        final HashMap<String, String> id = new HashMap<String, String>();
        id.put("time", String.valueOf(time));
        id.put("x", String.valueOf(loc.getFloorX()));
        id.put("y", String.valueOf(loc.getFloorY()));
        id.put("z", String.valueOf(loc.getFloorZ()));
        id.put("playerdestination", ItemSerializer.serializeLocation(new Location(loc.getX(), loc.getY(), loc.getZ(), p.getYaw(), p.getPitch(), p.getLevel())));
        TeleportManager.list.put(p.getName(), id);
    }
    
    public static void removePlayer(final Player p) {
        TeleportManager.list.remove(p.getName());
    }
    
    public static boolean isContains(final String id) {
        return TeleportManager.list.containsKey(id);
    }
    
    public static Map<String, HashMap<String, String>> getTeleports() {
        return TeleportManager.list;
    }
    
    static {
        list = new ConcurrentHashMap<>();
    }
}
