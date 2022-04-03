package xyz.rokkiitt.sector.objects.premiumcase;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.objects.pandora.Pandora;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PremiumCaseGUI extends ChestFakeInventory {

    private final Player who;

    public PremiumCaseGUI(final Player p) {
        super(null, Util.fixColor("&f&oOtwieranie &8..."));
        this.who = p;
        this.refreshGui();
        p.addWindow((Inventory) this);
    }

    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
    }

    private void refreshGui() {
        ItemBuilder backgroup = (new ItemBuilder(160, 1, (short) 15)).setTitle(Util.fixColor("&8-&cPuste pole&8-"));
        ItemBuilder biale = (new ItemBuilder(160, 1, (short) 0)).setTitle(Util.fixColor("&8-&cPuste pole&8-"));
        ItemBuilder win = (new ItemBuilder(Item.TORCH, 1, (short) 0)).setTitle(Util.fixColor("&a&lWYGRANA"));
        ItemBuilder szyba1 = (new ItemBuilder(160, 1, (short) 8)).setTitle(Util.fixColor("&8-&cPuste pole&8-"));
        int i;
        for (i = 0; i <= 1; i++)
            setItem(i, backgroup.build());
        setItem(2, szyba1.build());
        setItem(3, biale.build());
        setItem(4, win.build());
        setItem(5, biale.build());
        setItem(6, szyba1.build());
        for (i = 7; i <= 8; i++)
            setItem(i, backgroup.build());
        for (i = 18; i <= 19; i++)
            setItem(i, backgroup.build());
        setItem(20, szyba1.build());
        setItem(21, biale.build());
        setItem(22, win.build());
        setItem(23, biale.build());
        setItem(24, szyba1.build());
        for (i = 25; i <= 26; i++)
            setItem(i, backgroup.build());

        Item i1 = CaseManager.drop.get(0).getWhat();
        Item i2 = CaseManager.drop.get(1).getWhat();
        Item i3 = CaseManager.drop.get(2).getWhat();
        Item i4 = CaseManager.drop.get(3).getWhat();
        Item i5 = CaseManager.drop.get(4).getWhat();
        Item i6 = CaseManager.drop.get(5).getWhat();
        Item i7 = CaseManager.drop.get(6).getWhat();
        Item i8 = CaseManager.drop.get(7).getWhat();
        Item i9 = CaseManager.drop.get(8).getWhat();
            setItem(9, i1);
            setItem(10, i2);
            setItem(11, i3);
            setItem(12, i4);
            setItem(13, i5);
            setItem(14, i6);
            setItem(15, i7);
            setItem(16, i8);
            setItem(17, i9);
            if(CaseManager.isInCase(who)){
                return;
            }else {
                CaseInv caseInv = new CaseInv(who, this);
                CaseManager.addCase(who, caseInv);
            }
    }

}
