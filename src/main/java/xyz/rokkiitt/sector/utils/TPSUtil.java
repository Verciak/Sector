package xyz.rokkiitt.sector.utils;

import xyz.rokkiitt.sector.Main;
import cn.nukkit.utils.*;

public class TPSUtil
{
    private static long tpss;
    
    public static void setTPSTime(final long l) {
        TPSUtil.tpss = l;
    }
    
    public static long getTPSTime() {
        return TPSUtil.tpss;
    }
    
    public static String getTPSNoColor() {
        final float tps = Main.getPlugin().getServer().getTicksPerSecond();
        return String.valueOf(((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0));
    }
    
    public static String getCurrentTPS() {
        final float tps = Main.getPlugin().getServer().getTicksPerSecond();
        return String.valueOf(((tps > 18.0) ? TextFormat.GREEN : ((tps > 16.0) ? TextFormat.YELLOW : TextFormat.RED)).toString()) + ((tps > 20.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }
    
    public static Double getTPS() {
        return Math.min(Math.round(Main.getPlugin().getServer().getTicksPerSecond() * 100.0) / 100.0, 20.0);
    }
}
