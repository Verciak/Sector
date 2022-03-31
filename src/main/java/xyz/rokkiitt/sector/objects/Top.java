package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.packets.commands.PacketToprankCommand;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;

import java.util.regex.*;
import java.util.*;

public class Top extends ChestFakeInventory
{
    private final List<String> points;
    private final List<String> kills;
    private final List<String> deaths;
    private final List<String> assist;
    private final List<String> time;
    private final List<String> guild;
    private final List<String> broken;
    
    public Top(final Player p, final PacketToprankCommand pa) {
        super(null, Util.fixColor("&6Rankingi"));
        this.holder = (InventoryHolder)p;
        this.points = pa.points;
        this.kills = pa.kills;
        this.deaths = pa.deaths;
        this.assist = pa.assist;
        this.time = pa.time;
        this.guild = pa.guild;
        this.broken = pa.broken;
        this.show();
        p.addWindow((Inventory)this);
    }
    
    private void show() {
        this.clearAll();
        this.setSmallServerGui();
        this.setItem(10, new ItemBuilder(323).setTitle("&r&ePUNKTY\u270b").setLore(this.sortPoints()).build());
        this.setItem(11, new ItemBuilder(315).setTitle("&r&eZABOJSTWA\u270b").setLore(this.sortKills()).build());
        this.setItem(12, new ItemBuilder(397).setTitle("&r&eSMIERCI\u270b").setLore(this.sortDeaths()).build());
        this.setItem(13, new ItemBuilder(267).setTitle("&r&eASYSTY\u270b").setLore(this.sortAssists()).build());
        this.setItem(14, new ItemBuilder(347).setTitle("&r&eSPEDZONY CZAS\u270b").setLore(this.sortTime()).build());
        this.setItem(15, new ItemBuilder(399).setTitle("&r&eGILDIE\u270b").setLore(this.sortGuilds()).build());
        this.setItem(16, new ItemBuilder(278).setTitle("&r&eWYKOPANE BLOKI\u270b").setLore(this.sortBroken()).build());
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
    }
    
    private String[] sortPoints() {
        final String[] x = { "", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "" };
        int b = 1;
        for (final String s : this.points) {
            final String[] split = s.split(Pattern.quote("|&|"));
            if (split.length > 1) {
                x[b] = "&r&8>> &f" + b + ". &e" + split[0].toUpperCase() + " &8[&4" + split[1] + "&8]\u270b";
            }
            else {
                x[b] = "&r&8>> &f" + b + ". &eBRAK &8[&40000&8]\u270b";
            }
            ++b;
        }
        return x;
    }
    
    private String[] sortKills() {
        final String[] x = { "", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "" };
        int b = 1;
        for (final String s : this.kills) {
            final String[] split = s.split(Pattern.quote("|&|"));
            if (split.length > 1) {
                x[b] = "&r&8>> &f" + b + ". &e" + split[0].toUpperCase() + " &8[&4" + split[1] + "&8]\u270b";
            }
            else {
                x[b] = "&r&8>> &f" + b + ". &eBRAK &8[&40000&8]\u270b";
            }
            ++b;
        }
        return x;
    }
    
    private String[] sortDeaths() {
        final String[] x = { "", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "" };
        int b = 1;
        for (final String s : this.deaths) {
            final String[] split = s.split(Pattern.quote("|&|"));
            if (split.length > 1) {
                x[b] = "&r&8>> &f" + b + ". &e" + split[0].toUpperCase() + " &8[&4" + split[1] + "&8]\u270b";
            }
            else {
                x[b] = "&r&8>> &f" + b + ". &eBRAK &8[&40000&8]\u270b";
            }
            ++b;
        }
        return x;
    }
    
    private String[] sortAssists() {
        final String[] x = { "", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "" };
        int b = 1;
        for (final String s : this.assist) {
            final String[] split = s.split(Pattern.quote("|&|"));
            if (split.length > 1) {
                x[b] = "&r&8>> &f" + b + ". &e" + split[0].toUpperCase() + " &8[&4" + split[1] + "&8]\u270b";
            }
            else {
                x[b] = "&r&8>> &f" + b + ". &eBRAK &8[&40000&8]\u270b";
            }
            ++b;
        }
        return x;
    }
    
    private String[] sortTime() {
        final String[] x = { "", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "" };
        int b = 1;
        for (final String s : this.time) {
            final String[] split = s.split(Pattern.quote("|&|"));
            if (split.length > 1) {
                x[b] = "&r&8>> &f" + b + ". &e" + split[0].toUpperCase() + " &8[&4" + Util.formatTime(Long.valueOf(split[1])) + "&8]\u270b";
            }
            else {
                x[b] = "&r&8>> &f" + b + ". &eBRAK &8[&40s&8]\u270b";
            }
            ++b;
        }
        return x;
    }
    
    private String[] sortGuilds() {
        final String[] x = { "", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "" };
        int b = 1;
        for (final String s : this.guild) {
            final String[] split = s.split(Pattern.quote("|&|"));
            if (split.length > 1) {
                x[b] = "&r&8>> &f" + b + ". &e" + split[0].toUpperCase() + " &8[&4" + split[1] + "&8]\u270b";
            }
            else {
                x[b] = "&r&8>> &f" + b + ". &eBRAK &8[&40000&8]\u270b";
            }
            ++b;
        }
        return x;
    }
    
    private String[] sortBroken() {
        final String[] x = { "", "{1}", "{2}", "{3}", "{4}", "{5}", "{6}", "{7}", "{8}", "{9}", "{10}", "" };
        int b = 1;
        for (final String s : this.broken) {
            final String[] split = s.split(Pattern.quote("|&|"));
            if (split.length > 1) {
                x[b] = "&r&8>> &f" + b + ". &e" + split[0].toUpperCase() + " &8[&4" + split[1] + "&8]\u270b";
            }
            else {
                x[b] = "&r&8>> &f" + b + ". &eBRAK &8[&40000&8]\u270b";
            }
            ++b;
        }
        return x;
    }
}
