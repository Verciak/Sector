package xyz.rokkiitt.sector.objects.abbys;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.Config;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.tasks.SecondTask;
import xyz.rokkiitt.sector.utils.Util;

public class AbbysInventory extends DoubleChestFakeInventory {

    private final Player who;

    public AbbysInventory(final Player p) {
        super(null, Util.fixColor("&6OTCHLAN"));
        this.who = p;
        this.refreshGui();
        p.addWindow(this);
    }

    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        int slot = e.getAction().getSlot();
    }

    private void refreshGui() {
        final Config c = Main.getPlugin().getConfig();
        this.clearAll();
        int is = 0;
        for (Item i : SecondTask.items){
            this.setItem(is, i);
            is++;
        }
    }
}
