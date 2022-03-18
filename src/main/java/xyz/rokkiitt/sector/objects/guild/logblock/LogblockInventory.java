package xyz.rokkiitt.sector.objects.guild.logblock;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.DateUtil;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;

public class LogblockInventory extends DoubleChestFakeInventory {
    private final HashMap<Integer, List<Logblock>> pages = new HashMap<>();

    private int selectedPage = 0;

    private final Player holder;

    private static final Comparator<Logblock> uc;

    private final String hash;

    private final Guild g;

    private String type;

    static {
        uc = ((o1, o2) -> Long.compare(o2.time, o1.time));
    }

    private final List<Logblock> loaded = new ArrayList<>();

    public LogblockInventory(Player p, Guild gu, String has) {
        super(null, Util.fixColor("&6Logblock"));
        this.holder = p;
        this.hash = has;
        this.type = "unknown";
        this.g = gu;
        setGui();
        Main.logs.add(this);
        p.addWindow((Inventory)this);
    }

    private void requestLogblocks() {
        this.loaded.clear();
        this.pages.clear();
        setGui();
    }

    public void loadRequest(List<Logblock> logs, Player who) {
        if (who.equals(this.holder)) {
            this.loaded.addAll(logs);
            Server.getInstance().getScheduler().scheduleDelayedTask(() -> {
                calculatePages();
                openPage(this.selectedPage = 0);
            },5);
        }
    }

    private void calculatePages() {
        this.pages.clear();
        int i = 0;
        List<Logblock> ba = new ArrayList<>();
        ba.addAll(this.g.getLogblocks(this.hash, this.type));
        ba.addAll(this.loaded);
        ba.sort(uc);
        for (Logblock bazaritem : ba) {
            if (!this.pages.containsKey(i)) {
                List<Logblock> list = new ArrayList<>();
                this.pages.put(i, list);
            }
            if (((List)this.pages.get(i)).size() < 28) {
                List<Logblock> list = this.pages.get(i);
                list.add(bazaritem);
                this.pages.replace(i, list);
                continue;
            }
            i++;
            List<Logblock> newList = new ArrayList<>();
            newList.add(bazaritem);
            this.pages.put(i, newList);
        }
    }

    private void openPage(int pagee) {
        clearAll();
        setServerGui();
        int page = pagee;
        setItem(49, (new ItemBuilder(-161))
                .setTitle(" &r&cKliknij aby cofnac").build());
        if (this.pages.isEmpty())
            return;
        int lastPage = 0;
        for (Map.Entry<Integer, List<Logblock>> entry : this.pages.entrySet()) {
            if (entry.getKey() > lastPage)
                lastPage = entry.getKey();
        }
        if (page > this.pages.size() - 1)
            page = lastPage;
        if (page < 0)
            page = 0;
        if (!this.pages.containsKey(page))
            page = 0;
        setItem(47, (new ItemBuilder(77))
                .setTitle((this.selectedPage == 0) ? "&r&cBrak poprzedniej strony" : (" &r&cPoprzednia strona (" + this.selectedPage + ")")).build());
        setItem(51, (new ItemBuilder(77))

                .setTitle((this.selectedPage >= this.pages.size() - 1) ? "&r&cBrak nastepnej strony" : (" &r&aNastepna strona (" + (this.selectedPage + 2) + ")")).build());
        if (this.pages.size() > 0)
            for (Logblock log : this.pages.get(page)) {
                if (this.type.equalsIgnoreCase("break")) {
                    addOneItem((new ItemBuilder(389)).setTitle("&r")
                            .setLore(new String[] { "&r&l&fKto: &e" + log.who, "&r&l&fKiedy: &e" + DateUtil.formatDate(log.time), "&r&l&fZniszczyl: &e" + log.what, "" }).build());
                    continue;
                }
                if (this.type.equalsIgnoreCase("place")) {
                    addOneItem((new ItemBuilder(389)).setTitle("&r")
                            .setLore(new String[] { "&r&l&fKto: &e" + log.who, "&r&l&fKiedy: &e" + DateUtil.formatDate(log.time), "&r&l&fPostawil: &e" + log.what, "" }).build());
                    continue;
                }
                if (this.type.equalsIgnoreCase("takenout")) {
                    addOneItem((new ItemBuilder(389)).setTitle("&r")
                            .setLore(new String[] { "&r&l&fKto: &e" + log.who, "&r&l&fKiedy: &e" + DateUtil.formatDate(log.time), "&r&l&fWyjal: &e" + log.what, "" }).build());
                    continue;
                }
                if (this.type.equalsIgnoreCase("putin")) {
                    addOneItem((new ItemBuilder(389)).setTitle("&r")
                            .setLore(new String[] { "&r&l&fKto: &e" + log.who, "&r&l&fKiedy: &e" + DateUtil.formatDate(log.time), "&r&l&fWlozyl: &e" + log.what, "" }).build());
                    continue;
                }
                if (this.type.equalsIgnoreCase("liquid"))
                    addOneItem((new ItemBuilder(389)).setTitle("&r")
                            .setLore(new String[] { "&r&l&fKto: &e" + log.who, "&r&l&fKiedy: &e" + DateUtil.formatDate(log.time), "&r&l&fCo zrobil: &e" + log.what, "" }).build());
            }
        this.selectedPage = page;
    }

