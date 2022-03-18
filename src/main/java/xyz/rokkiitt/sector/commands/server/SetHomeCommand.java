package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import cn.nukkit.command.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.home.PlayerHomeData;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;

public class SetHomeCommand extends ServerCommand
{
    public SetHomeCommand() {
        super("sethome", "sethome", "/sethome <nazwa>", "", new String[] { "ustawdom" });
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.kick("null data - SetHome");
            return false;
        }
        if (args.length < 1) {
            return this.sendCorrectUsage((CommandSender)p);
        }
        final PlayerHomeData home = u.getHome(args[0]);
        if (home != null) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("sethomecommandexist"));
        }
        int s = 1;
        if (p.hasPermission(Perms.SPONSOR.getPermission())) {
            s = 7;
        }
        else if (p.hasPermission(Perms.SVIP.getPermission())) {
            s = 5;
        }
        else if (p.hasPermission(Perms.VIP.getPermission())) {
            s = 3;
        }
        if (u.getHomes().size() >= s) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("sethomecommandlimit"));
        }
        if (args[0].length() < 1 || args[0].length() > 10) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("sethomecommandlenght"));
        }
        if (!args[0].matches("[a-zA-Z]+")) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("sethomecommandletters"));
        }
        final Guild g = GuildManager.getGuild(p.getLocation());
        if (g == null) {
            final PlayerHomeData wp = new PlayerHomeData();
            wp.x = p.getPosition().getFloorX();
            wp.y = p.getPosition().getFloorY();
            wp.z = p.getPosition().getFloorZ();
            wp.name = args[0];
            u.getHomes().add(wp);
            return Util.sendMessage((CommandSender)p, Settings.getMessage("sethomecommandsucces").replace("{NAME}", args[0]));
        }
        if (!g.getTag().equalsIgnoreCase(u.getTag())) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("sethomecommandguild"));
        }
        final PlayerHomeData wp = new PlayerHomeData();
        wp.x = p.getPosition().getFloorX();
        wp.y = p.getPosition().getFloorY();
        wp.z = p.getPosition().getFloorZ();
        wp.name = args[0];
        u.getHomes().add(wp);
        return Util.sendMessage((CommandSender)p, Settings.getMessage("sethomecommandsucces").replace("{NAME}", args[0]));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
