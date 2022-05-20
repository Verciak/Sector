package xyz.rokkiitt.sector.objects.drop;

import com.jsoniter.JsonIterator;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedDropItem;
import xyz.rokkiitt.sector.utils.PolishItemNames;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import cn.nukkit.item.enchantment.*;
import java.util.concurrent.*;

public class DropManager
{
    private static final Set<Drop> drops;
    private static final List<Integer> dropsslot;
    
    public static void load() {
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS `drops` (" +
                "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                "`drop` TEXT NOT NULL);");
        DropManager.drops.clear();
        DropManager.dropsslot.clear();
        try {
            ResultSet query = Main.getProvider().query("SELECT * FROM `drops`");
            while (query.next()) {
                SerializedDropItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedDropItem.class);
                DropManager.drops.add(new Drop(Item.get(drop.what, 0, 1), drop.chance, drop.exp, drop.minAmount, drop.maxAmount, drop.guislot));
                for (final Drop p : DropManager.drops) {
                    DropManager.dropsslot.add(p.getSlot());
                }
                DropManager.dropsslot.add(37);
            }
            query.close();
            Main.getPlugin().getLogger().info("Loaded " + drops.size() + " drops from 'drops'");
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli drops");
            ex.printStackTrace();
        }
    }
    
    public static Set<Drop> getItems() {
        return DropManager.drops;
    }
    
    public static List<Integer> getSlots() {
        return DropManager.dropsslot;
    }
    
    public static void getDrop(final Player p, final User u, final Item item) {
        final List<TemporaryDrop> drop = new ArrayList<TemporaryDrop>();
        for (final Drop d : DropManager.drops) {
            int expDrop = d.getExp();
            double chance = d.getChance();
            if (p.hasPermission(Perms.SPONSOR.getPermission())) {
                chance *= 1.5;
            }
            else if (p.hasPermission(Perms.SVIP.getPermission())) {
                chance *= 1.35;
            }
            else if (p.hasPermission(Perms.VIP.getPermission())) {
                chance *= 1.2;
            }
            if (!RandomUtil.getChance(chance)) {
                continue;
            }
            int a = 1;
            if (item.hasEnchantment(18)) {
                a = addFortuneEnchant((d.getMinAmount() == d.getMaxAmount()) ? d.getMinAmount() : RandomUtil.getRandInt(d.getMinAmount(), d.getMaxAmount()), item);
                expDrop *= a;
            }
            if ((u.getTurbodrop() / 2L > 0L && u.getTurbodrop() / 2L + System.currentTimeMillis() >= System.currentTimeMillis()) || Settings.DROP_TURBO >= System.currentTimeMillis()) {
                a *= 2;
                expDrop *= 4;
            }
            final Item what = d.getWhat().clone();
            what.setCount(a);
            final TemporaryDrop dro = new TemporaryDrop();
            dro.item = what;
            dro.enabled = !u.hasDrop(d.getSlot());
            dro.exp = expDrop;
            drop.add(dro);
        }
        if (drop.size() == 1) {
            final TemporaryDrop d2 = drop.get(0);
            p.sendTip(Util.fixColor("&fTrafiles na: &9{WHAT}&f. &8(&e{AMOUNT} szt.&8) &e+{EXP} {STATUS}".replace("{STATUS}", d2.enabled ? "" : "&cWYLACZONE").replace("{EXP}", d2.exp + "").replace("{AMOUNT}", d2.item.getCount() + "").replace("{WHAT}", PolishItemNames.getPolishName(d2.item))));
            if (d2.enabled) {
                Util.giveItem(p, d2.item);
            }
            p.addExperience(d2.exp);
        }
        else if (drop.size() > 1) {
            Collections.shuffle(drop);
            final TemporaryDrop d2 = drop.get(RandomUtil.getRandInt(0, drop.size() - 1));
            p.sendTip(Util.fixColor("&fTrafiles na: &9{WHAT}&f. &8(&e{AMOUNT} szt.&8) &e+{EXP} {STATUS}".replace("{STATUS}", d2.enabled ? "" : "&cWYLACZONE").replace("{EXP}", d2.exp + "").replace("{AMOUNT}", d2.item.getCount() + "").replace("{WHAT}", PolishItemNames.getPolishName(d2.item))));
            if (d2.enabled) {
                Util.giveItem(p, d2.item);
            }
            p.addExperience(d2.exp);
        }
        if (!u.hasDrop(37)) {
            Util.giveItem(p, Item.get(item.hasEnchantment(16) ? 1 : 4, Integer.valueOf(0), 1));
        }
    }
    
    public static int addFortuneEnchant(final int amount, final Item tool) {
        int a = amount;
        final Enchantment fort = tool.getEnchantment(18);
        if (fort != null && RandomUtil.getChance(45.0) && fort.getLevel() >= 3) {
            a += 3;
        }
        else if (fort != null && RandomUtil.getChance(30.0) && fort.getLevel() == 2) {
            a += 2;
        }
        else if (fort != null && RandomUtil.getChance(20.0) && fort.getLevel() == 1) {
            ++a;
        }
        return a;
    }
    
    static {
        drops = ConcurrentHashMap.newKeySet();
        dropsslot = new ArrayList<Integer>();
    }
    
    private static class TemporaryDrop
    {
        public boolean enabled;
        public Item item;
        public int exp;
        
        public TemporaryDrop() {
        }
    }
}
