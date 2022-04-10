package xyz.rokkiitt.sector.utils;

import cn.nukkit.entity.Entity;
import org.apache.commons.lang3.StringUtils;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.wings.Wings;
import xyz.rokkiitt.sector.packets.PacketBackupCreate;
import xyz.rokkiitt.sector.packets.PacketBanCommand;
import xyz.rokkiitt.sector.packets.PacketLog;
import xyz.rokkiitt.sector.packets.PacketPlayerKick;
import cn.nukkit.item.*;
import cn.nukkit.block.*;
import cn.nukkit.level.format.generic.*;
import java.io.*;

import cn.nukkit.*;
import java.util.*;
import java.math.*;

import cn.nukkit.level.*;
import cn.nukkit.math.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.command.*;
import cn.nukkit.utils.*;

public class Util
{
    private static String[] names;
    
    public static String getRandomNickname() {
        return Util.names[RandomUtil.getRandInt(0, Util.names.length - 1)] + RandomUtil.getRandInt(100, 10000000);
    }
    
    public static void banAC(final Player p, final String time, final String server) {
        final PacketBanCommand pa = new PacketBanCommand();
        pa.sender = "Antycheat";
        pa.sendersector = "";
        pa.reason = "";
        pa.receiver = p.getName();
        pa.receiversector = "";
        pa.time = time;
        pa.deviceId = "";
        pa.isCheck = true;
        pa.banreason = "";
        pa.server = server;
//        Main.getNats().publish("command", "ban||" + JsonStream.serialize(pa));
    }
    
    public static void createBackup(final Player p, final String killer, final int points) {
        final PacketBackupCreate pa = new PacketBackupCreate();
        pa.player = p.getName();
        pa.killer = ((killer == null) ? "Brak" : killer);
//        pa.inventory = ItemSerializer.getStringFromItemMap(p.getInventory().getContents());
        pa.points = points;
//        Main.getNats().publish("hubguildpanel", "backupcreate||" + JsonStream.serialize(pa));
    }
    
    public static Block getBlock(final Level level, final int x, final int y, final int z, final boolean load) {
        int fullState;
        if (y >= 0 && y < 256) {
            final int cx = x >> 4;
            final int cz = z >> 4;
            BaseFullChunk chunk;
            if (load) {
                chunk = level.getChunk(cx, cz);
            }
            else {
                chunk = level.getChunkIfLoaded(cx, cz);
            }
            if (chunk != null) {
                fullState = chunk.getFullBlock(x & 0xF, y, z & 0xF);
            }
            else {
                fullState = 0;
            }
        }
        else {
            fullState = 0;
        }
        final Block block = Block.fullList[fullState & 0xFFF].clone();
        block.x = x;
        block.y = y;
        block.z = z;
        block.level = level;
        return block;
    }
    
    public static double round(final double d) {
        return Math.round(d * 100.0) / 100.0;
    }
    
