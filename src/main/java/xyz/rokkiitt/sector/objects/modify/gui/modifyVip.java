package xyz.rokkiitt.sector.objects.modify.gui;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import com.jsoniter.output.JsonStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.kits.KitManager;
import xyz.rokkiitt.sector.objects.kits.data.KitData;
import xyz.rokkiitt.sector.objects.modify.KitItem;
import xyz.rokkiitt.sector.packets.PacketModifyDrop;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedKitItem;
import xyz.rokkiitt.sector.utils.DyeColor;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.Util;

public class modifyVip extends DoubleChestFakeInventory {
    public modifyVip(Player p) {
        super(null, Util.fixColor("&6Modyfikujesz kit vip"));
        this.state = State.MAIN;
        this.drops = ConcurrentHashMap.newKeySet();
        this.holder = (InventoryHolder)p;
        for (KitData d : KitManager.getVip()) {
            KitItem item = new KitItem();
            item.guislot = d.getSlot();
            item.amount = d.getAmount();
            item.what = d.getWhat();
            this.drops.add(item);
        }
        sendGui();
        p.addWindow((Inventory)this);
    }

    static List<Integer> available_slots = Arrays.asList(new Integer[] {
            Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(12), Integer.valueOf(13), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21),
            Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33),
            Integer.valueOf(34), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43) });

    private KitItem selected;

    private State state;

    private final Set<KitItem> drops;

    protected void onSlotChange(FakeSlotChangeEvent e) {
        e.setCancelled();
        if (this.state == State.MAIN) {
            if (!available_slots.contains(Integer.valueOf(e.getAction().getSlot())))
                return;
            if (e.getAction().getSourceItem() != null && e.getAction().getSourceItem().getId() != 0) {
                this.drops.forEach(s -> {
                    if (s.guislot == e.getAction().getSlot()) {
                        this.state = State.EDIT;
                        this.selected = s;
                        sendGui();
                    }
                });
                return;
            }
            Item target = e.getAction().getTargetItem().clone();
            target.setCount(1);
            if (target.hasCustomName())
                target.setCustomName(Util.fixColor(target.getCustomName()));
            KitItem item = new KitItem();
            item.guislot = e.getAction().getSlot();
            item.what = target;
            item.amount = 1;
            this.state = State.EDIT;
            this.selected = item;
            sendGui();
        } else if (this.state == State.EDIT) {
            if (this.selected != null)
                if (e.getAction().getSlot() == 31) {
                    this.drops.forEach(s -> {
                        if (s.equals(this.selected)) {
                            this.drops.remove(s);
                            SerializedKitItem drop = new SerializedKitItem();
                            drop.guislot = s.guislot;
                            drop.amount = s.amount;
                            s.what.setCount(1);
                            Main.getDatabase().addQueue("DELETE FROM `vipkit` WHERE `drop` = '" + JsonStream.serialize(drop) + "'");

                            this.state = modifyVip.State.MAIN;
                            sendGui();
                        }
                    });
                } else if (e.getAction().getSlot() == 39) {
                    this.selected = null;
                    this.state = State.MAIN;
                    sendGui();
                } else if (e.getAction().getSlot() == 41) {
                    this.state = State.AMOUNT;
                    sendGui();
                }
        } else if (this.state == State.AMOUNT &&
                this.selected != null) {
            if (e.getAction().getSlot() == 20) {
                if (this.selected.amount <= 1) {
                    this.selected.amount = 1;
                    setItem(22, parseItem(this.selected));
                    return;
                }
                this.selected.amount--;
                setItem(22, parseItem(this.selected));
            } else if (e.getAction().getSlot() == 24) {
                this.selected.amount++;
                setItem(22, parseItem(this.selected));
            } else if (e.getAction().getSlot() == 39) {
                this.state = State.EDIT;
                sendGui();
            } else if (e.getAction().getSlot() == 41) {
                this.drops.add(this.selected);
                this.selected = null;
                this.state = State.MAIN;
                sendGui();
            }
        }
    }

    public void onClose(Player p) {
        PacketModifyDrop pa = new PacketModifyDrop();
        if (this.drops.isEmpty()) {
            pa.data = "brak";
        } else {
            Main.getDatabase().addQueue("TRUNCATE vipkit;");

            this.drops.forEach(s -> {
                SerializedKitItem drop = new SerializedKitItem();
                drop.guislot = s.guislot;
                drop.amount = s.amount;
                s.what.setCount(1);
                String wath = ItemSerializer.itemToString(s.what);
                String data = JsonStream.serialize(drop);
                pa.data = data;
                Main.getDatabase().addQueue("INSERT INTO vipkit(`id`, `drop`, `item`) VALUES (NULL, '" + pa.data + "', '"+wath+"')");
            });
        }
        super.onClose(p);
        Server.getInstance().getScheduler().scheduleDelayedTask(Main.getPlugin(), () -> KitManager.load("vip"), 100);

    }

    private void sendGui() {
        clearAll();
        setServerGui();
        if (this.state == State.MAIN) {
            this.drops.forEach(v -> setItem(v.guislot, parseItem(v)));
        } else if (this.state == State.EDIT) {
            if (this.selected != null) {
                setItem(22, parseItem(this.selected));
                setItem(31, DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r")).setLore(new String[] { Util.fixColor("&r&fKliknij aby usunac!"), "" }));
                setItem(39, Item.get(77).setCustomName(Util.fixColor("&r")).setLore(new String[] { Util.fixColor("&r&cKliknij aby cofnac"), " " }));
                setItem(41, Item.get(77).setCustomName(Util.fixColor("&r")).setLore(new String[] { Util.fixColor("&r&aKliknij aby potwierdzic"), " " }));
            }
        } else if (this.state == State.AMOUNT &&
                this.selected != null) {
            setItem(22, parseItem(this.selected));
            setItem(20, DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r")).setLore(new String[] { Util.fixColor("&r&fKliknij aby odjac &e1 &fdo ilosci"), " " }));
            setItem(24, DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&r")).setLore(new String[] { Util.fixColor("&r&fKliknij aby dodac &e1 &fdo ilosci"), " " }));
            setItem(39, Item.get(77).setCustomName(Util.fixColor("&r")).setLore(new String[] { Util.fixColor("&r&cKliknij aby cofnac"), " " }));
            setItem(41, Item.get(77).setCustomName(Util.fixColor("&r")).setLore(new String[] { Util.fixColor("&r&aKliknij aby zakonczyc"), " " }));
        }
    }

    private Item parseItem(KitItem v) {
        Item what = getCopyItem(v.what.clone());
        what.setLore(parseLore(v.what.getLore(), new String[] { "", "&r&fIlosc: &e{AMOUNT}", "" }, v));
        if (what.hasCustomName())
            what.setCustomName(Util.fixColor(what.getCustomName()));
        what.setCount(1);
        return what;
    }

    private Item getCopyItem(Item i) {
        Item item = Item.get(i.getId(), Integer.valueOf(i.getDamage()), 1);
        if ((i.getLore()).length > 0)
            item.setLore(i.getLore());
        if (i.hasCustomName())
            item.setCustomName(i.getCustomName());
        if (i.hasEnchantments())
            item.addEnchantment(i.getEnchantments());
        if (i.hasCompoundTag())
            item.setCompoundTag(i.getCompoundTag());
        return item;
    }

    private String[] parseLore(String[] oldlore, String[] t, KitItem v) {
        String[] lor = new String[oldlore.length + t.length];
        int i = 0;
        for (String s : oldlore) {
            lor[i] = parseLore(s, v);
            i++;
        }
        for (String ss : t) {
            lor[i] = parseLore(ss, v);
            i++;
        }
        return lor;
    }

    private String parseLore(String msg, KitItem v) {
        msg = msg.replace("{AMOUNT}", String.valueOf(v.amount));
        return Util.fixColor(msg);
    }

    private enum State {
        MAIN, EDIT, AMOUNT;
    }
}

