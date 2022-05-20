package xyz.rokkiitt.sector.objects.drop;

import cn.nukkit.utils.DyeColor;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.cobblex.Cobblex;
import xyz.rokkiitt.sector.objects.cobblex.CobblexManager;
import xyz.rokkiitt.sector.objects.drop.gui.MainDropInventory;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteDrop;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.pandora.Pandora;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.PolishItemNames;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.*;
import cn.nukkit.inventory.*;
import cn.nukkit.nbt.tag.*;

public class DropInventory extends DoubleChestFakeInventory
{
    static Item bar;
    static Item onall;
    static Item offall;
    static Item cx;
    static Item pcase;
    static Item meteorite;
    static String[] DROP_STONE_LORE;
    public static Item back;
    private final User u;
    public static Player who;
    
    public DropInventory(final Player p, final User u) {
        super(null, Util.fixColor("&6Drop"));
        this.who = p;
        this.u = u;
        this.refreshGui();
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
            switch (e.getAction().getSlot()) {
                case 42: {
                    new MainDropInventory(e.getPlayer(), u);
                    break;
                }
                case 37: {
                    this.u.setAllDrops(true);
                    this.setItem(43, new ItemBuilder(4, 1).setTitle("&r&l&7Kamien").setLore(new String[] { "\u270b", "&r&8>> &fWypadanie: &e{STATUS}".replace("{STATUS}", this.u.hasDrop(43) ? "&cNie" : "&aTak") }).addGlow(!this.u.hasDrop(43)).build());
                    for (final Drop drop : DropManager.getItems()) {
                        final Item what = this.getCopyItem(drop.getWhat().clone());
                        what.setCustomName(Util.fixColor("&r&9&l" + PolishItemNames.getPolishName(drop.getWhat())));
                        what.setLore(this.parseLore(drop.getWhat().getLore(), DropInventory.DROP_STONE_LORE, drop, this.u));
                        if (!this.u.hasDrop(drop.getSlot()) && !what.hasEnchantments()) {
                            CompoundTag tag = what.getNamedTag();
                            if (tag == null) {
                                tag = new CompoundTag();
                            }
                            tag.putList(new ListTag("ench"));
                            what.setNamedTag(tag);
                        }
                        this.setItem(drop.getSlot(), what);
                    }
                    break;
                }
                case 38: {
                    this.u.setAllDrops(false);
                    this.setItem(43, new ItemBuilder(4, 1).setTitle("&r&l&7Kamien").setLore(new String[] { "\u270b", "&r&8>> &fWypadanie: &e{STATUS}".replace("{STATUS}", this.u.hasDrop(43) ? "&cNie" : "&aTak") }).addGlow(!this.u.hasDrop(43)).build());
                    for (final Drop drop : DropManager.getItems()) {
                        final Item what = this.getCopyItem(drop.getWhat().clone());
                        what.setCustomName(Util.fixColor("&r&9&l" + PolishItemNames.getPolishName(drop.getWhat())));
                        what.setLore(this.parseLore(drop.getWhat().getLore(), DropInventory.DROP_STONE_LORE, drop, this.u));
                        if (!this.u.hasDrop(drop.getSlot()) && !what.hasEnchantments()) {
                            CompoundTag tag = what.getNamedTag();
                            if (tag == null) {
                                tag = new CompoundTag();
                            }
                            tag.putList(new ListTag("ench"));
                            what.setNamedTag(tag);
                        }
                        this.setItem(drop.getSlot(), what);
                    }
                    break;
                }
                default: {
                    this.u.setDrop(e.getAction().getSlot());
                    this.refreshGui();
                    break;
                }
            }
    }
    
