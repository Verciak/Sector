package xyz.rokkiitt.sector.objects.guild;

import cn.nukkit.item.Item;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.guild.collection.Collection;
import xyz.rokkiitt.sector.objects.guild.entity.EntityHead;
import xyz.rokkiitt.sector.objects.guild.logblock.Logblock;
import xyz.rokkiitt.sector.objects.home.PlayerHomeData;
import xyz.rokkiitt.sector.objects.waypoint.WaypointData;
import xyz.rokkiitt.sector.packets.guild.PacketGuildData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.*;
import java.util.concurrent.*;
import java.util.*;
import cn.nukkit.block.*;
import xyz.rokkiitt.sector.utils.ItemSerializer;

public class Guild
{
    private Cuboid cuboid;
    private String tag;
    private String name;
    private String leader;
    private Set<String> members;
    private GuildTreasureGUI skarbiec;
    private boolean pvp;
    private boolean allypvp;
    private long penaltytnt;
    private long GuildProtectionTime;
    private long HeartProtectionTime;
    private int hoppers;
    private Set<Collection> collections;
    private String hearttype;
    private String heartcolor;
    private int hearts;
    private int hearthp;
    private int centerx;
    private int centerz;
    private int size;
    private long heartRegen;
    private Heart heart;
    private EntityHead head;
    private Map<String, Map<String, Set<Logblock>>> logblocks;
    private boolean isLogblockenabled;
    private GuildRegenerationGUI regengui;
    private boolean isRegen;
    private int guildBalance;
    private long coolDown;
    private long createTime;
    private Map<Integer, Set<HashMap<String, Integer>>> b;
    private int rgetY;
    private long regenexplode;
    private int rBlocksize;
    private int regenMissedBlock;
    private int regenDoneBlock;
    private int regenDoneSize;
    private long explodeTime;
    
    public Collection getCollection(final int type) {
        for (final Collection c : this.collections) {
            if (c.type == type) {
                return c;
            }
        }
        return null;
    }
    
    public void heartRegen() {
        if (this.hearthp < 100 && this.heartRegen <= System.currentTimeMillis()) {
            ++this.hearthp;
            this.heartRegen = System.currentTimeMillis() + 2000L;
        }
    }
    
    public List<Logblock> getLogblocks(final String hash, final String type) {
        final List<Logblock> logs = new ArrayList<Logblock>();
        if (this.logblocks.containsKey(hash) && this.logblocks.get(hash).containsKey(type)) {
            logs.addAll(this.logblocks.get(hash).get(type));
        }
        return logs;
    }
    
    public void addLogblock(final String who, final String hash, final String what, final String type, final long time) {
        final Logblock lb = new Logblock();
        lb.hash = hash;
        lb.type = type;
        lb.what = what;
        lb.who = who;
        lb.time = time;
        if (this.logblocks.containsKey(hash)) {
            if (this.logblocks.get(hash).containsKey(type)) {
                this.logblocks.get(hash).get(type).add(lb);
            }
            else {
                final Set<Logblock> ls = ConcurrentHashMap.newKeySet();
                ls.add(lb);
                this.logblocks.get(hash).put(type, ls);
            }
        }
        else {
            final Map<String, Set<Logblock>> map = new HashMap<String, Set<Logblock>>();
            final Set<Logblock> ls2 = ConcurrentHashMap.newKeySet();
            ls2.add(lb);
            map.put(type, ls2);
            this.logblocks.put(hash, map);
        }
    }
    
    public void saveLogblock() {
    }
    
    public void clearLogblock() {
    }
    
    public int getLogBlockSize() {
        int ss = 0;
        for (final Map.Entry<String, Map<String, Set<Logblock>>> entry : this.logblocks.entrySet()) {
            for (final Map.Entry<String, Set<Logblock>> loge : entry.getValue().entrySet()) {
                ss += loge.getValue().size();
            }
        }
        return ss;
    }
    
    public void clearRegeneration() {
    }
    
    public void saveRegeneration() {
    }
    
    public int getRegenSizeBlocks() {
        int ss = 0;
        for (final Map.Entry<Integer, Set<HashMap<String, Integer>>> s : this.b.entrySet()) {
            ss += s.getValue().size();
        }
        return ss;
    }
    
