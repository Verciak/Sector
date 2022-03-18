package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.entity.*;
import cn.nukkit.item.*;
import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.math.*;
import xyz.rokkiitt.sector.utils.RandomUtil;
import cn.nukkit.network.protocol.*;
import java.util.concurrent.*;

public abstract class BaseEntity extends EntityCreature implements EntityAgeable
{
    public int stayTime;
    protected int moveTime;
    private int airTicks;
    protected float moveMultiplier;
    protected Vector3 target;
    protected Entity followTarget;
    private boolean baby;
    private boolean movement;
    private boolean friendly;
    protected int attackDelay;
    public Item[] armor;
    
    public BaseEntity(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
        this.stayTime = 0;
        this.moveTime = 0;
        this.airTicks = 0;
        this.moveMultiplier = 1.0f;
        this.target = null;
        this.followTarget = null;
        this.baby = false;
        this.movement = true;
        this.friendly = false;
        this.attackDelay = 0;
        this.setHealth((float)this.getMaxHealth());
        this.setAirTicks(300);
    }
    
    public abstract Vector3 updateMove(final int p0);
    
    public abstract int getKillExperience();
    
    public boolean isFriendly() {
        return this.friendly;
    }
    
    public boolean isMovement() {
        return this.movement;
    }
    
    public boolean isKnockback() {
        return this.attackTime > 0;
    }
    
    public void setFriendly(final boolean bool) {
        this.friendly = bool;
    }
    
    public void setMovement(final boolean value) {
        this.movement = value;
    }
    
    public double getSpeed() {
        if (this.isBaby()) {
            return 1.2;
        }
        return 1.0;
    }
    
    public Vector3 getTarget() {
        return this.target;
    }
    
    public void setTarget(final Vector3 vec) {
        this.target = vec;
    }
    
    public Entity getFollowTarget() {
        if (this.followTarget != null) {
            return this.followTarget;
        }
        if (this.target instanceof Entity) {
            return (Entity)this.target;
        }
        return null;
    }
    
    public Vector3 getTargetVector() {
        if (this.followTarget != null) {
            return (Vector3)this.followTarget;
        }
        if (this.target instanceof Entity) {
            return this.target;
        }
        return null;
    }
    
    public void setFollowTarget(final Entity target) {
        this.followTarget = target;
        this.moveTime = 0;
        this.stayTime = 0;
        this.target = null;
    }
    
    public boolean isBaby() {
        return this.baby;
    }
    
    public void setBaby(final boolean baby) {
        this.setDataFlag(0, 11, this.baby = baby);
        this.setScale(0.5f);
    }
    
    protected void initEntity() {
        super.initEntity();
        if (this.namedTag.contains("Movement")) {
            this.setMovement(this.namedTag.getBoolean("Movement"));
        }
        if (this.namedTag.contains("Age")) {
            this.age = this.namedTag.getShort("Age");
        }
        if (this.namedTag.getBoolean("Baby")) {
            this.setBaby(true);
        }
    }
    
