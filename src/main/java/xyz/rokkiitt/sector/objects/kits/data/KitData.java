package xyz.rokkiitt.sector.objects.kits.data;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;

public class KitData
{
    private final int amount;
    private Item what;
    private final int slot;
    
    public KitData(final Item what, final int slot, final int amount) {
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
        if (this.what instanceof ItemLeggingsDiamond && !Settings.ENABLE_DIAX) {
            return this.what = this.getClone(308);
        }
        if (this.what instanceof ItemLeggingsIron && Settings.ENABLE_DIAX) {
            return this.what = this.getClone(312);
        }
        if (this.what instanceof ItemChestplateDiamond && !Settings.ENABLE_DIAX) {
            return this.what = this.getClone(307);
        }
        if (this.what instanceof ItemChestplateIron && Settings.ENABLE_DIAX) {
            return this.what = this.getClone(311);
        }
        if (this.what instanceof ItemHelmetDiamond && !Settings.ENABLE_DIAX) {
            return this.what = this.getClone(306);
        }
        if (this.what instanceof ItemHelmetIron && Settings.ENABLE_DIAX) {
            return this.what = this.getClone(310);
        }
        if (this.what instanceof ItemBootsDiamond && !Settings.ENABLE_DIAX) {
            return this.what = this.getClone(309);
        }
        if (this.what instanceof ItemBootsIron && Settings.ENABLE_DIAX) {
            return this.what = this.getClone(313);
        }
        if (this.what instanceof ItemSwordDiamond && !Settings.ENABLE_DIAX) {
            return this.what = this.getClone(267);
        }
        if (this.what instanceof ItemSwordIron && Settings.ENABLE_DIAX) {
            return this.what = this.getClone(276);
        }
        return this.what.clone();
    }
    
    private Item getClone(final int id) {
        final Item what = Item.get(id);
        what.setCount(this.what.getCount());
        what.setDamage(Integer.valueOf(this.what.getDamage()));
        what.setLore(this.what.getLore());
        if (this.what.hasCustomName()) {
            what.setCustomName(Util.fixColor(this.what.getCustomName()));
        }
        if (this.what.hasCompoundTag()) {
            what.setCompoundTag(this.what.getCompoundTag());
        }
        if (this.what.hasEnchantments()) {
            what.addEnchantment(this.what.getEnchantments());
        }
        return what;
    }
}
