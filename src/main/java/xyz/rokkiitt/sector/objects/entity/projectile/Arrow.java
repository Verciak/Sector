package xyz.rokkiitt.sector.objects.entity.projectile;

import cn.nukkit.entity.projectile.*;
import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.entity.*;

public class Arrow extends EntityArrow
{
    public Arrow(final FullChunk chunk, final CompoundTag nbt) {
        this(chunk, nbt, null);
    }
    
    public Arrow(final FullChunk chunk, final CompoundTag nbt, final Entity shootingEntity) {
        this(chunk, nbt, shootingEntity, false);
    }
    
    public Arrow(final FullChunk chunk, final CompoundTag nbt, final Entity shootingEntity, final boolean critical) {
        super(chunk, nbt, shootingEntity);
    }
    
    public float getGravity() {
        return 0.02f;
    }
    
    public float getDrag() {
        return 0.006f;
    }
}
