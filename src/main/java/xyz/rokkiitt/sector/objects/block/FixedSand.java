package xyz.rokkiitt.sector.objects.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSand;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.rokkiitt.sector.Main;

public class FixedSand extends BlockSand {
    public int onUpdate(int type) {
        if (type == 1)
            Main.getQuery().addQueue(() -> {
                BlockFace face = BlockFace.DOWN;
                FullChunk chunk = getChunk();
                Block down = getBlock(chunk, getFloorX() + face.getXOffset(), getFloorY() + face.getYOffset(), getFloorZ() + face.getZOffset());
                if (canFall(chunk, (Vector3)down.getLocation()) && getFloorY() >= 0) {
                    Vector3 vector31 = new Vector3();
                    chunk.setBlockId(getFloorX() & 0xF, getFloorY(), getFloorZ() & 0xF, 0);
                    Block block1 = down();
                    while (canFall(chunk, (Vector3)block1) && block1.getY() > 0.0D)
                        vector31 = block1.down();
                    Vector3 blockup = vector31.up();
                    chunk.setBlockId(blockup.getFloorX() & 0xF, blockup.getFloorY(), blockup.getFloorZ() & 0xF, getId());
                    int j = 1;
                    List<Block> bo = new ArrayList<>(Collections.singletonList(Block.get(0, this.level, getFloorX(), getFloorY(), getFloorZ())));
                    List<Block> bn = new ArrayList<>();
                    Block block = Block.get(getId());
                    block.setLevel(this.level);
                    block.setComponents(blockup.getX(), blockup.getY(), blockup.getZ());
                    bn.add(block);
                    for (int i = 0; i <= 256.0D - getY(); i++) {
                        Vector3 pp = new Vector3(getX(), getY() + i, getZ());
                        Block id = getBlock(chunk, getFloorX(), getFloorY() + i, getFloorZ());
                        if (id.getId() != 0)
                            if (id.getId() == 13 || id.getId() == 12 || id.getId() == 145) {
                                bo.add(Block.get(0, this.level, pp.getFloorX(), pp.getFloorY(), pp.getFloorZ()));
                                chunk.setBlockId(pp.getFloorX() & 0xF, pp.getFloorY(), pp.getFloorZ() & 0xF, 0);
                                Vector3 bp = new Vector3(vector31.up().getFloorX(), (vector31.up().getFloorY() + j), vector31.up().getFloorZ());
                                chunk.setBlockId(bp.getFloorX() & 0xF, bp.getFloorY(), bp.getFloorZ() & 0xF, id.getId());
                                Block b = Block.get(id.getId());
                                b.setComponents(bp.getX(), bp.getY(), bp.getZ()).setLevel(this.level);
                                bn.add(b);
                                j++;
                            } else {
                                break;
                            }
                    }
                    Player[] players = (Player[])this.level.getChunkPlayers(chunk.getX(), chunk.getZ()).values().toArray((Object[])new Player[0]);
                    this.level.sendBlocks(players, (Vector3[])bo.toArray((Object[])new Block[0]));
                    this.level.sendBlocks(players, (Vector3[])bn.toArray((Object[])new Block[0]));
                }
            });
        return type;
    }

    public boolean canFall(FullChunk chunk, Vector3 blockposition) {
        Block block = getBlock(chunk, blockposition.getFloorX(), blockposition.getFloorY(), blockposition.getFloorZ());
        return (block.getId() == 0 || block instanceof cn.nukkit.block.BlockLiquid || block instanceof cn.nukkit.block.BlockFire);
    }

    public Block getBlock(FullChunk chunk, int x, int y, int z) {
        int fullState;
        if (y >= 0 && y < 256) {
            int cx = x >> 4;
            int cz = z >> 4;
            if (chunk != null) {
                fullState = chunk.getFullBlock(x & 0xF, y, z & 0xF);
            } else {
                fullState = 0;
            }
        } else {
            fullState = 0;
        }
        Block block = Block.fullList[fullState & 0xFFF].clone();
        block.x = x;
        block.y = y;
        block.z = z;
        block.level = this.level;
        return block;
    }
}
