package xyz.rokkiitt.sector.objects.meteorite;

import cn.nukkit.level.*;

public class MeteoriteRegion
{
    private final Level world;
    private final int centerX;
    private final int centerZ;
    private final int centerY;
    
    public MeteoriteRegion(final Level world, final int x, final int y, final int z) {
        this.world = world;
        this.centerX = x;
        this.centerZ = z;
        this.centerY = y;
    }
    
    public Location getCenter() {
        return new Location((double)this.centerX, (double)this.centerY, (double)this.centerZ, this.world);
    }
    
    public boolean isInCuboid(final Location loc) {
        if (!loc.getLevel().equals(this.world)) {
            return false;
        }
        final int distancex = Math.abs(loc.getFloorX() - this.centerX);
        final int distancez = Math.abs(loc.getFloorZ() - this.centerZ);
        return distancex <= 10 && distancez <= 10;
    }
    
    public Level getWorld() {
        return this.world;
    }
    
    public int getCenterY() {
        return this.centerY;
    }
    
    public int getCenterX() {
        return this.centerX;
    }
    
    public int getCenterZ() {
        return this.centerZ;
    }
}
