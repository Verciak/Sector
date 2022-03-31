package xyz.rokkiitt.sector.commands.server.admins;

import cn.nukkit.Player;
import cn.nukkit.Server;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;

public class GamemodeCommand extends SectorCommand
{
    public GamemodeCommand() {
        super("gm", "gm", "/gm [value] [nickname]", Perms.CMD_GM.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length < 1) {
            return this.sendCorrectUsage(p);
        }
        final int gm = this.getMode(args[0]);
        if (p.hasPermission(Perms.CMD_GM_ALL.getPermission())) {
            if (args.length == 1) {
                p.setGamemode(gm);
                return Util.sendMessage(p, Settings.getMessage("commandgmsucces").replace("{TYPE}", gm + ""));
            }
            if (args.length != 2) {
                return this.sendCorrectUsage(p);
            }
            final Player p2 = Server.getInstance().getPlayer(args[1]);
            if (p2 != null) {
                p2.setGamemode(gm);
                return Util.sendMessage(p, Settings.getMessage("commandgmsuccesother").replace("{TYPE}", gm + "").replace("{WHO}", p2.getName()));
            }
            return Util.sendMessage(p, Settings.getMessage("playersector"));
        }
        else {
            if (p.hasPermission(Perms.CMD_GM_ADMIN.getPermission())) {
                p.setGamemode(gm);
                return Util.sendMessage(p, Settings.getMessage("commandgmsucces").replace("{TYPE}", gm + ""));
            }
            if (gm != 1) {
                p.setGamemode(gm);
                return Util.sendMessage(p, Settings.getMessage("commandgmsucces").replace("{TYPE}", gm + ""));
            }
            return Util.sendMessage(p, Settings.getMessage("commandgmperms").replace("{PERM}", Perms.CMD_GM_ADMIN.getPermission()));
        }
    }
    
    public boolean onCallback(final String s) {
        return false;
    }
    
    private int getMode(final String args) {
        if (args.equalsIgnoreCase("1") || args.equalsIgnoreCase("creative") || args.equalsIgnoreCase("true")) {
            return 1;
        }
        if (args.equalsIgnoreCase("0") || args.equalsIgnoreCase("survival") || args.equalsIgnoreCase("false")) {
            return 0;
        }
        if (args.equalsIgnoreCase("2") || args.equalsIgnoreCase("adventure")) {
            return 2;
        }
        if (args.equalsIgnoreCase("3") || args.equalsIgnoreCase("spectator")) {
            return 3;
        }
        return 0;
    }
}
