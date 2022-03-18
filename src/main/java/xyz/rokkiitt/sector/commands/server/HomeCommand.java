package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.home.PlayerHomeData;
import xyz.rokkiitt.sector.objects.teleport.TeleportManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;
import org.apache.commons.lang3.*;
import cn.nukkit.command.*;
import cn.nukkit.level.*;

import java.util.*;

public class HomeCommand extends ServerCommand
{
    public HomeCommand() {
        super("home", "home", "/home <nazwa>", "", new String[] { "dom" });
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.kick("null data - Home");
            return false;
        }
        if (args.length < 1) {
            final List<String> names = new ArrayList<String>();
            for (final PlayerHomeData home : u.getHomes()) {
                names.add(home.name);
            }
            return Util.sendMessage((CommandSender)p, Settings.getMessage("homecommandavailable").replace("{LIST}", names.isEmpty() ? "&fNIE POSIADASZ" : ("&f" + StringUtils.join(names, "&7,&f "))));
        }
        final PlayerHomeData home2 = u.getHome(args[0]);
        if (home2 != null) {
            final Location l = new Location(home2.x, home2.y, home2.z);
            int time = 10;
            if (p.hasPermission(Perms.SPONSOR.getPermission())) {
                time = 4;
            }
            else if (p.hasPermission(Perms.SVIP.getPermission())) {
                time = 6;
            }
            else if (p.hasPermission(Perms.VIP.getPermission())) {
                time = 8;
            }
            TeleportManager.teleport(p, time, l);
            return false;
        }
        return Util.sendMessage((CommandSender)p, Settings.getMessage("homecommandunknown"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
