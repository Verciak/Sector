package xyz.rokkiitt.sector.objects.pandora;

import cn.nukkit.item.*;

public class Pandora
{
    private final double chance;
    private final int minAmount;
    private final int maxAmount;
    private final Item what;
    private final int slot;
    
    public Pandora(final Item what, final int slot, final double chance, final int minamount, final int maxamount) {
        this.chance = chance;
        this.slot = slot;
        this.minAmount = minamount;
        this.maxAmount = maxamount;
        this.what = what;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public double getChance() {
        return this.chance;
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
