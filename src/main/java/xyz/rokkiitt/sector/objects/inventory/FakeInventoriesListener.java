package xyz.rokkiitt.sector.objects.inventory;

import cn.nukkit.event.server.*;
import cn.nukkit.math.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.event.*;
import cn.nukkit.event.inventory.*;
import xyz.rokkiitt.sector.objects.inventory.inventories.FakeInventory;
import cn.nukkit.inventory.transaction.action.*;
import java.util.*;

import cn.nukkit.*;

public class FakeInventoriesListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPacketSend(final DataPacketSendEvent event) {
        final DataPacket packet = event.getPacket();
        if (packet instanceof UpdateBlockPacket) {
            final UpdateBlockPacket updateBlock = (UpdateBlockPacket)packet;
            final List<BlockVector3> positions = FakeInventories.getFakeInventoryPositions(event.getPlayer());
            if (positions != null) {
                for (final BlockVector3 pos : positions) {
                    if (pos.x == updateBlock.x && pos.y == updateBlock.y && pos.z == updateBlock.z) {
                        event.setCancelled();
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTransaction(final InventoryTransactionEvent event) {
        final Map<FakeInventory, List<SlotChangeAction>> actions = new HashMap<FakeInventory, List<SlotChangeAction>>();
        final Player source = event.getTransaction().getSource();
        for (final InventoryAction action : event.getTransaction().getActions()) {
            if (action instanceof SlotChangeAction) {
                final SlotChangeAction slotChange = (SlotChangeAction)action;
                if (!(slotChange.getInventory() instanceof FakeInventory)) {
                    continue;
                }
                final FakeInventory inventory = (FakeInventory)slotChange.getInventory();
                final List<SlotChangeAction> slotChanges = actions.computeIfAbsent(inventory, fakeInventory -> new ArrayList());
                slotChanges.add(slotChange);
            }
        }
        boolean cancel = false;
        for (final Map.Entry<FakeInventory, List<SlotChangeAction>> entry : actions.entrySet()) {
            for (final SlotChangeAction action2 : entry.getValue()) {
                if (entry.getKey().onSlotChange(source, action2)) {
                    cancel = true;
                }
            }
        }
        if (cancel) {
            event.setCancelled();
        }
    }
}
