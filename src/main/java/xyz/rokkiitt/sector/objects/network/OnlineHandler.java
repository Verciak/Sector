package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import com.jsoniter.*;
//import xyz.rokkiitt.sector.config.*;
//import xyz.rokkiitt.sector.objects.block.*;
//import xyz.rokkiitt.sector.objects.user.*;
//import cn.nukkit.scheduler.*;
//import cn.nukkit.item.*;
//import cn.nukkit.potion.*;
//import cn.nukkit.*;
//import java.util.*;
//import xyz.rokkiitt.sector.objects.home.*;
//import xyz.rokkiitt.sector.objects.waypoint.*;
//import xyz.rokkiitt.sector.objects.guild.*;
//import xyz.rokkiitt.sector.*;
//import xyz.rokkiitt.sector.utils.*;
//import cn.nukkit.command.*;
//import com.jsoniter.output.*;
//import xyz.rokkiitt.sector.packets.*;
//
//public class OnlineHandler implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final PacketSectorCheck pa = JsonIterator.deserialize(message.getBody(), PacketSectorCheck.class);
//        if (pa.sectorfrom.equalsIgnoreCase(Config.getInstance().Sector)) {
//            final Player p = Main.getPlugin().getServer().getPlayerExact(pa.nickname);
//            if (p != null && p.isOnline()) {
//                final User u = UserManager.getUser(p.getName());
//                if (u != null) {
//                    if (Cooldown.getInstance().has(p, "playerjoin")) {
//                        return;
//                    }
//                    Cooldown.getInstance().add(p, "playerjoin", 15.0f);
//                    Server.getInstance().getScheduler().scheduleDelayedTask((Runnable)new Runnable() {
//                        @Override
//                        public void run() {
//                            p.getInventory().close(p);
//                            Server.getInstance().getScheduler().scheduleDelayedTask((Task)new Task() {
//                                public void onRun(final int i) {
//                                    final PacketPlayerData paa = new PacketPlayerData();
//                                    paa.nickname = pa.nickname;
//                                    paa.curentsector = pa.sectorfrom;
//                                    paa.destinationsector = pa.sectorto;
//                                    paa.slot = p.getInventory().getHeldItemSlot();
//                                    paa.invcontent = (p.getInventory().getContents().isEmpty() ? "brak" : ItemSerializer.getStringFromItemMap(p.getInventory().getContents()));
//                                    paa.invender = (p.getEnderChestInventory().getContents().isEmpty() ? "brak" : ItemSerializer.getStringFromItemMap(p.getEnderChestInventory().getContents()));
//                                    paa.effect = (p.getEffects().isEmpty() ? "brak" : ItemSerializer.serializeEffects(p.getEffects()));
//                                    paa.location = (pa.isplayer ? ("true|^|" + pa.playerdestination) : (pa.isteleport ? ("teleport|^|" + pa.playerdestination) : (pa.isRandom ? ("random|^|" + pa.playerdestination) : ("false|^|" + ItemSerializer.serializeLocationSector(p.getLocation(), pa.sectortoid)))));
//                                    paa.playerlvl = p.getExperienceLevel();
//                                    paa.playerxp = p.getExperience();
//                                    paa.playerhealth = p.getHealth();
//                                    paa.playerfood = p.getFoodData().getLevel();
//                                    paa.playerisflying = p.getAdventureSettings().get(AdventureSettings.Type.FLYING);
//                                    paa.hasop = p.isOp();
//                                    paa.playerfireticks = p.fireTicks;
//                                    paa.playerallowflying = p.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT);
//                                    paa.wings = u.wings;
//                                    paa.playergamemode = p.getGamemode();
//                                    paa.rank = u.rank;
//                                    paa.tag = u.tag;
//                                    paa.alliances.addAll(u.alliances);
//                                    paa.slot = p.getInventory().getHeldItemSlot();
//                                    paa.allypvp = u.allypvp;
//                                    paa.pvp = u.pvp;
//                                    paa.protection = u.protection;
//                                    paa.isGod = u.isGod;
//                                    paa.isVanish = u.isVanish;
//                                    paa.kills = u.kills;
//                                    paa.deaths = u.deaths;
//                                    paa.assist = u.assist;
//                                    paa.points = u.points;
//                                    paa.turbodrop = u.turbodrop;
//                                    paa.drops.addAll(u.disableddrops);
//                                    paa.guildpermissions = u.getPermissionString();
//                                    paa.egapple = u.egapple;
//                                    paa.gapple = u.gapple;
//                                    paa.arrows = u.arrows;
//                                    paa.primedtnt = u.primedtnt;
//                                    paa.snowballs = u.snowballs;
//                                    paa.pearls = u.pearls;
//                                    paa.viptime = u.viptime;
//                                    paa.sviptime = u.sviptime;
//                                    paa.sponsortime = u.sponsortime;
//                                    paa.foodtime = u.foodtime;
//                                    paa.endertime = u.endertime;
//                                    paa.playertime = u.playertime;
//                                    paa.onlineoverall = u.onlineoverall;
//                                    paa.onlinetime = u.onlinetime;
//                                    paa.onlinesession = u.onlinesession;
//                                    paa.firstLogin = u.firstLogin;
//                                    paa.mutetime = u.mutetime;
//                                    paa.ignored.addAll(u.ignored);
//                                    paa.alertsenabled = u.alertsenabled;
//                                    paa.goldblocks = u.goldblocks;
//                                    paa.placed = u.placed;
//                                    paa.broken = u.broken;
//                                    paa.homes = PlayerHomeData.serialize(u);
//                                    paa.waypoints = WaypointData.serialize(u);
//                                    paa.discordreceived = u.discordReceived;
//                                    paa.discordreward = u.discordReward;
//                                    paa.discordcode = u.discordcode;
//                                    paa.incognito = u.incognito;
//                                    paa.isIncognito = u.isIncognito;
//                                    paa.IncognitoAlliance = u.IncognitoAlliance;
//                                    paa.IncognitoDead = u.IncognitoDead;
//                                    paa.IncognitoKill = u.IncognitoKill;
//                                    paa.boosts = GuildBoosts.serialize(u.boosts);
//                                    paa.chapel = u.chapel;
//                                    if (!pa.enabled) {
//                                        paa.curentsector = pa.sectorto;
//                                        Util.sendMessage((CommandSender)p, Settings.getMessage("destinationsectoroffline").replace("{SECTOR}", String.valueOf(pa.sectorto)));
//                                    }
//                                    Server.getInstance().getScheduler().scheduleDelayedTask((Runnable)new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (pa.enabled) {
//                                                Main.getNats().publish("playerhubdatasavechangesector", JsonStream.serialize(paa));
//                                            }
//                                            else {
//                                                Main.getNats().publish("databasesave", "user||" + JsonStream.serialize(paa));
//                                                final PacketPlayerChangeSector paa = new PacketPlayerChangeSector();
//                                                paa.curentsector = Config.getInstance().Sector;
//                                                paa.destinationsector = "lobby";
//                                                paa.nickname = pa.nickname;
//                                                Main.getNats().publish("proxyplayertransfer", JsonStream.serialize(paa));
//                                            }
//                                        }
//                                    }, 10);
//                                }
//                            }, 20);
//                        }
//                    }, 10);
//                }
//                else {
//                    p.kick("checkonline null data");
//                }
//            }
//        }
//    }
//}
