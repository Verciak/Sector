package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.entity.*;
import java.util.*;

public interface Monster
{
    void attackEntity(final Entity p0);
    
    float getDamage();
    
    float getDamage(final Integer p0);
    
    float getMinDamage();
    
    float getMinDamage(final Integer p0);
    
    float getMaxDamage();
    
    float getMaxDamage(final Integer p0);
    
    void setDamage(final float p0);
    
    void setDamage(final float[] p0);
    
    void setDamage(final float p0, final int p1);
    
    void setMinDamage(final float p0);
    
    void setMinDamage(final float[] p0);
    
    void setMinDamage(final float p0, final int p1);
    
    void setMaxDamage(final float p0);
    
    void setMaxDamage(final float[] p0);
    
    void setMaxDamage(final float p0, final int p1);
    
    public static final class ArmorPoints extends HashMap<Integer, Float>
    {
        public ArmorPoints() {
            this.put(298, 1.0f);
            this.put(299, 3.0f);
            this.put(300, 2.0f);
            this.put(301, 1.0f);
            this.put(302, 1.0f);
            this.put(303, 5.0f);
            this.put(304, 4.0f);
            this.put(305, 1.0f);
            this.put(314, 1.0f);
            this.put(315, 5.0f);
            this.put(316, 3.0f);
            this.put(317, 1.0f);
            this.put(306, 2.0f);
            this.put(307, 6.0f);
            this.put(308, 5.0f);
            this.put(309, 2.0f);
            this.put(310, 3.0f);
            this.put(311, 8.0f);
            this.put(312, 6.0f);
            this.put(313, 3.0f);
        }
    }
}
