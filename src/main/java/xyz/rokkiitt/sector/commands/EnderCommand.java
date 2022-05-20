package xyz.rokkiitt.sector.commands;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Enderchest;
import xyz.rokkiitt.sector.objects.Perms;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class EnderCommand extends ServerCommand
{
    public EnderCommand() {
        super("ec", "ec", "/ec", Perms.CMD_EC.getPermission(), new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        new Enderchest(p);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
