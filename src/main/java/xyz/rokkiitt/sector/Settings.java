package xyz.rokkiitt.sector;

import cn.nukkit.utils.Config;
import xyz.rokkiitt.sector.utils.AutoMsgUtil;
import xyz.rokkiitt.sector.utils.Util;

import java.util.regex.*;
import cn.nukkit.*;
import cn.nukkit.scheduler.*;
import cn.nukkit.command.*;
import java.util.*;

public class Settings {
    public static int GUILD_SIZE_MAX;
    public static int GUILD_SIZE_START;
    public static int GUILD_SIZE_ADD;
    public static int GUILD_SIZE_BETWEEN;
    public static int GUILD_SIZE_COST;
    public static int GUILD_Y;
    public static int NAME_LENGHT_MIN;
    public static int NAME_LENGHT_MAX;
    public static int TAG_LENGHT_MIN;
    public static int TAG_LENGHT_MAX;
    public static long DROP_TURBO;
    public static long PANDORA_TIME;
    public static double PANDORA_CHANCE;
    public static boolean ENABLE_PANDORA;
    public static boolean ENABLE_VOUCHER;
    public static boolean ENABLE_DIAX;
    public static boolean ENABLE_GUILD;
    public static boolean ENABLE_TNT;
    public static Map<String, String> messages;
    public static long EVENT_POINTS;
    public static int LIMIT_EGAPPLE;
    public static int LIMIT_GAPPLE;
    public static int LIMIT_ARROWS;
    public static int LIMIT_PRIMEDTNT;
    public static int LIMIT_SNOWBALLS;
    public static int LIMIT_PEARLS;
    public static int LIMIT_MACRO;
    public static boolean ENABLE_KIT;
    public static boolean ENABLE_STRENGHT1;
    public static boolean ENABLE_STRENGHT2;
    public static boolean ENABLE_SPEED1;
    public static boolean ENABLE_SPEED2;
    public static boolean ENABLE_CHAT;
    public static boolean ENABLE_METEOR;
    public static boolean ENABLE_GITEMS;
    public static long FREEZE_TIME;
    public static long KIT_PLAYER;
    public static long KIT_FOOD;
    public static long KIT_ENDER;
    public static long KIT_VIP;
    public static long KIT_SVIP;
    public static long KIT_SPONSOR;
    public static boolean ENABLE_ITEMSHOP;
    public static boolean ENABLE_ITEMSHOP_ONLYRANKS;
    public static boolean ENABLE_ENCHANT;
    public static Set<String> profanity;

    public static String getMessage(final String property) {
        if (Main.getPlugin().getConfig().getString(property) != null) {
            return Main.getPlugin().getConfig().getString(property);
        }
        return "&4Error: &cNie odnaleziono wiadomosci dla: " + property.toLowerCase();
    }

    public static void set() {
        Config pd = Main.getPlugin().getConfig();
        Settings.ENABLE_GUILD = pd.getBoolean("enable_guild");
        Settings.ENABLE_TNT = pd.getBoolean("enable_tnt");
        Settings.ENABLE_ENCHANT = pd.getBoolean("ENABLE_ENCHANT");
        Settings.GUILD_Y = pd.getInt("guild_y");
        Settings.GUILD_SIZE_ADD = pd.getInt("guild_size_add");
        Settings.GUILD_SIZE_BETWEEN = pd.getInt("guild_size_between");
        Settings.GUILD_SIZE_MAX = pd.getInt("guild_size_max");
        Settings.GUILD_SIZE_START = pd.getInt("guild_size_start");
        Settings.GUILD_SIZE_COST = pd.getInt("guild_size_cost");
        Settings.NAME_LENGHT_MAX = pd.getInt("name_lenght_max");
        Settings.NAME_LENGHT_MIN = pd.getInt("name_lenght_min");
        Settings.TAG_LENGHT_MAX = pd.getInt("tag_lenght_max");
        Settings.TAG_LENGHT_MIN = pd.getInt("tag_lenght_min");
        Settings.DROP_TURBO = pd.getLong("DROP_TURBO");
        Settings.PANDORA_TIME = pd.getLong("PANDORA_TIME");
        Settings.PANDORA_CHANCE = pd.getDouble("PANDORA_CHANCE");
        Settings.ENABLE_PANDORA = pd.getBoolean("ENABLE_PANDORA");
        Settings.ENABLE_VOUCHER = pd.getBoolean("ENABLE_VOUCHER");
        Settings.ENABLE_DIAX = pd.getBoolean("ENABLE_DIAX");
        Settings.LIMIT_MACRO = pd.getInt("LIMIT_MACRO");
        AutoMsgUtil.msg.clear();
        final String ss = getMessage("automsg");
        AutoMsgUtil.msg.addAll(Arrays.asList(ss.split(Pattern.quote("|&|"))));
        if (Server.getInstance() != null) {
            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                public void onRun(final int i) {
                    final CommandMap cmdp = Server.getInstance().getCommandMap();
                    if (cmdp != null) {
                        for (final Command cmd : Server.getInstance().getCommandMap().getCommands().values()) {
                            cmd.setPermissionMessage(Util.fixColor(Settings.getMessage("commandpermission").replace("{PERM}", (cmd.getPermission() == null) ? "" : cmd.getPermission())));
                        }
                    }
                }
            }, 10);
        }
        Settings.EVENT_POINTS = pd.getLong("EVENT_POINTS");
        Settings.LIMIT_EGAPPLE = pd.getInt("LIMIT_EGAPPLE");
        Settings.LIMIT_GAPPLE = pd.getInt("LIMIT_GAPPLE");
        Settings.LIMIT_ARROWS = pd.getInt("LIMIT_ARROWS");
        Settings.LIMIT_PRIMEDTNT = pd.getInt("LIMIT_PRIMEDTNT");
        Settings.LIMIT_SNOWBALLS = pd.getInt("LIMIT_SNOWBALLS");
        Settings.LIMIT_PEARLS = pd.getInt("LIMIT_PEARLS");
        Settings.ENABLE_KIT = pd.getBoolean("ENABLE_KIT");
        Settings.ENABLE_STRENGHT1 = pd.getBoolean("ENABLE_STRENGHT1");
        Settings.ENABLE_STRENGHT2 = pd.getBoolean("ENABLE_STRENGHT2");
        Settings.ENABLE_SPEED1 = pd.getBoolean("ENABLE_SPEED1");
        Settings.ENABLE_SPEED2 = pd.getBoolean("ENABLE_SPEED2");
        Settings.ENABLE_CHAT = pd.getBoolean("ENABLE_CHAT");
        Settings.ENABLE_METEOR = pd.getBoolean("ENABLE_METEOR");
        Settings.ENABLE_GITEMS = pd.getBoolean("ENABLE_GITEMS");
        Settings.FREEZE_TIME = pd.getLong("FREEZE_TIME");
        Settings.KIT_PLAYER = pd.getLong("KIT_PLAYER");
        Settings.KIT_FOOD = pd.getLong("KIT_FOOD");
        Settings.KIT_ENDER = pd.getLong("KIT_ENDER");
        Settings.KIT_VIP = pd.getLong("KIT_VIP");
        Settings.KIT_SVIP = pd.getLong("KIT_SVIP");
        Settings.KIT_SPONSOR = pd.getLong("KIT_SPONSOR");
        Settings.ENABLE_ITEMSHOP = pd.getBoolean("ENABLE_ITEMSHOP");
        Settings.ENABLE_ITEMSHOP_ONLYRANKS = pd.getBoolean("ENABLE_ITEMSHOP_ONLYRANKS");
    }
}
