package xyz.rokkiitt.sector.utils;


public class SerializedItemLore {

    public String lore;

    public String getLore() {
        lore.replace(lore.substring(0,0), "");
        lore.replace(lore.substring(lore.length(), lore.length()), "");
        return this.lore;
    }

}
