package xyz.rokkiitt.sector.listeners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Listener;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.Util;

import java.util.NoSuchElementException;

public class NetherListener implements Listener
{
    
    public static void sendBlocks(final Level level, final Player[] target, final Vector3[] blocks, final int flags, final int dataLayer, final boolean optimizeRebuilds) {
        int size = 0;
        final Vector3[] var7 = blocks;
        for (int var8 = blocks.length, var9 = 0; var9 < var8; ++var9) {
            final Vector3 block = var7[var9];
            if (block != null) {
                ++size;
            }
        }
        int packetIndex = 0;
        final UpdateBlockPacket[] packets = new UpdateBlockPacket[size];
        LongSet chunks = null;
        if (optimizeRebuilds) {
            chunks = new LongOpenHashSet();
        }
        final Vector3[] var10 = blocks;
        for (int var11 = blocks.length, var12 = 0; var12 < var11; ++var12) {
            final Vector3 b = var10[var12];
            if (b != null) {
                boolean first = !optimizeRebuilds;
                if (optimizeRebuilds) {
                    final long index = Level.chunkHash((int)b.x >> 4, (int)b.z >> 4);
                    if (!chunks.contains(index)) {
                        chunks.add(index);
                        first = true;
                    }
                }
                final UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
                updateBlockPacket.x = (int)b.x;
                updateBlockPacket.y = (int)b.y;
                updateBlockPacket.z = (int)b.z;
                updateBlockPacket.flags = (first ? flags : 0);
                updateBlockPacket.dataLayer = dataLayer;
                try {
                    final Block n = Util.getBlock(level, b.getFloorX(), b.getFloorY(), b.getFloorZ(), false);
                    if (n.getId() == 0) {
                        final Block x = GlassColor.getWool(GlassColor.RED);
                        updateBlockPacket.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(x.getFullId());
                    }
                    else {
                        updateBlockPacket.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(n.getFullId());
                    }
                }
                catch (NoSuchElementException var13) {
                    throw new IllegalStateException("Unable to create BlockUpdatePacket at (" + b.x + ", " + b.y + ", " + b.z + ") in " + level.getName(), var13);
                }
                packets[packetIndex++] = updateBlockPacket;
            }
        }
        level.getServer().batchPackets(target, packets);
    }
}
