package xyz.rokkiitt.sector.objects.inventory.inventories;

import cn.nukkit.*;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.inventory.*;

public class WorkbenchMobileInventory extends WBInventory
{
    public WorkbenchMobileInventory(final Player p) {
        super(null, Util.fixColor("&6Panel serca"));
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
    }
}
