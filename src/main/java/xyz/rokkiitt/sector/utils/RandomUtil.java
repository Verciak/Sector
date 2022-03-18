package xyz.rokkiitt.sector.utils;

import java.util.*;
import org.apache.commons.lang3.Validate;

public final class RandomUtil
{
    public static final Random rand;
    
    public static int rand(final int min, final int max) {
        if (min == max) {
            return max;
        }
        return RandomUtil.rand.nextInt(max + 1 - min) + min;
    }
    
    public static double rand(final double min, final double max) {
        if (min == max) {
            return max;
        }
        return min + RandomUtil.rand.nextDouble() * (max - min);
    }
    
    public static boolean rand() {
        return RandomUtil.rand.nextBoolean();
    }
    
    public static int getRandInt(final int min, final int max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return RandomUtil.rand.nextInt(max - min + 1) + min;
    }
    
    private static Double getRandDouble(final double min, final double max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return RandomUtil.rand.nextDouble() * (max - min) + min;
    }
    
    public static Float getRandFloat(final float min, final float max) throws IllegalArgumentException {
        Validate.isTrue(max > min, "Max can't be smaller than min!");
        return RandomUtil.rand.nextFloat() * (max - min) + min;
    }
    
    public static boolean getChance(final double chance) {
        return chance >= 100.0 || chance >= getRandDouble(0.0, 100.0);
    }
    
    public static int pickRandomPointBetween(final int corner1, final int corner2) {
        if (corner1 == corner2) {
            return corner1;
        }
        final int delta = corner2 - corner1;
        final int offset = RandomUtil.rand.nextInt() * delta;
        return corner1 + offset;
    }
    
    static {
        rand = new Random();
    }
}
