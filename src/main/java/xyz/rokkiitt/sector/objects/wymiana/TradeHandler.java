package xyz.rokkiitt.sector.objects.wymiana;

import java.util.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.objects.user.User;
import cn.nukkit.inventory.*;
import java.util.concurrent.*;

public class TradeHandler
{
    private static final Set<Trade> wymiana;
    
    public static void create(final Player p1, final Player p2, final User u1, final User u2) {
        final Trade v = new Trade(p1, p2, u1, u2);
        TradeHandler.wymiana.add(v);
    }
    
    public static void deleteOnClose(final Trade e) {
        if (!e.p1closed) {
            e.p1.removeWindow((Inventory)e);
        }
        if (!e.p2closed) {
            e.p2.removeWindow((Inventory)e);
        }
        if (e.p1closed && e.p2closed) {
            e.u1.setTrading(false);
            e.u2.setTrading(false);
            TradeHandler.wymiana.remove(e);
        }
    }
    
    public static void deleteOnSucces(final Trade e) {
        e.u1.setTrading(false);
        e.u2.setTrading(false);
        TradeHandler.wymiana.remove(e);
    }
    
    static {
        wymiana = ConcurrentHashMap.newKeySet();
    }
}
