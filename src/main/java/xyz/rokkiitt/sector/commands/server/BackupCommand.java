package xyz.rokkiitt.sector.commands.server;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.backup.BackupInventory;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class BackupCommand extends ServerCommand
{
    public BackupCommand() {
        super("backup", "backup", "/backup [nickname]", Perms.CMD_BACKUP.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length > 0) {
            new BackupInventory(p, args[0]);
            return false;
        }
        return this.sendCorrectUsage(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
