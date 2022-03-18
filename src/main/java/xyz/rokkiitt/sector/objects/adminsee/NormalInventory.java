package xyz.rokkiitt.sector.objects.adminsee;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;

public class NormalInventory extends DoubleChestFakeInventory
{
    private Player who;
    
    public NormalInventory(final Player p, final Player who) {
        super(null, Util.fixColor("&6Inventory gracza: " + who.getName()));
        this.setContents(who.getInventory().getContents());
        this.who = who;
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
    }
    
    @Override
    public void onClose(final Player p) {
        if (this.who.isOnline()) {
            this.who.getInventory().setContents(this.getContents());
        }
        super.onClose(p);
    }
}
