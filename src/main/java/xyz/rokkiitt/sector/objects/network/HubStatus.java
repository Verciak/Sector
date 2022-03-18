package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import xyz.rokkiitt.sector.*;
//import cn.nukkit.*;
//import xyz.rokkiitt.sector.config.*;
//import java.util.*;
//
//public class HubStatus implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final String x = message.getBody();
//        if (x.equalsIgnoreCase("stop")) {
//            Main.isStop = true;
//        }
//        else if (x.equalsIgnoreCase("start")) {
//            Main.isStop = false;
//            for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
//                Main.getNats().publish("playernames", "join||" + Config.getInstance().Sector + "|&|" + p.getName());
//            }
//        }
//    }
//}
