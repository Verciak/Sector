package xyz.rokkiitt.sector.listeners;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.ac.EspModule;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.DepositUtil;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.entity.*;
import cn.nukkit.event.*;
import cn.nukkit.event.server.*;
import cn.nukkit.*;
import cn.nukkit.scheduler.*;
import cn.nukkit.math.*;
import cn.nukkit.level.*;
import cn.nukkit.network.protocol.*;
import cn.nukkit.item.food.*;
import cn.nukkit.inventory.transaction.data.*;
import cn.nukkit.item.*;
import java.util.*;

public class DataPacketEvents implements Listener
{
    private static List<Integer> sounds;
    private static Map<String, Eatings> eatings;
    
    @EventHandler
    public void onSend(final DataPacketSendEvent e) {
        if (e.getPacket() instanceof LevelSoundEventPacket) {
            if (DataPacketEvents.sounds.contains(((LevelSoundEventPacket)e.getPacket()).sound)) {
                e.setCancelled(true);
            }
        }
        else if (e.getPacket() instanceof SetEntityDataPacket) {
            final SetEntityDataPacket pa = (SetEntityDataPacket)e.getPacket();
            final Entity enn = e.getPlayer().getLevel().getEntity(pa.eid);
            if (!(enn instanceof Player)) {
                return;
            }
            final Player en = (Player)enn;
            if (en.getId() == e.getPlayer().getId()) {
                return;
            }
            if (!pa.metadata.exists(4)) {
                return;
            }
            final User u = UserManager.getUser(e.getPlayer().getName());
            final User u2 = UserManager.getUser(en.getName());
            if (u != null && u2 != null) {
                if (u.getTag().equalsIgnoreCase(u2.getTag()) && !u.getTag().equalsIgnoreCase("NIEPOSIADA") && !u2.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                    String nametag = "{VANISH}{OCHRONA}{TAG}{NICK}{INCOGNITO}{DEVICE}";
                    nametag = nametag.replace("{VANISH}", e.getPlayer().hasPermission(Perms.CMD_VANISH.getPermission()) ? (u2.isVanish() ? "&8[&6VANISH&8]\n " : "") : "");
                    nametag = nametag.replace("{OCHRONA}", u2.hasProtection() ? "&8[&6OCHRONA&8] " : "");
                    nametag = nametag.replace("{TAG}", this.getTag("&a", u, u2, false, true));
                    nametag = nametag.replace("{NICK}", u2.isIncognito() ? u2.getIncognito() : en.getName());
                    nametag = nametag.replace("{INCOGNITO}", this.getIncognito("&a", e.getPlayer(), u, u2, false, true));
                    nametag = nametag.replace("{DEVICE}", e.getPlayer().hasPermission(Perms.DEVICESEE.getPermission()) ? ("\n&e" + getDeviceName(en)) : "");
                    pa.metadata.putString(4, Util.fixColor(nametag));
                }
                else if (u.getAlliances().contains(u2.getTag().toLowerCase()) && !u.getTag().equalsIgnoreCase("NIEPOSIADA") && !u2.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                    String nametag = "{VANISH}{OCHRONA}{TAG}{NICK}{INCOGNITO}{DEVICE}";
                    nametag = nametag.replace("{VANISH}", e.getPlayer().hasPermission(Perms.CMD_VANISH.getPermission()) ? (u2.isVanish() ? "&8[&6VANISH&8]\n " : "") : "");
                    nametag = nametag.replace("{OCHRONA}", u2.hasProtection() ? "&8[&6OCHRONA&8] " : "");
                    nametag = nametag.replace("{TAG}", this.getTag("&b", u, u2, true, false));
                    nametag = nametag.replace("{NICK}", u2.isIncognito() ? u2.getIncognito() : en.getName());
                    nametag = nametag.replace("{INCOGNITO}", this.getIncognito("&b", e.getPlayer(), u, u2, true, false));
                    nametag = nametag.replace("{DEVICE}", e.getPlayer().hasPermission(Perms.DEVICESEE.getPermission()) ? ("\n&e" + getDeviceName(en)) : "");
                    pa.metadata.putString(4, Util.fixColor(nametag));
                }
                else {
                    String nametag = "{VANISH}{OCHRONA}{TAG}{NICK}{INCOGNITO}{DEVICE}";
                    nametag = nametag.replace("{VANISH}", e.getPlayer().hasPermission(Perms.CMD_VANISH.getPermission()) ? (u2.isVanish() ? "&8[&6VANISH&8]\n " : "") : "");
                    nametag = nametag.replace("{OCHRONA}", u2.hasProtection() ? "&8[&6OCHRONA&8] " : "");
                    nametag = nametag.replace("{TAG}", this.getTag("&c", u, u2, false, false));
                    nametag = nametag.replace("{NICK}", u2.isIncognito() ? u2.getIncognito() : en.getName());
                    nametag = nametag.replace("{INCOGNITO}", this.getIncognito("&c", e.getPlayer(), u, u2, false, false));
                    nametag = nametag.replace("{DEVICE}", e.getPlayer().hasPermission(Perms.DEVICESEE.getPermission()) ? ("\n&e" + getDeviceName(en)) : "");
                    pa.metadata.putString(4, Util.fixColor(nametag));
                }
            }
        }
        else if (e.getPacket() instanceof AddPlayerPacket) {
            final AddPlayerPacket pa2 = (AddPlayerPacket)e.getPacket();
            final Entity enn = e.getPlayer().getLevel().getEntity(pa2.entityRuntimeId);
            if (!(enn instanceof Player)) {
                return;
            }
            final Player en = (Player)enn;
            if (en.getId() == e.getPlayer().getId()) {
                return;
            }
            if (!pa2.metadata.exists(4)) {
                return;
            }
            final User u = UserManager.getUser(e.getPlayer().getName());
            final User u2 = UserManager.getUser(en.getName());
            if (u != null && u2 != null) {
                if (u.getTag().equalsIgnoreCase(u2.getTag()) && !u.getTag().equalsIgnoreCase("NIEPOSIADA") && !u2.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                    String nametag = "{VANISH}{OCHRONA}{TAG}{NICK}{INCOGNITO}{DEVICE}";
                    nametag = nametag.replace("{VANISH}", e.getPlayer().hasPermission(Perms.CMD_VANISH.getPermission()) ? (u2.isVanish() ? "&8[&6VANISH&8]\n " : "") : "");
                    nametag = nametag.replace("{OCHRONA}", u2.hasProtection() ? "&8[&6OCHRONA&8] " : "");
                    nametag = nametag.replace("{TAG}", this.getTag("&a", u, u2, false, true));
                    nametag = nametag.replace("{NICK}", u2.isIncognito() ? u2.getIncognito() : en.getName());
                    nametag = nametag.replace("{INCOGNITO}", this.getIncognito("&a", e.getPlayer(), u, u2, false, true));
                    nametag = nametag.replace("{DEVICE}", "\n&e" + getDeviceName(en));
                    pa2.metadata.putString(4, Util.fixColor(nametag));
                }
                else if (u.getAlliances().contains(u2.getTag().toLowerCase()) && !u.getTag().equalsIgnoreCase("NIEPOSIADA") && !u2.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                    String nametag = "{VANISH}{OCHRONA}{TAG}{NICK}{INCOGNITO}{DEVICE}";
                    nametag = nametag.replace("{VANISH}", e.getPlayer().hasPermission(Perms.CMD_VANISH.getPermission()) ? (u2.isVanish() ? "&8[&6VANISH&8]\n " : "") : "");
                    nametag = nametag.replace("{OCHRONA}", u2.hasProtection() ? "&8[&6OCHRONA&8] " : "");
                    nametag = nametag.replace("{TAG}", this.getTag("&b", u, u2, true, false));
                    nametag = nametag.replace("{NICK}", u2.isIncognito() ? u2.getIncognito() : en.getName());
                    nametag = nametag.replace("{INCOGNITO}", this.getIncognito("&b", e.getPlayer(), u, u2, true, false));
                    nametag = nametag.replace("{DEVICE}", "\n&e" + getDeviceName(en));
                    pa2.metadata.putString(4, Util.fixColor(nametag));
                }
                else {
                    String nametag = "{VANISH}{OCHRONA}{TAG}{NICK}{INCOGNITO}{DEVICE}";
                    nametag = nametag.replace("{VANISH}", e.getPlayer().hasPermission(Perms.CMD_VANISH.getPermission()) ? (u2.isVanish() ? "&8[&6VANISH&8]\n " : "") : "");
                    nametag = nametag.replace("{OCHRONA}", u2.hasProtection() ? "&8[&6OCHRONA&8] " : "");
                    nametag = nametag.replace("{TAG}", this.getTag("&c", u, u2, false, false));
                    nametag = nametag.replace("{NICK}", u2.isIncognito() ? u2.getIncognito() : en.getName());
                    nametag = nametag.replace("{INCOGNITO}", this.getIncognito("&c", e.getPlayer(), u, u2, false, false));
                    nametag = nametag.replace("{DEVICE}", "\n&e" + getDeviceName(en));
                    pa2.metadata.putString(4, Util.fixColor(nametag));
                }
            }
        }
    }
    
