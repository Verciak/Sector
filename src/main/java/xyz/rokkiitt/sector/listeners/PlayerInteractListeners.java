package xyz.rokkiitt.sector.listeners;

import bimopower.musiccontroller.api.MusicControllerApi;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.network.protocol.PlaySoundPacket;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.cobblex.CobblexManager;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.objects.enchant.EnchantInventory;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteRegion;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.premiumcase.CaseInv;
import xyz.rokkiitt.sector.objects.premiumcase.CaseManager;
import xyz.rokkiitt.sector.objects.premiumcase.PremiumCaseGUI;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.DepositUtil;
import xyz.rokkiitt.sector.utils.SpaceUtil;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.entity.*;
import cn.nukkit.*;
import cn.nukkit.math.*;
import cn.nukkit.item.*;
import java.util.*;
import cn.nukkit.event.*;
import cn.nukkit.event.player.*;
import cn.nukkit.block.*;
import cn.nukkit.level.*;

import java.util.concurrent.*;

public class PlayerInteractListeners implements Listener
{
    private List<Integer> enchants;
    private static final Map<String, Long> times;
    
    public PlayerInteractListeners() {
        this.enchants = Arrays.asList(305, 313, 317, 309, 301, 303, 311, 315, 307, 299, 304, 312, 316, 308, 300, 302, 310, 314, 306, 298, 276, 283, 267, 272, 268, 279, 286, 258, 275, 271, 277, 284, 256, 273, 269, 293, 294, 292, 291, 290, 278, 285, 257, 274, 270, 261);
    }
    
