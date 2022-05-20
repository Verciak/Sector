package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.*;
import java.util.*;

public class VanishCommand extends ServerCommand
{
    public VanishCommand() {
        super("vanish", "ustawia tryb vanish", "/vanish [nickname]", Perms.CMD_VANISH.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length == 0) {
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                u.setVanish(!u.isVanish());
                setVanish(p, u);
                return Util.sendMessage(p, Settings.getMessage("commandvanish").replace("{STATUS}", u.isVanish() ? "&aWlaczono" : "&cWylaczono"));
            }
            p.kick(Util.fixColor("&9not properly loaded user data - God"));
            return false;
        }
        else {
            final Player o = Util.matchPlayer(args[0]);
            if (o == null) {
                return Util.sendMessage(p, Settings.getMessage("playersector"));
            }
            final User uu = UserManager.getUser(o.getName());
            if (uu != null) {
                uu.setVanish(!uu.isVanish());
                setVanish(o, uu);
                return Util.sendMessage(p, Settings.getMessage("commandvanishsomeone").replace("{STATUS}", uu.isVanish() ? "&awlaczony" : "&cwylaczony").replace("{PLAYER}", o.getName()));
            }
            return Util.sendMessage(p, Settings.getMessage("playersector"));
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
    
    public static void setVanish(final Player p, final User u) {
        if (u.isVanish()) {
            for (final Player online : new ArrayList<>(p.getViewers().values())) {
                if (online.hasPermission(Perms.CMD_VANISH.getPermission())) {
                    continue;
                }
                p.hidePlayer(online);
            }
        }
        else {
            for (final Player online : new ArrayList<>(Server.getInstance().getOnlinePlayers().values())) {
                if (p.canSee(online)) {
                    p.spawnTo(online);
                }
            }
        }
        Util.changeNametag(p);
    }
}
