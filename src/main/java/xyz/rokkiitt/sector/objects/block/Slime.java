package xyz.rokkiitt.sector.objects.block;

import cn.nukkit.block.*;
import cn.nukkit.entity.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.math.*;

public class Slime extends BlockSlime
{
    public boolean hasEntityCollision() {
        return true;
    }
    
    public void onEntityCollide(final Entity entity) {
        if (!(entity instanceof Player)) {
            entity.resetFallDistance();
            return;
        }
        if (Cooldown.getInstance().has((Player)entity, "slimeblock")) {
            entity.resetFallDistance();
            return;
        }
        double force = entity.fallDistance * 3.0f / 10.0f / 2.0f;
        if (force > 3.5) {
            force = 3.5;
        }
        force = Util.round(force, 2);
        if (force < 0.5) {
            entity.resetFallDistance();
            return;
        }
        Cooldown.getInstance().add((Player)entity, "slimeblock", 0.3f);
        entity.setMotion(new Vector3(0.0, force, 0.0));
        entity.resetFallDistance();
    }
}
