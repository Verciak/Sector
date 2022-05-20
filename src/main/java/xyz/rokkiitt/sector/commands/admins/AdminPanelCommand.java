package xyz.rokkiitt.sector.commands.admins;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.adminpanel.AdminPanelInventory;
import xyz.rokkiitt.sector.utils.Util;

public class AdminPanelCommand extends ServerCommand
{
    public AdminPanelCommand() {
        super("adminpanel", "adminpanel", "/adminpanel", Perms.CMDADMINPANEL.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        new AdminPanelInventory(p);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
