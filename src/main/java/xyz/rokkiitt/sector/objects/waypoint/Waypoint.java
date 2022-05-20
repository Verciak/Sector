package xyz.rokkiitt.sector.objects.waypoint;

import cn.nukkit.entity.data.*;
import cn.nukkit.utils.*;
import cn.nukkit.*;
import cn.nukkit.level.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.entity.*;
import cn.nukkit.math.*;
import java.nio.charset.*;
import java.util.UUID;

import cn.nukkit.event.entity.*;
import cn.nukkit.item.*;

public class Waypoint extends EntityHuman
{
    private static final Skin SKIN;
    private static final SerializedImage SKIN_DATA;
    public final String waypointname;
    public final WaypointData data;
    public final Player holder;

    public static CompoundTag getNbt(final Player p, final Location l, final String name) {
        final CompoundTag nbt = Entity.getDefaultNBT((Vector3)l);
        final Skin skin = Waypoint.SKIN;
        final CompoundTag skinTag = new CompoundTag().putByteArray("Data", skin.getSkinData().data).putInt("SkinImageWidth", skin.getSkinData().width).putInt("SkinImageHeight", skin.getSkinData().height).putString("ModelId", skin.getSkinId()).putString("CapeId", skin.getCapeId()).putByteArray("CapeData", skin.getCapeData().data).putInt("CapeImageWidth", skin.getCapeData().width).putInt("CapeImageHeight", skin.getCapeData().height).putByteArray("SkinResourcePatch", skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8)).putByteArray("GeometryData", skin.getGeometryData().getBytes(StandardCharsets.UTF_8)).putByteArray("AnimationData", skin.getAnimationData().getBytes(StandardCharsets.UTF_8)).putBoolean("PremiumSkin", skin.isPremium()).putBoolean("PersonaSkin", skin.isPersona()).putBoolean("CapeOnClassicSkin", skin.isCapeOnClassic());
        nbt.putCompound("Skin", skinTag);
        nbt.putString("NameTag", p.getName() + " | " + name);
        return nbt;
    }

    public boolean attack(final EntityDamageEvent e) {
        e.setCancelled(true);
        return false;
    }

    public void spawnTo(final Player player) {
        if (this.holder == player) {
            super.spawnTo(player);
        }
    }

    public Waypoint(final Player holder, final WaypointData data, final String wpname, final Location l, final Player p) {
        super(p.getChunk(), getNbt(p, l, wpname));
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
        this.waypointname = wpname;
        this.data = data;
        this.holder = holder;
    }

    public float getWidth() {
        return 0.1f;
    }

    public float getLength() {
        return 0.1f;
    }

    public float getHeight() {
        return 0.1f;
    }

    public Item[] getDrops() {
        return new Item[0];
    }

    public void collidingWith(final Entity ent) {
    }

    protected float getGravity() {
        return 0.0f;
    }

    public void setCanClimb(final boolean value) {
        this.setDataFlag(0, 19, false);
    }

    public void setCanClimbWalls(final boolean value) {
        this.setDataFlag(0, 18, false);
    }

    public void setOnFire(final int seconds) {
        super.setOnFire(0);
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean canTriggerWalking() {
        return true;
    }

    public boolean isInsideOfWater() {
        return false;
    }

    public boolean isInsideOfSolid() {
        return false;
    }

    public boolean isInsideOfFire() {
        return false;
    }

    public boolean isOnLadder() {
        return false;
    }

    public boolean canBeMovedByCurrents() {
        return true;
    }

    public boolean doesTriggerPressurePlate() {
        return false;
    }

    public boolean canPassThrough() {
        return false;
    }

    static {
        SKIN = new Skin();
        SKIN_DATA = SerializedImage.fromLegacy(new byte[8192]);
        Waypoint.SKIN.setSkinData(Waypoint.SKIN_DATA);
        byte[] data = Binary.appendBytes(Waypoint.SKIN.getSkinData().data, Waypoint.SKIN.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8));
        String skinId = UUID.nameUUIDFromBytes(data) + "." + "FloatingText";
        Waypoint.SKIN.setSkinId(skinId);
    }
}

