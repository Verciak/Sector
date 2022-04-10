package xyz.rokkiitt.sector.objects.inventory.inventories;

import cn.nukkit.inventory.*;
import cn.nukkit.*;
import cn.nukkit.math.*;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.Util;

import java.util.*;
import cn.nukkit.level.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.nbt.tag.*;
import java.nio.*;
import cn.nukkit.nbt.*;
import java.io.*;

public abstract class ChestFakeInventory extends FakeInventory
{
    public ChestFakeInventory(final InventoryHolder holder, final String title) {
        super(InventoryType.CHEST, holder, title);
    }
    
    ChestFakeInventory(final InventoryType type, final InventoryHolder holder, final String title) {
        super(type, holder, title);
    }
    
    @Override
    public void onOpen(final Player who) {
        this.viewers.add(who);
        final List<BlockVector3> blocks = this.onOpenBlock(who);
        this.blockPositions.put(who, blocks);
        Server.getInstance().getScheduler().scheduleDelayedTask(() -> this.onFakeOpen(who, blocks), 3);
    }

    public void setSmallEnchantGui() {
        final int[] black = { 1, 2, 3, 4, 5, 6, 7, 9, 13, 17, 19, 20, 21, 23, 24, 25, 22 };
        final int[] orange = { 0, 8, 11, 18, 26 };
        for (final int b : black) {
            this.setItem(b, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        }
        for (final int b : orange) {
            this.setItem(b, GlassColor.get(GlassColor.LIME).setCustomName(Util.fixColor("&r")));
        }
    }

    public void setIncoServerGui() {
        final int[] black = { 1, 2, 3, 5, 6, 7, 9, 13, 17, 19, 20, 21, 23, 24, 25, 22 };
        final int[] orange = { 0, 4, 8, 18, 26, 10, 16, 22 };
        for (final int b : black) {
            this.setItem(b, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        }
        for (final int b : orange) {
            this.setItem(b, GlassColor.get(GlassColor.LIME).setCustomName(Util.fixColor("&r")));
        }
    }
    
    public void setSmallServerGui() {
        final int[] black = { 1, 2, 3, 5, 6, 7, 9, 13, 17, 19, 20, 21, 23, 24, 25, 22 };
        final int[] orange = { 0, 4, 8, 18, 26 };
        for (final int b : black) {
            this.setItem(b, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        }
        for (final int b : orange) {
            this.setItem(b, GlassColor.get(GlassColor.LIME).setCustomName(Util.fixColor("&r")));
        }
    }

    public void fill() {
        for(int i = 0; i < this.getSize(); i++) {
            this.setItem(i, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        }
    }

    public void setCraftingGUI() {
        final int[] array;
        final int[] black = array = new int[] { 2,6,7,10,11,15,16,17,19,20,24,25,26,28,29,33,34,35,37,38,39,40,41,42,43,44,47,51,52 };
        for (final int i : array) {
            this.setItem(i, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        }
        final int[] array2;
        final int[] orange = array2 = new int[] { 1,3,5,8,46,48,50,53 };
        for (final int j : array2) {
            this.setItem(j, GlassColor.get(GlassColor.LIME).setCustomName(Util.fixColor("&r")));
        }
    }
    
    public void setServerGui() {
        final int[] array;
        final int[] black = array = new int[] { 0, 2, 3, 4, 5, 6, 8, 18, 26, 27, 35, 45, 47, 48, 49, 50, 51, 53 };
        for (final int i : array) {
            this.setItem(i, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        }
        final int[] array2;
        final int[] orange = array2 = new int[] { 1, 7, 9, 17, 36, 44, 46, 52 };
        for (final int j : array2) {
            this.setItem(j, GlassColor.get(GlassColor.LIME).setCustomName(Util.fixColor("&r")));
        }
    }
    
    @Override
    protected List<BlockVector3> onOpenBlock(final Player who) {
        final BlockVector3 blockPosition = new BlockVector3((int)who.x, (int)who.y + 2, (int)who.z);
        this.placeChest(who, blockPosition);
        return Collections.singletonList(blockPosition);
    }
    
    protected void placeChest(final Player who, final BlockVector3 pos) {
        final UpdateBlockPacket updateBlock = new UpdateBlockPacket();
        updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(54, 0);
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
        final CompoundTag tag = new CompoundTag().putString("id", "Chest").putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z).putString("CustomName", (this.title == null) ? "Chest" : this.title);
        try {
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to create NBT for chest");
        }
    }
}
