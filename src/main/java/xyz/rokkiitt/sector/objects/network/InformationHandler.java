package xyz.rokkiitt.sector.objects.network;

//import nats.client.*;
//import java.util.regex.*;
//import cn.nukkit.*;
//import xyz.rokkiitt.sector.utils.*;
//import cn.nukkit.command.*;
//import xyz.rokkiitt.sector.objects.user.*;
//import xyz.rokkiitt.sector.objects.*;
//import java.util.*;
//
//public class InformationHandler implements MessageHandler
//{
//    @Override
//    public void onMessage(final Message message) {
//        final String[] split = message.getBody().split(Pattern.quote("||"));
//        if (split.length >= 2) {
//            if (split[0].equalsIgnoreCase("CHAT")) {
//                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
//                    Util.sendMessage((CommandSender)p, split[1]);
//                }
//            }
//            else if (split[0].equalsIgnoreCase("TITLE")) {
//                final String[] s = split[1].split(Pattern.quote("|^|"));
//                if (s.length >= 2) {
//                    for (final Player p2 : Server.getInstance().getOnlinePlayers().values()) {
//                        p2.sendTitle(Util.fixColor(s[0]), Util.fixColor(s[1]));
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("ACTION")) {
//                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
//                    p.sendActionBar(Util.fixColor(split[1]));
//                }
//            }
//            else if (split[0].equalsIgnoreCase("GUILDCHAT")) {
//                final String[] s = split[1].split(Pattern.quote("|^|"));
//                if (s.length >= 2) {
//                    for (final User u : UserManager.users) {
//                        if (u.tag.equalsIgnoreCase(s[0])) {
//                            final Player p3 = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
//                            if (p3 == null) {
//                                continue;
//                            }
//                            Util.sendMessage((CommandSender)p3, s[1]);
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("GUILDTITLE")) {
//                final String[] s = split[1].split(Pattern.quote("|^|"));
//                if (s.length >= 3) {
//                    for (final User u : UserManager.users) {
//                        if (u.tag.equalsIgnoreCase(s[0])) {
//                            final Player p3 = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
//                            if (p3 == null) {
//                                continue;
//                            }
//                            p3.sendTitle(Util.fixColor(s[1]), Util.fixColor(s[2]));
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("infoadmin")) {
//                Server.getInstance().getLogger().info(Util.fixColor(split[1]));
//                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
//                    if (p.hasPermission(Perms.INFOADMIN.getPermission())) {
//                        final User u = UserManager.getUser(p.getName());
//                        if (u != null) {
//                            if (u.alertsenabled) {
//                                continue;
//                            }
//                            Util.sendMessage((CommandSender)p, split[1]);
//                        }
//                        else {
//                            p.kick(Util.fixColor("&3not properly loaded data - InfoAdmin"));
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("infoadminhelpop")) {
//                Server.getInstance().getLogger().info(Util.fixColor(split[1]));
//                for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
//                    if (p.hasPermission(Perms.INFOADMIN.getPermission())) {
//                        final User u = UserManager.getUser(p.getName());
//                        if (u != null) {
//                            Util.sendMessage((CommandSender)p, split[1]);
//                        }
//                        else {
//                            p.kick(Util.fixColor("&3not properly loaded data - InfoAdmin"));
//                        }
//                    }
//                }
//            }
//            else if (split[0].equalsIgnoreCase("DEADCHAT")) {
//                final String[] s = split[1].split(Pattern.quote("|&|"));
//                if (s.length >= 3) {
//                    for (final User u : UserManager.users) {
//                        final Player p3 = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
//                        if (p3 != null) {
//                            String mes = s[2];
//                            mes = mes.replace("{PGUILD}", s[0].equalsIgnoreCase("NIEPOSIADA") ? "" : ("&8[" + this.getColor(u, s[0]) + s[0].toUpperCase() + "&8]"));
//                            mes = mes.replace("{KGUILD}", s[1].equalsIgnoreCase("NIEPOSIADA") ? "" : ("&8[" + this.getColor(u, s[1]) + s[1].toUpperCase() + "&8]"));
//                            Util.sendMessage((CommandSender)p3, mes);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private String getColor(final User u, final String second) {
//        if (u.tag.equalsIgnoreCase(second)) {
//            return "&a";
//        }
//        if (u.alliances.contains(second.toLowerCase())) {
//            return "&b";
//        }
//        return "&c";
//    }
//}
