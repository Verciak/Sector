package xyz.rokkiitt.sector.packets;

import java.util.*;
import java.util.concurrent.*;

public class PacketPlayerData
{
    public String nickname;
    public String curentsector;
    public String destinationsector;
    public String invcontent;
    public String invender;
    public String rank;
    public String location;
    public int playerlvl;
    public int playerxp;
    public float playerhealth;
    public int playerfood;
    public int playerfireticks;
    public boolean playerisflying;
    public boolean hasop;
    public boolean playerallowflying;
    public String wings;
    public int playergamemode;
    public String effect;
    public String tag;
    public int slot;
    public boolean pvp;
    public boolean allypvp;
    public String guildpermissions;
    public Set<String> alliances;
    public boolean isGod;
    public boolean isVanish;
    public long protection;
    public int kills;
    public int deaths;
    public int points;
    public int assist;
    public Set<String> drops;
    public long turbodrop;
    public int egapple;
    public int gapple;
    public int arrows;
    public int primedtnt;
    public int snowballs;
    public int pearls;
    public long viptime;
    public long sviptime;
    public long sponsortime;
    public long foodtime;
    public long endertime;
    public long playertime;
    public int onlineoverall;
    public int onlinetime;
    public int onlinesession;
    public boolean firstLogin;
    public long mutetime;
    public Set<String> ignored;
    public boolean alertsenabled;
    public int goldblocks;
    public int placed;
    public int broken;
    public String homes;
    public String waypoints;
    public boolean discordreceived;
    public boolean discordreward;
    public String discordcode;
    public boolean isIncognito;
    public String incognito;
    public boolean IncognitoAlliance;
    public boolean IncognitoDead;
    public boolean IncognitoKill;
    public String boosts;
    public String chapel;
    
    public PacketPlayerData() {
        this.alliances = ConcurrentHashMap.newKeySet();
        this.drops = ConcurrentHashMap.newKeySet();
        this.onlinesession = 0;
        this.firstLogin = false;
        this.ignored = ConcurrentHashMap.newKeySet();
    }
}
