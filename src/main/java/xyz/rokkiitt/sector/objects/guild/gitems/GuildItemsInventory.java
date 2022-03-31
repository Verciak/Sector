package xyz.rokkiitt.sector.objects.guild.gitems;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.Util;

public class GuildItemsInventory extends DoubleChestFakeInventory
{
    public GuildItemsInventory() {
        super(null, Util.fixColor("&6Itemy"));
        this.setServerGui();
        for (final GuildItems id : GuildItemsManager.getItems()) {
            this.setItem(id.getSlot(), id.getWhat().setLore("\u270b", Util.fixColor("&r&8>> &fIlosc: &e" + id.getAmount()), ""));
        }
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
    }
}
