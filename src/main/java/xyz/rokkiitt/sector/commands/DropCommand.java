package xyz.rokkiitt.sector.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.drop.gui.MainDropInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;

public class DropCommand extends SectorCommand
{
    public DropCommand() {
        super("drop", "drop", "/drop", "", new String[0]);
    }

    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        new MainDropInventory(p, u);
        return false;
    }
}
