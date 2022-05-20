package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import xyz.rokkiitt.sector.objects.boss.Boss;
import xyz.rokkiitt.sector.objects.boss.BossManager;
import xyz.rokkiitt.sector.objects.entity.utils.Utils;
import xyz.rokkiitt.sector.utils.Util;

public abstract class WalkingMonster extends WalkingEntity implements Monster {
    protected float[] minDamage;

    protected float[] maxDamage;

    protected boolean canAttack;

    public long isAngryTo;

    public WalkingMonster(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.canAttack = true;
        this.isAngryTo = -1L;
    }

    public void setFollowTarget(Entity target) {
        setFollowTarget(target, true);
    }

    public void setFollowTarget(Entity target, boolean attack) {
        super.setFollowTarget(target);
        this.canAttack = attack;
    }

    public float getDamage() {
        return getDamage((Integer)null);
    }

    public float getDamage(Integer difficulty) {
        return Utils.rand(5, 15);
    }

    public float getMinDamage() {
        return getMinDamage((Integer)null);
    }

    public float getMinDamage(Integer difficulty) {
        if (difficulty == null || difficulty.intValue() > 3 || difficulty.intValue() < 0)
            difficulty = Integer.valueOf(Server.getInstance().getDifficulty());
        return this.minDamage[difficulty.intValue()];
    }

    public float getMaxDamage() {
        return getMaxDamage((Integer)null);
    }

    public float getMaxDamage(Integer difficulty) {
        if (difficulty == null || difficulty.intValue() > 3 || difficulty.intValue() < 0)
            difficulty = Integer.valueOf(Server.getInstance().getDifficulty());
        return this.maxDamage[difficulty.intValue()];
    }

    public void setDamage(float damage) {
        setDamage(damage, Server.getInstance().getDifficulty());
    }

    public void setDamage(float damage, int difficulty) {
        if (difficulty >= 1 && difficulty <= 3) {
            this.minDamage[difficulty] = damage;
            this.maxDamage[difficulty] = damage;
        }
    }

    public void setDamage(float[] damage) {
        if (damage.length < 4)
            throw new IllegalArgumentException("Invalid damage array length");
        if (this.minDamage == null || this.minDamage.length < 4)
            this.minDamage = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
        if (this.maxDamage == null || this.maxDamage.length < 4)
            this.maxDamage = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
        for (int i = 0; i < 4; i++) {
            this.minDamage[i] = damage[i];
            this.maxDamage[i] = damage[i];
        }
    }

    public void setMinDamage(float[] damage) {
        if (damage.length < 4)
            return;
        for (int i = 0; i < 4; i++)
            setMinDamage(Math.min(damage[i], getMaxDamage(Integer.valueOf(i))), i);
    }

    public void setMinDamage(float damage) {
        setMinDamage(damage, Server.getInstance().getDifficulty());
    }

    public void setMinDamage(float damage, int difficulty) {
        if (difficulty >= 1 && difficulty <= 3)
            this.minDamage[difficulty] = Math.min(damage, getMaxDamage(Integer.valueOf(difficulty)));
    }

    public void setMaxDamage(float[] damage) {
        if (damage.length < 4)
            return;
        for (int i = 0; i < 4; i++)
            setMaxDamage(Math.max(damage[i], getMinDamage(Integer.valueOf(i))), i);
    }

    public void setMaxDamage(float damage) {
        setMinDamage(damage, Server.getInstance().getDifficulty());
    }

    public void setMaxDamage(float damage, int difficulty) {
        if (difficulty >= 1 && difficulty <= 3)
            this.maxDamage[difficulty] = Math.max(damage, getMinDamage(Integer.valueOf(difficulty)));
    }

    public boolean onUpdate(int currentTick) {
        if (this.closed)
            return false;
        if (this.server.getDifficulty() < 1) {
            close();
            return false;
        }
        if (isAlive()) {
            int tickDiff = currentTick - this.lastUpdate;
            this.lastUpdate = currentTick;
            entityBaseTick(tickDiff);
            Vector3 target = updateMove(tickDiff);
            if (target instanceof Entity && (!isFriendly() || !(target instanceof cn.nukkit.Player) || ((Entity)target)
                    .getId() == this.isAngryTo)) {
                Entity entity = (Entity)target;
                if (!entity.closed && (target != this.followTarget || this.canAttack))
                    attackEntity(entity);
            }
            return true;
        }
        if (++this.deadTicks >= 23) {
            close();
            Boss boss = BossManager.users.iterator().next();
            BossManager.deleteBoss(boss);
            for(Player p : Server.getInstance().getOnlinePlayers().values()){
                if(getLastDamageCause() instanceof EntityDamageByEntityEvent){
                    p.sendTitle(Util.fixColor("&r&c&lBOSS"), Util.fixColor("&r&7BOSS ZOSTAL ZABITY PRZEZ &9" + ((EntityDamageByEntityEvent) getLastDamageCause()).getDamager().getName()), 30, 80, 30);
                }
            }
            return false;
        }
        return true;
    }
}
