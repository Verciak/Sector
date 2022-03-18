package xyz.rokkiitt.sector.objects.meteorite;

import com.jsoniter.JsonIterator;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.pandora.Pandora;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedPandoraItem;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.SpaceUtil;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.item.*;
import cn.nukkit.level.*;
import cn.nukkit.block.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import cn.nukkit.math.*;

import java.util.concurrent.*;

public class MeteoriteManager
{
    private static MeteoriteRegion mr;
    private static Set<HashMap<String, Integer>> meteblocks;
    private static boolean isRunning;
    private static boolean wasThrown;
    private static boolean isOpened;
    private static int countdown;
    private static final Set<MeteoriteDrop> drops;
    static Random rand;
    
    public static void load() {
        Main.getProvider().update("CREATE TABLE IF NOT EXISTS `meteordrops` (" +
                "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                "`drop` varchar(9000) NOT NULL," +
                "`item` varchar(9000) NOT NULL);");
        MeteoriteManager.drops.clear();
        try {
            ResultSet query = Main.getProvider().query("SELECT * FROM `meteordrops`");
            while (query.next()) {
                SerializedPandoraItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedPandoraItem.class);

                Item i = ItemSerializer.itemFromString(query.getString("item"));
                drops.add(new MeteoriteDrop(i, drop.guislot, drop.minAmount, drop.maxAmount));
            }
            query.close();
            Main.getPlugin().getLogger().info("Loaded " + drops.size() + " drops from 'meteordrops'");
        } catch (SQLException ex) {
            Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli meteordrops");
            ex.printStackTrace();
        }
    }
    
    public static Set<MeteoriteDrop> getDrops() {
        return MeteoriteManager.drops;
    }
    
    public static void getDrop(final Player p) {
        final List<Item> drop = new ArrayList<Item>();
        final List<MeteoriteDrop> dropy = new ArrayList<MeteoriteDrop>(MeteoriteManager.drops);
        if (dropy.size() == 1) {
            final MeteoriteDrop d = dropy.get(0);
            final int a = (d.getMinAmount() == d.getMaxAmount()) ? d.getMinAmount() : RandomUtil.getRandInt(d.getMinAmount(), d.getMaxAmount());
            final Item itemDrop = d.getWhat().clone();
            itemDrop.setCount(a);
            drop.add(itemDrop);
        }
        else if (dropy.size() > 1) {
            Collections.shuffle(dropy);
            final MeteoriteDrop d = dropy.get(RandomUtil.getRandInt(0, dropy.size() - 1));
            final int a = (d.getMinAmount() == d.getMaxAmount()) ? d.getMinAmount() : RandomUtil.getRandInt(d.getMinAmount(), d.getMaxAmount());
            final Item itemDrop = d.getWhat().clone();
            itemDrop.setCount(a);
            drop.add(itemDrop);
        }
        drop.add(PItemsGUI.antynogi);
        final Item s = PItemsGUI.rzucane.clone();
        s.setCount(RandomUtil.getRandInt(1, 3));
        drop.add(s);
        Util.giveItem(p, drop);
    }
    
    public static void removeCountDown() {
        --MeteoriteManager.countdown;
    }
    
    public static void setCountDown(final int t) {
        MeteoriteManager.countdown = t;
    }
    
    public static int getCountDown() {
        return MeteoriteManager.countdown;
    }
    
    public static boolean isOpened() {
        return MeteoriteManager.isOpened;
    }
    
    public static boolean isOpenedListener(final Location l) {
        return MeteoriteManager.mr != null && MeteoriteManager.mr.isInCuboid(l);
    }
    
    public static void setOpened(final boolean t) {
        MeteoriteManager.isOpened = t;
    }
    
    public static void chceckForRepairs() {
        if (MeteoriteManager.mr != null) {
            if (!MeteoriteManager.meteblocks.isEmpty() && MeteoriteManager.meteblocks.iterator().hasNext()) {
                final HashMap<String, Integer> b = MeteoriteManager.meteblocks.iterator().next();
                MeteoriteManager.mr.getWorld().setBlockAt((int)b.get("x"), (int)b.get("y"), (int)b.get("z"), (int)b.get("id"), (int)b.get("meta"));
                MeteoriteManager.meteblocks.remove(b);
            }
            else {
                MeteoriteManager.mr = null;
            }
        }
    }
    
    public static void setThrown(final boolean t) {
        MeteoriteManager.wasThrown = t;
    }
    
    public static boolean wasThrown() {
        return MeteoriteManager.wasThrown;
    }
    
    public static void setRunning(final boolean t) {
        MeteoriteManager.isRunning = t;
    }
    
    public static boolean isRunning() {
        return MeteoriteManager.isRunning;
    }
    
    public static MeteoriteRegion getRegion() {
        return MeteoriteManager.mr;
    }
    
    public static void summon(final Level w) {
        for (int i = 0; i < 150; ++i) {
            final Location l = getLocation(w);
            if (l != null) {
                l.add(0.0, 1.0, 0.0);
                final List<HashMap<String, Integer>> mblocks = new ArrayList<HashMap<String, Integer>>();
                MeteoriteManager.mr = new MeteoriteRegion(l.getLevel(), l.getFloorX(), l.getFloorY(), l.getFloorZ());
                setRunning(true);
                MeteoriteManager.countdown = 300;
                setOpened(false);
                final Block bbb = SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 4, l.getFloorZ());
                if (bbb.getId() != 0) {
                    final HashMap<String, Integer> id = new HashMap<String, Integer>();
                    id.put("id", bbb.getId());
                    id.put("meta", bbb.getDamage());
                    id.put("x", bbb.getLocation().getFloorX());
                    id.put("y", bbb.getLocation().getFloorY());
                    id.put("z", bbb.getLocation().getFloorZ());
                    mblocks.add(id);
                }
                SpaceUtil.setBlock(MeteoriteManager.mr.getWorld(), 49, 0, l.getFloorX(), l.getFloorY() - 4, l.getFloorZ());
                final List<Block> b = new ArrayList<Block>();
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY(), l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 4, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 5, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() - 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() - 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() - 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() - 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() - 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() - 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() - 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY(), l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY(), l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY(), l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY(), l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY(), l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY(), l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY(), l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY(), l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY(), l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY(), l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY(), l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY(), l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY(), l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY(), l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY(), l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY(), l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY(), l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY(), l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY(), l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY(), l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY(), l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY(), l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY(), l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY(), l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY(), l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY(), l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY(), l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY(), l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY(), l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY(), l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY(), l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY(), l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY(), l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY(), l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY(), l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY(), l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY(), l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY(), l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY(), l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY(), l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY(), l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY(), l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY(), l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY(), l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 1, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 1, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 1, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 1, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 1, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 1, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 1, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 2, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 2, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 2, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 2, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 2, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 2, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 2, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 3, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 3, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 3, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 3, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 3, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 3, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 3, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 3, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 3, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 3, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 3, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 3, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 3, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 3, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 3, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 4, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 4, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 4, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 4, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 4, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 4, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 4, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 4, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 4, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 4, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 4, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 4, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 4, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 4, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 4, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 4, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 4, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 4, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 4, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 4, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 4, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 4, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 4, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 4, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 4, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 4, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 4, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 4, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 4, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 4, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 4, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 4, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 4, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 4, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 4, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 4, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 4, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 4, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 4, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 4, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 4, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 4, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 4, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 4, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 5, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 5, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 5, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 5, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 5, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 5, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 1, l.getFloorY() + 5, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 5, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 5, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 5, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 5, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 5, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 5, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 2, l.getFloorY() + 5, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 5, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 5, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 5, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 5, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() + 3, l.getFloorY() + 5, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 5, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 5, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 5, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 5, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 5, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX(), l.getFloorY() + 5, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 5, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 5, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 5, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 5, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 5, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 5, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 1, l.getFloorY() + 5, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 5, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 5, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 5, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 5, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 5, l.getFloorZ() + 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 5, l.getFloorZ() - 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 2, l.getFloorY() + 5, l.getFloorZ() + 3));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 5, l.getFloorZ()));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 5, l.getFloorZ() - 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 5, l.getFloorZ() + 1));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 5, l.getFloorZ() - 2));
                b.add(SpaceUtil.getBlock(l, l.getFloorX() - 3, l.getFloorY() + 5, l.getFloorZ() + 2));
                for (final Block bs : b) {
                    if (bs.getId() != 0) {
                        final HashMap<String, Integer> id2 = new HashMap<String, Integer>();
                        id2.put("id", bs.getId());
                        id2.put("meta", bs.getDamage());
                        id2.put("x", bs.getLocation().getFloorX());
                        id2.put("y", bs.getLocation().getFloorY());
                        id2.put("z", bs.getLocation().getFloorZ());
                        mblocks.add(id2);
                    }
                    bs.getLevel().setBlockAt(bs.getLocation().getFloorX(), bs.getLocation().getFloorY(), bs.getLocation().getFloorZ(), 0);
                }
                mblocks.sort(Comparator.comparing(m -> m.get("z")));
                mblocks.sort(Comparator.comparing(m -> m.get("x")));
                MeteoriteManager.meteblocks.clear();
                MeteoriteManager.meteblocks.addAll(mblocks);
                return;
            }
        }
    }
    
    public static void reset() {
        setRunning(false);
        setThrown(false);
    }
    
    public static void forceCancel() {
        setRunning(false);
        setThrown(true);
        setOpened(true);
        if (!MeteoriteManager.meteblocks.isEmpty()) {
            for (final HashMap<String, Integer> b : MeteoriteManager.meteblocks) {
                final Block bbb = Block.get((int)b.get("id"));
                bbb.setDamage(Integer.valueOf(b.get("meta")));
                MeteoriteManager.mr.getWorld().setBlock((Vector3)new Location((double)b.get("x"), (double)b.get("y"), (double)b.get("z"), MeteoriteManager.mr.getWorld()), bbb);
            }
        }
        MeteoriteManager.meteblocks.clear();
        MeteoriteManager.mr = null;
    }
    
    private static Location getLocation(final Level w) {
//        final int x1 = Math.min(SectorManager.firstCorner.getFloorX(), SectorManager.secondCorner.getFloorX());
//        final int x2 = Math.max(SectorManager.firstCorner.getFloorX(), SectorManager.secondCorner.getFloorX());
//        final int y1 = Math.min(SectorManager.firstCorner.getFloorZ(), SectorManager.secondCorner.getFloorZ());
//        final int y2 = Math.max(SectorManager.firstCorner.getFloorZ(), SectorManager.secondCorner.getFloorZ());
//        final int distanceX = Math.abs(x1 - x2);
//        final int distanceY = Math.abs(y1 - y2);
//        final int offsetX = (x1 + x2) / 2;
//        final int offsetY = (y1 + y2) / 2;
//        final int randX = MeteoriteManager.rand.nextInt(distanceX) - distanceX / 2 + offsetX;
//        final int randY = MeteoriteManager.rand.nextInt(distanceY) - distanceY / 2 + offsetY;
//        final Location loc = Util.getHighestLocation(randX, randY);
//        if (GuildManager.canTeleportByGuild(loc) && GuildManager.canTeleportByBorder(loc) && loc.getFloorY() > 5 && (SectorManager.isInSector(randX, randY) || SectorManager.isInSquareSector(randX, randY))) {
//            return loc;
//        }
//        return null;
        return null;
    }
    
    static {
        MeteoriteManager.mr = null;
        MeteoriteManager.meteblocks = ConcurrentHashMap.newKeySet();
        drops = ConcurrentHashMap.newKeySet();
        MeteoriteManager.rand = new Random();
    }
}
