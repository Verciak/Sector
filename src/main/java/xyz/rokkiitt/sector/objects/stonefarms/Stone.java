package xyz.rokkiitt.sector.objects.stonefarms;

import xyz.rokkiitt.sector.Main;
import cn.nukkit.level.*;
import cn.nukkit.*;
import java.sql.*;

public class Stone
{
    private Level world;
    private int x;
    private int y;
    private int z;
    private int id;
    private String hash;
    
    public void insert() {
        Main.getDatabase().addQueue("INSERT INTO `stoniarka` (`id`,`world`, `x`, `y`, `z`,`hash`) VALUES ('" + this.id + "','" + this.world.getName() + "', '" + this.x + "', '" + this.y + "', '" + this.z + "','" + this.hash + "') ON DUPLICATE KEY UPDATE world=VALUES(world),x=VALUES(x),y=VALUES(y),z=VALUES(z),hash=VALUES(hash)");
    }
    
    public void delete() {
        Main.getDatabase().addQueue("DELETE FROM `stoniarka` WHERE `id` = '" + this.id + "'");
    }
    
    public Location getLocation() {
        return new Location((double)this.x, (double)this.y, (double)this.z, this.world);
    }
    
    public int getZ() {
        return this.z;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getHash() {
        return this.hash;
    }
    
    public Level getWorld() {
        return this.world;
    }
    
    public Stone(final int i, final String s, final Location l) {
        this.id = i;
        this.world = l.getLevel();
        this.x = l.getFloorX();
        this.y = l.getFloorY();
        this.z = l.getFloorZ();
        this.hash = s;
    }
    
    public Stone(final ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.world = Server.getInstance().getLevelByName(rs.getString("world"));
        this.x = rs.getInt("x");
        this.y = rs.getInt("y");
        this.z = rs.getInt("z");
        this.hash = rs.getString("hash");
    }
}
