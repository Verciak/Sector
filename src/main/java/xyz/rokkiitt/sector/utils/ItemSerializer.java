package xyz.rokkiitt.sector.utils;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Location;
import com.google.common.io.ByteStreams;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

public class ItemSerializer {

    public static String itemToString(Item item) {
        return item.getId() + ":" +
                item.getDamage() + ":" +
                item.getCount() + ":" +
                (item.hasCompoundTag() ? bytesToBase64(item.getCompoundTag()) : "not");
    }

    public static Item itemFromString(String itemString) throws NumberFormatException {
        String[] info = itemString.split(":");
        Item item = Item.get(
                Integer.parseInt(info[0]),
                Integer.parseInt(info[1]),
                Integer.parseInt(info[2])
        );

        if(!info[3].equals("not")) item.setCompoundTag(base64ToBytes(info[3]));

        return item;
    }
    private static String bytesToBase64(byte[] bytes) {
        if(bytes == null || bytes.length <= 0) return "not";

        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] base64ToBytes(String hexString) {
        if(hexString == null || hexString.equals("")) return null;

        return Base64.getDecoder().decode(hexString);
    }



    public static Location getLocation(String s) {
        SerializedLocation l = JsonIterator.deserialize(s, SerializedLocation.class);
        return new Location(l.x, l.y, l.z, l.yaw, l.pitch, Server.getInstance().getLevelByName(l.world));
    }

    public static String serializeLocation(Location l) {
        SerializedLocation k = new SerializedLocation();
        k.x = l.getX();
        k.y = l.getY();
        k.z = l.getZ();
        k.yaw = l.getYaw();
        k.pitch = l.getPitch();
        k.world = l.getLevel().getName();
        return JsonStream.serialize(k);
    }

    public static Location getLocationSector(String sectorType, Location l, String s) {
        if (sectorType.equalsIgnoreCase("spawn")) {
            switch (s) {
                case "west":
                    l = new Location((l.getX() > 0.0D) ? (l.getX() - 3.0D) : (l.getX() + 3.0D), l.getY(), l.getZ() + 8.0D, l.getYaw(), l.getPitch(), l.getLevel());
                    break;
                case "east":
                    l = new Location((l.getX() > 0.0D) ? (l.getX() - 3.0D) : (l.getX() + 3.0D), l.getY(), l.getZ() - 8.0D, l.getYaw(), l.getPitch(), l.getLevel());
                    break;
                case "north":
                    l = new Location(l.getX() - 8.0D, l.getY(), (l.getZ() > 0.0D) ? (l.getZ() - 3.0D) : (l.getZ() + 3.0D), l.getYaw(), l.getPitch(), l.getLevel());
                    break;
                case "south":
                    l = new Location(l.getX() + 8.0D, l.getY(), (l.getZ() > 0.0D) ? (l.getZ() - 3.0D) : (l.getZ() + 3.0D), l.getYaw(), l.getPitch(), l.getLevel());
                    break;
            }
        } else {
            switch (s) {
                case "west":
                    l = new Location(l.getX(), l.getY(), l.getZ() + 8.0D, l.getYaw(), l.getPitch(), l.getLevel());
                    break;
                case "east":
                    l = new Location(l.getX(), l.getY(), l.getZ() - 8.0D, l.getYaw(), l.getPitch(), l.getLevel());
                    break;
                case "north":
                    l = new Location(l.getX() - 8.0D, l.getY(), l.getZ(), l.getYaw(), l.getPitch(), l.getLevel());
                    break;
                case "south":
                    l = new Location(l.getX() + 8.0D, l.getY(), l.getZ(), l.getYaw(), l.getPitch(), l.getLevel());
                    break;
            }
        }
        return l;
    }

    public static Location getLocationSectorToKnock(Location l, String s) {
        switch (s) {
            case "west":
                l = new Location(l.getX(), l.getY(), l.getZ() - 1.0D, l.getYaw(), l.getPitch(), l.getLevel());
                break;
            case "east":
                l = new Location(l.getX(), l.getY(), l.getZ() + 1.0D, l.getYaw(), l.getPitch(), l.getLevel());
                break;
            case "north":
                l = new Location(l.getX() + 1.0D, l.getY(), l.getZ(), l.getYaw(), l.getPitch(), l.getLevel());
                break;
            case "south":
                l = new Location(l.getX() - 1.0D, l.getY(), l.getZ(), l.getYaw(), l.getPitch(), l.getLevel());
                break;
        }
        return l;
    }

    public static Item getItemFromSerializedItem(SerializedItem serializedItem) {
        Item item = Item.get(serializedItem.getId(), serializedItem.getMeta(), serializedItem.getCount());
        if (!serializedItem.customName.equalsIgnoreCase("brak"))
            item.setCustomName(serializedItem.getCustomName());
        if (!serializedItem.tags.equalsIgnoreCase("brak")) {
            byte[] b = (byte[])JsonIterator.deserialize(serializedItem.tags, byte[].class);
            item.setCompoundTag(b);
        }
        if (!serializedItem.lore.equalsIgnoreCase("brak")) {
            String[] b = (String[])JsonIterator.deserialize(serializedItem.lore, String[].class);
            item.setLore(b);
        }
        for (String enchantmentID : serializedItem.getEnchantments()) {
            String[] v = enchantmentID.split(Pattern.quote("::"));
            Enchantment enchantment = Enchantment.getEnchantment(Integer.parseInt(v[0]));
            enchantment.setLevel(Integer.parseInt(v[1]), false);
            item.addEnchantment(enchantment);
        }
        return item;
    }

