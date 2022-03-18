package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import java.util.regex.*;
//import xyz.rokkiitt.sector.*;
//
//public class CommandCallback implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final String[] split = message.getBody().split(Pattern.quote("||"));
//        if (split.length >= 2) {
//            final SectorCommand cmd = SectorCommand.getCommand(split[0].toLowerCase());
//            if (cmd != null) {
//                cmd.onCallback(split[1]);
//            }
//        }
//    }
//}
