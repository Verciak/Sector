package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import org.apache.commons.lang3.*;
import cn.nukkit.command.*;
import java.util.*;

public class CmdlistCommand extends ServerCommand
{
    public CmdlistCommand() {
        super("cmdlist", "cmdlist", "/cmdlist", Perms.CMD_CMDLIST.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final List<String> names = new ArrayList<>();
        for (final Command cmd : Server.getInstance().getCommandMap().getCommands().values()) {
            names.add(cmd.getName());
        }
        return Util.sendMessage(p, Settings.getMessage("commandcmdlist").replace("{CMD}", StringUtils.join(names, "&8,&e ")));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
