package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class StatuteCommand extends ServerCommand
{
    public StatuteCommand() {
        super("regulamin", "regulamin", "/regulamin", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        return Util.sendMessage((CommandSender)p, Settings.getMessage("statute"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
