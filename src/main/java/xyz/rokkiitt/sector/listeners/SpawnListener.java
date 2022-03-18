package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.entity.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.event.block.*;
import cn.nukkit.event.player.*;
import cn.nukkit.command.*;
import cn.nukkit.*;
import cn.nukkit.item.enchantment.*;
import cn.nukkit.event.*;

public class SpawnListener implements Listener
{
    @EventHandler
    public void onProjectile(final ProjectileLaunchEvent e) {
        if (Config.isSpawn(e.getEntity().getLocation())) {
            e.setCancelled();
        }
    }
    
    @EventHandler
    public void onSpread(final BlockSpreadEvent e) {
        if (Config.isSpawn(e.getBlock().getLocation())) {
            e.setCancelled();
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onDrop(final PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        if (Settings.FREEZE_TIME >= System.currentTimeMillis()) {
            e.setCancelled(true);
            return;
        }
        if (e.getItem() != null && e.getItem().getId() == 278) {
            final Enchantment eff = e.getItem().getEnchantment(15);
            if (eff != null && eff.getLevel() >= 6) {
                e.setCancelled(true);
                return;
            }
        }
        if (e.getItem() != null && e.getItem().getId() == 450) {
            e.setCancelled();
            return;
        }
        if (Config.isSpawn(e.getPlayer().getLocation()) && !p.hasPermission(Perms.SPAWNBYPASS.getPermission())) {
            e.setCancelled(true);
            Util.sendMessage((CommandSender)p, Settings.getMessage("dropitemspawn"));
            return;
        }
    }
}