    private int validatePage(int page) {
        int lastPage = this.pages.size() - 1;
        if (page > lastPage)
            page = lastPage;
        if (this.pages.size() == 0 || page < 0 || this.pages.get(page) == null)
            page = 0;
        return this.selectedPage = page;
    }

    public void onClose(Player who) {
        Main.logs.remove(this);
        super.onClose(who);
    }

    protected void onSlotChange(FakeSlotChangeEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
        int slot = e.getAction().getSlot();
        if (this.type.equalsIgnoreCase("unknown")) {
            if (this.g.isLogblockenabled()) {
                if (!Cooldown.getInstance().has(p, "logblockchange")) {
                    if (slot == 20) {
                        this.type = "break";
                        requestLogblocks();
                        Cooldown.getInstance().add(p, "logblockchange", 5.0F);
                    } else if (slot == 30) {
                        this.type = "takenout";
                        requestLogblocks();
                        Cooldown.getInstance().add(p, "logblockchange", 5.0F);
                    } else if (slot == 31) {
                        this.type = "liquid";
                        requestLogblocks();
                        Cooldown.getInstance().add(p, "logblockchange", 5.0F);
                    } else if (slot == 32) {
                        this.type = "putin";
                        requestLogblocks();
                        Cooldown.getInstance().add(p, "logblockchange", 5.0F);
                    } else if (slot == 24) {
                        this.type = "place";
                        requestLogblocks();
                        Cooldown.getInstance().add(p, "logblockchange", 5.0F);
                    }
                } else {
                    Util.sendMessage((CommandSender)p, Settings.getMessage("logblockcooldown")
                            .replace("{CD}", Util.formatTime(Cooldown.getInstance().get(p, "logblockchange").longValue() - System.currentTimeMillis())));
                }
            } else {
                Util.sendMessage((CommandSender)p, Settings.getMessage("logblocksaving"));
            }
        } else if (slot == 47) {
            int page = this.selectedPage;
            this.selectedPage = validatePage(--this.selectedPage);
            if (page != this.selectedPage)
                openPage(this.selectedPage);
        } else if (slot == 51) {
            int page = this.selectedPage;
            this.selectedPage = validatePage(++this.selectedPage);
            if (page != this.selectedPage)
                openPage(this.selectedPage);
        } else if (slot == 49) {
            this.type = "unknown";
            setGui();
            this.pages.clear();
            this.loaded.clear();
        }
    }

    public void setGui() {
        clearAll();
        setServerGui();
        if (this.type.equalsIgnoreCase("unknown")) {
            setItem(20, (new ItemBuilder(4)).setTitle("&r").setLore(new String[] { "&r&l&fKliknij aby sprawdzic logblock &ezniszczonych &fblokow w tym miejscu", "" }).build());
                    setItem(30, (new ItemBuilder(54)).setTitle("&r").setLore(new String[] { "&r&l&fKliknij aby sprawdzic logblock &ewyjetych itemow ze &fskrzynki/hoppera w tym miejscu", "" }).build());
                            setItem(31, (new ItemBuilder(325)).setTitle("&r").setLore(new String[] { "&r&l&fKliknij aby sprawdzic logblock &ewylania/zebrania wody/lavy &fw tym miejscu", "" }).build());
                                    setItem(32, (new ItemBuilder(54)).setTitle("&r").setLore(new String[] { "&r&l&fKliknij aby sprawdzic logblock &ewlozonych itemow do &fskrzynki/hoppera w tym miejscu", "" }).build());
            setItem(24, (new ItemBuilder(1)).setTitle("&r").setLore(new String[] { "&r&l&fKliknij aby sprawdzic logblock &epostawionych &fblokow w tym miejscu", "" }).build());
        } else {
            for (int i = 0; i < 28; i++) {
                addOneItem((new ItemBuilder(-161)).setTitle("&r").setLore(new String[] { "&r&l&cTrwa ladowanie logblocka!", "" }).build());
                        }
                        setItem(49, (new ItemBuilder(-161))
                                .setTitle(" &r&cKliknij aby cofnac").build());
            }
        }

        public Item[] addOneItem(Item... slots) {
            List<Item> itemSlots = new ArrayList<>();
            for (Item slot : slots) {
                if (slot.getId() != 0 && slot.getCount() > 0)
                    itemSlots.add(slot.clone());
            }
            List<Integer> emptySlots = new ArrayList<>();
            for (int i = 0; i < getSize(); i++) {
                Item item = getItem(i);
                if (item.getId() == 0 || item.getCount() <= 0)
                    emptySlots.add(i);
                if (itemSlots.isEmpty())
                    break;
            }
            if (!itemSlots.isEmpty() && !emptySlots.isEmpty())
                for (Iterator<Integer> iterator = emptySlots.iterator(); iterator.hasNext(); ) {
                    int slotIndex = ((Integer)iterator.next()).intValue();
                    if (!itemSlots.isEmpty()) {
                        Item slot = itemSlots.get(0);
                        int amount = Math.min(slot.getMaxStackSize(), slot.getCount());
                        amount = Math.min(amount, getMaxStackSize());
                        slot.setCount(slot.getCount() - amount);
                        Item item = slot.clone();
                        item.setCount(amount);
                        setItem(slotIndex, item);
                        if (slot.getCount() <= 0)
                            itemSlots.remove(slot);
                    }
                }
            return itemSlots.<Item>toArray(new Item[0]);
        }
    }
