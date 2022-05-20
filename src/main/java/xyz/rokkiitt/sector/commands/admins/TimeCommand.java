package xyz.rokkiitt.sector.commands.admins;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;

public class TimeCommand extends ServerCommand
{
    public TimeCommand() {
        super("time", "time", "/time [value]", Perms.CMD_TIME.getPermission());
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[] { CommandParameter.newEnum("mode", new CommandEnum("TimeMode", "start", "stop")) });
        this.commandParameters.put("add", new CommandParameter[] { CommandParameter.newEnum("mode", new CommandEnum("TimeModeAdd", "add")), CommandParameter.newType("amount", CommandParamType.INT) });
        this.commandParameters.put("setAmount", new CommandParameter[] { CommandParameter.newEnum("mode", false, new CommandEnum("TimeModeSet", "set")), CommandParameter.newType("amount", CommandParamType.INT) });
        this.commandParameters.put("setTime", new CommandParameter[] { CommandParameter.newEnum("mode", new CommandEnum("TimeModeSet", "set")), CommandParameter.newEnum("time", new CommandEnum("TimeSpec", "day", "night", "midnight", "noon", "sunrise", "sunset")) });
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length < 1) {
            return this.sendCorrectUsage(p);
        }
        if ("start".equals(args[0])) {
            for (final Level level : p.getServer().getLevels().values()) {
                level.checkTime();
                level.startTime();
                level.checkTime();
            }
            return Util.sendMessage(p, Settings.getMessage("commandtimestart"));
        }
        if ("stop".equals(args[0])) {
            for (final Level level : p.getServer().getLevels().values()) {
                level.checkTime();
                level.stopTime();
                level.checkTime();
            }
            return Util.sendMessage(p, Settings.getMessage("commandtimestop"));
        }
        if (args.length < 2) {
            return this.sendCorrectUsage(p);
        }
        if ("set".equals(args[0])) {
            int value;
            if ("day".equals(args[1])) {
                value = 0;
            }
            else if ("night".equals(args[1])) {
                value = 14000;
            }
            else if ("midnight".equals(args[1])) {
                value = 18000;
            }
            else if ("noon".equals(args[1])) {
                value = 6000;
            }
            else if ("sunrise".equals(args[1])) {
                value = 23000;
            }
            else if ("sunset".equals(args[1])) {
                value = 12000;
            }
            else {
                try {
                    value = Math.max(0, Integer.parseInt(args[1]));
                }
                catch (Exception e) {
                    return this.sendCorrectUsage(p);
                }
            }
            for (final Level level2 : p.getServer().getLevels().values()) {
                level2.checkTime();
                level2.setTime(value);
                level2.checkTime();
            }
            return Util.sendMessage(p, Settings.getMessage("commandtimeset").replace("{TIME}", value + ""));
        }
        if ("add".equals(args[0])) {
            int value;
            try {
                value = Math.max(0, Integer.parseInt(args[1]));
            }
            catch (Exception e) {
                return this.sendCorrectUsage(p);
            }
            for (final Level level2 : p.getServer().getLevels().values()) {
                level2.checkTime();
                level2.setTime(level2.getTime() + value);
                level2.checkTime();
            }
            return Util.sendMessage(p, Settings.getMessage("commandtimeadd").replace("{TIME}", value + ""));
        }
        return this.sendCorrectUsage(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
