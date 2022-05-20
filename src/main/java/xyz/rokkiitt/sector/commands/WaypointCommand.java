package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.waypoint.Waypoint;
import xyz.rokkiitt.sector.objects.waypoint.WaypointData;
import xyz.rokkiitt.sector.utils.Util;
import org.apache.commons.lang3.*;
import cn.nukkit.command.*;

import java.util.*;

public class WaypointCommand extends ServerCommand
{
    public WaypointCommand() {
        super("waypoint", "waypoint", "/waypoint dodaj/usun/aktywuj [nazwa]", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.kick("null data - WaypointCommand");
            return false;
        }
        if (args.length < 2) {
            final List<String> waypoints = new ArrayList<String>();
            for (final WaypointData wd : u.getWaypoints()) {
                waypoints.add((wd.active ? "&a" : "&c") + wd.name);
            }
            Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandlist").replace("{LIST}", StringUtils.join(waypoints, "&7,")));
            return this.sendCorrectUsage((CommandSender)p);
        }
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "dodaj": {
                if (args[1].length() > 10 || args[1].length() < 2) {
                    return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandlenght"));
                }
                final WaypointData wd = u.getWaypoint(args[1]);
                if (wd != null) {
                    wd.x = p.getPosition().getFloorX();
                    wd.y = p.getPosition().getFloorY();
                    wd.z = p.getPosition().getFloorZ();
                    u.calcWaypoints(p);
                    u.updateWaypoints(p);
                    return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandmoved").replace("{NAME}", args[1].toUpperCase()));
                }
                int limit = 1;
                if (p.hasPermission(Perms.SPONSOR.getPermission())) {
                    limit = 7;
                }
                else if (p.hasPermission(Perms.SVIP.getPermission())) {
                    limit = 5;
                }
                else if (p.hasPermission(Perms.VIP.getPermission())) {
                    limit = 3;
                }
                if (u.getWaypoints().size() >= limit) {
                    return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandlimit").replace("{LIMIT}", "" + limit));
                }
                final WaypointData wp = new WaypointData();
                wp.x = p.getPosition().getFloorX();
                wp.y = p.getPosition().getFloorY() + 1;
                wp.z = p.getPosition().getFloorZ();
                wp.active = true;
                wp.name = args[1];
                u.getWaypoints().add(wp);
                u.calcWaypoints(p);
                u.updateWaypoints(p);
                return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandcreate").replace("{NAME}", args[1].toUpperCase()));
            }
            case "usun": {
                final WaypointData wd = u.getWaypoint(args[1]);
                if (wd != null) {
                    final Waypoint wp2 = u.getactiveWaypoint(wd.name);
                    u.getWaypoints().remove(wd);
                    u.calcWaypoints(p);
                    u.updateWaypoints(p);
                    return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommanddelete").replace("{NAME}", args[1].toUpperCase()));
                }
                return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandunknown"));
            }
            case "aktywuj": {
                final WaypointData wd = u.getWaypoint(args[1]);
                if (wd != null) {
                    wd.active = !wd.active;
                    u.calcWaypoints(p);
                    u.updateWaypoints(p);
                    return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandactive").replace("{STATUS}", wd.active ? "&aAktywowano" : "&cDezaktywowano").replace("{NAME}", args[1].toUpperCase()));
                }
                return Util.sendMessage((CommandSender)p, Settings.getMessage("waypointcommandunknown"));
            }
            default: {
                return this.sendCorrectUsage((CommandSender)p);
            }
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
