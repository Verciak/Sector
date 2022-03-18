package xyz.rokkiitt.sector.utils;

import java.util.ArrayList;
import java.util.List;

public class SerializedItem {
    public int id;

    public int meta;

    public int count;

    public int durability;

    public String lore;

    public String customName;

    public String tags;

    public int slot;

    public List<String> enchantments;

    public void reinitItem() {
        if (this.enchantments == null)
            this.enchantments = new ArrayList<>();
        if (this.customName == null)
            this.customName = "";
        if (this.lore == null)
            this.lore = "";
        if (this.tags == null)
            this.tags = "";
    }

    public int getId() {
        return this.id;
    }

    public int getMeta() {
        return this.meta;
    }

    public int getCount() {
        return this.count;
    }

    public int getDurability() {
        return this.durability;
    }

    public String getLore() {
        return this.lore;
    }

    public String getTags() {
        return this.tags;
    }

    public List<String> getEnchantments() {
        return this.enchantments;
    }

    public void setEnchantments(List<String> enchantments) {
        this.enchantments = enchantments;
    }

    public String getCustomName() {
        return this.customName;
    }
}

