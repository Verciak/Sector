package xyz.rokkiitt.sector.objects.network;

import xyz.rokkiitt.sector.packets.*;
import xyz.rokkiitt.sector.*;
import xyz.rokkiitt.sector.utils.*;
import cn.nukkit.*;
import java.util.*;
import xyz.rokkiitt.sector.objects.user.*;

//public class ChatHandler
//{
//    public void onMessage() {
//        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
//            if (pa.guild.equalsIgnoreCase("NIEPOSIADA")) {
//                String format = Util.fixColor(pa.format);
//                format = format.replace("{MESSAGE}", Util.fixColor("") + pa.message);
//                p.sendMessage(format);
//            }
//            else {
//                final User u = UserManager.getUser(p.getName());
//                if (u != null) {
//                    String gff = pa.format;
//                    final String color = Util.getChatColor(pa.guild, u);
//                    gff = Util.fixColor(gff.replace("{COLOR}", color));
//                    gff = gff.replace("{MESSAGE}", Util.fixColor("") + pa.message);
//                    p.sendMessage(gff);
//                }
//                else {
//                    p.kick(Util.fixColor("&9not properly loaded user data - ReceiveChat"));
//                }
//            }
//        }
//    }
//}
