package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class HelpCommand extends ServerCommand
{
    public HelpCommand() {
        super("pomoc", "pomoc", "/pomoc", "", new String[] { "help" });
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        return Util.sendMessage((CommandSender)p, Settings.getMessage("help"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
