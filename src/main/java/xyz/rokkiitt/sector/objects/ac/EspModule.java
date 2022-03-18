package xyz.rokkiitt.sector.objects.ac;

import cn.nukkit.*;
import java.util.*;

import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.math.*;
import cn.nukkit.level.*;
import cn.nukkit.block.*;

public class EspModule implements Runnable
{
    @Override
    public void run() {
        for (final Player player : new ArrayList<Player>(Server.getInstance().getOnlinePlayers().values())) {
            for (final Player p : new ArrayList<Player>(player.getLocation().getLevel().getPlayers().values())) {
                if (p != player) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null && u.isVanish()) {
                        player.hidePlayer(p);
                    }
                    else {
                        final boolean canSeePlayer = canSeeEntity(player, p);
                        if (!canSeePlayer) {
                            player.hidePlayer(p);
                        }
                        else {
                            player.showPlayer(p);
                        }
                    }
                }
            }
        }
    }
    
    public static boolean canSeeEntity(final Player player, final Player p) {
        return player.distance((Vector3)p) <= 60.0;
    }
    
    private static Location getLocation(final Vector3 v, final Level l) {
        return new Location(v.getX(), v.getY(), v.getZ(), l);
    }
    
    private static Block getBlock(final Vector3 v, final Level l) {
        return Util.getBlock(l, v.getFloorX(), v.getFloorY(), v.getFloorZ(), false);
    }
}
