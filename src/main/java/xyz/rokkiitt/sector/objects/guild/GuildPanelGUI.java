package xyz.rokkiitt.sector.objects.guild;

import cn.nukkit.utils.DyeColor;
import com.jsoniter.JsonIterator;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.packets.guild.PacketGuildPanelRequest;
import xyz.rokkiitt.sector.packets.guild.PacketGuildPanelUpdate;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;
import java.util.regex.*;
import java.util.*;

public class GuildPanelGUI extends DoubleChestFakeInventory
{
    private int selectedPage;
    private Player holder;
    private final HashMap<Integer, List<String>> pages;
    private final List<String> memb;
    private String tag;
    private boolean isPerms;
    private String receiver;
    private List<String> perms;
    public static List<String> allperms;
    
    public GuildPanelGUI(final Player p, final String ta, final String members) {
        super(null, Util.fixColor("&6Panel"));
        this.selectedPage = 0;
        this.pages = new HashMap<Integer, List<String>>();
        this.tag = ta;
        this.memb = JsonIterator.deserialize(members, List.class);
        this.holder = p;
        this.isPerms = false;
        this.calculatePages();
        this.openPage(this.selectedPage);
        Main.panels.add(this);
        this.holder.addWindow((Inventory)this);
    }
    
    public void onRequest(final PacketGuildPanelRequest e) {
        if (this.holder.getName().equalsIgnoreCase(e.sender)) {
            this.receiver = e.receiver;
            this.perms = new ArrayList<String>(Arrays.asList(e.receiverpermissions.split(Pattern.quote("|&|"))));
            this.isPerms = true;
            this.setPerms();
        }
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        if (this.isPerms) {
            final int slot = e.getAction().getSlot();
            if (this.getItem(slot).getId() == 102 || this.getItem(slot).getId() == 0) {
                return;
            }
            if (slot == 47) {
                this.changePermission(false);
                this.setPerms();
                return;
            }
            if (slot == 51) {
                this.changePermission(true);
                this.setPerms();
                return;
            }
            if (slot == 49) {
                this.selectedPage = 0;
                this.isPerms = false;
                this.calculatePages();
                this.openPage(this.selectedPage);
                return;
            }
            this.changePermission(String.valueOf(Util.getPanelInventory(slot)));
            this.setPerms();
        }
        else {
            int slot = e.getAction().getSlot();
            if (this.holder == null) {
                return;
            }
            final Player p = e.getPlayer();
            if (slot == 47) {
                if (this.getItem(47) == null || this.getItem(47).getId() == 102) {
                    return;
                }
                this.openPage(--this.selectedPage);
            }
            else if (slot == 51) {
                if (this.getItem(51) == null || this.getItem(51).getId() == 102) {
                    return;
                }
                this.openPage(++this.selectedPage);
            }
            else {
                if (this.getItem(slot) == null) {
                    return;
                }
                if (this.getItem(slot).getId() != 397) {
                    return;
                }
                if (this.pages.size() == 0) {
                    return;
                }
                slot = Util.getPanelInventory(slot);
                if (slot > this.pages.get(this.selectedPage).size() - 1) {
                    return;
                }
                final String p2 = this.pages.get(this.selectedPage).get(slot);
                if (p2.equalsIgnoreCase(p.getName())) {
                    return;
                }
                final PacketGuildPanelRequest pa = new PacketGuildPanelRequest();
                pa.tag = this.tag;
                pa.sender = p.getName();
//                pa.sendersector = Config.getInstance().Sector;
                pa.receiver = p2;
                pa.receiverpermissions = " ";
//                Main.getNats().publish("hubguildpanel", "request||" + JsonStream.serialize(pa));
            }
        }
    }
    
    private void calculatePages() {
        this.clearAll();
        this.pages.clear();
        int i = 0;
        final int max = this.getSize() - 9;
        for (final String b : this.memb) {
            if (!this.pages.containsKey(i)) {
                final List<String> list = new ArrayList<String>();
                this.pages.put(i, list);
            }
            if (this.pages.get(i).size() < max) {
                final List<String> list = this.pages.get(i);
                list.add(b);
                this.pages.replace(i, list);
            }
            else {
                ++i;
                final List<String> newList = new ArrayList<String>();
                newList.add(b);
                this.pages.put(i, newList);
            }
        }
    }
    
    private void openPage(final int pag) {
        this.clearAll();
        int page = pag;
        if (this.pages.isEmpty()) {
            page = 0;
        }
        int lastPage = 0;
        for (final Map.Entry<Integer, List<String>> entry : this.pages.entrySet()) {
            if (entry.getKey() > lastPage) {
                lastPage = entry.getKey();
            }
        }
        if (page > this.pages.size() - 1) {
            page = lastPage;
        }
        if (page < 0) {
            page = 0;
        }
        if (!this.pages.containsKey(page)) {
            page = 0;
        }
        int slot = 0;
        if (this.pages.size() > 0) {
            for (final String p : this.pages.get(page)) {
                if (slot <= 36) {
                    this.setItem(Util.getSlotInventory(slot), new ItemBuilder(397, 1, 3).setTitle("&r&9" + p.toUpperCase()).build());
                }
                ++slot;
            }
        }
        this.selectedPage = page;
        this.createButtons();
    }
    
