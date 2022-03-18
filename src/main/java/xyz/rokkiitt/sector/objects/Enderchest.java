package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import cn.nukkit.*;
import cn.nukkit.inventory.*;

public class Enderchest extends ChestFakeInventory
{
    public Enderchest(final Player p) {
        super(null, "Ender Chest");
        this.holder = (InventoryHolder)p;
        this.setContents(p.getEnderChestInventory().getContents());
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
    }
    
    @Override
    public void onClose(final Player who) {
        who.getEnderChestInventory().setContents(this.getContents());
        super.onClose(who);
    }
}
