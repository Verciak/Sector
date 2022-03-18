package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.inventory.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.item.enchantment.*;
import cn.nukkit.event.*;

public class RepairItemListener implements Listener
{
    @EventHandler
    public void onRepair(final RepairItemEvent e) {
        if (e.getPlayer().getExperienceLevel() <= e.getCost() && e.getPlayer().getGamemode() != 1) {
            e.setCancelled(true);
            Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "tried to combine item without enought expierience (" + e.getPlayer().getExperience() + "|" + e.getPlayer().getExperienceLevel() + "|" + e.getCost() + ")"));
            e.getPlayer().setExperience(e.getPlayer().getExperience(), e.getPlayer().getExperienceLevel());
            return;
        }
        final Item item = e.getNewItem();
        if (item != null) {
            if (item instanceof ItemArmor) {
                final Enchantment prot = item.getEnchantment(0);
                final Enchantment dur = item.getEnchantment(17);
                final Enchantment fall = item.getEnchantment(2);
                if ((prot != null && prot.getLevel() > 3) || (dur != null && dur.getLevel() > 2) || (fall != null && fall.getLevel() > 2)) {
                    e.setCancelled(true);
                    return;
                }
            }
            if (item instanceof ItemTool && item.isSword()) {
                final Enchantment damage = item.getEnchantment(9);
                final Enchantment fire = item.getEnchantment(13);
                if ((damage != null && damage.getLevel() > 3) || (fire != null && fire.getLevel() > 1)) {
                    e.setCancelled(true);
                    return;
                }
            }
            if (item instanceof ItemBow) {
                final Enchantment flame = item.getEnchantment(21);
                final Enchantment power = item.getEnchantment(19);
                if ((flame != null && flame.getLevel() > 1) || (power != null && power.getLevel() > 3)) {
                    e.setCancelled(true);
                    return;
                }
            }
            if (item.getId() == 54 && Util.hasNBTTag(item, "safe")) {
                e.setCancelled();
            }
        }
    }
}
