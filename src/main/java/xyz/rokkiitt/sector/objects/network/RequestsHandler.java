package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import java.util.regex.*;
//import com.jsoniter.*;
//import xyz.rokkiitt.sector.config.*;
//import xyz.rokkiitt.sector.packets.guild.*;
//import java.util.concurrent.*;
//import cn.nukkit.*;
//import xyz.rokkiitt.sector.objects.user.*;
//import xyz.rokkiitt.sector.utils.*;
//import cn.nukkit.item.*;
//import xyz.rokkiitt.sector.objects.*;
//import xyz.rokkiitt.sector.objects.block.*;
//import xyz.rokkiitt.sector.*;
//import cn.nukkit.command.*;
//import cn.nukkit.level.*;
//import com.jsoniter.output.*;
//import xyz.rokkiitt.sector.packets.commands.*;
//import xyz.rokkiitt.sector.packets.*;
//import java.util.*;
//import xyz.rokkiitt.sector.objects.backup.*;
//import xyz.rokkiitt.sector.objects.itemshop.*;
//import xyz.rokkiitt.sector.objects.guild.*;
//
//public class RequestsHandler implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final String[] split = message.getBody().split(Pattern.quote("||"));
//        if (split.length >= 2) {
//            if (split[0].equalsIgnoreCase("receive")) {
//                final PacketGuildPanelRequest pa = JsonIterator.deserialize(split[1], PacketGuildPanelRequest.class);
//                if (pa.succes && pa.sendersector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayerExact(pa.sender.toLowerCase());
//                    if (p != null) {
//                        Main.panels.forEach(s -> s.onRequest(pa));
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("itemshop")) {
//                final PacketItemshopLoad pa2 = JsonIterator.deserialize(split[1], PacketItemshopLoad.class);
//                if (pa2.playersector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayer(pa2.player);
//                    if (p != null) {
//                        final Player p2;
//                        Main.items.forEach(s -> s.loadRequest(pa2, p2));
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("backup")) {
//                final PacketBackupLoad pa3 = JsonIterator.deserialize(split[1], PacketBackupLoad.class);
//                if (pa3.playersector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayer(pa3.player);
//                    if (p != null) {
//                        final Player p3;
//                        Main.backups.forEach(s -> s.loadRequest(pa3, p3));
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("backupreceive")) {
//                final PacketBackupReceive pa4 = JsonIterator.deserialize(split[1], PacketBackupReceive.class);
//                if (pa4.whosector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayer(pa4.who);
//                    if (p != null) {
//                        p.getInventory().setContents((Map)ItemSerializer.getItemMapFromString(pa4.items));
//                        final User u = UserManager.getUser(pa4.who);
//                        if (u != null) {
//                            u.addPoints(pa4.points);
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("update")) {
//                final PacketGuildPanelUpdate pa5 = JsonIterator.deserialize(split[1], PacketGuildPanelUpdate.class);
//                if (pa5.receiversector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final User u2 = UserManager.getUser(pa5.receiver);
//                    if (u2 != null) {
//                        u2.guildpermissions = u2.getGuildPermissions(pa5.perms);
//                        u2.querySave();
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("updateremove")) {
//                final PacketPlayerGuildRemove pa6 = JsonIterator.deserialize(split[1], PacketPlayerGuildRemove.class);
//                if (pa6.sector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final User u2 = UserManager.getUser(pa6.nickname);
//                    if (u2 != null) {
//                        u2.tag = "NIEPOSIADA";
//                        u2.allypvp = false;
//                        u2.pvp = false;
//                        final Set<String> n = (Set<String>)ConcurrentHashMap.newKeySet();
//                        n.add("brak");
//                        u2.boosts = n;
//                        u2.chapel = "brak";
//                        final Set<String> v = (Set<String>)ConcurrentHashMap.newKeySet();
//                        v.add("brak");
//                        u2.guildpermissions = v;
//                        u2.querySave();
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("updatenametag")) {
//                final PacketPlayerGuildRemove pa6 = JsonIterator.deserialize(split[1], PacketPlayerGuildRemove.class);
//                if (pa6.sector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final User u2 = UserManager.getUser(pa6.nickname);
//                    if (u2 != null) {
//                        final Player p4 = Server.getInstance().getPlayerExact(u2.getNickname());
//                        if (p4 != null) {
//                            p4.getInventory().close(p4);
//                            p4.sendTitle("", Util.fixColor("&cUTRACILES SWOJA GILDIE"));
//                            Util.changeNametag(p4);
//                            if (Config.getInstance().isNether()) {
//                                Util.kickPlayer(p4, "Straciles gildie bedac w netherze");
//                            }
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("removealliance")) {
//                final String[] sp = split[1].split(Pattern.quote("|^|"));
//                if (sp.length >= 2) {
//                    final String tag = sp[0];
//                    final String inv = sp[1];
//                    final List<Player> players = new ArrayList<Player>();
//                    for (final User u3 : UserManager.users) {
//                        if (u3.tag.equalsIgnoreCase(tag)) {
//                            u3.alliances.remove(inv.toLowerCase());
//                            u3.querySave();
//                            final Player p5 = Server.getInstance().getPlayerExact(u3.getNickname());
//                            if (p5 == null) {
//                                continue;
//                            }
//                            players.add(p5);
//                        }
//                        else {
//                            if (!u3.tag.equalsIgnoreCase(inv)) {
//                                continue;
//                            }
//                            u3.alliances.remove(tag.toLowerCase());
//                            u3.querySave();
//                            final Player p5 = Server.getInstance().getPlayerExact(u3.getNickname());
//                            if (p5 == null) {
//                                continue;
//                            }
//                            players.add(p5);
//                        }
//                    }
//                    for (final Player p6 : players) {
//                        Util.changeNametag(p6);
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("addalliance")) {
//                final String[] sp = split[1].split(Pattern.quote("|^|"));
//                if (sp.length >= 2) {
//                    final String tag = sp[0];
//                    final String inv = sp[1];
//                    final List<Player> players = new ArrayList<Player>();
//                    for (final User u3 : UserManager.users) {
//                        if (u3.tag.equalsIgnoreCase(tag)) {
//                            u3.alliances.add(inv.toLowerCase());
//                            u3.querySave();
//                            final Player p5 = Server.getInstance().getPlayerExact(u3.getNickname());
//                            if (p5 == null) {
//                                continue;
//                            }
//                            players.add(p5);
//                        }
//                        else {
//                            if (!u3.tag.equalsIgnoreCase(inv)) {
//                                continue;
//                            }
//                            u3.alliances.add(tag.toLowerCase());
//                            u3.querySave();
//                            final Player p5 = Server.getInstance().getPlayerExact(u3.getNickname());
//                            if (p5 == null) {
//                                continue;
//                            }
//                            players.add(p5);
//                        }
//                    }
//                    for (final Player p6 : players) {
//                        Util.changeNametag(p6);
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("addheart")) {
//                final Guild g = GuildManager.getGuild(split[1]);
//                if (g != null && g.hearts < 5) {
//                    final Guild guild = g;
//                    ++guild.hearts;
//                }
//            }
//            else if (split[0].equalsIgnoreCase("safecreate")) {
//                final PacketSafeCreate pa7 = JsonIterator.deserialize(split[1], PacketSafeCreate.class);
//                if (pa7.sector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayerExact(pa7.sender.toLowerCase());
//                    if (p != null) {
//                        final Item safe = new ItemBuilder(54, 1, 0).setTitle("&r&6Sejf id: &r&7" + pa7.id).setLore(new String[] { "", "&r &eOpis: &7brak", "&r &eWlasciciel: &7" + p.getName().toLowerCase(), "&r &eOstatnio otwarty: &7nigdy", "&r &eW sejfie znajduje sie &70 &eprzedmiotow", "", "&r&6Kliknij &7PPM &6aby otworzyc!" }).build();
//                        Util.addNBTTag(safe, "safe");
//                        Util.addNBTTagWithValue(safe, "id", String.valueOf(pa7.id));
//                        Util.addNBTTagWithValue(safe, "owner", p.getName().toLowerCase());
//                        Util.addNBTTagWithValue(safe, "data", "brak");
//                        Util.addNBTTagWithValue(safe, "opis", "brak");
//                        Util.giveItem(p, safe);
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("safeopen")) {
//                final PacketSafeOpen pa8 = JsonIterator.deserialize(split[1], PacketSafeOpen.class);
//                if (pa8.sector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayerExact(pa8.sender.toLowerCase());
//                    if (p != null) {
//                        final Item hand = p.getInventory().getItemInHand();
//                        if (Util.hasNBTTag(hand, "safe") && Util.getNBTTagValue(hand, "owner").equalsIgnoreCase(p.getName()) && Integer.valueOf(Util.getNBTTagValue(hand, "id")) == pa8.id) {
//                            final Safe safe2 = new Safe(p, hand, pa8.data.equalsIgnoreCase("brak") ? new HashMap<Integer, Item>() : ItemSerializer.getItemMapFromString(pa8.data));
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("bazargold")) {
//                final PacketBazarGoldAdd pa9 = JsonIterator.deserialize(split[1], PacketBazarGoldAdd.class);
//                if (Config.getInstance().Sector.equalsIgnoreCase(pa9.sector)) {
//                    final User u2 = UserManager.getUser(pa9.nickname);
//                    if (u2 != null) {
//                        final User user = u2;
//                        user.goldblocks += pa9.amount;
//                        u2.querySave();
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("randomtp")) {
//                final PacketRandomTeleport pa10 = JsonIterator.deserialize(split[1], PacketRandomTeleport.class);
//                if (Cooldown.getInstance().has(pa10.nickname, "playerjoin")) {
//                    return;
//                }
//                if (pa10.succes) {
//                    if (Config.getInstance().Sector.equalsIgnoreCase(pa10.currentsector)) {
//                        if (pa10.type == 0) {
//                            final Player p = Server.getInstance().getPlayerExact(pa10.nickname);
//                            if (p != null) {
//                                p.removeAllEffects();
//                                Util.sendMessage((CommandSender)p, Settings.getMessage("randomtpsucces"));
//                                final PacketTeleportCommand paa = new PacketTeleportCommand();
//                                paa.isPlayer = false;
//                                paa.isRandom = true;
//                                paa.isSector = true;
//                                paa.x = pa10.x;
//                                paa.y = 100;
//                                paa.z = pa10.z;
//                                paa.sendersector = Config.getInstance().Sector;
//                                paa.sender = p.getName();
//                                paa.playerdestination = ItemSerializer.serializeLocation(new Location((double)pa10.x, 100.0, (double)pa10.z, p.getYaw(), p.getPitch(), p.getLevel()));
//                                paa.succes = false;
//                                paa.reason = "";
//                                paa.destinationsector = "";
//                                Main.getNats().publish("command", "tp||" + JsonStream.serialize(paa));
//                            }
//                        }
//                        else if (pa10.type == 1 || pa10.type == 2) {
//                            for (final String s2 : pa10.players) {
//                                final Player p7 = Server.getInstance().getPlayerExact(s2);
//                                if (p7 != null) {
//                                    p7.removeAllEffects();
//                                    Util.sendMessage((CommandSender)p7, Settings.getMessage("randomtpsucces"));
//                                    final PacketTeleportCommand paa2 = new PacketTeleportCommand();
//                                    paa2.isPlayer = false;
//                                    paa2.isRandom = true;
//                                    paa2.isSector = true;
//                                    paa2.x = pa10.x;
//                                    paa2.y = 100;
//                                    paa2.z = pa10.z;
//                                    paa2.sendersector = Config.getInstance().Sector;
//                                    paa2.sender = p7.getName();
//                                    paa2.playerdestination = ItemSerializer.serializeLocation(new Location((double)pa10.x, 100.0, (double)pa10.z, p7.getYaw(), p7.getPitch(), p7.getLevel()));
//                                    paa2.succes = false;
//                                    paa2.reason = "";
//                                    paa2.destinationsector = "";
//                                    Main.getNats().publish("command", "tp||" + JsonStream.serialize(paa2));
//                                }
//                            }
//                        }
//                    }
//                }
//                else if (Config.getInstance().Sector.equalsIgnoreCase(pa10.currentsector)) {
//                    final Player p = Server.getInstance().getPlayerExact(pa10.nickname);
//                    if (p != null && !pa10.reason.isEmpty()) {
//                        Util.sendMessage((CommandSender)p, pa10.reason);
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("dtp")) {
//                final PacketDoubleTeleportCommand pa11 = JsonIterator.deserialize(split[1], PacketDoubleTeleportCommand.class);
//                if (pa11.sendersector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayerExact(pa11.sender);
//                    if (p != null) {
//                        Util.sendMessage((CommandSender)p, pa11.reason);
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("discordreward")) {
//                final DiscordRewardOnline pa12 = JsonIterator.deserialize(split[1], DiscordRewardOnline.class);
//                if (pa12.playersector.equalsIgnoreCase(Config.getInstance().Sector)) {
//                    final Player p = Server.getInstance().getPlayerExact(pa12.player);
//                    if (p != null) {
//                        final User u = UserManager.getUser(p.getName());
//                        if (u != null) {
//                            Util.sendMessage((CommandSender)p, Settings.getMessage("discordreward"));
//                            u.discordReceived = true;
//                            u.querySave();
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("clearregen")) {
//                final Iterator<Guild> iterator6;
//                Guild g2;
//                Main.getQuery().addQueue(() -> {
//                    GuildManager.guilds.iterator();
//                    while (iterator6.hasNext()) {
//                        g2 = iterator6.next();
//                        g2.clearRegeneration();
//                    }
//                });
//            }
//            else if (split[0].equalsIgnoreCase("clearlogblock")) {
//                final Iterator<Guild> iterator7;
//                Guild g3;
//                Main.getQuery().addQueue(() -> {
//                    GuildManager.guilds.iterator();
//                    while (iterator7.hasNext()) {
//                        g3 = iterator7.next();
//                        g3.clearLogblock();
//                    }
//                });
//            }
//        }
//    }
//}
