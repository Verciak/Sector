package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import cn.nukkit.command.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;

public class UnIgnoreCommand extends ServerCommand
{
    public UnIgnoreCommand() {
        super("unignore", "unignore", "/unignore [nick]", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length <= 0) {
            return this.sendCorrectUsage((CommandSender)p);
        }
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.kick(Util.fixColor("&9not properly loaded user data - Ignore"));
            return false;
        }
        if (!u.getIgnored().contains(args[0].toLowerCase())) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandunignorefail").replace("{WHO}", args[0]));
        }
        u.getIgnored().remove(args[0].toLowerCase());
        return Util.sendMessage((CommandSender)p, Settings.getMessage("commandunignoresucces").replace("{WHO}", args[0]));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
