package xyz.rokkiitt.sector.objects.guild.collection;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;
import cn.nukkit.item.*;
import cn.nukkit.command.*;

import java.util.*;

public class CollectionInventory extends DoubleChestFakeInventory
{
    private final Player holder;
    private final Guild guild;
    private int type;
    private final User u;
    private final HashMap<Integer, List<CollectionData>> pages;
    private int selectedPage;
    
    public CollectionInventory(final Player p, final User u, final Guild g) {
        super(null, Util.fixColor("&6Zbiorki gildii"));
        this.pages = new HashMap<Integer, List<CollectionData>>();
        this.selectedPage = 0;
        this.guild = g;
        this.holder = p;
        this.u = u;
        this.setGui(this.type = 0);
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled();
        final int slot = e.getAction().getSlot();
        final Item target = e.getAction().getSourceItem();
        if (!target.isNull()) {
            if (this.type == 0) {
                final String x = Util.getNBTTagValue(target, "id");
                final String item = Util.getNBTTagValue(target, "item");
                if (!x.isEmpty() && !item.isEmpty()) {
                    this.type = Integer.valueOf(x);
                    this.setGui(Integer.valueOf(item));
                }
            }
            else if (this.type > 0) {
                if (slot == 49) {
                    this.setGui(this.type = 0);
                }
                else if (slot == 11) {
                    final String x = Util.getNBTTagValue(target, "item");
                    if (!x.isEmpty()) {
                        final int item2 = Integer.valueOf(x);
                        final Item v = Item.get(item2);
                        v.setCount(v.getMaxStackSize());
                        if (!v.isNull()) {
                            final int amount = Util.getItemTypeCount(e.getPlayer(), item2);
                            if (amount >= v.getCount()) {
                                final Collection collection = this.guild.getCollection(this.type);
                                if (collection != null) {
                                    Util.removeItemById(e.getPlayer(), v);
                                    collection.addAmount(e.getPlayer().getName(), v.getCount());
                                    Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("payin").replace("{WHAT}", v.getName()).replace("{COUNT}", "" + v.getCount()));
                                    this.setGui(item2);
                                }
                            }
                        }
                    }
                }
                else if (slot == 15) {
                    if (this.u.hasPermission("25")) {
                        final String x = Util.getNBTTagValue(target, "item");
                        if (!x.isEmpty()) {
                            final Collection collection2 = this.guild.getCollection(this.type);
                            if (collection2 != null) {
                                final int item3 = Integer.valueOf(x);
                                final Item v2 = Item.get(item3);
                                if (collection2.toReceive > 0 && !v2.isNull()) {
                                    if (collection2.toReceive >= v2.getMaxStackSize()) {
                                        v2.setCount(v2.getMaxStackSize());
                                    }
                                    else {
                                        v2.setCount(collection2.toReceive);
                                    }
                                    Util.giveItem(e.getPlayer(), v2);
                                    collection2.removeAmount(e.getPlayer().getName(), v2.getCount());
                                    Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("payout").replace("{COUNT}", "" + v2.getCount()).replace("{WHAT}", "" + v2.getName()));
                                    this.setGui(item3);
                                }
                            }
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("guildpermission").replace("{TYPE}", "wyplacania ze skladki"));
                    }
                }
                else if (slot == 48) {
                    final int page = this.selectedPage;
                    final int n = this.selectedPage - 1;
                    this.selectedPage = n;
                    this.selectedPage = this.validatePage(n);
                    if (page != this.selectedPage) {
                        this.openPage(this.selectedPage);
                    }
                }
                else if (slot == 50) {
                    final int page = this.selectedPage;
                    this.selectedPage = this.validatePage(++this.selectedPage);
                    if (page != this.selectedPage) {
                        this.openPage(this.selectedPage);
                    }
                }
            }
        }
    }
    
    private void setGui(final int itemId) {
        this.clearAll();
        this.setServerGui();
        if (this.type == 0) {
            final Collection collection1 = this.guild.getCollection(1);
            final Collection collection2 = this.guild.getCollection(2);
            final Collection collection3 = this.guild.getCollection(3);
            final Collection collection4 = this.guild.getCollection(4);
            final Collection collection5 = this.guild.getCollection(5);
            final Collection collection6 = this.guild.getCollection(6);
            final Collection collection7 = this.guild.getCollection(7);
            final Collection collection8 = this.guild.getCollection(8);
            final Collection collection9 = this.guild.getCollection(9);
            if (collection1 != null) {
                this.setItem(11, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(266).setCustomName(Util.fixColor("&r&6Status zlota")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection1.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection1.type), "item", "266"));
            }
            if (collection2 != null) {
                this.setItem(12, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(264).setCustomName(Util.fixColor("&r&6Status diamentow")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection2.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection2.type), "item", "264"));
            }
            if (collection3 != null) {
                this.setItem(14, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(388).setCustomName(Util.fixColor("&r&6Status emeraldow")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection3.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection3.type), "item", "388"));
            }
            if (collection4 != null) {
                this.setItem(15, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(265).setCustomName(Util.fixColor("&r&6Status zelaza")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection4.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection4.type), "item", "265"));
            }
            if (collection5 != null) {
                this.setItem(30, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(12).setCustomName(Util.fixColor("&r&6Status piasku")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection5.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection5.type), "item", "12"));
            }
            if (collection6 != null) {
                this.setItem(31, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(46).setCustomName(Util.fixColor("&r&6Status tnt")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection6.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection6.type), "item", "46"));
            }
            if (collection7 != null) {
                this.setItem(32, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(289).setCustomName(Util.fixColor("&r&6Status prochu")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection7.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection7.type), "item", "289"));
            }
            if (collection8 != null) {
                this.setItem(38, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(49).setCustomName(Util.fixColor("&r&6Status obsydianu")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection8.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection8.type), "item", "49"));
            }
            if (collection9 != null) {
                this.setItem(42, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Item.get(1).setCustomName(Util.fixColor("&r&6Status kamienia")).setLore(new String[] { "", Util.fixColor("&r&fObecna ilosc do wyplaty: &e" + collection9.toReceive), "", Util.fixColor("&r&fKliknij aby otworzyc menu") }), "id", "" + collection9.type), "item", "1"));
            }
        }
        else if (this.type > 0) {
            final Collection collection10 = this.guild.getCollection(this.type);
            if (collection10 != null) {
                final Item what = Item.get(itemId);
                this.setItem(11, Util.addNBTTagWithValue(new ItemBuilder(410).setTitle("&r").setLore(new String[] { "&r&fKliknij aby wplacic &e" + what.getMaxStackSize(), "" }).build(), "item", "" + itemId));
                int amount;
                if (collection10.toReceive >= what.getMaxStackSize()) {
                    amount = what.getMaxStackSize();
                }
                else {
                    amount = collection10.toReceive;
                }
                this.setItem(15, Util.addNBTTagWithValue(new ItemBuilder(410).setTitle("&r").setLore(new String[] { "&r&fKliknij aby wyplacic &e" + amount, "" }).build(), "item", "" + itemId));
                this.setItem(13, new ItemBuilder(itemId).setTitle("&r").setLore(new String[] { "&r&fAktualny stan mozliwy do wplaty &e" + collection10.toReceive, "" }).build());
                this.setItem(47, new ItemBuilder(77).setTitle((this.selectedPage == 0) ? "&r&cBrak poprzedniej strony" : (" &r&cPoprzednia strona (" + this.selectedPage + ")")).build());
                this.setItem(51, new ItemBuilder(77).setTitle((this.selectedPage >= this.pages.size() - 1) ? "&r&cBrak nastepnej strony" : (" &r&aNastepna strona (" + (this.selectedPage + 2) + ")")).build());
                final int[] array;
                final int[] black = array = new int[] { 10, 12, 14, 16, 19, 20, 21, 22, 23, 24, 25 };
                for (final int i : array) {
                    this.setItem(i, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
                }
                this.setItem(49, Item.get(-161).setCustomName(Util.fixColor("&r&cPowrot")));
                this.calculatePages(collection10);
                this.openPage(this.selectedPage = 0);
            }
            else {
                this.setGui(this.type = 0);
            }
        }
    }
    
    private void calculatePages(final Collection collection) {
        this.pages.clear();
        int i = 0;
        for (final CollectionData bazaritem : collection.participants) {
            if (!this.pages.containsKey(i)) {
                final List<CollectionData> list = new ArrayList<CollectionData>();
                this.pages.put(i, list);
            }
            if (this.pages.get(i).size() < 14) {
                final List<CollectionData> list = this.pages.get(i);
                list.add(bazaritem);
                this.pages.replace(i, list);
            }
            else {
                ++i;
                final List<CollectionData> newList = new ArrayList<CollectionData>();
                newList.add(bazaritem);
                this.pages.put(i, newList);
            }
        }
    }
    
    private void openPage(final int pagee) {
        int page = pagee;
        if (this.pages.isEmpty()) {
            return;
        }
        int lastPage = 0;
        for (final Map.Entry<Integer, List<CollectionData>> entry : this.pages.entrySet()) {
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
        this.pages.size();
        for (final CollectionData log : this.pages.get(page)) {
            this.addOneItem(new ItemBuilder(389).setTitle("&r&9" + log.who).setLore(new String[] { "", "&r&fWplacil: &e" + log.in, "&r&fWyplacil: &e" + log.out, "" }).build());
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
}
