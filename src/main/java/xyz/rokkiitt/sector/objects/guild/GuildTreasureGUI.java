package xyz.rokkiitt.sector.objects.guild;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.*;
import java.util.*;

public class GuildTreasureGUI extends DoubleChestFakeInventory
{
    Guild g;

    public GuildTreasureGUI(final Map<Integer, Item> map, Guild guild) {
        super(null, Util.fixColor("&6Skarbiec"));
        this.g = guild;
        this.setContents((Map)map);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        final List<Player> copy = new ArrayList<Player>(this.viewers);
        copy.remove(e.getPlayer());
        this.sendContents((Collection)copy);
    }

    @Override
    public void onClose(Player who) {
        g.setSkarbiec(this);
        super.onClose(who);
    }
}
