package xyz.rokkiitt.sector.utils;

import cn.nukkit.math.*;
import cn.nukkit.block.*;
import java.util.*;
import cn.nukkit.level.*;

public final class BoundingBox
{
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;
    
    public double getMinX() {
        return this.minX;
    }
    
    public double getMinY() {
        return this.minY;
    }
    
    public double getMinZ() {
        return this.minZ;
    }
    
    public double getMaxX() {
        return this.maxX;
    }
    
    public double getMaxZ() {
        return this.maxZ;
    }
    
    public double getMaxY() {
        return this.maxY;
    }
    
    public BoundingBox add(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return new BoundingBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }
    
    public void setMinX(final double s) {
        this.minX = s;
    }
    
    public void setMinY(final double s) {
        this.minY = s;
    }
    
    public void getMinZ(final double s) {
        this.minZ = s;
    }
    
    public void setMaxX(final double s) {
        this.maxX = s;
    }
    
    public void setMaxZ(final double s) {
        this.maxZ = s;
    }
    
    public void setMaxY(final double s) {
        this.maxY = s;
    }
    
    public double min(final int i) {
        switch (i) {
            case 0: {
                return this.minX;
            }
            case 1: {
                return this.minY;
            }
            case 2: {
                return this.minZ;
            }
            default: {
                return 0.0;
            }
        }
    }
    
    public double max(final int i) {
        switch (i) {
            case 0: {
                return this.maxX;
            }
            case 1: {
                return this.maxY;
            }
            case 2: {
                return this.maxZ;
            }
            default: {
                return 0.0;
            }
        }
    }
    
    public double getSize() {
        final Vector3 min = new Vector3(this.minX, this.minY, this.minZ);
        final Vector3 max = new Vector3(this.maxX, this.maxY, this.maxZ);
        return min.distance(max);
    }
    
    public BoundingBox union(final BoundingBox other) {
        final double minX = Math.min(this.minX, other.minX);
        final double minY = Math.min(this.minY, other.minY);
        final double minZ = Math.min(this.minZ, other.minZ);
        final double maxX = Math.max(this.maxX, other.maxX);
        final double maxY = Math.max(this.maxY, other.maxY);
        final double maxZ = Math.max(this.maxZ, other.maxZ);
        return new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
    }
    
    public BoundingBox(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        if (minX < maxX) {
            this.minX = minX;
            this.maxX = maxX;
        }
        else {
            this.minX = maxX;
            this.maxX = minX;
        }
        if (minY < maxY) {
            this.minY = minY;
            this.maxY = maxY;
        }
        else {
            this.minY = maxY;
            this.maxY = minY;
        }
        if (minZ < maxZ) {
            this.minZ = minZ;
            this.maxZ = maxZ;
        }
        else {
            this.minZ = maxZ;
            this.maxZ = minZ;
        }
    }
    
    public BoundingBox move(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }
    
    public List<Block> getBlocks(final Level world) {
        final List<Block> blockList = new ArrayList<Block>();
        final double minX = this.minX;
        final double minY = this.minY;
        final double minZ = this.minZ;
        final double maxX = this.maxX;
        final double maxY = this.maxY;
        final double maxZ = this.maxZ;
        for (double x = minX; x <= maxX; x += maxX - minX) {
            for (double y = minY; y <= maxY; y += maxY - minY) {
                for (double z = minZ; z <= maxZ; z += maxZ - minZ) {
                    final Block block = world.getBlock((Vector3)new Location(x, y, z, world));
                    blockList.add(block);
                }
            }
        }
        return blockList;
    }
    
    public BoundingBox expand(final double x, final double y, final double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }
    
    public BoundingBox expandSpecific(final double minX, final double maxX, final double minY, final double maxY, final double minZ, final double maxZ) {
        this.minX -= minX;
        this.minY -= minY;
        this.minZ -= minZ;
        this.maxX += maxX;
        this.maxY += maxY;
        this.maxZ += maxZ;
        return this;
    }
}
