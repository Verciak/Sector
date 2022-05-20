package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.itemshop.ItemshopInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class ItemshopCommand extends ServerCommand
{
    public ItemshopCommand() {
        super("is", "is", "/is", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (!Settings.ENABLE_ITEMSHOP) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("itemshopdisabled"));
        }
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
            new ItemshopInventory(p, u);
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
