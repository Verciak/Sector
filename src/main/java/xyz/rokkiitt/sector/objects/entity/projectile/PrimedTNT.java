package xyz.rokkiitt.sector.objects.entity.projectile;

import cn.nukkit.entity.item.*;
import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.entity.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.*;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.database.threads.QueryThread;
import cn.nukkit.level.*;

import java.util.*;

import xyz.rokkiitt.sector.objects.level.Explosion;

public class PrimedTNT extends EntityPrimedTNT
{
    public PrimedTNT(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    public PrimedTNT(final FullChunk chunk, final CompoundTag nbt, final Entity source) {
        super(chunk, nbt, source);
    }
    
    public void explode() {
        final EntityExplosionPrimeEvent event = new EntityExplosionPrimeEvent((Entity)this, 4.0);
        this.server.getPluginManager().callEvent((Event)event);
        if (!event.isCancelled()) {
            final Explosion explosion = new Explosion((Position)this, event.getForce(), (Entity)this);
            if (event.isBlockBreaking()) {
                final QueryThread query = Main.getQuery();
                final Explosion explosion2 = explosion;
                Objects.requireNonNull(explosion2);
                query.addQueue(explosion2::explodeA);
            }
        }
    }
}
