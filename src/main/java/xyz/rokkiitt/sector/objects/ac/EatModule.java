package xyz.rokkiitt.sector.objects.ac;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import cn.nukkit.item.*;
import cn.nukkit.event.*;
import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.DiscordWebhook;
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
            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958717437501661216/yqAzCBYi51G04MViNUgKN4otJQfNyvtqOEP8IlQzGGIjNui3eFzTz6zmd0OSSKZmVOQl");
            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
            embedObject.setAuthor("LOGI ANTY-CHEAT", "", "http://cravatar.eu/avatar/"+ player.getName() +"/64.png");
            embedObject.setColor(new Color(0x00FF00));
            embedObject.setDescription("Gracz **" + event.getPlayer().getName() + "** jest podjerzany o: **fast-eat**!");
            embedObject.setTitle("");
            webhook.addEmbed(embedObject);
            try {
                webhook.execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    

    
    static {
        times = new ConcurrentHashMap<String, Long>();
    }
}
