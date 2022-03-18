package xyz.rokkiitt.sector.objects.guild;

import xyz.rokkiitt.sector.Settings;
import cn.nukkit.level.*;
import cn.nukkit.*;
import cn.nukkit.block.*;

public class Heart
{
    private final int centerX;
    private final int centerZ;
    private int size;
    private int centerY1;
    private int centerY2;
    
    public Heart(final int x, final int z, final int size) {
        this.centerX = x;
        this.centerZ = z;
        this.centerY1 = Settings.GUILD_Y + 5;
        this.centerY2 = Settings.GUILD_Y - 2;
        this.size = size;
    }
    
    public Location getCenter() {
        return new Location((double)this.centerX, (double)Settings.GUILD_Y, (double)this.centerZ, Server.getInstance().getLevelByName("world"));
    }
    
    public boolean isInHeart(final Location loc) {
        final int distancex = Math.abs(loc.getFloorX() - this.centerX);
        final int distancez = Math.abs(loc.getFloorZ() - this.centerZ);
        return distancex <= this.size && loc.getFloorY() <= this.centerY1 && loc.getFloorY() >= this.centerY2 && distancez <= this.size;
    }
    
    public boolean isInHeartDistance(final Location loc) {
        final int distancex = Math.abs(loc.getFloorX() - this.centerX);
        final int distancez = Math.abs(loc.getFloorZ() - this.centerZ);
        return distancex <= this.size + 2 && loc.getFloorY() <= this.centerY1 + 2 && loc.getFloorY() >= this.centerY2 - 2 && distancez <= this.size + 2;
    }
    
    public boolean inInHeart(final Player p) {
        return this.isInHeart(p.getLocation());
    }
    
    public boolean inInHeart(final Block b) {
        return this.isInHeart(b.getLocation());
    }
    
    public int getCenterX() {
        return this.centerX;
    }
    
    public int getCenterZ() {
        return this.centerZ;
    }
}
