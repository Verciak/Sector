package xyz.rokkiitt.sector.commands.server.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.teleport.TeleportManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.guild.commands.PacketGuildHome;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.Util;

public class GuildHomeCommand extends SectorCommand {
    public GuildHomeCommand() {
        super("baza", "teleportuje na baze gildii", "/baza", "", new String[0]);
    }

    public boolean onCommand(Player p, String[] args) {
        if (!Settings.ENABLE_GUILD) {
            return Util.sendMessage((CommandSender) p, Settings.getMessage("guildsoffline"));
        }
        User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                return Util.sendMessage((CommandSender) p, Settings.getMessage("donthaveguild"));
            }
            if (p != null) {
                int time = 10;
                if (p.hasPermission(Perms.SPONSOR.getPermission())) {
                    time = 4;
                } else if (p.hasPermission(Perms.SVIP.getPermission())) {
                    time = 6;
                } else if (p.hasPermission(Perms.VIP.getPermission())) {
                    time = 8;
                }
                TeleportManager.teleport(p, time, new Location(GuildManager.getGuild(u.getTag()).getCenterx(), 41, GuildManager.getGuild(u.getTag()).getCenterz()));
            }
            return false;
        }
        p.kick("&9 not properly loaded data");
        return false;
    }

    public boolean onCallback(String s) {
        PacketGuildHome pa = (PacketGuildHome)JsonIterator.deserialize(s, PacketGuildHome.class);
        return false;
    }
}

