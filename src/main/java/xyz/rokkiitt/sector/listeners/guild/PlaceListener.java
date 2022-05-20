package xyz.rokkiitt.sector.listeners.guild;

import java.util.*;
import java.util.concurrent.*;
import cn.nukkit.event.block.*;
import cn.nukkit.command.*;
import cn.nukkit.event.player.PlayerInteractEvent;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.listeners.PlayerInteractListeners;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.antigrief.AntiGrief;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.block.Slime;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.stonefarms.Stone;
import xyz.rokkiitt.sector.objects.stonefarms.StoneManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.BlockUtils;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.level.*;
import cn.nukkit.item.*;
import cn.nukkit.event.*;
import cn.nukkit.entity.*;

public class PlaceListener implements Listener
{
    private final Map<UUID, Long> times;
    
    public PlaceListener() {
        this.times = new ConcurrentHashMap<UUID, Long>();
    }



    @EventHandler
    public void onInt(PlayerInteractEvent e){
        if(e.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(final BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        final int m = b.getId();
        final Location l = b.getLocation();
        final Item ss = p.getInventory().getItemInHand();
        if (b instanceof BlockRedstoneWire || b instanceof BlockPiston || b instanceof BlockRedstoneDiode) {
            e.setCancelled();
            return;
        }
        if (p.hasPermission(Perms.PLACEBYPASS.getPermission())) {
            if (m == 121) {
                if (Util.hasNBTTag(ss, "stoniarka")) {
                    final Stone s = StoneManager.checkStone(b.getLocationHash());
                    if (s != null) {
                        e.setCancelled(true);
                    }
                    else if (StoneManager.AddStone(b.getLocationHash(), l)) {
                        Server.getInstance().getScheduler().scheduleDelayedTask(() -> b.getLevel().setBlockAt(l.getFloorX(), l.getFloorY(), l.getFloorZ(), 1), 1);                    }
                }
                else {
                    if (l.getFloorY() >= 80 && !p.isSneaking() && AntiGrief.AddBlock(b)) {
                        final Long time = this.times.get(p.getUniqueId());
                        if (time == null || System.currentTimeMillis() - time >= Time.SECOND.getTime(5)) {
                            Util.sendMessage((CommandSender)p, Settings.getMessage("antigriefplace"));
                            this.times.put(p.getUniqueId(), System.currentTimeMillis());
                        }
                    }
                }
            }
            else if (m == 159) {
                if (Util.hasNBTTag(ss, "boyfarmer")) {
                    final Guild g = GuildManager.getGuild(l);
                    if (g == null) {
                        e.setCancelled(true);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerguild"));
                        return;
                    }
                    if (l.getFloorY() <= 100) {
                        BlockUtils.boyfarmer(g, l);
                        return;
                    }
                    Util.sendMessage((CommandSender)p, Settings.getMessage("farmerlimit"));
                    e.setCancelled(true);
                }
                else if (Util.hasNBTTag(ss, "sandfarmer")) {
                    final Guild g = GuildManager.getGuild(l);
                    if (g == null) {
                        e.setCancelled(true);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerguild"));
                        return;
                    }
                    if (l.getFloorY() <= 100) {
                        BlockUtils.sandfarmer(g, l);
                        return;
                    }
                    Util.sendMessage((CommandSender)p, Settings.getMessage("farmerlimit"));
                    e.setCancelled(true);
                }
                else if (Util.hasNBTTag(ss, "kopacz")) {
                    final Guild g = GuildManager.getGuild(l);
                    if (g == null) {
                        e.setCancelled(true);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerguild"));
                        return;
                    }
                    if (l.getFloorY() <= 100) {
                        BlockUtils.kopacz(g, l);
                        return;
                    }
                    Util.sendMessage((CommandSender)p, Settings.getMessage("farmerlimit"));
                    e.setCancelled(true);
                }
            }
        }
        else {
            if (Config.isSpawn(p.getLocation())) {
                Util.sendMessage((CommandSender)p, Settings.getMessage("placespawn"));
                e.setCancelled(true);
                return;
            }
            else {
                if (MeteoriteManager.isOpenedListener(l)) {
                    this.isSlime(p, b);
                    e.setCancelled(true);
                    return;
                }
                final Guild g = GuildManager.getGuild(l);
                if (g != null) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null) {
                        if (u.getTag().equalsIgnoreCase(g.getTag())) {
                            if (g.getHeart().isInHeart(b.getLocation())) {
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildplaceheart"));
                                e.setCancelled(true);
                                return;
                            }
                            if (g.getExplodeTime() >= System.currentTimeMillis()) {
                                this.isSlime(p, b);
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildplacetnt").replace("{TIME}", Util.formatTime(g.getExplodeTime() - System.currentTimeMillis())));
                                return;
                            }
                            if (!u.hasPermission("1")) {
                                this.isSlime(p, b);
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "stawiania blokow"));
                                return;
                            }
                            if (m == 49 && !u.hasPermission("27")) {
                                e.setCancelled(true);
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "stawiania obsydianu"));
                                return;
                            }
                            if (m == 159) {
                                if (Util.hasNBTTag(ss, "boyfarmer")) {
                                    if (!u.hasPermission("18")) {
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "stawiania farmerow"));
                                        e.setCancelled(true);
                                        return;
                                    }
                                    if (CombatManager.isContains(p.getName())) {
                                        e.setCancelled();
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerspvp"));
                                        return;
                                    }
                                    if (l.getFloorY() <= 100) {
                                        BlockUtils.boyfarmer(g, l);
                                        return;
                                    }
                                    Util.sendMessage((CommandSender)p, Settings.getMessage("farmerlimit"));
                                    e.setCancelled(true);
                                    return;
                                }
                                else if (Util.hasNBTTag(ss, "sandfarmer")) {
                                    if (!u.hasPermission("18")) {
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "stawiania farmerow"));
                                        e.setCancelled(true);
                                        return;
                                    }
                                    if (CombatManager.isContains(p.getName())) {
                                        e.setCancelled();
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerspvp"));
                                        return;
                                    }
                                    if (l.getFloorY() <= 100) {
                                        BlockUtils.sandfarmer(g, l);
                                        return;
                                    }
                                    Util.sendMessage((CommandSender)p, Settings.getMessage("farmerlimit"));
                                    e.setCancelled(true);
                                    return;
                                }
                                else if (Util.hasNBTTag(ss, "kopacz")) {
                                    if (!u.hasPermission("18")) {
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "stawiania farmerow"));
                                        e.setCancelled(true);
                                        return;
                                    }
                                    if (CombatManager.isContains(p.getName())) {
                                        e.setCancelled();
                                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerspvp"));
                                        return;
                                    }
                                    if (l.getFloorY() <= 100) {
                                        BlockUtils.kopacz(g, l);
                                        return;
                                    }
                                    Util.sendMessage((CommandSender)p, Settings.getMessage("farmerlimit"));
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                            else {
                                if (m == 46 && !u.hasPermission("2")) {
                                    e.setCancelled(true);
                                    Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "stawiania tnt"));
                                    return;
                                }
                                if (m == 121 && Util.hasNBTTag(ss, "stoniarka")) {
                                    final Stone s2 = StoneManager.checkStone(b.getLocationHash());
                                    if (s2 != null) {
                                        e.setCancelled(true);
                                        return;
                                    }
                                    if (StoneManager.AddStone(b.getLocationHash(), l)) {
                                        Server.getInstance().getScheduler().scheduleDelayedTask(() -> b.getLevel().setBlockAt(l.getFloorX(), l.getFloorY(), l.getFloorZ(), 1), 1);                                    }
                                }
                            }
                            g.addLogblock(p.getName(), b.getLocationHash(), b.getName().toUpperCase(), "place", System.currentTimeMillis());
                        }
                        else {
                            this.isSlime(p, b);
                            e.setCancelled(true);
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildplaceenemy"));
                        }
                    }
                    else {
                        e.setCancelled(true);
                        p.kick(Util.fixColor("&9not properly loaded user data - BlockPlace"));
                    }
                }
                else if (m == 121) {
                    if (Util.hasNBTTag(ss, "stoniarka")) {
                        final Stone s3 = StoneManager.checkStone(b.getLocationHash());
                        if (s3 != null) {
                            e.setCancelled(true);
                            return;
                        }
                        if (StoneManager.AddStone(b.getLocationHash(), l)) {
                            final Block block3;
                            final Location location3;
                            Server.getInstance().getScheduler().scheduleDelayedTask(() -> b.getLevel().setBlockAt(l.getFloorX(), l.getFloorY(), l.getFloorZ(), 1), 1);                        }
                    }
                    else {
                        if (l.getFloorY() > 70 && !p.isSneaking() && AntiGrief.AddBlock(b)) {
                            final Long time2 = this.times.get(p.getUniqueId());
                            if (time2 == null || System.currentTimeMillis() - time2 >= Time.SECOND.getTime(5)) {
                                Util.sendMessage((CommandSender)p, Settings.getMessage("antigriefplace"));
                                this.times.put(p.getUniqueId(), System.currentTimeMillis());
                            }
                        }
                    }
                }
                else if (m == 159) {
                    if (Util.hasNBTTag(ss, "boyfarmer")) {
                        e.setCancelled(true);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerguild"));
                        return;
                    }
                    if (Util.hasNBTTag(ss, "sandfarmer")) {
                        e.setCancelled(true);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerguild"));
                        return;
                    }
                    if (Util.hasNBTTag(ss, "kopacz")) {
                        e.setCancelled(true);
                        Util.sendMessage((CommandSender)p, Settings.getMessage("farmerguild"));
                        return;
                    }
                    if (l.getFloorY() > 70 && !p.isSneaking() && AntiGrief.AddBlock(b)) {
                        final Long time2 = this.times.get(p.getUniqueId());
                        if (time2 == null || System.currentTimeMillis() - time2 >= Time.SECOND.getTime(5)) {
                            Util.sendMessage((CommandSender)p, Settings.getMessage("antigriefplace"));
                            this.times.put(p.getUniqueId(), System.currentTimeMillis());
                        }
                    }
                }
                else {
                    if (l.getFloorY() > 70 && !p.isSneaking() && AntiGrief.AddBlock(b)) {
                        final Long time2 = this.times.get(p.getUniqueId());
                        if (time2 == null || System.currentTimeMillis() - time2 >= Time.SECOND.getTime(5)) {
                            Util.sendMessage((CommandSender)p, Settings.getMessage("antigriefplace"));
                            this.times.put(p.getUniqueId(), System.currentTimeMillis());
                        }
                    }
                }
            }
        }
    }
    
    private void isSlime(final Player player, final Block block) {
        if (block instanceof Slime && block.floor().equals((Object)player.floor().down())) {
            player.fallDistance = (float)(player.highestPosition - player.y);
            block.onEntityCollide((Entity)player);
        }
    }
}
