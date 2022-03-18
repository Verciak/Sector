package xyz.rokkiitt.sector.commands.server.admins;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Level;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.utils.Util;

public class KillCommand extends ServerCommand
{
    public KillCommand() {
        super("kill", "kill", "/kill [player/@e]", Perms.CMD_KILL.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length >= 2) {
            return this.sendCorrectUsage(p);
        }
        if (args.length != 1) {
            return this.sendCorrectUsage(p);
        }
        final Player player = p.getServer().getPlayer(args[0]);
        if (player != null) {
            final EntityDamageEvent ev = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000.0f);
            p.getServer().getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return true;
            }
            player.setLastDamageCause(ev);
            player.setHealth(0.0f);
            return Util.sendMessage(p, Settings.getMessage("commandkillsucces").replace("{WHO}", player.getName()));
        }
        else {
            if (args[0].equals("@e")) {
                int i = 0;
                for (final Level level : Server.getInstance().getLevels().values()) {
                    for (final Entity entity : level.getEntities()) {
                        if (!(entity instanceof EntityHuman)) {
                            ++i;
                            entity.close();
                        }
                    }
                }
                return Util.sendMessage(p, Settings.getMessage("commandkillsuccesentity").replace("{COUNT}", "" + i));
            }
            return Util.sendMessage(p, Settings.getMessage("playersector"));
        }
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
