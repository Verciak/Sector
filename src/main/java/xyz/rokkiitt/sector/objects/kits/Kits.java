package xyz.rokkiitt.sector.objects.kits;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.utils.DyeColor;
import java.util.ArrayList;
import java.util.List;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.kits.data.KitData;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;

public class Kits extends DoubleChestFakeInventory {
    private final User u;

    private State state;

    static Item bar = GlassColor.get(GlassColor.ORANGE).setCustomName(Util.fixColor("&r"));

    static Item back = (new ItemBuilder(351, 1, DyeColor.RED.getDyeData())).setTitle("&r&cPowrot").build();

    static Item claim = (new ItemBuilder(351, 1, DyeColor.LIME.getDyeData())).setTitle("&r&aOdbierz").build();

    public Kits(Player p, User u) {
        super(null, Util.fixColor("&6Zestawy"));
        this.u = u;
        this.holder = (InventoryHolder) p;
        this.state = State.MAIN;
        show();
        p.addWindow((Inventory) this);
    }

    private void show() {
        clearAll();
        if (this.state == State.MAIN) {
            setServerGui();
            setItem(20, gracz);
            setItem(22, zestawender);
            setItem(24, zestawjedzenie);
            setItem(29, vip);
            setItem(31, svip);
            setItem(33, sponsor);
        } else if (this.state == State.FOOD) {
            setServerGui();
            addItem(new Item[]{jedzenie});
            setItem(49, back);
            setItem(53, claim);
        } else if (this.state == State.ENDER) {
            setServerGui();
            addItem(new Item[]{ender});
            setItem(49, back);
            setItem(53, claim);
        } else if (this.state == State.GRACZ) {
            setServerGui();
            for (KitData kit : KitManager.getPlayer()) {
                setItem(kit.getSlot(), kit.getWhat().clone().setLore(parseLore(kit.getWhat().getLore(), new String[]{"", "&r&l&8%> &fIlosc: &e{AMOUNT}", ""}, kit)));
            }
            setItem(49, back);
            setItem(53, claim);
        } else if (this.state == State.VIP) {
            setServerGui();
            for (KitData kit : KitManager.getVip()) {
                setItem(kit.getSlot(), kit.getWhat().clone().setLore(parseLore(kit.getWhat().getLore(), new String[]{"", "&r&l&8%> &fIlosc: &e{AMOUNT}", ""}, kit)));
            }
            setItem(49, back);
            setItem(53, claim);
        } else if (this.state == State.SVIP) {
            setServerGui();
            for (KitData kit : KitManager.getSvip()) {
                setItem(kit.getSlot(), kit.getWhat().clone().setLore(parseLore(kit.getWhat().getLore(), new String[]{"", "&r&l&8%> &fIlosc: &e{AMOUNT}", ""}, kit)));
            }
            setItem(49, back);
            setItem(53, claim);
        } else if (this.state == State.SPONSOR) {
            setServerGui();
            for (KitData kit : KitManager.getSponsor()) {
                setItem(kit.getSlot(), kit.getWhat().clone().setLore(parseLore(kit.getWhat().getLore(), new String[]{"", "&r&l&8%> &fIlosc: &e{AMOUNT}", ""}, kit)));
            }
            setItem(49, back);
            setItem(53, claim);
        }
    }

