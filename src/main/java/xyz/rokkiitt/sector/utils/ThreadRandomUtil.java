package xyz.rokkiitt.sector.utils;

import org.apache.commons.lang3.Validate;

import java.util.concurrent.*;

public final class ThreadRandomUtil
{
    public ThreadLocalRandom rand;
    
    public ThreadRandomUtil() {
        this.rand = ThreadLocalRandom.current();
    }
    
    public int getRandInt(final int min, final int max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return this.rand.nextInt(max - min + 1) + min;
    }
    
    private Double getRandDouble(final double min, final double max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return this.rand.nextDouble() * (max - min) + min;
    }
    
    public Float getRandFloat(final float min, final float max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return this.rand.nextFloat() * (max - min) + min;
    }
    
    public boolean getChance(final double chance) {
        return chance >= 100.0 || chance >= this.getRandDouble(0.0, 100.0);
    }
}
