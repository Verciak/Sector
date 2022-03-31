package xyz.rokkiitt.sector.objects.drop.gui;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import xyz.rokkiitt.sector.objects.drop.DropInventory;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.pandora.Pandora;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;

public class PandoraDropInventory extends DoubleChestFakeInventory {

    private final User u;
    private final Player who;

    public PandoraDropInventory(final Player p, final User u) {
        super(null, Util.fixColor("&6MENU DROPU"));
        this.who = p;
        this.u = u;
        this.refreshGui();
        p.addWindow((Inventory) this);
    }

    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        int slot = e.getAction().getSlot();
        Player p = e.getPlayer();
        if(slot == 49){
            new MainDropInventory(p, u);
        }
        e.setCancelled(true);
    }

    private void refreshGui() {
        this.clearAll();
        this.fill();
        this.setServerGui();
        this.setItem(49, DropInventory.back);

        for(int i = 37; i < 44; i++) {
            this.setItem(i, new Item(0));
        }
        for (final Pandora id : PandoraManager.getItems()) {
            this.setItem(id.getSlot(), DropInventory.getCopyItem(id.getWhat().clone()).setLore(Util.fixColor(DropInventory.parseLore(id.getWhat().getLore(), new String[] { "\u270b", "&r&8>> &fSzansa na drop: &e" + Util.round(id.getChance()), "&r&8>> &fMinimalnie: &e" + id.getMinAmount(), "&r&8>> &fMaxymalnie: &e" + id.getMaxAmount() }))));
        }
    }
}
