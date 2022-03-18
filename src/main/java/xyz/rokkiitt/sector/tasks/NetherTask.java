package xyz.rokkiitt.sector.tasks;

import cn.nukkit.*;
import xyz.rokkiitt.sector.utils.Util;

public class NetherTask implements Runnable
{
    @Override
    public void run() {
        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
            Util.changeNametag(p);
        }
    }
}
