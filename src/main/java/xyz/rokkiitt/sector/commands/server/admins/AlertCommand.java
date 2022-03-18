package xyz.rokkiitt.sector.commands.server.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;
import org.apache.commons.lang3.*;

public class AlertCommand extends ServerCommand
{
    public AlertCommand() {
        super("alert", "alert", "/alert [message]", Perms.CMD_ALERT.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length <= 0) {
            return this.sendCorrectUsage(p);
        }
        final String message = StringUtils.join(args, " ");
        for(Player pa : Server.getInstance().getOnlinePlayers().values()){
            pa.sendTitle(Util.fixColor("&6ALERT"),Util.fixColor(message), 40, 100, 40);
        }

        return Util.sendMessage(p, Settings.getMessage("commandalert").replace("{MSG}", message));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
