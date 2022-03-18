package xyz.rokkiitt.sector.utils;

import cn.nukkit.item.*;

public final class DyeColor
{
    public static int BLACK;
    public static int RED;
    public static int GREEN;
    public static int BROWN;
    public static int BLUE;
    public static int PURPLE;
    public static int CYAN;
    public static int LIGHT_GRAy;
    public static int GRAY;
    public static int PINK;
    public static int LIME;
    public static int YELLOW;
    public static int LIGHT_BLUE;
    public static int MAGENTA;
    public static int ORANGE;
    public static int WHITE;
    
    public static Item get(final int color) {
        return Item.get(351, Integer.valueOf(color));
    }
    
    static {
        DyeColor.BLACK = 0;
        DyeColor.RED = 1;
        DyeColor.GREEN = 2;
        DyeColor.BROWN = 3;
        DyeColor.BLUE = 4;
        DyeColor.PURPLE = 5;
        DyeColor.CYAN = 6;
        DyeColor.LIGHT_GRAy = 7;
        DyeColor.GRAY = 8;
        DyeColor.PINK = 9;
        DyeColor.LIME = 10;
        DyeColor.YELLOW = 11;
        DyeColor.LIGHT_BLUE = 12;
        DyeColor.MAGENTA = 13;
        DyeColor.ORANGE = 14;
        DyeColor.WHITE = 15;
    }
}