    private void createButtons() {
        this.setServerGui();
        this.clear(47);
        this.clear(51);
        int lastPage = 0;
        for (final Map.Entry<Integer, List<String>> entry : this.pages.entrySet()) {
            if (entry.getKey() > lastPage) {
                lastPage = entry.getKey();
            }
        }
        if (this.selectedPage != 0 && !this.pages.isEmpty()) {
            this.setItem(47, new ItemBuilder(351, 1, DyeColor.RED.getDyeData()).build());
        }
        if (this.selectedPage != lastPage && !this.pages.isEmpty()) {
            this.setItem(51, new ItemBuilder(351, 1, DyeColor.LIME.getDyeData()).build());
        }
    }
    
    @Override
    public void onClose(final Player who) {
        Main.panels.remove(this);
        super.onClose(who);
    }
    
    public void changePermission(final String s) {
        if (this.perms.contains(s)) {
            this.perms.remove(s);
        }
        else {
            this.perms.add(s);
        }
        final PacketGuildPanelUpdate pa = new PacketGuildPanelUpdate();
        pa.receiver = this.receiver;
        pa.receiversector = "";
        pa.perms = String.join("|&|", this.perms);
        pa.isOnline = false;
//        Main.getNats().publish("hubguildpanel", "update||" + JsonStream.serialize(pa));
    }
    
    public void changePermission(final boolean t) {
        if (t) {
            for (final String s : GuildPanelGUI.allperms) {
                if (!this.perms.contains(s)) {
                    this.perms.add(s);
                }
            }
        }
        else {
            for (final String s : GuildPanelGUI.allperms) {
                this.perms.remove(s);
            }
        }
        final PacketGuildPanelUpdate pa = new PacketGuildPanelUpdate();
        pa.receiver = this.receiver;
        pa.receiversector = "";
        pa.perms = String.join("|&|", this.perms);
        pa.isOnline = false;
//        Main.getNats().publish("hubguildpanel", "update||" + JsonStream.serialize(pa));
    }
    
