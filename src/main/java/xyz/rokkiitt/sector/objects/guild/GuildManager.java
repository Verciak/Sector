package xyz.rokkiitt.sector.objects.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.Vector3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.guild.entity.EntityHead;
import xyz.rokkiitt.sector.objects.stonefarms.Stone;
import xyz.rokkiitt.sector.objects.stonefarms.StoneManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.guild.PacketGuildData;
import xyz.rokkiitt.sector.utils.SpaceUtil;

public class GuildManager {
    public static Set<Guild> guilds = ConcurrentHashMap.newKeySet();

    public static void deleteGuild(Guild u) {
        guilds.remove(u);
        if (u.getHead() != null) {
            u.getHead().despawnFromAll();
            u.getHead().close();
        }
        Main.getQuery().addQueue(() -> {
            u.clearLogblock();
            u.clearRegeneration();
            for (Location loc : SpaceUtil.getSquare(u.getCuboid().getCenter().add(0.0D, -2.0D, 0.0D), 4, 7))
                u.getCuboid().getCenter().getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 0);
            List<FullChunk> chunks = new ArrayList<>();
            int radius = u.getCuboid().getSize();
            int cX = u.getCuboid().getCenter().getFloorX();
            int cZ = u.getCuboid().getCenter().getFloorZ();
            int minX = Math.min(cX + radius, cX - radius);
            int maxX = Math.max(cX + radius, cX - radius);
            int minZ = Math.min(cZ + radius, cZ - radius);
            int maxZ = Math.max(cZ + radius, cZ - radius);
            Location first = new Location(minX, 1.0D, minZ, u.getCuboid().getCenter().getLevel());
            Location second = new Location(maxX, 256.0D, maxZ, u.getCuboid().getCenter().getLevel());
            int topBlockX = Math.max(first.getFloorX(), second.getFloorX());
            int bottomBlockX = Math.min(first.getFloorX(), second.getFloorX());
            int topBlockY = Math.max(first.getFloorY(), second.getFloorY());
            int bottomBlockY = Math.min(first.getFloorY(), second.getFloorY());
            int topBlockZ = Math.max(first.getFloorZ(), second.getFloorZ());
            int bottomBlockZ = Math.min(first.getFloorZ(), second.getFloorZ());
            for (int x = bottomBlockX; x <= topBlockX; x++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    for (int y = bottomBlockY; y <= topBlockY; y++) {
                        if (y >= 0 && y < 256) {
                            int cx = x >> 4;
                            int cz = z >> 4;
                            BaseFullChunk baseFullChunk = u.getCuboid().getCenter().getLevel().getChunk(cx, cz);
                            if (baseFullChunk != null) {
                                Block block = Block.fullList[baseFullChunk.getFullBlock(x & 0xF, y, z & 0xF) & 0xFFF].clone();
                                block.x = x;
                                block.y = y;
                                block.z = z;
                                block.level = u.getCuboid().getCenter().getLevel();
                                if (block.getId() == 154) {
                                    baseFullChunk.setBlockId(x & 0xF, y, z & 0xF, 0);
                                    if (!chunks.contains(baseFullChunk))
                                        chunks.add(baseFullChunk);
                                }
                            }
                        }
                    }
                }
            }
            chunks.forEach(chunkk -> u.getCuboid().getCenter().getLevel().getChunkPlayers(chunkk.getX(), chunkk.getZ()).values().forEach(player -> player.unloadChunk(chunkk.getX(), chunkk.getZ(), chunkk.getProvider().getLevel())));
        });
    }

    public static Guild getGuild(Location loc) {
        for (Guild g : guilds) {
            if (g.getCuboid().isInCuboid(loc))
                return g;
        }
        return null;
    }

    public static Guild getHeart(Location loc) {
        for (Guild g : guilds) {
            if (g.getHeart().isInHeart(loc))
                return g;
        }
        return null;
    }

    public static Guild getGuild(PacketGuildData id) {
        for (Guild u : guilds) {
            if (u.getTag().equalsIgnoreCase(id.tag))
                return u;
        }
        return createGuild(id.tag, id.name, id.leader, id.centerx, id.centerz, id.size, id.members, id.guildBalance, id.skarbiec, id.createTime, id.guildprot, id.heartprot, id.penaltytnt, id.hearttype, id.heartcolor, id.hearts, id.hoppers, id.collections);
    }

    public static Guild createGuild(String tag, String name, String leader, int centerx, int centerz, int size, String members, int goldblocks, String skarbiec, long createTime, long guildprot, long heartprot, long penalty, String hearttype, String heartcolor, int hearts, int hoppers, String coll) {
        Guild g = new Guild(tag, name, leader, centerx, centerz, size, members, goldblocks, skarbiec, createTime, guildprot, heartprot, penalty, hearttype, heartcolor, hearts, hoppers, coll);
        guilds.add(g);
        Location loc = g.getCuboid().getCenter();
        loc.add(0.5D, -0.3D, 0.5D);
        setGuildRoom(g);
        g.setHead(new EntityHead(g));
        for (Player p : Server.getInstance().getOnlinePlayers().values()) {
            if (g.getHeart().isInHeart(p.getLocation()))
                g.getHead().spawnTo(p);
        }
        User u = UserManager.getUser(leader);
        if (u != null) {
            u.setTag(tag);
            Set<String> x = ConcurrentHashMap.newKeySet();
            x.addAll(GuildPanelGUI.allperms);
            u.setGuildpermissions(x);
            u.save();
        }
        return g;
    }

    public static Guild loadGuilds() {
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS `guilds` (" +
                "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                "`tag` TEXT NOT NULL, " +
                "`name` TEXT NOT NULL, " +
                "`leader` TEXT NOT NULL, " +
                "`centerx` int(100) NOT NULL, " +
                "`centerz` int(100) NOT NULL, " +
                "`size` int(100) NOT NULL, " +
                "`members` TEXT NOT NULL, " +
                "`guildBalance` int(100) NOT NULL, " +
                "`guildprot` bigint(22) NOT NULL, " +
                "`heartprot` bigint(22) NOT NULL, " +
                "`createTime` bigint(22) NOT NULL, " +
                "`penaltytnt` bigint(22) NOT NULL, " +
                "`heartcolor` TEXT NOT NULL, " +
                "`hearttype` TEXT NOT NULL, " +
                "`hearts` int(100) NOT NULL, " +
                "`skarbiec` TEXT NOT NULL, " +
                "`collections` TEXT NOT NULL, " +
                "`hoppers` int(255) NOT NULL," +
                "`points` int(255) NOT NULL," +
                "`kills` int(255) NOT NULL," +
                "`deaths` int(255) NOT NULL);");
        try {
            ResultSet query = Main.getProvider().query("SELECT * FROM `guilds`");
            while (query.next()) {
                Guild value = new Guild(query);
                guilds.add(value);
            }
            query.close();
            Main.getPlugin().getLogger().info("Loaded " + guilds.size() + " guilds from guilds");
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().warning("Nie mozna zaladowac tabeli guilds");
            ex.printStackTrace();
        }
        return null;
    }

    public static Guild getGuild(String id) {
        for (Guild u : guilds) {
            if (u.getTag().equalsIgnoreCase(id))
                return u;
        }
        return null;
    }

    public static boolean canCreateGuildByGuild(Location loc) {
        for (Guild g : guilds) {
            if (Math.abs(g.getCuboid().getCenterX() - loc.getFloorX()) <= Settings.GUILD_SIZE_MAX * 2 + Settings.GUILD_SIZE_BETWEEN &&
                    Math.abs(g.getCuboid().getCenterZ() - loc.getFloorZ()) <= Settings.GUILD_SIZE_MAX * 2 + Settings.GUILD_SIZE_BETWEEN)
                return false;
        }
        return true;
    }

    public static boolean canTeleportByGuild(Location loc) {
        for (Guild g : guilds) {
            if (Math.abs(g.getCuboid().getCenterX() - loc.getFloorX()) <= Settings.GUILD_SIZE_MAX + Settings.GUILD_SIZE_BETWEEN &&
                    Math.abs(g.getCuboid().getCenterZ() - loc.getFloorZ()) <= Settings.GUILD_SIZE_MAX + Settings.GUILD_SIZE_BETWEEN)
                return false;
        }
        return true;
    }

    public static void setGuildRoom(Guild g) {
        Location c = g.getCuboid().getCenter();
        Location locc = g.getCuboid().getCenter();
        c = new Location(c.getFloorX(), Settings.GUILD_Y - 1.0D, c.getFloorZ(), (c.getLevel() == null) ? Server.getInstance().getDefaultLevel() : c.getLevel());
        for (Location loc : SpaceUtil.getSquare(c, 4, 5)) {
            locc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 0);
            Stone s = StoneManager.checkStone(locc.getLevelBlock().getLocationHash());
            if (s != null &&
                    g.getHeart().isInHeart(loc)) {
                StoneManager.removeStone(loc.getLevelBlock().getLocationHash());
                loc.getLevel().dropItem((Vector3)loc, PItemsGUI.stoniarka);
            }
        }
        for (Location loc : SpaceUtil.getCorners(c, 4, 5))
            locc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 49);
        for (Location loc : SpaceUtil.getSquare(c, 4))
            locc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 49);
        c = new Location(c.getFloorX(), c.getFloorY() - 1.0D, c.getFloorZ(), c.getLevel());
        for (Location loc : SpaceUtil.getSquare(c, 4))
            locc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 49);
        c = new Location(c.getFloorX(), Settings.GUILD_Y + 5.0D, c.getFloorZ(), c.getLevel());
        for (Location loc : SpaceUtil.getSquare(c, 4, 0))
            locc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 49);
        c = new Location(c.getFloorX(), Settings.GUILD_Y + 4.0D, c.getFloorZ(), c.getLevel());
        for (Location loc : SpaceUtil.getWalls(c, 4))
            locc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 49);
        for (Location loc : SpaceUtil.getCorners(c, 3, 0))
            locc.getLevel().setBlockAt(loc.getFloorX(), loc.getFloorY(), loc.getFloorZ(), 98);
        SpaceUtil.setBlock(locc.getLevel(), 169, 0, locc.getFloorX(), Settings.GUILD_Y - 1, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 169, 0, locc.getFloorX(), Settings.GUILD_Y + 4, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 4, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 4, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 4, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 4, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 1, Settings.GUILD_Y, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 1, Settings.GUILD_Y, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 1, Settings.GUILD_Y, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 1, Settings.GUILD_Y, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 1, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 2, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 1, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 2, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 1, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 2, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 1, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 2, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 1, Settings.GUILD_Y, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 2, Settings.GUILD_Y, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX(), Settings.GUILD_Y, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX(), Settings.GUILD_Y, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX(), Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 1, Settings.GUILD_Y, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 2, Settings.GUILD_Y, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX(), Settings.GUILD_Y, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX(), Settings.GUILD_Y, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 171, 14, locc.getFloorX(), Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 5, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 5, locc.getFloorX() + 3, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 5, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 5, locc.getFloorX() - 3, Settings.GUILD_Y, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 3, locc.getFloorX() + 4, Settings.GUILD_Y, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 2, locc.getFloorX() + 4, Settings.GUILD_Y, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 3, locc.getFloorX() - 4, Settings.GUILD_Y, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 2, locc.getFloorX() - 4, Settings.GUILD_Y, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 0, locc.getFloorX() + 2, Settings.GUILD_Y, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 0, locc.getFloorX() + 2, Settings.GUILD_Y, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 1, locc.getFloorX() - 2, Settings.GUILD_Y, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 1, locc.getFloorX() - 2, Settings.GUILD_Y, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 3, locc.getFloorX() + 4, Settings.GUILD_Y + 1, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 2, locc.getFloorX() + 4, Settings.GUILD_Y + 1, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 3, locc.getFloorX() - 4, Settings.GUILD_Y + 1, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 2, locc.getFloorX() - 4, Settings.GUILD_Y + 1, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 0, locc.getFloorX() + 3, Settings.GUILD_Y + 1, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 0, locc.getFloorX() + 3, Settings.GUILD_Y + 1, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 1, locc.getFloorX() - 3, Settings.GUILD_Y + 1, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 1, locc.getFloorX() - 3, Settings.GUILD_Y + 1, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 7, locc.getFloorX() + 4, Settings.GUILD_Y + 2, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 6, locc.getFloorX() + 4, Settings.GUILD_Y + 2, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 7, locc.getFloorX() - 4, Settings.GUILD_Y + 2, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 6, locc.getFloorX() - 4, Settings.GUILD_Y + 2, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 4, locc.getFloorX() + 3, Settings.GUILD_Y + 2, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 4, locc.getFloorX() + 3, Settings.GUILD_Y + 2, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 5, locc.getFloorX() - 3, Settings.GUILD_Y + 2, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 5, locc.getFloorX() - 3, Settings.GUILD_Y + 2, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 4, Settings.GUILD_Y + 3, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 4, Settings.GUILD_Y + 3, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 4, Settings.GUILD_Y + 3, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 4, Settings.GUILD_Y + 3, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 3, Settings.GUILD_Y + 3, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() + 3, Settings.GUILD_Y + 3, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 3, Settings.GUILD_Y + 3, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 49, 0, locc.getFloorX() - 3, Settings.GUILD_Y + 3, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 7, locc.getFloorX() + 4, Settings.GUILD_Y + 3, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 6, locc.getFloorX() + 4, Settings.GUILD_Y + 3, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 7, locc.getFloorX() - 4, Settings.GUILD_Y + 3, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 6, locc.getFloorX() - 4, Settings.GUILD_Y + 3, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 4, locc.getFloorX() + 2, Settings.GUILD_Y + 3, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 4, locc.getFloorX() + 2, Settings.GUILD_Y + 3, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 5, locc.getFloorX() - 2, Settings.GUILD_Y + 3, locc.getFloorZ() - 4);
        SpaceUtil.setBlock(locc.getLevel(), 109, 5, locc.getFloorX() - 2, Settings.GUILD_Y + 3, locc.getFloorZ() + 4);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 3, Settings.GUILD_Y + 3, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 3, Settings.GUILD_Y + 3, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 3, Settings.GUILD_Y + 3, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 3, Settings.GUILD_Y + 3, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 7, locc.getFloorX() + 2, Settings.GUILD_Y + 4, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 7, locc.getFloorX() - 2, Settings.GUILD_Y + 4, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 6, locc.getFloorX() + 2, Settings.GUILD_Y + 4, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 6, locc.getFloorX() - 2, Settings.GUILD_Y + 4, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 109, 5, locc.getFloorX() - 3, Settings.GUILD_Y + 4, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 5, locc.getFloorX() - 3, Settings.GUILD_Y + 4, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 4, locc.getFloorX() + 3, Settings.GUILD_Y + 4, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 109, 4, locc.getFloorX() + 3, Settings.GUILD_Y + 4, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 1, Settings.GUILD_Y + 4, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 2, Settings.GUILD_Y + 4, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 3, Settings.GUILD_Y + 4, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX(), Settings.GUILD_Y + 4, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX(), Settings.GUILD_Y + 4, locc.getFloorZ() + 2);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX(), Settings.GUILD_Y + 4, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 1, Settings.GUILD_Y + 4, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 2, Settings.GUILD_Y + 4, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 3, Settings.GUILD_Y + 4, locc.getFloorZ());
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX(), Settings.GUILD_Y + 4, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX(), Settings.GUILD_Y + 4, locc.getFloorZ() - 2);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX(), Settings.GUILD_Y + 4, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 3, Settings.GUILD_Y + 4, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 3, Settings.GUILD_Y + 4, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 3, Settings.GUILD_Y + 4, locc.getFloorZ() - 1);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 3, Settings.GUILD_Y + 4, locc.getFloorZ() + 1);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 1, Settings.GUILD_Y + 4, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 1, Settings.GUILD_Y + 4, locc.getFloorZ() - 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() - 1, Settings.GUILD_Y + 4, locc.getFloorZ() + 3);
        SpaceUtil.setBlock(locc.getLevel(), 44, 13, locc.getFloorX() + 1, Settings.GUILD_Y + 4, locc.getFloorZ() + 3);
    }
}
