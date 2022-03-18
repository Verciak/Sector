package xyz.rokkiitt.sector.commands.server;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Deposit;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class DepositCommand extends ServerCommand
{
    public DepositCommand() {
        super("schowek", "schowek", "/schowek", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            new Deposit(p, u);
            return false;
        }
        p.kick(Util.fixColor("&9not properly loaded user data - DepositCommand"));
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
