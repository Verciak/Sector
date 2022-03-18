package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;

public class Craftings extends DoubleChestFakeInventory
{
    private final Item bar;
    private final Item craft;
    private State state;
    
    public Craftings() {
        super(null, Util.fixColor("&6Craftingi"));
        this.bar = GlassColor.get(GlassColor.ORANGE).setCustomName(Util.fixColor("&r"));
        this.craft = new ItemBuilder(58).setTitle("&r&eWytworz item").build();
        this.state = State.STONIARKA;
        this.sendGui();
        this.sendCrafting();
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        final int slot = e.getAction().getSlot();
        if (slot == 49) {
            if (this.state == State.STONIARKA) {
                final int i = Util.getItemTypeCount(e.getPlayer(), 1);
                final int i2 = Util.getItemTypeCount(e.getPlayer(), 264);
                if (e.getPlayer().hasPermission(Perms.CRAFTINGBYPASS.getPermission())) {
                    Util.giveItem(e.getPlayer(), PItemsGUI.stoniarka);
                }
                else if (i >= 8 && i2 >= 1) {
                    Util.removeItemById(e.getPlayer(), Item.get(1, Integer.valueOf(0), 8));
                    Util.removeItemById(e.getPlayer(), Item.get(264, Integer.valueOf(0), 1));
                    Util.giveItem(e.getPlayer(), PItemsGUI.stoniarka);
                }
            }
            else if (this.state == State.CX) {
                final int i = Util.getItemTypeCount(e.getPlayer(), 4);
                if (e.getPlayer().hasPermission(Perms.CRAFTINGBYPASS.getPermission())) {
                    Util.giveItem(e.getPlayer(), PItemsGUI.cx);
                }
                else if (i >= 576) {
                    Util.removeItemById(e.getPlayer(), Item.get(4, Integer.valueOf(0), 576));
                    Util.giveItem(e.getPlayer(), PItemsGUI.cx);
                }
            }
            else if (this.state == State.BOY) {
                final int i = Util.getItemTypeCount(e.getPlayer(), 49);
                final int i2 = Util.getItemTypeCount(e.getPlayer(), 264);
                if (e.getPlayer().hasPermission(Perms.CRAFTINGBYPASS.getPermission())) {
                    Util.giveItem(e.getPlayer(), PItemsGUI.boy);
                }
                else if (i >= 8 && i2 >= 1) {
                    Util.removeItemById(e.getPlayer(), Item.get(49, Integer.valueOf(0), 8));
                    Util.removeItemById(e.getPlayer(), Item.get(264, Integer.valueOf(0), 1));
                    Util.giveItem(e.getPlayer(), PItemsGUI.boy);
                }
            }
            else if (this.state == State.SAND) {
                final int i = Util.getItemTypeCount(e.getPlayer(), 12);
                final int i2 = Util.getItemTypeCount(e.getPlayer(), 264);
                if (e.getPlayer().hasPermission(Perms.CRAFTINGBYPASS.getPermission())) {
                    Util.giveItem(e.getPlayer(), PItemsGUI.sand);
                }
                else if (i >= 8 && i2 >= 1) {
                    Util.removeItemById(e.getPlayer(), Item.get(12, Integer.valueOf(0), 8));
                    Util.removeItemById(e.getPlayer(), Item.get(264, Integer.valueOf(0), 1));
                    Util.giveItem(e.getPlayer(), PItemsGUI.sand);
                }
            }
            else if (this.state == State.KOPACZ) {
                final int i = Util.getItemTypeCount(e.getPlayer(), 1);
                final int i2 = Util.getItemTypeCount(e.getPlayer(), 278);
                if (e.getPlayer().hasPermission(Perms.CRAFTINGBYPASS.getPermission())) {
                    Util.giveItem(e.getPlayer(), PItemsGUI.kopacz);
                }
                else if (i >= 8 && i2 >= 1) {
                    Util.removeItemById(e.getPlayer(), Item.get(1, Integer.valueOf(0), 8));
                    Util.removeItemById(e.getPlayer(), Item.get(278, Integer.valueOf(0), 1));
                    Util.giveItem(e.getPlayer(), PItemsGUI.kopacz);
                }
            }
            else if (this.state == State.RZUCANE) {
                final int i = Util.getItemTypeCount(e.getPlayer(), 46);
                if (e.getPlayer().hasPermission(Perms.CRAFTINGBYPASS.getPermission())) {
                    Util.giveItem(e.getPlayer(), PItemsGUI.rzucane);
                }
                else if (i >= 576) {
                    Util.removeItemById(e.getPlayer(), Item.get(46, Integer.valueOf(0), 576));
                    Util.giveItem(e.getPlayer(), PItemsGUI.rzucane);
                }
            }
        }
        else if (slot == 0) {
            this.state = State.STONIARKA;
            this.sendCrafting();
        }
        else if (slot == 9) {
            this.state = State.CX;
            this.sendCrafting();
        }
        else if (slot == 18) {
            this.state = State.BOY;
            this.sendCrafting();
        }
        else if (slot == 27) {
            this.state = State.SAND;
            this.sendCrafting();
        }
        else if (slot == 36) {
            this.state = State.KOPACZ;
            this.sendCrafting();
        }
        else if (slot == 45) {
            this.state = State.RZUCANE;
            this.sendCrafting();
        }
    }
    
