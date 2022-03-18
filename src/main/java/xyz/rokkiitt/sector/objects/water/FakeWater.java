package xyz.rokkiitt.sector.objects.water;

import cn.nukkit.level.*;
import xyz.rokkiitt.sector.utils.Time;

public class FakeWater extends Position
{
    private long placetime;
    
    public FakeWater(final double x, final double y, final double z, final Level l) {
        super(x, y, z, l);
        this.placetime = System.currentTimeMillis();
    }
    
    public long getPlaceTime() {
        return this.placetime;
    }
    
    public boolean canBeRemoved() {
        return this.placetime + Time.SECOND.getTime(3) <= System.currentTimeMillis();
    }
}
