package xyz.rokkiitt.sector.commands;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Blocks;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class BlocksCommand extends ServerCommand
{
    public BlocksCommand() {
        super("bloki", "bloki", "/bloki", "");
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        new Blocks(p);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