    public static String[] getLoreFromSerializedItem(String v) {
        SerializedItemLore serializedItem = JsonIterator.deserialize(v, SerializedItemLore.class);
        String[] b = new String[0];
        if (!serializedItem.lore.equalsIgnoreCase("brak")) {
             b = (String[])JsonIterator.deserialize(serializedItem.lore, String[].class);
        }
        return b;
    }

    public static Item getItemFromSerializedItem(String v) {
        SerializedItem serializedItem = (SerializedItem)JsonIterator.deserialize(v, SerializedItem.class);
        Item item = Item.get(serializedItem.getId(), serializedItem.getMeta(), serializedItem.getCount());
        if (!serializedItem.customName.equalsIgnoreCase("brak"))
            item.setCustomName(serializedItem.getCustomName());
        if (!serializedItem.tags.equalsIgnoreCase("brak")) {
            byte[] b = (byte[])JsonIterator.deserialize(serializedItem.tags, byte[].class);
            item.setCompoundTag(b);
        }
//        if (!serializedItem.lore.equalsIgnoreCase("brak")) {
//            String[] b = (String[])JsonIterator.deserialize(serializedItem.lore, String[].class);
//            item.setLore(b);
//        }
        for (String enchantmentID : serializedItem.getEnchantments()) {
            String[] vv = enchantmentID.split(Pattern.quote("::"));
            Enchantment enchantment = Enchantment.getEnchantment(Integer.parseInt(vv[0]));
            enchantment.setLevel(Integer.parseInt(vv[1]), false);
            item.addEnchantment(enchantment);
        }
        return item;
    }
    public static String getSerializedItemJsonFromLore(Item serializedItem) {
        SerializedItemLore item = new SerializedItemLore();
        item.lore = ((serializedItem.getLore()).length == 0) ? "brak" : JsonStream.serialize(serializedItem.getLore()).replace(serializedItem.getLore().toString().substring(0,0), "").replace((char) serializedItem.getLore().length, (char) serializedItem.getLore().length);
        return JsonStream.serialize(item);
    }

    public static String getSerializedItemJsonFromItem(Item serializedItem) {
        SerializedItem item = new SerializedItem();
        item.id = serializedItem.getId();
        item.meta = serializedItem.getDamage();
        item.count = serializedItem.getCount();
        item.durability = serializedItem.getMaxDurability();
//        item.lore = ((serializedItem.getLore()).length == 0) ? "brak" : JsonStream.serialize(serializedItem.getLore());
        item.customName = serializedItem.getCustomName().isEmpty() ? "brak" : serializedItem.getCustomName();
        item.tags = ((serializedItem.getCompoundTag()).length == 0) ? "brak" : JsonStream.serialize(serializedItem.getCompoundTag());
        item.enchantments = new ArrayList<>();
        item.slot = -1;
        for (Enchantment enchantment : serializedItem.getEnchantments())
            item.enchantments.add(enchantment.getId() + "::" + enchantment.getLevel());
        return JsonStream.serialize(item);
    }

    private static String getSerializedItemJsonFromItem(Item serializedItem, int s) {
        SerializedItem item = new SerializedItem();
        item.id = serializedItem.getId();
        item.meta = serializedItem.getDamage();
        item.count = serializedItem.getCount();
        item.durability = serializedItem.getMaxDurability();
        item.lore = ((serializedItem.getLore()).length == 0) ? "brak" : JsonStream.serialize(serializedItem.getLore());
        item.customName = serializedItem.getCustomName().isEmpty() ? "brak" : serializedItem.getCustomName();
        item.tags = ((serializedItem.getCompoundTag()).length == 0) ? "brak" : JsonStream.serialize(serializedItem.getCompoundTag());
        item.enchantments = new ArrayList<>();
        for (Enchantment enchantment : serializedItem.getEnchantments())
            item.enchantments.add(enchantment.getId() + "::" + enchantment.getLevel());
        item.slot = s;
        return JsonStream.serialize(item);
    }

    public static String getStringFromItemMap(Map<Integer, Item> s) {
        List<String> it = new ArrayList<>();
        for (Map.Entry<Integer, Item> entry : s.entrySet())
            it.add(getSerializedItemJsonFromItem(entry.getValue(), (Integer) entry.getKey()));
        return JsonStream.serialize(it);
    }

    public static Map<Integer, Item> getItemMapFromString(String s) {
        Map<Integer, Item> n = new HashMap<>();
        if (s == null || s.isEmpty())
            return n;
        List<String> x = (List<String>)JsonIterator.deserialize(s, List.class);
        for (String v : x) {
            SerializedItem h = (SerializedItem)JsonIterator.deserialize(v, SerializedItem.class);
            n.put(h.slot, getItemFromSerializedItem(h));
        }
        return n;
    }
}

