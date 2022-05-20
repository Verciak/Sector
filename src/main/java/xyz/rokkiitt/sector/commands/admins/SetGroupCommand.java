package xyz.rokkiitt.sector.commands.admins;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Permissions;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;

public class SetGroupCommand extends ServerCommand {

    public SetGroupCommand() {
        super("setgroup", "setgroup", "/setgroup [nickname] [gracz/vip/svip/yt/sponsor/helper/mod/admin/headadmin/root/custom]", Perms.CMD_SETGROUP.getPermission());
    }

    public boolean onCommand(Player p, String[] args) {
        if(args.length < 1) {
            sendCorrectUsage(p);
            return false;
        }
        if(UserManager.getUser(args[0]) != null){
            Player player = Server.getInstance().getPlayer(args[0]);
            User u =UserManager.getUser(player.getName());
            u.setRank(args[1]);
            Permissions.refreshPermissions(player, u);
        }
        return false;
    }

    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
