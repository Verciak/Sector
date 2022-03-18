package xyz.rokkiitt.sector.objects.entity.monster;

import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.objects.entity.Monster;
import xyz.rokkiitt.sector.objects.entity.WalkingMonster;
import xyz.rokkiitt.sector.objects.entity.utils.Utils;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import cn.nukkit.event.entity.*;
import cn.nukkit.item.*;

import java.util.*;
import cn.nukkit.item.enchantment.*;
import cn.nukkit.math.*;
import cn.nukkit.level.*;
import cn.nukkit.entity.*;

public class Enderman extends WalkingMonster
{
    public static final int NETWORK_ID = 38;
    private int angry;
    
    public Enderman(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
        this.angry = 0;
    }
    
    public int getNetworkId() {
        return 38;
    }
    
    public float getWidth() {
        return 0.6f;
    }
    
    public float getHeight() {
        return 2.9f;
    }
    
    @Override
    public double getSpeed() {
        return this.isAngry() ? 1.4 : 1.21;
    }
    
    @Override
    protected void initEntity() {
        this.setMaxHealth(40);
        super.initEntity();
        this.setDamage(new float[] { 0.0f, 4.0f, 7.0f, 10.0f });
    }
    
    @Override
    public void attackEntity(final Entity player) {
        if (this.attackDelay > 23 && this.distanceSquared((Vector3)player) < 1.0) {
            this.attackDelay = 0;
            final HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<EntityDamageEvent.DamageModifier, Float>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
            if (player instanceof Player) {
                final HashMap<Integer, Float> armorValues = new Monster.ArmorPoints();
                float points = 0.0f;
                for (final Item i : ((Player)player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0.0f);
                }
                damage.put(EntityDamageEvent.DamageModifier.ARMOR, (float)(damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0.0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1.0f) * points * 0.04)));
            }
            player.attack((EntityDamageEvent)new EntityDamageByEntityEvent((Entity)this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Map)damage));
        }
    }
    
    @Override
    public boolean attack(final EntityDamageEvent ev) {
        if (!ev.isCancelled()) {
            if (ev.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && !this.isAngry()) {
                this.setAngry(2400);
            }
            if (ev.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                if (!this.isAngry()) {
                    this.setAngry(2400);
                }
                ev.setCancelled(true);
                this.tp();
            }
            else if (Utils.rand(1, 10) == 1) {
                this.tp();
            }
        }
        super.attack(ev);
        return true;
    }
    
    public Item[] getDrops() {
        final List<Item> drops = new ArrayList<Item>();
        drops.add(Item.get(368, Integer.valueOf(0), 1));
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)this.lastDamageCause;
            if (e.getDamager() instanceof Player) {
                final Player d = (Player)e.getDamager();
                final Item hand = d.getInventory().getItemInHand();
                if (hand != null) {
                    if (hand.getId() == 276 || hand.getId() == 283 || hand.getId() == 283 || hand.getId() == 267 || hand.getId() == 743 || hand.getId() == 272 || hand.getId() == 268) {
                        final Enchantment ench = hand.getEnchantment(14);
                        if (ench != null && ench.getLevel() > 0) {
                            final User u = UserManager.getUser(d.getName());
                            if (u != null) {
                                int x = Utils.rand(0, ench.getLevel());
                                drops.add(Item.get(368, Integer.valueOf(0), x));
                            }
                        }
                        else {
                            drops.add(Item.get(368, Integer.valueOf(0), 1));
                        }
                    }
                    else {
                        drops.add(Item.get(368, Integer.valueOf(0), 1));
                    }
                }
                else {
                    drops.add(Item.get(368, Integer.valueOf(0), 1));
                }
            }
        }
        return drops.toArray(new Item[0]);
    }
    
    @Override
    public int getKillExperience() {
        return 5;
    }
    
    @Override
    public boolean entityBaseTick(final int tickDiff) {
        if (this.getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        final int b = this.level.getBlockIdAt(NukkitMath.floorDouble(this.x), (int)this.y, NukkitMath.floorDouble(this.z));
        if (b == 8 || b == 9) {
            this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.DROWNING, 2.0f));
            if (this.isAngry()) {
                this.setAngry(0);
            }
            this.tp();
        }
        else if (Utils.rand(0, 500) == 20) {
            this.tp();
        }
        if (this.age % 20 == 0 && this.level.isRaining() && this.level.canBlockSeeSky((Vector3)this)) {
            this.attack(new EntityDamageEvent((Entity)this, EntityDamageEvent.DamageCause.DROWNING, 2.0f));
            if (this.isAngry()) {
                this.setAngry(0);
            }
            this.tp();
        }
        if (this.angry > 0) {
            --this.angry;
        }
        return super.entityBaseTick(tickDiff);
    }
    
    private void tp() {
            this.level.addSound((Vector3)this, Sound.MOB_ENDERMEN_PORTAL);
            this.move(Utils.rand(-15, 15), 0.0, Utils.rand(-15, 15));
            this.level.addSound((Vector3)this, Sound.MOB_ENDERMEN_PORTAL);
    }
    
    @Override
    public boolean canDespawn() {
        return this.getLevel().getDimension() != 2 && super.canDespawn();
    }
    
    public void makeVibrating(final boolean bool) {
        this.setDataFlag(0, 25, bool);
    }
    
    public boolean isAngry() {
        return this.angry > 0;
    }
    
    public void setAngry(final int val) {
        this.angry = val;
        this.makeVibrating(val > 0);
    }
    
    @Override
    public boolean targetOption(final EntityCreature creature, final double distance) {
        if (!this.isAngry()) {
            return false;
        }
        if (creature instanceof Player) {
            final Player player = (Player)creature;
            return !player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure()) && distance <= 144.0;
        }
        return creature.isAlive() && !creature.closed && distance <= 144.0;
    }
    
    public void stareToAngry() {
        if (!this.isAngry()) {
            this.setAngry(2400);
        }
    }
}
