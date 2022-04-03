package xyz.rokkiitt.sector.tasks;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.entity.Zombie;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteRegion;
import xyz.rokkiitt.sector.utils.SpaceUtil;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.block.*;
import cn.nukkit.math.*;
import cn.nukkit.*;
import cn.nukkit.level.*;
import cn.nukkit.entity.item.*;
import cn.nukkit.entity.mob.*;

import java.util.*;
import cn.nukkit.entity.*;

public class SecondTask implements Runnable
{
    boolean alerted;
    int countdown;
    int openedfor;
    int landtime;
    
    public SecondTask() {
        this.alerted = false;
        this.countdown = 300;
        this.openedfor = 0;
        this.landtime = 30;
    }
    
    @Override
    public void run() {
        for (Player player : Server.getInstance().getOnlinePlayers().values())
        if (!Config.isSpawn(player.getLocation())) {
            if (MeteoriteManager.isRunning()) {
                final MeteoriteRegion region = MeteoriteManager.getRegion();
                if (region != null) {
                    if (MeteoriteManager.getCountDown() > 0) {
                        MeteoriteManager.removeCountDown();
                    }
                    else if (!MeteoriteManager.wasThrown()) {
                        MeteoriteManager.getRegion().getWorld().setBlock((Vector3)MeteoriteManager.getRegion().getCenter().add(0.0, 10.0, 0.0), Block.get(122));
                        MeteoriteManager.reset();
                        this.landtime = 30;
                    }
                }
            }
            else {
                if (this.landtime <= 0) {
                    if (!MeteoriteManager.isOpened()) {
                        final MeteoriteRegion mr = MeteoriteManager.getRegion();
                        if (mr != null) {
                            SpaceUtil.setBlock(mr.getWorld(), 0, 0, mr.getCenter().getFloorX(), mr.getCenter().getFloorY() - 3, mr.getCenter().getFloorZ());
                            MeteoriteManager.setOpened(true);
                        }
                    }
                    MeteoriteManager.chceckForRepairs();
                }
                else {
                    --this.landtime;
                }
                if (!MeteoriteManager.isRunning()) {
                    MeteoriteManager.summon(Server.getInstance().getLevelByName("world"));
                    Config.meteorite = System.currentTimeMillis();
                }
            }
        }
        --this.countdown;
        final Iterator<Level> w = Server.getInstance().getLevels().values().iterator();
        int amount = 0;
        while (w.hasNext()) {
            final Entity[] entities;
            final Entity[] ee = entities = w.next().getEntities();
            for (final Entity e : entities) {
                if (e instanceof EntityItem || e instanceof EntityXPOrb || e instanceof EntityEnderman || e instanceof EntityCreeper|| e instanceof Zombie) {
                    ++amount;
                }
            }
        }
        if (amount >= 500 && this.countdown > 1) {
            this.countdown = 15;
        }
        if (this.countdown > 0) {
            if (this.countdown == 30 || this.countdown == 15 || this.countdown <= 3) {
                Util.sendTip(Server.getInstance().getOnlinePlayers().values(), Settings.getMessage("abyssleft").replace("{TIME}", this.countdown + ""));
            }
        }
        else {
            final Iterator<Level> ww = Server.getInstance().getLevels().values().iterator();
            while (ww.hasNext()) {
                final Entity[] entities2;
                final Entity[] ee2 = entities2 = ww.next().getEntities();
                for (final Entity e2 : entities2) {
                    if (e2 instanceof EntityItem || e2 instanceof EntityXPOrb || e2 instanceof EntityEnderman || e2 instanceof EntityCreeper) {
                        e2.close();
                    }
                }
            }
            Util.sendTip(Server.getInstance().getOnlinePlayers().values(), Settings.getMessage("abyss"));
            this.countdown = 300;
        }
    }
}
