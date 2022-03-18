package xyz.rokkiitt.sector.commands.server.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class FeedCommand extends ServerCommand
{
    public FeedCommand() {
        super("feed", "feed", "/feed [player]", Perms.CMD_FEED.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length <= 0) {
            p.getFoodData().setLevel(p.getFoodData().getMaxLevel());
            return Util.sendMessage(p, Settings.getMessage("commandfeed"));
        }
        final Player p2 = Util.matchPlayer(args[0]);
        if (p2 != null) {
            p2.getFoodData().setLevel(p2.getFoodData().getMaxLevel());
            return Util.sendMessage(p, Settings.getMessage("commandfeedsomeone").replace("{PLAYER}", p2.getName()));
        }
        return this.sendCorrectUsage(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
