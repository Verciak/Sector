package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.randomtp.RandomTPManager;
import xyz.rokkiitt.sector.objects.randomtp.RandomTPStatus;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class RandomTpCommand extends ServerCommand
{
    public RandomTpCommand() {
        super("randomtp", "randomtp", "/randomtp [usun/doda] [solo/max2/group]", Perms.CMD_RANDOMTP.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (!Config.isSpawn(p.getLocation())) {
            return Util.sendMessage(p, Settings.getMessage("commandonlyspawn"));
        }
        if (args.length == 1) {
            if (args[0].contains("usun")) {
                RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.DELETE);
                return Util.sendMessage(p, Settings.getMessage("commandrandomtpdelete"));
            }
            return this.sendCorrectUsage(p);
        }
        else {
            if (args.length != 2) {
                return this.sendCorrectUsage(p);
            }
            if (!args[0].contains("dodaj")) {
                return this.sendCorrectUsage(p);
            }
            if (args[1].contains("solo")) {
                RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.SOLO);
                return Util.sendMessage(p, Settings.getMessage("commandrandomtpsucces").replace("{TYPE}", "solo"));
            }
            if (args[1].contains("max2")) {
                RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.MAX2);
                return Util.sendMessage(p, Settings.getMessage("commandrandomtpsucces").replace("{TYPE}", "2 osobowy"));
            }
            if (args[1].contains("group")) {
                RandomTPManager.setStatus(p.getUniqueId(), RandomTPStatus.GROUP);
                return Util.sendMessage(p, Settings.getMessage("commandrandomtpsucces").replace("{TYPE}", "grupowy"));
            }
            return this.sendCorrectUsage(p);
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
