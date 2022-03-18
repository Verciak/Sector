package xyz.rokkiitt.sector.objects.inventory.inventories;

import cn.nukkit.inventory.*;
import cn.nukkit.*;
import cn.nukkit.math.*;
import java.util.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.nbt.tag.*;
import java.nio.*;
import cn.nukkit.nbt.*;
import java.io.*;

public abstract class DoubleChestFakeInventory extends ChestFakeInventory
{
    public DoubleChestFakeInventory(final InventoryHolder holder, final String title) {
        super(InventoryType.DOUBLE_CHEST, holder, title);
    }
    
    @Override
    public void onOpen(final Player who) {
        this.viewers.add(who);
        final List<BlockVector3> blocks = this.onOpenBlock(who);
        this.blockPositions.put(who, blocks);
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> this.onFakeOpen(who, blocks), 3);
    }
    
    @Override
    protected List<BlockVector3> onOpenBlock(final Player who) {
        final BlockVector3 blockPositionA = new BlockVector3((int)who.x, (int)who.y + 2, (int)who.z);
        final BlockVector3 blockPositionB = blockPositionA.add(1, 0, 0);
        this.placeChest(who, blockPositionA);
        this.placeChest(who, blockPositionB);
        this.pair(who, blockPositionA, blockPositionB);
        this.pair(who, blockPositionB, blockPositionA);
        return Arrays.asList(blockPositionA, blockPositionB);
    }
    
    private void pair(final Player who, final BlockVector3 pos1, final BlockVector3 pos2) {
        final BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.x = pos1.x;
        blockEntityData.y = pos1.y;
        blockEntityData.z = pos1.z;
        blockEntityData.namedTag = this.getDoubleNbt(pos1, pos2);
        who.dataPacket((DataPacket)blockEntityData);
    }
    
    private byte[] getDoubleNbt(final BlockVector3 pos, final BlockVector3 pairPos) {
        final CompoundTag tag = new CompoundTag().putString("id", "Chest").putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z).putInt("pairx", pairPos.x).putInt("pairz", pairPos.z).putString("CustomName", (this.title == null) ? "Chest" : this.title);
        try {
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to create NBT for chest");
        }
    }
}
