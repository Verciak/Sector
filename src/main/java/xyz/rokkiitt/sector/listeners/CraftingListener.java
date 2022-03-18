package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.inventory.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.item.*;
import cn.nukkit.event.*;

public class CraftingListener implements Listener
{
    @EventHandler
    public void onItemCraft(final CraftItemEvent e) {
        final Item result = e.getRecipe().getResult();
        if (result != null) {
            if (result.getId() == 513 || result.getId() == 333 || result.getId() == 328 || result.getId() == 425) {
                e.setCancelled();
                return;
            }
            if (!e.getPlayer().hasPermission(Perms.CRAFTINGBYPASS.getPermission()) && !Settings.ENABLE_DIAX && (result.getId() == 311 || result.getId() == 313 || result.getId() == 310 || result.getId() == 312 || result.getId() == 276)) {
                e.setCancelled(true);
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("enablediamond"));
                return;
            }
        }
    }
}
