package xyz.rokkiitt.sector.objects.itemshop;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.packets.PacketItemshopLoad;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.inventory.*;
import cn.nukkit.command.*;
import cn.nukkit.*;

import java.util.*;

public class ItemshopInventory extends DoubleChestFakeInventory
{
    private final HashMap<Integer, List<ItemShop>> pages;
    private int selectedPage;
    private final Player holder;
    private final User u;
    private static final Comparator<ItemShop> sort;
    private boolean isLoading;
    private final List<ItemShop> itemShops;
    
    public ItemshopInventory(final Player p, final User u) {
        super(null, Util.fixColor("&6Itemshop"));
        this.pages = new HashMap<Integer, List<ItemShop>>();
        this.selectedPage = 0;
        this.isLoading = true;
        this.itemShops = new ArrayList<ItemShop>();
        this.holder = p;
        this.u = u;
        Main.items.add(this);
        final PacketItemshopLoad pa = new PacketItemshopLoad();
        pa.player = p.getName();
        pa.items = "brak";
//        Main.getNats().publish("hubguildpanel", "itemshop||" + JsonStream.serialize(pa));
        this.setGui();
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled();
        if (!this.isLoading) {
            final int slot = e.getAction().getSlot();
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
                final String id = Util.getNBTTagValue(e.getAction().getSourceItem(), "idis");
                if (!id.isEmpty()) {
                    for (final ItemShop ac : this.itemShops) {
                        if (String.valueOf(ac.id).equalsIgnoreCase(id)) {
                            if (Settings.ENABLE_ITEMSHOP_ONLYRANKS && ac.type > 2) {
                                Util.sendMessage((CommandSender)this.holder, Settings.getMessage("itemshoponlyranks"));
                                return;
                            }
                            this.itemShops.remove(ac);
//                            Main.getNats().publish("hubguildpanel", "itemshopdelete||" + ac.id);
                            switch (ac.type) {
                                case 0: {
                                    this.u.setRank("vip");
                                    this.u.save();
                                    break;
                                }
                                case 1: {
                                    this.u.setRank("svip");
                                    this.u.save();
                                    break;
                                }
                                case 2: {
                                    this.u.setRank("sponsor");
                                    this.u.save();
                                    break;
                                }
                                case 5: {
                                    final User u = this.u;
                                    u.setTurbodrop(u.getTurbodrop() + Time.MINUTE.getTime(60) * 2L);
                                    this.u.save();
                                    break;
                                }
                                case 6: {
                                    Util.giveItem(this.holder, PandoraManager.getItem(16));
                                    break;
                                }
                                case 7: {
                                    Util.giveItem(this.holder, PandoraManager.getItem(32));
                                    break;
                                }
                                case 8: {
                                    Util.giveItem(this.holder, PandoraManager.getItem(64));
                                    break;
                                }
                                case 9: {
                                    Util.giveItem(this.holder, PandoraManager.getItem(128));
                                    break;
                                }
                                case 10: {
                                    Util.giveItem(this.holder, PandoraManager.getItem(256));
                                    break;
                                }
                                case 11: {
                                    Util.giveItem(this.holder, PandoraManager.getItem(512));
                                    break;
                                }
                            }
                            Util.sendInformation("CHAT||\n&8[&7==============&8[&6ITEMSHOP&8]&7==============&8]" + "\n&r&8* &e{P} &7zakupil usluge &e{S}".replace("{S}", ac.getType()).replace("{P}", this.holder.getName()) + "\n&r&8* &7Chcesz tez cos kupic? Wejdz na: &6blazepe.pl\n&8[&7==============&8[&6ITEMSHOP&8]&7==============&8]\n ");
                            this.calculatePages();
                            this.openPage(this.validatePage(this.selectedPage));
                        }
                    }
                }
            }
        }
    }
    
    public void loadRequest(final PacketItemshopLoad logs, final Player who) {
        if (who.equals((Object)this.holder)) {
            final String w = Util.StringFromString(logs.items);
            if (w != null && !w.equalsIgnoreCase("brak")) {
//                final List<String> item = JsonIterator.deserialize(w, (Class<List<String>>)List.class);
//                for (final String s : item) {
//                    final String[] split = s.split(Pattern.quote("||"));
//                    if (split.length > 2) {
//                        final ItemShop is = new ItemShop();
//                        is.owner = split[0];
//                        is.id = Integer.valueOf(split[1]);
//                        is.type = Integer.valueOf(split[2]);
//                        this.itemShops.add(is);
//                    }
//                }
            }
            this.isLoading = false;
            final Object selectedPage = null;
            Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                this.calculatePages();
                this.openPage(this.selectedPage = (int)selectedPage);
            }, 5);
        }
    }
    
    private void setGui() {
        this.clearAll();
        this.setServerGui();
        for (int i = 0; i < 28 && this.isLoading; ++i) {
            this.addOneItem(new ItemBuilder(-161).setTitle("&r\u270b").setLore(new String[] { "&r&cTrwa ladowanie itemshopu!", "" }).build());
        }
    }
    
    @Override
    public void onClose(final Player who) {
        Main.items.remove(this);
        super.onClose(who);
    }
    
    private void calculatePages() {
        this.pages.clear();
        int i = 0;
        final List<ItemShop> ba = new ArrayList<ItemShop>(this.itemShops);
        ba.sort(ItemshopInventory.sort);
        for (final ItemShop bazaritem : ba) {
            if (!this.pages.containsKey(i)) {
                final List<ItemShop> list = new ArrayList<ItemShop>();
                this.pages.put(i, list);
            }
            if (this.pages.get(i).size() < 28) {
                final List<ItemShop> list = this.pages.get(i);
                list.add(bazaritem);
                this.pages.replace(i, list);
            }
            else {
                ++i;
                final List<ItemShop> newList = new ArrayList<ItemShop>();
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
        for (final Map.Entry<Integer, List<ItemShop>> entry : this.pages.entrySet()) {
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
            for (final ItemShop log : this.pages.get(page)) {
                if (log.type == 0) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(339).setTitle("&r&6Ranga vip").build(), "idis", "" + log.id));
                }
                else if (log.type == 1) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(339).setTitle("&r&6Ranga svip").build(), "idis", "" + log.id));
                }
                else if (log.type == 2) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(339).setTitle("&r&6Ranga sponsor").build(), "idis", "" + log.id));
                }
                else if (log.type == 3) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(54).setTitle("&r&6Sejf").build(), "idis", "" + log.id));
                }
                else if (log.type == 4) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(280).setTitle("&r&6Lom").build(), "idis", "" + log.id));
                }
                else if (log.type == 5) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(340).setTitle("&r&6Turbodrop 1h").build(), "idis", "" + log.id));
                }
                else if (log.type == 6) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(122).setTitle("&r&6Pandora x16").build(), "idis", "" + log.id));
                }
                else if (log.type == 7) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(122).setTitle("&r&6Pandora x32").build(), "idis", "" + log.id));
                }
                else if (log.type == 8) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(122).setTitle("&r&6Pandora x64").build(), "idis", "" + log.id));
                }
                else if (log.type == 9) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(122).setTitle("&r&6Pandora x128").build(), "idis", "" + log.id));
                }
                else if (log.type == 10) {
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(122).setTitle("&r&6Pandora x256").build(), "idis", "" + log.id));
                }
                else {
                    if (log.type != 11) {
                        continue;
                    }
                    this.addOneItem(Util.addNBTTagWithValue(new ItemBuilder(122).setTitle("&r&6Pandora x512").build(), "idis", "" + log.id));
                }
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
        sort = ((o1, o2) -> o2.type - o1.type);
    }
}
