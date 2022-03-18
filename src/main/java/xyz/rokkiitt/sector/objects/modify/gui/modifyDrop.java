package xyz.rokkiitt.sector.objects.modify.gui;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import com.jsoniter.output.JsonStream;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import xyz.rokkiitt.sector.objects.drop.Drop;
import xyz.rokkiitt.sector.objects.drop.DropManager;
import xyz.rokkiitt.sector.packets.PacketModifyDrop;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.modify.DropItem;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedDropItem;
import xyz.rokkiitt.sector.utils.DyeColor;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;

public class modifyDrop extends DoubleChestFakeInventory {
    public modifyDrop(Player p) {
        super(null, Util.fixColor("&6Modyfikujesz drop"));
        this.state = State.MAIN;
        this.drops = ConcurrentHashMap.newKeySet();
        this.holder = (InventoryHolder)p;
        for (Drop d : DropManager.getItems()) {
            DropItem item = new DropItem();
            item.chance = d.getChance();
            item.exp = d.getExp();
            item.guislot = d.getSlot();
            item.maxAmount = d.getMaxAmount();
            item.minAmount = d.getMinAmount();
            item.what = d.getWhat();
            this.drops.add(item);
        }
        sendGui();
        p.addWindow(this);
    }

    static List<Integer> available_slots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21,
            22, 23, 24, 25);

    private DropItem selected;

    private State state;

    private final Set<DropItem> drops;

    protected void onSlotChange(FakeSlotChangeEvent e) {
        e.setCancelled();
        if (this.state == State.MAIN) {
            if (!available_slots.contains(e.getAction().getSlot()))
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
            DropItem item = new DropItem();
            item.guislot = e.getAction().getSlot();
            item.what = target;
            item.maxAmount = 1;
            item.minAmount = 1;
            item.chance = 0.0D;
            item.exp = 0;
            this.state = State.EDIT;
            this.selected = item;
            sendGui();
        } else if (this.state == State.EDIT) {
            if (this.selected != null)
                if (e.getAction().getSlot() == 31) {
                    this.drops.forEach(s -> {
                        if (s.equals(this.selected)) {
                            this.drops.remove(s);
                            this.state = State.MAIN;
                            sendGui();
                        }
                    });
                } else if (e.getAction().getSlot() == 39) {
                    this.selected = null;
                    this.state = State.MAIN;
                    sendGui();
                } else if (e.getAction().getSlot() == 41) {
                    this.state = State.CHANCE;
                    sendGui();
                }
        } else if (this.state == State.CHANCE) {
            if (this.selected != null)
                if (e.getAction().getSlot() == 20) {
                    if (this.selected.chance - 0.01D <= 0.0D) {
                        this.selected.chance = 0.0D;
                        setItem(22, parseItem(this.selected));
                        return;
                    }
                    this.selected.chance -= 0.01D;
                    setItem(22, parseItem(this.selected));
                } else if (e.getAction().getSlot() == 24) {
                    if (this.selected.chance + 0.01D >= 100.0D)
                        this.selected.chance = 100.0D;
                    this.selected.chance += 0.01D;
                    setItem(22, parseItem(this.selected));
                } else if (e.getAction().getSlot() == 39) {
                    this.selected = null;
                    this.state = State.MAIN;
                    sendGui();
                } else if (e.getAction().getSlot() == 41) {
                    this.state = State.EXP;
                    sendGui();
                } else if (e.getAction().getSlot() == 19) {
                    if (this.selected.chance - 0.1D <= 0.0D) {
                        this.selected.chance = 0.0D;
                        setItem(22, parseItem(this.selected));
                        return;
                    }
                    this.selected.chance -= 0.1D;
                    setItem(22, parseItem(this.selected));
                } else if (e.getAction().getSlot() == 25) {
                    if (this.selected.chance + 0.1D >= 100.0D)
                        this.selected.chance = 100.0D;
                    this.selected.chance += 0.1D;
                    setItem(22, parseItem(this.selected));
                }
        } else if (this.state == State.EXP) {
            if (this.selected != null)
                if (e.getAction().getSlot() == 20) {
                    if (this.selected.exp <= 0) {
                        this.selected.exp = 0;
                        setItem(22, parseItem(this.selected));
                        return;
                    }
                    this.selected.exp--;
                    setItem(22, parseItem(this.selected));
                } else if (e.getAction().getSlot() == 24) {
                    this.selected.exp++;
                    setItem(22, parseItem(this.selected));
                } else if (e.getAction().getSlot() == 39) {
                    this.state = State.CHANCE;
                    sendGui();
                } else if (e.getAction().getSlot() == 41) {
                    this.state = State.MINAMOUNT;
                    sendGui();
                }
        } else if (this.state == State.MINAMOUNT) {
            if (this.selected != null)
                if (e.getAction().getSlot() == 20) {
                    if (this.selected.minAmount <= 1) {
                        this.selected.minAmount = 1;
                        setItem(22, parseItem(this.selected));
                        return;
                    }
                    this.selected.minAmount--;
                    setItem(22, parseItem(this.selected));
                } else if (e.getAction().getSlot() == 24) {
                    this.selected.minAmount++;
                    if (this.selected.maxAmount <= this.selected.minAmount)
                        this.selected.maxAmount = this.selected.minAmount;
                    setItem(22, parseItem(this.selected));
                } else if (e.getAction().getSlot() == 39) {
                    this.state = State.EXP;
                    sendGui();
                } else if (e.getAction().getSlot() == 41) {
                    this.state = State.MAXAMOUNT;
                    sendGui();
                }
        } else if (this.state == State.MAXAMOUNT &&
                this.selected != null) {
            if (e.getAction().getSlot() == 20) {
                if (this.selected.maxAmount <= this.selected.minAmount) {
                    this.selected.maxAmount = this.selected.minAmount;
                    setItem(22, parseItem(this.selected));
                    return;
                }
                this.selected.maxAmount--;
                setItem(22, parseItem(this.selected));
            } else if (e.getAction().getSlot() == 24) {
                this.selected.maxAmount++;
                if (this.selected.maxAmount >= 100)
                    this.selected.maxAmount = 100;
                setItem(22, parseItem(this.selected));
            } else if (e.getAction().getSlot() == 39) {
                this.state = State.MINAMOUNT;
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
            Main.getDatabase().addQueue("TRUNCATE drops;");
            this.drops.forEach(s -> {
                SerializedDropItem drop = new SerializedDropItem();
                drop.chance = s.chance;
                drop.exp = s.exp;
                drop.guislot = s.guislot;
                drop.maxAmount = s.maxAmount;
                drop.minAmount = s.minAmount;
                s.what.setCount(1);
                drop.what = s.what.getId();
                Main.getDatabase().addQueue("INSERT INTO `drops`(`id`, `drop`) VALUES (NULL, '" + JsonStream.serialize(drop) + "')");
            });
        }
        super.onClose(p);
        Server.getInstance().getScheduler().scheduleDelayedTask(Main.getPlugin(), () -> DropManager.load(), 100);
    }

    private void sendGui() {
        clearAll();
        setServerGui();
        if (this.state == State.MAIN) {
            int[] disabled = {
                    28, 29, 30, 31, 32, 33, 34, 37, 38, 39,
                    40, 41, 42, 43 };
            for (int i : disabled)
                setItem(i, (new ItemBuilder(-161)).setTitle("&r&6Strefa wylaczona od mozliwosci ustawienia dropu").build());
            this.drops.forEach(v -> setItem(v.guislot, parseItem(v)));
        } else if (this.state == State.EDIT) {
            if (this.selected != null) {
                setItem(22, parseItem(this.selected));
                setItem(31, DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r")).setLore(Util.fixColor("&r&fKliknij aby usunac!"), ""));
                setItem(39, Item.get(77).setCustomName(Util.fixColor("&r")).setLore(Util.fixColor("&r&cKliknij aby cofnac"), " "));
                setItem(41, Item.get(77).setCustomName(Util.fixColor("&r")).setLore(Util.fixColor("&r&aKliknij aby potwierdzic"), " "));
            }
        } else if (this.state == State.CHANCE) {
            if (this.selected != null) {
                setItem(22, parseItem(this.selected));
                setItem(20,
                        DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby odjac &e0.01 &fdo szansy"), " "));
                setItem(24,
                        DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby dodac &e0.01 &fod szansy"), " "));
                setItem(19,
                        DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby odjac &e0.1 &fdo szansy"), " "));
                setItem(25,
                        DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby dodac &e0.1 &fod szansy"), " "));
                setItem(39, Item.get(77).setCustomName(Util.fixColor("&r"))
                        .setLore(Util.fixColor("&r&cKliknij aby cofnac"), " "));
                setItem(41, Item.get(77).setCustomName(Util.fixColor("&r"))
                        .setLore(Util.fixColor("&r&aKliknij aby potwierdzic"), " "));
            }
        } else if (this.state == State.EXP) {
            if (this.selected != null) {
                setItem(22, parseItem(this.selected));
                setItem(20,
                        DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby odjac &e1 &fexpa"), " "));
                setItem(24,
                        DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby dodac &e1 &fexpa"), " "));
                setItem(39, Item.get(77).setCustomName(Util.fixColor("&r"))
                        .setLore(Util.fixColor("&r&cKliknij aby cofnac"), " "));
                setItem(41, Item.get(77).setCustomName(Util.fixColor("&r"))
                        .setLore(Util.fixColor("&r&aKliknij aby potwierdzic"), " "));
            }
        } else if (this.state == State.MINAMOUNT) {
            if (this.selected != null) {
                setItem(22, parseItem(this.selected));
                setItem(20,
                        DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby odjac &e1 &fdo minimalnej ilosci"), " "));
                setItem(24,
                        DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&r"))
                                .setLore(Util.fixColor("&r&fKliknij aby dodac &e1 &fdo minimalnej ilosci"), " "));
                setItem(39, Item.get(77).setCustomName(Util.fixColor("&r"))
                        .setLore(Util.fixColor("&r&cKliknij aby cofnac"), " "));
                setItem(41, Item.get(77).setCustomName(Util.fixColor("&r"))
                        .setLore(Util.fixColor("&r&aKliknij aby potwierdzic"), " "));
            }
        } else if (this.state == State.MAXAMOUNT &&
                this.selected != null) {
            setItem(22, parseItem(this.selected));
            setItem(20,
                    DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&r"))
                            .setLore(Util.fixColor("&r&fKliknij aby odjac &e1 &fdo maxymalnej ilosci"), " "));
            setItem(24,
                    DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&r"))
                            .setLore(Util.fixColor("&r&fKliknij aby dodac &e1 &fdo maxymalnej ilosci"), " "));
            setItem(39, Item.get(77).setCustomName(Util.fixColor("&r"))
                    .setLore(Util.fixColor("&r&cKliknij aby cofnac"), " "));
            setItem(41, Item.get(77).setCustomName(Util.fixColor("&r"))
                    .setLore(Util.fixColor("&r&aKliknij aby zakonczyc"), " "));
        }
    }

    private Item parseItem(DropItem v) {
        Item what = getCopyItem(v.what.clone());
        what.setLore(parseLore(v.what.getLore(), new String[] { "", "&r&fSzansa: &e{CHANCE}", "&r&fExp: &e{EXP}", "&r&fMin: &e{MIN}", "&r&fMax: &e{MAX}", "" }, v));
        if (what.hasCustomName())
            what.setCustomName(what.getCustomName());
        what.setCount(1);
        return what;
    }

    private Item getCopyItem(Item i) {
        Item item = Item.get(i.getId(), i.getDamage(), 1);
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

    private String[] parseLore(String[] oldlore, String[] t, DropItem v) {
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

    private String parseLore(String msg, DropItem v) {
        msg = msg.replace("{CHANCE}", String.valueOf(Util.round(v.chance)));
        msg = msg.replace("{EXP}", String.valueOf(v.exp));
        msg = msg.replace("{MAX}", String.valueOf(v.maxAmount));
        msg = msg.replace("{MIN}", String.valueOf(v.minAmount));
        return Util.fixColor(msg);
    }

    private enum State {
        MAIN, EDIT, CHANCE, EXP, MINAMOUNT, MAXAMOUNT;
    }
}
