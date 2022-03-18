package xyz.rokkiitt.sector.commands.server;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.inventory.inventories.WorkbenchMobileInventory;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class WorkbenchCommand extends ServerCommand
{
    public WorkbenchCommand() {
        super("wb", "wb", "/wb", Perms.CMD_WB.getPermission(), new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        new WorkbenchMobileInventory(p);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