    protected void onSlotChange(FakeSlotChangeEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        int slot = e.getAction().getSlot();
        if (this.state == State.MAIN) {
            if (slot == 20) {
                this.state = State.GRACZ;
                show();
            } else if (slot == 22) {
                this.state = State.ENDER;
                show();
            } else if (slot == 24) {
                this.state = State.FOOD;
                show();
            } else if (slot == 29) {
                this.state = State.VIP;
                show();
            } else if (slot == 31) {
                this.state = State.SVIP;
                show();
            } else if (slot == 33) {
                this.state = State.SPONSOR;
                show();
            }
        } else if (this.state == State.GRACZ) {
            if (slot == 49) {
                this.state = State.MAIN;
                show();
            } else if (slot == 53) {
                if (this.u.getPlayertime() + Settings.KIT_PLAYER <= System.currentTimeMillis() || p.hasPermission(Perms.KITBYPASS.getPermission())) {
                    List<Item> items = new ArrayList<>();
                    for (KitData kit : KitManager.getPlayer()) {
                        Item what = kit.getWhat().clone();
                        what.setCount(kit.getAmount());
                        items.add(what);
                    }
                    Util.giveItem(p, items);
                    this.u.setPlayertime(System.currentTimeMillis());
                } else {
                    Util.sendMessage((CommandSender) p, Settings.getMessage("kitcooldown").replace("{TIME}", Util.formatTime(this.u.getPlayertime() + Settings.KIT_PLAYER - System.currentTimeMillis())));
                }
            }
        } else if (this.state == State.ENDER) {
            if (slot == 49) {
                this.state = State.MAIN;
                show();
            } else if (slot == 53) {
                if (this.u.getEndertime() + Settings.KIT_ENDER <= System.currentTimeMillis() || p.hasPermission(Perms.KITBYPASS.getPermission())) {
                    Util.giveItem(p, new Item[]{ender});
                    this.u.setEndertime(System.currentTimeMillis());
                } else {
                    Util.sendMessage((CommandSender) p, Settings.getMessage("kitcooldown").replace("{TIME}", Util.formatTime(this.u.getEndertime() + Settings.KIT_ENDER - System.currentTimeMillis())));
                }
            }
        } else if (this.state == State.FOOD) {
            if (slot == 49) {
                this.state = State.MAIN;
                show();
            } else if (slot == 53) {
                if (this.u.getFoodtime() + Settings.KIT_FOOD <= System.currentTimeMillis() || p.hasPermission(Perms.KITBYPASS.getPermission())) {
                    Util.giveItem(p, new Item[]{jedzenie});
                    this.u.setFoodtime(System.currentTimeMillis());
                } else {
                    Util.sendMessage((CommandSender) p, Settings.getMessage("kitcooldown").replace("{TIME}", Util.formatTime(this.u.getFoodtime() + Settings.KIT_FOOD - System.currentTimeMillis())));
                }
            }
        } else if (this.state == State.VIP) {
            if (slot == 49) {
                this.state = State.MAIN;
                show();
            } else if (slot == 53) {
                if (Settings.ENABLE_KIT) {
                    if (p.hasPermission(Perms.VIP_KIT.getPermission())) {
                        if (this.u.getViptime() + getFixedTime(Settings.KIT_VIP) <= System.currentTimeMillis() || p.hasPermission(Perms.KITBYPASS.getPermission())) {
                            List<Item> items = new ArrayList<>();
                            for (KitData kit : KitManager.getVip()) {
                                Item what = kit.getWhat().clone();
                                what.setCount(kit.getAmount());
                                items.add(what);
                            }
                            Util.giveItem(p, items);
                            this.u.setViptime(System.currentTimeMillis());
                        } else {
                            Util.sendMessage((CommandSender) p, Settings.getMessage("kitcooldown").replace("{TIME}", Util.formatTime(this.u.getViptime() + Settings.KIT_VIP - System.currentTimeMillis())));
                        }
                    } else {
                        Util.sendMessage((CommandSender) p, Settings.getMessage("kitnoperm"));
                    }
                } else {
                    Util.sendMessage((CommandSender) p, Settings.getMessage("kitoff"));
                }
            }
        } else if (this.state == State.SVIP) {
            if (slot == 49) {
                this.state = State.MAIN;
                show();
            } else if (slot == 53) {
                if (Settings.ENABLE_KIT) {
                    if (p.hasPermission(Perms.SVIP_KIT.getPermission())) {
                        if (this.u.getSviptime() + getFixedTime(Settings.KIT_SVIP) <= System.currentTimeMillis() || p.hasPermission(Perms.KITBYPASS.getPermission())) {
                            List<Item> items = new ArrayList<>();
                            for (KitData kit : KitManager.getSvip()) {
                                Item what = kit.getWhat().clone();
                                what.setCount(kit.getAmount());
                                items.add(what);
                            }
                            Util.giveItem(p, items);
                            this.u.setSviptime(System.currentTimeMillis());
                        } else {
                            Util.sendMessage((CommandSender) p, Settings.getMessage("kitcooldown").replace("{TIME}", Util.formatTime(this.u.getSviptime() + Settings.KIT_SVIP - System.currentTimeMillis())));
                        }
                    } else {
                        Util.sendMessage((CommandSender) p, Settings.getMessage("kitnoperm"));
                    }
                } else {
                    Util.sendMessage((CommandSender) p, Settings.getMessage("kitoff"));
                }
            }
        } else if (this.state == State.SPONSOR) {
            if (slot == 49) {
                this.state = State.MAIN;
                show();
            } else if (slot == 53) {
                if (Settings.ENABLE_KIT) {
                    if (p.hasPermission(Perms.SPONSOR_KIT.getPermission())) {
                        if (this.u.getSponsortime() + getFixedTime(Settings.KIT_SPONSOR) <= System.currentTimeMillis() || p.hasPermission(Perms.KITBYPASS.getPermission())) {
                            List<Item> items = new ArrayList<>();
                            for (KitData kit : KitManager.getSponsor()) {
                                Item what = kit.getWhat().clone();
                                what.setCount(kit.getAmount());
                                items.add(what);
                            }
                            Util.giveItem(p, items);
                            this.u.setSponsortime(System.currentTimeMillis());
                        } else {
                            Util.sendMessage((CommandSender) p, Settings.getMessage("kitcooldown").replace("{TIME}", Util.formatTime(this.u.getSponsortime() + Settings.KIT_SPONSOR - System.currentTimeMillis())));
                        }
                    } else {
                        Util.sendMessage((CommandSender) p, Settings.getMessage("kitnoperm"));
                    }
                } else {
                    Util.sendMessage((CommandSender) p, Settings.getMessage("kitoff"));
                }
            }
        }
    }

    static Item zestawjedzenie = (new ItemBuilder(320)).setTitle("&r&6&lZestaw jedznie").build();

    static Item zestawender = (new ItemBuilder(130)).setTitle("&r&6&lZestaw enderchest").build();

    static Item gracz = (new ItemBuilder(299)).setTitle("&r&6&lZestaw gracz").build();

    static Item vip = (new ItemBuilder(307)).setTitle("&r&6&lZestaw vip").build();

    static Item svip = (new ItemBuilder(315)).setTitle("&r&6&lZestaw svip").build();

    static Item sponsor = (new ItemBuilder(311)).setTitle("&r&6&lZestaw sponsor").build();

    static Item ender = (new ItemBuilder(130)).build();

    static Item jedzenie = (new ItemBuilder(320, 64)).build();

    private long getFixedTime(long time) {
        return time;
    }

    private String[] parseLore(String[] oldlore, String[] t, KitData v) {
        String[] lor = new String[oldlore.length + t.length];
        int i = 0;
        for (String s : oldlore) {
            lor[i] = parseDrop(s, v);
            i++;
        }
        for (String ss : t) {
            lor[i] = parseDrop(ss, v);
            i++;
        }
        return lor;
    }

    private String parseDrop(String msg, KitData d) {
        msg = msg.replace("{AMOUNT}", String.valueOf(d.getAmount()));
        return Util.fixColor(msg);
    }

    private enum State {
        MAIN, FOOD, ENDER, GRACZ, VIP, SVIP, SPONSOR;
    }
}
