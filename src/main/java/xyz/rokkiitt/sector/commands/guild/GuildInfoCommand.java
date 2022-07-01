package xyz.rokkiitt.sector.commands.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import org.apache.commons.lang3.StringUtils;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.PacketStatsCommand;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;

import java.util.Set;

public class GuildInfoCommand extends SectorCommand {
    public GuildInfoCommand() {
        super("info", "sprawdza statystki gildii", "/info [tag]", "", new String[0]);
    }

    public boolean onCommand(Player p, String[] args) {
        if (args.length < 1) {
            User u = UserManager.getUser(p.getName());
            if (u != null) {
                if (!u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                    Guild g = GuildManager.getGuild(u.getTag());
                    Util.sendMessage(p, Settings.getMessage("guildinfocmd")
                            .replace("{TAG}", g.getTag())
                            .replace("{NAME}", g.getName())
                            .replace("{LEADER}", g.getLeader())
                            .replace("{SIZE}", String.valueOf(g.getCuboid().getSize()))
                            .replace("{ZYCIA}", String.valueOf(g.getHearts()))
                            .replace("{POINTS}", String.valueOf(g.getPoints()))
                            .replace("{HP}", String.valueOf(g.getHearthp()))
                            .replace("{KILLS}", String.valueOf(g.getKills()))
                            .replace("{DEATHS}", String.valueOf(g.getDeaths()))
                            .replace("{GUILDPROTTIME}", Util.formatTime(g.getGuildProtectionTime() - System.currentTimeMillis()))
                            .replace("{HEARTHPROTTIME}", Util.formatTime(g.getHeartProtectionTime() - System.currentTimeMillis()))
                            .replace("{MEMBERS}", StringUtils.join(getMemberList(g.getMembers()), "&8, "))
                    );
                } else {
                    sendCorrectUsage(p);
                }
            } else {
                p.kick("not properly loaded data - Stats");
            }
        } else {
            Guild g = GuildManager.getGuild(args[0]);
            if(g == null){
                Util.sendMessage(p, Settings.getMessage("hubcommandallianceunknown"));
            }
        }
        return false;
    }

    public static String[] getMemberList(final Set<String> members) {
        final String[] s = new String[members.size()];
        int i = 0;
        for (final String u : members) {
            String color= "&c";
            Player p = Server.getInstance().getPlayer(u);
            if(p == null){
                color = "&c";
            }
            if(p.isOnline()){
                color = "&a";
            }
            s[i] = color + u;
            ++i;
        }
        return s;
    }

}
