package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.entity.*;

import xyz.rokkiitt.sector.objects.entity.utils.RouteFinder;
import xyz.rokkiitt.sector.objects.entity.utils.RouteFinderSearchTask;
import xyz.rokkiitt.sector.objects.entity.utils.RouteFinderThreadPool;
import xyz.rokkiitt.sector.objects.entity.utils.Utils;
import cn.nukkit.level.particle.*;
import cn.nukkit.math.*;
import cn.nukkit.block.*;

public abstract class WalkingEntity extends BaseEntity
{
    protected RouteFinder route;
    protected final boolean isDrowned;
    
    public WalkingEntity(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
        this.route = null;
        this.isDrowned = false;
    }
    
    protected void checkTarget() {
        if (this.isKnockback()) {
            return;
        }
        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.targetOption((EntityCreature)this.followTarget, this.distanceSquared((Vector3)this.followTarget)) && this.target != null) {
            return;
        }
        this.followTarget = null;
        if (!this.passengers.isEmpty()) {
            return;
        }
        double near = 2.147483647E9;
        for (final Entity entity : this.getViewers().values()) {
            if (entity != this && entity instanceof EntityCreature) {
                if (entity instanceof Animal) {
                    continue;
                }
                final EntityCreature creature = (EntityCreature)entity;
                if (creature instanceof BaseEntity && ((BaseEntity)creature).isFriendly() == this.isFriendly()) {
                    continue;
                }
                final double distance = this.distanceSquared((Vector3)creature);
                if (distance > near) {
                    continue;
                }
                if (!this.targetOption(creature, distance)) {
                    continue;
                }
                near = distance;
                this.stayTime = 0;
                this.moveTime = 0;
                this.followTarget = (Entity)creature;
                if (this.route != null) {
                    continue;
                }
                if (!this.passengers.isEmpty()) {
                    continue;
                }
                this.target = (Vector3)creature;
            }
        }
        if (this.followTarget instanceof EntityCreature && !this.followTarget.closed && this.followTarget.isAlive() && this.targetOption((EntityCreature)this.followTarget, this.distanceSquared((Vector3)this.followTarget)) && this.target != null) {
            return;
        }
        if (this.stayTime > 0) {
            if (Utils.rand(1, 100) > 5) {
                return;
            }
            final int x = Utils.rand(10, 30);
            final int z = Utils.rand(10, 30);
            this.target = (Vector3)this.add(Utils.rand() ? ((double)x) : ((double)(-x)), Utils.rand(-20.0, 20.0) / 10.0, Utils.rand() ? ((double)z) : ((double)(-z)));
        }
        else if (Utils.rand(1, 100) == 1) {
            final int x = Utils.rand(10, 30);
            final int z = Utils.rand(10, 30);
            this.stayTime = Utils.rand(100, 200);
            this.target = (Vector3)this.add(Utils.rand() ? ((double)x) : ((double)(-x)), Utils.rand(-20.0, 20.0) / 10.0, Utils.rand() ? ((double)z) : ((double)(-z)));
        }
        else if (this.moveTime <= 0 || this.target == null) {
            final int x = Utils.rand(20, 100);
            final int z = Utils.rand(20, 100);
            this.stayTime = 0;
            this.moveTime = Utils.rand(100, 200);
            this.target = (Vector3)this.add(Utils.rand() ? ((double)x) : ((double)(-x)), 0.0, Utils.rand() ? ((double)z) : ((double)(-z)));
        }
    }
    
    protected boolean checkJump(final double dx, final double dz) {
        if (this.motionY == this.getGravity() * 2.0f) {
            final int b = this.level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int)this.y, NukkitMath.floorDouble(this.z));
            return b == 8 || b == 9;
        }
        final int b = this.level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int)(this.y + 0.8), NukkitMath.floorDouble(this.z));
        if (b == 8 || b == 9) {
            if (!this.isDrowned || this.target == null) {
                this.motionY = this.getGravity() * 2.0f;
            }
            return true;
        }
        if (!this.onGround || this.stayTime > 0) {
            return false;
        }
        final Block that = this.getLevel().getBlock(new Vector3((double)NukkitMath.floorDouble(this.x + dx), (double)(int)this.y, (double)NukkitMath.floorDouble(this.z + dz)));
        final Block block = that.getSide(this.getHorizontalFacing());
        final Block down = block.down();
        if (!down.isSolid() && !block.isSolid() && !down.down().isSolid()) {
            this.stayTime = 10;
        }
        else if (!block.canPassThrough() && block.up().canPassThrough() && that.up(2).canPassThrough()) {
            if (block instanceof BlockFence || block instanceof BlockFenceGate) {
                this.motionY = this.getGravity();
            }
            else if (this.motionY <= this.getGravity() * 4.0f) {
                this.motionY = this.getGravity() * 4.0f;
            }
            else if (block instanceof BlockStairs) {
                this.motionY = this.getGravity() * 4.0f;
            }
            else if (this.motionY <= this.getGravity() * 8.0f) {
                this.motionY = this.getGravity() * 8.0f;
            }
            else {
                this.motionY += this.getGravity() * 0.25;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public Vector3 updateMove(final int tickDiff) {
        if (this.isImmobile()) {
            return null;
        }
        if (!this.isMovement()) {
            return null;
        }
        if (this.age % 10 == 0 && this.route != null && !this.route.isSearching()) {
            RouteFinderThreadPool.executeRouteFinderThread(new RouteFinderSearchTask(this.route));
            if (this.route.hasNext()) {
                this.target = this.route.next();
            }
        }
        if (this.isKnockback()) {
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionY -= this.getGravity();
            this.updateMovement();
            return null;
        }
        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.target != null) {
            final double x = this.target.x - this.x;
            final double z = this.target.z - this.z;
            final double diff = Math.abs(x) + Math.abs(z);
            if (this.stayTime > 0 || this.distance((Vector3)this.followTarget) <= this.getWidth() / 2.0f + 0.05) {
                this.motionX = 0.0;
                this.motionZ = 0.0;
            }
            else if (Utils.entityInsideWaterFast((Entity)this)) {
                this.motionX = this.getSpeed() * this.moveMultiplier * 0.05 * (x / diff);
                this.motionZ = this.getSpeed() * this.moveMultiplier * 0.05 * (z / diff);
                if (!this.isDrowned) {
                    this.level.addParticle((Particle)new BubbleParticle((Vector3)this.add(Utils.rand(-2.0, 2.0), Utils.rand(-0.5, 0.0), Utils.rand(-2.0, 2.0))));
                }
            }
            else {
                this.motionX = this.getSpeed() * this.moveMultiplier * 0.1 * (x / diff);
                this.motionZ = this.getSpeed() * this.moveMultiplier * 0.1 * (z / diff);
            }
            if (this.passengers.isEmpty() && (this.stayTime <= 0 || Utils.rand())) {
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
            }
        }
        final Vector3 before = this.target;
        this.checkTarget();
        if (this.target instanceof Vector3 || before != this.target) {
            final double x2 = this.target.x - this.x;
            final double z2 = this.target.z - this.z;
            final double diff2 = Math.abs(x2) + Math.abs(z2);
            if (this.stayTime > 0 || this.distance(this.target) <= (this.getWidth() / 2.0f + 0.05) * this.nearbyDistanceMultiplier()) {
                this.motionX = 0.0;
                this.motionZ = 0.0;
            }
            else if (Utils.entityInsideWaterFast((Entity)this)) {
                this.motionX = this.getSpeed() * this.moveMultiplier * 0.05 * (x2 / diff2);
                this.motionZ = this.getSpeed() * this.moveMultiplier * 0.05 * (z2 / diff2);
                if (!this.isDrowned) {
                    this.level.addParticle((Particle)new BubbleParticle((Vector3)this.add(Utils.rand(-2.0, 2.0), Utils.rand(-0.5, 0.0), Utils.rand(-2.0, 2.0))));
                }
            }
            else {
                this.motionX = this.getSpeed() * this.moveMultiplier * 0.15 * (x2 / diff2);
                this.motionZ = this.getSpeed() * this.moveMultiplier * 0.15 * (z2 / diff2);
            }
            if (this.passengers.isEmpty() && (this.stayTime <= 0 || Utils.rand())) {
                this.yaw = Math.toDegrees(-Math.atan2(x2 / diff2, z2 / diff2));
            }
        }
        final double dx = this.motionX;
        final double dz = this.motionZ;
        final boolean isJump = this.checkJump(dx, dz);
        if (this.stayTime > 0) {
            this.stayTime -= tickDiff;
            this.move(0.0, this.motionY, 0.0);
        }
        else {
            final Vector2 be = new Vector2(this.x + dx, this.z + dz);
            this.move(dx, this.motionY, dz);
            final Vector2 af = new Vector2(this.x, this.z);
            if ((be.x != af.x || be.y != af.y) && !isJump) {
                this.moveTime -= 90;
            }
        }
        if (!isJump) {
            if (this.onGround) {
                this.motionY = 0.0;
            }
            else if (this.motionY > -this.getGravity() * 4.0f) {
                if (!(this.level.getBlock(new Vector3((double)NukkitMath.floorDouble(this.x), (double)(int)(this.y + 0.8), (double)NukkitMath.floorDouble(this.z))) instanceof BlockLiquid)) {
                    this.motionY -= this.getGravity();
                }
            }
            else {
                this.motionY -= this.getGravity();
            }
        }
        this.updateMovement();
        if (this.route != null && this.route.hasCurrentNode() && this.route.hasArrivedNode((Vector3)this) && this.route.hasNext()) {
            this.target = this.route.next();
        }
        return (Vector3)((this.followTarget != null) ? this.followTarget : this.target);
    }
    
    public RouteFinder getRoute() {
        return this.route;
    }
    
    public void setRoute(final RouteFinder route) {
        this.route = route;
    }
}
