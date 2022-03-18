package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.player.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.PacketPlayerChat;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.*;

import java.util.*;
import cn.nukkit.event.*;
import java.util.concurrent.*;

public class ChatListener implements Listener
{
    private static final Map<UUID, Long> times;
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onChat(final PlayerChatEvent e) {
        e.setCancelled(true);
        final Player p = e.getPlayer();
        if (Settings.FREEZE_TIME >= System.currentTimeMillis() && !p.hasPermission(Perms.FREEZEBYPASS.getPermission())) {
            e.setCancelled();
            return;
        }
        if (!Settings.ENABLE_CHAT && !p.hasPermission(Perms.CHATBYPASS.getPermission())) {
            Util.sendMessage((CommandSender)p, Settings.getMessage("chatoff"));
            return;
        }
        final Long time = ChatListener.times.get(p.getUniqueId());
        if (time != null && time > System.currentTimeMillis() && !p.hasPermission(Perms.CHATBYPASS.getPermission())) {
            Util.sendMessage((CommandSender)p, Settings.getMessage("chatcooldown").replace("{TIME}", Util.formatTime(time - System.currentTimeMillis())));
            return;
        }
        ChatListener.times.put(p.getUniqueId(), System.currentTimeMillis() + Time.SECOND.getTime(5));
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.kick(Util.fixColor("&9not properly loaded user data - PlayerChat"));
            return;
        }
        if (u.getMutetime() >= System.currentTimeMillis()) {
            Util.sendMessage((CommandSender)p, Settings.getMessage("mutedleft").replace("{TIME}", Util.formatTime(u.getMutetime() - System.currentTimeMillis())));
            return;
        }
        final String msg = e.getMessage();
        if (msg.startsWith("!!")) {
            if (!u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                String formatally = Util.fixColor(Settings.getMessage("allianceprefix"));
                formatally = formatally.replace("{TAG}", u.getTag().toUpperCase());
                formatally = formatally.replace("{PLAYER}", p.getName());
                formatally = formatally.replace("{MESSAGE}", msg);
                formatally = formatally.replaceFirst("!!", "");
                for (final String ss : u.getAlliances()) {
                    Util.sendInformation("GUILDCHAT||" + ss + "|^|" + formatally);
                }
                Util.sendInformation("GUILDCHAT||" + u.getTag() + "|^|" + formatally);
            }
        }
        else if (msg.startsWith("!")) {
            if (!u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                String formatally = Util.fixColor(Settings.getMessage("guildprefix"));
                formatally = formatally.replace("{TAG}", u.getTag().toUpperCase());
                formatally = formatally.replace("{PLAYER}", p.getName());
                formatally = formatally.replace("{MESSAGE}", msg);
                formatally = formatally.replaceFirst("!", "");
                Util.sendInformation("GUILDCHAT||" + u.getTag() + "|^|" + formatally);
            }
        }
        else if (!u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
            String gformat = Settings.getMessage(u.getRank() + "guild");
            if (!gformat.isEmpty()) {
                gformat = gformat.replace("{GUILD}", u.getTag().toUpperCase());
                gformat = gformat.replace("{POINTS}", String.valueOf(u.getPoints()));
                gformat = gformat.replace("{PLAYER}", p.getName());
                gformat = gformat.replace("{MESSAGE}", msg);
                final PacketPlayerChat pa = new PacketPlayerChat();
                pa.format = gformat;
                pa.guild = u.getTag();
                pa.message = msg;
            Server.getInstance().broadcastMessage(Util.fixColor(gformat));
            }
        }
        else {
            String nogformat = Settings.getMessage(u.getRank() + "noguild");
            if (!nogformat.isEmpty()) {
                nogformat = nogformat.replace("{POINTS}", String.valueOf(u.getPoints()));
                nogformat = nogformat.replace("{PLAYER}", p.getName());
                nogformat = nogformat.replace("{MESSAGE}", msg);
                final PacketPlayerChat pa = new PacketPlayerChat();
                pa.format = nogformat;
                pa.guild = u.getTag();
                pa.message = msg;
                Server.getInstance().broadcastMessage(Util.fixColor(nogformat));
            }
        }
    }
    
    static {
        times = new ConcurrentHashMap<UUID, Long>();
    }
}
