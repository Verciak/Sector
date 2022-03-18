package xyz.rokkiitt.sector.commands.server.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class HealCommand extends ServerCommand
{
    public HealCommand() {
        super("heal", "heal", "/heal [player]", Perms.CMD_HEAL.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length <= 0) {
            p.setHealth((float)p.getMaxHealth());
            return Util.sendMessage(p, Settings.getMessage("commandheal"));
        }
        final Player p2 = Util.matchPlayer(args[0]);
        if (p2 != null) {
            p2.setHealth((float)p2.getMaxHealth());
            return Util.sendMessage(p, Settings.getMessage("commandhealsomeone").replace("{WHO}", p2.getName()));
        }
        return this.sendCorrectUsage(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
