package xyz.rokkiitt.sector.objects.network;
//
//import nats.client.*;
//import java.util.regex.*;
//import xyz.rokkiitt.sector.config.*;
//import cn.nukkit.*;
//
//public class PlayerTimeout implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final String[] split = message.getBody().split(Pattern.quote("||"));
//        if (split.length >= 2 && split[0].equalsIgnoreCase(Config.getInstance().Sector)) {
//            final Player p = Server.getInstance().getPlayerExact(split[1]);
//            if (p != null) {
//                p.close("", "TIME OUT FIX", false);
//            }
//        }
//    }
//}
