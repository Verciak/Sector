package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.anvil.AnvilFakeInventory;
import xyz.rokkiitt.sector.utils.Util;

public class TestCommand extends ServerCommand
{
    public TestCommand() {
        super("test", "test", "/test", "", new String[] {"tt" });
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {


        new AnvilFakeInventory(p);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
