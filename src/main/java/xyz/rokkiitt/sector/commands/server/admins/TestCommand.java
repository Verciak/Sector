package xyz.rokkiitt.sector.commands.server.admins;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.packets.commands.PacketTeleportCommand;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;

public class TestCommand extends ServerCommand
{
    public TestCommand() {
        super("test", "test", "/test", "");
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("nether")) {
                final PacketTeleportCommand pa = new PacketTeleportCommand();
                pa.isPlayer = false;
                pa.isRandom = false;
                pa.isSector = false;
                pa.x = 111;
                pa.y = 21;
                pa.z = 93;
                pa.sender = p.getName();
                pa.playerdestination = ItemSerializer.serializeLocation(new Location(111.0, 21.0, 93.0, p.getYaw(), p.getPitch(), p.getLevel()));
                pa.succes = false;
                pa.reason = "nether";
                pa.destinationsector = "";
            }
            else if (args[0].equalsIgnoreCase("EVENT")) {
                final PacketTeleportCommand pa = new PacketTeleportCommand();
                pa.isPlayer = false;
                pa.isRandom = false;
                pa.isSector = false;
                pa.x = 0;
                pa.y = 105;
                pa.z = 0;
                pa.sender = p.getName();
                pa.playerdestination = ItemSerializer.serializeLocation(new Location(0.0, 105.0, 0.0, p.getYaw(), p.getPitch(), p.getLevel()));
                pa.succes = false;
                pa.reason = "nether";
                pa.destinationsector = "";
            }
            else if (args[0].equalsIgnoreCase("withertest")) {
                int dropped = 0;
                for (int i = 0; i < 1000; ++i) {
                    if (RandomUtil.getChance(1.0)) {
                        ++dropped;
                    }
                }
                Util.sendMessage(p, "Wydropiono " + dropped + " z 1000 prob");
            }
            else if (args[0].equalsIgnoreCase("szkloborder") && args.length >= 4) {
                final Location center = new Location(0.0, 100.0, 0.0, Server.getInstance().getDefaultLevel());
                final int radius = Integer.parseInt(args[1]);
                final int cX = center.getFloorX();
                final int cZ = center.getFloorZ();
                final int minX = Math.min(cX + radius, cX - radius);
                final int maxX = Math.max(cX + radius, cX - radius);
                final int minZ = Math.min(cZ + radius, cZ - radius);
                final int maxZ = Math.max(cZ + radius, cZ - radius);
                final Location x1 = new Location(minX, 1.0, minZ, center.getLevel());
                final Location x2 = new Location(maxX, 256.0, minZ, center.getLevel());
                final Location z1 = new Location(minX, 256.0, maxZ, center.getLevel());
                final Location z2 = new Location(maxX, 1.0, maxZ, center.getLevel());
                final int id = Integer.parseInt(args[2]);
                final int meta = Integer.parseInt(args[3]);
                if (args[1].equalsIgnoreCase("1")) {
                    getLocs(x2, x1, id, meta);
                }
                else if (args[1].equalsIgnoreCase("2")) {
                    getLocs(x2, z2, id, meta);
                }
                else if (args[1].equalsIgnoreCase("3")) {
                    getLocs(z2, z1, id, meta);
                }
                else if (args[1].equalsIgnoreCase("4")) {
                    getLocs(z1, x1, id, meta);
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
    
    public static void getLocs(final Location first, final Location second, final int id, final int meta) {
        final int topBlockX = Math.max(first.getFloorX(), second.getFloorX());
        final int bottomBlockX = Math.min(first.getFloorX(), second.getFloorX());
        final int topBlockY = Math.max(first.getFloorY(), second.getFloorY());
        final int bottomBlockY = Math.min(first.getFloorY(), second.getFloorY());
        final int topBlockZ = Math.max(first.getFloorZ(), second.getFloorZ());
        final int bottomBlockZ = Math.min(first.getFloorZ(), second.getFloorZ());
        for (int x = bottomBlockX; x <= topBlockX; ++x) {
            for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                for (int y = bottomBlockY; y <= topBlockY; ++y) {
                    first.getLevel().setBlock(new Vector3(x, y, z), Block.get(id, meta));
                }
            }
        }
    }
}
