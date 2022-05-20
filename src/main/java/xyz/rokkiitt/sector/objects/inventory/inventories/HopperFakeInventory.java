package xyz.rokkiitt.sector.objects.inventory.inventories;

import cn.nukkit.block.BlockID;
import cn.nukkit.inventory.*;
import cn.nukkit.*;
import cn.nukkit.math.*;
import java.util.*;
import cn.nukkit.level.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.nbt.tag.*;
import java.nio.*;
import cn.nukkit.nbt.*;
import java.io.*;

public abstract class HopperFakeInventory extends FakeInventory
{
    public HopperFakeInventory(final InventoryHolder holder, final String title) {
        super(InventoryType.HOPPER, holder, title);
    }

    HopperFakeInventory(final InventoryType type, final InventoryHolder holder, final String title) {
        super(type, holder, title);
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
        final BlockVector3 blockPosition = new BlockVector3((int)who.x, (int)who.y + 2, (int)who.z);
        this.placeChest(who, blockPosition);
        return Collections.singletonList(blockPosition);
    }

    protected void placeChest(final Player who, final BlockVector3 pos) {
        final UpdateBlockPacket updateBlock = new UpdateBlockPacket();
        updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(who.protocol, 154, 0);
        updateBlock.flags = 11;
        updateBlock.x = pos.x;
        updateBlock.y = pos.y;
        updateBlock.z = pos.z;
        who.dataPacket((DataPacket)updateBlock);
        final BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.x = pos.x;
        blockEntityData.y = pos.y;
        blockEntityData.z = pos.z;
        blockEntityData.namedTag = this.getNbt(pos);
        who.dataPacket((DataPacket)blockEntityData);
    }

    private byte[] getNbt(final BlockVector3 pos) {
        final CompoundTag tag = new CompoundTag().putString("id", "Hopper").putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z).putString("CustomName", (this.title == null) ? "Hopper" : this.title);
        try {
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to create NBT for hopper");
        }
    }
}
