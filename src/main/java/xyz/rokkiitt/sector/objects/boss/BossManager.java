package xyz.rokkiitt.sector.objects.boss;

import cn.nukkit.Player;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BossManager
{
    public static Set<Boss> users;

    public static void createBoss(Player player) {
        Boss value = new Boss(player);
        BossManager.users.add(value);
        value.setBoss(true);
        value.setPerch(false);
    }

    public static void deleteBoss(final Boss u) {
        BossManager.users.remove(u);
        u.setBoss(false);
        u.setPerch(false);
    }
    
    public static Boss getBoss(final String id) {
        for (final Boss u : BossManager.users) {
            if (u.getAuthor().equalsIgnoreCase(id)) {
                return u;
            }
        }
        return null;
    }
    
    static {
        BossManager.users = ConcurrentHashMap.newKeySet();
    }
}
