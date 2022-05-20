package xyz.rokkiitt.sector.commands.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.math.*;
import java.lang.management.*;
import cn.nukkit.*;
import cn.nukkit.level.*;

public class StatusCommand extends ServerCommand
{
    public StatusCommand() {
        super("status", "status", "/status", Perms.CMD_STATUS.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        return this.execute(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return this.execute(p);
    }
    
    private boolean execute(final CommandSender sender) {
        final Server server = sender.getServer();
        final long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 2L / 1048576L;
        final long allocatedMemory = Runtime.getRuntime().totalMemory() / 1048576L;
        Util.sendMessage(sender, "         &6SERVER");
        Util.sendMessage(sender, " &f Current TPS: &e" + NukkitMath.round(server.getTicksPerSecond(), 2));
        Util.sendMessage(sender, " &f Load: &e" + server.getTickUsage() + "%");
        Util.sendMessage(sender, " &f Online: &e" + server.getOnlinePlayers().size() + "/" + server.getMaxPlayers());
        Util.sendMessage(sender, " &f Memory: &e" + usedMemory + "&7/&e" + allocatedMemory + " MB");
        Util.sendMessage(sender, " &f Uptime: &e" + Util.formatTime(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()));
        Util.sendMessage(sender, " &f Thread count: &e" + Thread.getAllStackTraces().size());
        Util.sendMessage(sender, "         &6WORLD");
        final Level level = server.getDefaultLevel();
        Util.sendMessage(sender, " &f Chunks: &e" + level.getChunks().size());
        Util.sendMessage(sender, " &f Entities: &e" + level.getEntities().length);
        Util.sendMessage(sender, " &f BlockEntities: &e" + level.getBlockEntities().size());
        Util.sendMessage(sender, " &f Time: &e" + NukkitMath.round(level.getTickRateTime(), 2) + "ms" + ((level.getTickRate() > 1) ? (" (tick rate " + (19 - level.getTickRate()) + ")") : ""));
        return true;
    }
}
