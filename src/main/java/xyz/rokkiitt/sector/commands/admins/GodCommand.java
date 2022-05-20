package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class GodCommand extends ServerCommand
{
    public GodCommand() {
        super("god", "ustawia tryb god", "/god [nickname]", Perms.CMD_GOD.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length == 0) {
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                u.setGod(!u.isGod());
                return Util.sendMessage(p, Settings.getMessage("commandgod").replace("{STATUS}", u.isGod() ? "&aWlaczono" : "&cWylaczono"));
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
                uu.setGod(!uu.isGod());
                return Util.sendMessage(p, Settings.getMessage("commandgodsomeone").replace("{PLAYER}", o.getName()).replace("{STATUS}", o.getName()).replace("{STATUS}", uu.isGod() ? "&aWlaczono" : "&cWylaczono"));
            }
            return Util.sendMessage(p, Settings.getMessage("playersector"));
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
