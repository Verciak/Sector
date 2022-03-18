package xyz.rokkiitt.sector.listeners;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.objects.teleport.TeleportManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.entity.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.*;
import cn.nukkit.item.*;
import cn.nukkit.math.*;
import cn.nukkit.scheduler.*;
import cn.nukkit.entity.projectile.*;

import java.util.*;
import cn.nukkit.event.*;

public class CombatListener implements Listener
{
    @EventHandler
    public void onEntityDamageByChildEntity(final EntityDamageByChildEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Entity projectile = e.getChild();
        if (projectile instanceof EntityArrow || projectile instanceof EntitySnowball) {
            final Entity damager = e.getDamager();
            final Entity player = e.getEntity();
            if (damager instanceof Player && player instanceof Player && !damager.equals((Object)player) && !Cooldown.getInstance().has(player.getName(), "arrowhp")) {
                final float health = (player.getHealth() + player.getAbsorption()) / 2.0f;
                Util.sendMessage((CommandSender)damager, Settings.getMessage("arrowshowhp").replace("{HP}", String.valueOf(health)).replace("{P}", player.getName()));
                Cooldown.getInstance().add(player.getName(), "arrowhp", 1.0f);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onDamage(final EntityDamageEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (Config.isSpawn(e.getEntity().getLocation()) || Settings.FREEZE_TIME >= System.currentTimeMillis() || Cooldown.getInstance().has(e.getEntity().getName(), "playerjoin")) {
            e.setCancelled(true);
            return;
        }
        if (e.getDamage() < 0.0) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player)e.getEntity();
        if (Cooldown.getInstance().has(p.getName(), "dead")) {
            e.setCancelled();
            return;
        }
        if (p.getHealth() - e.getFinalDamage() < 1.0f) {
            CombatManager.removePlayer(p);
            Cooldown.getInstance().add(p, "dead", 8.0f);
            Entity killer = null;
            switch (e.getCause()) {
                case ENTITY_ATTACK:
                case PROJECTILE: {
                    if (e instanceof EntityDamageByEntityEvent) {
                        final Entity ent = ((EntityDamageByEntityEvent)e).getDamager();
                        if (ent instanceof Player) {
                            killer = ent;
                        }
                        else {
                            final User u = UserManager.getUser(p.getName());
                            if (u.getLastattacker() != null && !u.getLastattacker().isEmpty()) {
                                final Player pp = Server.getInstance().getPlayerExact(u.getLastattacker());
                                if (pp != null) {
                                    killer = (Entity)pp;
                                }
                            }
                        }
                        break;
                    }
                    break;
                }
                default: {
                    final User u2 = UserManager.getUser(p.getName());
                    if (u2 == null || u2.getLastattacker() == null || u2.getLastattacker().isEmpty()) {
                        break;
                    }
                    final Player pp2 = Server.getInstance().getPlayerExact(u2.getLastattacker());
                    if (pp2 != null) {
                        killer = (Entity)pp2;
                        break;
                    }
                    break;
                }
            }
            p.setHealth((float)p.getMaxHealth());
            p.getFoodData().setLevel(p.getFoodData().getMaxLevel());
            schedulePlayerDeadbyEntity(p, killer);
            p.removeAllEffects();
            p.extinguish();
            final List<Item> drops = new ArrayList<Item>();
            if (p.getCursorInventory() != null) {
                final Item cursor = p.getCursorInventory().getItem(0);
                if (!cursor.isNull()) {
                    drops.add(cursor);
                }
            }
            drops.addAll(Arrays.asList(p.getDrops()));
            if (killer != null) {
                for (final Item i : drops) {
                    Util.giveItemOnDeath((Player)killer, p, i);
                }
            }
            else {
                for (final Item item : drops) {
                    p.getLevel().dropItem((Vector3)p, item, (Vector3)null, false, 30);
                }
            }
            p.getInventory().clearAll();
            p.getCursorInventory().clearAll();
            p.setExperience(0, 0);
            e.setCancelled(true);
            final Entity finalKiller = killer;
            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                public void onRun(final int i) {
                    for (final Player b : new ArrayList<Player>(Server.getInstance().getOnlinePlayers().values())) {
                        if (b != null && p != b) {
                            b.hidePlayer(p);
                            p.hidePlayer(b);
                        }
                    }
                }
            }, 2);
            return;
        }
        if (e instanceof EntityDamageByEntityEvent) {
            final Entity damager = ((EntityDamageByEntityEvent)e).getDamager();
            if (e.getEntity() == damager || damager instanceof EntityEnderPearl) {
                e.setCancelled();
                return;
            }
            if (damager instanceof Player && (Cooldown.getInstance().has((Player)damager, "slotchange") || Cooldown.getInstance().has((Player)damager, "dead"))) {
                e.setCancelled();
                return;
            }
            ((EntityDamageByEntityEvent)e).setKnockBack(0.356f);
            ((EntityDamageByEntityEvent)e).setAttackCooldown(9);
            if (!(damager instanceof Player)) {
                return;
            }
            final Player d = (Player)damager;
            final User u = UserManager.getUser(p.getName());
            final User u3 = UserManager.getUser(d.getName());
            if (u != null && u3 != null) {
                if (u.hasProtection() || u3.hasProtection() || u.isGod() || u3.isGod() || u.isVanish() || u3.isVanish()) {
                    e.setCancelled(true);
                    return;
                }
                if (!u.getTag().equalsIgnoreCase("NIEPOSIADA") && !u3.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                    if (u.getTag().equalsIgnoreCase(u3.getTag())) {
                        if (u3.isPvp() && u.isPvp()) {
                            e.setDamage(0.0f);
                            return;
                        }
                        e.setCancelled(true);
                        return;
                    }
                    else if (u.getAlliances().contains(u3.getTag()) && u3.getAlliances().contains(u.getTag())) {

                            if (u.isAllypvp() && u3.isAllypvp()) {
                                e.setDamage(0.0f);
                                return;
                            }
                            e.setCancelled(true);
                            return;
                    }
                    else {
                        u.addAssistPlayer(d, e.getDamage());
                        u3.addAssistPlayer(p, 0.0);
                    }
                }
                else {
                    u.addAssistPlayer(d, e.getDamage());
                    u3.addAssistPlayer(p, 0.0);
                }
                u.setLastattacker(d.getName());
                u3.setLastattacker(p.getName());
            }
            TeleportManager.removePlayer(p);
            TeleportManager.removePlayer(d);
            CombatManager.addPlayer(p, System.currentTimeMillis() + Time.SECOND.getTime(31));
            CombatManager.addPlayer(d, System.currentTimeMillis() + Time.SECOND.getTime(31));
        }
    }
    
