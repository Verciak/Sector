package xyz.rokkiitt.sector.objects.inventory.inventories;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import cn.nukkit.event.*;
import com.google.common.base.*;
import cn.nukkit.inventory.*;
import cn.nukkit.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.level.*;
import cn.nukkit.math.*;
import cn.nukkit.inventory.transaction.action.*;
import cn.nukkit.item.*;
import java.util.*;
import java.util.concurrent.*;

public abstract class FakeInventory extends ContainerInventory
{
    private static final BlockVector3 ZERO;
    public static final Map<Player, FakeInventory> open;
    protected final Map<Player, List<BlockVector3>> blockPositions;
    private boolean closed;
    protected String title;
    
    public FakeInventory(final InventoryType type, final InventoryHolder holder, final String title) {
        super(holder, type);
        this.blockPositions = new HashMap<Player, List<BlockVector3>>();
        this.closed = false;
        this.title = ((title == null) ? type.getDefaultTitle() : title);
    }
    
    @EventHandler
    protected abstract void onSlotChange(final FakeSlotChangeEvent p0);
    
    public void onOpen(final Player who) {
        Preconditions.checkState(!this.closed, (Object)"Already closed");
        this.viewers.add(who);
        if (FakeInventory.open.putIfAbsent(who, this) != null) {
            throw new IllegalStateException("Inventory was already open");
        }
        final List<BlockVector3> blocks = this.onOpenBlock(who);
        this.blockPositions.put(who, blocks);
        this.onFakeOpen(who, blocks);
    }
    
    protected void onFakeOpen(final Player who, final List<BlockVector3> blocks) {
        final BlockVector3 blockPosition = blocks.isEmpty() ? FakeInventory.ZERO : blocks.get(0);
        final ContainerOpenPacket containerOpen = new ContainerOpenPacket();
        containerOpen.windowId = who.getWindowId((Inventory)this);
        containerOpen.type = this.getType().getNetworkType();
        containerOpen.x = blockPosition.x;
        containerOpen.y = blockPosition.y;
        containerOpen.z = blockPosition.z;
        who.dataPacket((DataPacket)containerOpen);
        this.sendContents(who);
    }
    
    protected abstract List<BlockVector3> onOpenBlock(final Player p0);
    
    public void onClose(final Player who) {
        super.onClose(who);
        FakeInventory.open.remove(who, this);
        final List<BlockVector3> blocks = this.blockPositions.get(who);
        if (blocks != null && !blocks.isEmpty()) {
            for (int i = 0, size = blocks.size(); i < size; ++i) {
                final int index = i;
                Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                    Vector3 blockPosition = blocks.get(index).asVector3();
                    UpdateBlockPacket updateBlock = new UpdateBlockPacket();
                    updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(who.getLevel().getBlock(blockPosition).getFullId());
                    updateBlock.flags = 11;
                    updateBlock.x = blockPosition.getFloorX();
                    updateBlock.y = blockPosition.getFloorY();
                    updateBlock.z = blockPosition.getFloorZ();
                    who.dataPacket((DataPacket)updateBlock);
                    return;
                }, 2 + i, false);
            }
        }
    }
    
    public List<BlockVector3> getPosition(final Player player) {
        Preconditions.checkState(!this.closed, (Object)"Already closed");
        return this.blockPositions.getOrDefault(player, null);
    }
    
    public boolean onSlotChange(final Player source, final SlotChangeAction action) {
        final FakeSlotChangeEvent event = new FakeSlotChangeEvent(source, this, action);
        this.onSlotChange(event);
        return event.isCancelled();
    }
    
    public void close() {
        Preconditions.checkState(!this.closed, (Object)"Already closed");
        this.getViewers().forEach(player -> player.removeWindow((Inventory)this));
        this.closed = true;
    }
    
    public void clearAll() {
        for (int i = 0; i < this.getSize(); ++i) {
            this.setItem(i, Item.get(0));
        }
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        if (title == null) {
            this.title = this.type.getDefaultTitle();
        }
        else {
            this.title = title;
        }
    }
    
    public Item[] addOneItem(final Item... slots) {
        final List<Item> itemSlots = new ArrayList<Item>();
        for (final Item slot : slots) {
            if (slot.getId() != 0 && slot.getCount() > 0) {
                itemSlots.add(slot.clone());
            }
        }
        final List<Integer> emptySlots = new ArrayList<Integer>();
        for (int i = 0; i < this.getSize(); ++i) {
            final Item item = this.getItem(i);
            if (item.getId() == 0 || item.getCount() <= 0) {
                emptySlots.add(i);
            }
            if (itemSlots.isEmpty()) {
                break;
            }
        }
        if (!itemSlots.isEmpty() && !emptySlots.isEmpty()) {
            for (final int slotIndex : emptySlots) {
                if (!itemSlots.isEmpty()) {
                    final Item slot = itemSlots.get(0);
                    int amount = Math.min(slot.getMaxStackSize(), slot.getCount());
                    amount = Math.min(amount, this.getMaxStackSize());
                    slot.setCount(slot.getCount() - amount);
                    final Item item2 = slot.clone();
                    item2.setCount(amount);
                    this.setItem(slotIndex, item2);
                    if (slot.getCount() > 0) {
                        continue;
                    }
                    itemSlots.remove(slot);
                }
            }
        }
        return itemSlots.toArray(new Item[0]);
    }
    
    static {
        ZERO = new BlockVector3(0, 0, 0);
        open = new ConcurrentHashMap<Player, FakeInventory>();
    }
}
