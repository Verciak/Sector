package xyz.rokkiitt.sector.utils;

import cn.nukkit.item.enchantment.*;
import cn.nukkit.item.*;
import cn.nukkit.nbt.tag.*;
import java.util.*;

public class ItemBuilder
{
    private int amount;
    private int data;
    private Map<Enchantment, Integer> enchants;
    private String[] lore;
    private int id;
    private String name;
    private boolean glow;
    private boolean hideTags;
    
    public ItemBuilder(final int mat) {
        this(mat, 1);
    }
    
    public ItemBuilder(final ItemBuilder it) {
        this.hideTags = false;
        this.amount = it.getAmount();
        this.data = it.getData();
        this.enchants = it.getEnchants();
        this.lore = it.getLore();
        this.id = it.getMat();
        this.name = it.getName();
        this.glow = false;
    }
    
    public ItemBuilder(final int mat, final int amount) {
        this(mat, amount, 0);
    }
    
    public ItemBuilder(final int mat, final int amount, final int data) {
        this.hideTags = false;
        this.name = null;
        this.enchants = new HashMap<Enchantment, Integer>();
        this.id = mat;
        this.amount = amount;
        this.data = data;
        this.glow = false;
    }
    
    public ItemBuilder(final int mat, final int amount, final String name) {
        this.hideTags = false;
        this.data = 0;
        this.enchants = new HashMap<Enchantment, Integer>();
        this.id = mat;
        this.amount = amount;
        this.name = name;
        this.glow = false;
    }
    
    public ItemBuilder(final int mat, final int amount, final String name, final List<String> lore) {
        this.hideTags = false;
        this.data = 0;
        this.enchants = new HashMap<Enchantment, Integer>();
        this.id = mat;
        this.amount = amount;
        this.name = name;
        this.glow = false;
    }
    
    public ItemBuilder(final int mat, final short data) {
        this(mat, 1, data);
    }
    
    public ItemBuilder addEnchant(final Enchantment enchant, final int i) {
        this.enchants.put(enchant, i);
        return this;
    }
    
    public ItemBuilder addEnchantment(final Enchantment enchant, final int level) {
        this.enchants.remove(enchant);
        this.enchants.put(enchant, level);
        return this;
    }
    
    public Item build(final boolean hideTags) {
        this.hideTags = hideTags;
        return this.build();
    }
    
    public Item build() {
        final Item im = Item.get(this.id, Integer.valueOf(this.data), this.amount);
        if (this.hideTags) {
            CompoundTag tag = im.getNamedTag();
            if (tag == null) {
                tag = new CompoundTag();
            }
            final CompoundTag finalTag = tag;
            tag.putList(new ListTag("2"));
            im.setNamedTag(finalTag);
        }
        if (this.name != null) {
            im.setCustomName(Util.fixColor(this.name));
        }
        if (this.lore != null) {
            im.setLore(Util.fixColor(this.lore));
        }
        for (final Map.Entry<Enchantment, Integer> e : this.enchants.entrySet()) {
            im.addEnchantment(new Enchantment[] { e.getKey().setLevel((int)e.getValue(), false) });
        }
        if (this.glow) {
            CompoundTag tag = im.getNamedTag();
            if (tag == null) {
                tag = new CompoundTag();
            }
            tag.putList(new ListTag("ench"));
            im.setNamedTag(tag);
        }
        return im;
    }
    
    public ItemBuilder addGlow(final boolean t) {
        this.glow = t;
        return this;
    }
    
    private int getAmount() {
        return this.amount;
    }
    
    private int getData() {
        return this.data;
    }
    
    private Map<Enchantment, Integer> getEnchants() {
        return this.enchants;
    }
    
    private String[] getLore() {
        return this.lore;
    }
    
    private int getMat() {
        return this.id;
    }
    
    private String getName() {
        return this.name;
    }
    
    public ItemBuilder setAmount(final int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemBuilder setData(final short data) {
        this.data = data;
        return this;
    }
    
    public ItemBuilder setEnchants(final Map<Enchantment, Integer> enchants) {
        this.enchants = enchants;
        return this;
    }
    
    public ItemBuilder setLore(final String[] lore) {
        this.lore = lore;
        return this;
    }
    
    public void setID(final int mat) {
        this.id = mat;
    }
    
    public ItemBuilder setTitle(final String name) {
        this.name = name;
        return this;
    }
}
