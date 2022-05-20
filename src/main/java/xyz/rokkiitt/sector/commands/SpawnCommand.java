package xyz.rokkiitt.sector.commands;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.teleport.TeleportManager;
import cn.nukkit.*;
import cn.nukkit.level.*;
import cn.nukkit.command.*;

public class SpawnCommand extends ServerCommand
{
    public SpawnCommand() {
        super("spawn", "teleportuje na spawn", "/spawn", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final Location l = new Location(-124.0, 105.0, 90.0);
        int time = 10;
        if (p.hasPermission(Perms.SPONSOR.getPermission())) {
            time = 4;
        }
        else if (p.hasPermission(Perms.SVIP.getPermission())) {
            time = 6;
        }
        else if (p.hasPermission(Perms.VIP.getPermission())) {
            time = 8;
        }
        TeleportManager.teleport(p, time, l);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
