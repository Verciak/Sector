package xyz.rokkiitt.sector.objects.ac;

import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.DiscordWebhook;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.event.*;
import cn.nukkit.level.*;
import cn.nukkit.math.*;
import cn.nukkit.utils.*;
import cn.nukkit.block.*;

import java.awt.*;
import java.io.IOException;

public class PhaseModule implements Listener
{
    @EventHandler(ignoreCancelled = false)
    public void onPhase2(final PlayerMoveEvent e) {
        if (e.getPlayer().hasPermission(Perms.ANTYCHEAT.getPermission())) {
            return;
        }
        if (e.getTo().getY() < 0.0) {
            e.getPlayer().teleport(Util.getHighestLocation(e.getFrom()));
            return;
        }
        if (e.getFrom().clone().setComponents(e.getFrom().getX(), 0.0, e.getFrom().getZ()).distance((Vector3)e.getTo().clone().setComponents(e.getTo().getX(), 0.0, e.getTo().getZ())) > 3.0) {
            e.getPlayer().teleport(e.getFrom());
            if (!Cooldown.getInstance().has(e.getPlayer(), "phase1")) {
                Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "movement was too distant"));
                Cooldown.getInstance().add(e.getPlayer(), "phase1", 2.0f);
                DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958717437501661216/yqAzCBYi51G04MViNUgKN4otJQfNyvtqOEP8IlQzGGIjNui3eFzTz6zmd0OSSKZmVOQl");
                DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                embedObject.setAuthor("LOGI ANTY-CHEAT", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
                embedObject.setColor(new Color(0x00FF00));
                embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** jest podjerzany o: **phase**!");
                embedObject.setTitle("");
                webhook.addEmbed(embedObject);
                try {
                    webhook.execute();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getFrom().getFloorX() != e.getTo().getFloorX() || e.getFrom().getFloorZ() != e.getTo().getFloorZ() || e.getFrom().getFloorY() != e.getTo().getFloorY()) {
            if (this.inBlock(e.getFrom()) && this.inBlock(e.getTo())) {
                e.getPlayer().teleport(e.getFrom());
                if (!Cooldown.getInstance().has(e.getPlayer(), "phase2")) {
                    Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "trying to walk too wall when he is in wall"));
                    Cooldown.getInstance().add(e.getPlayer(), "phase2", 2.0f);
                    DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958717437501661216/yqAzCBYi51G04MViNUgKN4otJQfNyvtqOEP8IlQzGGIjNui3eFzTz6zmd0OSSKZmVOQl");
                    DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                    embedObject.setAuthor("LOGI ANTY-CHEAT", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
                    embedObject.setColor(new Color(0x00FF00));
                    embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** jest podjerzany o: **phase**!");
                    embedObject.setTitle("");
                    webhook.addEmbed(embedObject);
                    try {
                        webhook.execute();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return;
            }
            if (!this.inBlock(e.getFrom()) && this.inBlock(e.getTo())) {
                e.getPlayer().teleport(e.getFrom());
                if (!Cooldown.getInstance().has(e.getPlayer(), "phase3")) {
                    Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "trying to walk too wall"));
                    Cooldown.getInstance().add(e.getPlayer(), "phase3", 2.0f);
                    DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/958717437501661216/yqAzCBYi51G04MViNUgKN4otJQfNyvtqOEP8IlQzGGIjNui3eFzTz6zmd0OSSKZmVOQl");
                    DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                    embedObject.setAuthor("LOGI ANTY-CHEAT", "", "http://cravatar.eu/avatar/"+ e.getPlayer().getName() +"/64.png");
                    embedObject.setColor(new Color(0x00FF00));
                    embedObject.setDescription("Gracz **" + e.getPlayer().getName() + "** jest podjerzany o: **phase**!");
                    embedObject.setTitle("");
                    webhook.addEmbed(embedObject);
                    try {
                        webhook.execute();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    private boolean inBlock(final Location loc) {
        final Block b1 = Util.getBlock(loc.getLevel(), loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), false);
        final Location lup = loc.getSide(BlockFace.UP).getLocation();
        final Block b2 = Util.getBlock(loc.getLevel(), lup.getFloorX(), lup.getFloorY(), lup.getFloorZ(), false);
        return b1.isNormalBlock() && b2.isNormalBlock() && b1.getId() != 0 && b2.getId() != 0 && !(b1 instanceof BlockFence) && !(b2 instanceof BlockFence) && !(b1 instanceof Faceable) && !(b2 instanceof Faceable) && !b2.canPassThrough() && !b1.canPassThrough();
    }
}
