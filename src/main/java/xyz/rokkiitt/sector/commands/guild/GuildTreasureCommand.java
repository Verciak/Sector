package xyz.rokkiitt.sector.commands.guild;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.inventory.*;

public class GuildTreasureCommand extends ServerCommand
{
    public GuildTreasureCommand() {
        super("skarbiec", "otwiera skarbiec gildii", "/skarbiec", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                return Util.sendMessage((CommandSender)p, Settings.getMessage("donthaveguild"));
            }
            if (!u.hasPermission("13")) {
                return Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "otwierania skarbca"));
            }
            final Guild g = GuildManager.getGuild(u.getTag());
            if (g == null) {
                return Util.sendMessage((CommandSender)p, Settings.getMessage("needguildsector"));
            }
            p.addWindow((Inventory)g.getSkarbiec());
        }
        else {
            p.kick("not properly loaded data");
        }
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
