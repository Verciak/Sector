package xyz.rokkiitt.sector.objects.guild;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.Settings;
import cn.nukkit.level.*;
import cn.nukkit.*;
import cn.nukkit.block.*;

public class Cuboid
{
    private int centerX;
    private int centerZ;
    private int size;
    
    public Cuboid(final int x, final int z, final int size) {
        this.centerX = x;
        this.centerZ = z;
        this.size = size;
    }
    
    public Location getCenter() {
        return new Location((double)this.centerX, (double) Settings.GUILD_Y, (double)this.centerZ, Server.getInstance().getDefaultLevel());
    }
    
    public void setCenterX(final int t) {
        this.centerX = t;
    }
    
    public void setCenterZ(final int t) {
        this.centerZ = t;
    }
    
    public boolean addSize(String tag) {
        if (this.size >= Settings.GUILD_SIZE_MAX) {
            return false;
        }
        this.size += Settings.GUILD_SIZE_ADD;
        Main.getProvider().update("UPDATE `guilds` SET `size` ='" + getSize() +"' WHERE `tag` ='" + tag + "'");

        return true;
    }
    
    public boolean isInCuboid(final Location loc) {
        final int distancex = Math.abs(loc.getFloorX() - this.centerX);
        final int distancez = Math.abs(loc.getFloorZ() - this.centerZ);
        return distancex <= this.size && distancez <= this.size;
    }
    
    public boolean isInCuboid(final Level w, final int x, final int z) {
        final int distancex = Math.abs(x - this.centerX);
        final int distancez = Math.abs(z - this.centerZ);
        return distancex <= this.size && distancez <= this.size;
    }
    
    public boolean isNearCuboid(final Location loc) {
        final int distancex = Math.abs(loc.getFloorX() - this.centerX);
        final int distancez = Math.abs(loc.getFloorZ() - this.centerZ);
        return distancex <= this.size + 10 && distancez <= this.size + 10;
    }
    
    public boolean inInCuboid(final Player p) {
        return this.isInCuboid(p.getLocation());
    }
    
    public boolean inInCuboid(final Block b) {
        return this.isInCuboid(b.getLocation());
    }
    
    public int getCenterX() {
        return this.centerX;
    }
    
    public int getCenterZ() {
        return this.centerZ;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
}
