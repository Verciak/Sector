package xyz.rokkiitt.sector.objects;

import cn.nukkit.item.Item;
import org.apache.commons.lang3.ArrayUtils;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.packets.commands.PacketToprankCommand;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.*;
import java.util.*;
import java.util.stream.Collectors;

public class Top extends DoubleChestFakeInventory
{
    private final List<String> points;
    private final List<String> kills;
    private final List<String> deaths;
    private final List<String> assist;
    private final List<String> time;
    private final List<String> guild;
    private final List<String> broken;
    private final List<String> eatKox;
    private final List<String> eatRef;
    private final List<String> throwPearl;
    
    public Top(final Player p, final PacketToprankCommand pa) {
        super(null, Util.fixColor("&9Rankingi"));
        this.holder = (InventoryHolder)p;
        this.points = pa.points;
        this.kills = pa.kills;
        this.deaths = pa.deaths;
        this.assist = pa.assist;
        this.time = pa.time;
        this.guild = pa.guild;
        this.broken = pa.broken;
        this.eatKox = pa.eatKox;
        this.eatRef = pa.eatRef;
        this.throwPearl = pa.throwPearl;
        this.show();
        p.addWindow((Inventory)this);
    }
    
    private void show() {
        this.clearAll();
        this.fill();
        this.setServerGui();
        this.setItem(13, new ItemBuilder(Item.SKULL, 1, 3).setTitle("&r&l&9PUNKTY\u270b").setLore(this.sortPoints()).build());
        this.setItem(22, new ItemBuilder(Item.GOLDEN_SWORD).setTitle("&r&l&9ASYSTY\u270b").setLore(this.sortAssists()).build());
        this.setItem(28, new ItemBuilder(Item.CLOCK).setTitle("&r&l&9SPEDZONY CZAS\u270b").setLore(this.sortTime()).build());
        this.setItem(29, new ItemBuilder(Item.STONE).setTitle("&r&l&9WYKOPANE BLOKI\u270b").setLore(this.sortBroken()).build());
        this.setItem(30, new ItemBuilder(Item.SKULL).setTitle("&r&l&9SMIERCI\u270b").setLore(this.sortDeaths()).build());
        this.setItem(32, new ItemBuilder(Item.GOLDEN_APPLE).setTitle("&r&l&9ZJEDZONE REFILE\u270b").setLore(this.sortRefil()).build());
        this.setItem(33, new ItemBuilder(Item.ENDER_PEARL).setTitle("&r&l&9RZUCONE PERLY\u270b").setLore(this.sortPearl()).build());
        this.setItem(34, new ItemBuilder(Item.GOLDEN_APPLE_ENCHANTED).setTitle("&r&l&9ZJEDZONE KOXY\u270b").setLore(this.sortKox()).build());
        this.setItem(40, new ItemBuilder(Item.TOTEM).setTitle("&r&l&9ZABOJSTWA\u270b").setLore(this.sortKills()).build());
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
    }

    public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortValue(LinkedHashMap<K, V> map) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private String[] sortPoints() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.points) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
                x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
                ++b;

        }
        return x;
    }
    
    private String[] sortKills() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.kills) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }
    
    private String[] sortDeaths() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.deaths) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }
    private String[] sortKox() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.eatKox) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }

    private String[] sortPearl() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.throwPearl) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }

    private String[] sortRefil() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.eatRef) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }
    
    private String[] sortAssists() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.assist) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }
    
    private String[] sortTime() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.time) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + Util.formatTime(Long.parseLong(String.valueOf(s.getValue()))) + "&8]\u270b";
            ++b;

        }
        return x;
    }
    
    private String[] sortGuilds() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.guild) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }
    
    private String[] sortBroken() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (final String s : this.broken) {
            final String[] split = s.split(Pattern.quote("|&|"));
            map.put(split[0].toUpperCase(), Integer.valueOf(split[1]));
        }
        LinkedHashMap<String, Integer> linkedHashMap = sortValue(map);
        final String[] x = new String[12];

        int length = x.length;
        for (int i = 0; i < length; i++) {
            x[i] = i > 0 && i <= length - 2 ? "&r&8>> &FBRAK &8[&90000&8]\u270b" : "";
        }

        int b = 1;
        for (final Map.Entry<String, Integer> s : linkedHashMap.entrySet()) {
            x[b] = "&r&8>> &f" + b + ". &7" + s.getKey().toUpperCase() + " &8[&9" + s.getValue() + "&8]\u270b";
            ++b;

        }
        return x;
    }
}
