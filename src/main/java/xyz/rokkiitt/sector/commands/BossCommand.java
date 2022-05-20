package xyz.rokkiitt.sector.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.boss.BossManager;
import xyz.rokkiitt.sector.objects.entity.BaseEntity;
import xyz.rokkiitt.sector.objects.entity.Zombie;
import xyz.rokkiitt.sector.utils.Util;

import java.util.Iterator;

public class BossCommand extends ServerCommand
{
    public BossCommand() {
        super("boss", "Stworz BOSSA", "/boss", "admin.boss");
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length < 1) {
            Util.sendMessage(p, "&c/boss list &8- &6Lista bossow");
            Util.sendMessage(p, "&c/boss usun &8- &6Usuwa wszystkie bossy");
            Util.sendMessage(p, "&c/boss create &8- &6Tworzy bossa");
            return false;
        }
        if(args[0].contains("list")){
            BossManager.users.forEach(boss -> {
                p.sendMessage(Util.fixColor("&cBoss &8- &9" + boss.getAuthor()));
            });
        }
        if(args[0].contains("create")){
            if(!BossManager.users.isEmpty()){
                Util.sendMessage(p, "&cBoss juz istnieje!");
                return false;
            }
            if(BossManager.getBoss(p.getName()) != null){
                Util.sendMessage(p, "&cBoss juz istnieje!");
                return false;
            }
            BossManager.createBoss(p);
            p.sendTitle(Util.fixColor("&cBOSS"), Util.fixColor("&7X: &9" + p.getFloorX() + " &7Y: &9" + p.getFloorY() + " &7Z: &9" + p.getFloorZ()), 30, 80, 30);
            createEntity("Zombie", p.getLocation().add(0.0D, 1.0D, 0.0D)).setScale(3f);
        }
        if(args[0].contains("usun")){
            p.sendMessage(Util.fixColor("&aBoss zostal usuniety!"));
            BossManager.users.forEach(BossManager::deleteBoss);
            final Iterator<Level> ww = Server.getInstance().getLevels().values().iterator();
            while (ww.hasNext()) {
                final Entity[] entities2;
                final Entity[] ee2 = entities2 = ww.next().getEntities();
                for (final Entity e2 : entities2) {
                    if (e2 instanceof EntityItem || e2 instanceof EntityXPOrb || e2 instanceof EntityEnderman || e2 instanceof EntityCreeper || e2 instanceof Zombie) {
                        e2.close();
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }

    public BaseEntity createEntity(Object type, Position pos) {
        BaseEntity entity = (BaseEntity) Entity.createEntity((String)type, pos, new Object[0]);
        if (entity != null)
            if (!entity.isInsideOfSolid()) {
                CreatureSpawnEvent ev = new CreatureSpawnEvent(entity.getNetworkId(), pos, entity.namedTag, CreatureSpawnEvent.SpawnReason.NATURAL);
                Server.getInstance().getPluginManager().callEvent((Event)ev);
                if (!ev.isCancelled()) {
                    entity.spawnToAll();
                } else {
                    entity.close();
                    entity = null;
                }
            } else {
                entity.close();
                entity = null;
            }
        return entity;
    }
}
