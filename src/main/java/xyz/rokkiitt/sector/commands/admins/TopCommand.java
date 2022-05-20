package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.level.*;
import cn.nukkit.command.*;

public class TopCommand extends ServerCommand
{
    public TopCommand() {
        super("top", "top", "/top", Perms.CMD_TOP.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final Location l = Util.getHighestLocation(p.getLocation());
        p.teleport(l.add(0.0, 2.0, 0.0));
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
