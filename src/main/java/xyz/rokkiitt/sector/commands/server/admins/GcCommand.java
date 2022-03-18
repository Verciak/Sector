package xyz.rokkiitt.sector.commands.server.admins;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.ThreadCache;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;

public class GcCommand extends ServerCommand
{
    public GcCommand() {
        super("gc", "gc", "/gc", Perms.CMD_GC.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        return this.execute(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return this.execute(p);
    }
    
    private boolean execute(final CommandSender p) {
        int chunksCollected = 0;
        int entitiesCollected = 0;
        int tilesCollected = 0;
        final long memory = Runtime.getRuntime().freeMemory();
        for (final Level level : p.getServer().getLevels().values()) {
            final int chunksCount = level.getChunks().size();
            final int entitiesCount = level.getEntities().length;
            final int tilesCount = level.getBlockEntities().size();
            level.doChunkGarbageCollection();
            level.unloadChunks(true);
            chunksCollected += chunksCount - level.getChunks().size();
            entitiesCollected += entitiesCount - level.getEntities().length;
            tilesCollected += tilesCount - level.getBlockEntities().size();
        }
        ThreadCache.clean();
        System.gc();
        Runtime.getRuntime().gc();
        final long freedMemory = Runtime.getRuntime().freeMemory() - memory;
        p.sendMessage(TextFormat.GREEN + "---- " + TextFormat.WHITE + "Garbage collection result" + TextFormat.GREEN + " ----");
        p.sendMessage(TextFormat.GOLD + "Chunks: " + TextFormat.RED + chunksCollected);
        p.sendMessage(TextFormat.GOLD + "Entities: " + TextFormat.RED + entitiesCollected);
        p.sendMessage(TextFormat.GOLD + "Block Entities: " + TextFormat.RED + tilesCollected);
        p.sendMessage(TextFormat.GOLD + "Memory freed: " + TextFormat.RED + NukkitMath.round(freedMemory / 1024.0 / 1024.0, 2) + " MB");
        return false;
    }
}
