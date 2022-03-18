package xyz.rokkiitt.sector.objects.adminsee;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;

public class EnderInventory extends ChestFakeInventory
{
    private Player who;
    
    public EnderInventory(final Player p, final Player who) {
        super(null, Util.fixColor("&6Ender gracza: " + who.getName()));
        this.setContents(who.getEnderChestInventory().getContents());
        this.who = who;
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
    }
    
    @Override
    public void onClose(final Player p) {
        if (this.who.isOnline()) {
            this.who.getEnderChestInventory().setContents(this.getContents());
        }
        super.onClose(p);
    }
}
