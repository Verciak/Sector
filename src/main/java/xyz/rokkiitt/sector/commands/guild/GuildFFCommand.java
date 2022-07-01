package xyz.rokkiitt.sector.commands.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.guild.commands.PacketGuildFF;
import xyz.rokkiitt.sector.utils.Util;

public class GuildFFCommand extends SectorCommand {
    public GuildFFCommand() {
        super("ff", "przelacza tryb pvp w gildii", "/ff", "", new String[0]);
    }

    public boolean onCommand(Player p, String[] args) {
        if (!Settings.ENABLE_GUILD)
            return Util.sendMessage((CommandSender)p, Settings.getMessage("guildsoffline"));
        User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (u.getTag().equalsIgnoreCase("NIEPOSIADA"))
                return Util.sendMessage((CommandSender)p, Settings.getMessage("donthaveguild"));
            if (!u.hasPermission("8"))
                return Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "zmiany pvp w gildii"));
            if(GuildManager.getGuild(u.getTag()).isPvp() == false){
                GuildManager.getGuild(u.getTag()).setPvp(true);
            }else {
                GuildManager.getGuild(u.getTag()).setPvp(false);
            }
            for (User ua : UserManager.users) {
                if (ua.getTag().equalsIgnoreCase(u.getTag())) {
                    Player pa = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
                    if (pa != null)
                        pa.sendTitle(Util.fixColor("&l&3Friendly Fire"), Util.fixColor("&fOgien gildyjny zostal {STATUS} &fprzez &3{PLAYER}"
                                .replace("{PLAYER}", p.getName()).replace("{STATUS}", GuildManager.getGuild(u.getTag()).isPvp() ? "&awlaczony" : "&cwylaczony")));
                }
            }
            return false;
        }
        p.kick("&9 not properly loaded data");
        return false;
    }
}

