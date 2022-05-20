package xyz.rokkiitt.sector.commands.guild;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.utils.Util;

public class GuildExpandCommand extends ServerCommand {
    public GuildExpandCommand() {
        super("powieksz", "powieksza gildie", "/powieksz", "", new String[0]);
    }

    public boolean onCommand(Player p, String[] args) {
        if (!Settings.ENABLE_GUILD)
            return Util.sendMessage((CommandSender)p, Settings.getMessage("guildsoffline"));
        User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (u.getTag().equalsIgnoreCase("NIEPOSIADA"))
                return Util.sendMessage((CommandSender)p, Settings.getMessage("donthaveguild"));
            Guild g = GuildManager.getGuild(u.getTag());
            if (g != null) {
                if (g.getExplodeTime() >= System.currentTimeMillis())
                    return Util.sendMessage((CommandSender)p, Settings.getMessage("guildplacetnt").replace("{TIME}", Util.formatTime(g.getExplodeTime() - System.currentTimeMillis())));
                if (!g.addSize())
                    return Util.sendMessage((CommandSender)p, Settings.getMessage("commandexpandmax"));
                if (!p.hasPermission("payments.free")) {
                    Item item = Item.get(388);
                    item.setCount(Settings.GUILD_SIZE_COST);
                    if (item.getId() != 0 && item.getCount() > 0) {
                        if (!p.getInventory().contains(item))
                            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandexpandnoeme").replace("{AMOUNT}", "" + Settings.GUILD_SIZE_COST));
                        Util.removeItemById(p, new Item[] { item });
                    }
                }
                return Util.sendMessage((CommandSender)p, Settings.getMessage("commandexpandsucces"));
            }
            return Util.sendMessage((CommandSender)p, Settings.getMessage("needguildsector"));
        }
        p.kick("&9 not properly loaded data");
        return false;
    }

    public boolean onConsoleCommand(CommandSender p, String[] args) {
        return false;
    }
}
