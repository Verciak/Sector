package xyz.rokkiitt.sector.objects.ac;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import cn.nukkit.event.player.*;
import cn.nukkit.event.block.*;
import cn.nukkit.event.*;
import xyz.rokkiitt.sector.DiscordWebhook;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.item.*;
import cn.nukkit.item.enchantment.*;
import cn.nukkit.potion.*;

public class SpeedmineModule implements Listener
{
    private final Map<String, Long> changes;
    
    public SpeedmineModule() {
        this.changes = new ConcurrentHashMap<String, Long>();
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if (e.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            this.changes.put(e.getPlayer().getName(), System.currentTimeMillis());
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(final BlockBreakEvent e) {
        if (e.isFastBreak()) {
            this.handleBreak(e);
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        this.handleBreak(e);
    }
    
    private void handleBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        if (p.getGamemode() == 0) {
            if (p.hasPermission(Perms.ANTYCHEAT.getPermission())) {
                return;
            }
            final Item item = e.getItem();
            final Enchantment eff = item.getEnchantment(15);
            if (!this.changes.containsKey(p.getName())) {
                if (!Cooldown.getInstance().has(e.getPlayer(), "speedmine2")) {
                    final User u = UserManager.getUser(e.getPlayer().getName());
                    if (u != null) {
                        if (u.acdata.speedmineLimit((eff == null || eff.getLevel() < 6) && (!p.hasEffect(3) || p.getEffect(3) == null || p.getEffect(3).getAmplifier() < 1))) {
                            e.setCancelled();
                            Util.banAC(e.getPlayer(), "3d", "gildie");
                            Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "Speedmine - A (Banned)"));
                            Cooldown.getInstance().add(e.getPlayer(), "speedmine2", 10.0f);
                            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958717437501661216/yqAzCBYi51G04MViNUgKN4otJQfNyvtqOEP8IlQzGGIjNui3eFzTz6zmd0OSSKZmVOQl");
                            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                            embedObject.setAuthor("LOGI ANTY-CHEAT", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
                            embedObject.setColor(new Color(0x00FF00));
                            embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** jest podjerzany o: **speedmine**!");
                            embedObject.setTitle("");
                            webhook.addEmbed(embedObject);
                            try {
                                webhook.execute();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        else if (!Cooldown.getInstance().has(e.getPlayer(), "speedmine")) {
                            Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "Speedmine - A " + (e.getPlayer().hasEffect(3) ? "HASTE" : "") + " Eff: " + (item.isNull() ? "0" : (item.hasEnchantment(15) ? Integer.valueOf(item.getEnchantment(15).getLevel()) : "0"))));
                            Cooldown.getInstance().add(e.getPlayer(), "speedmine", ((eff != null && eff.getLevel() >= 6) || p.hasEffect(3)) ? 6.0f : 3.0f);
                            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958717437501661216/yqAzCBYi51G04MViNUgKN4otJQfNyvtqOEP8IlQzGGIjNui3eFzTz6zmd0OSSKZmVOQl");
                            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                            embedObject.setAuthor("LOGI ANTY-CHEAT", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
                            embedObject.setColor(new Color(0x00FF00));
                            embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** jest podjerzany o: **speedmine**!");
                            embedObject.setTitle("");
                            webhook.addEmbed(embedObject);
                            try {
                                webhook.execute();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    else {
                        e.setCancelled();
                    }
                }
                else {
                    e.setCancelled();
                }
                return;
            }
            double expectedTime = Math.ceil(b.getBreakTime(item) * 20.0);
            if (p.hasEffect(4)) {
                expectedTime *= 1.0 + 0.5 * p.getEffect(4).getAmplifier();
            }
            if (eff != null && eff.getLevel() >= 6) {
                expectedTime = 0.0;
            }
            else if (p.hasEffect(3)) {
                final Effect haste = p.getEffect(3);
                if (haste != null) {
                    if (haste.getAmplifier() >= 1) {
                        if (!item.isNull() && item.isPickaxe() && item instanceof ItemPickaxeDiamond) {
                            if (eff != null) {
                                if (eff.getLevel() >= 4) {
                                    expectedTime = 0.0;
                                }
                                else {
                                    expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                                }
                            }
                            else {
                                expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                            }
                        }
                        else {
                            expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                        }
                    }
                    else {
                        expectedTime -= expectedTime * 0.25 * (haste.getAmplifier() + 1);
                    }
                }
            }
            --expectedTime;
            final double actualTime = (double)(System.currentTimeMillis() - this.changes.get(p.getName()));
            if (actualTime < expectedTime || e.isFastBreak()) {
                e.setCancelled();
                if (!Cooldown.getInstance().has(e.getPlayer(), "speedmine2")) {
                    final User u2 = UserManager.getUser(e.getPlayer().getName());
                    if (u2 != null && !Cooldown.getInstance().has(e.getPlayer(), "speedmine3")) {
                        Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", p.getName()).replace("{WHAT}", "Speedmine - B " + (p.hasEffect(3) ? "HASTE" : "") + " Eff: " + (item.isNull() ? "0" : (item.hasEnchantment(15) ? Integer.valueOf(item.getEnchantment(15).getLevel()) : "0"))));
                        Cooldown.getInstance().add(e.getPlayer(), "speedmine3", 3.0f);
                        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958717437501661216/yqAzCBYi51G04MViNUgKN4otJQfNyvtqOEP8IlQzGGIjNui3eFzTz6zmd0OSSKZmVOQl");
                        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                        embedObject.setAuthor("LOGI ANTY-CHEAT", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
                        embedObject.setColor(new Color(0x00FF00));
                        embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** jest podjerzany o: **speedmine**!");
                        embedObject.setTitle("");
                        webhook.addEmbed(embedObject);
                        try {
                            webhook.execute();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                return;
            }
            this.changes.remove(p.getName());
        }
    }
}
