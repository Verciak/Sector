package xyz.rokkiitt.sector.objects.adminpanel;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.item.*;
import cn.nukkit.utils.Config;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.Util;

import java.util.Map;

public class AdminPanelInventory extends DoubleChestFakeInventory {

    private final Player who;

    public AdminPanelInventory(final Player p) {
        super(null, Util.fixColor("&6ADMIN PANEL"));
        this.who = p;
        this.refreshGui();
        p.addWindow(this);
    }

    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        int slot = e.getAction().getSlot();
        Player p = e.getPlayer();
        final Config c = Main.getPlugin().getConfig();
        e.setCancelled(true);
        if(slot == 0){
            if(c.getBoolean("enable_guild") == true){
                c.set("enable_guild", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("enable_guild", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 1){
            if(c.getBoolean("enable_tnt") == true){
                c.set("enable_tnt", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("enable_tnt", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 2){
            if(c.getBoolean("ENABLE_ENCHANT") == true){
                c.set("ENABLE_ENCHANT", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_ENCHANT", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 3){
            if(c.getBoolean("ENABLE_PANDORA") == true){
                c.set("ENABLE_PANDORA", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_PANDORA", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 4){
            if(c.getBoolean("ENABLE_VOUCHER") == true){
                c.set("ENABLE_VOUCHER", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_VOUCHER", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 5){
            if(c.getBoolean("ENABLE_DIAX") == true){
                c.set("ENABLE_DIAX", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_DIAX", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 6){
            if(c.getBoolean("ENABLE_KIT") == true){
                c.set("ENABLE_KIT", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_KIT", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 7){
            if(c.getBoolean("ENABLE_STRENGHT1") == true){
                c.set("ENABLE_STRENGHT1", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_STRENGHT1", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 8){
            if(c.getBoolean("ENABLE_STRENGHT2") == true){
                c.set("ENABLE_STRENGHT2", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_STRENGHT2", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 9){
            if(c.getBoolean("ENABLE_SPEED1") == true){
                c.set("ENABLE_SPEED1", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_SPEED1", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 10){
            if(c.getBoolean("ENABLE_SPEED2") == true){
                c.set("ENABLE_SPEED2", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_SPEED2", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 11){
            if(c.getBoolean("ENABLE_METEOR") == true){
                c.set("ENABLE_METEOR", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_METEOR", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 12){
            if(c.getBoolean("ENABLE_GITEMS") == true){
                c.set("ENABLE_GITEMS", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_GITEMS", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 13){
            if(c.getBoolean("ENABLE_ITEMSHOP") == true){
                c.set("ENABLE_ITEMSHOP", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_ITEMSHOP", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
        if(slot == 14){
            if(c.getBoolean("ENABLE_ITEMSHOP_ONLYRANKS") == true){
                c.set("ENABLE_ITEMSHOP_ONLYRANKS", false);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }else{
                c.set("ENABLE_ITEMSHOP_ONLYRANKS", true);
                c.save();
                Settings.set();
                new AdminPanelInventory(p);
            }
        }
    }

    private void refreshGui() {
        final Config c = Main.getPlugin().getConfig();
        this.clearAll();
        Item guild = new Item(ItemID.PAPER, 0, 1);
        guild.setCustomName(Util.fixColor("&r&6Gildie"));
        guild.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("enable_guild") ? "&awlaczone" : "&cwylaczone")}));

        Item tnt = new Item(ItemID.PAPER, 0, 1);
        tnt.setCustomName(Util.fixColor("&r&6TNT"));
        tnt.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("enable_tnt") ? "&awlaczone" : "&cwylaczone")}));

        Item ENCHANT = new Item(ItemID.PAPER, 0, 1);
        ENCHANT.setCustomName(Util.fixColor("&r&6ENCHANT"));
        ENCHANT.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_ENCHANT") ? "&awlaczone" : "&cwylaczone")}));

        Item PANDORA = new Item(ItemID.PAPER, 0, 1);
        PANDORA.setCustomName(Util.fixColor("&r&6PANDORA"));
        PANDORA.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_PANDORA") ? "&awlaczone" : "&cwylaczone")}));

        Item VOUCHER = new Item(ItemID.PAPER, 0, 1);
        VOUCHER.setCustomName(Util.fixColor("&r&6VOUCHER"));
        VOUCHER.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_VOUCHER") ? "&awlaczone" : "&cwylaczone")}));

        Item DIAX = new Item(ItemID.PAPER, 0, 1);
        DIAX.setCustomName(Util.fixColor("&r&6DIAX"));
        DIAX.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_DIAX") ? "&awlaczone" : "&cwylaczone")}));

        Item KIT = new Item(ItemID.PAPER, 0, 1);
        KIT.setCustomName(Util.fixColor("&r&6KIT"));
        KIT.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_KIT") ? "&awlaczone" : "&cwylaczone")}));

        Item STRENGHT1 = new Item(ItemID.PAPER, 0, 1);
        STRENGHT1.setCustomName(Util.fixColor("&r&6STRENGHT1"));
        STRENGHT1.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_STRENGHT1") ? "&awlaczone" : "&cwylaczone")}));

        Item STRENGHT2 = new Item(ItemID.PAPER, 0, 1);
        STRENGHT2.setCustomName(Util.fixColor("&r&6STRENGHT2"));
        STRENGHT2.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_STRENGHT2") ? "&awlaczone" : "&cwylaczone")}));

        Item SPEED1 = new Item(ItemID.PAPER, 0, 1);
        SPEED1.setCustomName(Util.fixColor("&r&6SPEED1"));
        SPEED1.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_SPEED1") ? "&awlaczone" : "&cwylaczone")}));

        Item SPEED2 = new Item(ItemID.PAPER, 0, 1);
        SPEED2.setCustomName(Util.fixColor("&r&6SPEED2"));
        SPEED2.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_SPEED2") ? "&awlaczone" : "&cwylaczone")}));

        Item METEOR = new Item(ItemID.PAPER, 0, 1);
        METEOR.setCustomName(Util.fixColor("&r&6METEOR"));
        METEOR.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_METEOR") ? "&awlaczone" : "&cwylaczone")}));

        Item GITEMS = new Item(ItemID.PAPER, 0, 1);
        GITEMS.setCustomName(Util.fixColor("&r&6GITEMS"));
        GITEMS.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_GITEMS") ? "&awlaczone" : "&cwylaczone")}));

        Item ITEMSHOP = new Item(ItemID.PAPER, 0, 1);
        ITEMSHOP.setCustomName(Util.fixColor("&r&6ITEMSHOP"));
        ITEMSHOP.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_ITEMSHOP") ? "&awlaczone" : "&cwylaczone")}));

        Item ITEMSHOP_ONLYRANKS = new Item(ItemID.PAPER, 0, 1);
        ITEMSHOP_ONLYRANKS.setCustomName(Util.fixColor("&r&6ITEMSHOP_ONLYRANKS"));
        ITEMSHOP_ONLYRANKS.setLore(Util.fixColor(new String[]{"&r&8>> &7Status: " + (c.getBoolean("ENABLE_ITEMSHOP_ONLYRANKS") ? "&awlaczone" : "&cwylaczone")}));


        this.setItem(0, guild);
        this.setItem(1, tnt);
        this.setItem(2, ENCHANT);
        this.setItem(3, PANDORA);
        this.setItem(4, VOUCHER);
        this.setItem(5, DIAX);
        this.setItem(6, KIT);
        this.setItem(7, STRENGHT1);
        this.setItem(8, STRENGHT2);
        this.setItem(9, SPEED1);
        this.setItem(10, SPEED2);
        this.setItem(11, METEOR);
        this.setItem(12, GITEMS);
        this.setItem(13, ITEMSHOP);
        this.setItem(14, ITEMSHOP_ONLYRANKS);
    }
}
