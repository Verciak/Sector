package xyz.rokkiitt.sector.listeners.guild;

import xyz.rokkiitt.sector.config.Config;
import cn.nukkit.block.*;
import cn.nukkit.event.*;
import cn.nukkit.event.block.*;

public class BlockUpdateListener implements Listener
{
    @EventHandler
    public void onUpdate(final BlockUpdateEvent e) {
        if (Config.isSpawn(e.getBlock().getLocation()) || e.getBlock() instanceof BlockLeaves || e.getBlock() instanceof BlockLeaves2) {
            e.setCancelled();
        }
    }
    
    @EventHandler
    public void onFire(final BlockBurnEvent e) {
        if (Config.isSpawn(e.getBlock().getLocation())) {
            e.setCancelled();
        }
    }
    
    @EventHandler
    public void onFire(final BlockIgniteEvent e) {
        if (Config.isSpawn(e.getBlock().getLocation())) {
            e.setCancelled();
        }
    }
    
    @EventHandler
    public void onSpread(final LiquidFlowEvent e) {
        e.setCancelled();
    }
    
    @EventHandler
    public void onLeash(final LeavesDecayEvent e) {
        e.setCancelled();
    }
}
