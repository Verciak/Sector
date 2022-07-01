package xyz.rokkiitt.sector.commands.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.math.Vector3;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.guild.commands.PacketGuildInvite;
import xyz.rokkiitt.sector.utils.Util;

public class GuildInviteCommand extends SectorCommand {
    public GuildInviteCommand() {
        super("zapros", "zaprasza osobe do gildii", "/zapros [nickname/all/*]", "", new String[0]);
    }

    public boolean onCommand(Player p, String[] args) {
        if (!Settings.ENABLE_GUILD)
            return Util.sendMessage((CommandSender)p, Settings.getMessage("guildsoffline"));
        if (args.length < 1)
            return sendCorrectUsage(p);
        User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (u.getTag().equalsIgnoreCase("NIEPOSIADA"))
                return Util.sendMessage((CommandSender)p, Settings.getMessage("donthaveguild"));
            if (!u.hasPermission("11"))
                return Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "zapraszania czlonkow"));
            if (p.getName().equalsIgnoreCase(args[0]))
                return Util.sendMessage((CommandSender)p, Settings.getMessage("commandinvitesame"));
            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                for (Player pp : p.getViewers().values()) {
                    if (pp.getLocation().distance((Vector3)p.getLocation()) > 5.0D)
                        continue;
                    if (pp.getName() == p.getName())
                        continue;
                    User uu = UserManager.getUser(pp.getName());
                    if (uu == null ||
                            !uu.getTag().equalsIgnoreCase("NIEPOSIADA"))
                        continue;
                    if (uu.getInvites().contains(u.getTag().toLowerCase()))
                        continue;
                    uu.getInvites().add(u.getTag().toLowerCase());
                    Util.sendMessage((CommandSender)pp, Settings.getMessage("commandinvitesucces").replace("{TAG}", u.getTag().toUpperCase()));
                }
                return Util.sendMessage((CommandSender)p, Settings.getMessage("commandinvitesend"));
            }
        }
        p.kick("&9 not properly loaded data");
        return false;
    }

}

