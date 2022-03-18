package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.entity.*;
import xyz.rokkiitt.sector.objects.entity.utils.Utils;
import cn.nukkit.*;
import cn.nukkit.math.*;

public abstract class WalkingMonster extends WalkingEntity implements Monster
{
    protected float[] minDamage;
    protected float[] maxDamage;
    protected boolean canAttack;
    public long isAngryTo;
    
    public WalkingMonster(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
        this.canAttack = true;
        this.isAngryTo = -1L;
    }
    
    @Override
    public void setFollowTarget(final Entity target) {
        this.setFollowTarget(target, true);
    }
    
    public void setFollowTarget(final Entity target, final boolean attack) {
        super.setFollowTarget(target);
        this.canAttack = attack;
    }
    
    @Override
    public float getDamage() {
        return this.getDamage(null);
    }
    
    @Override
    public float getDamage(final Integer difficulty) {
        return Utils.rand(this.getMinDamage(difficulty), this.getMaxDamage(difficulty));
    }
    
    @Override
    public float getMinDamage() {
        return this.getMinDamage(null);
    }
    
    @Override
    public float getMinDamage(Integer difficulty) {
        if (difficulty == null || difficulty > 3 || difficulty < 0) {
            difficulty = Server.getInstance().getDifficulty();
        }
        return this.minDamage[difficulty];
    }
    
    @Override
    public float getMaxDamage() {
        return this.getMaxDamage(null);
    }
    
    @Override
    public float getMaxDamage(Integer difficulty) {
        if (difficulty == null || difficulty > 3 || difficulty < 0) {
            difficulty = Server.getInstance().getDifficulty();
        }
        return this.maxDamage[difficulty];
    }
    
    @Override
    public void setDamage(final float damage) {
        this.setDamage(damage, Server.getInstance().getDifficulty());
    }
    
    @Override
    public void setDamage(final float damage, final int difficulty) {
        if (difficulty >= 1 && difficulty <= 3) {
            this.minDamage[difficulty] = damage;
            this.maxDamage[difficulty] = damage;
        }
    }
    
    @Override
    public void setDamage(final float[] damage) {
        if (damage.length < 4) {
            throw new IllegalArgumentException("Invalid damage array length");
        }
        if (this.minDamage == null || this.minDamage.length < 4) {
            this.minDamage = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        }
        if (this.maxDamage == null || this.maxDamage.length < 4) {
            this.maxDamage = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        }
        for (int i = 0; i < 4; ++i) {
            this.minDamage[i] = damage[i];
            this.maxDamage[i] = damage[i];
        }
    }
    
    @Override
    public void setMinDamage(final float[] damage) {
        if (damage.length < 4) {
            return;
        }
        for (int i = 0; i < 4; ++i) {
            this.setMinDamage(Math.min(damage[i], this.getMaxDamage(i)), i);
        }
    }
    
    @Override
    public void setMinDamage(final float damage) {
        this.setMinDamage(damage, Server.getInstance().getDifficulty());
    }
    
    @Override
    public void setMinDamage(final float damage, final int difficulty) {
        if (difficulty >= 1 && difficulty <= 3) {
            this.minDamage[difficulty] = Math.min(damage, this.getMaxDamage(difficulty));
        }
    }
    
    @Override
    public void setMaxDamage(final float[] damage) {
        if (damage.length < 4) {
            return;
        }
        for (int i = 0; i < 4; ++i) {
            this.setMaxDamage(Math.max(damage[i], this.getMinDamage(i)), i);
        }
    }
    
    @Override
    public void setMaxDamage(final float damage) {
        this.setMinDamage(damage, Server.getInstance().getDifficulty());
    }
    
    @Override
    public void setMaxDamage(final float damage, final int difficulty) {
        if (difficulty >= 1 && difficulty <= 3) {
            this.maxDamage[difficulty] = Math.max(damage, this.getMinDamage(difficulty));
        }
    }
    
    public boolean onUpdate(final int currentTick) {
        if (this.closed) {
            return false;
        }
        if (this.server.getDifficulty() < 1) {
            this.close();
            return false;
        }
        if (this.isAlive()) {
            final int tickDiff = currentTick - this.lastUpdate;
            this.lastUpdate = currentTick;
            this.entityBaseTick(tickDiff);
            final Vector3 target = this.updateMove(tickDiff);
            if (target instanceof Entity && (!this.isFriendly() || !(target instanceof Player) || ((Entity)target).getId() == this.isAngryTo)) {
                final Entity entity = (Entity)target;
                if (!entity.closed && (target != this.followTarget || this.canAttack)) {
                    this.attackEntity(entity);
                }
            }
            return true;
        }
        if (++this.deadTicks >= 23) {
            this.close();
            return false;
        }
        return true;
    }
}
