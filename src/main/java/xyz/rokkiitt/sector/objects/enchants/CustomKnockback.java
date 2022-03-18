package xyz.rokkiitt.sector.objects.enchants;

import cn.nukkit.item.enchantment.*;
import cn.nukkit.entity.*;
import cn.nukkit.math.*;

public class CustomKnockback extends EnchantmentKnockback
{
    public void doPostAttack(final Entity attacker, final Entity victim) {
        Vector3 diff = (Vector3)victim.getPosition().subtract((Vector3)attacker.getPosition());
        if (attacker.getPosition().floor().equals((Object)victim.getPosition().floor())) {
            diff = attacker.getDirectionVector();
        }
        this.knockBack(victim, diff.getX(), diff.getZ(), this.getLevel() * 0.7);
    }
    
    private void knockBack(final Entity victim, final double x, final double z, final double base) {
        double f = Math.sqrt(x * x + z * z);
        if (f <= 0.0) {
            return;
        }
        f = 1.0 / f;
        final Vector3 motion2;
        final Vector3 motion = motion2 = victim.getMotion();
        motion2.x /= 2.0;
        final Vector3 vector3 = motion;
        vector3.z /= 2.0;
        final Vector3 vector4 = motion;
        vector4.x += x * f * base;
        final Vector3 vector5 = motion;
        vector5.z += z * f * base;
        if (victim.isOnGround()) {
            final Vector3 vector6 = motion;
            vector6.y /= 2.0;
            final Vector3 vector7 = motion;
            vector7.y += base;
            if (motion.y > 0.4) {
                motion.y = 0.4;
            }
        }
        victim.setMotion(motion);
    }
}
