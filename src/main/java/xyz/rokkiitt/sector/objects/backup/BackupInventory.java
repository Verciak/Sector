package xyz.rokkiitt.sector.objects.backup;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.packets.commands.PacketBackupLoad;
import xyz.rokkiitt.sector.packets.commands.PacketBackupReceive;
import cn.nukkit.inventory.*;
import cn.nukkit.*;
import cn.nukkit.item.*;
import java.util.*;
import xyz.rokkiitt.sector.utils.*;

public class BackupInventory extends DoubleChestFakeInventory
{
    private final HashMap<Integer, List<Backup>> pages;
    private int selectedPage;
    private final Player holder;
    private static final Comparator<Backup> sort;
    private boolean isLoading;
    private boolean isMenu;
    private final List<Backup> backups;
    private final String who;
    private Backup selected;
    
    public BackupInventory(final Player p, final String who) {
        super(null, Util.fixColor("&6Backup: " + who));
        this.pages = new HashMap<Integer, List<Backup>>();
        this.selectedPage = 0;
        this.isLoading = true;
        this.isMenu = true;
        this.backups = new ArrayList<Backup>();
        this.selected = null;
        this.holder = p;
        this.who = who;
        Main.backups.add(this);
        final PacketBackupLoad pa = new PacketBackupLoad();
        pa.player = p.getName();
        pa.who = who;
        pa.items = "brak";
        this.setGui();
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled();
        if (!this.isLoading) {
            final int slot = e.getAction().getSlot();
            if (this.isMenu) {
                if (slot == 47) {
                    final int page = this.selectedPage;
                    final int n = this.selectedPage - 1;
                    this.selectedPage = n;
                    this.selectedPage = this.validatePage(n);
                    if (page != this.selectedPage) {
                        this.openPage(this.selectedPage);
                    }
                }
                else if (slot == 51) {
                    final int page = this.selectedPage;
                    this.selectedPage = this.validatePage(++this.selectedPage);
                    if (page != this.selectedPage) {
                        this.openPage(this.selectedPage);
                    }
                }
                else {
                    final String id = Util.getNBTTagValue(e.getAction().getSourceItem(), "id");
                    if (!id.isEmpty()) {
                        for (final Backup ac : this.backups) {
                            if (String.valueOf(ac.id).equalsIgnoreCase(id)) {
                                this.selected = ac;
                                this.isMenu = false;
                                this.setRedem();
                            }
                        }
                    }
                }
            }
            else if (this.selected != null) {
                if (slot == 50) {
                    this.isMenu = true;
                    this.selected = null;
                    this.calculatePages();
                    this.openPage(this.validatePage(this.selectedPage));
                }
                else if (slot == 51) {
                    this.backups.remove(this.selected);
                    this.selected = null;
                    this.isMenu = true;
                    this.calculatePages();
                    this.openPage(this.validatePage(this.selectedPage));
                }
                else if (slot == 52) {
                    this.holder.getInventory().setContents((Map)this.selected.inventory);
                }
                else if (slot == 53) {
                    final PacketBackupReceive pa = new PacketBackupReceive();
                    pa.who = this.who;
                    pa.whosector = "";
                    pa.points = this.selected.points;
                }
            }
            else {
                this.isMenu = true;
                this.calculatePages();
                this.openPage(this.validatePage(this.selectedPage));
            }
        }
    }
    
//    public void loadRequest(final PacketBackupLoad logs, final Player who) {
//        if (who.equals((Object)this.holder)) {
//            final String w = Util.StringFromString(logs.items);
//            if (w != null && !w.equalsIgnoreCase("brak")) {
//                for (final String s : item) {
//                    final String[] split = s.split(Pattern.quote("||"));
//                    if (split.length >= 6) {
//                        final Backup is = new Backup();
//                        is.id = Integer.valueOf(split[0]);
//                        is.player = split[1];
//                        is.killer = split[2];
//                        is.date = Long.valueOf(split[3]);
//                        is.points = Integer.valueOf(split[4]);
//                        is.inventory = ItemSerializer.getItemMapFromString(split[5]);
//                        this.backups.add(is);
//                    }
//                }
//            }
//            this.isLoading = false;
//            final Object selectedPage;
//            Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
//                this.calculatePages();
//                this.openPage(this.selectedPage = (int)selectedPage);
//            }, 5);
//        }
//    }
    
