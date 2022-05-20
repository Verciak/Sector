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
import xyz.rokkiitt.sector.packets.guild.commands.PacketGuildDelete;
import xyz.rokkiitt.sector.utils.Util;

import java.util.Collections;

public class GuildDeleteCommand extends SectorCommand {
    public GuildDeleteCommand() {
        super("usun", "usuwa gildie", "/usun", "", new String[0]);
    }

    public boolean onCommand(Player p, String[] args) {
        if (!Settings.ENABLE_GUILD)
            return Util.sendMessage((CommandSender)p, Settings.getMessage("guildsoffline"));
        User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                return Util.sendMessage((CommandSender) p, Settings.getMessage("donthaveguild"));
            }
            if (GuildManager.getGuild(u.getTag()) != null) {
                for(String pa : GuildManager.getGuild(u.getTag()).getMembers()){
                    UserManager.getUser(pa).setGuildpermissions(Collections.singleton("|&|"));
                }
                GuildManager.deleteGuild(GuildManager.getGuild(u.getTag()));
                Util.sendInformation(Settings.getMessage("commanddeletesucces").replace("{OWNER}", p.getName()).replace("{TAG}", u.getTag()));
            }
            Main.getProvider().update(String.valueOf(new StringBuilder().append("DELETE FROM `guilds` WHERE `tag` = '").append(u.getTag()).append("'")));
            u.setTag("NIEPOSIADA");
            return false;
        }
        p.kick("&9 not properly loaded data");
        return false;
    }

    public boolean onCallback(String s) {
        PacketGuildDelete pa = (PacketGuildDelete)JsonIterator.deserialize(s, PacketGuildDelete.class);

        return false;
    }
}

