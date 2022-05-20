package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class ClearCommand extends ServerCommand
{
    public ClearCommand() {
        super("clear", "clear", "/clear [player]", Perms.CMD_CLEAR.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length <= 0) {
            p.getInventory().clearAll();
            return Util.sendMessage(p, Settings.getMessage("commandclear"));
        }
        final Player p2 = Util.matchPlayer(args[0]);
        if (p2 != null) {
            p2.getInventory().clearAll();
            return Util.sendMessage(p, Settings.getMessage("commandclearsomeone").replace("{PLAYER}", p2.getName()));
        }
        return this.sendCorrectUsage(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
