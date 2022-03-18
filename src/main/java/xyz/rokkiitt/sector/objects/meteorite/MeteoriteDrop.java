package xyz.rokkiitt.sector.objects.meteorite;

import cn.nukkit.item.*;

public class MeteoriteDrop
{
    private final int minAmount;
    private final int maxAmount;
    private final Item what;
    private final int slot;
    
    public MeteoriteDrop(final Item w, final int slot, final int minamount, final int maxamount) {
        this.slot = slot;
        this.minAmount = minamount;
        this.maxAmount = maxamount;
        this.what = w;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public int getMinAmount() {
        return this.minAmount;
    }
    
    public int getMaxAmount() {
        return this.maxAmount;
    }
    
    public Item getWhat() {
        return this.what.clone();
    }
}
