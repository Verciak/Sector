package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class TnTCommand extends ServerCommand
{
    public TnTCommand() {
        super("tnt", "tnt", "/tnt", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        return Util.sendMessage((CommandSender)p, Settings.getMessage("tnt"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
