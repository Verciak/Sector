package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class AlertACCommand extends ServerCommand
{
    public AlertACCommand() {
        super("ac", "ac", "/ac", Perms.CMD_ALERTAC.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            u.setAlertsenabled(!u.isAlertsenabled());
            Util.sendMessage(p, Settings.getMessage("commandac").replace("{STATE}", u.isAlertsenabled() ? "wylaczyles" : "wlaczyles"));
        }
        else {
            p.kick(Util.fixColor("&3not properly loaded data - Ac"));
        }
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
