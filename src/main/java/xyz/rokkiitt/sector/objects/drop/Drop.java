package xyz.rokkiitt.sector.objects.drop;

import cn.nukkit.item.*;

public class Drop
{
    private final double chance;
    private final int exp;
    private final int minAmount;
    private final int maxAmount;
    private final int guislot;
    private final Item what;
    
    public Drop(final Item what, final double chance, final int xp, final int amountmin, final int amountmax, final int guislot) {
        this.chance = chance;
        this.exp = xp;
        this.minAmount = amountmin;
        this.maxAmount = amountmax;
        this.guislot = guislot;
        this.what = what;
    }
    
    public int getSlot() {
        return this.guislot;
    }
    
    public double getChance() {
        return this.chance;
    }
    
    public int getExp() {
        return this.exp;
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
