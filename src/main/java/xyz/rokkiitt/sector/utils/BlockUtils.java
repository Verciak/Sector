package xyz.rokkiitt.sector.utils;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import java.util.Arrays;
import java.util.List;
import xyz.rokkiitt.sector.objects.guild.Guild;

public class BlockUtils {
    private static List<Integer> farmers = Arrays.asList(new Integer[] { Integer.valueOf(54), Integer.valueOf(146), Integer.valueOf(154), Integer.valueOf(138) });

    public static void boyfarmer(Guild g, Location l) {
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            for (int i = l.getFloorY(); i > 0; i--) {
                l.setComponents(l.getX(), i, l.getZ());
                Block vv = l.getLevelBlock();
                if (vv.getId() == 7)
                    return;
                if (!g.getHeart().isInHeart(l) && !farmers.contains(Integer.valueOf(vv.getId())))
                    l.getLevel().setBlockAt(l.getFloorX(), l.getFloorY(), l.getFloorZ(), 49);
            }
        },1);
    }

    public static void sandfarmer(Guild g, Location l) {
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            for (int i = l.getFloorY(); i > 0; i--) {
                l.setComponents(l.getX(), i, l.getZ());
                Block vv = l.getLevelBlock();
                if (vv.getId() == 7)
                    return;
                if (!g.getHeart().isInHeart(l) && !farmers.contains(Integer.valueOf(vv.getId())))
                    l.getLevel().setBlockAt(l.getFloorX(), l.getFloorY(), l.getFloorZ(), 12);
            }
        },1);
    }

    public static void kopacz(Guild g, Location l) {
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
            for (int i = l.getFloorY(); i > 0; i--) {
                l.setComponents(l.getX(), i, l.getZ());
                Block vv = l.getLevelBlock();
                if (vv.getId() == 7)
                    return;
                if (!g.getHeart().isInHeart(l) && !farmers.contains(Integer.valueOf(vv.getId())))
                    l.getLevel().setBlockAt(l.getFloorX(), l.getFloorY(), l.getFloorZ(), 0);
            }
        },1);
    }
}

