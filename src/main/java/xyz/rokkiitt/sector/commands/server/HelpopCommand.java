package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import cn.nukkit.command.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import org.apache.commons.lang3.*;
import com.github.benmanes.caffeine.cache.*;
import java.time.*;

public class HelpopCommand extends ServerCommand
{
    private static final Cache<String, Long> times;
    
    public HelpopCommand() {
        super("helpop", "helpop", "/helpop [wiadomosc]", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length < 1) {
            return this.sendCorrectUsage((CommandSender)p);
        }
        final Long t = HelpopCommand.times.getIfPresent(p.getName());
        if (t != null && t >= System.currentTimeMillis()) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandhelpopcooldown").replace("{TIME}", Util.formatTime(t - System.currentTimeMillis())));
        }
        final String message = StringUtils.join((Object[])args, " ");
        Util.sendInformation("infoadminhelpop||" + Settings.getMessage("commandhelpopsucces").replace("{PLAYER}", p.getName()).replace("{MSG}", message));
        HelpopCommand.times.put(p.getName(), System.currentTimeMillis() + Time.MINUTE.getTime(3));
        return Util.sendMessage((CommandSender)p, Settings.getMessage("commandhelpopsucces2"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
    
    static {
        times = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10L)).build();
    }
}
