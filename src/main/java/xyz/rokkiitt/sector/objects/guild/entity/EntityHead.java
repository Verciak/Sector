package xyz.rokkiitt.sector.objects.guild.entity;

import cn.nukkit.nbt.tag.*;
import cn.nukkit.entity.*;
import cn.nukkit.math.*;
import cn.nukkit.entity.data.*;
import java.io.*;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildHeartGUI;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.wings.Wings;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;

import java.nio.charset.*;

import cn.nukkit.event.entity.*;
import cn.nukkit.*;
import cn.nukkit.command.*;
import cn.nukkit.inventory.*;
import cn.nukkit.event.player.*;
import java.util.concurrent.*;
import cn.nukkit.level.*;
import java.util.*;
import cn.nukkit.item.*;

public class EntityHead extends EntityHuman
{
    private Guild g;
    private int y;
    private long lasthit;
    
    public static CompoundTag getNbt(final Guild g) {
        final CompoundTag nbt = Entity.getDefaultNBT((Vector3)g.getCuboid().getCenter().add(0.5, 1.0, 0.5));
        final Skin skin = new Skin();
        skin.setSkinId("heart-" + g.getTag().toLowerCase());
        skin.setSkinData(Wings.fromImage(new File(Main.getPlugin().getDataFolder(), "/skins/guild/" + g.getHearttype() + ".png").toPath()));
        skin.setGeometryName("geometry.player_head");
        skin.setGeometryData("{\r\n\t\"format_version\": \"1.16.1\",\r\n\t\"minecraft:geometry\": [\r\n\t\t{\r\n\t\t\t\"description\": {\r\n\t\t\t\t\"identifier\": \"geometry.player_head\",\r\n\t\t\t\t\"texture_width\": 64,\r\n\t\t\t\t\"texture_height\": 64,\r\n\t\t\t\t\"visible_bounds_width\": 4,\r\n\t\t\t\t\"visible_bounds_height\": 3,\r\n\t\t\t\t\"visible_bounds_offset\": [0, 0, 0]\r\n\t\t\t},\r\n\t\t\t\"bones\": [\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"root\",\r\n\t\t\t\t\t\"pivot\": [0, 0, 0]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"waist\",\r\n\t\t\t\t\t\"parent\": \"root\",\r\n\t\t\t\t\t\"pivot\": [0, 12, 0]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"body\",\r\n\t\t\t\t\t\"parent\": \"waist\",\r\n\t\t\t\t\t\"pivot\": [0, 24, 0]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"head\",\r\n\t\t\t\t\t\"parent\": \"body\",\r\n\t\t\t\t\t\"pivot\": [0, 24, 0],\r\n\t\t\t\t\t\"cubes\": [\r\n\t\t\t\t\t\t{\"origin\": [-4, 1, -4], \"size\": [8, 8, 8], \"uv\": [0, 0]}\r\n\t\t\t\t\t]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"hat\",\r\n\t\t\t\t\t\"parent\": \"head\",\r\n\t\t\t\t\t\"pivot\": [0, 24, 0],\r\n\t\t\t\t\t\"cubes\": [\r\n\t\t\t\t\t\t{\"origin\": [-4, 1, -4], \"size\": [8, 8, 8], \"inflate\": 0.5, \"uv\": [32, 0]}\r\n\t\t\t\t\t]\r\n\t\t\t\t}\r\n\t\t\t]\r\n\t\t}\r\n\t]\r\n}");
        final CompoundTag skinTag = new CompoundTag().putByteArray("Data", skin.getSkinData().data).putInt("SkinImageWidth", skin.getSkinData().width).putInt("SkinImageHeight", skin.getSkinData().height).putString("ModelId", skin.getSkinId()).putString("CapeId", skin.getCapeId()).putByteArray("CapeData", skin.getCapeData().data).putInt("CapeImageWidth", skin.getCapeData().width).putInt("CapeImageHeight", skin.getCapeData().height).putByteArray("SkinResourcePatch", skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8)).putByteArray("GeometryData", skin.getGeometryData().getBytes(StandardCharsets.UTF_8)).putByteArray("AnimationData", skin.getAnimationData().getBytes(StandardCharsets.UTF_8)).putBoolean("PremiumSkin", skin.isPremium()).putBoolean("PersonaSkin", skin.isPersona()).putBoolean("CapeOnClassicSkin", skin.isCapeOnClassic());
        nbt.putCompound("Skin", skinTag);
        nbt.putString("NameTag", Util.fixColor(g.getHeartcolor() + g.getTag().toUpperCase() + "\n" + getHeartString(g) + "\n" + getTimeString(g)));
        return nbt;
    }
    
    public boolean attack(final EntityDamageEvent e) {
        e.setCancelled(true);
        if (!Settings.ENABLE_TNT) {
            return false;
        }
        if (e instanceof EntityDamageByEntityEvent) {
            final Entity damager = ((EntityDamageByEntityEvent)e).getDamager();
            if (damager instanceof Player) {
                final User u = UserManager.getUser(((Player)damager).getName());
                if (u != null) {
                    if (u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                        return Util.sendMessage((CommandSender)damager, Settings.getMessage("donthaveguild"));
                    }
                    if (this.g.getTag().equalsIgnoreCase(u.getTag()) && !CombatManager.isContains(((Player)damager).getName())) {
                        if (u.hasPermission("22")) {
                            ((Player)damager).addWindow(new GuildHeartGUI(this.g));
                        }
                        else {
                            Util.sendMessage((CommandSender)damager, Settings.getMessage("guildpermission").replace("{TYPE}", "zarzadzania sercem gildii!"));
                        }
                        return false;
                    }
                    if (!this.g.getTag().equalsIgnoreCase(u.getTag()) && !u.getAlliances().contains(this.g.getTag())) {
                        if (this.lasthit >= System.currentTimeMillis()) {
                            return false;
                        }
                        if (!this.g.getHeart().isInHeart(((Player)damager).getLocation())) {
                            return false;
                        }
                        if (this.g.getHeartProtectionTime() >= System.currentTimeMillis()) {
                            return false;
                        }
                        this.lasthit = System.currentTimeMillis() + 2000L;
                        final int max = 3;
                        final int min = 1;
                        int hp = this.g.getHearthp();
                        final int val = (int)(Math.random() * max + min);
                        final int val2;
                        hp = (val2 = hp - val);
                        if (hp <= 0) {
                            Guild ga = GuildManager.getGuild(u.getTag());
                            ga.setHearts(ga.getHearts() + 1);
                            if (this.g.getHearts() <= 1) {
                                GuildManager.deleteGuild(this.g);

                                Util.sendInformation(Settings.getMessage("guilddestroyed").replace("{PLAYER}", ((Player)damager).getName()).replace("{TAG}", this.g.getTag()));
                                return false;
                            }
                            final Guild g = this.g;
                            g.setHearts(g.getHearts() - 1);
                            this.g.setHeartProtectionTime(System.currentTimeMillis() + Time.HOUR.getTime(24));
                            g.setHearthp(100);
                            Util.sendInformation(Settings.getMessage("guildtaken").replace("{PLAYER}", ((Player)damager).getName()).replace("{TAG}", this.g.getTag()));
                        }
                        else {
                            this.g.setHeartRegen(System.currentTimeMillis() + 15000L);
                            this.g.setHearthp(val2);
                        }
                        ((Player)damager).knockBack(damager, 0.0, damager.getPosition().subtract((Vector3)this.getPosition()).getX(), damager.getPosition().subtract((Vector3)this.getPosition()).getZ(), 0.5);
                    }
                }
                else {
                    ((Player)damager).kick(Util.fixColor("&9not properly loaded data - EntityHead"));
                }
            }
        }
        return false;
    }
    
    public boolean entityBaseTick(final int tickDiff) {
        if (this.g == null) {
            return false;
        }
        this.setRotation(this.getLocation().getYaw() + 1.0, 0.0);
        if (this.y > 61) {
            this.y = 0;
        }
        if (this.y <= 30) {
            ++this.y;
            this.teleport(new Vector3(this.getLocation().getX(), this.getLocation().getY() - 0.01, this.getLocation().getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
        else {
            ++this.y;
            this.teleport(new Vector3(this.getLocation().getX(), this.getLocation().getY() + 0.01, this.getLocation().getZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
        if (this.ticksLived % 20 == 0) {
            this.setNameTag(Util.fixColor(this.g.getHeartcolor() + this.g.getTag().toUpperCase() + "\n" + this.getHeartString() + "\n" + this.getTimeString()));
            this.g.heartRegen();
            final Map<Integer, Player> b = new ConcurrentHashMap<Integer, Player>(this.getViewers());
            for (final Map.Entry<Integer, Player> p : b.entrySet()) {
                if (!this.g.getHeart().isInHeart((Location)p.getValue())) {
                    this.despawnFrom((Player)p.getValue());
                    if (this.getViewers().isEmpty()) {
                        this.g.setHead(null);
                        this.close();
                        break;
                    }
                    continue;
                }
            }
        }
        return super.entityBaseTick(tickDiff);
    }
    
    public static String getTimeString(final Guild g) {
        if (g.getHeartProtectionTime() >= System.currentTimeMillis()) {
            return g.getHeartcolor() + Util.formatDotTime(g.getHeartProtectionTime() - System.currentTimeMillis());
        }
        g.heartRegen();
        return g.getHearthp() + "&7/" + g.getHeartcolor() + "100";
    }
    
    public String getTimeString() {
        if (this.g.getHeartProtectionTime() >= System.currentTimeMillis()) {
            return this.g.getHeartcolor() + Util.formatDotTime(this.g.getHeartProtectionTime() - System.currentTimeMillis());
        }
        this.g.heartRegen();
        return this.g.getHeartcolor() + this.g.getHearthp() + "&7/" + this.g.getHeartcolor() + "100";
    }
    
    private static String color2(final Guild g) {
        if (g.getHeartcolor().equalsIgnoreCase("&0")) {
            return "&8";
        }
        return "&0";
    }
    
    public static String getHeartString(final Guild g) {
        final String xd11 = Util.fixColor(g.getHeartcolor() + "\u2764");
        final String xd12 = Util.fixColor(color2(g) + "\u2764");
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < g.getHearts(); ++i) {
            sb.append(xd11);
        }
        for (int i = 0; i < 5 - g.getHearts(); ++i) {
            sb2.append(xd12);
        }
        final String string = sb.toString();
        final String string2 = sb2.toString();
        return string + string2;
    }
    
    private String color2() {
        if (this.g.getHeartcolor().equalsIgnoreCase("&0")) {
            return "&8";
        }
        return "&0";
    }
    
    public String getHeartString() {
        final String xd11 = Util.fixColor(this.g.getHeartcolor() + "\u2764");
        final String xd12 = Util.fixColor(this.color2() + "\u2764");
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < this.g.getHearts(); ++i) {
            sb.append(xd11);
        }
        for (int i = 0; i < 5 - this.g.getHearts(); ++i) {
            sb2.append(xd12);
        }
        final String string = sb.toString();
        final String string2 = sb2.toString();
        return string + string2;
    }
    
    public EntityHead(final Guild g) {
        super(g.getCuboid().getCenter().getChunk(), getNbt(g));
        this.y = 0;
        this.lasthit = System.currentTimeMillis();
        this.g = g;
    }
    
    public void setSkin(final String type) {
        final Skin skin = new Skin();
        skin.setSkinId("heart-" + this.g.getTag().toLowerCase());
        skin.setSkinData(Wings.fromImage(new File(Main.getPlugin().getDataFolder(), "/skins/guild/" + type + ".png").toPath()));
        skin.setGeometryName("geometry.player_head");
        skin.setGeometryData("{\r\n\t\"format_version\": \"1.16.1\",\r\n\t\"minecraft:geometry\": [\r\n\t\t{\r\n\t\t\t\"description\": {\r\n\t\t\t\t\"identifier\": \"geometry.player_head\",\r\n\t\t\t\t\"texture_width\": 64,\r\n\t\t\t\t\"texture_height\": 64,\r\n\t\t\t\t\"visible_bounds_width\": 4,\r\n\t\t\t\t\"visible_bounds_height\": 3,\r\n\t\t\t\t\"visible_bounds_offset\": [0, 0, 0]\r\n\t\t\t},\r\n\t\t\t\"bones\": [\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"root\",\r\n\t\t\t\t\t\"pivot\": [0, 0, 0]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"waist\",\r\n\t\t\t\t\t\"parent\": \"root\",\r\n\t\t\t\t\t\"pivot\": [0, 12, 0]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"body\",\r\n\t\t\t\t\t\"parent\": \"waist\",\r\n\t\t\t\t\t\"pivot\": [0, 24, 0]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"head\",\r\n\t\t\t\t\t\"parent\": \"body\",\r\n\t\t\t\t\t\"pivot\": [0, 24, 0],\r\n\t\t\t\t\t\"cubes\": [\r\n\t\t\t\t\t\t{\"origin\": [-4, 1, -4], \"size\": [8, 8, 8], \"uv\": [0, 0]}\r\n\t\t\t\t\t]\r\n\t\t\t\t},\r\n\t\t\t\t{\r\n\t\t\t\t\t\"name\": \"hat\",\r\n\t\t\t\t\t\"parent\": \"head\",\r\n\t\t\t\t\t\"pivot\": [0, 24, 0],\r\n\t\t\t\t\t\"cubes\": [\r\n\t\t\t\t\t\t{\"origin\": [-4, 1, -4], \"size\": [8, 8, 8], \"inflate\": 0.5, \"uv\": [32, 0]}\r\n\t\t\t\t\t]\r\n\t\t\t\t}\r\n\t\t\t]\r\n\t\t}\r\n\t]\r\n}");
        super.setSkin(skin);
        this.g.setHearttype(type);
        final List<Player> view = new ArrayList<Player>(this.getViewers().values());
        for (final Player p : view) {
            this.despawnFrom(p);
        }
        this.getServer().getScheduler().scheduleDelayedTask(() -> {
            Iterator<Player> iterator2 = view.iterator();
            while (iterator2.hasNext()) {
                Player p2 = iterator2.next();
                this.spawnTo(p2);
            }
        }, 1);
    }
    
    public float getWidth() {
        return 0.5f;
    }
    
    public float getLength() {
        return 0.5f;
    }
    
    public float getHeight() {
        return 0.6f;
    }
    
    public Item[] getDrops() {
        return new Item[0];
    }
    
    public void collidingWith(final Entity ent) {
    }
    
    protected float getGravity() {
        return 0.0f;
    }
    
    public void setCanClimb(final boolean value) {
        this.setDataFlag(0, 19, false);
    }
    
    public void setCanClimbWalls(final boolean value) {
        this.setDataFlag(0, 18, false);
    }
    
    public void setOnFire(final int seconds) {
        super.setOnFire(0);
    }
    
    public boolean isOnFire() {
        return false;
    }
    
    public boolean canTriggerWalking() {
        return true;
    }
    
    public boolean isInsideOfWater() {
        return false;
    }
    
    public boolean isInsideOfSolid() {
        return false;
    }
    
    public boolean isInsideOfFire() {
        return false;
    }
    
    public boolean isOnLadder() {
        return false;
    }
    
    public boolean canBeMovedByCurrents() {
        return true;
    }
    
    public boolean doesTriggerPressurePlate() {
        return false;
    }
    
    public boolean canPassThrough() {
        return false;
    }
}
