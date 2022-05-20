package xyz.rokkiitt.sector.commands;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.kits.Kits;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class KitCommand extends ServerCommand
{
    public KitCommand() {
        super("kit", "kit", "/kit", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            new Kits(p, u);
            return false;
        }
        p.kick(Util.fixColor("&9not properly loaded user data - DropCommand"));
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
