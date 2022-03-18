package xyz.rokkiitt.sector.objects.enchant;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.*;
import cn.nukkit.plugin.*;
import cn.nukkit.item.enchantment.*;
import cn.nukkit.inventory.*;
import cn.nukkit.event.player.*;

import java.util.*;
import cn.nukkit.event.*;

public class EnchantInventory extends ChestFakeInventory implements Listener
{
    private final Item prot3;
    private final Item prot2;
    private final Item prot1;
    private final Item featherfall2;
    private final Item featherfall1;
    private final Item unb3;
    private final Item unb2;
    private final Item unb1;
    private final Item eff5;
    private final Item eff4;
    private final Item eff3;
    private final Item eff2;
    private final Item eff1;
    private final Item fortune3;
    private final Item fortune2;
    private final Item fortune1;
    private final Item silk1;
    private final Item looting3;
    private final Item looting2;
    private final Item looting1;
    private final Item sharp3;
    private final Item sharp2;
    private final Item sharp1;
    private final Item fireasp1;
    private final Item knock1;
    private final Item knock2;
    private final Item power3;
    private final Item power2;
    private final Item power1;
    private final Item plomien1;
    static Item bar;
    private final int books;
    private Item item;
    private final HashMap<Integer, EnchantAction> enchants;
    private final List<Integer> boots;
    private final List<Integer> armors;
    private final List<Integer> swords;
    private final List<Integer> tools;
    private final List<Integer> pickaxe;
    
    public EnchantInventory(final Player p, final Item s, final int books) {
        super(null, Util.fixColor("&6Biblioteczek: " + books));
        this.prot3 = new ItemBuilder(403).setTitle("&r&6Ochrona III").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.prot2 = new ItemBuilder(403).setTitle("&r&6Ochrona II").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.prot1 = new ItemBuilder(403).setTitle("&r&6Ochrona I").setLore(new String[] { "&r&7Koszt: &e15 LvL" }).build();
        this.featherfall2 = new ItemBuilder(403).setTitle("&r&6Powolne opadanie II").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.featherfall1 = new ItemBuilder(403).setTitle("&r&6Powolne opadanie I").setLore(new String[] { "&r&7Koszt: &e15 LvL" }).build();
        this.unb3 = new ItemBuilder(403).setTitle("&r&6Niezniszczalnosc III").setLore(new String[] { "&r&7Koszt: &e40 LvL" }).build();
        this.unb2 = new ItemBuilder(403).setTitle("&r&6Niezniszczalnosc II").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.unb1 = new ItemBuilder(403).setTitle("&r&6Niezniszczalnosc I").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.eff5 = new ItemBuilder(403).setTitle("&r&6Wydajnosc V").setLore(new String[] { "&r&7Koszt: &e40 LvL" }).build();
        this.eff4 = new ItemBuilder(403).setTitle("&r&6Wydajnosc IV").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.eff3 = new ItemBuilder(403).setTitle("&r&6Wydajnosc III").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.eff2 = new ItemBuilder(403).setTitle("&r&6Wydajnosc II").setLore(new String[] { "&r&7Koszt: &e10 LvL" }).build();
        this.eff1 = new ItemBuilder(403).setTitle("&r&6Wydajnosc I").setLore(new String[] { "&r&7Koszt: &e5 LvL" }).build();
        this.fortune3 = new ItemBuilder(403).setTitle("&r&6Szczescie III").setLore(new String[] { "&r&7Koszt: &e35 LvL" }).build();
        this.fortune2 = new ItemBuilder(403).setTitle("&r&6Szczescie II").setLore(new String[] { "&r&7Koszt: &e25 LvL" }).build();
        this.fortune1 = new ItemBuilder(403).setTitle("&r&6Szczescie I").setLore(new String[] { "&r&7Koszt: &e15 LvL" }).build();
        this.silk1 = new ItemBuilder(403).setTitle("&r&6Jedwabny dotyk I").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.looting3 = new ItemBuilder(403).setTitle("&r&6Looting III").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.looting2 = new ItemBuilder(403).setTitle("&r&6Looting II").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.looting1 = new ItemBuilder(403).setTitle("&r&6Looting I").setLore(new String[] { "&r&7Koszt: &e10 LvL" }).build();
        this.sharp3 = new ItemBuilder(403).setTitle("&r&6Ostrosc III").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.sharp2 = new ItemBuilder(403).setTitle("&r&6Ostrosc II").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.sharp1 = new ItemBuilder(403).setTitle("&r&6Ostrosc I").setLore(new String[] { "&r&7Koszt: &e10 LvL" }).build();
        this.fireasp1 = new ItemBuilder(403).setTitle("&r&6Zaklety ogien I").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.knock1 = new ItemBuilder(403).setTitle("&r&6Odrzut I").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.knock2 = new ItemBuilder(403).setTitle("&r&6Odrzut II").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.power3 = new ItemBuilder(403).setTitle("&r&6Moc III").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.power2 = new ItemBuilder(403).setTitle("&r&6Moc II").setLore(new String[] { "&r&7Koszt: &e20 LvL" }).build();
        this.power1 = new ItemBuilder(403).setTitle("&r&6Moc I").setLore(new String[] { "&r&7Koszt: &e10 LvL" }).build();
        this.plomien1 = new ItemBuilder(403).setTitle("&r&6Plomien I").setLore(new String[] { "&r&7Koszt: &e30 LvL" }).build();
        this.enchants = new HashMap<Integer, EnchantAction>();
        this.boots = Arrays.asList(305, 313, 317, 309, 301);
        this.armors = Arrays.asList(303, 311, 315, 307, 299, 304, 312, 316, 308, 300, 302, 310, 314, 306, 298);
        this.swords = Arrays.asList(276, 283, 267, 272, 268);
        this.tools = Arrays.asList(279, 286, 258, 275, 271, 277, 284, 256, 273, 269, 293, 294, 292, 291, 290);
        this.pickaxe = Arrays.asList(278, 285, 257, 274, 270);
        Server.getInstance().getPluginManager().registerEvents((Listener)this, (Plugin) Main.getPlugin());
        this.books = books;
        this.item = s;
        this.holder = (InventoryHolder)p;
        this.openPage();
    }
    
