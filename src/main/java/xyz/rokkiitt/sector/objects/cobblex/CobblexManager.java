package xyz.rokkiitt.sector.objects.cobblex;

import com.jsoniter.JsonIterator;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedPandoraItem;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class CobblexManager
{
    private static final Set<Cobblex> pandora;
    
    public static void load() {
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS `cxdrops` (" +
                "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                "`drop` varchar(9000) NOT NULL," +
                "`what` varchar(9000) NOT NULL);");
        CobblexManager.pandora.clear();
        try {
            ResultSet query = Main.getProvider().query("SELECT * FROM `cxdrops`");
            while (query.next()) {
                SerializedPandoraItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedPandoraItem.class);
                Item i = ItemSerializer.itemFromString(query.getString("what"));
                pandora.add(new Cobblex(i,drop.guislot, drop.chance, drop.minAmount, drop.maxAmount));
            }
            query.close();
            Main.getPlugin().getLogger().info("Loaded " + pandora.size() + " drops from 'cxdrops'");
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli cxdrops");
            ex.printStackTrace();
        }
    }
    
    public static Item getItem(final int amount) {
        final Item pan = PItemsGUI.cx;
        pan.setCount(amount);
        return pan;
    }
    
    public static Set<Cobblex> getItems() {
        return CobblexManager.pandora;
    }
    
    public static void getDrop(final Player p) {
        final List<Cobblex> possibleDrops = new ArrayList<Cobblex>();
        for (final Cobblex d : CobblexManager.pandora) {
            final double chance = d.getChance();
            if (!RandomUtil.getChance(chance)) {
                continue;
            }
            possibleDrops.add(d);
        }
        if (possibleDrops.size() == 1) {
            final Cobblex d2 = possibleDrops.get(0);
            final int a = (d2.getMinAmount() == d2.getMaxAmount()) ? d2.getMinAmount() : RandomUtil.getRandInt(d2.getMinAmount(), d2.getMaxAmount());
            final Item itemDrop = d2.getWhat().clone();
            itemDrop.setCount(a);
            p.sendTitle(Util.fixColor("&6WYLOSOWALES"), (itemDrop.hasCustomName() ? itemDrop.getCustomName() : itemDrop.getName()) + " x" + itemDrop.getCount());
            Util.giveItem(p, itemDrop);
        }
        else if (possibleDrops.size() > 1) {
            Collections.shuffle(possibleDrops);
            final Cobblex d2 = possibleDrops.get(RandomUtil.getRandInt(0, possibleDrops.size() - 1));
            final int a = (d2.getMinAmount() == d2.getMaxAmount()) ? d2.getMinAmount() : RandomUtil.getRandInt(d2.getMinAmount(), d2.getMaxAmount());
            final Item itemDrop = d2.getWhat().clone();
            itemDrop.setCount(a);
            p.sendTitle(Util.fixColor("&6WYLOSOWALES"), (itemDrop.hasCustomName() ? itemDrop.getCustomName() : itemDrop.getName()) + " x" + itemDrop.getCount());
            Util.giveItem(p, itemDrop);
        }
        else {
            p.sendTitle(" ", Util.fixColor("&6NIC NIE WYLOSOWALES"));
        }
    }
    
    static {
        pandora = ConcurrentHashMap.newKeySet();
    }
}
