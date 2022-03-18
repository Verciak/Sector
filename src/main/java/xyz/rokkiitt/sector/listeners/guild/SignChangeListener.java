package xyz.rokkiitt.sector.listeners.guild;

import cn.nukkit.event.block.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.block.Cooldown;
import xyz.rokkiitt.sector.objects.entity.blockentity.ChestTile;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import cn.nukkit.math.*;
import cn.nukkit.*;
import cn.nukkit.block.*;
import cn.nukkit.blockentity.*;
import cn.nukkit.event.*;

public class SignChangeListener implements Listener
{
    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        final Player player = event.getPlayer();
        if (Config.isSpawn(player.getLocation())) {
            return;
        }
        final Block sign = event.getBlock();
        if (sign instanceof BlockWallSign) {
            final Guild g = GuildManager.getGuild(sign.getLocation());
            if (g != null) {
                final int[] faces = { 3, 2, 5, 4 };
                final Block behind = sign.getLevel().getBlock((Vector3)sign.getSide(BlockFace.fromIndex(faces[sign.getDamage() - 2])));
                if (behind.getId() != 54) {
                    return;
                }
                final Guild gg = GuildManager.getGuild(behind.getLocation());
                if (gg != null && gg.equals(g)) {
                    final BlockEntity tile = sign.getLevel().getBlockEntity((Vector3)behind);
                    if (!(tile instanceof ChestTile)) {
                        return;
                    }
                    if (((ChestTile)tile).isLocked()) {
                        if (((ChestTile)tile).getSign().equals((Object)sign)) {
                            event.setCancelled();
                        }
                        return;
                    }
                    if (event.getLine(0).equalsIgnoreCase("[lock]")) {
                        ((ChestTile)tile).setLocked(player);
                        ((ChestTile)tile).setSign((Vector3)sign);
                        event.setLine(0, Util.fixColor("&0[&6LOCK&0]"));
                        event.setLine(1, Util.fixColor("&4Skrzynia gracza:"));
                        event.setLine(2, player.getName());
                        Util.sendMessage((CommandSender)player, Settings.getMessage("lockcreate"));
                    }
                }
            }
        }
    }
}
