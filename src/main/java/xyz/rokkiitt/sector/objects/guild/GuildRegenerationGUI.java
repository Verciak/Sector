package xyz.rokkiitt.sector.objects.guild;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.HopperFakeInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.command.*;
import cn.nukkit.*;
import java.util.*;

public class GuildRegenerationGUI extends HopperFakeInventory
{
    private final Guild g;
    
    public GuildRegenerationGUI(final Guild g) {
        super(null, Util.fixColor("&6Regeneracja"));
        this.g = g;
        this.setItem(0, new ItemBuilder(339, 1).setTitle("&r&6Informacje").setLore(new String[] { "&r&7Regeneracja odnawia bloki zniszczone przez &eTNT", "&r&7Kosztem regeneracji sa &eBloki diamentow", "", "&6Regeneracja nie odnawia:", "&r&8&l- &e rudy diamentu, szmaragdu, zlota, zelaza", "&8&l- &e bloku diamentu, szmaragdu, zlota, zelaza", "&r&8&l- &e biblioteczki, enchantu, beacona, kowadel itd.", "&8&l- &e TNT, tabliczek, drabinek, lozek" }).build());
        this.setItem(1, new ItemBuilder(339, 1).setTitle("&r&6Cennik").setLore(new String[] { "&r&7Koszt regeneracji &e50 blokow = 1 blok diamentu" }).build());
        this.setItem(2, new ItemBuilder(101, 1).setTitle(" ").build());
        this.setItem(3, new ItemBuilder(35, 1, g.isRegen() ? 14 : 5).setTitle(" ").setLore(new String[] { " ", g.isRegen() ? "&r&eRegeneracja jest aktywna" : "&r&eRegeneracja jest nie aktywna" }).build());
        this.setItem(4, new ItemBuilder(323, 1).setTitle("&r").setLore(new String[] { "&r&7Wplata na balans gildii", "&r&eKliknij lewy przycisk aby dokonac wplaty 5 blokow diamentu", " ", "&r&7Stan balansu gildii: &e{GB}".replace("{GB}", String.valueOf(g.getGuildBalance())) }).build());
        this.sendContents((Collection)this.viewers);
    }
    
    public void refresh() {
        this.setItem(3, new ItemBuilder(35, 1, this.g.isRegen() ? 14 : 5).setTitle(" ").setLore(new String[] { " ", this.g.isRegen() ? "&r&eRegeneracja jest aktywna" : "&r&eRegeneracja jest nie aktywna" }).build());
        this.setItem(4, new ItemBuilder(323, 1).setTitle("&r").setLore(new String[] { "&r&7Wplata na balans gildii", "&r&eKliknij lewy przycisk aby dokonac wplaty 5 blokow diamentu", " ", "&r&7Stan balansu gildii: &e{GB}".replace("{GB}", String.valueOf(this.g.getGuildBalance())) }).build());
        this.sendContents((Collection)this.viewers);
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        final int slot = e.getAction().getSlot();
        final Player p = e.getPlayer();
        if (slot == 4) {
            if (!p.hasPermission("payments.free")) {
                final Item item = Item.get(57);
                item.setCount(5);
                if (item.getId() != 0 && p.getInventory().contains(item)) {
                    Util.removeItemById(e.getPlayer(), item);
                    final Guild g = this.g;
                    g.setGuildBalance(g.getGuildBalance() + 5);
                    this.refresh();
                }
            }
            else {
                final Guild g2 = this.g;
                g2.setGuildBalance(g2.getGuildBalance() + 5);
                this.refresh();
            }
        }
        else if (slot == 3) {
            final User uu = UserManager.getUser(p.getName());
            if (uu != null) {
                if (uu.hasPermission("12")) {
                    if (this.g.getCoolDown() <= System.currentTimeMillis()) {
                        g.setCoolDown(System.currentTimeMillis() + Time.SECOND.getTime(3));
                        if (this.g.getRegenExplodeTime() <= System.currentTimeMillis()) {
                            if (this.g.getGuildBalance() > 0) {
                                if (this.g.reSizeRegen() > 0) {
                                    if (this.g.isRegen()) {
                                        g.setRegen(false);
                                        this.g.getRegengui().refresh();
                                        for (final User u : UserManager.users) {
                                            if (u.getTag().equalsIgnoreCase(this.g.getTag())) {
                                                final Player pp = Server.getInstance().getPlayerExact(u.getNickname().toLowerCase());
                                                if (pp == null) {
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        this.g.rsetY(0);
                                        this.g.regenSort(this.g.rgetY());
                                        g.setRegen(true);
                                        this.g.getRegengui().refresh();
                                    }
                                }
                                else {
                                    Util.sendMessage((CommandSender)p, Settings.getMessage("guildregenempty"));
                                }
                            }
                            else {
                                Util.sendMessage((CommandSender)p, Settings.getMessage("guildgoldempty"));
                            }
                        }
                        else {
                            Util.sendMessage((CommandSender)p, Settings.getMessage("guildregencooldown").replace("{TIME}", Util.formatTime(this.g.getRegenExplodeTime() - System.currentTimeMillis())));
                        }
                    }
                    else {
                        Util.sendMessage((CommandSender)p, Settings.getMessage("guildcooldown").replace("{S}", Util.formatTime(this.g.getCoolDown() - System.currentTimeMillis())));
                    }
                }
                else {
                    Util.sendMessage((CommandSender)p, Settings.getMessage("guildpermission").replace("{TYPE}", "zarzadzania regeneracja!"));
                }
            }
            else {
                p.kick("not properly loaded data - RegenGui");
            }
        }
    }
}