    private void sendCrafting() {
        if (this.state == State.STONIARKA) {
            this.setItem(4, PItemsGUI.stoniarka);
            this.setItem(12, Item.get(1));
            this.setItem(13, Item.get(1));
            this.setItem(14, Item.get(1));
            this.setItem(21, Item.get(1));
            this.setItem(22, Item.get(264));
            this.setItem(23, Item.get(1));
            this.setItem(30, Item.get(1));
            this.setItem(31, Item.get(1));
            this.setItem(32, Item.get(1));
        }
        else if (this.state == State.CX) {
            this.setItem(4, PItemsGUI.cx);
            this.setItem(12, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(13, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(14, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(21, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(22, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(23, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(30, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(31, Item.get(4, Integer.valueOf(0), 64));
            this.setItem(32, Item.get(4, Integer.valueOf(0), 64));
        }
        else if (this.state == State.BOY) {
            this.setItem(4, PItemsGUI.boy);
            this.setItem(12, Item.get(49));
            this.setItem(13, Item.get(49));
            this.setItem(14, Item.get(49));
            this.setItem(21, Item.get(49));
            this.setItem(22, Item.get(264));
            this.setItem(23, Item.get(49));
            this.setItem(30, Item.get(49));
            this.setItem(31, Item.get(49));
            this.setItem(32, Item.get(49));
        }
        else if (this.state == State.SAND) {
            this.setItem(4, PItemsGUI.sand);
            this.setItem(12, Item.get(12));
            this.setItem(13, Item.get(12));
            this.setItem(14, Item.get(12));
            this.setItem(21, Item.get(12));
            this.setItem(22, Item.get(264));
            this.setItem(23, Item.get(12));
            this.setItem(30, Item.get(12));
            this.setItem(31, Item.get(12));
            this.setItem(32, Item.get(12));
        }
        else if (this.state == State.KOPACZ) {
            this.setItem(4, PItemsGUI.kopacz);
            this.setItem(12, Item.get(1));
            this.setItem(13, Item.get(1));
            this.setItem(14, Item.get(1));
            this.setItem(21, Item.get(1));
            this.setItem(22, Item.get(278));
            this.setItem(23, Item.get(1));
            this.setItem(30, Item.get(1));
            this.setItem(31, Item.get(1));
            this.setItem(32, Item.get(1));
        }
        else if (this.state == State.RZUCANE) {
            this.setItem(4, PItemsGUI.rzucane);
            this.setItem(12, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(13, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(14, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(21, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(22, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(23, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(30, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(31, Item.get(46, Integer.valueOf(0), 64));
            this.setItem(32, Item.get(46, Integer.valueOf(0), 64));
        }
    }
    
    private void sendGui() {
        this.setItem(8, this.bar);
        this.setItem(17, this.bar);
        this.setItem(26, this.bar);
        this.setItem(35, this.bar);
        this.setItem(44, this.bar);
        this.setItem(53, this.bar);
        this.setItem(0, PItemsGUI.stoniarka);
        this.setItem(9, PItemsGUI.cx);
        this.setItem(18, PItemsGUI.boy);
        this.setItem(27, PItemsGUI.sand);
        this.setItem(36, PItemsGUI.kopacz);
        this.setItem(45, PItemsGUI.rzucane);
        this.setItem(49, this.craft);
    }

    private enum State {
        STONIARKA, CX, BOY, KOPACZ, SAND, RZUCANE;
    }
}
