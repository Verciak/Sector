package xyz.rokkiitt.sector.objects;

import cn.nukkit.level.*;
import cn.nukkit.*;
import cn.nukkit.inventory.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.item.*;
import cn.nukkit.math.*;

public class FixedAnvilInventory extends AnvilInventory
{
    public FixedAnvilInventory(final PlayerUIInventory playerUI, final Position position) {
        super(playerUI, position);
    }
    
    public void onClose(final Player who) {
        final ContainerClosePacket pk = new ContainerClosePacket();
        pk.windowId = who.getWindowId((Inventory)this);
        pk.wasServerInitiated = (who.getClosingWindowId() != pk.windowId);
        who.dataPacket((DataPacket)pk);
        who.craftingType = 0;
        Item[] drops = { this.getInputSlot(), this.getMaterialSlot() };
        final Item[] addItem;
        drops = (addItem = who.getInventory().addItem(drops));
        for (final Item drop : addItem) {
            if (!who.dropItem(drop)) {
                this.getHolder().getLevel().dropItem(this.getHolder().add(0.5, 0.5, 0.5), drop);
            }
        }
        this.clear(0);
        this.clear(1);
        who.resetCraftingGridType();
    }
}
