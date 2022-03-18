package xyz.rokkiitt.sector.objects.inventory;

import cn.nukkit.event.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.objects.inventory.inventories.FakeInventory;
import cn.nukkit.inventory.transaction.action.*;

public class FakeSlotChangeEvent implements Cancellable
{
    private final Player player;
    private final FakeInventory inventory;
    private final SlotChangeAction action;
    private boolean cancelled;
    
    public FakeSlotChangeEvent(final Player player, final FakeInventory inventory, final SlotChangeAction action) {
        this.cancelled = false;
        this.player = player;
        this.inventory = inventory;
        this.action = action;
    }
    
    public SlotChangeAction getAction() {
        return this.action;
    }
    
    public FakeInventory getInventory() {
        return this.inventory;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public void setCancelled() {
        this.cancelled = true;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean arg0) {
        this.cancelled = arg0;
    }
}