    public String getIncognito(final String color, final Player p, final User u, final User u2, final boolean isAlliance, final boolean isGuild) {
        if (u2.isIncognito()) {
            if (p.hasPermission(Perms.INCOGNITOSEE.getPermission())) {
                return "&8[&6" + u2.getNickname() + "&8]";
            }
            if (isGuild) {
                return "&8[" + color + u2.getNickname() + "&8]";
            }
            if (!isAlliance) {
                return "";
            }
            if (u2.isIncognitoAlliance()) {
                return "&8[" + color + u2.getNickname() + "&8]";
            }
            return "";
        }
        else {
            if (!p.hasPermission(Perms.INCOGNITOSEE.getPermission())) {
                return "";
            }
            return "&8[&6" + u2.getNickname() + "&8]";
        }
    }
    
    public String getTag(final String color, final User u, final User u2, final boolean isAlliance, final boolean isGuild) {
        if (u2.getTag().equalsIgnoreCase("NIEPOSIADA")) {
            return "&7";
        }
        if (!u2.isIncognito()) {
            return color + "[" + u2.getTag().toUpperCase() + "] ";
        }
        if (isGuild) {
            return color + "[" + u2.getTag().toUpperCase() + "] ";
        }
        if (!isAlliance) {
            return color + "[HIDDEN] ";
        }
        if (u2.isIncognitoAlliance()) {
            return color + "[" + u2.getTag().toUpperCase() + "] ";
        }
        return color + "[HIDDEN] ";
    }
    
