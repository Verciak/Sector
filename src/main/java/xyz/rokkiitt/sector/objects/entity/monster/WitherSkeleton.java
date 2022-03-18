package xyz.rokkiitt.sector.objects.entity.monster;

import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import xyz.rokkiitt.sector.objects.entity.Monster;
import xyz.rokkiitt.sector.objects.entity.WalkingMonster;
import xyz.rokkiitt.sector.objects.entity.utils.WalkerRouteFinder;
import xyz.rokkiitt.sector.utils.RandomUtil;
import cn.nukkit.entity.*;
import cn.nukkit.math.*;
import cn.nukkit.*;
import cn.nukkit.event.entity.*;
import cn.nukkit.potion.*;
import cn.nukkit.item.*;
import cn.nukkit.network.protocol.*;

import java.util.*;

public class WitherSkeleton extends WalkingMonster implements EntitySmite
{
    public static final int NETWORK_ID = 48;
    
    public int getNetworkId() {
        return 48;
    }
    
    public WitherSkeleton(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
        this.route = new WalkerRouteFinder(this);
    }
    
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Wither Skeleton";
    }
    
    protected void initEntity() {
        super.initEntity();
        this.fireProof = true;
        this.setMaxHealth(40);
        this.setDamage(new float[] { 3.0f, 6.0f, 8.0f, 10.0f });
    }
    
    public float getWidth() {
        return 0.7f;
    }
    
    public float getHeight() {
        return 2.4f;
    }
    
    public void attackEntity(final Entity player) {
        if (this.attackDelay > 18 && player.distanceSquared((Vector3)this) <= 3.0) {
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
            if (player.attack((EntityDamageEvent)new EntityDamageByEntityEvent((Entity)this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Map)damage))) {
                player.addEffect(Effect.getEffect(20).setDuration(300));
            }
        }
    }
    
    public void spawnTo(final Player player) {
        super.spawnTo(player);
        final MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = (Item)new ItemSwordStone();
        pk.hotbarSlot = 0;
        player.dataPacket((DataPacket)pk);
    }
    
    public Item[] getDrops() {
        final List<Item> drops = new ArrayList<Item>();
        if (RandomUtil.getChance(1.0)) {
            drops.add(Item.get(397, Integer.valueOf(1), 1));
        }
        return drops.toArray(new Item[0]);
    }
    
    public int getKillExperience() {
        return 5;
    }
}
