package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.ChestFakeInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.packets.commands.PacketIncognitoCommand;
import xyz.rokkiitt.sector.utils.GlassColor;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;
import cn.nukkit.command.*;

public class IncognitoInventory extends ChestFakeInventory
{
    private User user;
    private Player holder;
    private long cooldown;
    
    public IncognitoInventory(final Player p, final User u) {
        super(null, Util.fixColor("&6Incognito"));
        this.cooldown = System.currentTimeMillis();
        this.holder = p;
        this.user = u;
        Main.incognitos.add(this);
        this.refreshGui();
        p.addWindow((Inventory)this);
    }
    
    public void callRefresh(final Player p) {
        if (p.getName().equalsIgnoreCase(this.holder.getName())) {
            this.refreshGui();
        }
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled();
        if (e.getAction().getSlot() == 11) {
            if (this.cooldown <= System.currentTimeMillis()) {
                if (this.user.getIncognito().equalsIgnoreCase("brak")) {
                    return;
                }
                this.refreshGui();
                Util.setIncognito(this.user, e.getPlayer());
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("incognitostatus").replace("{STATUS}", this.user.isIncognito() ? "Wlaczyles" : "Wylaczyles"));
                this.cooldown = System.currentTimeMillis() + Time.SECOND.getTime(10);
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("incognitocooldown2").replace("{CD}", Util.formatTime(this.cooldown - System.currentTimeMillis())));
            }
        }
        else if (e.getAction().getSlot() == 12) {
            if (this.cooldown <= System.currentTimeMillis()) {
                this.refreshGui();
                Util.setIncognito(this.user, e.getPlayer());
                this.cooldown = System.currentTimeMillis() + Time.SECOND.getTime(10);
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("incognitocooldown2").replace("{CD}", Util.formatTime(this.cooldown - System.currentTimeMillis())));
            }
        }
        else if (e.getAction().getSlot() == 13) {
            if (this.cooldown <= System.currentTimeMillis()) {
                final PacketIncognitoCommand pa = new PacketIncognitoCommand();
                pa.sender = e.getPlayer().getName();
                pa.incognitoNickname = "";
                pa.reason = "";
                pa.isList = false;
                pa.isCheck = false;
                this.cooldown = System.currentTimeMillis() + Time.SECOND.getTime(10);
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("incognitocooldown2").replace("{CD}", Util.formatTime(this.cooldown - System.currentTimeMillis())));
            }
        }
        else if (e.getAction().getSlot() == 14) {
            if (this.cooldown <= System.currentTimeMillis()) {
                this.refreshGui();
                Util.setIncognito(this.user, e.getPlayer());
                this.cooldown = System.currentTimeMillis() + Time.SECOND.getTime(10);
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("incognitocooldown2").replace("{CD}", Util.formatTime(this.cooldown - System.currentTimeMillis())));
            }
        }
        else if (e.getAction().getSlot() == 15) {
            if (this.cooldown <= System.currentTimeMillis()) {
                this.refreshGui();
                Util.setIncognito(this.user, e.getPlayer());
                this.cooldown = System.currentTimeMillis() + Time.SECOND.getTime(10);
            }
            else {
                Util.sendMessage((CommandSender)e.getPlayer(), Settings.getMessage("incognitocooldown2").replace("{CD}", Util.formatTime(this.cooldown - System.currentTimeMillis())));
            }
        }
    }
    
    @Override
    public void onClose(final Player who) {
        Main.incognitos.remove(this);
        super.onClose(who);
    }
    
    public void refreshGui() {
        this.clearAll();
        this.setSmallServerGui();
        this.setItem(10, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        this.setItem(16, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        this.setItem(13, GlassColor.get(GlassColor.BLACK).setCustomName(Util.fixColor("&r")));
        this.setItem(11, new ItemBuilder(421).setTitle((this.user.isIncognito() ? "&r&l&a" : "&r&l&c") + "Ukrywanie nazwy oraz tagu").setLore(new String[] { "\u270b", "&r&l&f Ukrywa twoj nick/tag dla wrogow", "&r&l&f Czlonkowie twojej gildii ", "&r&l&f widza twoj prawdziny nick" }).build());
        this.setItem(12, new ItemBuilder(421).setTitle((this.user.isIncognitoAlliance() ? "&r&l&a" : "&r&l&c") + "Ukrywanie przed sojuszami").setLore(new String[] { "\u270b", "&r&l&f Ukrywa twoj nick/tag dla sojuszy" }).build());
        this.setItem(13, new ItemBuilder(-161).setTitle("&r&l&6Resetuje twoj nickname").setLore(new String[] { "\u270b", "&r&l&f Resetuje twoj nickname incognito", "", "&r&l&fWygenerowany nick: &e" + (this.user.getIncognito().equalsIgnoreCase("brak") ? "&cBrak - Wygeneruj swoj nickname!" : this.user.getIncognito()) }).build());
        this.setItem(14, new ItemBuilder(421).setTitle((this.user.isIncognitoKill() ? "&r&l&a" : "&r&l&c") + "Ukrywanie nazwy oraz tagu podczas zabojstwa").setLore(new String[] { "\u270b", "&r&l&f Ukrywa twoj nick/tag podczas zabojstwa", " ", "&r&l&cPodstawowe incognito musi byc wlaczone!" }).build());
        this.setItem(15, new ItemBuilder(421).setTitle((this.user.isIncognitoDead() ? "&r&l&a" : "&r&l&c") + "Ukrywanie nazwy oraz tagu podczas smierci").setLore(new String[] { "\u270b", "&r&l&f Ukrywa twoj nick/tag podczas smierci", " ", "&r&l&cPodstawowe incognito musi byc wlaczone!" }).build());
    }
}
