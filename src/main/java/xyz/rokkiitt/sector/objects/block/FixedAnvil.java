package xyz.rokkiitt.sector.objects.block;

import cn.nukkit.block.*;
import cn.nukkit.item.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.objects.FixedAnvilInventory;
import cn.nukkit.level.*;
import cn.nukkit.inventory.*;

public class FixedAnvil extends BlockAnvil
{
    public boolean onActivate(final Item item, final Player player) {
        if (player != null) {
            player.addWindow((Inventory)new FixedAnvilInventory(player.getUIInventory(), (Position)this), Integer.valueOf(2));
        }
        return true;
    }
}
