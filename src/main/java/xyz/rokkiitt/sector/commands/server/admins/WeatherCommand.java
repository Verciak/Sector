package xyz.rokkiitt.sector.commands.server.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.data.*;
import cn.nukkit.command.*;
import cn.nukkit.*;
import cn.nukkit.scheduler.*;
import cn.nukkit.level.*;

public class WeatherCommand extends ServerCommand
{
    public WeatherCommand() {
        super("weather", "weather", "/weather [value]", Perms.CMD_WEATHER.getPermission());
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[] { CommandParameter.newEnum("type", new CommandEnum("WeatherType", "clear", "rain", "thunder")), CommandParameter.newType("duration", true, CommandParamType.INT) });
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length == 0 || args.length > 2) {
            return this.sendCorrectUsage(p);
        }
        final String weather = args[0];
        final Level level = p.getLevel();
        int seconds;
        Label_0057: {
            if (args.length > 1) {
                try {
                    seconds = Integer.parseInt(args[1]);
                    break Label_0057;
                }
                catch (Exception e) {
                    return this.sendCorrectUsage(p);
                }
            }
            seconds = 12000;
        }
        switch (weather) {
            case "clear": {
                level.setRaining(false);
                level.setThundering(false);
                level.setRainTime(seconds * 20);
                level.setThunderTime(seconds * 20);
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(final int arg0) {
                        level.sendWeather(Server.getInstance().getOnlinePlayers().values());
                    }
                }, 40);
                return Util.sendMessage(p, Settings.getMessage("commandweatherclear"));
            }
            case "rain": {
                level.setRaining(true);
                level.setRainTime(seconds * 20);
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(final int arg0) {
                        level.sendWeather(Server.getInstance().getOnlinePlayers().values());
                    }
                }, 40);
                return Util.sendMessage(p, Settings.getMessage("commandweatherrain"));
            }
            case "thunder": {
                level.setThundering(true);
                level.setRainTime(seconds * 20);
                level.setThunderTime(seconds * 20);
                Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                    public void onRun(final int arg0) {
                        level.sendWeather(Server.getInstance().getOnlinePlayers().values());
                    }
                }, 40);
                return Util.sendMessage(p, Settings.getMessage("commandweatherthunder"));
            }
            default: {
                return this.sendCorrectUsage(p);
            }
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
