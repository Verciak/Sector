package xyz.rokkiitt.sector.objects.guild.gitems;

import cn.nukkit.item.*;

public class GuildItems
{
    private final int amount;
    private Item what;
    private final int slot;
    
    public GuildItems(final Item what, final int slot, final int amount) {
        this.slot = slot;
        this.amount = amount;
        this.what = what;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public Item getWhat() {
        return this.what.clone();
    }
}
