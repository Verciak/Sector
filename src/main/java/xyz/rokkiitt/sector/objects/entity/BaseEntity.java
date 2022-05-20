package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import java.util.concurrent.ThreadLocalRandom;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;

public abstract class BaseEntity extends EntityCreature implements EntityAgeable {
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

    public BaseEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.stayTime = 0;
        this.moveTime = 0;
        this.airTicks = 0;
        this.moveMultiplier = 1.0F;
        this.target = null;
        this.followTarget = null;
        this.baby = false;
        this.movement = true;
        this.friendly = false;
        this.attackDelay = 0;
        setHealth(getMaxHealth());
        setAirTicks(300);
    }

    public abstract Vector3 updateMove(int paramInt);

    public abstract int getKillExperience();

    public boolean isFriendly() {
        return this.friendly;
    }

    public boolean isMovement() {
        return this.movement;
    }

    public boolean isKnockback() {
        return (this.attackTime > 0);
    }

    public void setFriendly(boolean bool) {
        this.friendly = bool;
    }

    public void setMovement(boolean value) {
        this.movement = value;
    }

    public double getSpeed() {
        if (isBaby())
            return 1.2D;
        return 1.0D;
    }

    public Vector3 getTarget() {
        return this.target;
    }

    public void setTarget(Vector3 vec) {
        this.target = vec;
    }

    public Entity getFollowTarget() {
        if (this.followTarget != null)
            return this.followTarget;
        if (this.target instanceof Entity)
            return (Entity)this.target;
        return null;
    }

    public Vector3 getTargetVector() {
        if (this.followTarget != null)
            return (Vector3)this.followTarget;
        if (this.target instanceof Entity)
            return this.target;
        return null;
    }

    public void setFollowTarget(Entity target) {
        this.followTarget = target;
        this.moveTime = 0;
        this.stayTime = 0;
        this.target = null;
    }

    public boolean isBaby() {
        return this.baby;
    }

    public void setBaby(boolean baby) {
        setDataFlag(0, 11, this.baby = baby);
        setScale(0.5F);
    }

    protected void initEntity() {
        super.initEntity();
        if (this.namedTag.contains("Movement"))
            setMovement(this.namedTag.getBoolean("Movement"));
        if (this.namedTag.contains("Age"))
            this.age = this.namedTag.getShort("Age");
        if (this.namedTag.getBoolean("Baby"))
            setBaby(true);
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("Baby", isBaby());
        this.namedTag.putBoolean("Movement", isMovement());
        this.namedTag.putShort("Age", this.age);
    }

    public boolean targetOption(EntityCreature creature, double distance) {
        if (!(this instanceof Monster))
            return false;
        if (creature instanceof Player) {
            Player player = (Player)creature;
            return (!player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= 100.0D);
        }
        return (creature.isAlive() && !creature.closed && distance <= 100.0D);
    }

    public boolean entityBaseTick(int tickDiff) {
        super.entityBaseTick(tickDiff);
        if (this instanceof Monster && this.attackDelay < 200)
            this.attackDelay++;
        return true;
    }

    public boolean attack(EntityDamageEvent source) {
        if (isKnockback() && source instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)source)
                .getDamager() instanceof Player)
            return false;
        if (this.fireProof && (source.getCause() == EntityDamageEvent.DamageCause.FIRE || source
                .getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || source
                .getCause() == EntityDamageEvent.DamageCause.LAVA))
            return false;
        if (source instanceof EntityDamageByEntityEvent) {
            ((EntityDamageByEntityEvent) source).setKnockBack(0.25F);

            Player p = (Player) ((EntityDamageByEntityEvent) source).getDamager();
            p.sendTip(Util.fixColor("&r&c&l   BOSS\n&r&7&lHP: &9" + this.getHealth() + " &c\u2665 \n\n\n\n\n\n"));
        }


        super.attack(source);
        this.target = null;
        this.stayTime = 0;
        return true;
    }

    public boolean move(double dx, double dy, double dz) {
        if (dy < -10.0D || dy > 10.0D)
            return false;
        double movX = dx * this.moveMultiplier;
        double movY = dy;
        double movZ = dz * this.moveMultiplier;
        AxisAlignedBB[] collisionCubes = this.level.getCollisionCubes((Entity)this, this.boundingBox
                .addCoord(dx, dy, dz), false), list = collisionCubes;
        for (AxisAlignedBB bb : collisionCubes)
            dx = bb.calculateXOffset(this.boundingBox, dx);
        this.boundingBox.offset(dx, 0.0D, 0.0D);
        for (AxisAlignedBB bb : list)
            dz = bb.calculateZOffset(this.boundingBox, dz);
        this.boundingBox.offset(0.0D, 0.0D, dz);
        for (AxisAlignedBB bb : list)
            dy = bb.calculateYOffset(this.boundingBox, dy);
        this.boundingBox.offset(0.0D, dy, 0.0D);
        setComponents(this.x + dx, this.y + dy, this.z + dz);
        checkChunks();
        checkGroundState(movX, movY, movZ, dx, dy, dz);
        updateFallState(this.onGround);
        return true;
    }

    public boolean onInteract(Player player, Item item) {
        if (item.getId() == 421 && item.hasCustomName()) {
            setNameTag(item.getCustomName());
            setNameTagVisible(true);
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            return true;
        }
        return false;
    }

    public float getMountedYOffset() {
        return getHeight() * 0.75F;
    }

    protected Item[] getRandomArmor() {
        Item[] slots = new Item[4];
        Item helmet = new Item(ItemID.DIAMOND_HELMET);
        helmet.addEnchantment(Enchantment.get(Enchantment.ID_PROTECTION_ALL).setLevel(4));
        helmet.addEnchantment(Enchantment.get(Enchantment.ID_DURABILITY).setLevel(3));
        Item chestplate = new Item(ItemID.DIAMOND_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.get(Enchantment.ID_PROTECTION_ALL).setLevel(4));
        chestplate.addEnchantment(Enchantment.get(Enchantment.ID_DURABILITY).setLevel(3));
        Item leggings = new Item(ItemID.DIAMOND_LEGGINGS);
        leggings.addEnchantment(Enchantment.get(Enchantment.ID_PROTECTION_ALL).setLevel(4));
        leggings.addEnchantment(Enchantment.get(Enchantment.ID_DURABILITY).setLevel(3));
        Item boots = new Item(ItemID.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.get(Enchantment.ID_PROTECTION_ALL).setLevel(4));
        boots.addEnchantment(Enchantment.get(Enchantment.ID_DURABILITY).setLevel(3));
        slots[0] = helmet;
        slots[1] = chestplate;
        slots[2] = leggings;
        slots[3] = boots;
        return slots;
    }

    public int nearbyDistanceMultiplier() {
        return 1;
    }

    public int getAirTicks() {
        return this.airTicks;
    }

    public void setAirTicks(int ticks) {
        this.airTicks = ticks;
    }

    public void addMovement(double x, double y, double z, double yaw, double pitch, double headYaw) {
        MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
        pk.eid = this.id;
        pk.x = (float)x;
        pk.y = (float)y;
        pk.z = (float)z;
        pk.yaw = (float)yaw;
        pk.headYaw = (float)headYaw;
        pk.pitch = (float)pitch;
        pk.onGround = this.onGround;
        for (Player p : this.hasSpawned.values())
            p.batchDataPacket((DataPacket)pk);
    }

    public void addMotion(double motionX, double motionY, double motionZ) {
        SetEntityMotionPacket pk = new SetEntityMotionPacket();
        pk.eid = this.id;
        pk.motionX = (float)motionX;
        pk.motionY = (float)motionY;
        pk.motionZ = (float)motionZ;
        for (Player p : this.hasSpawned.values())
            p.batchDataPacket((DataPacket)pk);
    }

    protected void checkGroundState(double movX, double movY, double movZ, double dx, double dy, double dz) {
        if (this.onGround && movX == 0.0D && movY == 0.0D && movZ == 0.0D && dx == 0.0D && dy == 0.0D && dz == 0.0D)
            return;
        this.isCollidedVertically = (movY != dy);
        this.isCollidedHorizontally = (movX != dx || movZ != dz);
        this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
        this.onGround = (movY != dy && movY < 0.0D);
    }

    public static void setProjectileMotion(Entity projectile, double pitch, double yawR, double pitchR, double speed) {
        double verticalMultiplier = Math.cos(pitchR);
        double x = verticalMultiplier * Math.sin(-yawR);
        double z = verticalMultiplier * Math.cos(yawR);
        double y = Math.sin(-Math.toRadians(pitch));
        double magnitude = Math.sqrt(x * x + y * y + z * z);
        if (magnitude > 0.0D) {
            x += x * (speed - magnitude) / magnitude;
            y += y * (speed - magnitude) / magnitude;
            z += z * (speed - magnitude) / magnitude;
        }
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        x += rand.nextGaussian() * 0.007499999832361937D * 6.0D;
        y += rand.nextGaussian() * 0.007499999832361937D * 6.0D;
        z += rand.nextGaussian() * 0.007499999832361937D * 6.0D;
        projectile.setMotion(new Vector3(x, y, z));
    }

    public void resetFallDistance() {
        this.highestPosition = this.y;
    }

    public boolean setMotion(Vector3 motion) {
        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        if (!this.justCreated)
            updateMovement();
        return true;
    }
}