    public String getName() {
        return this.getClass().getSimpleName();
    }
    
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Baby", this.isBaby());
        this.namedTag.putBoolean("Movement", this.isMovement());
        this.namedTag.putShort("Age", this.age);
    }
    
    public boolean targetOption(final EntityCreature creature, final double distance) {
        if (!(this instanceof Monster)) {
            return false;
        }
        if (creature instanceof Player) {
            final Player player = (Player)creature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= 100.0;
        }
        return creature.isAlive() && !creature.closed && distance <= 100.0;
    }
    
    public boolean entityBaseTick(final int tickDiff) {
        super.entityBaseTick(tickDiff);
        if (this.canDespawn()) {
            this.close();
        }
        if (this instanceof Monster && this.attackDelay < 200) {
            ++this.attackDelay;
        }
        return true;
    }
    
    public boolean attack(final EntityDamageEvent source) {
        if (this.isKnockback() && source instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)source).getDamager() instanceof Player) {
            return false;
        }
        if (this.fireProof && (source.getCause() == EntityDamageEvent.DamageCause.FIRE || source.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || source.getCause() == EntityDamageEvent.DamageCause.LAVA)) {
            return false;
        }
        if (source instanceof EntityDamageByEntityEvent) {
            ((EntityDamageByEntityEvent)source).setKnockBack(0.25f);
        }
        super.attack(source);
        this.target = null;
        this.stayTime = 0;
        return true;
    }
    
    public boolean move(double dx, double dy, double dz) {
        if (dy < -10.0 || dy > 10.0) {
            return false;
        }
        final double movX = dx * this.moveMultiplier;
        final double movY = dy;
        final double movZ = dz * this.moveMultiplier;
        final AxisAlignedBB[] list;
        final AxisAlignedBB[] array;
        final AxisAlignedBB[] collisionCubes = array = (list = this.level.getCollisionCubes((Entity)this, this.boundingBox.addCoord(dx, dy, dz), (boolean)(0 != 0)));
        for (final AxisAlignedBB bb : array) {
            dx = bb.calculateXOffset(this.boundingBox, dx);
        }
        this.boundingBox.offset(dx, 0.0, 0.0);
        for (final AxisAlignedBB bb : list) {
            dz = bb.calculateZOffset(this.boundingBox, dz);
        }
        this.boundingBox.offset(0.0, 0.0, dz);
        for (final AxisAlignedBB bb : list) {
            dy = bb.calculateYOffset(this.boundingBox, dy);
        }
        this.boundingBox.offset(0.0, dy, 0.0);
        this.setComponents(this.x + dx, this.y + dy, this.z + dz);
        this.checkChunks();
        this.checkGroundState(movX, movY, movZ, dx, dy, dz);
        this.updateFallState(this.onGround);
        return true;
    }
    
    public boolean onInteract(final Player player, final Item item) {
        if (item.getId() == 421 && item.hasCustomName()) {
            this.setNameTag(item.getCustomName());
            this.setNameTagVisible(true);
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            return true;
        }
        return false;
    }
    
    protected float getMountedYOffset() {
        return this.getHeight() * 0.75f;
    }
    
    protected Item[] getRandomArmor() {
        final Item[] slots = new Item[4];
        Item helmet = Item.get(0);
        Item chestplate = Item.get(0);
        Item leggings = Item.get(0);
        Item boots = Item.get(0);
        switch (RandomUtil.rand(1, 5)) {
            case 1: {
                if (RandomUtil.rand(1, 100) < 39 && RandomUtil.rand(0, 1) == 0) {
                    helmet = Item.get(298, Integer.valueOf(0), 1);
                    this.addHealth(1);
                    break;
                }
                break;
            }
            case 2: {
                if (RandomUtil.rand(1, 100) < 50 && RandomUtil.rand(0, 1) == 0) {
                    helmet = Item.get(314, Integer.valueOf(0), 1);
                    this.addHealth(1);
                    break;
                }
                break;
            }
            case 3: {
                if (RandomUtil.rand(1, 100) < 14 && RandomUtil.rand(0, 1) == 0) {
                    helmet = Item.get(302, Integer.valueOf(0), 1);
                    this.addHealth(1);
                    break;
                }
                break;
            }
            case 4: {
                if (RandomUtil.rand(1, 100) < 3 && RandomUtil.rand(0, 1) == 0) {
                    helmet = Item.get(306, Integer.valueOf(0), 1);
                    this.addHealth(1);
                    break;
                }
                break;
            }
            case 5: {
                if (RandomUtil.rand(1, 100) == 100 && RandomUtil.rand(0, 1) == 0) {
                    helmet = Item.get(310, Integer.valueOf(0), 1);
                    this.addHealth(2);
                    break;
                }
                break;
            }
        }
        slots[0] = helmet;
        if (RandomUtil.rand(1, 4) != 1) {
            switch (RandomUtil.rand(1, 5)) {
                case 1: {
                    if (RandomUtil.rand(1, 100) < 39 && RandomUtil.rand(0, 1) == 0) {
                        chestplate = Item.get(299, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (RandomUtil.rand(1, 100) < 50 && RandomUtil.rand(0, 1) == 0) {
                        chestplate = Item.get(315, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 3: {
                    if (RandomUtil.rand(1, 100) < 14 && RandomUtil.rand(0, 1) == 0) {
                        chestplate = Item.get(303, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 4: {
                    if (RandomUtil.rand(1, 100) < 3 && RandomUtil.rand(0, 1) == 0) {
                        chestplate = Item.get(307, Integer.valueOf(0), 1);
                        this.addHealth(2);
                        break;
                    }
                    break;
                }
                case 5: {
                    if (RandomUtil.rand(1, 100) == 100 && RandomUtil.rand(0, 1) == 0) {
                        chestplate = Item.get(311, Integer.valueOf(0), 1);
                        this.addHealth(3);
                        break;
                    }
                    break;
                }
            }
        }
        slots[1] = chestplate;
        if (RandomUtil.rand(1, 2) == 2) {
            switch (RandomUtil.rand(1, 5)) {
                case 1: {
                    if (RandomUtil.rand(1, 100) < 39 && RandomUtil.rand(0, 1) == 0) {
                        leggings = Item.get(300, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (RandomUtil.rand(1, 100) < 50 && RandomUtil.rand(0, 1) == 0) {
                        leggings = Item.get(316, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 3: {
                    if (RandomUtil.rand(1, 100) < 14 && RandomUtil.rand(0, 1) == 0) {
                        leggings = Item.get(304, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 4: {
                    if (RandomUtil.rand(1, 100) < 3 && RandomUtil.rand(0, 1) == 0) {
                        leggings = Item.get(308, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 5: {
                    if (RandomUtil.rand(1, 100) == 100 && RandomUtil.rand(0, 1) == 0) {
                        leggings = Item.get(312, Integer.valueOf(0), 1);
                        this.addHealth(2);
                        break;
                    }
                    break;
                }
            }
        }
        slots[2] = leggings;
        if (RandomUtil.rand(1, 5) < 3) {
            switch (RandomUtil.rand(1, 5)) {
                case 1: {
                    if (RandomUtil.rand(1, 100) < 39 && RandomUtil.rand(0, 1) == 0) {
                        boots = Item.get(301, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (RandomUtil.rand(1, 100) < 50 && RandomUtil.rand(0, 1) == 0) {
                        boots = Item.get(317, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 3: {
                    if (RandomUtil.rand(1, 100) < 14 && RandomUtil.rand(0, 1) == 0) {
                        boots = Item.get(305, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 4: {
                    if (RandomUtil.rand(1, 100) < 3 && RandomUtil.rand(0, 1) == 0) {
                        boots = Item.get(309, Integer.valueOf(0), 1);
                        this.addHealth(1);
                        break;
                    }
                    break;
                }
                case 5: {
                    if (RandomUtil.rand(1, 100) == 100 && RandomUtil.rand(0, 1) == 0) {
                        boots = Item.get(313, Integer.valueOf(0), 1);
                        this.addHealth(2);
                        break;
                    }
                    break;
                }
            }
        }
        slots[3] = boots;
        return slots;
    }
    
    private void addHealth(final int health) {
        this.setMaxHealth(this.getMaxHealth() + health);
        this.setHealth(this.getHealth() + health);
    }
    
    public boolean canDespawn() {
        final int despawnTicks = 6000;
        return this.age > 6000 && !this.hasCustomName() && !(this instanceof Boss);
    }
    
    public int nearbyDistanceMultiplier() {
        return 1;
    }
    
    public int getAirTicks() {
        return this.airTicks;
    }
    
    public void setAirTicks(final int ticks) {
        this.airTicks = ticks;
    }
    
    public void addMovement(final double x, final double y, final double z, final double yaw, final double pitch, final double headYaw) {
        final MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
        pk.eid = this.id;
        pk.x = (float)x;
        pk.y = (float)y;
        pk.z = (float)z;
        pk.yaw = (float)yaw;
        pk.headYaw = (float)headYaw;
        pk.pitch = (float)pitch;
        pk.onGround = this.onGround;
        for (final Player p : this.hasSpawned.values()) {
            p.batchDataPacket((DataPacket)pk);
        }
    }
    
    public void addMotion(final double motionX, final double motionY, final double motionZ) {
        final SetEntityMotionPacket pk = new SetEntityMotionPacket();
        pk.eid = this.id;
        pk.motionX = (float)motionX;
        pk.motionY = (float)motionY;
        pk.motionZ = (float)motionZ;
        for (final Player p : this.hasSpawned.values()) {
            p.batchDataPacket((DataPacket)pk);
        }
    }
    
    protected void checkGroundState(final double movX, final double movY, final double movZ, final double dx, final double dy, final double dz) {
        if (this.onGround && movX == 0.0 && movY == 0.0 && movZ == 0.0 && dx == 0.0 && dy == 0.0 && dz == 0.0) {
            return;
        }
        this.isCollidedVertically = (movY != dy);
        this.isCollidedHorizontally = (movX != dx || movZ != dz);
        this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
        this.onGround = (movY != dy && movY < 0.0);
    }
    
    public static void setProjectileMotion(final Entity projectile, final double pitch, final double yawR, final double pitchR, final double speed) {
        final double verticalMultiplier = Math.cos(pitchR);
        double x = verticalMultiplier * Math.sin(-yawR);
        double z = verticalMultiplier * Math.cos(yawR);
        double y = Math.sin(-Math.toRadians(pitch));
        final double magnitude = Math.sqrt(x * x + y * y + z * z);
        if (magnitude > 0.0) {
            x += x * (speed - magnitude) / magnitude;
            y += y * (speed - magnitude) / magnitude;
            z += z * (speed - magnitude) / magnitude;
        }
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        x += rand.nextGaussian() * 0.007499999832361937 * 6.0;
        y += rand.nextGaussian() * 0.007499999832361937 * 6.0;
        z += rand.nextGaussian() * 0.007499999832361937 * 6.0;
        projectile.setMotion(new Vector3(x, y, z));
    }
    
    public void resetFallDistance() {
        this.highestPosition = this.y;
    }
    
    public boolean setMotion(final Vector3 motion) {
        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        if (!this.justCreated) {
            this.updateMovement();
        }
        return true;
    }
}
