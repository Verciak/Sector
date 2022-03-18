package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import xyz.rokkiitt.sector.*;
//import xyz.rokkiitt.sector.packets.*;
//import xyz.rokkiitt.sector.utils.*;
//import xyz.rokkiitt.sector.config.*;
//import com.jsoniter.output.*;
//
//public class PerformanceHandler implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        if (!Main.isStop) {
//            final PacketPerformance pa = new PacketPerformance();
//            pa.tps = TPSUtil.getTPSNoColor();
//            pa.sector = Config.getInstance().Sector;
//            message.reply(JsonStream.serialize(pa));
//        }
//    }
//}