    private void refreshGui() {
        this.clearAll();
            this.setServerGui();
            if (Settings.PANDORA_TIME >= System.currentTimeMillis()) {
                this.setItem(40, new ItemBuilder(122).setTitle("&r&l&6Pandora").setLore(PandoraManager.parsePandora(this.u)).addGlow(!this.u.hasDrop(40)).build());
            }
            this.setItem(38, DropInventory.onall);
            this.setItem(37, DropInventory.offall);
            this.setItem(42, DropInventory.back);

            this.setItem(43, new ItemBuilder(4, 1).setTitle("&r&l&7Kamien").setLore(new String[] { "\u270b","&r&8>> &fWypadanie: &e{STATUS}".replace("{STATUS}", this.u.hasDrop(43) ? "&cNie" : "&aTak") }).addGlow(!this.u.hasDrop(43)).build());
            for (final Drop drop : DropManager.getItems()) {
                final Item what = this.getCopyItem(drop.getWhat().clone());
                what.setCustomName(Util.fixColor("&r&9&l" + PolishItemNames.getPolishName(drop.getWhat())));
                what.setLore(this.parseLore(drop.getWhat().getLore(), DropInventory.DROP_STONE_LORE, drop, this.u));
                if (!this.u.hasDrop(drop.getSlot()) && !what.hasEnchantments()) {
                    CompoundTag tag = what.getNamedTag();
                    if (tag == null) {
                        tag = new CompoundTag();
                    }
                    tag.putList(new ListTag("ench"));
                    what.setNamedTag(tag);
                }
                this.setItem(drop.getSlot(), what);
            }
    }

    public static Item getCopyItem(final Item i) {
        final Item item = Item.get(i.getId(), Integer.valueOf(i.getDamage()), 1);
        if (i.getLore().length > 0) {
            item.setLore(i.getLore());
        }
        if (i.hasCustomName()) {
            item.setCustomName(i.getCustomName());
        }
        if (i.hasEnchantments()) {
            item.addEnchantment(i.getEnchantments());
        }
        if (i.hasCompoundTag()) {
            item.setCompoundTag(i.getCompoundTag());
        }
        return item;
    }

    public static String[] parseLore(final String[] oldlore, final String[] t) {
        final String[] lor = new String[oldlore.length + t.length];
        int i = 0;
        for (final String s : oldlore) {
            lor[i] = s;
            ++i;
        }
        for (final String ss : t) {
            lor[i] = ss;
            ++i;
        }
        return lor;
    }

    public static String[] parseLore(final String[] oldlore, final String[] t, final Drop v, final User u) {
        final String[] lor = new String[oldlore.length + t.length];
        int i = 0;
        for (final String s : oldlore) {
            lor[i] = parseDrop(s, v, u);
            ++i;
        }
        for (final String ss : t) {
            lor[i] = parseDrop(ss, v, u);
            ++i;
        }
        return lor;
    }

    public static String parseDrop(String msg, final Drop d, final User u) {
        double chance = d.getChance();
        if (who.hasPermission(Perms.SPONSOR.getPermission())) {
            chance *= 1.5;
        }
        else if (who.hasPermission(Perms.SVIP.getPermission())) {
            chance *= 1.35;
        }
        else if (who.hasPermission(Perms.VIP.getPermission())) {
            chance *= 1.2;
        }
        msg = msg.replace("{CHANCE}", String.valueOf(Util.round(chance)));
        msg = msg.replace("{XP}", String.valueOf(d.getExp()));
        msg = msg.replace("{STATUS}", u.hasDrop(d.getSlot()) ? "&cWYLACZONY" : "&aWLACZONY");
        msg = msg.replace("{MIN}", d.getMinAmount() + "");
        msg = msg.replace("{MAX}", d.getMaxAmount() + "");
        return Util.fixColor(msg);
    }
    
    static {
        DropInventory.bar = new ItemBuilder(101).setTitle(" ").build();
        DropInventory.onall = new ItemBuilder(351, 1, DyeColor.LIME.getDyeData()).setTitle("&r&aWlacz wszystko").build();
        DropInventory.offall = new ItemBuilder(351, 1, DyeColor.RED.getDyeData()).setTitle("&r&cWylacz wszystko").build();
        DropInventory.cx = new ItemBuilder(48).setTitle("&r&7Drop z CobbleX").build();
        DropInventory.pcase = new ItemBuilder(122).setTitle("&r&7Drop z Pandory").build();
        DropInventory.meteorite = new ItemBuilder(432).setTitle("&r&7Drop z Meteorytu").build();
        DropInventory.DROP_STONE_LORE = new String[] { "\u270b", "&r&8>> &fSzansa na drop: &e{CHANCE}", "&r&8>> &fIlosc XP: &e{XP}", "&r&8>> &fMinimalnie: &e{MIN}", "&r&8>> &fMaxymalnie: &e{MAX}", "&r&8>> &fDrop: &e{STATUS}", "" };
        DropInventory.back = new ItemBuilder(-161).setTitle("&r&9Powrot").build();
    }
}
