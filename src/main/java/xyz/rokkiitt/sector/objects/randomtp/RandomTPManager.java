package xyz.rokkiitt.sector.objects.randomtp;

import cn.nukkit.level.*;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.config.Config;

import java.util.*;
import java.sql.*;
import java.util.concurrent.*;
import java.time.*;

public class RandomTPManager
{
    private static Set<RandomTP> rtp;
    private static Cache<UUID, RandomTPStatus> status;
    
    public static void setStatus(final UUID id, final RandomTPStatus s) {
        if (s.equals(RandomTPStatus.STOP)) {
            RandomTPManager.status.asMap().remove(id);
            return;
        }
        RandomTPManager.status.put(id, s);
    }
    
    public static RandomTPStatus getStatus(final UUID id) {
        return RandomTPManager.status.asMap().get(id);
    }
    
    public static void create(final Location l, final RandomTPStatus s) {
        ++Config.DATABASE_RTP;
        final RandomTP w = new RandomTP(Config.DATABASE_RTP, l, s);
        w.update();
        RandomTPManager.rtp.add(w);
    }
    
    public static RandomTP getTeleport(final Location l) {
        for (final RandomTP d : RandomTPManager.rtp) {
            if (d.getWorld().contains(l.getLevel().getName()) && d.getX() == l.getFloorX() && d.getY() == l.getFloorY() && d.getZ() == l.getFloorZ()) {
                return d;
            }
        }
        return null;
    }
    
    public static void delete(final RandomTP e) {
        e.Delete();
        RandomTPManager.rtp.remove(e);
    }
    
    public static void enable() {
        final long startTime = System.nanoTime();
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS randomtp (id INT(255) PRIMARY KEY, world VARCHAR(255), x INT(255), y INT(255), z INT(255), status VARCHAR(255));");
        try {
            final Connection con = Main.getProvider().getDatabase().getConnection();
            final Statement stmt = con.createStatement();
            final ResultSet rsge = stmt.executeQuery("SELECT * FROM randomtp");
            while (rsge.next()) {
                RandomTPManager.rtp.add(new RandomTP(rsge));
            }
            rsge.close();
            stmt.close();
            con.close();
            final long endTime = System.nanoTime();
            Main.getPlugin().getLogger().info("Loaded {G} randomtp in {S}ms".replace("{G}", String.valueOf(RandomTPManager.rtp.size())).replace("{S}", String.valueOf((endTime - startTime) / 1000000L)));
        }
        catch (SQLException e) {
            Main.getPlugin().getLogger().warning("Error: " + e.getMessage());
            Main.getPlugin().getLogger().error("", (Throwable)e);
        }
    }
    
    static {
        RandomTPManager.rtp = ConcurrentHashMap.newKeySet();
        RandomTPManager.status = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10L)).build();
    }
}
