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
import xyz.rokkiitt.sector.packets.PacketStatsCommand;
import xyz.rokkiitt.sector.utils.Util;

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
}
