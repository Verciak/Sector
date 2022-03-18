package xyz.rokkiitt.sector.tasks;

import cn.nukkit.scheduler.*;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;

public class SaveDataTask extends Task
{
    public void onRun(final int tick) {
        for (final User u : UserManager.users) {
            u.save();
        }
        for (final Guild g : GuildManager.guilds) {
            g.saveRegeneration();
            g.saveLogblock();
        }
    }
}
