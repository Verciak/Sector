package xyz.rokkiitt.sector.objects.kits;

import cn.nukkit.item.Item;
import com.jsoniter.JsonIterator;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.kits.data.KitData;
import xyz.rokkiitt.sector.objects.pandora.Pandora;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedKitItem;
import xyz.rokkiitt.sector.packets.serializedObjects.SerializedPandoraItem;
import xyz.rokkiitt.sector.utils.ItemSerializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class KitManager
{
    private static final Set<KitData> player;
    private static final Set<KitData> vip;
    private static final Set<KitData> svip;
    private static final Set<KitData> sponsor;
    
    public static void load(final String type) {
        final String lowerCase = type.toLowerCase();
        switch (lowerCase) {
            case "player": {
                Main.getProvider().update("CREATE TABLE IF NOT EXISTS `playerkit` (" +
                        "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                        "`drop` varchar(9000) NOT NULL," +
                        "`item` varchar(9000) NOT NULL);");
                KitManager.player.clear();
                try {
                    ResultSet query = Main.getProvider().query("SELECT * FROM `playerkit`");
                    while (query.next()) {
                        SerializedKitItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedKitItem.class);

                        Item i = ItemSerializer.itemFromString(query.getString("item"));
                        KitManager.player.add(new KitData(i, drop.guislot, drop.amount));
                    }
                    query.close();
                    Main.getPlugin().getLogger().info("Loaded " + KitManager.player.size() + " items from 'playerkit'");
                } catch (SQLException ex) {
                    Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli playerkit");
                    ex.printStackTrace();
                }
                break;
            }
            case "vip": {
                Main.getProvider().update("CREATE TABLE IF NOT EXISTS `vipkit` (" +
                        "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                        "`drop` varchar(9000) NOT NULL," +
                        "`item` varchar(9000) NOT NULL);");
                KitManager.vip.clear();
                try {
                    ResultSet query = Main.getProvider().query("SELECT * FROM `vipkit`");
                    while (query.next()) {
                        SerializedKitItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedKitItem.class);

                        Item i = ItemSerializer.itemFromString(query.getString("item"));
                        KitManager.vip.add(new KitData(i, drop.guislot, drop.amount));
                    }
                    query.close();
                    Main.getPlugin().getLogger().info("Loaded " + KitManager.vip.size() + " items from 'vipkit'");
                } catch (SQLException ex) {
                    Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli vipkit");
                    ex.printStackTrace();
                }
                break;
            }
            case "svip": {
                Main.getProvider().update("CREATE TABLE IF NOT EXISTS `svipkit` (" +
                        "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                        "`drop` varchar(9000) NOT NULL," +
                        "`item` varchar(9000) NOT NULL);");
                KitManager.svip.clear();
                try {
                    ResultSet query = Main.getProvider().query("SELECT * FROM `svipkit`");
                    while (query.next()) {
                        SerializedKitItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedKitItem.class);

                        Item i = ItemSerializer.itemFromString(query.getString("item"));
                        KitManager.svip.add(new KitData(i, drop.guislot, drop.amount));
                    }
                    query.close();
                    Main.getPlugin().getLogger().info("Loaded " + KitManager.svip.size() + " items from 'svipkit'");
                } catch (SQLException ex) {
                    Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli svipkit");
                    ex.printStackTrace();
                }
                break;
            }
            case "sponsor": {
                Main.getProvider().update("CREATE TABLE IF NOT EXISTS `sponsorkit` (" +
                        "`id` int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                        "`drop` varchar(9000) NOT NULL," +
                        "`item` varchar(9000) NOT NULL);");
                KitManager.sponsor.clear();
                try {
                    ResultSet query = Main.getProvider().query("SELECT * FROM `sponsorkit`");
                    while (query.next()) {
                        SerializedKitItem drop = JsonIterator.deserialize(query.getString("drop"), SerializedKitItem.class);

                        Item i = ItemSerializer.itemFromString(query.getString("item"));
                        KitManager.sponsor.add(new KitData(i, drop.guislot, drop.amount));
                    }
                    query.close();
                    Main.getPlugin().getLogger().info("Loaded " + KitManager.sponsor.size() + " items from 'sponsorkit'");
                } catch (SQLException ex) {
                    Main.getPlugin().getLogger().info("Nie mozna zaladowac tabeli sponsorkit");
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
    
    public static Set<KitData> getPlayer() {
        return KitManager.player;
    }
    
    public static Set<KitData> getVip() {
        return KitManager.vip;
    }
    
    public static Set<KitData> getSvip() {
        return KitManager.svip;
    }
    
    public static Set<KitData> getSponsor() {
        return KitManager.sponsor;
    }
    
    static {
        player = ConcurrentHashMap.newKeySet();
        vip = ConcurrentHashMap.newKeySet();
        svip = ConcurrentHashMap.newKeySet();
        sponsor = ConcurrentHashMap.newKeySet();
    }
}
