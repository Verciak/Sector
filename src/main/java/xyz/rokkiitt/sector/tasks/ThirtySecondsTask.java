package xyz.rokkiitt.sector.tasks;

import cn.nukkit.*;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.AutoMsgUtil;
import xyz.rokkiitt.sector.utils.DepositUtil;
import cn.nukkit.level.*;
import cn.nukkit.scheduler.*;

public class ThirtySecondsTask implements Runnable
{
    @Override
    public void run() {
        AutoMsgUtil.configure();
        for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                if (!Config.isSpawn(p.getLocation())) {
                    DepositUtil.reduce(p, u);
                }
                AutoMsgUtil.send(p);
            }
        }
        for (final Level level : Server.getInstance().getLevels().values()) {

                level.setRaining(false);
                level.setThundering(false);
                level.setRainTime(240000);
                level.setThunderTime(240000);
                Server.getInstance().getScheduler().scheduleDelayedTask((Task)new Task() {
                    public void onRun(final int arg0) {
                        level.sendWeather(Server.getInstance().getOnlinePlayers().values());
                    }
                }, 40);

        }
    }
}
