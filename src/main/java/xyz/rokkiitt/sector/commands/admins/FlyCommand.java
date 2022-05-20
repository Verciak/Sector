package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class FlyCommand extends ServerCommand
{
    public FlyCommand() {
        super("fly", "zmienia tryb fly", "/fly [nickname]", Perms.CMD_FLY.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length == 0) {
            p.getAdventureSettings().set(AdventureSettings.Type.FLYING, !p.getAdventureSettings().get(AdventureSettings.Type.FLYING));
            p.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, !p.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT));
            p.getAdventureSettings().update();
            return Util.sendMessage(p, Settings.getMessage("commandfly").replace("{STATUS}", p.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT) ? "&aWlaczono" : "&cWylaczono"));
        }
        final Player o = Util.matchPlayer(args[0]);
        if (o == null) {
            return Util.sendMessage(p, Settings.getMessage("playersector"));
        }
        o.getAdventureSettings().set(AdventureSettings.Type.FLYING, !o.getAdventureSettings().get(AdventureSettings.Type.FLYING));
        o.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, !o.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT));
        o.getAdventureSettings().update();
        return Util.sendMessage(p, Settings.getMessage("commandflysomeone").replace("{STATUS}", o.getAdventureSettings().get(AdventureSettings.Type.ALLOW_FLIGHT) ? "&aWlaczono" : "&cWylaczono").replace("{PLAYER}", o.getName()));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
