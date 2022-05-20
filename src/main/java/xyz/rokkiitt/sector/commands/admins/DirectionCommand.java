package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class DirectionCommand extends ServerCommand
{
    public DirectionCommand() {
        super("direction", "get player facing direction", "/direction", Perms.CMD_DIRECTION.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        String dir;
        double y = p.getLocation().getYaw();
        if (y < 0.0) {
            y += 360.0;
        }
        y %= 360.0;
        final int i = (int)((y + 8.0) / 22.5);
        if (i == 0) {
            dir = "west";
        }
        else if (i == 1) {
            dir = "west northwest";
        }
        else if (i == 2) {
            dir = "northwest";
        }
        else if (i == 3) {
            dir = "north northwest";
        }
        else if (i == 4) {
            dir = "north";
        }
        else if (i == 5) {
            dir = "north northeast";
        }
        else if (i == 6) {
            dir = "northeast";
        }
        else if (i == 7) {
            dir = "east northeast";
        }
        else if (i == 8) {
            dir = "east";
        }
        else if (i == 9) {
            dir = "east southeast";
        }
        else if (i == 10) {
            dir = "southeast";
        }
        else if (i == 11) {
            dir = "south southeast";
        }
        else if (i == 12) {
            dir = "south";
        }
        else if (i == 13) {
            dir = "south southwest";
        }
        else if (i == 14) {
            dir = "southwest";
        }
        else if (i == 15) {
            dir = "west southwest";
        }
        else {
            dir = "west";
        }
        Util.sendMessage(p, Settings.getMessage("commanddirectory").replace("{DIR}", dir));
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
