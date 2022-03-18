package xyz.rokkiitt.sector.objects.user;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import xyz.rokkiitt.sector.objects.drop.DropManager;
import xyz.rokkiitt.sector.objects.home.PlayerHomeData;
import xyz.rokkiitt.sector.utils.ItemSerializer;
import xyz.rokkiitt.sector.utils.Util;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.waypoint.Waypoint;
import xyz.rokkiitt.sector.objects.waypoint.WaypointData;
import xyz.rokkiitt.sector.utils.Time;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class User {



    public ACData acdata = new ACData();
    private String nickname;
    private long macroPerSecondTime;
    private int macroPerSecond;
    private long breakPerSecondTime;
    private int breakPerSecond;
    private long packetsPerSecondTime;
    private int packetsPerSecond;
    private String rank;
    private boolean isVanish;
    private boolean isGod;
    private long protection;
    private long turbodrop;
    private Set<String> disableddrops = ConcurrentHashMap.newKeySet();
    private Skin skin;
    private String wings;
    private Set<String> guildpermissions;
    private boolean pvp;
    private boolean allypvp;
    private String tag;
    private Set<String> invites = ConcurrentHashMap.newKeySet();
    private Set<String> alliances = ConcurrentHashMap.newKeySet();
    private Map<String, Double> assistattackers = new ConcurrentHashMap<>();
    private Map<String, Long> lastkills = new ConcurrentHashMap<>();
    private int kills;
    private int deaths;
    private int points;
    private int assist;
    private String location;
    private String lastattacker;
    private int egapple;
    private int gapple;
    private int arrows;
    private int primedtnt;
    private int snowballs;
    private int pearls;
    private long viptime;
    private long sviptime;
    private long sponsortime;
    private long foodtime;
    private long endertime;
    private long playertime;
    private int onlineoverall;
    private int onlinetime;
    private int onlinesession;
    private boolean firstLogin = false;
    private long mutetime;
    private Set<String> ignored = ConcurrentHashMap.newKeySet();
    private boolean alertsenabled;
    private int goldblocks;
    private int placed;
    private int broken;
    private String replyPlayer;
    private Set<PlayerHomeData> homes = ConcurrentHashMap.newKeySet();
    private Set<WaypointData> waypoints = ConcurrentHashMap.newKeySet();
    private Set<Waypoint> activeWaypoints = ConcurrentHashMap.newKeySet();
    private Set<String> teleportRequest = ConcurrentHashMap.newKeySet();
    private boolean isIncognito;
    private String incognito;
    private boolean IncognitoAlliance;
    private boolean IncognitoDead;
    private boolean IncognitoKill;
    private boolean isTrading = false;
    private Set<String> tradeInvites = ConcurrentHashMap.newKeySet();
    private String chapel;

    public void setAcdata(ACData acdata) {
        this.acdata = acdata;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMacroPerSecondTime(long macroPerSecondTime) {
        this.macroPerSecondTime = macroPerSecondTime;
    }

    public void setMacroPerSecond(int macroPerSecond) {
        this.macroPerSecond = macroPerSecond;
    }

    public void setBreakPerSecondTime(long breakPerSecondTime) {
        this.breakPerSecondTime = breakPerSecondTime;
    }

    public void setBreakPerSecond(int breakPerSecond) {
        this.breakPerSecond = breakPerSecond;
    }

    public void setPacketsPerSecondTime(long packetsPerSecondTime) {
        this.packetsPerSecondTime = packetsPerSecondTime;
    }

    public void setPacketsPerSecond(int packetsPerSecond) {
        this.packetsPerSecond = packetsPerSecond;
    }

    public void setRank(String rank) {
        this.rank = rank;
        Main.getProvider().update("UPDATE `users` SET `rank` ='" + getRank() + "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setVanish(boolean vanish) {
        isVanish = vanish;
        Main.getProvider().update("UPDATE `users` SET `isVanish` ='" + (getVanish() ? 1 : 0)+ "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setGod(boolean god) {
        isGod = god;
        Main.getProvider().update("UPDATE `users` SET `isGod` ='" + (isGod() ? 1 : 0)+ "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setProtection(long protection) {
        this.protection = protection;
        Main.getProvider().update("UPDATE `users` SET `protection` ='" + getProtection()+ "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setTurbodrop(long turbodrop) {
        this.turbodrop = turbodrop;
        Main.getProvider().update("UPDATE `users` SET `turbodrop` ='" + getTurbodrop()+ "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setDisableddrops(Set<String> disableddrops) {
        this.disableddrops = disableddrops;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;

    }

    public void setWings(String wings) {
        this.wings = wings;
        Main.getProvider().update("UPDATE `users` SET `wings` ='" + getWings()+"' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setGuildpermissions(Set<String> guildpermissions) {
        this.guildpermissions = guildpermissions;
        Main.getProvider().update("UPDATE `users` SET `guildperms` ='" + guildpermissions + "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
        Main.getProvider().update("UPDATE `users` SET `pvp` ='" + (getPVP() ? 1 : 0) + "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setAllypvp(boolean allypvp) {
        this.allypvp = allypvp;
        Main.getProvider().update("UPDATE `users` SET `allypvp` ='" + (getAllyPVP() ? 1 : 0) + "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setTag(String tag) {
        this.tag = tag;
        Main.getProvider().update("UPDATE `users` SET `tag` ='" + getTag() + "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setInvites(Set<String> invites) {
        this.invites = invites;
    }

    public void setAlliances(Set<String> alliances) {
        this.alliances = alliances;
    }

    public void setAssistattackers(Map<String, Double> assistattackers) {
        this.assistattackers = assistattackers;
    }

    public void setLastkills(Map<String, Long> lastkills) {
        this.lastkills = lastkills;
    }

    public void setLastattacker(String lastattacker) {
        this.lastattacker = lastattacker;
    }

    public void setEgapple(int egapple) {
        this.egapple = egapple;
        Main.getProvider().update("UPDATE `users` SET `egapple` ='" + getEgapple() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setGapple(int gapple) {
        this.gapple = gapple;
        Main.getProvider().update("UPDATE `users` SET `gapple` ='" + getGapple() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setArrows(int arrows) {
        this.arrows = arrows;
        Main.getProvider().update("UPDATE `users` SET `arrows` ='" + getArrows() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setPrimedtnt(int primedtnt) {
        this.primedtnt = primedtnt;
        Main.getProvider().update("UPDATE `users` SET `primedtnt` ='" + getPrimedtnt() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setSnowballs(int snowballs) {
        this.snowballs = snowballs;
        Main.getProvider().update("UPDATE `users` SET `snowballs` ='" + getSnowballs() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setPearls(int pearls) {
        this.pearls = pearls;
        Main.getProvider().update("UPDATE `users` SET `pearls` ='" + getPearls() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setViptime(long viptime) {
        this.viptime = viptime;
        Main.getProvider().update("UPDATE `users` SET `viptime` ='" + getViptime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setSviptime(long sviptime) {
        this.sviptime = sviptime;
        Main.getProvider().update("UPDATE `users` SET `sviptime` ='" + getSviptime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setSponsortime(long sponsortime) {
        this.sponsortime = sponsortime;
        Main.getProvider().update("UPDATE `users` SET `sponsortime` ='" + getSponsortime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setFoodtime(long foodtime) {
        this.foodtime = foodtime;
        Main.getProvider().update("UPDATE `users` SET `foodtime` ='" + getFoodtime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setEndertime(long endertime) {
        this.endertime = endertime;
        Main.getProvider().update("UPDATE `users` SET `endertime` ='" + getEndertime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setPlayertime(long playertime) {
        this.playertime = playertime;
        Main.getProvider().update("UPDATE `users` SET `playertime` ='" + getPlayertime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setOnlineoverall(int onlineoverall) {
        this.onlineoverall = onlineoverall;
        Main.getProvider().update("UPDATE `users` SET `onlineoverall` ='" + getOnlineoverall() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setOnlinetime(int onlinetime) {
        this.onlinetime = onlinetime;
        Main.getProvider().update("UPDATE `users` SET `onlinetime` ='" + getOnlinetime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setOnlinesession(int onlinesession) {
        this.onlinesession = onlinesession;
        Main.getProvider().update("UPDATE `users` SET `onlinesession` ='" + getOnlinesession() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
        Main.getProvider().update("UPDATE `users` SET `firstLogin` ='" + (isFirstLogin() ? 1 : 0) + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setMutetime(long mutetime) {
        this.mutetime = mutetime;
        Main.getProvider().update("UPDATE `users` SET `mutetime` ='" + getMutetime() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setIgnored(Set<String> ignored) {
        this.ignored = ignored;
    }

    public void setAlertsenabled(boolean alertsenabled) {
        this.alertsenabled = alertsenabled;
    }

    public void setGoldblocks(int goldblocks) {
        this.goldblocks = goldblocks;
        Main.getProvider().update("UPDATE `users` SET `goldblocks` ='" + getGoldblocks() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setPlaced(int placed) {
        this.placed = placed;
        Main.getProvider().update("UPDATE `users` SET `placed` ='" + getPlaced() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setBroken(int broken) {
        this.broken = broken;
        Main.getProvider().update("UPDATE `users` SET `broken` ='" + getBroken() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setReplyPlayer(String replyPlayer) {
        this.replyPlayer = replyPlayer;
    }

    public void setHomes(Set<PlayerHomeData> homes) {
        this.homes = homes;
        Main.getProvider().update("UPDATE `users` SET `homes` ='" + PlayerHomeData.serialize(this) + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setWaypoints(Set<WaypointData> waypoints) {
        this.waypoints = waypoints;
        Main.getProvider().update("UPDATE `users` SET `waypoints` ='" + WaypointData.serialize(this) + "' WHERE `nickname` ='" + getNickname() + "'");
    }

    public void setActiveWaypoints(Set<Waypoint> activeWaypoints) {
        this.activeWaypoints = activeWaypoints;
    }

    public void setTeleportRequest(Set<String> teleportRequest) {
        this.teleportRequest = teleportRequest;
    }

    public void setIncognito(boolean incognito) {
        isIncognito = incognito;
        Main.getProvider().update("UPDATE `users` SET `isIncognito` ='" + (isIncognito() ? 1 : 0) + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setIncognito(String incognito) {
        this.incognito = incognito;
        Main.getProvider().update("UPDATE `users` SET `incognito` ='" + getIncognito() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setIncognitoAlliance(boolean incognitoAlliance) {
        IncognitoAlliance = incognitoAlliance;
        Main.getProvider().update("UPDATE `users` SET `IncognitoAlliance` ='" + (isIncognitoAlliance() ? 1 : 0) + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setIncognitoDead(boolean incognitoDead) {
        IncognitoDead = incognitoDead;
        Main.getProvider().update("UPDATE `users` SET `IncognitoDead` ='" + (isIncognitoDead() ? 1 : 0) + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setIncognitoKill(boolean incognitoKill) {
        IncognitoKill = incognitoKill;
        Main.getProvider().update("UPDATE `users` SET `IncognitoKill` ='" + (isIncognitoKill() ? 1 : 0) + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setTrading(boolean trading) {
        isTrading = trading;
    }

    public void setTradeInvites(Set<String> tradeInvites) {
        this.tradeInvites = tradeInvites;
    }

    public void setChapel(String chapel) {
        this.chapel = chapel;
        Main.getProvider().update("UPDATE `users` SET `chapel` ='" + getChapel() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String l) {
        this.location = l;
        Main.getProvider().update("UPDATE `users` SET `location` ='" + getLocation() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setWallLocations(Set<Location> wallLocations) {
        this.wallLocations = wallLocations;
    }
    

    public void save(){
    }

    private void insert() {
        Main.getDatabase().addQueue("INSERT INTO `users`(`id`, " +
                "`nickname`, " +
                "`wings`, " +
                "`rank`, " +
                "`tag`, " +
                "`allypvp`, " +
                "`pvp`, " +
                "`isGod`, " +
                "`kills`, " +
                "`deaths`," +
                "`assist`," +
                "`points`, " +
                "`isVanish`," +
                "`protection`, " +
                "`turbodrop`, " +
                "`egapple`, " +
                "`gapple`, " +
                "`arrows`, " +
                "`primedtnt`, " +
                "`snowballs`, " +
                "`pearls`, " +
                "`viptime`, " +
                "`sviptime`, " +
                "`sponsortime`, " +
                "`foodtime`, " +
                "`endertime`, " +
                "`playertime`, " +
                "`onlineoverall`, " +
                "`onlinetime`, " +
                "`onlinesession`, " +
                "`firstLogin`," +
                "`mutetime`," +
                "`alertsenabled`," +
                "`goldblocks`," +
                "`placed`, " +
                "`broken`, " +
                "`homes`, " +
                "`waypoints`, " +
                "`isIncognito`, " +
                "`incognito`, " +
                "`IncognitoAlliance`, " +
                "`IncognitoDead`," +
                "`IncognitoKill`," +
                "`chapel`," +
                "`location`," +
                "`guildperms`) VALUES (" +
                "NULL, " +
                "'" + getNickname() + "'," +
                "'" + getWings() + "'," +
                "'" + getRank() + "'," +
                "'" + getTag() + "'," +
                "'" + (getAllyPVP() ? 1 : 0) + "'," +
                "'" + (getPVP() ? 1 : 0) + "'," +
                "'" + (isGod() ? 1 : 0) + "'," +
                "'" + getKills() + "'," +
                "'" + getDeaths() + "'," +
                "'" + getAssist() + "'," +
                "'" + getPoints() + "'," +
                "'" + (getVanish() ? 1 : 0) + "'," +
                "'" + getProtection() + "'," +
                "'" + getTurbodrop() + "'," +
                "'" + getEgapple() + "'," +
                "'" + getGapple() + "'," +
                "'" + getArrows() + "'," +
                "'" + getPrimedtnt() + "'," +
                "'" + getSnowballs() + "'," +
                "'" + getPearls() + "'," +
                "'" + getViptime() + "'," +
                "'" + getSviptime() + "'," +
                "'" + getSponsortime() + "'," +
                "'" + getFoodtime() + "'," +
                "'" + getEndertime() + "'," +
                "'" + getPlayertime() + "'," +
                "'" + getOnlineoverall() + "'," +
                "'" + getOnlinetime() + "'," +
                "'" + getOnlinesession() + "'," +
                "'" + (isFirstLogin() ? 1 : 0) + "'," +
                "'" + getMutetime() + "'," +
                "'" + (isAlertsenabled() ? 1 : 0) + "'," +
                "'" + getGoldblocks() + "'," +
                "'" + getPlaced() + "'," +
                "'" + getBroken() + "'," +
                "'" + PlayerHomeData.serialize(this) + "'," +
                "'" + WaypointData.serialize(this)+  "'," +
                "'" + (isIncognito() ? 1 : 0) + "'," +
                "'" + getIncognito() + "'," +
                "'" + (isIncognitoAlliance() ? 1 : 0) + "'," +
                "'" + (isIncognitoDead() ? 1 : 0) + "'," +
                "'" + (isIncognitoKill() ? 1 : 0) + "'," +
                "'" + getChapel() + "'," +
                "'"+ getLocation() +"'," +
                "'"+ getGuildPermissions(getPermissionString()) +"')");
    }

    public User(ResultSet set) throws SQLException {
        this.nickname = set.getString("nickname");
        this.wings = set.getString("wings");
        this.rank = set.getString("rank");
        this.tag = set.getString("tag");
        this.allypvp = set.getBoolean("allypvp");
        this.pvp = set.getBoolean("pvp");
        this.alliances.addAll(this.alliances);
        this.isGod = set.getBoolean("isGod");
        this.kills = set.getInt("kills");
        this.deaths = set.getInt("deaths");
        this.assist = set.getInt("assist");
        this.points = set.getInt("points");
        this.isVanish = set.getBoolean("isVanish");
        this.protection = set.getLong("protection");
        this.turbodrop = set.getLong("turbodrop");
        this.egapple = set.getInt("egapple");
        this.gapple = set.getInt("gapple");
        this.arrows = set.getInt("arrows");
        this.primedtnt = set.getInt("primedtnt");
        this.snowballs = set.getInt("snowballs");
        this.pearls = set.getInt("pearls");
        this.viptime = set.getLong("viptime");
        this.sviptime = set.getLong("sviptime");
        this.sponsortime = set.getLong("sponsortime");
        this.foodtime = set.getLong("foodtime");
        this.endertime = set.getLong("endertime");
        this.playertime = set.getLong("playertime");
        this.onlineoverall = set.getInt("onlineoverall");
        this.onlinetime = set.getInt("onlinetime");
        this.onlinesession = set.getInt("onlinesession");
        this.firstLogin = set.getBoolean("firstLogin");
        this.mutetime = set.getLong("mutetime");
        this.ignored.addAll(this.ignored);
        this.alertsenabled = set.getBoolean("alertsenabled");
        this.goldblocks = set.getInt("goldblocks");
        this.placed = set.getInt("placed");
        this.broken = set.getInt("broken");
        this.homes = PlayerHomeData.deserialize(set.getString("homes"));
        this.waypoints = WaypointData.deserialize(set.getString("waypoints"));
        this.isIncognito = set.getBoolean("isIncognito");
        this.incognito = set.getString("incognito");
        this.IncognitoAlliance = set.getBoolean("IncognitoAlliance");
        this.IncognitoDead = set.getBoolean("IncognitoDead");
        this.IncognitoKill = set.getBoolean("IncognitoKill");
        this.chapel = set.getString("chapel");
        this.location = set.getString("location");
        this.guildpermissions = getGuildPermissions(set.getString("guildperms"));
    }


    public Player getPlayer() {
        return Server.getInstance().getPlayer(getNickname());
    }


    public User(Player player) {
        this.nickname = player.getName();
        this.wings = "";
        this.rank = "gracz";
        this.tag = "NIEPOSIADA";
        this.allypvp = false;
        this.pvp = false;
        this.alliances.addAll(this.alliances);
        this.isGod = false;
        this.kills = 0;
        this.deaths = 0;
        this.assist = 0;
        this.points = 1000;
        this.isVanish = false;
        this.protection = 0L;
        this.turbodrop = 0L;
        this.guildpermissions = Collections.singleton("|&|");
        this.egapple = 0;
        this.gapple = 0;
        this.arrows = 0;
        this.primedtnt = 0;
        this.snowballs = 0;
        this.pearls = 0;
        this.viptime = 0L;
        this.sviptime = 0L;
        this.sponsortime = 0L;
        this.foodtime = 0L;
        this.endertime = 0L;
        this.playertime = 0L;
        this.onlineoverall = 0;
        this.onlinetime = 0;
        this.onlinesession = 0;
        this.firstLogin = false;
        this.mutetime = 0L;
        this.ignored.addAll(this.ignored);
        this.alertsenabled = false;
        this.goldblocks = 0;
        this.placed = 0;
        this.broken = 0;
        this.homes = PlayerHomeData.deserialize("brak");
        this.waypoints = WaypointData.deserialize("brak");
        this.isIncognito = false;
        this.incognito = "";
        this.IncognitoAlliance = false;
        this.IncognitoDead = false;
        this.IncognitoKill = false;
        this.chapel = "";
        this.location = "false|^|" + ItemSerializer.serializeLocation(new Location(0.0D, 105.0D, 0.0D, 0.0D, 0.0D, player.getLevel()));
        insert();
    }


    public void calcWaypoints(Player p) {
        for (Waypoint wa : getActiveWaypoints()) {
            wa.close();
        }
        getActiveWaypoints().clear();
        for (WaypointData wp : getWaypoints()) {
            if (wp.active) {
                Waypoint wwp = new Waypoint(p, wp, wp.name, new Location(wp.x, wp.y, wp.z, 0.0D, 0.0D, p.getLevel()), p);
                p.getLevel().addEntity(wwp);
                getActiveWaypoints().add(wwp);
            }
        }
    }

    public Waypoint getactiveWaypoint(String name) {
        for (Waypoint wp :getActiveWaypoints()) {
            if (wp.data.name.equalsIgnoreCase(name))
                return wp;
        }
        return null;
    }

    public WaypointData getWaypoint(String name) {
        for (WaypointData wp : getWaypoints()) {
            if (wp.name.equalsIgnoreCase(name))
                return wp;
        }
        return null;
    }

    public void updateWaypoints(Player player) {
        int multiplier = 10;
        for (Waypoint wp : getActiveWaypoints()) {
            Vector3 destination = new Vector3(wp.data.x, wp.data.y, wp.data.z);
            Vector3 dest = new Vector3(wp.data.x, player.getPosition().getY(), wp.data.z);
            if (player.getPosition().distance(dest) > multiplier) {
                Vector3 dirVec = player.getPosition().clone().add(0.0D, 1.0D, 0.0D).subtract(dest).normalize();
                dirVec.x *= multiplier;
                dirVec.z *= multiplier;
                Position position1 = player.getPosition().clone().subtract(dirVec);
                position1.setComponents(position1.getX(), player.getPosition().getY() + 1.0D, position1.getZ());
                wp.setNameTag(Util.fixColor("&l&e" + wp.waypointname + "&r&f (" + Util.round(player.getPosition().distance(destination), 2) + "m)"));
                wp.setPosition(position1);
            } else {
                Vector3 position = destination.clone();
                if (Math.abs(player.getPosition().getY() - position.getY()) >= 3.0D)
                    position.setComponents(position.getX(), (player.getPosition().getY() > position.getY()) ? (player.getPosition().getY() - 3.0D) : (player.getPosition().getY() + 3.0D), position.getZ());
                wp.setNameTag(Util.fixColor("&l&e" + wp.waypointname + "&r&f (" + Util.round(player.getPosition().distance(destination), 2) + "m)"));
                wp.setPosition(position);
            }
            if (!wp.getViewers().containsValue(player))
                wp.spawnTo(player);
        }
    }

    public Set<PlayerHomeData> getHomes() {
        return this.homes;
    }

    public void removeHome(PlayerHomeData g) {
        this.homes.remove(g);
    }

    public PlayerHomeData getHome(String v) {
        for (PlayerHomeData home : this.homes) {
            if (home.name.equalsIgnoreCase(v))
                return home;
        }
        return null;
    }

    public void addHome(PlayerHomeData homes) {
        this.homes.add(homes);
        Main.getProvider().update("UPDATE `users` SET `homes` ='" + PlayerHomeData.serialize(this) + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void reduceTurbodrop(int t) {
        this.turbodrop -= t;
        Main.getProvider().update("UPDATE `users` SET `turbodrop` ='" + getTurbodrop() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void addOnlinetime(int t) {
        this.onlineoverall += t;
        this.onlinetime += t;
        this.onlinesession += t;
        Main.getProvider().update("UPDATE `users` SET `onlineoverall` ='" + getOnlineoverall() + "' WHERE `nickname` ='" + getNickname() + "'");
        Main.getProvider().update("UPDATE `users` SET `onlinetime` ='" + getOnlinetime() + "' WHERE `nickname` ='" + getNickname() + "'");
        Main.getProvider().update("UPDATE `users` SET `onlinesession` ='" + getOnlinesession() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void setAllDrops(boolean xd) {
        for (Integer f : DropManager.getSlots()) {
            if (xd) {
                if (!this.disableddrops.contains(String.valueOf(f)))
                    this.disableddrops.add(String.valueOf(f));
                continue;
            }
            if (this.disableddrops.contains(String.valueOf(f)))
                this.disableddrops.remove(String.valueOf(f));
        }
    }

    public void setDrop(int xd) {
        if (this.disableddrops.contains(String.valueOf(xd))) {
            this.disableddrops.remove(String.valueOf(xd));
        } else {
            this.disableddrops.add(String.valueOf(xd));
        }
    }

    public boolean hasDrop(int xd) {
        if (this.disableddrops.contains(String.valueOf(xd)))
            return true;
        return false;
    }

    public int getAssist() {
        return this.assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
        Main.getProvider().update("UPDATE `users` SET `points` ='" + getPoints() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public int getKills() {
        return this.kills;
    }

    public void addKills(int kills) {
        this.kills += kills;
        Main.getProvider().update("UPDATE `users` SET `kills` ='" + getKills() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void removeKills(int kills) {
        this.kills -= kills;
    }

    public void addAssist(int assist) {
        this.assist += assist;
        Main.getProvider().update("UPDATE `users` SET `assist` ='" + getAssist() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void removeAssist(int assist) {
        this.assist -= assist;
    }

    public void addDeaths(int points) {
        this.deaths += points;
        Main.getProvider().update("UPDATE `users` SET `deaths` ='" + getDeaths() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void removeDeaths(int deaths) {
        this.deaths -= deaths;
    }

    public void addPoints(int points) {
        this.points += points;
        Main.getProvider().update("UPDATE `users` SET `points` ='" + getPoints() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void removePoints(int points) {
        this.points -= points;
        Main.getProvider().update("UPDATE `users` SET `points` ='" + getPoints() + "' WHERE `nickname` ='" + getNickname() + "'");

    }

    public void addLastKill(Player p) {
        if (this.lastkills.containsKey(p.getName())) {
            this.lastkills.replace(p.getName(), System.currentTimeMillis() + Time.MINUTE.getTime(10));
        } else {
            this.lastkills.put(p.getName(), System.currentTimeMillis() + Time.MINUTE.getTime(10));
        }
    }

    public boolean isLastKill(Player p) {
        if (this.lastkills.containsKey(p.getName()) && this.lastkills.get(p.getName()) >= System.currentTimeMillis())
            return true;
        return false;
    }

    public int getAssistersSize() {
        return this.assistattackers.size();
    }

    public boolean isAssister(Player p) {
        return this.assistattackers.containsKey(p.getName());
    }

    public Player getAssister(Player p) {
        this.assistattackers.remove(p.getName());
        if (!this.assistattackers.isEmpty()) {
            Double max = Collections.<Double>max(this.assistattackers.values());
            List<String> id = (List<String>)this.assistattackers.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), max)).map(Map.Entry::getKey).collect(Collectors.toList());
            return Server.getInstance().getPlayerExact(id.get(0));
        }
        return null;
    }

    public void removeAssistPlayer(Player p) {
        this.assistattackers.remove(p.getName());
    }

    public void clearAssistAttackers() {
        this.assistattackers.clear();
    }

    public Set<Location> wallLocations = ConcurrentHashMap.newKeySet();

    public void removeWall(Location l) {
        this.wallLocations.remove(l);
    }

    public void addLocation(Location l) {
        this.wallLocations.add(l);
    }

    public Set<Location> getWallLocations() {
        return this.wallLocations;
    }

    public boolean hasWall(int x, int y, int z) {
        for (Location l : this.wallLocations) {
            if (l.getFloorX() == x && l.getFloorY() == y && l.getFloorZ() == z)
                return true;
        }
        return false;
    }

    public Map<String, Double> getAsistAtackers() {
        return this.assistattackers;
    }

    public void addAssistPlayer(Player p, double d) {
        if (this.assistattackers.containsKey(p.getName())) {
            Double dmg = this.assistattackers.get(p.getName());
            this.assistattackers.replace(p.getName(), Double.valueOf(dmg.doubleValue() + d));
        } else {
            this.assistattackers.put(p.getName(), Double.valueOf(d));
        }
    }

    public boolean hasProtection() {
        return (this.protection >= System.currentTimeMillis());
    }

    public boolean hasPermission(String s) {
        for (String ss : this.guildpermissions) {
            if (ss.equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    public boolean breakLimit() {
        if (this.breakPerSecondTime < System.currentTimeMillis()) {
            this.breakPerSecondTime = System.currentTimeMillis() + 1000L;
            this.breakPerSecond = 0;
        }
        if (++this.breakPerSecond > 600)
            return true;
        return false;
    }

    public boolean packetLimit() {
        if (this.packetsPerSecondTime < System.currentTimeMillis()) {
            this.packetsPerSecondTime = System.currentTimeMillis() + 1000L;
            this.packetsPerSecond = 0;
        }
        if (++this.packetsPerSecond > 850)
            return true;
        return false;
    }

    public boolean hasMacroLimit() {
        if (this.macroPerSecond > Settings.LIMIT_MACRO)
            return true;
        return false;
    }

    public boolean hasMacroMax() {
        if (this.macroPerSecond >= 60)
            return true;
        return false;
    }

    public boolean macroLimit() {
        if (this.macroPerSecondTime < System.currentTimeMillis()) {
            this.macroPerSecondTime = System.currentTimeMillis() + 1000L;
            this.macroPerSecond = 0;
        }
        if (++this.macroPerSecond > Settings.LIMIT_MACRO)
            return true;
        return false;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getWings() {
        return this.wings;
    }

    public String getRank() {
        return this.rank;
    }

    public String getTag() {
        return this.tag;
    }

    public boolean getAllyPVP() {
        return this.allypvp;
    }

    public boolean getPVP() {
        return this.pvp;
    }

    public boolean isGod() {
        return this.isGod;
    }
    public boolean getVanish() {
        return this.isVanish;
    }

    public long getProtection() {
        return this.protection;
    }

    public ACData getAcdata() {
        return acdata;
    }

    public long getMacroPerSecondTime() {
        return macroPerSecondTime;
    }

    public int getMacroPerSecond() {
        return macroPerSecond;
    }

    public long getBreakPerSecondTime() {
        return breakPerSecondTime;
    }

    public int getBreakPerSecond() {
        return breakPerSecond;
    }

    public long getPacketsPerSecondTime() {
        return packetsPerSecondTime;
    }

    public int getPacketsPerSecond() {
        return packetsPerSecond;
    }

    public boolean isVanish() {
        return isVanish;
    }

    public long getTurbodrop() {
        return turbodrop;
    }

    public Set<String> getDisableddrops() {
        return disableddrops;
    }

    public Skin getSkin() {
        return skin;
    }

    public Set<String> getGuildpermissions() {
        return guildpermissions;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isAllypvp() {
        return allypvp;
    }

    public Set<String> getInvites() {
        return invites;
    }

    public Set<String> getAlliances() {
        return alliances;
    }

    public Map<String, Double> getAssistattackers() {
        return assistattackers;
    }

    public Map<String, Long> getLastkills() {
        return lastkills;
    }

    public String getLastattacker() {
        return lastattacker;
    }

    public int getEgapple() {
        return egapple;
    }

    public int getGapple() {
        return gapple;
    }

    public int getArrows() {
        return arrows;
    }

    public int getPrimedtnt() {
        return primedtnt;
    }

    public int getSnowballs() {
        return snowballs;
    }

    public int getPearls() {
        return pearls;
    }

    public long getViptime() {
        return viptime;
    }

    public long getSviptime() {
        return sviptime;
    }

    public long getSponsortime() {
        return sponsortime;
    }

    public long getFoodtime() {
        return foodtime;
    }

    public long getEndertime() {
        return endertime;
    }

    public long getPlayertime() {
        return playertime;
    }

    public int getOnlineoverall() {
        return onlineoverall;
    }

    public int getOnlinetime() {
        return onlinetime;
    }

    public int getOnlinesession() {
        return onlinesession;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public long getMutetime() {
        return mutetime;
    }

    public Set<String> getIgnored() {
        return ignored;
    }

    public boolean isAlertsenabled() {
        return alertsenabled;
    }

    public int getGoldblocks() {
        return goldblocks;
    }

    public int getPlaced() {
        return placed;
    }

    public int getBroken() {
        return broken;
    }

    public String getReplyPlayer() {
        return replyPlayer;
    }

    public Set<WaypointData> getWaypoints() {
        return waypoints;
    }

    public Set<Waypoint> getActiveWaypoints() {
        return activeWaypoints;
    }

    public Set<String> getTeleportRequest() {
        return teleportRequest;
    }

    public boolean isIncognito() {
        return isIncognito;
    }

    public String getIncognito() {
        return incognito;
    }

    public boolean isIncognitoAlliance() {
        return IncognitoAlliance;
    }

    public boolean isIncognitoDead() {
        return IncognitoDead;
    }

    public boolean isIncognitoKill() {
        return IncognitoKill;
    }

    public boolean isTrading() {
        return isTrading;
    }

    public Set<String> getTradeInvites() {
        return tradeInvites;
    }

    public String getChapel() {
        return chapel;
    }

    public String getPermissionString() {
        return String.join("|&|", this.guildpermissions);
    }

    public Set<String> getGuildPermissions(String s) {
        Set<String> x = ConcurrentHashMap.newKeySet();
        x.addAll(Arrays.asList(s.split(Pattern.quote("|&|"))));
        return x;
    }
    
}

