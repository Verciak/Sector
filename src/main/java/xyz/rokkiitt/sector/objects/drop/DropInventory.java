package xyz.rokkiitt.sector.objects.drop;

import cn.nukkit.utils.DyeColor;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.cobblex.Cobblex;
import xyz.rokkiitt.sector.objects.cobblex.CobblexManager;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteDrop;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.pandora.Pandora;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.ItemBuilder;
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
    static Item back;
    private final User u;
    private DropState state;
    private final Player who;
    
    public DropInventory(final Player p, final User u) {
        super(null, Util.fixColor("&6Drop"));
        this.who = p;
        this.u = u;
        this.state = DropState.MAIN;
        this.refreshGui();
        p.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        if (this.state == DropState.MAIN) {
            switch (e.getAction().getSlot()) {
                case 42: {
                    this.u.setAllDrops(true);
                    this.setItem(37, new ItemBuilder(4, 1).setTitle("&r&l&7Kamien").setLore(new String[] { "\u270b", "&r&l&8%> &fWypadanie: &e{STATUS}".replace("{STATUS}", this.u.hasDrop(37) ? "&cNie" : "&aTak") }).addGlow(!this.u.hasDrop(37)).build());
                    for (final Drop drop : DropManager.getItems()) {
                        final Item what = this.getCopyItem(drop.getWhat().clone());
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
                case 43: {
                    this.u.setAllDrops(false);
                    this.setItem(37, new ItemBuilder(4, 1).setTitle("&r&l&7Kamien").setLore(new String[] { "\u270b", "&r&l&8%> &fWypadanie: &e{STATUS}".replace("{STATUS}", this.u.hasDrop(37) ? "&cNie" : "&aTak") }).addGlow(!this.u.hasDrop(37)).build());
                    for (final Drop drop : DropManager.getItems()) {
                        final Item what = this.getCopyItem(drop.getWhat().clone());
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
                    this.state = DropState.COBBLEX;
                    this.refreshGui();
                    break;
                }
                case 39: {
                    this.state = DropState.PANDORA;
                    this.refreshGui();
                    break;
                }
                case 40: {
                    this.state = DropState.METEOR;
                    this.refreshGui();
                    break;
                }
                default: {
                    this.u.setDrop(e.getAction().getSlot());
                    this.refreshGui();
                    break;
                }
            }
        }
        else if (this.state == DropState.PANDORA) {
            if (e.getAction().getSlot() == 49) {
                this.state = DropState.MAIN;
                this.refreshGui();
            }
        }
        else if (this.state == DropState.COBBLEX) {
            if (e.getAction().getSlot() == 49) {
                this.state = DropState.MAIN;
                this.refreshGui();
            }
        }
        else if (this.state == DropState.METEOR && e.getAction().getSlot() == 49) {
            this.state = DropState.MAIN;
            this.refreshGui();
        }
    }
    
    private void refreshGui() {
        this.clearAll();
        if (this.state == DropState.MAIN) {
            this.setServerGui();
            if (Settings.PANDORA_TIME >= System.currentTimeMillis()) {
                this.setItem(34, new ItemBuilder(122).setTitle("&r&l&6Pandora").setLore(PandoraManager.parsePandora(this.u)).addGlow(!this.u.hasDrop(34)).build());
            }
            this.setItem(43, DropInventory.onall);
            this.setItem(42, DropInventory.offall);
            this.setItem(37, new ItemBuilder(4, 1).setTitle("&r&l&7Kamien").setLore(new String[] { "\u270b", "&r&l&8%> &fWypadanie: &e{STATUS}".replace("{STATUS}", this.u.hasDrop(37) ? "&cNie" : "&aTak") }).addGlow(!this.u.hasDrop(37)).build());
            this.setItem(38, DropInventory.cx);
            this.setItem(39, DropInventory.pcase);
            this.setItem(40, DropInventory.meteorite);
            for (final Drop drop : DropManager.getItems()) {
                final Item what = this.getCopyItem(drop.getWhat().clone());
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
        else if (this.state == DropState.PANDORA) {
            this.setServerGui();
            this.setItem(49, DropInventory.back);
            for (final Pandora id : PandoraManager.getItems()) {
                this.setItem(id.getSlot(), this.getCopyItem(id.getWhat().clone()).setLore(Util.fixColor(this.parseLore(id.getWhat().getLore(), new String[] { "\u270b", "&r&l&8%> &fSzansa na drop: &e" + Util.round(id.getChance()), "&r&l&8%> &fMinimalnie: &e" + id.getMinAmount(), "&r&l&8%> &fMaxymalnie: &e" + id.getMaxAmount() }))));
            }
        }
        else if (this.state == DropState.COBBLEX) {
            this.setServerGui();
            this.setItem(49, DropInventory.back);
            for (final Cobblex id2 : CobblexManager.getItems()) {
                this.setItem(id2.getSlot(), this.getCopyItem(id2.getWhat().clone()).setLore(Util.fixColor(this.parseLore(id2.getWhat().getLore(), new String[] { "\u270b", "&r&l&8%> &fSzansa na drop: &e" + Util.round(id2.getChance()), "&r&l&8%> &fMinimalnie: &e" + id2.getMinAmount(), "&r&l&8%> &fMaxymalnie: &e" + id2.getMaxAmount() }))));
            }
        }
        else if (this.state == DropState.METEOR) {
            this.setServerGui();
            this.setItem(49, DropInventory.back);
            for (final MeteoriteDrop id3 : MeteoriteManager.getDrops()) {
                this.setItem(id3.getSlot(), this.getCopyItem(id3.getWhat().clone()).setLore(Util.fixColor(this.parseLore(id3.getWhat().getLore(), new String[] { "\u270b", "&r&l&8%> &fMinimalnie: &e" + id3.getMinAmount(), "&r&l&8%> &fMaxymalnie: &e" + id3.getMaxAmount() }))));
            }
        }
    }
    
    private Item getCopyItem(final Item i) {
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
    
    private String[] parseLore(final String[] oldlore, final String[] t) {
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
    
    private String[] parseLore(final String[] oldlore, final String[] t, final Drop v, final User u) {
        final String[] lor = new String[oldlore.length + t.length];
        int i = 0;
        for (final String s : oldlore) {
            lor[i] = this.parseDrop(s, v, u);
            ++i;
        }
        for (final String ss : t) {
            lor[i] = this.parseDrop(ss, v, u);
            ++i;
        }
        return lor;
    }
    
    private String parseDrop(String msg, final Drop d, final User u) {
        double chance = d.getChance();
        if (this.who.hasPermission(Perms.SPONSOR.getPermission())) {
            chance *= 1.5;
        }
        else if (this.who.hasPermission(Perms.SVIP.getPermission())) {
            chance *= 1.35;
        }
        else if (this.who.hasPermission(Perms.VIP.getPermission())) {
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
        DropInventory.onall = new ItemBuilder(351, 1, DyeColor.LIME.getDyeData()).setTitle("&r&a&lWlacz wszystko").build();
        DropInventory.offall = new ItemBuilder(351, 1, DyeColor.RED.getDyeData()).setTitle("&r&c&lWylacz wszystko").build();
        DropInventory.cx = new ItemBuilder(48).setTitle("&r&7&lDrop z CobbleX").build();
        DropInventory.pcase = new ItemBuilder(122).setTitle("&r&7&lDrop z Pandory").build();
        DropInventory.meteorite = new ItemBuilder(432).setTitle("&r&7&lDrop z Meteorytu").build();
        DropInventory.DROP_STONE_LORE = new String[] { "\u270b", "&r&l&8%> &fSzansa na drop: &e{CHANCE}", "&r&l&8%> &fIlosc XP: &e{XP}", "&r&l&8%> &fMinimalnie: &e{MIN}", "&r&l&8%> &fMaxymalnie: &e{MAX}", "&r&l&8%> &fDrop: &e{STATUS}", "" };
        DropInventory.back = new ItemBuilder(-161).setTitle("&r&9Powrot").build();
    }

    private enum DropState {
        MAIN, PANDORA, COBBLEX, METEOR;
    }
}
