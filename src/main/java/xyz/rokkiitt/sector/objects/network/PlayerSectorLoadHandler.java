package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import com.jsoniter.*;
//import xyz.rokkiitt.sector.config.*;
//import xyz.rokkiitt.sector.objects.user.*;
//import xyz.rokkiitt.sector.packets.*;
//import xyz.rokkiitt.sector.*;
//import com.jsoniter.output.*;
//
//public class PlayerSectorLoadHandler implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final PacketPlayerData pa = JsonIterator.deserialize(message.getBody(), PacketPlayerData.class);
//        if (pa.destinationsector.equalsIgnoreCase(Config.getInstance().Sector)) {
//            UserManager.createUser(pa);
//            final PacketPlayerChangeSector paa = new PacketPlayerChangeSector();
//            paa.curentsector = pa.curentsector;
//            paa.destinationsector = pa.destinationsector;
//            paa.nickname = pa.nickname;
//            Main.getNats().publish("proxyplayertransfer", JsonStream.serialize(paa));
//        }
//    }
//}
