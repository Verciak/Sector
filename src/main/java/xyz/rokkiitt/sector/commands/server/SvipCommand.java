package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class SvipCommand extends ServerCommand
{
    public SvipCommand() {
        super("svip", "svip", "/svip", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        return Util.sendMessage((CommandSender)p, Settings.getMessage("svip").replace("{PLAYER}", p.getName()));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
