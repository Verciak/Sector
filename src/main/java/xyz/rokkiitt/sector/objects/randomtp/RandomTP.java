package xyz.rokkiitt.sector.objects.randomtp;

import xyz.rokkiitt.sector.Main;
import cn.nukkit.level.*;
import java.sql.*;
import cn.nukkit.*;

public class RandomTP
{
    private final int x;
    private final int y;
    private final int z;
    private final String world;
    private final RandomTPStatus status;
    private final int id;
    
    public void update() {
        Main.getDatabase().addQueue("INSERT INTO `randomtp` (`id`,`world`, `x`,`y`,`z`,`status`)VALUES('" + this.id + "','" + this.world + "','" + this.x + "','" + this.y + "','" + this.z + "','" + this.status + "')ON DUPLICATE KEY UPDATE world=VALUES(world),x=VALUES(x),y=VALUES(y),z=VALUES(z),status=VALUES(status)");
    }
    
    public void Delete() {
        Main.getDatabase().addQueue("DELETE FROM `randomtp` WHERE `id` = '" + this.id + "'");
    }
    
    public RandomTP(final int i, final Location l, final RandomTPStatus s) {
        this.id = i;
        this.x = l.getFloorX();
        this.y = l.getFloorY();
        this.z = l.getFloorZ();
        this.world = l.getLevel().getName();
        this.status = s;
    }
    
    public RandomTP(final ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.x = rs.getInt("x");
        this.y = rs.getInt("y");
        this.z = rs.getInt("z");
        this.world = rs.getString("world");
        this.status = RandomTPStatus.valueOf(rs.getString("status"));
    }
    
    public Location getLocation() {
        return new Location((double)this.x, (double)this.y, (double)this.z, Server.getInstance().getLevelByName(this.world));
    }
    
    public RandomTPStatus getStatus() {
        return this.status;
    }
    
    public String getWorld() {
        return this.world;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
}
