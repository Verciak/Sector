package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import cn.nukkit.item.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class ResetRankingCommand extends ServerCommand
{
    public ResetRankingCommand() {
        super("resetujranking", "resetujranking", "/resetujranking", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (!p.hasPermission("payments.free")) {
                final Item item = Item.get(388);
                item.setCount(64);
                if (item.getId() != 0 && item.getCount() > 0) {
                    if (!p.getInventory().contains(item)) {
                        return Util.sendMessage((CommandSender)p, Settings.getMessage("commandresetpointsfail"));
                    }
                    Util.removeItemById(p, item);
                }
            }
            u.setPoints(1000);
            u.setKills(0);
            u.setDeaths(0);
            u.setAssist(0);
            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandresetpointssucces"));
        }
        p.kick(Util.fixColor("&9not properly loaded user data - ResetRanking"));
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