    private void setRedem() {
        if (!this.isMenu) {
            if (this.selected != null) {
                this.clearAll();
                for (final Item c : this.selected.inventory.values()) {
                    this.addOneItem(c);
                }
                for (final int x : Arrays.asList(41, 42, 43, 44, 45, 46, 47, 48, 49)) {
                    this.setItem(x, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
                }
                this.setItem(50, Item.get(-161).setCustomName(Util.fixColor("&r&cCofnij")));
                this.setItem(51, DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r&cUsun")));
                this.setItem(52, DyeColor.get(DyeColor.BLUE).setCustomName(Util.fixColor("&r&aOddaj dla siebie")));
                this.setItem(53, DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&r&aOddaj dla gracza")));
            }
            else {
                this.isMenu = true;
                this.calculatePages();
                this.openPage(this.validatePage(this.selectedPage));
            }
        }
        else {
            this.calculatePages();
            this.openPage(this.validatePage(this.selectedPage));
        }
    }
    
    private void setGui() {
        this.clearAll();
        this.setServerGui();
        for (int i = 0; i < 28 && this.isLoading; ++i) {
            this.addOneItem(new ItemBuilder(-161).setTitle("&r\u270b").setLore(new String[] { "&r&l&cTrwa ladowanie backupow!", "" }).build());
        }
    }
    
    @Override
    public void onClose(final Player who) {
        Main.backups.remove(this);
        super.onClose(who);
    }
    
    private void calculatePages() {
        this.pages.clear();
        int i = 0;
        final List<Backup> ba = new ArrayList<Backup>(this.backups);
        ba.sort(BackupInventory.sort);
        for (final Backup bazaritem : ba) {
            if (!this.pages.containsKey(i)) {
                final List<Backup> list = new ArrayList<Backup>();
                this.pages.put(i, list);
            }
            if (this.pages.get(i).size() < 28) {
                final List<Backup> list = this.pages.get(i);
                list.add(bazaritem);
                this.pages.replace(i, list);
            }
            else {
                ++i;
                final List<Backup> newList = new ArrayList<Backup>();
                newList.add(bazaritem);
                this.pages.put(i, newList);
            }
        }
    }
    
    private void openPage(final int pagee) {
        this.clearAll();
        this.setServerGui();
        int page = pagee;
        if (this.pages.isEmpty()) {
            return;
        }
        int lastPage = 0;
        for (final Map.Entry<Integer, List<Backup>> entry : this.pages.entrySet()) {
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
        this.setItem(47, new ItemBuilder(77).setTitle((this.selectedPage == 0) ? "&r&cBrak poprzedniej strony" : (" &r&cPoprzednia strona (" + this.selectedPage + ")")).build());
        this.setItem(51, new ItemBuilder(77).setTitle((this.selectedPage >= this.pages.size() - 1) ? "&r&cBrak nastepnej strony" : (" &r&aNastepna strona (" + (this.selectedPage + 2) + ")")).build());
        if (this.pages.size() > 0) {
            for (final Backup log : this.pages.get(page)) {
                this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(389).setTitle("&r&6&lBackup: " + log.id).setLore(new String[] { "&r&6Zabojca: &e" + (log.killer.equalsIgnoreCase("brak") ? "Brak" : log.killer), "&r&6Punkty: &e" + log.points, "&r&6Data: &e" + DateUtil.formatDate(log.date), "&r&6Usuwa sie za: &e" + Util.formatTime(log.date - System.currentTimeMillis()) }).build(), "id", "" + log.id));
            }
        }
        this.selectedPage = page;
    }
    
    private int validatePage(int page) {
        final int lastPage = this.pages.size() - 1;
        if (page > lastPage) {
            page = lastPage;
        }
        if (this.pages.size() == 0 || page < 0 || this.pages.get(page) == null) {
            page = 0;
        }
        return this.selectedPage = page;
    }
    
    static {
        sort = ((o1, o2) -> Long.compare(o1.date, o2.date));
    }
}
