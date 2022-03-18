package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import xyz.rokkiitt.sector.packets.*;
//import com.jsoniter.*;
//import xyz.rokkiitt.sector.config.*;
//import xyz.rokkiitt.sector.*;
//import xyz.rokkiitt.sector.objects.user.*;
//
//public class PlayerLoadHandler implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final PacketPlayerData pa = JsonIterator.deserialize(message.getBody(), PacketPlayerData.class);
//        if (pa.destinationsector.equalsIgnoreCase(Config.getInstance().Sector)) {
//            final User u = UserManager.createUser(pa);
//            u.lastsector = "lobby";
//            Main.getNats().publish("lobbysectortransfer", pa.nickname);
//        }
//    }
//}
