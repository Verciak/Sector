package xyz.rokkiitt.sector.utils;

import cn.nukkit.item.*;
import cn.nukkit.block.*;

public final class GlassColor
{
    public static int WHITE;
    public static int ORANGE;
    public static int MAGENTA;
    public static int LIGHT_BLUE;
    public static int YELLOW;
    public static int LIME;
    public static int PINK;
    public static int GRAY;
    public static int LIGHT_GRAY;
    public static int CYAN;
    public static int PURPLE;
    public static int BLUE;
    public static int BROWN;
    public static int GREEN;
    public static int RED;
    public static int BLACK;
    
    public static Item get(final int color) {
        return Item.get(160, Integer.valueOf(color));
    }
    
    public static Block getWool(final int color) {
        return Block.get(35, color);
    }
    
    static {
        GlassColor.WHITE = 0;
        GlassColor.ORANGE = 1;
        GlassColor.MAGENTA = 2;
        GlassColor.LIGHT_BLUE = 3;
        GlassColor.YELLOW = 4;
        GlassColor.LIME = 5;
        GlassColor.PINK = 6;
        GlassColor.GRAY = 7;
        GlassColor.LIGHT_GRAY = 8;
        GlassColor.CYAN = 9;
        GlassColor.PURPLE = 10;
        GlassColor.BLUE = 11;
        GlassColor.BROWN = 12;
        GlassColor.GREEN = 13;
        GlassColor.RED = 14;
        GlassColor.BLACK = 15;
    }
}
