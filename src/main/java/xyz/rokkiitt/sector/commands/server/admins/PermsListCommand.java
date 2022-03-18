package xyz.rokkiitt.sector.commands.server.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import org.apache.commons.lang3.*;
import cn.nukkit.command.*;
import java.util.*;

public class PermsListCommand extends ServerCommand
{
    public PermsListCommand() {
        super("permslist", "permslist", "/permslist", Perms.CMD_PERMSLIST.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final List<String> names = new ArrayList<>();
        for (final Perms x : Perms.values()) {
            names.add(x.getPermission().toLowerCase());
        }
        return Util.sendMessage(p, Settings.getMessage("commandpermslist").replace("{PERMS}", StringUtils.join(names, "&8,&e ")));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
