package xyz.rokkiitt.sector.objects.entity.utils;

import xyz.rokkiitt.sector.objects.entity.WalkingEntity;

public class SimpleRouteFinder extends RouteFinder {
    public SimpleRouteFinder(WalkingEntity entity) {
        super(entity);
    }

    public boolean search() {
        resetNodes();
        addNode(new Node(this.destination));
        return true;
    }
}
