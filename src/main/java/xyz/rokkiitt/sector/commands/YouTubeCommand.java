package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class YouTubeCommand extends ServerCommand
{
    public YouTubeCommand() {
        super("yt", "yt", "/yt", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        return Util.sendMessage((CommandSender)p, Settings.getMessage("yt").replace("{PLAYER}", p.getName()));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
