package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import org.apache.commons.lang3.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class IgnoreListCommand extends ServerCommand
{
    public IgnoreListCommand() {
        super("ignorelist", "ignorelist", "/ignorelist", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            final String message = StringUtils.join(u.getIgnored(), ", ");
            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandignorelist").replace("{MSG}", message));
        }
        p.kick(Util.fixColor("&9not properly loaded user data - Ignore"));
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
