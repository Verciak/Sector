package xyz.rokkiitt.sector.objects.entity.blockentity;

import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.item.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.event.inventory.*;
import cn.nukkit.event.*;
import cn.nukkit.math.*;
import cn.nukkit.inventory.*;

public class FixedHopperTile extends BlockEntityHopper
{
    public FixedHopperTile(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        --this.transferCooldown;
        if (this.level.isBlockPowered((Vector3)this.getBlock())) {
            return true;
        }
        if (!this.isOnTransferCooldown()) {
            boolean changed = this.pushItems();
            if (!changed) {
                final BlockEntity blockEntity = this.level.getBlockEntity(this.up());
                if (!(blockEntity instanceof BlockEntityContainer)) {
                    changed = this.pickupItems();
                }
                else {
                    changed = this.pullItems();
                }
            }
            if (changed) {
                this.setDirty();
            }
            this.setTransferCooldown(100);
        }
        return true;
    }
    
    public int getItemCount(final Inventory inv, final Item item) {
        int count = 0;
        for (int i = 0; i < inv.getSize(); ++i) {
            final Item items = inv.getItem(i);
            if (items.isNull()) {
                return item.getCount();
            }
            count += items.getMaxStackSize() - items.getCount();
        }
        if (count > item.getCount()) {
            count = item.getCount();
        }
        return count;
    }
    
