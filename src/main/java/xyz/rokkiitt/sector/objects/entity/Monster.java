package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.entity.Entity;
import java.util.HashMap;

public interface Monster {
    void attackEntity(Entity paramEntity);

    float getDamage();

    float getDamage(Integer paramInteger);

    float getMinDamage();

    float getMinDamage(Integer paramInteger);

    float getMaxDamage();

    float getMaxDamage(Integer paramInteger);

    void setDamage(float paramFloat);

    void setDamage(float[] paramArrayOffloat);

    void setDamage(float paramFloat, int paramInt);

    void setMinDamage(float paramFloat);

    void setMinDamage(float[] paramArrayOffloat);

    void setMinDamage(float paramFloat, int paramInt);

    void setMaxDamage(float paramFloat);

    void setMaxDamage(float[] paramArrayOffloat);

    void setMaxDamage(float paramFloat, int paramInt);

    public static final class ArmorPoints extends HashMap<Integer, Float> {
        public ArmorPoints() {
            put(Integer.valueOf(298), Float.valueOf(1.0F));
            put(Integer.valueOf(299), Float.valueOf(3.0F));
            put(Integer.valueOf(300), Float.valueOf(2.0F));
            put(Integer.valueOf(301), Float.valueOf(1.0F));
            put(Integer.valueOf(302), Float.valueOf(1.0F));
            put(Integer.valueOf(303), Float.valueOf(5.0F));
            put(Integer.valueOf(304), Float.valueOf(4.0F));
            put(Integer.valueOf(305), Float.valueOf(1.0F));
            put(Integer.valueOf(314), Float.valueOf(1.0F));
            put(Integer.valueOf(315), Float.valueOf(5.0F));
            put(Integer.valueOf(316), Float.valueOf(3.0F));
            put(Integer.valueOf(317), Float.valueOf(1.0F));
            put(Integer.valueOf(306), Float.valueOf(2.0F));
            put(Integer.valueOf(307), Float.valueOf(6.0F));
            put(Integer.valueOf(308), Float.valueOf(5.0F));
            put(Integer.valueOf(309), Float.valueOf(2.0F));
            put(Integer.valueOf(310), Float.valueOf(3.0F));
            put(Integer.valueOf(311), Float.valueOf(8.0F));
            put(Integer.valueOf(312), Float.valueOf(6.0F));
            put(Integer.valueOf(313), Float.valueOf(3.0F));
        }
    }
}
