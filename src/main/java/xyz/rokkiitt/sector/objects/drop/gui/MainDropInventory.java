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
        e.setCancelled(true);
    }

    private void refreshGui() {
        this.clearAll();
        this.setServerGui();
        this.setItem(34,
                new ItemBuilder(Item.STONE)
                        .setTitle("&r&l&6DROP Z KAMIENIA")
                        .setLore(Util.fixColor(new String[]{"", "&7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(42,
                new ItemBuilder(Item.DRAGON_EGG)
                        .setTitle("&r&l&6DROP Z PANDORY")
                        .setLore(Util.fixColor(new String[]{"", "&7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(29,
                new ItemBuilder(Item.MOSSY_STONE)
                        .setTitle("&r&l&6DROP Z COBBLEXA")
                        .setLore(Util.fixColor(new String[]{"", "&7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(11,
                new ItemBuilder(Item.CHEST)
                        .setTitle("&r&l&6DROP Z SKRZYNKI &8[50%/50% BEACONA&8]")
                        .setLore(Util.fixColor(new String[]{"", "&7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(15,
                new ItemBuilder(Item.CHEST)
                        .setTitle("&r&l&DROP Z SKRZYNKI &8[50%/50% 6/3/3&8]")
                        .setLore(Util.fixColor(new String[]{"", "&7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(22,
                new ItemBuilder(Item.CHEST)
                        .setTitle("&r&l&6DROP Z MAGICZNEJ SKRZYNI")
                        .setLore(Util.fixColor(new String[]{"", "&7Kliknij, aby zobaczyc drop!"})).build());
        this.setItem(39,
                new ItemBuilder(Item.CHORUS_FRUIT)
                        .setTitle("&r&l&6DROP Z METEORYTU")
                        .setLore(Util.fixColor(new String[]{"", "&7Kliknij, aby zobaczyc drop!"})).build());
    }
}
