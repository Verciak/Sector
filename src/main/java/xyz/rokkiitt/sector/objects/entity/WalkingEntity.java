package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BubbleParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import xyz.rokkiitt.sector.objects.entity.utils.RouteFinder;
import xyz.rokkiitt.sector.objects.entity.utils.RouteFinderSearchTask;
import xyz.rokkiitt.sector.objects.entity.utils.RouteFinderThreadPool;
import xyz.rokkiitt.sector.objects.entity.utils.Utils;

public abstract class WalkingEntity extends BaseEntity {
    protected RouteFinder route;

    protected final boolean isDrowned;

    public WalkingEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.route = null;
        this.isDrowned = false;
    }

    protected void checkTarget() {
        if (isKnockback())
            return;
        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() &&
                targetOption((EntityCreature)this.followTarget, distanceSquared((Vector3)this.followTarget)) && this.target != null)
            return;
        this.followTarget = null;
        if (!this.passengers.isEmpty())
            return;
        double near = 2.147483647E9D;
        for (Entity entity : getViewers().values()) {
            if (entity == this || !(entity instanceof EntityCreature) ||
                    entity instanceof Animal)
                continue;
            EntityCreature creature = (EntityCreature)entity;
            if (creature instanceof BaseEntity && ((BaseEntity)creature).isFriendly() == isFriendly())
                continue;
            double distance = distanceSquared((Vector3)creature);
            if (distance > near)
                continue;
            if (!targetOption(creature, distance))
                continue;
            near = distance;
            this.stayTime = 0;
            this.moveTime = 0;
            this.followTarget = (Entity)creature;
            if (this.route != null || !this.passengers.isEmpty())
                continue;
            this.target = (Vector3)creature;
        }
        if (this.followTarget instanceof EntityCreature && !this.followTarget.closed && this.followTarget.isAlive() &&
                targetOption((EntityCreature)this.followTarget,
                        distanceSquared((Vector3)this.followTarget)) && this.target != null)
            return;
        if (this.stayTime > 0) {
            if (Utils.rand(1, 100) > 5)
                return;
            int x = Utils.rand(10, 30);
            int z = Utils.rand(10, 30);
            this.target = (Vector3)add(Utils.rand() ? x : -x,
                    Utils.rand(-20.0D, 20.0D) / 10.0D, Utils.rand() ? z : -z);
        } else if (Utils.rand(1, 100) == 1) {
            int x = Utils.rand(10, 30);
            int z = Utils.rand(10, 30);
            this.stayTime = Utils.rand(100, 200);
            this.target = (Vector3)add(Utils.rand() ? x : -x,
                    Utils.rand(-20.0D, 20.0D) / 10.0D, Utils.rand() ? z : -z);
        } else if (this.moveTime <= 0 || this.target == null) {
            int x = Utils.rand(20, 100);
            int z = Utils.rand(20, 100);
            this.stayTime = 0;
            this.moveTime = Utils.rand(100, 200);
            this.target = (Vector3)add(Utils.rand() ? x : -x, 0.0D,
                    Utils.rand() ? z : -z);
        }
    }

    protected boolean checkJump(double dx, double dz) {
        if (this.motionY == (getGravity() * 2.0F)) {
            int i = this.level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int)this.y,
                    NukkitMath.floorDouble(this.z));
            return (i == 8 || i == 9);
        }
        int b = this.level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int)(this.y + 0.8D), NukkitMath.floorDouble(this.z));
        if (b == 8 || b == 9) {
            if (!this.isDrowned || this.target == null)
                this.motionY = (getGravity() * 2.0F);
            return true;
        }
        if (!this.onGround || this.stayTime > 0)
            return false;
        Block that = getLevel().getBlock(new Vector3(NukkitMath.floorDouble(this.x + dx), (int)this.y,
                NukkitMath.floorDouble(this.z + dz)));
        Block block = that.getSide(getHorizontalFacing());
        Block down = block.down();
        if (!down.isSolid() && !block.isSolid() && !down.down().isSolid()) {
            this.stayTime = 10;
        } else if (!block.canPassThrough() && block.up().canPassThrough() && that.up(2).canPassThrough()) {
            if (block instanceof cn.nukkit.block.BlockFence || block instanceof cn.nukkit.block.BlockFenceGate) {
                this.motionY = getGravity();
            } else if (this.motionY <= (getGravity() * 4.0F)) {
                this.motionY = (getGravity() * 4.0F);
            } else if (block instanceof cn.nukkit.block.BlockStairs) {
                this.motionY = (getGravity() * 4.0F);
            } else if (this.motionY <= (getGravity() * 8.0F)) {
                this.motionY = (getGravity() * 8.0F);
            } else {
                this.motionY += getGravity() * 0.25D;
            }
            return true;
        }
        return false;
    }

    public Vector3 updateMove(int tickDiff) {
        if (isImmobile())
            return null;
        if (!isMovement())
            return null;
        if (this.age % 10 == 0 && this.route != null && !this.route.isSearching()) {
            RouteFinderThreadPool.executeRouteFinderThread(new RouteFinderSearchTask(this.route));
            if (this.route.hasNext())
                this.target = this.route.next();
        }
        if (isKnockback()) {
            move(this.motionX, this.motionY, this.motionZ);
            this.motionY -= getGravity();
            updateMovement();
            return null;
        }
        if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.target != null) {
            double x = this.target.x - this.x;
            double z = this.target.z - this.z;
            double diff = Math.abs(x) + Math.abs(z);
            if (this.stayTime > 0 || distance((Vector3)this.followTarget) <= (getWidth() / 2.0F) + 0.05D) {
                this.motionX = 0.0D;
                this.motionZ = 0.0D;
            } else if (Utils.entityInsideWaterFast((Entity)this)) {
                this.motionX = getSpeed() * this.moveMultiplier * 0.05D * x / diff;
                this.motionZ = getSpeed() * this.moveMultiplier * 0.05D * z / diff;
                if (!this.isDrowned)
                    this.level.addParticle((Particle)new BubbleParticle((Vector3)
                            add(Utils.rand(-2.0D, 2.0D), Utils.rand(-0.5D, 0.0D), Utils.rand(-2.0D, 2.0D))));
            } else {
                this.motionX = getSpeed() * this.moveMultiplier * 0.1D * x / diff;
                this.motionZ = getSpeed() * this.moveMultiplier * 0.1D * z / diff;
            }
            if (this.passengers.isEmpty() && (this.stayTime <= 0 ||
                    Utils.rand()))
                this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
        }
        Vector3 before = this.target;
        checkTarget();
        if (this.target instanceof Vector3 || before != this.target) {
            double x2 = this.target.x - this.x;
            double z2 = this.target.z - this.z;
            double diff2 = Math.abs(x2) + Math.abs(z2);
            if (this.stayTime > 0 || distance(this.target) <= ((getWidth() / 2.0F) + 0.05D) *
                    nearbyDistanceMultiplier()) {
                this.motionX = 0.0D;
                this.motionZ = 0.0D;
            } else if (Utils.entityInsideWaterFast((Entity)this)) {
                this.motionX = getSpeed() * this.moveMultiplier * 0.05D * x2 / diff2;
                this.motionZ = getSpeed() * this.moveMultiplier * 0.05D * z2 / diff2;
                if (!this.isDrowned)
                    this.level.addParticle((Particle)new BubbleParticle((Vector3)
                            add(Utils.rand(-2.0D, 2.0D), Utils.rand(-0.5D, 0.0D), Utils.rand(-2.0D, 2.0D))));
            } else {
                this.motionX = getSpeed() * this.moveMultiplier * 0.15D * x2 / diff2;
                this.motionZ = getSpeed() * this.moveMultiplier * 0.15D * z2 / diff2;
            }
            if (this.passengers.isEmpty() && (this.stayTime <= 0 ||
                    Utils.rand()))
                this.yaw = Math.toDegrees(-Math.atan2(x2 / diff2, z2 / diff2));
        }
        double dx = this.motionX;
        double dz = this.motionZ;
        boolean isJump = checkJump(dx, dz);
        if (this.stayTime > 0) {
            this.stayTime -= tickDiff;
            move(0.0D, this.motionY, 0.0D);
        } else {
            Vector2 be = new Vector2(this.x + dx, this.z + dz);
            move(dx, this.motionY, dz);
            Vector2 af = new Vector2(this.x, this.z);
            if ((be.x != af.x || be.y != af.y) && !isJump)
                this.moveTime -= 90;
        }
        if (!isJump)
            if (this.onGround) {
                this.motionY = 0.0D;
            } else if (this.motionY > (-getGravity() * 4.0F)) {
                if (!(this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int)(this.y + 0.8D),
                        NukkitMath.floorDouble(this.z))) instanceof cn.nukkit.block.BlockLiquid))
                    this.motionY -= getGravity();
            } else {
                this.motionY -= getGravity();
            }
        updateMovement();
        if (this.route != null && this.route.hasCurrentNode() && this.route.hasArrivedNode((Vector3)this) && this.route
                .hasNext())
            this.target = this.route.next();
        return (this.followTarget != null) ? (Vector3)this.followTarget : this.target;
    }

    public RouteFinder getRoute() {
        return this.route;
    }

    public void setRoute(RouteFinder route) {
        this.route = route;
    }
}
