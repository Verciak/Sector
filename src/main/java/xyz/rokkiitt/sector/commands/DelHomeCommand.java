package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import cn.nukkit.command.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.home.PlayerHomeData;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;

public class DelHomeCommand extends ServerCommand
{
    public DelHomeCommand() {
        super("delhome", "delhome", "/delhome <nazwa>", "", new String[] { "usundom" });
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.kick("null data - DelHome");
            return false;
        }
        if (args.length < 1) {
            return this.sendCorrectUsage(p);
        }
        final PlayerHomeData home = u.getHome(args[0]);
        if (home != null) {
            u.removeHome(home);
            return Util.sendMessage(p, Settings.getMessage("delhomecommandsucces").replace("{NAME}", args[0]));
        }
        return Util.sendMessage(p, Settings.getMessage("delhomecommanderror"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
