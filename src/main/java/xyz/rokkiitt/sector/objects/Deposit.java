package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.DepositUtil;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;

public class Deposit extends ChestFakeInventory
{
    private final User u;
    
    public Deposit(final Player p, final User u) {
        super(null, Util.fixColor("&6Depozyt"));
        this.u = u;
        this.holder = (InventoryHolder)p;
        this.setSmallServerGui();
        this.show();
        p.addWindow((Inventory)this);
    }
    
    private void show() {
        this.setItem(10, new ItemBuilder(466).setTitle("&r&6Koxy").setLore(new String[] { " ", "&r&e Posiadasz: &7" + this.u.getEgapple(), "&r&e Limit: &7" + Settings.LIMIT_EGAPPLE }).build());
        this.setItem(11, new ItemBuilder(322).setTitle("&r&6Refile").setLore(new String[] { " ", "&r&e Posiadasz: &7" + this.u.getGapple(), "&r&e Limit: &7" + Settings.LIMIT_GAPPLE }).build());
        this.setItem(12, new ItemBuilder(368).setTitle("&r&5Perly").setLore(new String[] { " ", "&r&e Posiadasz: &7" + this.u.getPearls(), "&r&e Limit: &7" + Settings.LIMIT_PEARLS }).build());
        this.setItem(14, new ItemBuilder(262).setTitle("&r&fStrzaly").setLore(new String[] { " ", "&r&e Posiadasz: &7" + this.u.getArrows(), "&r&e Limit: &7" + Settings.LIMIT_ARROWS }).build());
        this.setItem(15, new ItemBuilder(332).setTitle("&r&fSniezki").setLore(new String[] { " ", "&r&e Posiadasz: &7" + this.u.getSnowballs(), "&r&e Limit: &7" + Settings.LIMIT_SNOWBALLS }).build());
        this.setItem(16, PItemsGUI.rzucane.clone().setLore(Util.fixColor(new String[] { "", "&r&e Posiadasz: &7" + this.u.getPrimedtnt(), "&r&e Limit: &7" + Settings.LIMIT_PRIMEDTNT })));
        this.setItem(22, new ItemBuilder(410).setTitle("&r&aWyrownaj do limitu").setLore(new String[] { " ", "&r&e Kliknij aby wyrownac wszystkie ", "&r&e przedmiotu do limitu" }).build());
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        final Player p = e.getPlayer();
        final int slot = e.getAction().getSlot();
        if (slot == 10) {
            DepositUtil.koxy(p, this.u);
            this.show();
        }
        else if (slot == 11) {
            DepositUtil.refy(p, this.u);
            this.show();
        }
        else if (slot == 12) {
            DepositUtil.perly(p, this.u);
            this.show();
        }
        else if (slot == 14) {
            DepositUtil.strzaly(p, this.u);
            this.show();
        }
        else if (slot == 15) {
            DepositUtil.sniezki(p, this.u);
            this.show();
        }
        else if (slot == 16) {
            DepositUtil.rzucane(p, this.u);
            this.show();
        }
        else if (slot == 22) {
            DepositUtil.koxy(p, this.u);
            DepositUtil.refy(p, this.u);
            DepositUtil.perly(p, this.u);
            DepositUtil.strzaly(p, this.u);
            DepositUtil.sniezki(p, this.u);
            DepositUtil.rzucane(p, this.u);
            this.show();
        }
    }
}
