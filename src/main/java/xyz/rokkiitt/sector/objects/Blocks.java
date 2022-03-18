package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.*;
import cn.nukkit.inventory.*;

public class Blocks extends DoubleChestFakeInventory
{
    static Item bar;
    
    public Blocks(final Player p) {
        super(null, Util.fixColor("&6Bloki"));
        this.holder = (InventoryHolder)p;
        this.show();
        p.addWindow((Inventory)this);
    }
    
    private void show() {
        this.clearAll();
        this.setItem(10, new ItemBuilder(264).setTitle("&r&eZamien diamenty na diamentowe bloki").build());
        this.setItem(19, new ItemBuilder(57).setTitle("&r&eZamien diamentowe bloki na diamenty").build());
        this.setItem(12, new ItemBuilder(266).setTitle("&r&eZamien sztabki zlota na zlote bloki").build());
        this.setItem(21, new ItemBuilder(41).setTitle("&r&eZamien zlote bloki na sztabki zlota").build());
        this.setItem(14, new ItemBuilder(388).setTitle("&r&eZamien emeraldy na emeraldowe bloki").build());
        this.setItem(23, new ItemBuilder(133).setTitle("&r&eZamien emeraldowe bloki na emeraldy").build());
        this.setItem(16, new ItemBuilder(265).setTitle("&r&eZamien sztabki zelaza na zelazne bloki").build());
        this.setItem(25, new ItemBuilder(42).setTitle("&r&eZamien zelazne bloki na sztabki zelaza").build());
        this.setItem(38, new ItemBuilder(331).setTitle("&r&eZamien redstone na redstone bloki").build());
        this.setItem(37, new ItemBuilder(152).setTitle("&r&eZamien redstone bloki na redstone").build());
        this.setItem(42, new ItemBuilder(263).setTitle("&r&eZamien wegiel na weglowe bloki").build());
        this.setItem(43, new ItemBuilder(173).setTitle("&r&eZamien weglowe bloki na wegiel").build());
        this.setItem(16, new ItemBuilder(265).setTitle("&r&eZamien sztabki zelaza na zelazne bloki").build());
        this.setItem(25, new ItemBuilder(42).setTitle("&r&eZamien zelazne bloki na sztabki zelaza").build());
        this.setItem(40, new ItemBuilder(399).setTitle("&r&eZamien wszystkie sztabki na bloki").build());
        this.setItem(49, new ItemBuilder(399).setTitle("&r&eZamien wszystkie bloki na sztabki").build());
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        final Player p = e.getPlayer();
        final Item clicked = e.getAction().getSourceItem();
        if (clicked != null) {
            if (clicked.getId() == 264) {
                this.replaceIngots(p, 264, 57);
                return;
            }
            if (clicked.getId() == 57) {
                this.replaceBlocks(p, 57, 264);
                return;
            }
            if (clicked.getId() == 266) {
                this.replaceIngots(p, 266, 41);
                return;
            }
            if (clicked.getId() == 41) {
                this.replaceBlocks(p, 41, 266);
                return;
            }
            if (clicked.getId() == 388) {
                this.replaceIngots(p, 388, 133);
                return;
            }
            if (clicked.getId() == 133) {
                this.replaceBlocks(p, 133, 388);
                return;
            }
            if (clicked.getId() == 265) {
                this.replaceIngots(p, 265, 42);
                return;
            }
            if (clicked.getId() == 42) {
                this.replaceBlocks(p, 42, 265);
                return;
            }
            if (clicked.getId() == 331) {
                this.replaceIngots(p, 331, 152);
                return;
            }
            if (clicked.getId() == 152) {
                this.replaceBlocks(p, 152, 331);
                return;
            }
            if (clicked.getId() == 263) {
                this.replaceIngots(p, 263, 173);
                return;
            }
            if (clicked.getId() == 173) {
                this.replaceBlocks(p, 173, 263);
                return;
            }
            if (clicked.getId() == 399) {
                if (e.getAction().getSlot() == 40) {
                    this.replaceIngots(p, 264, 57);
                    this.replaceIngots(p, 266, 41);
                    this.replaceIngots(p, 388, 133);
                    this.replaceIngots(p, 265, 42);
                    this.replaceIngots(p, 331, 152);
                    this.replaceIngots(p, 263, 173);
                    return;
                }
                if (e.getAction().getSlot() == 49) {
                    this.replaceBlocks(p, 57, 264);
                    this.replaceBlocks(p, 41, 266);
                    this.replaceBlocks(p, 133, 388);
                    this.replaceBlocks(p, 42, 265);
                    this.replaceBlocks(p, 152, 331);
                    this.replaceBlocks(p, 173, 263);
                }
            }
        }
    }
    
    private void replaceIngots(final Player player, final int item, final int b) {
        int what = Util.getItemTypeCount(player, item);
        int block = 9;
        final int wynik;
        what = (wynik = what / block);
        final int wynik2;
        block = (wynik2 = block * what);
        if (wynik2 != 0) {
            Util.removeItemById(player, Item.get(item, Integer.valueOf(0), wynik2));
            Util.giveItem(player, Item.get(b, Integer.valueOf(0), wynik));
        }
    }
    
    private void replaceBlocks(final Player player, final int b, final int item) {
        final int what = Util.getItemTypeCount(player, b);
        int amount = 9;
        final int wynik;
        amount = (wynik = amount * what);
        if (wynik >= 9) {
            Util.removeItemById(player, Item.get(b, Integer.valueOf(0), what));
            Util.giveItem(player, Item.get(item, Integer.valueOf(0), wynik));
        }
    }
    
    static {
        Blocks.bar = new ItemBuilder(101).setTitle(" ").build();
    }
}
