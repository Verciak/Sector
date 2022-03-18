package xyz.rokkiitt.sector.objects.entity.projectile;

import cn.nukkit.entity.projectile.*;
import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.entity.*;
import cn.nukkit.math.*;

public class EnderPearl extends EntityEnderPearl
{
    public EnderPearl(final FullChunk chunk, final CompoundTag nbt, final Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
        this.setScale(0.5f);
        this.setPosition((Vector3)this.getPosition().add(0.0, 0.3, 0.0));
    }
    
    public float getGravity() {
        return 0.02f;
    }
    
    public float getDrag() {
        return 0.006f;
    }
}
