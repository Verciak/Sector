package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import java.util.regex.*;
//import xyz.rokkiitt.sector.config.*;
//import xyz.rokkiitt.sector.packets.guild.*;
//import com.jsoniter.*;
//import xyz.rokkiitt.sector.objects.guild.*;
//import xyz.rokkiitt.sector.utils.*;
//import xyz.rokkiitt.sector.objects.nether.*;
//import xyz.rokkiitt.sector.*;
//
//public class DatabaseHandler implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final String[] split = message.getBody().split(Pattern.quote("||"));
//        if (split.length >= 3) {
//            if (split[0].equalsIgnoreCase(Config.getInstance().Sector)) {
//                final String lowerCase = split[1].toLowerCase();
//                switch (lowerCase) {
//                    case "guild": {
//                        final PacketGuildData pa = JsonIterator.deserialize(split[2], PacketGuildData.class);
//                        GuildManager.createGuildLoad(pa.tag, pa.name, pa.leader, pa.sector, pa.centerx, pa.centerz, pa.size, pa.members, pa.guildBalance, pa.skarbiec, pa.createTime, pa.guildprot, pa.heartprot, pa.penaltytnt, pa.hearttype, pa.heartcolor, pa.hearts, pa.hoppers, pa.collections, pa.boosts, pa.chopel);
//                        break;
//                    }
//                    case "nether": {
//                        ChapelHandler.load(Util.StringFromString(split[2]));
//                        break;
//                    }
//                    default: {
//                        Main.getPlugin().getLogger().info("Deafult switch on database: " + split[0]);
//                        break;
//                    }
//                }
//            }
//        }
//        else {
//            Main.getPlugin().getLogger().info("database pub but split lenght < 2: " + message.getBody());
//        }
//    }
//}
