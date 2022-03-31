package xyz.rokkiitt.sector.objects.drop.gui;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.DyeColor;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.cobblex.Cobblex;
import xyz.rokkiitt.sector.objects.cobblex.CobblexManager;
import xyz.rokkiitt.sector.objects.drop.DropInventory;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteDrop;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.pandora.Pandora;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;

public class MainDropInventory extends DoubleChestFakeInventory {
    static Item cx;
    static Item pcase;
    static Item pcasekilof;
    static Item pcasebikon;
    static Item meteorite;
    static Item stone;
    static Item pandora;

    private final User u;
    private final Player who;

    public MainDropInventory(final Player p, final User u) {
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
        if(slot == 20){
            new PremiumCaseDropInventory(p, u);
        }
        if(slot == 22){
            new DropInventory(p, u);
        }
        if(slot == 24){
            new PandoraDropInventory(p, u);
        }
        if(slot == 39){
            new CXDropInventory(p, u);
        }
        if(slot == 41){
            new MeteorDropInventory(p, u);
        }
        e.setCancelled(true);
    }

    private void refreshGui() {
        this.clearAll();
        this.fill();
        this.setServerGui();
        this.setItem(22,
                new ItemBuilder(Item.STONE)
                        .setTitle("&r&l&6DROP Z KAMIENIA")
                        .setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(24,
                new ItemBuilder(Item.DRAGON_EGG)
                        .setTitle("&r&l&6DROP Z PANDORY")
                        .setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(39,
                new ItemBuilder(Item.MOSSY_STONE)
                        .setTitle("&r&l&6DROP Z COBBLEXA")
                        .setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(20,
                new ItemBuilder(Item.CHEST)
                        .setTitle("&r&l&6DROP Z PREMIUMCASE")
                        .setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(41,
                new ItemBuilder(Item.CHORUS_FRUIT)
                        .setTitle("&r&l&6DROP Z METEORYTU")
                        .setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Kliknij, aby zobaczyc drop!"})).build());
    }
}