    public boolean pullItems() {
        if (this.inventory.isFull()) {
            return false;
        }
        final BlockEntity blockEntity = this.level.getBlockEntity(this.up());
        if (blockEntity instanceof ChestTile && ((ChestTile)blockEntity).isLocked()) {
            return false;
        }
        if (blockEntity instanceof BlockEntityFurnace) {
            final FurnaceInventory inv = ((BlockEntityFurnace)blockEntity).getInventory();
            final Item item = inv.getResult();
            if (item != null && !item.isNull()) {
                final Item itemToAdd = item.clone();
                itemToAdd.count = 1;
                if (!this.inventory.canAddItem(itemToAdd)) {
                    return false;
                }
                final InventoryMoveItemEvent ev = new InventoryMoveItemEvent((Inventory)inv, (Inventory)this.inventory, (InventoryHolder)this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                this.server.getPluginManager().callEvent((Event)ev);
                if (ev.isCancelled()) {
                    return false;
                }
                final Item[] items = this.inventory.addItem(new Item[] { itemToAdd });
                if (items.length <= 0) {
                    final Item item3 = item;
                    --item3.count;
                    inv.setResult(item);
                    return true;
                }
            }
        }
        else if (blockEntity instanceof InventoryHolder) {
            final Inventory inv2 = ((InventoryHolder)blockEntity).getInventory();
            for (int i = 0; i < inv2.getSize(); ++i) {
                final Item item2 = inv2.getItem(i);
                if (!item2.isNull()) {
                    final Item itemToAdd2 = item2.clone();
                    itemToAdd2.count = this.getItemCount(inv2, itemToAdd2);
                    if (this.inventory.canAddItem(itemToAdd2)) {
                        final InventoryMoveItemEvent ev2 = new InventoryMoveItemEvent(inv2, (Inventory)this.inventory, (InventoryHolder)this, itemToAdd2, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                        this.server.getPluginManager().callEvent((Event)ev2);
                        if (!ev2.isCancelled()) {
                            final Item[] items2 = this.inventory.addItem(new Item[] { itemToAdd2 });
                            if (items2.length < 1) {
                                final Item item4 = item2;
                                item4.count -= itemToAdd2.getCount();
                                inv2.setItem(i, item2);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean pushItems() {
        if (this.inventory.isEmpty()) {
            return false;
        }
        final BlockEntity be = this.level.getBlockEntity((Vector3)this.getSide(BlockFace.fromIndex(this.level.getBlockDataAt(this.getFloorX(), this.getFloorY(), this.getFloorZ()))));
        if (be instanceof ChestTile && ((ChestTile)be).isLocked()) {
            return false;
        }
        if ((be instanceof BlockEntityHopper && this.getBlock().getDamage() == 0) || !(be instanceof InventoryHolder)) {
            return false;
        }
        if (be instanceof BlockEntityFurnace) {
            final BlockEntityFurnace furnace = (BlockEntityFurnace)be;
            final FurnaceInventory inventory = furnace.getInventory();
            if (inventory.isFull()) {
                return false;
            }
            boolean pushedItem = false;
            for (int i = 0; i < this.inventory.getSize(); ++i) {
                final Item item = this.inventory.getItem(i);
                if (!item.isNull()) {
                    final Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);
                    if (this.getBlock().getDamage() == 0) {
                        final Item smelting = inventory.getSmelting();
                        if (smelting.isNull()) {
                            final InventoryMoveItemEvent event = new InventoryMoveItemEvent((Inventory)this.inventory, (Inventory)inventory, (InventoryHolder)this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent((Event)event);
                            if (!event.isCancelled()) {
                                inventory.setSmelting(itemToAdd);
                                final Item item3 = item;
                                --item3.count;
                                pushedItem = true;
                            }
                        }
                        else if (inventory.getSmelting().getId() == itemToAdd.getId() && inventory.getSmelting().getDamage() == itemToAdd.getDamage() && smelting.count < smelting.getMaxStackSize()) {
                            final InventoryMoveItemEvent event = new InventoryMoveItemEvent((Inventory)this.inventory, (Inventory)inventory, (InventoryHolder)this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent((Event)event);
                            if (!event.isCancelled()) {
                                final Item item4 = smelting;
                                ++item4.count;
                                inventory.setSmelting(smelting);
                                final Item item5 = item;
                                --item5.count;
                                pushedItem = true;
                            }
                        }
                    }
                    else if (Fuel.duration.containsKey(itemToAdd.getId())) {
                        final Item fuel = inventory.getFuel();
                        if (fuel.isNull()) {
                            final InventoryMoveItemEvent event = new InventoryMoveItemEvent((Inventory)this.inventory, (Inventory)inventory, (InventoryHolder)this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent((Event)event);
                            if (!event.isCancelled()) {
                                inventory.setFuel(itemToAdd);
                                final Item item6 = item;
                                --item6.count;
                                pushedItem = true;
                            }
                        }
                        else if (fuel.getId() == itemToAdd.getId() && fuel.getDamage() == itemToAdd.getDamage() && fuel.count < fuel.getMaxStackSize()) {
                            final InventoryMoveItemEvent event = new InventoryMoveItemEvent((Inventory)this.inventory, (Inventory)inventory, (InventoryHolder)this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent((Event)event);
                            if (!event.isCancelled()) {
                                final Item item7 = fuel;
                                ++item7.count;
                                inventory.setFuel(fuel);
                                final Item item8 = item;
                                --item8.count;
                                pushedItem = true;
                            }
                        }
                    }
                    if (pushedItem) {
                        this.inventory.setItem(i, item);
                    }
                }
            }
            return pushedItem;
        }
        else {
            final Inventory inventory2 = ((InventoryHolder)be).getInventory();
            if (inventory2.isFull()) {
                return false;
            }
            for (int j = 0; j < this.inventory.getSize(); ++j) {
                final Item item2 = this.inventory.getItem(j);
                if (!item2.isNull()) {
                    final Item itemToAdd2 = item2.clone();
                    itemToAdd2.count = this.getItemCount(inventory2, itemToAdd2);
                    if (inventory2.canAddItem(itemToAdd2)) {
                        final InventoryMoveItemEvent ev = new InventoryMoveItemEvent((Inventory)this.inventory, inventory2, (InventoryHolder)this, itemToAdd2, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                        this.server.getPluginManager().callEvent((Event)ev);
                        if (!ev.isCancelled()) {
                            final Item[] items = inventory2.addItem(new Item[] { itemToAdd2 });
                            if (items.length <= 0) {
                                final Item item9 = item2;
                                item9.count -= itemToAdd2.getCount();
                                this.inventory.setItem(j, item2);
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
}
