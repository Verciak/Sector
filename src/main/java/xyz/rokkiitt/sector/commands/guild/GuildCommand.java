package xyz.rokkiitt.sector.commands.guild;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;

public class GuildCommand extends ServerCommand {
    public GuildCommand() {
        super("g", "sprawdza komendy gildijne", "/g", "", new String[0]);
    }

    public boolean onCommand(Player p, String[] args) {
        return Util.sendMessage((CommandSender)p, Settings.getMessage("guildhelp"));
    }

    public boolean onConsoleCommand(CommandSender p, String[] args) {
        return false;
    }
}