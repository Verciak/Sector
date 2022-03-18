package xyz.rokkiitt.sector.objects.block;

import cn.nukkit.item.*;
import cn.nukkit.block.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.math.*;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.level.format.*;

public class FixedHopper extends BlockHopper
{
    public boolean place(final Item item, final Block block, final Block target, final BlockFace face, final double fx, final double fy, final double fz, final Player player) {
        final Guild g = GuildManager.getGuild(this.getLocation());
        if (g == null) {
            if (player != null) {
                Util.sendMessage((CommandSender)player, Settings.getMessage("hopperguild"));
            }
            return false;
        }
        if (g.getHoppers() >= 50) {
            if (player != null) {
                Util.sendMessage((CommandSender)player, Settings.getMessage("hopperlimit").replace("{LIMIT}", "50"));
            }
            return false;
        }
        final Guild guild = g;
        g.setHoppers(g.getHoppers() + 1);
        BlockFace facing = face.getOpposite();
        if (facing == BlockFace.UP) {
            facing = BlockFace.DOWN;
        }
        this.setDamage(facing.getIndex());
        final boolean powered = this.level.isBlockPowered((Vector3)this.getLocation());
        if (powered == this.isEnabled()) {
            this.setEnabled(!powered);
        }
        this.level.setBlock((Vector3)this, (Block)this);
        final CompoundTag nbt = new CompoundTag().putList(new ListTag("Items")).putString("id", "Hopper").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        final BlockEntityHopper hopper = (BlockEntityHopper)BlockEntity.createBlockEntity("Hopper", (FullChunk)this.level.getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4), nbt, new Object[0]);
        return hopper != null;
    }
}