    public static String getDeviceName(final Player p) {
        switch (p.getLoginChainData().getDeviceOS()) {
            case 1: {
                return "Android";
            }
            case 2: {
                return "iOS";
            }
            case 3: {
                return "MacOS";
            }
            case 4: {
                return "FireOS";
            }
            case 5: {
                return "GearVR";
            }
            case 6: {
                return "HoloLens";
            }
            case 7: {
                return "Windows 10";
            }
            case 8: {
                return "Windows";
            }
            case 9: {
                return "Dedicated";
            }
            case 10: {
                return "PlayStation 4";
            }
            case 11: {
                return "Switch";
            }
            default: {
                return "Unknown";
            }
        }
    }
    
    @EventHandler
    public void onReceive(final DataPacketReceiveEvent e) {
        final User u = UserManager.getUser(e.getPlayer().getName());
        if (e.getPacket() instanceof InventoryTransactionPacket) {
            final InventoryTransactionPacket pa = (InventoryTransactionPacket)e.getPacket();
            if (pa.transactionType == 3) {
                if (u != null && u.acdata.entityLimit()) {
                    Util.sendLog(e.getPlayer().getName(), " " + e.getPlayer().getName() + " -> maxpackets");
                    e.setCancelled(true);
                    e.getPlayer().close(Util.fixColor(Settings.getMessage("maxpackets")));
                    return;
                }
                final UseItemOnEntityData data = (UseItemOnEntityData)pa.transactionData;
                if (data.actionType == 1 && u != null && u.hasMacroLimit()) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        else if (e.getPacket() instanceof AddPlayerPacket) {
            if (!e.getPlayer().hasPermission(Perms.CMD_VANISH.getPermission())) {
                final AddPlayerPacket pa2 = (AddPlayerPacket)e.getPacket();
                final Player o = Server.getInstance().getPlayerExact(pa2.username);
                if (o != null) {
                    final boolean canSeePlayer = EspModule.canSeeEntity(e.getPlayer(), o);
                    if (!canSeePlayer) {
                        e.setCancelled();
                        return;
                    }
                }
            }
            if (u != null && u.isVanish()) {
                e.setCancelled();
                Server.getInstance().getScheduler().scheduleDelayedTask((Task)new Task() {
                    public void onRun(final int i) {
                    }
                }, 20);
            }
        }
        else if (e.getPacket() instanceof MobEquipmentPacket) {
            final MobEquipmentPacket packet = (MobEquipmentPacket)e.getPacket();
            if (packet.hotbarSlot != e.getPlayer().getInventory().getHeldItemIndex()) {
                Cooldown.getInstance().add(e.getPlayer().getName(), "slotchange", 0.4f);
            }
        }
        else if (e.getPacket() instanceof InventoryTransactionPacket) {
            final InventoryTransactionPacket pa = (InventoryTransactionPacket)e.getPacket();
            if (pa.transactionType == 2) {
                if (u != null && u.breakLimit()) {
                    if (!Cooldown.getInstance().has(e.getPlayer(), "speedmine2")) {
                        Util.sendLog(e.getPlayer().getName(), "Speedmine C (BANNED)");
                        Util.banAC(e.getPlayer(), "3d", "gildie");
                        Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "Speedmine - C (BANNED)"));
                        Cooldown.getInstance().add(e.getPlayer(), "speedmine2", 10.0f);
                        Cooldown.getInstance().add(e.getPlayer(), "speedmineban", 10.0f);
                    }
                    e.setCancelled(true);
                    return;
                }
            }
            else if (pa.transactionType == 3) {
                if (u != null && u.acdata.entityLimit()) {
                    e.setCancelled(true);
                    Util.sendLog(e.getPlayer().getName(), " " + e.getPlayer().getName() + " -> maxpackets");
                    e.getPlayer().close(Util.fixColor(Settings.getMessage("maxpackets")));
                    Util.kickPlayer(e.getPlayer(), Settings.getMessage("maxpackets"));
                    return;
                }
                final UseItemOnEntityData data = (UseItemOnEntityData)pa.transactionData;
                if (data.actionType == 1 && u != null && u.hasMacroLimit()) {
                    e.setCancelled(true);
                    return;
                }
                final Entity en = e.getPlayer().getLevel().getEntity(data.entityRuntimeId);
                if (en != null) {
                    if (en.getName().equals(e.getPlayer().getName())) {
                        e.getPlayer().close("rup lud");
                        e.setCancelled(true);
                        if (!Cooldown.getInstance().has(e.getPlayer(), "selfhit")) {
                            Util.sendLog(e.getPlayer().getName(), " Selfhit (BANNED)");
                            Cooldown.getInstance().add(e.getPlayer(), "speedmine2", 10.0f);
                            Cooldown.getInstance().add(e.getPlayer(), "speedmineban", 10.0f);
                            Util.banAC(e.getPlayer(), "3d", "gildie");
                            Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "SH - A (BANNED)"));
                            Cooldown.getInstance().add(e.getPlayer(), "selfhit", 10.0f);
                        }
                        return;
                    }
                    if (e.getPlayer().getGamemode() == 0) {
                        final double dist = en.distance((Vector3)e.getPlayer());
                        double dif = 4.82;
                        if (e.getPlayer().isSprinting()) {
                            dif = 5.05;
                        }
                        if (dist > dif && !Cooldown.getInstance().has(e.getPlayer(), "reach")) {
                            Util.sendLog(e.getPlayer().getName(), " Reach: " + Util.round(dist, 3));
                            Util.sendInformation("infoadmin||" + Settings.getMessage("antycheat").replace("{PLAYER}", e.getPlayer().getName()).replace("{WHAT}", "Reach - Distance: " + Util.round(dist, 3)));
                            Cooldown.getInstance().add(e.getPlayer(), "reach", 3.0f);
                        }
                    }
                }
                else {
                    e.setCancelled(true);
                }
            }
        }
        if (u != null && u.packetLimit()) {
            e.setCancelled(true);
            Util.sendLog(e.getPlayer().getName(), " " + e.getPlayer().getName() + " -> maxpackets");
            e.getPlayer().close(Util.fixColor(Settings.getMessage("maxpackets")));
            Util.kickPlayer(e.getPlayer(), Settings.getMessage("maxpackets"));
            return;
        }
        if (e.getPacket() instanceof LevelSoundEventPacket) {
            if (DataPacketEvents.sounds.contains(((LevelSoundEventPacket)e.getPacket()).sound)) {
                e.setCancelled(true);
                final Player p = e.getPlayer();
                if (u != null) {
                    if (u.hasMacroMax()) {
                        Util.sendLog(e.getPlayer().getName(), " " + e.getPlayer().getName() + " -> maxpackets");
                        e.getPlayer().close(Util.fixColor(Settings.getMessage("maxpackets")));
                        Util.kickPlayer(e.getPlayer(), Settings.getMessage("maxpackets"));
                        return;
                    }
                    if (u.macroLimit()) {
                        e.getPlayer().sendTitle(Util.fixColor("&l&4LIMIT CPS"), Util.fixColor("&cMaksymalna ilosc CPS: " + Settings.LIMIT_MACRO));
                    }
                }
                else {
                    Main.getPlugin().getLogger().info("Datapacket receive null user: " + p.getName());
                }
            }
        }
        else if (e.getPacket() instanceof PlaySoundPacket) {
            if (((PlaySoundPacket)e.getPacket()).name.equalsIgnoreCase(Sound.GAME_PLAYER_ATTACK_STRONG.getSound())) {
                e.setCancelled();
                final Player p = e.getPlayer();
                if (u != null) {
                    if (u.hasMacroMax()) {
                        Util.sendLog(e.getPlayer().getName(), " " + e.getPlayer().getName() + " -> maxpackets");
                        e.getPlayer().close(Util.fixColor(Settings.getMessage("maxpackets")));
                        Util.kickPlayer(e.getPlayer(), Settings.getMessage("maxpackets"));
                        return;
                    }
                    if (u.macroLimit()) {
                        e.getPlayer().sendTitle(Util.fixColor("&l&4LIMIT CPS"), Util.fixColor("&cMaksymalna ilosc CPS: " + Settings.LIMIT_MACRO));
                    }
                }
                else {
                    Main.getPlugin().getLogger().info("Datapacket receive null user: " + p.getName());
                }
            }
        }
        else if (e.getPacket() instanceof InventoryTransactionPacket) {
            final TransactionData td = ((InventoryTransactionPacket)e.getPacket()).transactionData;
            if (td instanceof ReleaseItemData) {
                if (((ReleaseItemData)td).actionType == 0) {
                    DataPacketEvents.eatings.remove(e.getPlayer().getName());
                }
            }
            else if (td instanceof UseItemData) {
                final Item item = e.getPlayer().getInventory().getItemInHand();
                if (item != null && !Config.isSpawn(e.getPlayer().getLocation()) && (item.getId() == 322 || item.getId() == 466 || item.getId() == 262 || item.getId() == 332 || item.getId() == 368 || Util.hasNBTTag(item, "rzucanetnt")) && u != null) {
                    DepositUtil.reduce(e.getPlayer(), u);
                }
            }
        }
        else if (e.getPacket() instanceof EntityEventPacket) {
            final int event = ((EntityEventPacket)e.getPacket()).event;
            final Item item = e.getPlayer().getInventory().getItemInHand();
            if (event == 57 && (item instanceof ItemAppleGoldEnchanted || item instanceof ItemAppleGold)) {
                if (!DataPacketEvents.eatings.containsKey(e.getPlayer().getName())) {
                    DataPacketEvents.eatings.put(e.getPlayer().getName(), new Eatings(item));
                    return;
                }
                if (!item.equalsExact(DataPacketEvents.eatings.get(e.getPlayer().getName()).item)) {
                    DataPacketEvents.eatings.replace(e.getPlayer().getName(), new Eatings(item));
                    return;
                }
                final Eatings eatings = DataPacketEvents.eatings.get(e.getPlayer().getName());
                ++eatings.times;
                if (DataPacketEvents.eatings.get(e.getPlayer().getName()).times >= 7) {
                    final Food food = Food.getByRelative(item);
                    e.getPlayer().setUsingItem(false);
                    food.eatenBy(e.getPlayer());
                    item.setCount(item.getCount() - 1);
                    e.getPlayer().getInventory().setItemInHand(item);
                    DataPacketEvents.eatings.remove(e.getPlayer().getName());
                }
            }
        }
    }
    
    static {
        DataPacketEvents.sounds = Arrays.asList(41, 42, 43, 130, 235, 228);
        DataPacketEvents.eatings = new HashMap<String, Eatings>();
    }
    
    class Eatings
    {
        public int times;
        public Item item;
        
        public Eatings(final Item item) {
            this.times = 1;
            this.item = item;
        }
    }
}
