package xyz.rokkiitt.sector.objects.premiumcase;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import com.jsoniter.JsonIterator;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedPandoraItem;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PremiumCaseManager
{
    private static final Set<PremiumCase> pandora;
    public static String[] PANDORA_LORE;
    
    public static void load() {
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS `pcdrops` (" +
                "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                "`drop` TEXT NOT NULL," +
                "`item` TEXT NOT NULL);");
        PremiumCaseManager.pandora.clear();
        try {
            ResultSet query = Main.getProvider().query("SELECT * FROM `pcdrops`");
            while (query.next()) {
                SerializedPandoraItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedPandoraItem.class);

                Item i = ItemSerializer.itemFromString(query.getString("item"));
                pandora.add(new PremiumCase(i, drop.guislot, drop.chance, drop.minAmount, drop.maxAmount));
            }
            query.close();
            Main.getPlugin().getLogger().info("Loaded " + pandora.size() + " drops from 'pcdrops'");
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli pcdrops");
            ex.printStackTrace();
        }
    }
    
    public static void getDrop(final Player p, final User u) {
        if (Settings.PANDORA_TIME >= System.currentTimeMillis()) {
            if (!RandomUtil.getChance(Settings.PANDORA_CHANCE)) {
                return;
            }
            if (!u.hasDrop(34)) {
                Util.giveItem(p, getItem(1));
            }
        }
    }

    public static PremiumCase get(final Integer name) {
        for (final PremiumCase drop : getItems()) {
            if (drop.getWhat().getId() == name) {
                return drop;
            }
        }
        return null;
    }
    
    public static Item getItem(final int amount) {
        final Item pan = PItemsGUI.pandora.clone();
        pan.setCount(amount);
        return pan;
    }
    
    public static Set<PremiumCase> getItems() {
        return PremiumCaseManager.pandora;
    }
    
    public static void getDrop(final Player p) {
        final List<PremiumCase> possibleDrops = new ArrayList<PremiumCase>();
        for (final PremiumCase d : PremiumCaseManager.pandora) {
            final double chance = d.getChance();
            if (!RandomUtil.getChance(chance)) {
                continue;
            }
            possibleDrops.add(d);
        }
        if (possibleDrops.size() == 1) {
            final PremiumCase d2 = possibleDrops.get(0);
            final int a = (d2.getMinAmount() == d2.getMaxAmount()) ? d2.getMinAmount() : RandomUtil.getRandInt(d2.getMinAmount(), d2.getMaxAmount());
            final Item itemDrop = d2.getWhat().clone();
            itemDrop.setCount(a);
            p.sendTitle(Util.fixColor("&5WYLOSOWALES"), (itemDrop.hasCustomName() ? itemDrop.getCustomName() : itemDrop.getName()) + " x" + itemDrop.getCount());
            Util.giveItem(p, itemDrop);
        }
        else if (possibleDrops.size() > 1) {
            Collections.shuffle(possibleDrops);
            final PremiumCase d2 = possibleDrops.get(RandomUtil.getRandInt(0, possibleDrops.size() - 1));
            final int a = (d2.getMinAmount() == d2.getMaxAmount()) ? d2.getMinAmount() : RandomUtil.getRandInt(d2.getMinAmount(), d2.getMaxAmount());
            final Item itemDrop = d2.getWhat().clone();
            itemDrop.setCount(a);
            p.sendTitle(Util.fixColor("&5WYLOSOWALES"), (itemDrop.hasCustomName() ? itemDrop.getCustomName() : itemDrop.getName()) + " x" + itemDrop.getCount());
            Util.giveItem(p, itemDrop);
        }
        else {
            p.sendTitle(" ", Util.fixColor("&5NIC NIE WYLOSOWALES"));
        }
    }
    
    public static String[] parsePandora(final User u) {
        final String[] fin = new String[PremiumCaseManager.PANDORA_LORE.length];
        for (int i = 0; i < fin.length; ++i) {
            fin[i] = parsePandora(PremiumCaseManager.PANDORA_LORE[i], u);
        }
        return fin;
    }
    
    private static String parsePandora(String msg, final User u) {
        msg = msg.replace("{CHANCE}", String.valueOf(Settings.PANDORA_CHANCE));
        msg = msg.replace("{STATUS}", u.hasDrop(34) ? "&cWYLACZONY" : "&aWLACZONY");
        return Util.fixColor(msg);
    }
    
    static {
        pandora = ConcurrentHashMap.newKeySet();
        PremiumCaseManager.PANDORA_LORE = new String[] { "\u270b", "&r&8>> &7Szansa: &9{CHANCE}", "&r&8>> &7Drop: &9{STATUS}" };
    }
}
