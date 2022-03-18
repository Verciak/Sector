package xyz.rokkiitt.sector.objects.guild;

import cn.nukkit.utils.DyeColor;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.SpaceUtil;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.command.*;

import java.util.concurrent.*;

public class GuildHeartGUI extends DoubleChestFakeInventory
{
    private Item color;
    private Item carpet;
    private Item stairs;
    private Item head;
    private Item back;
    private State state;
    private Guild g;
    
    public GuildHeartGUI(final Guild g) {
        super(null, Util.fixColor("&6Panel serca"));
        this.color = new ItemBuilder(351, 1, DyeColor.WHITE.getDyeData()).setTitle("&r&6Wybor koloru napisu").build();
        this.carpet = new ItemBuilder(171, 1, DyeColor.RED.getWoolData()).setTitle("&r&6Wybor koloru dywanu").build();
        this.stairs = new ItemBuilder(108).setTitle("&r&6Wybor wygladu schodow").build();
        this.head = new ItemBuilder(397, 1, 3).setTitle("&r&6Wybor wygladu glowki").build();
        this.back = new ItemBuilder(-161).setTitle("&r&6Cofnij do menu").build();
        this.state = State.MAIN;
        this.g = g;
        this.sendGui();
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        final int slot = e.getAction().getSlot();
        if (this.state == State.MAIN) {
            if (slot == 20) {
                this.state = State.COLOR;
                this.sendGui();
                return;
            }
            if (slot == 24) {
                this.state = State.CARPET;
                this.sendGui();
                return;
            }
            if (slot == 30) {
                this.state = State.STAIRS;
                this.sendGui();
                return;
            }
            if (slot == 32) {
                this.state = State.HEAD;
                this.sendGui();
            }
        }
        else if (this.state == State.COLOR) {
            if (slot == 40) {
                this.state = State.MAIN;
                this.sendGui();
                return;
            }
            if (this.g.getCoolDown() <= System.currentTimeMillis()) {
                g.setCoolDown(System.currentTimeMillis() + Time.SECOND.getTime(5));
                final String color = Util.getNBTTagValue(e.getAction().getSourceItem(), "color");
                if (!color.isEmpty()) {
                    g.setHeartcolor(color);
                }
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("guildcooldown").replace("{S}", Util.formatTime(this.g.getCoolDown() - System.currentTimeMillis())));
            }
        }
        else if (this.state == State.CARPET) {
            if (slot == 40) {
                this.state = State.MAIN;
                this.sendGui();
                return;
            }
            if (this.g.getCoolDown() <= System.currentTimeMillis()) {
                g.setCoolDown(System.currentTimeMillis() + Time.SECOND.getTime(5));
                SpaceUtil.CarpetkColor(this.g, e.getAction().getSourceItem().getDamage());
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("guildcooldown").replace("{S}", Util.formatTime(this.g.getCoolDown() - System.currentTimeMillis())));
            }
        }
        else if (this.state == State.HEAD) {
            if (slot == 40) {
                this.state = State.MAIN;
                this.sendGui();
                return;
            }
            if (this.g.getCoolDown() <= System.currentTimeMillis()) {
                g.setCoolDown(System.currentTimeMillis() + Time.SECOND.getTime(5));
                Executors.newFixedThreadPool(1).submit(() -> {
                    String type = Util.getNBTTagValue(e.getAction().getSourceItem(), "type");
                    if (!type.isEmpty()) {
                        this.g.getHead().setSkin(type);
                    }
                });
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("guildcooldown").replace("{S}", Util.formatTime(this.g.getCoolDown() - System.currentTimeMillis())));
            }
        }
        else if (this.state == State.STAIRS) {
            if (slot == 49) {
                this.state = State.MAIN;
                this.sendGui();
                return;
            }
            if (this.g.getCoolDown() <= System.currentTimeMillis()) {
                g.setCoolDown(System.currentTimeMillis() + Time.SECOND.getTime(5));
                final String stair = Util.getNBTTagValue(e.getAction().getSourceItem(), "stairs");
                final String id = Util.getNBTTagValue(e.getAction().getSourceItem(), "blockid");
                final String meta = Util.getNBTTagValue(e.getAction().getSourceItem(), "blockmeta");
                final String slab = Util.getNBTTagValue(e.getAction().getSourceItem(), "slab");
                final String slabmeta1 = Util.getNBTTagValue(e.getAction().getSourceItem(), "slabmeta1");
                final String slabmeta2 = Util.getNBTTagValue(e.getAction().getSourceItem(), "slabmeta2");
                if (!stair.isEmpty()) {
                    SpaceUtil.HeartDesignStairs(this.g, Integer.valueOf(stair));
                }
                if (!id.isEmpty() && !meta.isEmpty()) {
                    SpaceUtil.HeartDesignBlocks(this.g, Integer.valueOf(id), Integer.valueOf(meta));
                }
                if (!slab.isEmpty()) {
                    SpaceUtil.HeartDesignSlab(this.g, Integer.valueOf(slab), Integer.valueOf(slabmeta1), Integer.valueOf(slabmeta2));
                }
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("guildcooldown").replace("{S}", Util.formatTime(this.g.getCoolDown() - System.currentTimeMillis())));
            }
        }
    }
    
    private void sendGui() {
        this.clearAll();
        this.setServerGui();
        if (this.state == State.MAIN) {
            this.setItem(20, this.color);
            this.setItem(24, this.carpet);
            this.setItem(30, this.stairs);
            this.setItem(32, this.head);
        }
        else if (this.state == State.COLOR) {
            this.setItem(40, this.back);
            this.setItem(19, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.BLACK.getDyeData()).setTitle(" ").build(), "color", "&0"));
            this.setItem(20, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.BLUE.getDyeData()).setTitle(" ").build(), "color", "&1"));
            this.setItem(22, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.CYAN.getDyeData()).setTitle(" ").build(), "color", "&b"));
            this.setItem(23, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.GRAY.getDyeData()).setTitle(" ").build(), "color", "&8"));
            this.setItem(24, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.GREEN.getDyeData()).setTitle(" ").build(), "color", "&2"));
            this.setItem(25, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.LIGHT_BLUE.getDyeData()).setTitle(" ").build(), "color", "&9"));
            this.setItem(28, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.LIGHT_GRAY.getDyeData()).setTitle(" ").build(), "color", "&7"));
            this.setItem(29, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.LIME.getDyeData()).setTitle(" ").build(), "color", "&a"));
            this.setItem(30, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.MAGENTA.getDyeData()).setTitle(" ").build(), "color", "&d"));
            this.setItem(31, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.PINK.getDyeData()).setTitle(" ").build(), "color", "&d"));
            this.setItem(32, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.PURPLE.getDyeData()).setTitle(" ").build(), "color", "&5"));
            this.setItem(33, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.RED.getDyeData()).setTitle(" ").build(), "color", "&c"));
            this.setItem(34, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.WHITE.getDyeData()).setTitle(" ").build(), "color", "&f"));
            this.setItem(21, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.YELLOW.getDyeData()).setTitle(" ").build(), "color", "&e"));
        }
        else if (this.state == State.HEAD) {
            this.setItem(40, this.back);
            this.setItem(19, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.BLACK.getDyeData()).setTitle(" ").build(), "type", "black"));
            this.setItem(20, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.BLUE.getDyeData()).setTitle(" ").build(), "type", "blue"));
            this.setItem(22, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.CYAN.getDyeData()).setTitle(" ").build(), "type", "cyan"));
            this.setItem(24, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.GREEN.getDyeData()).setTitle(" ").build(), "type", "green"));
            this.setItem(25, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.LIGHT_BLUE.getDyeData()).setTitle(" ").build(), "type", "light_blue"));
            this.setItem(29, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.LIME.getDyeData()).setTitle(" ").build(), "type", "lime"));
            this.setItem(30, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.MAGENTA.getDyeData()).setTitle(" ").build(), "type", "magenta"));
            this.setItem(31, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.PINK.getDyeData()).setTitle(" ").build(), "type", "pink"));
            this.setItem(32, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.PURPLE.getDyeData()).setTitle(" ").build(), "type", "purple"));
            this.setItem(33, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.RED.getDyeData()).setTitle(" ").build(), "type", "red"));
            this.setItem(23, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.WHITE.getDyeData()).setTitle(" ").build(), "type", "white"));
            this.setItem(21, Util.addNBTTagWithValue(new ItemBuilder(351, 1, DyeColor.YELLOW.getDyeData()).setTitle(" ").build(), "type", "gold"));
        }
        else if (this.state == State.CARPET) {
            this.setItem(40, this.back);
            this.setItem(19, new ItemBuilder(171, 1, DyeColor.BLACK.getWoolData()).setTitle(" ").build());
            this.setItem(20, new ItemBuilder(171, 1, DyeColor.BLUE.getWoolData()).setTitle(" ").build());
            this.setItem(22, new ItemBuilder(171, 1, DyeColor.CYAN.getWoolData()).setTitle(" ").build());
            this.setItem(23, new ItemBuilder(171, 1, DyeColor.GRAY.getWoolData()).setTitle(" ").build());
            this.setItem(24, new ItemBuilder(171, 1, DyeColor.GREEN.getWoolData()).setTitle(" ").build());
            this.setItem(25, new ItemBuilder(171, 1, DyeColor.LIGHT_BLUE.getWoolData()).setTitle(" ").build());
            this.setItem(28, new ItemBuilder(171, 1, DyeColor.LIGHT_GRAY.getWoolData()).setTitle(" ").build());
            this.setItem(29, new ItemBuilder(171, 1, DyeColor.LIME.getWoolData()).setTitle(" ").build());
            this.setItem(30, new ItemBuilder(171, 1, DyeColor.MAGENTA.getWoolData()).setTitle(" ").build());
            this.setItem(31, new ItemBuilder(171, 1, DyeColor.PINK.getWoolData()).setTitle(" ").build());
            this.setItem(32, new ItemBuilder(171, 1, DyeColor.PURPLE.getWoolData()).setTitle(" ").build());
            this.setItem(33, new ItemBuilder(171, 1, DyeColor.RED.getWoolData()).setTitle(" ").build());
            this.setItem(34, new ItemBuilder(171, 1, DyeColor.WHITE.getWoolData()).setTitle(" ").build());
            this.setItem(21, new ItemBuilder(171, 1, DyeColor.YELLOW.getWoolData()).setTitle(" ").build());
        }
        else if (this.state == State.STAIRS) {
            this.setItem(49, this.back);
            this.setItem(19, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(67).setTitle(" ").build(), "stairs", "67"), "blockid", "4"), "blockmeta", "0"), "slab", "44"), "slabmeta1", "3"), "slabmeta2", "11"));
            this.setItem(20, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(53).setTitle(" ").build(), "stairs", "53"), "blockid", "5"), "blockmeta", "0"), "slab", "158"), "slabmeta1", "0"), "slabmeta2", "8"));
            this.setItem(21, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(134).setTitle(" ").build(), "stairs", "134"), "blockid", "5"), "blockmeta", "1"), "slab", "158"), "slabmeta1", "1"), "slabmeta2", "9"));
            this.setItem(22, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(135).setTitle(" ").build(), "stairs", "135"), "blockid", "5"), "blockmeta", "2"), "slab", "158"), "slabmeta1", "2"), "slabmeta2", "10"));
            this.setItem(23, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(136).setTitle(" ").build(), "stairs", "136"), "blockid", "5"), "blockmeta", "3"), "slab", "158"), "slabmeta1", "3"), "slabmeta2", "11"));
            this.setItem(24, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(163).setTitle(" ").build(), "stairs", "163"), "blockid", "5"), "blockmeta", "4"), "slab", "158"), "slabmeta1", "4"), "slabmeta2", "12"));
            this.setItem(25, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(164).setTitle(" ").build(), "stairs", "164"), "blockid", "5"), "blockmeta", "5"), "slab", "158"), "slabmeta1", "5"), "slabmeta2", "13"));
            this.setItem(28, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(109).setTitle(" ").build(), "stairs", "109"), "blockid", "98"), "blockmeta", "0"), "slab", "44"), "slabmeta1", "5"), "slabmeta2", "13"));
            this.setItem(29, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(128).setTitle(" ").build(), "stairs", "128"), "blockid", "24"), "blockmeta", "0"), "slab", "44"), "slabmeta1", "1"), "slabmeta2", "9"));
            this.setItem(30, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(180).setTitle(" ").build(), "stairs", "180"), "blockid", "179"), "blockmeta", "0"), "slab", "182"), "slabmeta1", "0"), "slabmeta2", "8"));
            this.setItem(31, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(108).setTitle(" ").build(), "stairs", "108"), "blockid", "45"), "blockmeta", "0"), "slab", "44"), "slabmeta1", "4"), "slabmeta2", "12"));
            this.setItem(32, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(114).setTitle(" ").build(), "stairs", "114"), "blockid", "112"), "blockmeta", "0"), "slab", "44"), "slabmeta1", "7"), "slabmeta2", "15"));
            this.setItem(33, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(156).setTitle(" ").build(), "stairs", "156"), "blockid", "155"), "blockmeta", "0"), "slab", "44"), "slabmeta1", "6"), "slabmeta2", "14"));
            this.setItem(34, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(203).setTitle(" ").build(), "stairs", "203"), "blockid", "201"), "blockmeta", "0"), "slab", "182"), "slabmeta1", "1"), "slabmeta2", "9"));
        }
    }

    private enum State {
        MAIN, COLOR, CARPET, STAIRS, HEAD;
    }
}
