package xyz.rokkiitt.sector.objects.entity.utils;

import xyz.rokkiitt.sector.objects.entity.WalkingEntity;

public class SimpleRouteFinder extends RouteFinder
{
    public SimpleRouteFinder(final WalkingEntity entity) {
        super(entity);
    }
    
    @Override
    public boolean search() {
        this.resetNodes();
        this.addNode(new Node(this.destination));
        return true;
    }
}
