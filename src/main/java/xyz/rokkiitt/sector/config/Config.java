package xyz.rokkiitt.sector.config;

import java.io.*;

import cn.nukkit.level.Location;
import xyz.rokkiitt.sector.Main;

public class Config
{
    public String Sector;
    public static long meteorite;
    public static String STORE_MYSQL_HOST;
    public static int STORE_MYSQL_PORT;
    public static String STORE_MYSQL_BASE$NAME;
    public static String STORE_MYSQL_USERNAME;
    public static String STORE_MYSQL_PASSWORD;
    public static int DATABASE_STONIARKA;
    public static int DATABASE_RTP;
    public static int SPAWN;
    
    public Config() {
        STORE_MYSQL_HOST = "localhost";
        STORE_MYSQL_PORT = 3306;
        STORE_MYSQL_BASE$NAME = "core";
        STORE_MYSQL_USERNAME = "root";
        STORE_MYSQL_PASSWORD = "domiS2002_!_!";
        DATABASE_STONIARKA = 0;
        DATABASE_RTP = 0;
        SPAWN = 100;
    }

    public static boolean isSpawn(final Location loc) {
        final Location l2 = loc.getLocation().getLevel().getSpawnLocation().getLocation();
        final int distancex = Math.abs(loc.getFloorX() - l2.getFloorX());
        final int distancez = Math.abs(loc.getFloorZ() - l2.getFloorZ());
        return distancex <= SPAWN && distancez <= SPAWN;
    }
    

    public static void loadOthers() {
        final File g = Main.getPlugin().getDataFolder();
        if (!g.exists()) {
            g.mkdirs();
        }
        final File s = new File(Main.getPlugin().getDataFolder(), "skins");
        if (!s.exists()) {
            s.mkdirs();
        }
        final File ss = new File(s, "guild");
        if (!ss.exists()) {
            ss.mkdirs();
        }
        final File sss = new File(s, "wings");
        if (!sss.exists()) {
            sss.mkdirs();
        }
    }
}
