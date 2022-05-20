package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.adminsee.EnderInventory;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class EnderseeCommand extends ServerCommand
{
    public EnderseeCommand() {
        super("endersee", "endersee", "/endersee [nick]", Perms.CMD_ENDERSEE.getPermission(), new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length < 1) {
            return this.sendCorrectUsage(p);
        }
        final Player pp = Util.matchPlayer(args[0]);
        if (pp != null) {
            new EnderInventory(p, pp);
        }
        else {
            Util.sendMessage(p, Settings.getMessage("playersector"));
        }
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
