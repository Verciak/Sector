package xyz.rokkiitt.sector.objects.anvil;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.cobblex.Cobblex;
import xyz.rokkiitt.sector.objects.cobblex.CobblexManager;
import xyz.rokkiitt.sector.objects.drop.DropInventory;
import xyz.rokkiitt.sector.objects.drop.gui.MainDropInventory;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.Util;

import java.util.Map;

public class AnvilFakeInventory extends DoubleChestFakeInventory {

    private final Player who;

    public AnvilFakeInventory(final Player p) {
        super(null, Util.fixColor("&6KOWADLO"));
        this.who = p;
        this.refreshGui();
        p.addWindow(this);
    }

    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        int slot = e.getAction().getSlot();
        Player p = e.getPlayer();
        e.setCancelled(true);
        if(slot == 24){
            if(p.getExperienceLevel() < 5){
                close(p);
                Util.sendMessage(p, Settings.getMessage("repairlvl").replace("{LVL}", "5"));
                return;
            }
            final Item item = p.getInventory().getItemInHand().clone();
            if (item.isNull() || !(item instanceof ItemDurable)) {
                Util.sendMessage(p, Settings.getMessage("commandrepairerror"));
                return;
            }
            if (item.getDamage() == 0) {
                Util.sendMessage(p, Settings.getMessage("commandrepairfull"));
                return;
            }
            item.setDamage(0);
            p.getInventory().setItemInHand(item);
            p.setExperience(0, p.getExperienceLevel() - 5);
            Util.sendMessage(p, Settings.getMessage("repairsucces"));
            close(p);
        }
        if(slot == 31){
            if(p.getExperienceLevel() < 30){
                close(p);
                Util.sendMessage(p, Settings.getMessage("repairlvl").replace("{LVL}", "30"));
                return;
            }
            for (final Map.Entry<Integer, Item> entry : p.getInventory().getContents().entrySet()) {
                if (entry.getValue() instanceof ItemDurable) {
                    final Item item2 = entry.getValue();
                    if(!item2.isArmor()){
                        item2.setDamage(0);
                        p.getInventory().setItem(entry.getKey(), item2);
                    }
                }
            }
            p.setExperience(0, p.getExperienceLevel() - 30);
            Util.sendMessage(p, Settings.getMessage("repairsucces"));
            close(p);
        }
        if(slot == 20){
            if(p.getExperienceLevel() < 20){
                close(p);
                Util.sendMessage(p, Settings.getMessage("repairlvl").replace("{LVL}", "20"));
                return;
            }
            for (final Item entry : p.getInventory().getArmorContents()) {
                entry.setDamage(Integer.valueOf(0));
                if(entry instanceof ItemHelmetIron){
                    p.getInventory().setHelmet(entry);
                }
                if(entry instanceof ItemChestplateIron){
                    p.getInventory().setChestplate(entry);
                }
                if(entry instanceof ItemLeggingsIron){
                    p.getInventory().setLeggings(entry);
                }
                if(entry instanceof ItemBootsIron){
                    p.getInventory().setBoots(entry);
                }
            }
            p.setExperience(0, p.getExperienceLevel() - 20);
            Util.sendMessage(p, Settings.getMessage("repairsucces"));
            close(p);
        }
        e.setCancelled(true);
    }

    private void refreshGui() {
        this.clearAll();
        for(int i = 0; i < 53; i++) {
            this.setItem(i, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        }
        this.setServerGui();
        Item all_set = new Item(ItemID.IRON_CHESTPLATE, 0, 1);
        all_set.setCustomName(Util.fixColor("&r&9&lNAPRAWA CALEJ ZBROJI"));
        all_set.setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Koszt: &620 LVL", "&r&8>> &7Kilknij, aby naprawic zbroje!"}));

        Item all_eq = new Item(BlockID.ANVIL, 0, 1);
        all_eq.setCustomName(Util.fixColor("&r&9&lNAPRAWA CALEGO EKWIPUNKU"));
        all_eq.setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Koszt: &630 LVL", "&r&8>> &7Kilknij, aby naprawic caly ekwipunek!"}));

        Item item_hand = new Item(ItemID.IRON_SWORD, 0, 1);
        item_hand.setCustomName(Util.fixColor("&r&9&lNAPRAWA ITEMU W RECE"));
        item_hand.setLore(Util.fixColor(new String[]{"\u270b", "&r&8>> &7Koszt: &65 LVL", "&r&8>> &7Kilknij, aby naprawic item w rece!"}));

        this.setItem(20, all_set);
        this.setItem(31, all_eq);
        this.setItem(24, item_hand);
    }
}
