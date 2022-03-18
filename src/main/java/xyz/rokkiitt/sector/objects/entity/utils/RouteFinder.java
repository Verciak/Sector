package xyz.rokkiitt.sector.objects.entity.utils;

import xyz.rokkiitt.sector.objects.entity.WalkingEntity;
import cn.nukkit.math.*;
import cn.nukkit.level.*;
import java.util.concurrent.locks.*;
import java.util.*;

public abstract class RouteFinder
{
    protected ArrayList<Node> nodes;
    protected boolean finished;
    protected boolean searching;
    protected int current;
    public WalkingEntity entity;
    protected Vector3 start;
    protected Vector3 destination;
    protected Level level;
    protected boolean interrupt;
    private ReentrantReadWriteLock lock;
    protected boolean reachable;
    
    RouteFinder(final WalkingEntity entity) {
        this.nodes = new ArrayList<Node>();
        this.finished = false;
        this.searching = false;
        this.current = 0;
        this.interrupt = false;
        this.lock = new ReentrantReadWriteLock();
        this.reachable = true;
        Objects.requireNonNull(entity, "RouteFinder: entity can not be null");
        this.entity = entity;
        this.level = entity.getLevel();
    }
    
    public WalkingEntity getEntity() {
        return this.entity;
    }
    
    public Vector3 getStart() {
        return this.start;
    }
    
    public void setStart(final Vector3 start) {
        if (!this.isSearching()) {
            this.start = start;
        }
    }
    
    public Vector3 getDestination() {
        return this.destination;
    }
    
    public void setDestination(final Vector3 destination) {
        this.destination = destination;
        if (this.isSearching()) {
            this.interrupt = true;
            this.research();
        }
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public boolean isSearching() {
        return this.searching;
    }
    
    public void addNode(final Node node) {
        try {
            this.lock.writeLock().lock();
            this.nodes.add(node);
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public void addNode(final ArrayList<Node> node) {
        try {
            this.lock.writeLock().lock();
            this.nodes.addAll(node);
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public boolean isReachable() {
        return this.reachable;
    }
    
    public Node getCurrentNode() {
        try {
            this.lock.readLock().lock();
            if (this.hasCurrentNode()) {
                return this.nodes.get(this.current);
            }
            return null;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    
    public boolean hasCurrentNode() {
        return this.current < this.nodes.size();
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public int getCurrent() {
        return this.current;
    }
    
    public boolean hasArrivedNode(final Vector3 vec) {
        try {
            this.lock.readLock().lock();
            if (this.hasNext() && this.getCurrentNode().getVector3() != null) {
                final Vector3 cur = this.getCurrentNode().getVector3();
                return vec.getX() == cur.getX() && vec.getZ() == cur.getZ();
            }
            return false;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    
    public void resetNodes() {
        try {
            this.lock.writeLock().lock();
            this.nodes.clear();
            this.current = 0;
            this.interrupt = false;
            this.destination = null;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public abstract boolean search();
    
    public void research() {
        this.resetNodes();
        this.search();
    }
    
    public boolean hasNext() {
        try {
            if (this.current + 1 < this.nodes.size()) {
                return this.nodes.get(this.current + 1) != null;
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    public Vector3 next() {
        try {
            this.lock.readLock().lock();
            if (this.hasNext()) {
                return this.nodes.get(++this.current).getVector3();
            }
            return null;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    
    public boolean isInterrupted() {
        return this.interrupt;
    }
    
    public boolean interrupt() {
        return this.interrupt ^= true;
    }
}
