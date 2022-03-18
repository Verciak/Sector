package xyz.rokkiitt.sector.utils;


import cn.nukkit.level.Location;

public enum Direction {
    NORTH, SOUTH, EAST, WEST, UP_DOWN;

    public Location add(Location base) {
        if (this == NORTH)
            return base.clone().add(0.0D, 0.0D, -5.0D);
        if (this == SOUTH)
            return base.clone().add(0.0D, 0.0D, 5.0D);
        if (this == WEST)
            return base.clone().add(-5.0D, 0.0D, 0.0D);
        if (this == EAST)
            return base.clone().add(5.0D, 0.0D, 0.0D);
        return base;
    }

    public static Direction fromLocation(Location from, Location to) {
        if (to.getFloorZ() < from.getFloorZ())
            return NORTH;
        if (to.getFloorZ() > from.getFloorZ())
            return SOUTH;
        if (to.getFloorX() < from.getFloorX())
            return WEST;
        if (to.getFloorX() > from.getFloorX())
            return EAST;
        return UP_DOWN;
    }
}