    @EventHandler
    public void onEntityClick(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        final Entity entity = e.getEntity();
        if (!Cooldown.getInstance().has(p, "entityClick") && entity instanceof Player) {
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                final Player p2 = (Player)entity;
                final User u2 = UserManager.getUser(p2.getName());
                if (u2 != null) {
                    int winPoints;
                    if (u2.getPoints() <= 0) {
                        winPoints = 1;
                    }
                    else {
                        double percent = 7.0;
                        if (Settings.EVENT_POINTS >= System.currentTimeMillis()) {
                            percent *= 2.0;
                        }
                        int attacker_set = (int) Math.abs((u2.getPoints() / 100) *  percent);
                        if (attacker_set <= 0) {
                            attacker_set = 1;
                        }
                        else if (attacker_set > ((Settings.EVENT_POINTS >= System.currentTimeMillis()) ? 600 : 300)) {
                            attacker_set = ((Settings.EVENT_POINTS >= System.currentTimeMillis()) ? 600 : 300);
                        }
                        winPoints = attacker_set;
                    }
                    final int losePoints = Math.abs(winPoints / 2);
                    Util.sendMessage((CommandSender)p, Settings.getMessage("playerpoints").replace("{P}", p2.getName()).replace("{PKT}", "" + winPoints).replace("{PKT2}", "" + losePoints));
                }
            }
            Cooldown.getInstance().add(p, "entityClick", 3.0f);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onConsume(final PlayerItemConsumeEvent e) {
        final Item item = e.getItem();
        final Player p = e.getPlayer();
        if (item.getId() == 373 && Util.hasNBTTag(item, "antynogi")) {
            if (!CombatManager.isContains(p.getName())) {
                e.setCancelled(true);
                Util.sendMessage((CommandSender)p, Settings.getMessage("antynogicombat"));
            }
            else {
                final User u = UserManager.getUser(p.getName());
                if (u != null) {
                    double dist = 999.0;
                    Player player = null;
                    for (final Player all : Server.getInstance().getOnlinePlayers().values()) {
                        final double di = all.getPosition().distance((Vector3)p.getPosition());
                        if (di < 5.0 && di <= dist && u.isAssister(all)) {
                            dist = di;
                            player = all;
                        }
                    }
                    if (player != null) {
                        p.teleport((Location)player);
                    }
                }
                else {
                    e.setCancelled(true);
                    p.kick("&9not properly loaded user data - Consume");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onInteract2(final PlayerInteractEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        if (Settings.FREEZE_TIME >= System.currentTimeMillis()) {
            e.setCancelled(true);
            return;
        }
        if (b != null) {
            if ((b.getId() == 58 || b.getId() == 130 || b.getId() == 116 || b.getId() == 54 || b.getId() == 146) && CombatManager.isContains(p.getName()) && e.getAction().equals((Object)PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                Util.sendMessage((CommandSender)p, Settings.getMessage("openincombat"));
            }
            else if (b.getId() == 122) {
                e.setCancelled(true);
                b.getLevel().setBlockAt(b.getLocation().getFloorX(), b.getLocation().getFloorY(), b.getLocation().getFloorZ(), 0);
                final MeteoriteRegion mr = MeteoriteManager.getRegion();
                if (mr != null && mr.isInCuboid(b.getLocation()) && !MeteoriteManager.isOpened()) {
                    MeteoriteManager.setOpened(true);
                    MeteoriteManager.getDrop(p);
                }
            }
            else if (b.getId() == 116 && e.getAction().equals((Object)PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                if (e.getItem() == null || !this.enchants.contains(e.getItem().getId())) {
                    Util.sendMessage((CommandSender)p, Settings.getMessage("cantenchant"));
                    return;
                }
                if ((Config.isSpawn(p.getLocation()) && !p.hasPermission(Perms.SPAWNBYPASS.getPermission()))) {
                    return;
                }
                int books = 0;
                for (final Location loc : SpaceUtil.getWalls(b.getLocation(), 2, 1)) {
                    if (loc.getLevelBlock().getId() == 47) {
                        ++books;
                    }
                }
                if (!Settings.ENABLE_ENCHANT && !e.getItem().isPickaxe()) {
                    Util.sendMessage((CommandSender)p, Settings.getMessage("enchantdisabled"));
                    return;
                }
                new EnchantInventory(p, e.getPlayer().getInventory().getItemInHand(), books);
            }
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlace(final BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final Item item = e.getItem();
        if (item.getId() == Item.CHEST && Util.hasNBTTag(item, "premiumcase")) {
            e.setCancelled(true);
            if (CaseManager.isInCase(p)) {
                p.sendMessage(Util.fixColor("&cOtwierasz juz magiczna skrzynke!"));
                return;
            }
            item.setCount(item.getCount() - 1);
            p.getInventory().setItemInHand(item);
            new PremiumCaseGUI(p);

        }
        if (item.getId() == 122) {
            if (Util.hasNBTTag(item, "pandora")) {
                e.setCancelled(true);
                if (!CombatManager.isContains(p.getName())) {
                    if (Settings.ENABLE_PANDORA) {
                        item.setCount(item.getCount() - 1);
                        p.getInventory().setItemInHand(item);
                        PandoraManager.getDrop(p);
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("pandoraopen"));
                    }
                }
                else {
                    Util.sendMessage((CommandSender)p, Settings.getMessage("openincombat"));
                }
            }
        }
        else if (item.getId() == 48) {
            if (Util.hasNBTTag(item, "cx")) {
                e.setCancelled(true);
                if (!CombatManager.isContains(p.getName())) {
                    item.setCount(item.getCount() - 1);
                    CobblexManager.getDrop(p);
                    p.getInventory().setItemInHand(item);
                }
                else {
                    Util.sendMessage((CommandSender)p, Settings.getMessage("openincombat"));
                }
            }
        }
        else if (item.getId() == 46) {
            if (Util.hasNBTTag(item, "rzucanetnt")) {
                e.setCancelled(true);
                if (!Config.isSpawn(e.getPlayer().getLocation())) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null && DepositUtil.reduce(p, u)) {
                        item.setCount(item.getCount() - 1);
                        final Entity tnt = Entity.createEntity("PrimedTnt", (Position)p.getLocation(), new Object[0]);
                        tnt.spawnToAll();
                        tnt.setMotion(p.getLocation().getDirectionVector().normalize().multiply(0.7));
                        p.getInventory().setItemInHand(item);
                    }
                }
            }
        }
    }


    
    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(final PlayerInteractEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player p = e.getPlayer();
        final Item item = e.getItem();
        if (item != null && (e.getAction().equals((Object)PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) || e.getAction().equals((Object)PlayerInteractEvent.Action.RIGHT_CLICK_AIR))) {
           if (item.getId() == 340) {
                if (Util.hasNBTTag(item, "vouchervip")) {
                    e.setCancelled(true);
                    if (Settings.ENABLE_VOUCHER) {
                        final User u = UserManager.getUser(p.getName());
                        if (u != null) {
                            item.setCount(item.getCount() - 1);
                            p.getInventory().setItemInHand(item);
                            u.setRank("vip");
                            Util.sendMessage((CommandSender)p, Settings.getMessage("vouchersucces").replace("{WHAT}", "vipa"));
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("voucherdisabled"));
                    }
                }
                else if (Util.hasNBTTag(item, "vouchersvip")) {
                    e.setCancelled(true);
                    if (Settings.ENABLE_VOUCHER) {
                        final User u = UserManager.getUser(p.getName());
                        if (u != null) {
                            item.setCount(item.getCount() - 1);
                            p.getInventory().setItemInHand(item);
                            u.setRank("svip");
                            
                            Util.sendMessage((CommandSender)p, Settings.getMessage("vouchersucces").replace("{WHAT}", "svipa"));
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("voucherdisabled"));
                    }
                }
                else if (Util.hasNBTTag(item, "vouchersponsor")) {
                    e.setCancelled(true);
                    if (Settings.ENABLE_VOUCHER) {
                        final User u = UserManager.getUser(p.getName());
                        if (u != null) {
                            item.setCount(item.getCount() - 1);
                            p.getInventory().setItemInHand(item);
                            u.setRank("sponsor");
                            
                            Util.sendMessage((CommandSender)p, Settings.getMessage("vouchersucces").replace("{WHAT}", "sponsora"));
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("voucherdisabled"));
                    }
                }
                else if (Util.hasNBTTag(item, "voucherturbo10")) {
                    e.setCancelled(true);
                    if (Settings.ENABLE_VOUCHER) {
                        final User u = UserManager.getUser(p.getName());
                        if (u != null) {
                            item.setCount(item.getCount() - 1);
                            p.getInventory().setItemInHand(item);
                            final User user = u;
                            u.setTurbodrop(u.getTurbodrop() + (Time.MINUTE.getTime(10) * 2));
                            
                            Util.sendMessage((CommandSender)p, Settings.getMessage("vouchersucces").replace("{WHAT}", "10 minut turbodropu"));
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("voucherdisabled"));
                    }
                }
                else if (Util.hasNBTTag(item, "voucherturbo30")) {
                    e.setCancelled(true);
                    if (Settings.ENABLE_VOUCHER) {
                        final User u = UserManager.getUser(p.getName());
                        if (u != null) {
                            item.setCount(item.getCount() - 1);
                            p.getInventory().setItemInHand(item);
                            final User user2 = u;
                            u.setTurbodrop(u.getTurbodrop() + (Time.MINUTE.getTime(30) * 2));
                            
                            Util.sendMessage((CommandSender)p, Settings.getMessage("vouchersucces").replace("{WHAT}", "30 minut turbodropu"));
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("voucherdisabled"));
                    }
                }
                else if (Util.hasNBTTag(item, "voucherturbo60")) {
                    e.setCancelled(true);
                    if (Settings.ENABLE_VOUCHER) {
                        final User u = UserManager.getUser(p.getName());
                        if (u != null) {
                            item.setCount(item.getCount() - 1);
                            p.getInventory().setItemInHand(item);
                            final User user3 = u;
                            u.setTurbodrop(u.getTurbodrop() + (Time.MINUTE.getTime(60) * 2));
                            
                            Util.sendMessage((CommandSender)p, Settings.getMessage("vouchersucces").replace("{WHAT}", "60 minut turbodropu"));
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("voucherdisabled"));
                    }
                }
            }
        }
    }
    
    static {
        times = new ConcurrentHashMap<String, Long>();
    }
}