    public void regenClearBlocks() {
        this.regenMissedBlock = 0;
        this.regenDoneBlock = 0;
        this.rgetY = 0;
    }
    
    public int getRegenDoneBlock() {
        return this.regenDoneBlock;
    }
    
    public void clearRegenDoneBlock() {
        this.regenDoneSize = 0;
    }
    
    public void addRegenDone() {
        ++this.regenDoneSize;
        ++this.regenDoneBlock;
    }
    
    public int getRegenDoneSize() {
        return this.regenDoneSize;
    }
    
    public void addRegenMissedBlock() {
        ++this.regenMissedBlock;
    }
    
    public int getRegenMissedBlock() {
        return this.regenMissedBlock;
    }
    
    public void setRegenSize(final int d) {
        this.rBlocksize = d;
    }
    
    public int getRegenSize() {
        return this.rBlocksize;
    }
    
    public int getYSize(final int k) {
        if (this.b.containsKey(k)) {
            return this.b.get(k).size();
        }
        return 0;
    }
    
    public int reSizeRegen() {
        int s = 0;
        for (final Integer ggg : this.b.keySet()) {
            s += this.b.get(ggg).size();
        }
        return this.rBlocksize = s;
    }
    
    public void setRegenExplodeTime(final long time) {
        this.regenexplode = time;
    }
    
    public long getRegenExplodeTime() {
        return this.regenexplode;
    }
    
    public Map<String, Integer> getblocks(final int key) {
        if (!this.b.containsKey(key)) {
            return null;
        }
        if (!this.b.get(key).isEmpty()) {
            final Optional<HashMap<String, Integer>> c = this.b.get(key).stream().findFirst();
            return c.orElse(null);
        }
        return null;
    }
    
    public void removeKey(final int key) {
        this.b.remove(key);
    }
    
    public void regenSort(final int key) {
        if (this.b.containsKey(key) && !this.b.get(key).isEmpty()) {
            final List<HashMap<String, Integer>> v = new ArrayList<HashMap<String, Integer>>(this.b.get(key));
            v.sort(Comparator.comparing(m -> m.get("z")));
            v.sort(Comparator.comparing(m -> m.get("x")));
            final Set<HashMap<String, Integer>> f = ConcurrentHashMap.newKeySet();
            f.addAll(v);
            this.b.replace(key, f);
        }
    }
    
    public HashMap<String, Integer> checkBlock(final Block b) {
        for (final HashMap<String, Integer> gg : this.b.get(b.getLocation().getFloorY())) {
            if (gg.get("x") == b.getLocation().getFloorX() && gg.get("z") == b.getLocation().getFloorZ()) {
                return gg;
            }
        }
        return null;
    }
    
    public HashMap<String, Integer> checkBlock(final int x, final int y, final int z) {
        for (final HashMap<String, Integer> gg : this.b.get(y)) {
            if (gg.get("x") == x && gg.get("z") == z) {
                return gg;
            }
        }
        return null;
    }
    
    public void rAddBlock(final int idd, final int meta, final int x, final int y, final int z) {
        final HashMap<String, Integer> id = new HashMap<String, Integer>();
        id.put("id", idd);
        id.put("meta", meta);
        id.put("x", x);
        id.put("y", y);
        id.put("z", z);
        if (this.b.containsKey(y)) {
            final HashMap<String, Integer> h = this.checkBlock(x, y, z);
            if (h != null) {
                this.b.get(y).remove(h);
            }
            this.b.get(y).add(id);
        }
        else {
            this.b.put(y, ConcurrentHashMap.newKeySet());
            this.b.get(y).add(id);
        }
    }
    
    public void rAddBlock(final Block b) {
        final HashMap<String, Integer> id = new HashMap<String, Integer>();
        id.put("id", b.getId());
        id.put("meta", b.getDamage());
        id.put("x", b.getLocation().getFloorX());
        id.put("y", b.getLocation().getFloorY());
        id.put("z", b.getLocation().getFloorZ());
        if (this.b.containsKey(b.getLocation().getFloorY())) {
            final HashMap<String, Integer> h = this.checkBlock(b);
            if (h != null) {
                this.b.get(b.getLocation().getFloorY()).remove(h);
            }
            this.b.get(b.getLocation().getFloorY()).add(id);
        }
        else {
            this.b.put(b.getLocation().getFloorY(), ConcurrentHashMap.newKeySet());
            this.b.get(b.getLocation().getFloorY()).add(id);
        }
    }
    
