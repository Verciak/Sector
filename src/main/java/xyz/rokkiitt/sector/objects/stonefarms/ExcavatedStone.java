package xyz.rokkiitt.sector.objects.stonefarms;

import cn.nukkit.level.*;

public class ExcavatedStone
{
    private Level world;
    private int x;
    private int y;
    private int z;
    private long timer;
    private String hash;
    
    public long getTime() {
        return this.timer;
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
    
    public Level getWorld() {
        return this.world;
    }
    
    public String getHash() {
        return this.hash;
    }
    
    public ExcavatedStone(final String s, final Location l) {
        this.world = l.getLevel();
        this.x = l.getFloorX();
        this.y = l.getFloorY();
        this.z = l.getFloorZ();
        this.timer = System.currentTimeMillis();
        this.hash = s;
    }
}
