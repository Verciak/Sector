package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import xyz.rokkiitt.sector.config.*;
//import xyz.rokkiitt.sector.objects.network.nether.*;
//import xyz.rokkiitt.sector.objects.network.guild.*;
//import xyz.rokkiitt.sector.objects.network.bazar.*;
//
//public class Network
//{
//    public static void load(final Nats nats) {
//        nats.subscribe("performance", new PerformanceHandler());
//        nats.subscribe("checkonline", new OnlineHandler());
//        nats.subscribe("playerloaddata", new PlayerLoadHandler());
//        nats.subscribe("playerdatasectorload", new PlayerSectorLoadHandler());
//        nats.subscribe("synchronizesettings", new SynchronizeHandler());
//        nats.subscribe("information", new InformationHandler());
//        nats.subscribe("playerchat", new ChatHandler());
//        nats.subscribe("database", new DatabaseHandler());
//        nats.subscribe("guildpanel", new RequestsHandler());
//        nats.subscribe("commandcallback", new CommandCallback());
//        nats.subscribe("playertimeout", new PlayerTimeout());
//        nats.subscribe("hubstatus", new HubStatus());
//        nats.subscribe("serverrequest", new ServerRequest());
//        nats.publish("databaserequest", Config.getInstance().Sector);
//        nats.subscribe("boostupdate", new BoostUpdate());
//        nats.subscribe("netherpost", new NetherPost());
//        nats.subscribe("chapelupdate", new ChapelUpdate());
//        nats.subscribe("chapelguildupdate", new ChapelGuildUpdate());
//        nats.subscribe("chapeldisband", new ChapelDisband());
//        nats.subscribe("bazarbuy", new BazarBuy());
//        nats.subscribe("bazartake", new BazarTake());
//        nats.subscribe("bazarload", new BazarLoad());
//        nats.subscribe("bazarrefresh", new BazarRefresh());
//    }
//}
