package xyz.rokkiitt.sector.commands.server;

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
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.abbys.AbbysInventory;
import xyz.rokkiitt.sector.objects.boss.BossManager;
import xyz.rokkiitt.sector.objects.entity.BaseEntity;
import xyz.rokkiitt.sector.objects.entity.Zombie;
import xyz.rokkiitt.sector.utils.Util;

import java.util.Iterator;

public class AbbysCommand extends ServerCommand
{
    public AbbysCommand() {
        super("otchlan", "Otwiera otchlan", "/otchlan", "cmd.otchlan");
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        new AbbysInventory(p);
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
