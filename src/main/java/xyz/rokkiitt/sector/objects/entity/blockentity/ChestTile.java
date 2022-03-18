package xyz.rokkiitt.sector.objects.entity.blockentity;

import cn.nukkit.blockentity.*;
import cn.nukkit.level.format.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.*;
import cn.nukkit.math.*;

public class ChestTile extends BlockEntityChest
{
    public ChestTile(final FullChunk chunk, final CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    public String getOwner() {
        return this.namedTag.getString("owner");
    }
    
    public boolean isLocked() {
        return !this.namedTag.getString("owner").isEmpty();
    }
    
    public void setLocked(final String owner) {
        this.namedTag.putString("owner", owner);
        final ChestTile pair = (ChestTile)this.getPair();
        if (pair != null) {
            pair.namedTag.putString("owner", owner);
        }
    }
    
    public void setLocked(final Player player) {
        this.setLocked(player.getName());
    }
    
    public void unlock() {
        this.namedTag.remove("signX");
        this.namedTag.remove("signY");
        this.namedTag.remove("signZ");
        this.namedTag.remove("owner");
        final ChestTile pair = (ChestTile)this.getPair();
        if (pair != null) {
            pair.namedTag.remove("signX");
            pair.namedTag.remove("signY");
            pair.namedTag.remove("signZ");
            pair.namedTag.remove("owner");
        }
    }
    
    public void setSign(final Vector3 position) {
        this.namedTag.putInt("signX", (int)position.x);
        this.namedTag.putInt("signY", (int)position.y);
        this.namedTag.putInt("signZ", (int)position.z);
    }
    
    public Vector3 getSign() {
        return new Vector3((double)this.namedTag.getInt("signX"), (double)this.namedTag.getInt("signY"), (double)this.namedTag.getInt("signZ"));
    }
    
    public boolean hasSign() {
        return this.namedTag.contains("signX") && this.namedTag.contains("signY") && this.namedTag.contains("signZ");
    }
    
    public void createPair(final BlockEntityChest chest) {
        this.namedTag.putInt("pairx", (int)chest.x);
        this.namedTag.putInt("pairz", (int)chest.z);
        chest.namedTag.putInt("pairx", (int)this.x);
        chest.namedTag.putInt("pairz", (int)this.z);
        chest.namedTag.putString("owner", this.getOwner());
        this.namedTag.putString("owner", ((ChestTile)chest).getOwner());
    }
    
    public boolean unpair() {
        final ChestTile pair = (ChestTile)this.getPair();
        if (pair != null && this.hasSign()) {
            pair.namedTag.remove("owner");
        }
        return super.unpair();
    }
    
    public CompoundTag getSpawnCompound() {
        CompoundTag c;
        if (this.isPaired()) {
            c = new CompoundTag().putString("id", "Chest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putInt("pairx", this.namedTag.getInt("pairx")).putInt("pairz", this.namedTag.getInt("pairz"));
        }
        else {
            c = new CompoundTag().putString("id", "Chest").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        }
        c.putString("owner", this.namedTag.getString("owner"));
        if (this.hasSign()) {
            c.putInt("signX", this.namedTag.getInt("signX"));
            c.putInt("signY", this.namedTag.getInt("signY"));
            c.putInt("signZ", this.namedTag.getInt("signZ"));
        }
        if (this.hasName()) {
            c.put("CustomName", this.namedTag.get("CustomName"));
        }
        return c;
    }
}
