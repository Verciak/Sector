package xyz.rokkiitt.sector.utils;

import cn.nukkit.*;
import cn.nukkit.block.*;
import xyz.rokkiitt.sector.Main;

import java.lang.reflect.*;

public final class BlockFactory
{
    public static void registerBlock(final int id, final Class<? extends Block> block) {
        try {
            Block.list[id] = block;
            final Block blockInstance = (Block)block.newInstance();
            try {
                try {
                    final Constructor<? extends Block> constructor = block.getDeclaredConstructor(Integer.TYPE);
                    constructor.setAccessible(true);
                    for (int data = 0; data < 16; ++data) {
                        Block.fullList[id << 4 | data] = (Block)constructor.newInstance(data);
                    }
                    Block.hasMeta[id] = true;
                }
                catch (NoSuchMethodException ignored) {
                    for (int data = 0; data < 16; ++data) {
                        Block.fullList[id << 4 | data] = blockInstance;
                    }
                }
            }
            catch (Exception e) {
                Server.getInstance().getLogger().error("[Block] Failed to register block (ID: " + id + "): " + block.getSimpleName(), (Throwable)e);
                for (int data = 0; data < 16; ++data) {
                    Block.fullList[id << 4 | data] = (Block)new BlockUnknown(id, Integer.valueOf(data));
                }
                return;
            }
            Block.solid[id] = blockInstance.isSolid();
            Block.transparent[id] = blockInstance.isTransparent();
            Block.hardness[id] = blockInstance.getHardness();
            Block.light[id] = blockInstance.getLightLevel();
            if (blockInstance.isSolid()) {
                if (blockInstance.isTransparent()) {
                    if (!(blockInstance instanceof BlockLiquid) && !(blockInstance instanceof BlockIce)) {
                        Block.lightFilter[id] = 1;
                    }
                    else {
                        Block.lightFilter[id] = 2;
                    }
                }
                else {
                    Block.lightFilter[id] = 15;
                }
            }
            else {
                Block.lightFilter[id] = 1;
            }
            for (int i = 0; i < 15; ++i) {
                Block.fullList[id << 4 | i] = blockInstance;
            }
        }
        catch (Exception e2) {
            Main.getPlugin().getLogger().error("[Block] Failed to register block (ID: " + id + "): ", (Throwable)e2);
        }
    }
}
