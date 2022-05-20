package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.adminsee.NormalInventory;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class InvseeCommand extends ServerCommand
{
    public InvseeCommand() {
        super("invsee", "invsee", "/invsee [nick]", Perms.CMD_INVSEE.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length < 1) {
            return this.sendCorrectUsage(p);
        }
        final Player pp = Util.matchPlayer(args[0]);
        if (pp != null) {
            new NormalInventory(p, pp);
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
