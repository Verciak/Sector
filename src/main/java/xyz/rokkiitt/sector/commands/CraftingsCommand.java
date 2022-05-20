package xyz.rokkiitt.sector.commands;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Craftings;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class CraftingsCommand extends ServerCommand
{
    public CraftingsCommand() {
        super("craftingi", "craftingi", "/craftingi", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        p.addWindow(new Craftings());
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