    public static void schedulePlayerDeadbyEntity(final Player p, final Entity killer) {
        if (killer instanceof Player) {
            final Player k = (Player) killer;
            CombatManager.removePlayer(p);
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                final User uk = UserManager.getUser(k.getName());
                if (uk != null) {
                    if (u != uk) {
                        if (uk.isLastKill(p)) {
                            Util.sendMessage((CommandSender) k, Settings.getMessage("killedsameperson"));
                            uk.addLastKill(p);
                            u.addDeaths(1);
                            u.removePoints(50);
                            u.clearAssistAttackers();
                            Util.createBackup(p, k.getName(), 50);
                        } else {
                            double percent = 7.0;
                            if (Settings.EVENT_POINTS >= System.currentTimeMillis()) {
                                percent *= 2.0;
                            }
                            int winPoints;
                            if (u.getPoints() <= 0) {
                                winPoints = 1;
                            } else {
                                int attacker_set = (int) Math.abs((u.getPoints() / 100) * percent);
                                if (attacker_set <= 0) {
                                    attacker_set = 1;
                                } else if (attacker_set > ((Settings.EVENT_POINTS >= System.currentTimeMillis()) ? 600 : 300)) {
                                    attacker_set = ((Settings.EVENT_POINTS >= System.currentTimeMillis()) ? 600 : 300);
                                }
                                winPoints = attacker_set;
                            }
                            final int losePoints = Math.abs(winPoints / 2);
                            u.addDeaths(1);
                            uk.addKills(1);
                            u.removePoints(Math.abs(losePoints));
                            uk.addPoints(Math.abs(winPoints));
                            Util.createBackup(p, k.getName(), losePoints);
                            final Player ap = u.getAssister(k);
                            String mes;
                            if (ap != null) {
                                final User up = UserManager.getUser(ap.getName());
                                if (up != null) {
                                    int assPoints = Math.abs(winPoints / 2);
                                    if (assPoints <= 0) {
                                        assPoints = 1;
                                    }
                                    up.addAssist(1);
                                    up.addPoints(assPoints);
                                    mes = Settings.getMessage("killedwithassist");
                                    mes = mes.replace("{ASSISTER}", getNickname(false, up));
                                    mes = mes.replace("{ASSISTPOINTS}", "+" + Integer.toString(assPoints));
                                } else {
                                    mes = Settings.getMessage("killedsolo");
                                }
                            } else {
                                mes = Settings.getMessage("killedsolo");
                            }
                            mes = mes.replace("{PLAYER}", getNickname(true, u));
                            mes = mes.replace("{LOSEPOINTS}", "-" + Integer.toString(losePoints));
                            mes = mes.replace("{WINPOINTS}", "+" + Integer.toString(winPoints));
                            mes = mes.replace("{KILLER}", getNickname(false, uk));
                            mes = mes.replace("{PGUILD}", u.getTag().equalsIgnoreCase("NIEPOSIADA") ? "" : ("&8[" + getColor(u, u.getTag()) + u.getTag().toUpperCase() + "&8]"));
                            mes = mes.replace("{KGUILD}", uk.getTag().equalsIgnoreCase("NIEPOSIADA") ? "" : ("&8[" + getColor(u, uk.getTag()) + uk.getTag().toUpperCase() + "&8]"));

                            uk.addLastKill(p);
                            Server.getInstance().broadcastMessage(Util.fixColor(mes));
                            u.clearAssistAttackers();
                            Util.sendLog(uk.getNickname(), " zabil gracza " + u.getNickname());
                            Util.sendLog(u.getNickname(), " zginal od gracza " + uk.getNickname());
                        }
                    } else {
                        u.addDeaths(1);
                        u.removePoints(50);
                        u.clearAssistAttackers();
                        Util.createBackup(p, null, 50);
                    }
                } else {
                    u.addDeaths(1);
                    u.removePoints(50);
                    u.clearAssistAttackers();
                    Util.createBackup(p, null, 50);
                }
            }
        } else {
            final User u2 = UserManager.getUser(p.getName());
            if (u2 != null) {
                Util.sendInformation("CHAT||" + Settings.getMessage("killedunknown").replace("{PLAYER}", getNickname(true, u2)));
                u2.addDeaths(1);
                u2.removePoints(50);
                u2.clearAssistAttackers();
                Util.createBackup(p, null, 50);
            }
        }
    }

    private static String getColor(User u, String second) {
        if (u.getTag().equalsIgnoreCase(second))
            return "&a";
        if (u.getAlliances().contains(second.toLowerCase()))
            return "&b";
        return "&c";
    }
    
    public static String getNickname(final boolean isDead, final User u) {
        if (!u.isIncognito()) {
            return u.getNickname();
        }
        if (isDead) {
            if (u.isIncognitoDead()) {
                return u.getIncognito();
            }
            return u.getNickname();
        }
        else {
            if (u.isIncognitoKill()) {
                return u.getIncognito();
            }
            return u.getNickname();
        }
    }
}