    private void openPage() {
        this.enchants.clear();
        this.clearAll();
        this.setItem(0, EnchantInventory.bar);
        this.setItem(1, EnchantInventory.bar);
        this.setItem(2, EnchantInventory.bar);
        this.setItem(3, EnchantInventory.bar);
        this.setItem(4, EnchantInventory.bar);
        this.setItem(5, EnchantInventory.bar);
        this.setItem(6, EnchantInventory.bar);
        this.setItem(7, EnchantInventory.bar);
        this.setItem(8, EnchantInventory.bar);
        this.setItem(9, EnchantInventory.bar);
        this.setItem(10, this.item);
        this.setItem(11, EnchantInventory.bar);
        this.setItem(12, EnchantInventory.bar);
        if (this.pickaxe.contains(this.item.getId())) {
            if (this.books < 4) {
                this.setItem(13, this.eff1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(1), 5));
            }
            else if (this.books >= 4 && this.books < 8) {
                this.setItem(13, this.eff2);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(2), 10));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 20));
            }
            else if (this.books >= 8 && this.books < 12) {
                this.setItem(13, this.eff3);
                this.setItem(14, this.unb2);
                this.setItem(15, this.fortune1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(3), 20));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 30));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(18).setLevel(1), 15));
            }
            else if (this.books >= 12 && this.books < 16) {
                this.setItem(13, this.eff4);
                this.setItem(14, this.unb2);
                this.setItem(15, this.fortune2);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(4), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 30));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(18).setLevel(2), 25));
            }
            else if (this.books >= 16) {
                this.setItem(13, this.eff5);
                this.setItem(14, this.unb3);
                this.setItem(15, this.fortune3);
                this.setItem(16, this.silk1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(5), 40));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(3), 40));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(18).setLevel(3), 35));
                this.enchants.put(16, new EnchantAction(Enchantment.getEnchantment(16).setLevel(1), 30));
            }
        }
        else if (this.tools.contains(this.item.getId())) {
            if (this.books < 4) {
                this.setItem(13, this.eff1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(1), 5));
            }
            else if (this.books >= 4 && this.books < 8) {
                this.setItem(13, this.eff2);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(2), 10));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 20));
            }
            else if (this.books >= 8 && this.books < 12) {
                this.setItem(13, this.eff3);
                this.setItem(14, this.unb2);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(3), 20));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 30));
            }
            else if (this.books >= 12 && this.books < 16) {
                this.setItem(13, this.eff3);
                this.setItem(14, this.unb2);
                this.setItem(15, this.silk1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(4), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 30));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(16).setLevel(1), 20));
            }
            else if (this.books >= 16) {
                this.setItem(13, this.eff3);
                this.setItem(14, this.unb2);
                this.setItem(15, this.silk1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(15).setLevel(5), 40));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(3), 40));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(16).setLevel(1), 30));
            }
        }
        else if (this.swords.contains(this.item.getId())) {
            if (this.books < 4) {
                this.setItem(13, this.sharp1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(9).setLevel(1), 10));
            }
            else if (this.books >= 4 && this.books < 8) {
                this.setItem(13, this.sharp2);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(9).setLevel(2), 20));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 20));
            }
            else if (this.books >= 8 && this.books < 12) {
                this.setItem(13, this.sharp3);
                this.setItem(14, this.unb2);
                this.setItem(12, this.looting1);
                this.enchants.put(12, new EnchantAction(Enchantment.getEnchantment(14).setLevel(1), 10));
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(9).setLevel(3), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 30));
            }
            else if (this.books >= 12 && this.books < 16) {
                this.setItem(12, this.looting2);
                this.setItem(13, this.sharp3);
                this.setItem(14, this.unb3);
                this.setItem(15, this.knock1);
                this.enchants.put(12, new EnchantAction(Enchantment.getEnchantment(14).setLevel(2), 20));
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(9).setLevel(3), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(3), 40));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(12).setLevel(1), 20));
            }
            else if (this.books >= 16) {
                this.setItem(12, this.looting3);
                this.setItem(13, this.sharp3);
                this.setItem(14, this.unb3);
                this.setItem(15, this.fireasp1);
                this.setItem(16, this.knock2);
                this.enchants.put(12, new EnchantAction(Enchantment.getEnchantment(14).setLevel(3), 30));
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(9).setLevel(3), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(3), 40));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(13).setLevel(1), 20));
                this.enchants.put(16, new EnchantAction(Enchantment.getEnchantment(12).setLevel(2), 30));
            }
        }
        else if (this.armors.contains(this.item.getId())) {
            if (this.books < 4) {
                this.setItem(13, this.prot1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(1), 15));
            }
            else if (this.books >= 4 && this.books < 8) {
                this.setItem(13, this.prot1);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(1), 20));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 20));
            }
            else if (this.books >= 8 && this.books < 12) {
                this.setItem(13, this.prot2);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(2), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 30));
            }
            else if (this.books >= 12 && this.books < 16) {
                this.setItem(13, this.prot3);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(3), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 30));
            }
            else if (this.books >= 16) {
                this.setItem(13, this.prot3);
                this.setItem(14, this.unb2);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(3), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 30));
            }
        }
        else if (this.boots.contains(this.item.getId())) {
            if (this.books < 4) {
                this.setItem(13, this.prot1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(1), 15));
            }
            else if (this.books >= 4 && this.books < 8) {
                this.setItem(13, this.prot1);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(1), 20));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 20));
            }
            else if (this.books >= 8 && this.books < 12) {
                this.setItem(13, this.prot2);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(2), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 30));
            }
            else if (this.books >= 12 && this.books < 16) {
                this.setItem(13, this.prot2);
                this.setItem(14, this.unb1);
                this.setItem(14, this.featherfall1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(2), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 30));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(2).setLevel(1), 15));
            }
            else if (this.books >= 16) {
                this.setItem(13, this.prot3);
                this.setItem(14, this.unb2);
                this.setItem(15, this.featherfall2);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(0).setLevel(3), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 30));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(2).setLevel(2), 20));
            }
        }
        else if (this.item.getId() == 261) {
            if (this.books < 4) {
                this.setItem(13, this.power1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(19).setLevel(1), 10));
            }
            else if (this.books >= 4 && this.books < 8) {
                this.setItem(13, this.power1);
                this.setItem(14, this.unb1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(19).setLevel(2), 20));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(1), 10));
            }
            else if (this.books >= 8 && this.books < 12) {
                this.setItem(13, this.power2);
                this.setItem(14, this.unb2);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(19).setLevel(2), 30));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 20));
            }
            else if (this.books >= 12 && this.books < 16) {
                this.setItem(13, this.power3);
                this.setItem(14, this.unb2);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(19).setLevel(3), 40));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(2), 20));
            }
            else if (this.books >= 16) {
                this.setItem(13, this.power3);
                this.setItem(14, this.unb3);
                this.setItem(15, this.plomien1);
                this.enchants.put(13, new EnchantAction(Enchantment.getEnchantment(19).setLevel(3), 50));
                this.enchants.put(14, new EnchantAction(Enchantment.getEnchantment(17).setLevel(3), 40));
                this.enchants.put(15, new EnchantAction(Enchantment.getEnchantment(21).setLevel(1), 30));
            }
        }
        this.setItem(17, EnchantInventory.bar);
        this.setItem(18, EnchantInventory.bar);
        this.setItem(19, EnchantInventory.bar);
        this.setItem(20, EnchantInventory.bar);
        this.setItem(21, EnchantInventory.bar);
        this.setItem(22, EnchantInventory.bar);
        this.setItem(23, EnchantInventory.bar);
        this.setItem(24, EnchantInventory.bar);
        this.setItem(25, EnchantInventory.bar);
        this.setItem(26, EnchantInventory.bar);
        ((Player)this.holder).addWindow((Inventory)this);
    }
    
    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        if (e.getPlayer() == this.getHolder() && e.getItem() != null && e.getItem().equals((Object)this.item)) {
            e.setCancelled(true);
        }
    }
    
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        final EnchantAction s = this.enchants.get(e.getAction().getSlot());
        if (s != null) {
            final Item back = s.execute(e.getPlayer(), this.item.clone());
            int index = -1;
            for (final Map.Entry<Integer, Item> cont : e.getPlayer().getInventory().getContents().entrySet()) {
                if (cont.getValue().equals((Object)this.item)) {
                    index = cont.getKey();
                    break;
                }
            }
            if (index != -1) {
                e.getPlayer().getInventory().setItem(index, back);
                this.item = back;
            }
            this.setItem(10, back);
        }
    }
    
    public void onClose(final Player who) {
        HandlerList.unregisterAll((Listener)this);
        super.onClose(who);
    }
    
    static {
        EnchantInventory.bar = new ItemBuilder(101).setTitle(" ").build();
    }
}
