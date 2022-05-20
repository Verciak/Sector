package xyz.rokkiitt.sector.objects.level;

import cn.nukkit.level.*;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.entity.projectile.PrimedTNT;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.utils.ThreadRandomUtil;
import cn.nukkit.entity.*;
import cn.nukkit.event.*;
import cn.nukkit.scheduler.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.entity.item.*;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.inventory.*;
import cn.nukkit.item.*;
import it.unimi.dsi.fastutil.longs.*;
import cn.nukkit.math.*;
import cn.nukkit.utils.*;
import cn.nukkit.event.block.*;
import cn.nukkit.level.particle.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.level.format.generic.*;
import java.util.*;

public class Explosion
{
    private final Level level;
    private final Position source;
    private final double size;
    private List<Block> affectedBlocks;
    private final Object what;
    private boolean doesDamage;
    
    public Explosion(final Position center, final double size, final Entity what) {
        this.affectedBlocks = new ArrayList<Block>();
        this.doesDamage = true;
        this.level = center.getLevel();
        this.source = center;
        this.size = Math.max(size, 0.0);
        this.what = what;
    }
    
    public void sphere(final int xx, final int yy, final int zz, final int radius, final int height, final boolean hollow, final boolean sphere, final int plusY) {
        final int cx = xx;
        final int cy = yy;
        final int cz = zz;
        final ThreadRandomUtil random = new ThreadRandomUtil();
        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                for (int y = sphere ? (cy - radius) : cy; y < (sphere ? (cy + radius) : (cy + height)); ++y) {
                    final int dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < radius * radius && (!hollow || dist >= (radius - 1) * (radius - 1))) {
                        final Block block = this.getBlock(this.level, x, y, z, false);
                        if (block.getId() != 0 && block.getId() != 7 && !this.affectedBlocks.contains(block)) {
                            if (block.getId() == 49) {
                                final Guild g = GuildManager.getGuild(block.getLocation());
                                double chance = 35.0;
                                if (random.getChance(chance)) {
                                    this.affectedBlocks.add(block);
                                }
                            }
                            else if (block.getId() == 116) {
                                if (random.getChance(15.0)) {
                                    this.affectedBlocks.add(block);
                                }
                            }
                            else if (block.getId() == 130) {
                                if (random.getChance(15.0)) {
                                    this.affectedBlocks.add(block);
                                }
                            }
                            else if (block.getId() == 145) {
                                if (random.getChance(15.0)) {
                                    this.affectedBlocks.add(block);
                                }
                            }
                            else if (block.getId() == 11 || block.getId() == 10 || block.getId() == 9 || block.getId() == 8) {
                                this.affectedBlocks.add(block);
                            }
                            else if (block.getId() == 138) {
                                if (random.getChance(60.0)) {
                                    this.affectedBlocks.add(block);
                                }
                            }
                            else {
                                this.affectedBlocks.add(block);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void explodeA() {
        if (this.what instanceof EntityExplosive) {
            final Entity entity = (Entity)this.what;
            final int block = this.level.getBlockIdAt(entity.getFloorX(), entity.getFloorY(), entity.getFloorZ());
            if (block == 8 || block == 9) {
                this.doesDamage = false;
                return;
            }
        }
        if (this.size < 0.1) {
            return;
        }
        this.sphere(this.source.getFloorX(), this.source.getFloorY(), this.source.getFloorZ(), 4, 3, false, true, 1);
        this.explodeB();
    }
    
    public void explodeB() {
        double yield = 1.0 / this.size * 100.0;
        if (this.what instanceof Entity) {
            final EntityExplodeEvent ev = new EntityExplodeEvent((Entity)this.what, this.source, (List)this.affectedBlocks, yield);
            this.level.getServer().getPluginManager().callEvent((Event)ev);
            if (ev.isCancelled()) {
                return;
            }
            yield = ev.getYield();
            this.affectedBlocks = (List<Block>)ev.getBlockList();
        }
        final double explosionSize = this.size * 2.0;
        final double minX = NukkitMath.floorDouble(this.source.x - explosionSize - 1.0);
        final double maxX = NukkitMath.ceilDouble(this.source.x + explosionSize + 1.0);
        final double minY = NukkitMath.floorDouble(this.source.y - explosionSize - 1.0);
        final double maxY = NukkitMath.ceilDouble(this.source.y + explosionSize + 1.0);
        final double minZ = NukkitMath.floorDouble(this.source.z - explosionSize - 1.0);
        final double maxZ = NukkitMath.ceilDouble(this.source.z + explosionSize + 1.0);
        final AxisAlignedBB AxisAlignedBB = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
        Server.getInstance().getScheduler().scheduleDelayedTask((Task)new Task() {
            public void onRun(final int i) {
                final Entity[] nearbyEntities;
                final Entity[] list = nearbyEntities = Explosion.this.level.getNearbyEntities((AxisAlignedBB)AxisAlignedBB, (Explosion.this.what instanceof Entity) ? ((Entity)Explosion.this.what) : null);
                for (final Entity entity : nearbyEntities) {
                    if (entity != null) {
                        final double distance = entity.distance((Vector3)Explosion.this.source) / explosionSize;
                        if (distance <= 1.0) {
                            final Vector3 defMotion = entity.subtract((Vector3)Explosion.this.source).normalize();
                            final double exposure = 1.0;
                            final double impact = (1.0 - distance) * exposure;
                            final int damage = Explosion.this.doesDamage ? ((int)((impact * impact + impact) / 2.0 * 6.0 * explosionSize + 1.0)) : 0;
                            if (Explosion.this.what instanceof Entity) {
                                entity.attack((EntityDamageEvent)new EntityDamageByEntityEvent((Entity)Explosion.this.what, entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, (float)damage));
                            }
                            else if (Explosion.this.what instanceof Block) {
                                entity.attack((EntityDamageEvent)new EntityDamageByBlockEvent((Block)Explosion.this.what, entity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, (float)damage));
                            }
                            else {
                                entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, (float)damage));
                            }
                            if (entity instanceof PrimedTNT) {
                                final Vector3 motion = entity.getMotion().add(defMotion.multiply(impact)).add(0.0, 0.1);
                                if (motion.getY() > 1.0) {
                                    motion.y = 1.0;
                                }
                                entity.setMotion(motion);
                            }
                            else if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb) && entity instanceof Player && !((Player)entity).hasPermission(Perms.CMD_ALERTAC.getPermission())) {
                                entity.setMotion(defMotion.multiply(impact));
                            }
                        }
                    }
                }
            }
        }, 1, false);
        final ItemBlock air = new ItemBlock(Block.get(0));
        for (final Block block : this.affectedBlocks) {
            if (block.getId() == 46) {
                Server.getInstance().getScheduler().scheduleDelayedTask((Task)new Task() {
                    public void onRun(final int i) {
                        ((BlockTNT)block).prime(new NukkitRandom().nextRange(10, 30), (Explosion.this.what instanceof Entity) ? ((Entity)Explosion.this.what) : null);
                    }
                }, 1, false);
            }
            else {
                final BlockEntity container = block.getLevel().getBlockEntity((Vector3)block);
                if (container instanceof InventoryHolder) {
                    if (container instanceof BlockEntityShulkerBox) {
                        this.level.dropItem((Vector3)block.add(0.5, 0.5, 0.5), block.toItem());
                    }
                    else {
                        for (final Item drop : ((InventoryHolder)container).getInventory().getContents().values()) {
                            this.level.dropItem((Vector3)block.add(0.5, 0.5, 0.5), drop);
                        }
                    }
                    ((InventoryHolder)container).getInventory().clearAll();
                }
                else {
                    if (Math.random() * 100.0 >= yield) {
                        continue;
                    }
                    for (final Item drop2 : block.getDrops((Item)air)) {
                        this.level.dropItem((Vector3)block.add(0.5, 0.5, 0.5), drop2);
                    }
                }
            }
        }
        this.setBlock(this.affectedBlocks, air.getBlock());
        final LongArraySet updateBlocks = new LongArraySet();
        final List<Block> updateBlock = new ArrayList<Block>();
        for (final Block oldBlock : this.affectedBlocks) {
            final Vector3 pos = new Vector3(oldBlock.x, oldBlock.y, oldBlock.z);
            for (final BlockFace side : BlockFace.values()) {
                final Vector3 ps = pos.getSide(side);
                final Block sideBlock = this.getBlock(this.level, ps.getFloorX(), ps.getFloorY(), ps.getFloorZ(), false);
                final long index = Hash.hashBlock((int)sideBlock.x, (int)sideBlock.y, (int)sideBlock.z);
                if (!updateBlocks.contains(index)) {
                    final BlockUpdateEvent ev2 = new BlockUpdateEvent(sideBlock);
                    this.level.getServer().getPluginManager().callEvent((Event)ev2);
                    if (!ev2.isCancelled()) {
                        updateBlock.add(ev2.getBlock());
                    }
                    updateBlocks.add(index);
                }
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask((Task)new Task() {
            public void onRun(final int i) {
                for (final Block b : updateBlock) {
                    b.onUpdate(1);
                }
            }
        }, 1, false);
        this.level.addParticle((Particle)new HugeExplodeSeedParticle((Vector3)this.source));
        this.level.addLevelSoundEvent((Vector3)this.source, 48);
    }
    
    public void setBlock(final List<Block> oldBlocks, final Block block) {
        block.setLevel(this.level);
        final boolean shouldLoad = false;
        final Map<Long, BaseFullChunk> chunks = new HashMap<Long, BaseFullChunk>();
        for (final Block oldBlock : oldBlocks) {
            if (oldBlock.getFloorY() >= 0) {
                if (oldBlock.getFloorY() >= 256) {
                    continue;
                }
                final int oldState = oldBlock.getId();
                if (block.getId() == oldState) {
                    continue;
                }
                BaseFullChunk chunk2;
                if (shouldLoad) {
                    chunk2 = this.level.getChunk(oldBlock.getChunkX(), oldBlock.getChunkZ(), true);
                }
                else {
                    chunk2 = this.level.getChunkIfLoaded(oldBlock.getChunkX(), oldBlock.getChunkZ());
                }
                if (chunk2 == null) {
                    continue;
                }
                if (!chunks.containsKey(chunk2.getIndex())) {
                    chunks.put(chunk2.getIndex(), chunk2);
                }
                chunk2.setBlockId(oldBlock.getFloorX() & 0xF, oldBlock.getFloorY(), oldBlock.getFloorZ() & 0xF, block.getId());
            }
        }
        final List<Block> blocks = new ArrayList<Block>();
        this.affectedBlocks.forEach(c -> blocks.add(Block.get(0, c.getLevel(), c.getFloorX(), c.getFloorY(), c.getFloorZ())));
        chunks.values().forEach(chunk -> this.level.sendBlocks((Player[])block.getLevel().getChunkPlayers(chunk.getX(), chunk.getZ()).values().toArray(new Player[0]), (Vector3[])blocks.toArray((Vector3[])new Block[0])));
    }
    
    public Block getBlock(final Level level, final int x, final int y, final int z, final boolean load) {
        int fullState;
        if (y >= 0 && y < 256) {
            final int cx = x >> 4;
            final int cz = z >> 4;
            BaseFullChunk chunk;
            if (load) {
                chunk = level.getChunk(cx, cz);
            }
            else {
                chunk = level.getChunkIfLoaded(cx, cz);
            }
            if (chunk != null) {
                fullState = chunk.getFullBlock(x & 0xF, y, z & 0xF);
            }
            else {
                fullState = 0;
            }
        }
        else {
            fullState = 0;
        }
        final Block block = Block.fullList[fullState & 0xFFF].clone();
        block.x = x;
        block.y = y;
        block.z = z;
        block.level = level;
        return block;
    }
}
