package xyz.rokkiitt.sector.commands.sector.guilds;

import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.guild.commands.PacketGuildWar;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import com.jsoniter.*;
import cn.nukkit.*;

public class GuildWarCommand extends SectorCommand
{
    public GuildWarCommand() {
        super("walka", "wysyla propozycje walki", "/walka", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (!Settings.ENABLE_GUILD) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("guildsoffline"));
        }
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.kick("&9 not properly loaded data");
            return false;
        }
        if (u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("donthaveguild"));
        }
        if (!u.hasPermission("19")) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "wyslania walki"));
        }
        final PacketGuildWar pa = new PacketGuildWar();
        pa.reason = "";
        pa.player = p.getName();
        return false;
    }
    
    public boolean onCallback(final String s) {
        final PacketGuildWar pa = JsonIterator.deserialize(s, PacketGuildWar.class);
            final Player p = Server.getInstance().getPlayerExact(pa.player);
            if (p != null) {
                Util.sendMessage((CommandSender)p, pa.reason);
            }

        return false;
    }
}