    public void rremoveBlock(final int key) {
        if (this.b.containsKey(key)) {
            this.b.get(key).remove(0);
        }
    }
    
    public int rgetY() {
        return this.rgetY;
    }
    
    public void rsetY(final int id) {
        this.rgetY = id;
    }
    
    public Heart getHeart() {
        return this.heart;
    }
    
    public void setHeartProtectionTime(final long id) {
        this.HeartProtectionTime = id;
        Main.getProvider().update("UPDATE `guilds` SET `heartprot` ='" + getHeartProtectionTime()+"' WHERE `tag` ='" + getTag() + "'");

    }
    
    public long getHeartProtectionTime() {
        return this.HeartProtectionTime;
    }
    
    public void setGuildProtectionTime(final long id) {
        this.GuildProtectionTime = id;
        Main.getProvider().update("UPDATE `guilds` SET `guildprot` ='" + getGuildProtectionTime()+"' WHERE `tag` ='" + getTag() + "'");

    }
    
    public long getGuildProtectionTime() {
        return this.GuildProtectionTime;
    }
    
    public void setExplodeTime(final long time) {
        this.explodeTime = time;
    }
    
    public long getExplodeTime() {
        return this.explodeTime;
    }
    
    public boolean addSize() {
        return this.cuboid.addSize();
    }
    
//    public void queryDelete() {
//        if (Main.isHubOnline() || Main.saveOnStop) {
//            Main.getNats().publish("databasedelete", "guild||" + this.tag);
//       }
//       else {
//          HubTask.queries.add(() -> Main.getNats().publish("databasedelete", "guild||" + this.tag));
//        }
//    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public void setCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        Main.getProvider().update("UPDATE `guilds` SET `tag` ='" + getTag()+"' WHERE `tag` ='" + getTag() + "'");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Main.getProvider().update("UPDATE `guilds` SET `name` ='" + getName()+"' WHERE `tag` ='" + getTag() + "'");

    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
        Main.getProvider().update("UPDATE `guilds` SET `leader` ='" + getLeader()+"' WHERE `tag` ='" + getTag() + "'");

    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
        Main.getProvider().update("UPDATE `guilds` SET `members` ='" + JsonStream.serialize(getMembers())+"' WHERE `tag` ='" + getTag() + "'");

    }

    public GuildTreasureGUI getSkarbiec() {
        return skarbiec;
    }

    public void setSkarbiec(GuildTreasureGUI skarbiec) {
        this.skarbiec = skarbiec;
        Main.getProvider().update("UPDATE `guilds` SET `skarbiec` ='" + ItemSerializer.getStringFromItemMap(getSkarbiec().getContents()) +"' WHERE `tag` ='" + getTag() + "'");

    }

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean isAllypvp() {
        return allypvp;
    }

    public void setAllypvp(boolean allypvp) {
        this.allypvp = allypvp;
    }

    public long getPenaltytnt() {
        return penaltytnt;
    }

    public void setPenaltytnt(long penaltytnt) {
        this.penaltytnt = penaltytnt;
        Main.getProvider().update("UPDATE `guilds` SET `penaltytnt` ='" + getPenaltytnt() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public int getHoppers() {
        return hoppers;
    }

    public void setHoppers(int hoppers) {
        this.hoppers = hoppers;
        Main.getProvider().update("UPDATE `guilds` SET `hoppers` ='" + getHoppers() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
        Main.getProvider().update("UPDATE `guilds` SET `collections` ='" + Collection.serialize(getCollections()) +"' WHERE `tag` ='" + getTag() + "'");

    }

    public String getHearttype() {
        return hearttype;
    }

    public void setHearttype(String hearttype) {
        this.hearttype = hearttype;
        Main.getProvider().update("UPDATE `guilds` SET `hearttype` ='" + getHearttype() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public String getHeartcolor() {
        return heartcolor;
    }

    public void setHeartcolor(String heartcolor) {
        this.heartcolor = heartcolor;
        Main.getProvider().update("UPDATE `guilds` SET `heartcolor` ='" + getHeartcolor() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
        Main.getProvider().update("UPDATE `guilds` SET `hearts` ='" + getHearts() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public int getHearthp() {
        return hearthp;
    }

    public void setHearthp(int hearthp) {
        this.hearthp = hearthp;
    }

    public int getCenterx() {
        return centerx;
    }

    public void setCenterx(int centerx) {
        this.centerx = centerx;
        Main.getProvider().update("UPDATE `guilds` SET `centerx` ='" + getCenterx() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public int getCenterz() {
        return centerz;
    }

    public void setCenterz(int centerz) {
        this.centerz = centerz;
        Main.getProvider().update("UPDATE `guilds` SET `centerz` ='" + getCenterz() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        Main.getProvider().update("UPDATE `guilds` SET `size` ='" + getSize() +"' WHERE `tag` ='" + getTag() + "'");

    }


    public long getHeartRegen() {
        return heartRegen;
    }

    public void setHeartRegen(long heartRegen) {
        this.heartRegen = heartRegen;
    }

    public void setHeart(Heart heart) {
        this.heart = heart;
    }

    public EntityHead getHead() {
        return head;
    }

    public void setHead(EntityHead head) {
        this.head = head;

    }

    public Map<String, Map<String, Set<Logblock>>> getLogblocks() {
        return logblocks;
    }

    public void setLogblocks(Map<String, Map<String, Set<Logblock>>> logblocks) {
        this.logblocks = logblocks;
    }

    public boolean isLogblockenabled() {
        return isLogblockenabled;
    }

    public void setLogblockenabled(boolean logblockenabled) {
        isLogblockenabled = logblockenabled;
    }

    public GuildRegenerationGUI getRegengui() {
        return regengui;
    }

    public void setRegengui(GuildRegenerationGUI regengui) {
        this.regengui = regengui;
    }

    public boolean isRegen() {
        return isRegen;
    }

    public void setRegen(boolean regen) {
        isRegen = regen;
    }

    public int getGuildBalance() {
        return guildBalance;
    }

    public void setGuildBalance(int guildBalance) {
        this.guildBalance = guildBalance;
    }

    public long getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(long coolDown) {
        this.coolDown = coolDown;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
        Main.getProvider().update("UPDATE `guilds` SET `createTime` ='" + getCreateTime() +"' WHERE `tag` ='" + getTag() + "'");

    }

    public Map<Integer, Set<HashMap<String, Integer>>> getB() {
        return b;
    }

    public void setB(Map<Integer, Set<HashMap<String, Integer>>> b) {
        this.b = b;
    }

    public int getRgetY() {
        return rgetY;
    }

    public void setRgetY(int rgetY) {
        this.rgetY = rgetY;
    }

    public long getRegenexplode() {
        return regenexplode;
    }

    public void setRegenexplode(long regenexplode) {
        this.regenexplode = regenexplode;
    }

    public int getrBlocksize() {
        return rBlocksize;
    }

    public void setrBlocksize(int rBlocksize) {
        this.rBlocksize = rBlocksize;
    }

    public void setRegenMissedBlock(int regenMissedBlock) {
        this.regenMissedBlock = regenMissedBlock;
    }

    public void setRegenDoneBlock(int regenDoneBlock) {
        this.regenDoneBlock = regenDoneBlock;
    }

    public void setRegenDoneSize(int regenDoneSize) {
        this.regenDoneSize = regenDoneSize;
    }

    private void insert() {
        Main.getDatabase().addQueue("INSERT INTO `guilds`(`id`, " +
                "`tag`, " +
                "`name`, " +
                "`leader`, " +
                "`centerx`, " +
                "`centerz`, " +
                "`size`, " +
                "`members`, " +
                "`guildBalance`, " +
                "`guildprot`," +
                "`heartprot`," +
                "`createTime`, " +
                "`penaltytnt`," +
                "`heartcolor`, " +
                "`hearttype`, " +
                "`hearts`, " +
                "`skarbiec`, " +
                "`collections`, " +
                "`hoppers`) VALUES (" +
                "NULL, " +
                "'" + getTag() + "'," +
                "'" + getName() + "'," +
                "'" + getLeader() + "'," +
                "'" + getCenterx() + "'," +
                "'" + getCenterz() + "'," +
                "'" + getSize() + "'," +
                "'" + JsonStream.serialize(getMembers()) + "'," +
                "'" + getGuildBalance() + "'," +
                "'" + getGuildProtectionTime() + "'," +
                "'" + getHeartProtectionTime() + "'," +
                "'" + getCreateTime() + "'," +
                "'" + getPenaltytnt() + "'," +
                "'" + getHeartcolor() + "'," +
                "'" + getHearttype() + "'," +
                "'" + getHearts() + "'," +
                "'" + ItemSerializer.getStringFromItemMap(getSkarbiec().getContents()) + "'," +
                "'" + Collection.serialize(getCollections()) + "'," +
                "'" + getHoppers() + "')");
    }

    public Guild(ResultSet set) throws SQLException {
        this.tag = set.getString("tag");
        this.name = set.getString("name");
        this.leader = set.getString("leader");
        this.centerx = set.getInt("centerx");
        this.centerz = set.getInt("centerz");
        this.size = set.getInt("size");
        this.cuboid = new Cuboid(centerx, centerz, size);
        this.heart = new Heart(centerx, centerz, 4);
        this.members = ConcurrentHashMap.newKeySet();
        final Set<String> c = JsonIterator.deserialize(set.getString("members"), Set.class);
        if (!c.isEmpty()) {
            getMembers().addAll(c);
        }
        this.guildBalance = set.getInt("guildBalance");
        this.GuildProtectionTime = set.getLong("guildprot");
        this.HeartProtectionTime = set.getLong("heartprot");
        this.createTime = set.getLong("createTime");
        this.penaltytnt = set.getLong("penaltytnt");
        this.heartcolor = set.getString("heartcolor");
        this.hearttype = set.getString("hearttype");
        this.hearts = set.getInt("hearts");
        this.skarbiec = new GuildTreasureGUI(set.getString("skarbiec").equalsIgnoreCase("brak") ? new HashMap<>() : ItemSerializer.getItemMapFromString(set.getString("skarbiec")));
        this.collections = ConcurrentHashMap.newKeySet();
        if (!set.getString("collections").equalsIgnoreCase("brak")) {
            this.collections.addAll(Collection.deserialize(set.getString("collections")));
        }
        this.hoppers = set.getInt("hoppers");
    }
    
    public Guild(final String pd, final String name, final String leader, final int centerx, final int centerz, final int size, final String members, final int goldblocks, final String skarbiec, final long createTime, final long guildprot, final long heartprot, final long penalty, final String hearttype, final String heartcolor, final int hearts, final int hoppers, final String coll) {
        this.collections = ConcurrentHashMap.newKeySet();
        this.hearthp = 100;
        this.logblocks = new ConcurrentHashMap<>();
        this.isLogblockenabled = true;
        this.b = new ConcurrentHashMap<>();
        this.penaltytnt = penalty;
        this.tag = pd;
        this.name = name;
        this.leader = leader.toLowerCase();
        this.members = ConcurrentHashMap.newKeySet();
        final Set c = JsonIterator.deserialize(members, Set.class);
        if (!c.isEmpty()) {
            this.members.addAll(c);
        }
        this.cuboid = new Cuboid(centerx, centerz, size);
        this.centerx = centerx;
        this.centerz = centerz;
        this.size = size;
        this.heart = new Heart(centerx, centerz, 4);
        this.regengui = new GuildRegenerationGUI(this);
        this.skarbiec = new GuildTreasureGUI(skarbiec.equalsIgnoreCase("brak") ? new HashMap<Integer, Item>() : ItemSerializer.getItemMapFromString(skarbiec));
        this.isRegen = false;
        this.guildBalance = goldblocks;
        this.GuildProtectionTime = guildprot;
        this.HeartProtectionTime = heartprot;
        this.createTime = createTime;
        this.hearttype = hearttype;
        this.heartcolor = heartcolor;
        this.hearts = hearts;
        this.head = null;
        this.hoppers = hoppers;
        if (!coll.equalsIgnoreCase("brak")) {
            this.collections.addAll(Collection.deserialize(coll));
        }
        else {
            this.collections.addAll(Collection.getDeafults());
        }
        insert();
    }
}