    public static String StringToString(final String id) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(id);
            oos.flush();
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public static String StringFromString(final String s) {
        try {
            final ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(s));
            final ObjectInputStream ois = new ObjectInputStream(bis);
            return (String)ois.readObject();
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static boolean sendLog(final String who, final String reason) {
        final PacketLog pa = new PacketLog();
        pa.who = who;
        pa.message = reason;
//        Main.getNats().publish("hublog", JsonStream.serialize(pa));
        return false;
    }
    
    public static boolean sendChatLog(final String reason) {
//        Main.getNats().publish("discordlog", reason);
        return false;
    }
    
    public static boolean kickPlayer(final Player p, final String reason) {
        final PacketPlayerKick pa = new PacketPlayerKick();
        pa.player = p.getName().toLowerCase();
        pa.reason = fixColor(reason);
//        Main.getNats().publish("playerkick", JsonStream.serialize(pa));
        return false;
    }
    
    public static Player matchPlayer(final String s) {
        final Player[] p = Server.getInstance().matchPlayer(s);
        if (p.length > 0) {
            return p[0];
        }
        return null;
    }
    
    public static String matchPlayer(final Collection<String> names, String s) {
        s = s.toLowerCase();
        final List<String> matchedPlayer = new ArrayList<String>();
        for (final String players : names) {
            if (players.toLowerCase().equals(s)) {
                return players;
            }
            if (!players.toLowerCase().contains(s)) {
                continue;
            }
            matchedPlayer.add(players);
        }
        if (!matchedPlayer.isEmpty()) {
            return matchedPlayer.get(0);
        }
        return s;
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float round(final float value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
    
    public static int getItemTypeCountByNBT(final Player p, final String nbt) {
        int count = 0;
        for (final Item i : p.getInventory().getContents().values()) {
            if (hasNBTTag(i, nbt)) {
                count += i.getCount();
            }
        }
        return count;
    }
    
    public static int getItemTypeCount(final Player p, final int id) {
        int count = 0;
        for (final Item i : p.getInventory().getContents().values()) {
            if (i.getId() == id) {
                count += i.getCount();
            }
        }
        return count;
    }

    public static Item[] removeItemByNBTwithId(Player p, String taggg, Item... slots) {
        List<Item> itemSlots = new ArrayList<>();
        Item[] var3 = slots;
        int var4 = slots.length;
        for (int var5 = 0; var5 < var4; var5++) {
            Item slot = var3[var5];
            if (slot.getId() != 0 && slot.getCount() > 0)
                itemSlots.add(slot.clone());
        }
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            Item item = p.getInventory().getItem(i);
            if (item.getId() != 0 && item.getCount() > 0) {
                Iterator<?> var10 = (new ArrayList(itemSlots)).iterator();
                while (var10.hasNext()) {
                    Item slot = (Item)var10.next();
                    CompoundTag tag = slot.getNamedTag();
                    if (tag != null && tag.get(taggg) != null && slot.getId() == item.getId()) {
                        int amount = Math.min(item.getCount(), slot.getCount());
                        slot.setCount(slot.getCount() - amount);
                        item.setCount(item.getCount() - amount);
                        p.getInventory().setItem(i, item);
                        if (slot.getCount() <= 0)
                            itemSlots.remove(slot);
                    }
                }
                if (itemSlots.size() == 0)
                    break;
            }
        }
        return itemSlots.<Item>toArray(new Item[0]);
    }

    public static Item[] removeItemById(Player p, Item... slots) {
        List<Item> itemSlots = new ArrayList<>();
        Item[] var3 = slots;
        int var4 = slots.length;
        for (int var5 = 0; var5 < var4; var5++) {
            Item slot = var3[var5];
            if (slot.getId() != 0 && slot.getCount() > 0)
                itemSlots.add(slot.clone());
        }
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            Item item = p.getInventory().getItem(i);
            if (item.getId() != 0 && item.getCount() > 0) {
                Iterator<?> var10 = (new ArrayList(itemSlots)).iterator();
                while (var10.hasNext()) {
                    Item slot = (Item)var10.next();
                    if (slot.getId() == item.getId()) {
                        int amount = Math.min(item.getCount(), slot.getCount());
                        slot.setCount(slot.getCount() - amount);
                        item.setCount(item.getCount() - amount);
                        p.getInventory().setItem(i, item);
                        if (slot.getCount() <= 0)
                            itemSlots.remove(slot);
                    }
                }
                if (itemSlots.size() == 0)
                    break;
            }
        }
        return itemSlots.<Item>toArray(new Item[0]);
    }
    
    public static void changeNametag(final Player p) {
        p.setNameTag(p.getName() + RandomUtil.getRandInt(1, 10000));
    }
    
    public static void setIncognito(final User u, final Player p) {
        changeNametag(p);
        if (u.isIncognito()) {
            p.setSkin(Wings.applyIncognito(u.getSkin()));
        }
        else if (!u.getWings().isEmpty() && Wings.isExists(u.getWings())) {
            p.setSkin(Wings.apply(u.getSkin(), u.getWings()));
        }
        else {
            p.setSkin(u.getSkin());
        }
    }
    
    public static String getChatColor(final String p, final User g) {
        if (!g.getTag().equalsIgnoreCase("NIEPOSIADA") && !p.equalsIgnoreCase("NIEPOSIADA")) {
            if (p.equalsIgnoreCase(g.getTag())) {
                return "&a";
            }
            if (g.getAlliances().contains(p.toLowerCase())) {
                return "&b";
            }
        }
        return "&c";
    }
    
    public static String getChatColor(final User p, final User g) {
        if (!g.getTag().equalsIgnoreCase("NIEPOSIADA") || !p.getTag().equalsIgnoreCase("NIEPOSIADA")) {
            if (p.getTag().equalsIgnoreCase(g.getTag())) {
                return "&a";
            }
            if (p.getAlliances().contains(g.getTag())) {
                return "&b";
            }
        }
        return "&c";
    }


    public static List<Player> getPlayersInRadius(final Entity b, final Location location, final int size) {
        final List<Player> players = new ArrayList<Player>();
        for (final Player p : location.getLevel().getPlayers().values()) {
            if (b.distance(p.getLocation()) <= size) {
                players.add(p);
            }
        }
        return players;
    }
    
    public static List<Player> getNearbyPlayers(final Location l, final int distance) {
        final List<Player> nearPlayers = new ArrayList<Player>();
        for (final Player all : Server.getInstance().getOnlinePlayers().values()) {
            if (all.getPosition().distance(new Vector3(l.getX(), l.getY(), l.getZ())) < distance) {
                nearPlayers.add(all);
            }
        }
        return nearPlayers;
    }
    
    public static List<Player> getNearbyPlayers(final Player p, final int distance) {
        final List<Player> nearPlayers = new ArrayList<Player>();
        for (final Player all : Server.getInstance().getOnlinePlayers().values()) {
            if (all.getPosition().distance(new Vector3(p.getX(), p.getY(), p.getZ())) < distance) {
                nearPlayers.add(all);
            }
        }
        return nearPlayers;
    }
    
    public static Location getHighestLocation(final int x, final int z) {
        final Level level = Server.getInstance().getDefaultLevel();
        for (int y = 255; y >= 0; --y) {
            if (level.getBlock(x, y, z, true).getId() != 0) {
                return new Location((double)x, (double)y, (double)z, level);
            }
        }
        return new Location((double)x, 110.0, (double)z, level);
    }
    
    public static Location getHighestLocation(final Location l) {
        final Level level = Server.getInstance().getDefaultLevel();
        for (int y = 255; y >= 0; --y) {
            if (level.getBlock(l.getFloorX(), y, l.getFloorZ(), true).getId() != 0) {
                l.setComponents(l.getX(), (double)y, l.getZ());
                return l;
            }
        }
        l.setComponents(l.getX(), 110.0, l.getZ());
        return l;
    }
    
    public static Item setupNBTItem(final int id, final int am, final int b, final String keynbt, final boolean hidenc, final String name, final String[] lore) {
        final Item pan = addNBTTag(Item.get(id, Integer.valueOf(b), am), keynbt);
        if (hidenc) {
            CompoundTag tag = pan.getNamedTag();
            if (tag == null) {
                tag = new CompoundTag();
            }
            tag.putList(new ListTag("ench"));
            pan.setNamedTag(tag);
        }
        if (!name.isEmpty()) {
            pan.setCustomName(fixColor(name));
        }
        if (lore.length > 0) {
            pan.setLore(fixColor(lore));
        }
        return pan;
    }
    
    public static Item addNBTTagWithValue(final Item i, final String key, final String value) {
        CompoundTag tag = i.getNamedTag();
        if (tag == null) {
            tag = new CompoundTag();
        }
        if (tag.contains(key)) {
            tag.remove(key);
        }
        tag.putString(key, value);
        return i.setNamedTag(tag);
    }
    
    public static String getNBTTagValue(final Item i, final String key) {
        final CompoundTag tag = i.getNamedTag();
        if (tag != null) {
            return tag.getString(key);
        }
        return "";
    }
    
    public static Item addNBTTag(final Item i, final String key) {
        CompoundTag tag = i.getNamedTag();
        if (tag == null) {
            tag = new CompoundTag();
        }
        tag.put(key, (Tag)new CompoundTag());
        return i.setNamedTag(tag);
    }
    
    public static Item removeNBTTag(final Item i, final String key) {
        CompoundTag tag = i.getNamedTag();
        if (tag == null) {
            tag = new CompoundTag();
            return i.setNamedTag(tag);
        }
        tag.remove(key);
        return i.setNamedTag(tag);
    }
    
    public static boolean hasNBTTag(final Item i, final String key) {
        final CompoundTag tag = i.getNamedTag();
        return tag != null && tag.get(key) != null;
    }
    
    public static int getPanelInventory(final int s) {
        switch (s) {
            case 10: {
                return 0;
            }
            case 11: {
                return 1;
            }
            case 12: {
                return 2;
            }
            case 13: {
                return 3;
            }
            case 14: {
                return 4;
            }
            case 15: {
                return 5;
            }
            case 16: {
                return 6;
            }
            case 19: {
                return 7;
            }
            case 20: {
                return 8;
            }
            case 21: {
                return 9;
            }
            case 22: {
                return 10;
            }
            case 23: {
                return 11;
            }
            case 24: {
                return 12;
            }
            case 25: {
                return 13;
            }
            case 28: {
                return 14;
            }
            case 29: {
                return 15;
            }
            case 30: {
                return 16;
            }
            case 31: {
                return 17;
            }
            case 32: {
                return 18;
            }
            case 33: {
                return 19;
            }
            case 34: {
                return 20;
            }
            case 37: {
                return 21;
            }
            case 38: {
                return 22;
            }
            case 39: {
                return 23;
            }
            case 40: {
                return 24;
            }
            case 41: {
                return 25;
            }
            case 42: {
                return 26;
            }
            case 43: {
                return 27;
            }
            default: {
                return 999;
            }
        }
    }
    
    public static int getSlotInventory(final int s) {
        switch (s) {
            case 0: {
                return 10;
            }
            case 1: {
                return 11;
            }
            case 2: {
                return 12;
            }
            case 3: {
                return 13;
            }
            case 4: {
                return 14;
            }
            case 5: {
                return 15;
            }
            case 6: {
                return 16;
            }
            case 7: {
                return 19;
            }
            case 8: {
                return 20;
            }
            case 9: {
                return 21;
            }
            case 10: {
                return 22;
            }
            case 11: {
                return 23;
            }
            case 12: {
                return 24;
            }
            case 13: {
                return 25;
            }
            case 14: {
                return 28;
            }
            case 15: {
                return 29;
            }
            case 16: {
                return 30;
            }
            case 17: {
                return 31;
            }
            case 18: {
                return 32;
            }
            case 19: {
                return 33;
            }
            case 20: {
                return 34;
            }
            case 21: {
                return 37;
            }
            case 22: {
                return 38;
            }
            case 23: {
                return 29;
            }
            case 24: {
                return 40;
            }
            case 25: {
                return 41;
            }
            case 26: {
                return 42;
            }
            case 27: {
                return 43;
            }
            default: {
                return 999;
            }
        }
    }
    
    public static boolean sendInformation(final String s) {
        for(Player p : Server.getInstance().getOnlinePlayers().values()){
            p.sendMessage(fixColor(s));
        }
        return false;
    }
    
    public static void removeItemsNBT(final Player p, final String s, int amount) {
        final Map<Integer, Item> contents = (Map<Integer, Item>)p.getInventory().getContents();
        for (int length = contents.size(), i = 0; i < length; ++i) {
            final Item itemStack = contents.get(i);
            if (amount > 0 && itemStack != null && hasNBTTag(itemStack, s)) {
                if (itemStack.getCount() > amount) {
                    itemStack.setCount(itemStack.getCount() - amount);
                    amount = 0;
                }
                else {
                    amount -= itemStack.getCount();
                    p.getInventory().removeItem(new Item[] { itemStack });
                }
            }
        }
    }
    
    public static void removeItems(final int material, final Player p, final int durability, int amount) {
        final Map<Integer, Item> contents = (Map<Integer, Item>)p.getInventory().getContents();
        for (int length = contents.size(), i = 0; i < length; ++i) {
            final Item itemStack = contents.get(i);
            if (amount > 0 && itemStack != null && itemStack.getId() == material && itemStack.getDamage() == durability) {
                if (itemStack.getCount() > amount) {
                    itemStack.setCount(itemStack.getCount() - amount);
                    amount = 0;
                }
                else {
                    amount -= itemStack.getCount();
                    p.getInventory().removeItem(new Item[] { itemStack });
                }
            }
        }
    }
    
    public static void giveItem(final Player p, final Collection<Item> t) {
        final List<Item> drops = new ArrayList<Item>();
        for (final Item item : t) {
            final Item[] addItem;
            final Item[] returns = addItem = p.getInventory().addItem(new Item[] { item.clone() });
            for (final Item returned : addItem) {
                final int maxStackSize = returned.getMaxStackSize();
                if (returned.getCount() <= maxStackSize) {
                    drops.add(returned);
                }
                else {
                    while (returned.getCount() > maxStackSize) {
                        final Item drop = returned.clone();
                        final int toDrop = Math.min(returned.getCount(), maxStackSize);
                        drop.setCount(toDrop);
                        returned.setCount(returned.getCount() - toDrop);
                        drops.add(drop);
                    }
                    if (!returned.isNull()) {
                        drops.add(returned);
                    }
                }
            }
        }
        for (final Item drop2 : drops) {
            p.dropItem(drop2);
        }
    }
    
    public static void giveItemOnDeath(final Player killer, final Player p, final Item... t) {
        final Item[] returns = killer.getInventory().addItem((Item[])t.clone());
        final List<Item> drops = new ArrayList<Item>();
        for (final Item returned : returns) {
            final int maxStackSize = returned.getMaxStackSize();
            if (returned.getCount() <= maxStackSize) {
                drops.add(returned);
            }
            else {
                while (returned.getCount() > maxStackSize) {
                    final Item drop = returned.clone();
                    final int toDrop = Math.min(returned.getCount(), maxStackSize);
                    drop.setCount(toDrop);
                    returned.setCount(returned.getCount() - toDrop);
                    drops.add(drop);
                }
                if (!returned.isNull()) {
                    drops.add(returned);
                }
            }
        }
        for (final Item drop2 : drops) {
            p.dropItem(drop2);
        }
    }
    
    public static void giveItem(final Player p, final Item... t) {
        final Item[] returns = p.getInventory().addItem((Item[])t.clone());
        final List<Item> drops = new ArrayList<Item>();
        for (final Item returned : returns) {
            final int maxStackSize = returned.getMaxStackSize();
            if (returned.getCount() <= maxStackSize) {
                drops.add(returned);
            }
            else {
                while (returned.getCount() > maxStackSize) {
                    final Item drop = returned.clone();
                    final int toDrop = Math.min(returned.getCount(), maxStackSize);
                    drop.setCount(toDrop);
                    returned.setCount(returned.getCount() - toDrop);
                    drops.add(drop);
                }
                if (!returned.isNull()) {
                    drops.add(returned);
                }
            }
        }
        for (final Item drop2 : drops) {
            p.dropItem(drop2);
        }
    }
    
    public static String formatTime(final long millis) {
        final int seconds = (int)(millis / 1000L) % 60;
        final int minutes = (int)(millis / 60000L % 60L);
        final int hours = (int)(millis / 3600000L % 24L);
        final int days = (int)(millis / 86400000L % 365L);
        final ArrayList<String> timeArray = new ArrayList<String>();
        if (days > 0) {
            timeArray.add(String.valueOf(days) + "d");
        }
        if (hours > 0) {
            timeArray.add(String.valueOf(hours) + "h");
        }
        if (minutes > 0) {
            timeArray.add(String.valueOf(minutes) + "min");
        }
        if (seconds > 0) {
            timeArray.add(String.valueOf(seconds) + "s");
        }
        String time = "";
        for (int i = 0; i < timeArray.size(); ++i) {
            time += timeArray.get(i);
            if (i != timeArray.size() - 1) {
                time += ", ";
            }
        }
        if (time == "") {
            time = "0s";
        }
        return time;
    }
    
    public static String formatDotTime(final long millis) {
        final int seconds = (int)(millis / 1000L) % 60;
        final int minutes = (int)(millis / 60000L % 60L);
        final int hours = (int)(millis / 3600000L % 24L);
        final int days = (int)(millis / 86400000L % 365L);
        final ArrayList<String> timeArray = new ArrayList<String>();
        if (days > 0) {
            timeArray.add(String.valueOf(days));
        }
        if (hours > 0) {
            timeArray.add(String.valueOf(hours));
        }
        if (minutes > 0) {
            timeArray.add(String.valueOf(minutes));
        }
        if (seconds > 0) {
            timeArray.add(String.valueOf(seconds));
        }
        String time = "";
        for (int i = 0; i < timeArray.size(); ++i) {
            time += timeArray.get(i);
            if (i != timeArray.size() - 1) {
                time += ":";
            }
        }
        if (time == "") {
            time = "0";
        }
        return time;
    }
    
    public static boolean sendTip(final Player sender, final Collection<String> messages) {
        sendTip(sender, StringUtils.join("\n", messages));
        return true;
    }
    
    public static boolean sendTip(final Collection<? extends Player> col, final Collection<String> messages) {
        sendTip(col, StringUtils.join("\n", messages));
        return true;
    }
    
    public static boolean sendTip(final Collection<? extends Player> col, final String message) {
        for (final Player cs : col) {
            sendTip(cs, message);
        }
        return true;
    }
    
    public static boolean sendTip(final Player sender, final String message) {
        sender.sendTip(fixColor(message));
        return true;
    }
    
    public static boolean sendMessage(final CommandSender sender, final Collection<String> messages) {
        sendMessage(sender, StringUtils.join("\n", messages));
        return true;
    }
    
    public static boolean sendMessage(final Collection<? extends CommandSender> col, final Collection<String> messages) {
        sendMessage(col, StringUtils.join("\n", messages));
        return true;
    }
    
    public static boolean sendMessage(final Collection<? extends CommandSender> col, final String message) {
        for (final CommandSender cs : col) {
            sendMessage(cs, message);
        }
        return true;
    }
    
    public static boolean sendMessage(final CommandSender sender, final String message) {
        sender.sendMessage(fixColor(message));
        return true;
    }
    
    public static String fixColor(final String string) {
        return TextFormat.colorize('&', string).replace("&n", "\n");
    }
    
    public static List<String> fixColor(final List<String> strings) {
        final List<String> colors = new ArrayList<String>();
        for (final String s : strings) {
            colors.add(fixColor(s));
        }
        return colors;
    }
    
    public static String[] fixColor(final String[] strings) {
        for (int i = 0; i < strings.length; ++i) {
            strings[i] = fixColor(strings[i]);
        }
        return strings;
    }

    
    static {
        Util.names = new String[] { "Ada", "Adam", "Adela", "Adrian", "Agata", "Agnieszka", "Albert", "Aleksander", "Alicja", "Amadeusz", "Amelia", "Anastazja", "Andrzej", "Aneta", "Angelina", "Anna", "Arnold", "August", "Baltazar", "Bartosz", "Beata", "Bogdan", "Celina", "Cezary", "Cyprian", "Dalia", "Damian", "Daniel", "Danuta", "Daria", "Dariusz", "Dawid", "Dominik", "Dorian", "Dorota", "Dymitr", "Edmund", "Edward", "Edyta", "Eliza", "Elwira", "Erwin", "Eryk", "Eustachy", "Ewelina", "Fabian", "Faustyn", "Felicja", "Felicjan", "Feliks", "Ferdynand", "Filip", "Franciszek", "Fryderyk", "Gabriel", "Gerard", "Gracja", "Gracjan", "Grzegorz", "Halina", "Henryk", "Hubert", "Ignacy", "Igor", "Irena", "Ireneusz", "Iwona", "Izabela", "Jacek", "Jadwiga", "Jagoda", "Jakub", "Jan", "Janina", "Janusz", "Jerzy", "Joanna", "Jolanta", "Julia", "Juliusz", "Kacper", "Kajetan", "Kalina", "Kamil", "Karol", "Karolina", "Katarzyna", "Kinga", "Klara", "Klaudia", "Konrad", "Kornelia", "Krystian", "Krzysztof", "Laura", "Maciej", "Magda", "Magdalena", "Maksymilian", "Malwina", "Marcel", "Marcelina", "Marcin", "Marek", "Maria", "Marian", "Mariusz", "Marlena", "Marta", "Martyna", "Mieszko", "Milena", "Monika", "Narcyz", "Natalia", "Nikodem", "Norbert", "Norman", "Oktawia", "Oliwia", "Oskar", "Patrycja", "Patryk", "Paulina", "Piotr", "Renata", "Robert", "Roksana", "Rudolf", "Ryszard", "Sandra", "Sebastian", "Sergiusz", "Sofia", "Stefan", "Sylwia", "Szymon", "Teodor", "Tobiasz", "Tomasz", "Urszula", "Wanda", "Weronika", "Wiktor", "Wiktoria", "Wioletta", "Wojciech", "Zbigniew", "Zuzanna" };
    }
}
