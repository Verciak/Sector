package xyz.rokkiitt.sector.objects.stonefarms;

import cn.nukkit.level.*;
import cn.nukkit.block.*;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.config.Config;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class StoneManager
{
    private static Map<String, Stone> st;
    private static Map<String, ExcavatedStone> es;
    
    public static void createExcavatedStone(final String s, final Location l) {
        if (!StoneManager.es.containsKey(s)) {
            StoneManager.es.put(s, new ExcavatedStone(s, l));
        }
    }
    
    public static Map<String, ExcavatedStone> getExcavatedStones() {
        return StoneManager.es;
    }
    
    public static Map<String, Stone> getStones() {
        return StoneManager.st;
    }
    
    public static Stone checkStone(final String s) {
        return StoneManager.st.get(s);
    }
    
    public static Stone checkStone(final Block b) {
        if (b.getId() == 1) {
            return StoneManager.st.get(b.getLocationHash());
        }
        return null;
    }
    
    public static void removeExcavatedStone(final String l) {
        StoneManager.es.remove(l);
    }
    
    public static boolean removeStone(final String l) {
        final Stone stone = StoneManager.st.remove(l);
        if (stone != null) {
            stone.delete();
            return true;
        }
        return false;
    }
    
    public static boolean AddStone(final String s, final Location l) {
        final Stone h = StoneManager.st.get(s);
        if (h != null) {
            return false;
        }
        ++Config.DATABASE_STONIARKA;
        final Stone ss = new Stone(Config.DATABASE_STONIARKA, s, l);
        ss.insert();
        StoneManager.st.put(s, ss);
        return true;
    }
    
    public static void enable() {
        final long startTime = System.nanoTime();
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS stoniarka (id INT(255) PRIMARY KEY,world VARCHAR(255),x INT(200), y INT(200),z INT(200), hash VARCHAR(500));");
        try {
            final Connection con = Main.getProvider().getDatabase().getConnection();
            final Statement stmt = con.createStatement();
            final ResultSet rsge = stmt.executeQuery("SELECT * FROM stoniarka");
            while (rsge.next()) {
                final Stone g = new Stone(rsge);
                StoneManager.st.put(g.getHash(), g);
            }
            rsge.close();
            stmt.close();
            con.close();
            for (final Stone s : StoneManager.st.values()) {
                s.getLocation().getLevel().setBlockAt(s.getLocation().getFloorX(), s.getLocation().getFloorY(), s.getLocation().getFloorZ(), 1);
            }
            final long endTime = System.nanoTime();
            Main.getPlugin().getLogger().info("Loaded {G} stonefarms in {S}ms".replace("{G}", String.valueOf(StoneManager.st.size())).replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
        }
        catch (SQLException e) {
            Main.getPlugin().getLogger().warning("Error: " + e.getMessage());
            Main.getPlugin().getLogger().error("", (Throwable)e);
        }
    }
    
    static {
        StoneManager.st = new ConcurrentHashMap<String, Stone>();
        StoneManager.es = new ConcurrentHashMap<String, ExcavatedStone>();
    }
}
