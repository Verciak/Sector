package xyz.rokkiitt.sector.objects.block;

import cn.nukkit.entity.*;
import xyz.rokkiitt.sector.Main;
import cn.nukkit.block.*;
import cn.nukkit.nbt.tag.*;

public class TNT extends BlockTNT
{
    public void prime(final int fuse, final Entity source) {
        if (!Main.tntLimit()) {
            this.level.setBlock(this, Block.get(0), true);
            final CompoundTag nbt = new CompoundTag().putList(new ListTag("Pos").add(new DoubleTag("", this.getFloorX() + 0.5)).add(new DoubleTag("", this.getFloorY())).add(new DoubleTag("", this.getFloorZ() + 0.5))).putList(new ListTag("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putShort("Fuse", fuse);
            final Entity tnt = Entity.createEntity("PrimedTnt", this.level.getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4), nbt, new Object[] { source });
            if (tnt != null) {
                tnt.spawnToAll();
                this.level.addLevelEvent(this, 1005);
            }
        }
    }
}
