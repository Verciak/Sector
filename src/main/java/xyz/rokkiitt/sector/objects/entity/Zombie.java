package xyz.rokkiitt.sector.objects.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.mob.EntityDrowned;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemShovelIron;
import cn.nukkit.item.ItemSwordIron;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.potion.Effect;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.objects.boss.Boss;
import xyz.rokkiitt.sector.objects.boss.BossManager;
import xyz.rokkiitt.sector.objects.entity.utils.Utils;
import xyz.rokkiitt.sector.objects.entity.utils.WalkerRouteFinder;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Zombie extends WalkingMonster implements EntityAgeable, EntitySmite {

    public static final int NETWORK_ID = 32;

    public Item tool;

    public Zombie(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.route = new WalkerRouteFinder(this);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.95f;
    }

    @Override
    public double getSpeed() {
        return 1.1;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setDamage(new float[]{0, 4, 6, 8, 10, 12, 14, 16, 18, 20});
        this.setMaxHealth(1000);

        this.armor = getRandomArmor();
        this.setRandomTool();
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && player.distanceSquared(this) <= 1) {
            this.attackDelay = 0;
            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, this.getDamage());
            if (player instanceof Player) {
                HashMap<Integer, Float> armorValues = new ArmorPoints();

                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += armorValues.getOrDefault(i.getId(), 0f);
                }

                damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.getId();
            pk.event = 4;
            this.level.addChunkPacket(this.getChunkX() >> 4,this.getChunkZ() >> 4, pk);
        }
    }



    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }
        this.setNameTag(Util.fixColor("&r&c&lBOSS\n&r&7HP: &9" + this.getHealth() + " &c\u2665"));
        this.setNameTagVisible();
        this.setScoreTag(Util.fixColor("&r&c&lBOSS\n&r&7HP: &9" + this.getHealth() + " &c\u2665"));

        boolean hasUpdate = super.entityBaseTick(tickDiff);
        this.setOnFire(0);

        return hasUpdate;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(0, 2); i++) {
                drops.add(Item.get(Item.ROTTEN_FLESH, 0, 1));
            }

            if (this.tool != null) {
                if (tool instanceof ItemSwordIron && Utils.rand(1, 3) == 1) {
                    drops.add(tool);
                }

                if (tool instanceof ItemShovelIron && Utils.rand(1, 3) != 1) {
                    drops.add(tool);
                }
            }

            if (Utils.rand(1, 3) == 1) {
                switch (Utils.rand(1, 3)) {
                    case 1:
                        drops.add(Item.get(Item.IRON_INGOT, 0, Utils.rand(0, 1)));
                        break;
                    case 2:
                        drops.add(Item.get(Item.CARROT, 0, Utils.rand(0, 1)));
                        break;
                    case 3:
                        drops.add(Item.get(Item.POTATO, 0, Utils.rand(0, 1)));
                        break;
                }
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 12 : 5;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

            MobArmorEquipmentPacket pk = new MobArmorEquipmentPacket();
            pk.eid = this.getId();
            pk.slots = this.armor;
            player.dataPacket(pk);

        if (this.tool != null) {
            MobEquipmentPacket pk2 = new MobEquipmentPacket();
            pk2.eid = this.getId();
            pk2.hotbarSlot = 0;
            pk2.item = this.tool;
            player.dataPacket(pk2);
        }
    }

    private void setRandomTool() {
        Item i = Item.get(Item.DIAMOND_SWORD, 0, 1);
        i.addEnchantment(Enchantment.get(Enchantment.ID_DAMAGE_ALL).setLevel(5));
        i.addEnchantment(Enchantment.get(Enchantment.ID_FIRE_ASPECT).setLevel(2));
        i.addEnchantment(Enchantment.get(Enchantment.ID_DURABILITY).setLevel(3));

        this.tool = i;
        this.setDamage(new float[]{0, 4, 6, 8, 10, 12, 14, 16, 18, 20});
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);
        if(RandomUtil.getChance(5.0D)){
            ev.getEntity().setHealth(ev.getEntity().getHealth() + 20);
        }
        for (final Player p : Util.getPlayersInRadius(ev.getEntity(), ev.getEntity().getLocation(), 10)) {
            Boss boss = BossManager.users.iterator().next();
            if(!boss.isPerch()) {
                if (RandomUtil.getChance(0.5D)) {
                    boss.setPerch(true);
                    p.sendTitle(Util.fixColor("&c&lBOSS &8- &6BEDZIESZ LATAC"), Util.fixColor("&7Za 5 sekund uzyje swojej super mocy!"), 30, 60, 30);
                    Server.getInstance().getScheduler().scheduleDelayedTask(Main.getPlugin(), () -> {
                        final double x = p.getFloorX() - ev.getEntity().getLocation().getFloorX();
                        final double z = p.getFloorZ() - ev.getEntity().getLocation().getFloorZ();
                        p.knockBack(p, 2.5, x, z, 2.1);
                        boss.setPerch(false);
                    }, 100);
                }
            }
            if(!boss.isPerch()) {
                if (RandomUtil.getChance(0.5D)) {
                    boss.setPerch(true);
                    p.sendTitle(Util.fixColor("&c&lBOSS &8- &6BEDZIESZ OTRUTY"), Util.fixColor("&7Za 5 sekund uzyje swojej super mocy!"), 30, 60, 30);
                    Server.getInstance().getScheduler().scheduleDelayedTask(Main.getPlugin(), () -> {
                        p.addEffect(Effect.getEffect(Effect.POISON).setAmplifier(0).setDuration(200));
                        boss.setPerch(false);
                    }, 100);
                }
            }
            if(!boss.isPerch()) {
                if (RandomUtil.getChance(0.5D)) {
                    boss.setPerch(true);
                    p.sendTitle(Util.fixColor("&c&lBOSS &8- &6STRACISZ WIDOCZNOSC"), Util.fixColor("&7Za 5 sekund uzyje swojej super mocy!"), 30, 60, 30);
                    Server.getInstance().getScheduler().scheduleDelayedTask(Main.getPlugin(), () -> {
                        p.addEffect(Effect.getEffect(Effect.BLINDNESS).setAmplifier(0).setDuration(200));
                        boss.setPerch(false);
                    }, 100);
                }
            }
        }

        if (!ev.isCancelled() && ev.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            CreatureSpawnEvent cse = new CreatureSpawnEvent(EntityDrowned.NETWORK_ID, this, new CompoundTag(), CreatureSpawnEvent.SpawnReason.DROWNED);
            level.getServer().getPluginManager().callEvent(cse);

            if (cse.isCancelled()) {
                this.close();
                return true;
            }

            Entity ent = Entity.createEntity("Drowned", this);
            if (ent != null) {
                this.close();
                ent.spawnToAll();
            } else {
                this.close();
            }
        }

        return true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        if (tool != null) {
            this.namedTag.put("Item", NBTIO.putItemHelper(tool));
        }
    }
}

