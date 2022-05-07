package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.drop.gui.MainDropInventory;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;

public class DropCommand extends ServerCommand
{
    public DropCommand() {
        super("drop", "drop", "/drop", "", new String[0]);
    }

    @Override
    public boolean onCommand(final Player p, final String[] args) {
        final User u = UserManager.getUser(p.getName());
        if (u != null) {
//            MusicControllerApi.play(p, "song.drop");
            new MainDropInventory(p, u);
            return false;
        }
        p.kick(Util.fixColor("&9not properly loaded user data - DropCommand"));
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
