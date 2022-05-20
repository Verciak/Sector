package xyz.rokkiitt.sector.objects.wymiana;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.DyeColor;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.item.*;
import cn.nukkit.inventory.*;

import java.util.*;

public class Trade extends DoubleChestFakeInventory
{
    public final Player p1;
    public final Player p2;
    private final List<Integer> border;
    private final List<Integer> first_area;
    private final List<Integer> second_area;
    private final List<Integer> ITEMSfirst_area;
    private final List<Integer> ITEMSsecond_area;
    private final Item welna;
    private final Item welna1;
    private final Item welna2;
    private final Item szklo;
    public final User u1;
    public final User u2;
    public boolean p1closed;
    public boolean p2closed;
    private TradeStatus status1;
    private TradeStatus status2;
    private final List<Item> items1;
    private final List<Item> items2;
    
    public Trade(final Player p1, final Player p2, final User u1, final User u2) {
        super(null, Util.fixColor("&6Wymiana"));
        this.border = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 22, 31, 40, 49);
        this.first_area = Arrays.asList(0, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48);
        this.second_area = Arrays.asList(8, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 50, 51, 52, 53);
        this.ITEMSfirst_area = Arrays.asList(18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48);
        this.ITEMSsecond_area = Arrays.asList(23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 50, 51, 52, 53);
        this.welna = DyeColor.get(DyeColor.RED).setCustomName(Util.fixColor("&cOczekiwanie"));
        this.welna1 = DyeColor.get(DyeColor.YELLOW).setCustomName(Util.fixColor("&eZatwierdzono"));
        this.welna2 = DyeColor.get(DyeColor.LIME).setCustomName(Util.fixColor("&aZaakceptowano"));
        this.szklo = GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r"));
        this.p1closed = false;
        this.p2closed = false;
        this.status1 = TradeStatus.NOT_READY;
        this.status2 = TradeStatus.NOT_READY;
        this.items1 = new ArrayList<Item>();
        this.items2 = new ArrayList<Item>();
        this.p1 = p1;
        this.p2 = p2;
        this.u1 = u1;
        this.u2 = u2;
        this.openPage();
        p1.addWindow((Inventory)this);
        p2.addWindow((Inventory)this);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        final Player player = e.getPlayer();
        final Item clicked = e.getAction().getSourceItem();
        final int slot = e.getAction().getSlot();
        if (this.p1 == null || this.p2 == null || clicked == null) {
            player.removeWindow((Inventory)this);
            e.setCancelled(true);
            return;
        }
        if (this.border.contains(slot)) {
            e.setCancelled(true);
        }
        else if (this.first_area.contains(slot)) {
            if (this.p1 == player) {
                if (slot == 0) {
                    if (this.status1 == TradeStatus.NOT_READY) {
                        e.setCancelled(true);
                        this.setItem(slot, this.welna1);
                        this.status1 = TradeStatus.READY;
                        this.sendSlot(0, new Player[] { this.p2, this.p1 });
                    }
                    else if (this.status1 == TradeStatus.READY) {
                        if (this.status2 == TradeStatus.READY || this.status2 == TradeStatus.ACCEPTED) {
                            e.setCancelled(true);
                            this.items1.clear();
                            for (final int x : this.ITEMSfirst_area) {
                                final Item itemstack = this.getItem(x);
                                if (itemstack != null && !itemstack.isNull()) {
                                    this.items1.add(itemstack);
                                }
                            }
                            this.setItem(slot, this.welna2);
                            this.sendSlot(0, new Player[] { this.p2, this.p1 });
                            this.status1 = TradeStatus.ACCEPTED;
                            if (this.endWymiana()) {
                                Util.giveItem(this.p1, this.items2);
                                Util.giveItem(this.p2, this.items1);
                                this.p1.removeWindow((Inventory)this);
                                this.p2.removeWindow((Inventory)this);
                                TradeHandler.deleteOnSucces(this);
                            }
                        }
                        else {
                            e.setCancelled(true);
                        }
                    }
                    else if (this.status1 == TradeStatus.ACCEPTED) {
                        e.setCancelled(true);
                    }
                }
                else if (this.status1 == TradeStatus.READY) {
                    e.setCancelled(true);
                }
                else if (this.status1 == TradeStatus.ACCEPTED) {
                    e.setCancelled(true);
                }
            }
            else {
                e.setCancelled(true);
            }
        }
        else if (this.second_area.contains(slot)) {
            if (this.p2 == player) {
                if (slot == 8) {
                    if (this.status2 == TradeStatus.NOT_READY) {
                        e.setCancelled(true);
                        this.setItem(slot, this.welna1);
                        this.sendSlot(8, new Player[] { this.p2, this.p1 });
                        this.status2 = TradeStatus.READY;
                    }
                    else if (this.status2 == TradeStatus.READY) {
                        if (this.status1 == TradeStatus.READY || this.status1 == TradeStatus.ACCEPTED) {
                            e.setCancelled(true);
                            this.items2.clear();
                            for (final int x : this.ITEMSsecond_area) {
                                final Item itemstack = this.getItem(x);
                                if (itemstack != null && !itemstack.isNull()) {
                                    this.items2.add(itemstack);
                                }
                            }
                            this.setItem(slot, this.welna2);
                            this.sendSlot(8, new Player[] { this.p2, this.p1 });
                            this.status2 = TradeStatus.ACCEPTED;
                            if (this.endWymiana()) {
                                Util.giveItem(this.p1, this.items2);
                                Util.giveItem(this.p2, this.items1);
                                this.p1.removeWindow((Inventory)this);
                                this.p2.removeWindow((Inventory)this);
                                TradeHandler.deleteOnSucces(this);
                            }
                        }
                        else {
                            e.setCancelled(true);
                        }
                    }
                    else if (this.status2 == TradeStatus.ACCEPTED) {
                        e.setCancelled(true);
                    }
                }
                else if (this.status2 == TradeStatus.READY) {
                    e.setCancelled(true);
                }
                else if (this.status2 == TradeStatus.ACCEPTED) {
                    e.setCancelled(true);
                }
            }
            else {
                e.setCancelled(true);
            }
        }
    }
    
    @Override
    public void onClose(final Player who) {
        super.onClose(who);
        if (!this.endWymiana()) {
            if (this.p1 == who && !this.p1closed) {
                this.items1.clear();
                for (final int x : this.ITEMSfirst_area) {
                    final Item itemstack = this.getItem(x);
                    if (itemstack != null && !itemstack.isNull()) {
                        this.items1.add(itemstack);
                    }
                }
                Util.giveItem(this.p1, this.items1);
                this.p1closed = true;
            }
            else if (this.p2 == who && !this.p2closed) {
                this.items2.clear();
                for (final int x : this.ITEMSsecond_area) {
                    final Item itemstack = this.getItem(x);
                    if (itemstack != null && !itemstack.isNull()) {
                        this.items2.add(itemstack);
                    }
                }
                Util.giveItem(this.p2, this.items2);
                this.p2closed = true;
            }
            TradeHandler.deleteOnClose(this);
        }
    }
    
    private boolean endWymiana() {
        return this.status1 == TradeStatus.ACCEPTED && this.status2 == TradeStatus.ACCEPTED;
    }
    
    private void openPage() {
        final List<Integer> sl = Arrays.asList(1, 2, 4, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 22, 31, 40, 49);
        for (final Integer s : sl) {
            this.setItem((int)s, this.szklo);
        }
        this.setItem(3, new ItemBuilder(389).setTitle("&r&9" + this.p1.getName()).build());
        this.setItem(5, new ItemBuilder(389).setTitle("&r&9" + this.p2.getName()).build());
        this.setItem(0, this.welna);
        this.setItem(8, this.welna);
    }
}
