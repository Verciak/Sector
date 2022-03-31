package xyz.rokkiitt.sector.objects.premiumcase;

import cn.nukkit.Player;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.utils.RandomUtil;

public class CaseInv {
    private Player player;

    private int rool;

    private int roolMax;

    private ChestFakeInventory inv;

    public CaseInv(Player player, ChestFakeInventory inv) {
        this.player = player;
        this.rool = 0;
        this.roolMax = RandomUtil.getRandInt(18, 27);
        this.inv = inv;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getRool() {
        return this.rool;
    }

    public void setRool(int rool) {
        this.rool = rool;
    }

    public int getRoolMax() {
        return this.roolMax;
    }

    public void setRoolMax(int roolMax) {
        this.roolMax = roolMax;
    }

    public ChestFakeInventory getInv() {
        return this.inv;
    }

    public void setInv(ChestFakeInventory inv) {
        this.inv = inv;
    }
}

