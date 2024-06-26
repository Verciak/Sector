package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.item.enchantment.*;

public class PItemsGUI extends ChestFakeInventory
{
    public static Item antynogi;
    public static Item stoniarka;
    public static Item cx;
    public static Item rzucane;
    public static Item boy;
    public static Item sand;
    public static Item kopacz;
    public static Item vip;
    public static Item svip;
    public static Item sponsor;
    public static Item turbo10;
    public static Item turbo30;
    public static Item turbo60;
    public static Item kilof6;
    public static Item pandora;
    public static Item premiumcase;

    public PItemsGUI() {
        super(null, Util.fixColor("&6Premium Itemy"));
        this.setItem(0, PItemsGUI.stoniarka);
        this.setItem(1, PItemsGUI.antynogi);
        this.setItem(2, PItemsGUI.cx);
        this.setItem(3, PItemsGUI.rzucane);
        this.setItem(4, PItemsGUI.boy);
        this.setItem(5, PItemsGUI.sand);
        this.setItem(6, PItemsGUI.kopacz);
        this.setItem(7, PItemsGUI.vip);
        this.setItem(8, PItemsGUI.svip);
        this.setItem(9, PItemsGUI.sponsor);
        this.setItem(10, PItemsGUI.turbo10);
        this.setItem(11, PItemsGUI.turbo30);
        this.setItem(12, PItemsGUI.turbo60);
        this.setItem(13, PItemsGUI.kilof6);
        this.setItem(14, PItemsGUI.pandora);
        this.setItem(15, premiumcase);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        if (e.getAction().getSourceItem() != null) {
            Util.giveItem(e.getPlayer(), e.getAction().getSourceItem());
        }
    }
    
    static {
        PItemsGUI.premiumcase = Util.setupNBTItem(Item.CHEST, 64, 0, "premiumcase", true, "&r&6PremiumCase", new String[] { "",  "&r&7Aby sprawdzic drop z premiumcase wpisz &e/drop", "\u270b" });
        PItemsGUI.antynogi = Util.setupNBTItem(373, 1, 0, "antynogi", true, "&r&6Anty-nogi", new String[] { "", "&r>>&7 Wypij aby teleportowac sie do najblizszego gracza", "&r>>&7 Przedmiot dziala tylko jesli gracz uderzyl cie podczas walki", "&r>>&7 oraz jesli znajduje sie w promieniu 5 kratek!", "\u270b" });
        PItemsGUI.stoniarka = Util.setupNBTItem(121, 1, 0, "stoniarka", true, "&r&6Stoniarka", new String[] { "", "&r>>&7 Postaw, aby &eutworzyc stoniarke", "&r>>&7 Po postawieniu utworzy sie kamien", "&r>>&7 Aby go zniszczyc uzyj &eZlotego Kilofu", "\u270b" });
        PItemsGUI.cx = Util.setupNBTItem(48, 1, 0, "cx", true, "&r&6CobbleX", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby otworzyc", "\u270b" });
        PItemsGUI.rzucane = Util.setupNBTItem(46, 1, 0, "rzucanetnt", true, "&r&6Rzucane TNT", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby rzucic", "\u270b" });
        PItemsGUI.boy = Util.setupNBTItem(159, 1, 11, "boyfarmer", true, "&r&3Boy&7Farmer", new String[] { "", "&r>>&7 Postaw, aby &estworzyc sciane z obsydianu", "&r>>&7 Tworzy sie ona automatycznie do bedrocka", "&r>>&7 Mozliwosc postawienia tylko pozniej &e70 kratki", "&r>>&7 Oraz tylko na terenie &eswojej gildi", "\u270b" });
        PItemsGUI.sand = Util.setupNBTItem(159, 1, 4, "sandfarmer", true, "&r&6Sand&7Farmer", new String[] { "", "&r>>&7 Postaw, aby &estworzyc sciane z pisaku", "&r>>&7 Tworzy sie ona automatycznie do bedrocka", "&r>>&7 Mozliwosc postawienia tylko pozniej &e70 kratki", "&r>>&7 Oraz tylko na terenie &eswojej gildi", "\u270b" });
        PItemsGUI.kopacz = Util.setupNBTItem(159, 1, 9, "kopacz", true, "&r&fKopacz&7Fosy", new String[] { "", "&r>>&7 Postaw, aby &eusunac sciane blokow w lini pionowej", "&r>>&7 Tworzy sie ona automatycznie do bedrocka", "&r>>&7 Mozliwosc postawienia tylko pozniej &e70 kratki", "&r>>&7 Oraz tylko na terenie &eswojej gildi", "\u270b" });
        PItemsGUI.vip = Util.setupNBTItem(340, 1, 0, "vouchervip", true, "&r&6Voucher VIP", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby aktywowac range &eVIP", "\u270b" });
        PItemsGUI.svip = Util.setupNBTItem(340, 1, 0, "vouchersvip", true, "&r&6Voucher SVIP", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby aktywowac range &eSVIP", "\u270b" });
        PItemsGUI.sponsor = Util.setupNBTItem(340, 1, 0, "vouchersponsor", true, "&r&6Voucher SPONSOR", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby aktywowac range &eSPONSOR", "\u270b" });
        PItemsGUI.turbo10 = Util.setupNBTItem(340, 1, 0, "voucherturbo10", true, "&r&6Voucher Turbodrop 10 minut", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby aktywowac turbodrop na &e10 minut", "\u270b" });
        PItemsGUI.turbo30 = Util.setupNBTItem(340, 1, 0, "voucherturbo30", true, "&r&6Voucher Turbodrop 30 minut", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby aktywowac turbodrop na &e30 minut", "\u270b" });
        PItemsGUI.turbo60 = Util.setupNBTItem(340, 1, 0, "voucherturbo60", true, "&r&6Voucher Turbodrop 60 minut", new String[] { "", "&r>>&7 Kliknij &ePPM &7aby aktywowac turbodrop na &e60 minut", "\u270b" });
        PItemsGUI.kilof6 = new ItemBuilder(278).setTitle("&r&cKilof 6/3/3").addEnchantment(Enchantment.getEnchantment(15), 6).addEnchantment(Enchantment.getEnchantment(17), 3).addEnchantment(Enchantment.getEnchantment(18), 3).build();
        PItemsGUI.pandora = Util.setupNBTItem(122, 1, 0, "pandora", true, "&r&6Pandora", new String[] { "", "&r&7Aby sprawdzic drop z pandory wpisz &e/drop", "\u270b" });
    }
}
