package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.Perms;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class PItemsCommand extends ServerCommand
{
    public PItemsCommand() {
        super("pitems", "chuj cie to", "/pitems", Perms.CMD_PITEMS.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        p.addWindow(new PItemsGUI());
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
