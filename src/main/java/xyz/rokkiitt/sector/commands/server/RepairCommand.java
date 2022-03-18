package xyz.rokkiitt.sector.commands.server;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;
import cn.nukkit.item.*;
import java.util.*;

public class RepairCommand extends ServerCommand
{
    public RepairCommand() {
        super("repair", "repair", "/repair [all]", Perms.CMD_REPAIR.getPermission(), new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length <= 0) {
            final Item item = p.getInventory().getItemInHand().clone();
            if (item.isNull() || !(item instanceof ItemDurable)) {
                return Util.sendMessage((CommandSender)p, Settings.getMessage("commandrepairerror"));
            }
            if (item.getDamage() == 0) {
                return Util.sendMessage((CommandSender)p, Settings.getMessage("commandrepairfull"));
            }
            item.setDamage(Integer.valueOf(0));
            p.getInventory().setItemInHand(item);
            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandrepairsucces"));
        }
        else {
            if (!args[0].equalsIgnoreCase("all")) {
                return this.sendCorrectUsage((CommandSender)p);
            }
            if (p.hasPermission(Perms.CMD_REPAIRALL.getPermission())) {
                for (final Map.Entry<Integer, Item> entry : p.getInventory().getContents().entrySet()) {
                    if (entry.getValue() instanceof ItemDurable) {
                        final Item item2 = entry.getValue();
                        item2.setDamage(Integer.valueOf(0));
                        p.getInventory().setItem((int)entry.getKey(), item2);
                    }
                }
                return Util.sendMessage((CommandSender)p, Settings.getMessage("commandrepairsuccesall"));
            }
            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandpermission").replace("{PERM}", "sector.repairall"));
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
