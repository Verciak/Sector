package xyz.rokkiitt.sector.objects.entity.monster;

import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import xyz.rokkiitt.sector.objects.entity.WalkingMonster;
import xyz.rokkiitt.sector.objects.entity.utils.Utils;
import xyz.rokkiitt.sector.objects.entity.utils.WalkerRouteFinder;
import cn.nukkit.event.*;
import cn.nukkit.math.*;
import cn.nukkit.level.particle.*;
import cn.nukkit.item.*;
import cn.nukkit.entity.mob.*;
import java.util.*;
import cn.nukkit.*;
import cn.nukkit.level.*;
import cn.nukkit.plugin.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.entity.*;
import cn.nukkit.entity.data.*;
import xyz.rokkiitt.sector.objects.level.Explosion;

public class Creeper extends WalkingMonster implements EntityExplosive
{
    public static final int NETWORK_ID = 33;
    private int bombTime;
    private boolean exploding;
    
    public Creeper(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
        this.bombTime = 0;
        this.route = new WalkerRouteFinder(this);
    }
    
    public int getNetworkId() {
        return 33;
    }
    
    public float getWidth() {
        return 0.6f;
    }
    
    public float getHeight() {
        return 1.7f;
    }
    
    public double getSpeed() {
        return 0.9;
    }
    
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(20);
        if (this.namedTag.contains("powered")) {
            this.setPowered(this.namedTag.getBoolean("powered"));
        }
    }
    
    public int getBombTime() {
        return this.bombTime;
    }
    
    public void explode() {
        if (this.closed) {
            return;
        }
        final EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent((Entity)this, this.isPowered() ? 6.0 : 3.0);
        this.server.getPluginManager().callEvent((Event)ev);
        if (!ev.isCancelled()) {
            final Explosion explosion = new Explosion((Position)this, (float)ev.getForce(), (Entity)this);
            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }
            explosion.explodeB();
            this.level.addParticle((Particle)new HugeExplodeSeedParticle((Vector3)this));
        }
        this.close();
    }
    
    public void attackEntity(final Entity player) {
    }
    
    public Item[] getDrops() {
        final List<Item> drops = new ArrayList<Item>();
        for (int i = 0; i < Utils.rand(0, 2); ++i) {
            drops.add(Item.get(289, Integer.valueOf(0), 1));
        }
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            final Entity killer = ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager();
            if (killer instanceof EntitySkeleton || killer instanceof EntityStray) {
                drops.add(Item.get(Utils.rand(500, 511), Integer.valueOf(0), 1));
            }
            if (killer instanceof EntityCreeper && ((EntityCreeper)killer).isPowered()) {
                drops.add(Item.get(397, Integer.valueOf(4), 1));
            }
        }
        return drops.toArray(new Item[0]);
    }
    
    public int getKillExperience() {
        return 5;
    }
    
    public int getMaxFallHeight() {
        return (this.followTarget == null) ? 3 : (3 + (int)(this.getHealth() - 1.0f));
    }
    
    public boolean onInteract(final Player player, final Item item, final Vector3 clickedPos) {
        if (item.getId() == 259 && !this.exploding) {
            this.exploding = true;
            this.level.addSound((Vector3)this, Sound.FIRE_IGNITE);
            this.setDataFlag(0, 10, true);
            this.level.addSound((Vector3)this, Sound.RANDOM_FUSE);
            this.level.getServer().getScheduler().scheduleDelayedTask((Plugin)null, this::explode, 30);
            return true;
        }
        return super.onInteract(player, item, clickedPos);
    }
    
    public boolean isPowered() {
        return this.getDataFlag(0, 9);
    }
    
    public void setPowered(final boolean charged) {
        this.setDataFlag(0, 9, charged);
    }
    
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("powered", this.isPowered());
    }
    
    public void onStruckByLightning(final Entity entity) {
        if (this.attack((EntityDamageEvent)new EntityDamageByEntityEvent(entity, (Entity)this, EntityDamageEvent.DamageCause.LIGHTNING, 5.0f))) {
            if (this.fireTicks < 160) {
                this.setOnFire(8);
            }
            this.setPowered(true);
        }
    }
    
    public boolean entityBaseTick(final int tickDiff) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        final boolean hasUpdate = super.entityBaseTick(tickDiff);
        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.target != null) {
            final double x = this.target.x - this.x;
            final double z = this.target.z - this.z;
            final double diff = Math.abs(x) + Math.abs(z);
            final double distance = this.followTarget.distance((Vector3)this);
            if (distance <= 4.0) {
                if (this.followTarget instanceof EntityCreature) {
                    if (!this.exploding) {
                        if (this.bombTime >= 0) {
                            this.level.addSound((Vector3)this, Sound.RANDOM_FUSE);
                            this.setDataProperty((EntityData)new IntEntityData(55, this.bombTime));
                            this.setDataFlag(0, 10, true);
                        }
                        this.bombTime += tickDiff;
                        if (this.bombTime >= 30) {
                            this.explode();
                            return false;
                        }
                    }
                    if (distance <= 1.0) {
                        this.stayTime = 10;
                    }
                }
            }
            else {
                if (!this.exploding) {
                    this.setDataFlag(0, 10, false);
                    this.bombTime = 0;
                }
                this.motionX = this.getSpeed() * 0.15 * (x / diff);
                this.motionZ = this.getSpeed() * 0.15 * (z / diff);
            }
        }
        return hasUpdate;
    }
}
