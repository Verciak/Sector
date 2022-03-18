package xyz.rokkiitt.sector.utils;

import cn.nukkit.*;

import java.util.*;

public class AutoMsgUtil
{
    static int d;
    public static List<String> msg;
    
    public static void configure() {
        if (AutoMsgUtil.d > AutoMsgUtil.msg.size() - 1 || AutoMsgUtil.d < 0) {
            AutoMsgUtil.d = 0;
        }
        else {
            ++AutoMsgUtil.d;
        }
    }
    
    public static void send(final Player p) {
        if (AutoMsgUtil.d > AutoMsgUtil.msg.size() - 1 || AutoMsgUtil.d < 0 || AutoMsgUtil.msg.isEmpty()) {
            return;
        }
        Util.sendMessage(p, AutoMsgUtil.msg.get(AutoMsgUtil.d));
    }
    
    static {
        AutoMsgUtil.d = 0;
        AutoMsgUtil.msg = new ArrayList<>();
    }
}
