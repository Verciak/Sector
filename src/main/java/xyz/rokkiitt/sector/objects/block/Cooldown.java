package xyz.rokkiitt.sector.objects.block;

import java.util.concurrent.*;
import cn.nukkit.*;

public final class Cooldown
{
    private static final Cooldown instance;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> cooldowns;
    
    public Cooldown() {
        this.cooldowns = new ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>();
    }
    
    public static Cooldown getInstance() {
        return Cooldown.instance;
    }
    
    public void add(final String username, final String identifier, Float time) {
        ConcurrentHashMap<String, Long> map = this.cooldowns.get(username);
        if (map == null) {
            map = new ConcurrentHashMap<String, Long>();
        }
        time *= 1000.0f;
        map.put(identifier, System.currentTimeMillis() + time.longValue());
        this.cooldowns.put(username, map);
    }
    
    public void add(final Player player, final String name, final Float time) {
        this.add(player.getName(), name, time);
    }
    
    public void remove(final String username, final String identifier) {
        final ConcurrentHashMap<String, Long> map = this.cooldowns.get(username);
        if (map == null) {
            return;
        }
        map.remove(identifier);
        this.cooldowns.put(username, map);
    }
    
    public void remove(final Player player, final String identifier) {
        this.remove(player.getName(), identifier);
    }
    
    public boolean has(final String username, final String identifier) {
        final ConcurrentHashMap<String, Long> map = this.cooldowns.get(username);
        if (map == null) {
            return false;
        }
        final Long value = map.get(identifier);
        return value != null && value > System.currentTimeMillis();
    }
    
    public boolean has(final Player player, final String identifier) {
        return this.has(player.getName(), identifier);
    }
    
    public Long get(final String username, final String identifier) {
        final ConcurrentHashMap<String, Long> map = this.cooldowns.get(username);
        if (map == null) {
            return 0L;
        }
        final Long value = map.get(identifier);
        if (value == null) {
            return 0L;
        }
        return value;
    }
    
    public Long get(final Player player, final String identifier) {
        return this.get(player.getName(), identifier);
    }
    
    static {
        instance = new Cooldown();
    }
}
