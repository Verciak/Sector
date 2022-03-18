package xyz.rokkiitt.sector.listeners;

import cn.nukkit.event.server.*;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import cn.nukkit.event.*;

public class ServerStopListener implements Listener
{
    @EventHandler
    public void onBreak(final ServerStopEvent e) {
        CombatManager.clear();
    }
}
