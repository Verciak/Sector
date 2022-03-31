package xyz.rokkiitt.sector.commands.server.admins;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.Task;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.combat.CombatManager;

public class StopCommand extends ServerCommand
{
    public StopCommand() {
        super("stop", "stop", "/stop", Perms.CMD_STOP.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        Main.isStop = true;
        Main.saveOnStop = true;
        Server.getInstance().shutdown();
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        Main.isStop = true;
        Main.saveOnStop = true;
        Server.getInstance().shutdown();
        return false;
    }
}
