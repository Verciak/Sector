package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.wings.Wings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import cn.nukkit.*;

public class WingsCommand extends ServerCommand
{
    public WingsCommand() {
        super("wings", "Pozwala nadawac oraz usuwac skrzydla", "/wings [nickname/list] <skrzydla/usun>", Perms.CMD_WINGS.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("lista")) {
                return this.sendCorrectUsage(p);
            }
            final File dir = new File(Main.getPlugin().getDataFolder(), "skins/wings/");
            final File[] files = dir.listFiles((dir1, name) -> name.endsWith(".png"));
            if (files == null || files.length == 0) {
                return Util.sendMessage(p, Settings.getMessage("commandwingsempty"));
            }
            final String list = Arrays.stream(files).map(file -> file.getName().replace(".png", "")).collect(Collectors.joining("&8,&e "));
            return Util.sendMessage(p, Settings.getMessage("commandwingslist").replace("{LIST}", list));
        }
        else {
            if (args.length < 2) {
                return this.sendCorrectUsage(p);
            }
            final Player pp = Server.getInstance().getPlayerExact(args[0]);
            if (pp == null) {
                return Util.sendMessage(p, Settings.getMessage("playersector"));
            }
            final String wings = args[1];
            final User u = UserManager.getUser(pp.getName());
            if (u == null) {
                return true;
            }
            if (wings.equalsIgnoreCase("usun")) {
                if (u.getWings().isEmpty()) {
                    return Util.sendMessage(p, Settings.getMessage("commandwingsnowings"));
                }
                u.setWings("");
                if (!u.isIncognito()) {
                    pp.setSkin(u.getSkin());
                }
                return Util.sendMessage(p, Settings.getMessage("commandwingsdeleted").replace("{PLAYER}", pp.getName()));
            }
            else {
                if (!Wings.isExists(wings)) {
                    return Util.sendMessage(p, Settings.getMessage("commandwingsnotexist"));
                }
                if (u.getWings().equalsIgnoreCase(wings)) {
                    return Util.sendMessage(p, Settings.getMessage("commandwingsalreadyhas"));
                }
                u.setWings(wings);
                if (!u.isIncognito()) {
                    Wings.apply(u.getSkin(), wings);
                }
                return Util.sendMessage(p, Settings.getMessage("commandwingschanged").replace("{PLAYER}", pp.getName()).replace("{WINGS}", wings));
            }
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
