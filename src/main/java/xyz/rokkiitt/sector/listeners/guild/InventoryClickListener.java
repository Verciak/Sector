package xyz.rokkiitt.sector.listeners.guild;

import cn.nukkit.event.inventory.*;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import cn.nukkit.inventory.transaction.action.*;
import cn.nukkit.inventory.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.inventory.transaction.*;
import cn.nukkit.item.*;
import cn.nukkit.event.*;

public class InventoryClickListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onTransaction(final InventoryTransactionEvent e) {
        if (e.isCancelled() || Config.isSpawn(e.getTransaction().getSource().getLocation())) {
            return;
        }
        final InventoryTransaction data = e.getTransaction();
        for (final InventoryAction action : data.getActions()) {
            if (action instanceof SlotChangeAction) {
                final SlotChangeAction ac = (SlotChangeAction)action;
                if (!(ac.getInventory() instanceof ChestInventory)) {
                    continue;
                }
                final Guild g = GuildManager.getGuild(((BlockEntityChest)ac.getInventory().getHolder()).getLocation());
                if (g == null) {
                    continue;
                }
                final Item takenOut = action.getSourceItem();
                final Item putIn = action.getTargetItem();
                if (!takenOut.isNull()) {
                    g.addLogblock(data.getSource().getName(), ((BlockEntityChest)ac.getInventory().getHolder()).getBlock().getLocationHash(), (takenOut.hasCustomName() ? takenOut.getCustomName().toUpperCase() : takenOut.getName().toUpperCase()) + "x" + takenOut.getCount(), "takenout", System.currentTimeMillis());
                }
                if (putIn.isNull()) {
                    continue;
                }
                g.addLogblock(data.getSource().getName(), ((BlockEntityChest)ac.getInventory().getHolder()).getBlock().getLocationHash(), (putIn.hasCustomName() ? putIn.getCustomName().toUpperCase() : putIn.getName().toUpperCase()) + "x" + putIn.getCount(), "putin", System.currentTimeMillis());
            }
        }
    }
}
