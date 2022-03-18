package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.*;
import cn.nukkit.event.*;
import java.util.*;
import java.util.concurrent.*;

public class PrePlayerCommandListener implements Listener
{
    private static List<String> pvpcmd;
    private static final Map<UUID, Long> times;
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent e) {
        if ((Settings.FREEZE_TIME >= System.currentTimeMillis() && !e.getPlayer().hasPermission(Perms.FREEZEBYPASS.getPermission()))) {
            e.setCancelled(true);
            return;
        }
        final Player p = e.getPlayer();
        final String message = e.getMessage();
        final String[] sm = message.split(" ");
        final Long time = PrePlayerCommandListener.times.get(p.getUniqueId());
        if (time == null || time <= System.currentTimeMillis() || p.hasPermission(Perms.COMMANDBYPASS.getPermission())) {
            if (Server.getInstance().getCommandMap().getCommand(sm[0].toLowerCase().replaceFirst("/", "")) == null) {
                e.setCancelled(true);
                Util.sendMessage((CommandSender)p, Settings.getMessage("unknowncommand"));
            }
            else if (CombatManager.isContains(p.getName()) && !PrePlayerCommandListener.pvpcmd.contains(sm[0].toLowerCase().replaceFirst("/", "")) && !p.hasPermission("sectors.commandbypass")) {
                e.setCancelled(true);
                Util.sendMessage((CommandSender)p, Settings.getMessage("commandincombat"));
            }
            PrePlayerCommandListener.times.put(p.getUniqueId(), System.currentTimeMillis() + Time.SECOND.getTime(3));
        }
        else {
            Util.sendMessage((CommandSender)p, Settings.getMessage("commandcooldown").replace("{TIME}", Util.formatTime(time - System.currentTimeMillis())));
            e.setCancelled(true);
        }
    }
    
    static {
        PrePlayerCommandListener.pvpcmd = Arrays.asList("ff", "ffa", "efekty", "msg", "r", "helpop");
        times = new ConcurrentHashMap<UUID, Long>();
    }
}
