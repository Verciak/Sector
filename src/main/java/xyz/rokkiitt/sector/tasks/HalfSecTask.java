package xyz.rokkiitt.sector.tasks;

import cn.nukkit.item.Item;
import cn.nukkit.scheduler.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.objects.home.PlayerHomeData;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.teleport.TeleportManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.potion.*;
import xyz.rokkiitt.sector.objects.waypoint.WaypointData;

import java.util.*;

public class HalfSecTask extends Task
{
    public void onRun(final int tick) {
        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
            if (!Cooldown.getInstance().has(p, "removeMeta")) {
                p.getServer().removePlayerListData(p.getUniqueId());
                Cooldown.getInstance().add(p, "removeMeta", 5.0f);
            }
            final User user = UserManager.getUser(p.getName());
            Main.getProvider().update("UPDATE `users` SET `homes` ='" + PlayerHomeData.serialize(user) + "' WHERE `nickname` ='" + p.getName() + "'");
            Main.getProvider().update("UPDATE `users` SET `waypoints` ='" + WaypointData.serialize(user) + "' WHERE `nickname` ='" + p.getName() + "'");
            if (user != null) {
                if(Server.getInstance().isRunning()) {
                    user.setLocation("false|^|" + ItemSerializer.serializeLocation(p.getLocation()));
                    final StringJoiner nn = new StringJoiner(Util.fixColor(" &6| "));
                    if (Settings.FREEZE_TIME >= System.currentTimeMillis()) {
                        if (user.hasProtection()) {
                            user.setProtection(user.getProtection() + 500L);
                        }
                        if (!p.hasEffect(15)) {
                            p.addEffect(Effect.getEffect(15).setAmbient(true).setAmplifier(5).setVisible(false).setDuration(1000000000));
                        }
                        if (!p.hasEffect(2)) {
                            p.addEffect(Effect.getEffect(2).setAmbient(true).setAmplifier(5).setVisible(false).setDuration(1000000000));
                        }
                        nn.add(Util.fixColor("&6START EDYCJI: " + Util.fixColor("&e" + Util.formatTime(Settings.FREEZE_TIME - System.currentTimeMillis()))));
                    } else {
                        if (Settings.FREEZE_TIME + 5000L >= System.currentTimeMillis()) {
                            if (p.hasEffect(15)) {
                                p.removeEffect(15);
                            }
                            if (p.hasEffect(2)) {
                                p.removeEffect(2);
                            }
                        }
                        if (user.hasProtection()) {
                            nn.add(Settings.getMessage("ochrona").replace("{TIME}", Util.formatTime(user.getProtection() - System.currentTimeMillis())));
                        } else if (user.getProtection() + 700L >= System.currentTimeMillis()) {
                            Util.changeNametag(p);
                        }
                        if (CombatManager.isContains(p.getName())) {
                            long time = CombatManager.getTime(p).longValue();
                            if (time <= System.currentTimeMillis()) {
                                user.clearAssistAttackers();
                                CombatManager.removePlayer(p);
                                nn.add(Settings.getMessage("combatend"));
                            } else {
                                nn.add(Settings.getMessage("combatleft").replace("{TIME}", Util.formatTime(time - System.currentTimeMillis())));
                            }
                        }
                        if (TeleportManager.isContains(user.getNickname())) {
                            final HashMap<String, String> d = TeleportManager.getMap(p);
                            if (Long.valueOf(d.get("time")) <= System.currentTimeMillis()) {
                                nn.add(Util.fixColor(Settings.getMessage("teleportsucces")));
                                TeleportManager.initTeleport(p);
                            } else {
                                nn.add(Settings.getMessage("teleportleft").replace("{TIME}", Util.formatTime(Long.valueOf(d.get("time")) - System.currentTimeMillis())));
                            }
                        }
                        if (user.isVanish()) {
                            nn.add("&6VANISH");
                        }
                        if (Settings.PANDORA_TIME >= System.currentTimeMillis()) {
                            nn.add(Util.fixColor(Settings.getMessage("pandoraleft").replace("{TIME}", Util.formatTime(Settings.PANDORA_TIME - System.currentTimeMillis()))));
                        }
                        if (Settings.EVENT_POINTS >= System.currentTimeMillis()) {
                            nn.add(Util.fixColor(Settings.getMessage("pointsleft").replace("{TIME}", Util.formatTime(Settings.EVENT_POINTS - System.currentTimeMillis()))));
                        }
                        if (Settings.DROP_TURBO >= System.currentTimeMillis()) {
                            nn.add(Settings.getMessage("turboleft").replace("{TIME}", Util.formatTime(Settings.DROP_TURBO - System.currentTimeMillis())));
                        } else if (user.getTurbodrop() / 2L + System.currentTimeMillis() >= System.currentTimeMillis() && user.getTurbodrop() / 2L > 0L) {
                            nn.add(Settings.getMessage("turboleft").replace("{TIME}", Util.formatTime(user.getTurbodrop() / 2L)));
                            user.reduceTurbodrop(1000);
                        }
                        if (MeteoriteManager.isRunning()) {
                            if (nn.length() != 0) {
                                nn.add("\n");
                            }
                            nn.add(Settings.getMessage("meteoryt").replace("{TIME}", Util.formatTime(Time.SECOND.getTime(MeteoriteManager.getCountDown()))).replace("{Z}", "" + MeteoriteManager.getRegion().getCenterZ()).replace("{X}", "" + MeteoriteManager.getRegion().getCenterX()));
                        }
                    }
                    if (nn.length() != 0) {
                        p.sendActionBar(Util.fixColor(nn.toString()));
                    }
                    user.addOnlinetime(500);
                    user.setLocation("false|^|" + ItemSerializer.serializeLocation(p.getLocation()));

                }
                continue;
            }
            else {
                p.kick("not properly loaded data - HalfSec");
            }
        }
    }
}
