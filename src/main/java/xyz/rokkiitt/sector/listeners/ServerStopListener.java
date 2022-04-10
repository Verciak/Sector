package xyz.rokkiitt.sector.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.server.*;
import xyz.rokkiitt.sector.objects.combat.CombatManager;
import cn.nukkit.event.*;

public class ServerStopListener implements Listener
{
    @EventHandler
    public void onBreak(final ServerStopEvent e) {
        CombatManager.clear();
        for (Player p : Server.getInstance().getOnlinePlayers().values()){
            p.close(p.getLeaveMessage(), "KURWA");
        }
    }
}
