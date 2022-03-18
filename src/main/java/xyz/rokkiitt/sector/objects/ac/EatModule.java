package xyz.rokkiitt.sector.objects.ac;

import java.util.*;
import cn.nukkit.item.*;
import cn.nukkit.event.*;
import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.potion.*;
import cn.nukkit.*;
import cz.creeperface.nukkit.gac.player.*;
import java.util.concurrent.*;

public class EatModule implements Listener
{
    private static final Map<String, Long> times;
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Item hand = event.getItem();
        if (hand == null || hand.isNull()) {
            return;
        }
        if ((event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && (hand instanceof ItemAppleGoldEnchanted || hand instanceof ItemAppleGold)) {
            EatModule.times.put(player.getName(), System.currentTimeMillis());
        }
    }
    
    @EventHandler
    public void onSaturationChange(final PlayerEatFoodEvent event) {
        final Player player = event.getPlayer();
        if (player.hasEffect(23)) {
            return;
        }
        final long diff = System.currentTimeMillis() - EatModule.times.getOrDefault(player.getName(), 2000L);
        if (diff > 1400L) {
            return;
        }
        event.setCancelled();
        if (!Cooldown.getInstance().has(player, "eat")) {
            Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", player.getName()).replace("{WHAT}", "Fasteat"));
            Cooldown.getInstance().add(player, "eat", 2.0f);
        }
    }
    
    @EventHandler
    public void onEffect(final PlayerCustomAddEffect e) {
        final Effect effect = e.getReason();
        final Player p = e.getPlayer();
        if (effect.getId() == 5) {
            if (effect.getAmplifier() <= 0) {
                if (!Settings.ENABLE_STRENGHT1) {
                    e.setCancelled();
                    return;
                }
            }
            else if (!Settings.ENABLE_STRENGHT2) {
                e.setCancelled();
                return;
            }
            if (effect.getAmplifier() > 1) {
                effect.setAmplifier(1);
            }
        }
        if (effect.getId() == 1) {
            if (effect.getAmplifier() <= 0) {
                if (!Settings.ENABLE_SPEED1) {
                    e.setCancelled();
                    return;
                }
            }
            else if (!Settings.ENABLE_SPEED2) {
                e.setCancelled();
                return;
            }
            if (effect.getAmplifier() > 1) {
                effect.setAmplifier(1);
            }
        }
    }
    
    @EventHandler
    public void onKick(final PlayerAntycheatKick e) {
        if (e.getPlayer() != null && e.getReason() != null) {
            Server.getInstance().getScheduler().scheduleTask(() -> {
                Util.kickPlayer(e.getPlayer(), e.getReason());
                Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", e.getReason()));
            });
        }
    }
    
    @EventHandler
    public void onNotify(final PlayerAntyCheatNotify e) {
        if (!Cooldown.getInstance().has(e.getPlayer(), "acnotify")) {
            Cooldown.getInstance().add(e.getPlayer(), "acnotify", 2.0f);
            Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", e.getReason()));
        }
    }
    
    static {
        times = new ConcurrentHashMap<String, Long>();
    }
}
