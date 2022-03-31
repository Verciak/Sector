package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.*;
import xyz.rokkiitt.sector.DiscordWebhook;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.Permissions;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.guild.entity.EntityHead;
import xyz.rokkiitt.sector.objects.teleport.TeleportManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.waypoint.Waypoint;
import xyz.rokkiitt.sector.objects.waypoint.WaypointData;
import xyz.rokkiitt.sector.objects.wings.Wings;
import xyz.rokkiitt.sector.packets.PacketPlayerData;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.entity.data.*;
import cn.nukkit.scheduler.*;
import cn.nukkit.level.*;
import cn.nukkit.potion.*;
import cn.nukkit.*;
import cn.nukkit.item.*;
import cn.nukkit.event.player.*;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Pattern;

public class PlayerLoginListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(final PlayerPreLoginEvent event) {
        Player p = event.getPlayer();
        User u = UserManager.getUser(p.getName());
        if(u == null){
            UserManager.createUser(p);
        }
        event.getPlayer().setCheckMovement(false);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        e.setJoinMessage("");
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958721718388654114/DO8k2jBPB2_Oy7_MCbGjLSsc8zfO8V-p4aPa3_cGBvUYfx57oRj8IONjvEwIrAOUKiCJ");
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setAuthor("LOGI JOIN", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
        embedObject.setColor(new Color(0x00FF00));
        embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** dolaczyl na serwer!");
        embedObject.setTitle("");
        webhook.addEmbed(embedObject);
        try {
            webhook.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChangeSkin(final PlayerChangeSkinEvent e) {
        final Player p = e.getPlayer();
        final Skin newSkin = e.getSkin();
        if (newSkin == null || newSkin.isPersona() || !newSkin.isValid()) {
            e.setCancelled();
            return;
        }
        if (!p.getSkin().isPersona()) {
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                u.setSkin(newSkin);

                if (u.isIncognito()) {
                    e.setCancelled();
                    p.setSkin(Wings.applyIncognito(u.getSkin()));
                } else if (!u.getWings().isEmpty() && Wings.isExists(u.getWings())) {
                    e.setCancelled();
                    p.setSkin(Wings.apply(u.getSkin(), u.getWings()));
                }
            }
        }
    }

    @EventHandler
    public void handleFirstLogin(final PlayerRespawnEvent e) {
        if (e.isFirstSpawn()) {
            User u = UserManager.getUser(e.getPlayer().getName());
            if (u != null) {
                if(u.getLocation() != null){
                    e.getPlayer().teleport(ItemSerializer.getLocation(u.getLocation().replace("false|^|","")));
                }
                String[] loc = u.getLocation().split(Pattern.quote("|^|"));
                if (loc[0].equalsIgnoreCase("true")) {
                    Player pp = Server.getInstance().getPlayerExact(loc[1]);
                    if (pp != null && pp.isOnline())
                        e.setRespawnPosition((Position)pp.getLocation().add(0.0D, 1.0D, 0.0D));
                } else if (loc[0].equalsIgnoreCase("teleport")) {
                    Location des = ItemSerializer.getLocation(loc[1]);
                    des.add(0.0D, 2.0D, 0.0D);
                    e.setRespawnPosition((Position)des);
                } else if (loc[0].equalsIgnoreCase("random")) {
                    Location des = ItemSerializer.getLocation(loc[1]);
                    e.setRespawnPosition((Position)Util.getHighestLocation(des).add(0.0D, 2.0D, 0.0D));
                } else {
                    Location l = ItemSerializer.getLocation(loc[1]);
                    final Guild g = GuildManager.getGuild(l);
                    if (g != null) {
                        if (g.getTag().equalsIgnoreCase(u.getTag())) {
                            if (g.getHeart().isInHeart(l))
                                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                    public void onRun(int i) {
                                        if (g.getHead() == null)
                                            g.setHead(new EntityHead(g));
                                        g.getHead().spawnTo(e.getPlayer());
                                    }
                                },  40);
                            e.setRespawnPosition((Position)l.add(0.0D, 2.0D, 0.0D));
                        } else {
                            e.setRespawnPosition((Position)Util.getHighestLocation(l).add(0.0D, 2.0D, 0.0D));
                        }
                    } else {
                        e.setRespawnPosition((Position)l.add(0.0D, 2.0D, 0.0D));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLogin(final PlayerLocallyInitializedEvent e) {
        final Player p = e.getPlayer();
        p.setCheckMovement(false);
        p.noDamageTicks = 100;
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            if(u.getLocation() != null){
                e.getPlayer().teleport(ItemSerializer.getLocation(u.getLocation().replace("false|^|","")));
            }
            Cooldown.getInstance().add(u.getNickname(), "playerjoin", Float.valueOf(5.0F));
            u.calcWaypoints(p);
            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                public void onRun(int arg0) {
                    if (!p.getSkin().isValid() || p.getSkin().isPersona()) {
                        u.setSkin(Wings.applyDeafult(p.getSkin()));
                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                            public void onRun(int i) {
                                if (u.isIncognito()) {
                                    Main.getQuery().addQueue(() -> p.setSkin(Wings.applyIncognito(u.getSkin())));
                                } else if (!u.getWings().isEmpty() && Wings.isExists(u.getWings())) {
                                    Main.getQuery().addQueue(() -> p.setSkin(Wings.apply(u.getSkin(), u.getWings())));
                                } else {
                                    Main.getQuery().addQueue(() -> p.setSkin(u.getSkin()));
                                }
                            }
                        },30);
                    } else {
                        u.setSkin(p.getSkin());
                        if (u.isIncognito()) {
                            Main.getQuery().addQueue(() -> p.setSkin(Wings.applyIncognito(u.getSkin())));
                        } else if (!u.getWings().isEmpty() && Wings.isExists(u.getWings())) {
                            Main.getQuery().addQueue(() -> p.setSkin(Wings.apply(u.getSkin(), u.getWings())));
                        }
                    }
                    Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                        public void onRun(int arg0) {
                            u.updateWaypoints(p);
                            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                public void onRun(int i) {
                                    Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                        public void onRun(int i) {
                                            p.respawnToAll();
                                        }
                                    },10);
                                }
                            },60);
                        }
                    },10);
                }
            },10);
            String[] loc = u.getLocation().split(Pattern.quote("|^|"));
            if (loc[0].equalsIgnoreCase("true")) {
                Player pp = Server.getInstance().getPlayerExact(loc[1]);
                if (pp != null && pp.isOnline()) {
                    p.teleport(pp.getLocation().add(0.0D, 2.0D, 0.0D), null);
                        final Guild g = GuildManager.getGuild(p.getLocation());
                        if (g != null &&
                                g.getHeart().isInHeart(p.getLocation()))
                            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                public void onRun(int i) {
                                    if (g.getHeart().isInHeart(p.getLocation())) {
                                        if (g.getHead() == null)
                                            g.setHead(new EntityHead(g));
                                        g.getHead().spawnTo(e.getPlayer());
                                    }
                                }
                            },  40);
                    
                }
            } else if (loc[0].equalsIgnoreCase("teleport")) {
                Location l = ItemSerializer.getLocation(loc[1]);
                    final Guild g = GuildManager.getGuild(l);
                    if (g != null &&
                            g.getTag().equalsIgnoreCase(u.getTag()) &&
                            g.getHeart().isInHeart(l))
                        Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                            public void onRun(int i) {
                                if (g.getHeart().isInHeart(e.getPlayer().getLocation())) {
                                    if (g.getHead() == null)
                                        g.setHead(new EntityHead(g));
                                    g.getHead().spawnTo(e.getPlayer());
                                }
                            }
                        },  40);
                
                p.teleport(l.add(0.0D, 2.0D, 0.0D), null);
            } else if (loc[0].equalsIgnoreCase("random")) {
                Location des = ItemSerializer.getLocation(loc[1]);
                p.teleport(Util.getHighestLocation(des).add(0.0D, 2.0D, 0.0D), null);
            } else {
                Location l = ItemSerializer.getLocation(loc[1]);
                    final Guild g = GuildManager.getGuild(l);
                    if (g != null) {
                        if (g.getTag().equalsIgnoreCase(u.getTag())) {
                            if (g.getHeart().isInHeart(l))
                                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                                    public void onRun(int i) {
                                        if (g.getHeart().isInHeart(e.getPlayer().getLocation())) {
                                            if (g.getHead() == null)
                                                g.setHead(new EntityHead(g));
                                            g.getHead().spawnTo(e.getPlayer());
                                        }
                                    }
                                },  40);
                            p.teleport(l.add(0.0D, 1.0D, 0.0D), null);
                        } else {
                            p.teleport(Util.getHighestLocation(l).add(0.0D, 1.0D, 0.0D), null);
                        }
                    } else {
                        p.teleport(l.add(0.0D, 1.0D, 0.0D), null);
                    }
                
            }

            p.removeAllEffects();
            Effect ef = Effect.getEffect(16);
            ef.setVisible(false);
            ef.setAmbient(true);
            ef.setAmplifier(1);
            ef.setDuration(2147483647);
            p.addEffect(ef);
            if (!u.isFirstLogin()) {
                p.getInventory().clearAll();
                p.getInventory().setItem(0, Item.get(274));
                p.getInventory().setItem(1, Item.get(5, 0, 32));
                p.getInventory().setItem(2, Item.get(364, 0, 13));
                p.getInventory().setItem(3, Item.get(364, 0, 37));
                p.getInventory().setItem(4, Item.get(130));
                u.setProtection(System.currentTimeMillis() + Time.SECOND.getTime(60));
                u.setFirstLogin(true);
            }
            Permissions.setupPermissions(p, u);
        } else {
            p.kick("not properly loaded user data - Login");
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        e.setQuitMessage("");
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958722112493875250/SaJ12Vw5aTO2X0rUL71dICuorBrgJHtOTivhl582vHMEmrhdkHEZWDvshYymILuvoWT2");
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setAuthor("LOGI LEFT", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
        embedObject.setColor(new Color(0x00FF00));
        embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** opuscil serwer!");
        embedObject.setTitle("");
        webhook.addEmbed(embedObject);
        try {
            webhook.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final Player p = e.getPlayer();
        TeleportManager.removePlayer(p);
        Permissions.removePermissions(p);

        final Guild g = GuildManager.getGuild(p.getLocation());
        if (g != null && g.getHeart().isInHeart(p.getLocation()) && g.getHead() != null) {
            g.getHead().despawnFrom(p);
            if (g.getHead().getViewers().isEmpty()) {
                g.getHead().close();
                g.setHead(null);
            }
        }
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            u.setLocation("false|^|" + ItemSerializer.serializeLocation(p.getLocation()));

            final PacketPlayerData paa = new PacketPlayerData();
            paa.nickname = u.getNickname();
            if (p.getInventory() == null) {
                return;
            }
            if (CombatManager.isContains(p.getName())) {
                Util.sendLog(p.getName(), "wylogowal sie podczas walki");
                CombatListener.schedulePlayerDeadbyEntity(p, Server.getInstance().getPlayerExact(u.getLastattacker()));
            }
            for (final Waypoint wp : u.getActiveWaypoints()) {
                wp.close();
            }
            paa.waypoints = WaypointData.serialize(u);
        }
    }
}