    public void setPerms() {
        this.clearAll();
        this.setServerGui();
        this.setItem(47, new ItemBuilder(351, 1, DyeColor.RED.getDyeData()).setTitle("&r&cZabierz wszystkie permisjne").build());
        this.setItem(49, new ItemBuilder(-161).setTitle(Util.fixColor("&r&6Cofnij do panelu")).build());
        this.setItem(51, new ItemBuilder(351, 1, DyeColor.LIME.getDyeData()).setTitle("&r&aNadaj wszystkie permisjne").build());
        this.setItem(10, new ItemBuilder(257).setTitle(this.perms.contains("0") ? "&r&r&aNiszczenie blokow" : "&r&r&cNiszczenie blokow").addGlow(this.perms.contains("0")).build(true));
        this.setItem(11, new ItemBuilder(4).setTitle(this.perms.contains("1") ? "&r&r&aStawianie blokow" : "&r&r&cStawianie blokow").addGlow(this.perms.contains("1")).build());
        this.setItem(12, new ItemBuilder(46).setTitle(this.perms.contains("2") ? "&r&r&aStawianie tnt" : "&r&r&cStawianie tnt").addGlow(this.perms.contains("2")).build());
        this.setItem(13, new ItemBuilder(54).setTitle(this.perms.contains("3") ? "&r&r&aOtwieranie skrzynek " : "&r&cOtwieranie skrzynek").addGlow(this.perms.contains("3")).build());
        this.setItem(14, new ItemBuilder(368).setTitle(this.perms.contains("4") ? "&r&aTeleportowanie na teren" : "&r&cTeleportowanie na teren").addGlow(this.perms.contains("4")).build());
        this.setItem(15, new ItemBuilder(325, 1, 8).setTitle(this.perms.contains("5") ? "&r&aWylewanie wody" : "&r&cWylewanie wody").addGlow(this.perms.contains("5")).build());
        this.setItem(16, new ItemBuilder(325, 1, 10).setTitle(this.perms.contains("6") ? "&r&aWylewanie lavy" : "&r&cWylewanie lavy").addGlow(this.perms.contains("6")).build());
        this.setItem(19, new ItemBuilder(77).setTitle(this.perms.contains("7") ? "&r&aInterakcje" : "&r&cInterakcje").addGlow(this.perms.contains("7")).build());
        this.setItem(20, new ItemBuilder(276).setTitle(this.perms.contains("8") ? "&r&aUzywanie friendly fire" : "&r&cUzywanie friendly fire").addGlow(this.perms.contains("8")).build(true));
        this.setItem(21, new ItemBuilder(283).setTitle(this.perms.contains("9") ? "&r&aUzywanie alliance fire" : "&r&cUzywanie alliance fire").addGlow(this.perms.contains("9")).build(true));
        this.setItem(22, new ItemBuilder(301).setTitle(this.perms.contains("10") ? "&r&aWyrzucanie czlonkow" : "&r&cWyrzucanie czlonkow").addGlow(this.perms.contains("10")).build());
        this.setItem(23, new ItemBuilder(35, 1, DyeColor.GREEN.getWoolData()).setTitle(this.perms.contains("11") ? "&r&aZapraszanie czlonkow" : "&r&cZapraszanie czlonkow").addGlow(this.perms.contains("11")).build());
        this.setItem(24, new ItemBuilder(322).setTitle(this.perms.contains("12") ? "&r&aZarzadzanie regeneracja" : "&r&cZarzadzanie regeneracja").addGlow(this.perms.contains("12")).build());
        this.setItem(25, new ItemBuilder(54).setTitle(this.perms.contains("13") ? "&r&aOtwieranie skarbca" : "&r&cOtwieranie skarbca").addGlow(this.perms.contains("13")).build());
        this.setItem(28, new ItemBuilder(152).setTitle(this.perms.contains("14") ? "&r&aNiszczenie blokow redstone" : "&r&cNiszczenie blokow redstone").addGlow(this.perms.contains("14")).build());
        this.setItem(29, new ItemBuilder(138).setTitle(this.perms.contains("15") ? "&r&aNisczenie beacona" : "&r&cNiszczenie beacona").addGlow(this.perms.contains("15")).build());
        this.setItem(30, new ItemBuilder(54).setTitle(this.perms.contains("16") ? "&r&aOtwieranie zabezpieczonych skrzyn" : "&r&cOtwieranie zabezpieczonych skrzyn").addGlow(this.perms.contains("16")).build());
        this.setItem(31, new ItemBuilder(54).setTitle(this.perms.contains("17") ? "&r&aNiszczenie lockow na skrzyniach" : "&r&cNiszczenie zabezpieczonych skrzyn").addGlow(this.perms.contains("17")).build());
        this.setItem(32, new ItemBuilder(120).setTitle(this.perms.contains("18") ? "&r&aStawianie farmerow" : "&r&cStawianie farmerow").addGlow(this.perms.contains("18")).build());
        this.setItem(33, new ItemBuilder(272).setTitle(this.perms.contains("19") ? "&r&aWysylanie pozycji (walka)" : "&r&cWysylanie pozycji (walka)").addGlow(this.perms.contains("19")).build(true));
        this.setItem(34, new ItemBuilder(345).setTitle(this.perms.contains("20") ? "&r&aDostep do panelu z permisjami" : "&r&cDostep do panelu z permisjami").addGlow(this.perms.contains("20")).build());
        this.setItem(37, new ItemBuilder(381).setTitle(this.perms.contains("21") ? "&r&aMozliwosc ustawienia bazy gildii" : "&r&cMozliwosc ustawienia bazy gildii").addGlow(this.perms.contains("21")).build());
        this.setItem(38, new ItemBuilder(260).setTitle(this.perms.contains("22") ? "&r&aMozliwosc zarzadzania sercem gildii" : "&r&cMozliwosc zarzadzania sercem gildii").addGlow(this.perms.contains("22")).build());
        this.setItem(39, new ItemBuilder(267).setTitle(this.perms.contains("23") ? "&r&aMozliwosc nadania lub zerwania sojuszu" : "&r&cMozliwosc nadania lub zerwania sojuszu").addGlow(this.perms.contains("23")).build(true));
        this.setItem(40, new ItemBuilder(389).setTitle(this.perms.contains("24") ? "&r&aMozliwosc sprawdzania logblocka" : "&r&cMozliwosc sprawdzania logblocka").addGlow(this.perms.contains("24")).build());
        this.setItem(41, new ItemBuilder(389).setTitle(this.perms.contains("25") ? "&r&aMozliwosc wyplacania ze skladki" : "&r&cMozliwosc wyplacania ze skladki").addGlow(this.perms.contains("25")).build());
        this.setItem(42, new ItemBuilder(389).setTitle(this.perms.contains("26") ? "&r&aMozliwosc ulepszania boostow" : "&r&cMozliwosc ulepszania boostow").addGlow(this.perms.contains("26")).build());
        this.setItem(43, new ItemBuilder(49).setTitle(this.perms.contains("27") ? "&r&aInterakcja z obsydianem" : "&r&cInterakcja z obsydianem").addGlow(this.perms.contains("27")).build());
    }
    
    static {
        GuildPanelGUI.allperms = new ArrayList<String>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27"));
    }
}
