package xyz.rokkiitt.sector.commands.guild;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import java.util.ArrayList;
import java.util.List;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.guild.GuildPanelGUI;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.config.Config;
import xyz.rokkiitt.sector.objects.guild.Guild;
import xyz.rokkiitt.sector.objects.guild.GuildManager;
import xyz.rokkiitt.sector.objects.guild.gitems.GuildItems;
import xyz.rokkiitt.sector.objects.guild.gitems.GuildItemsInventory;
import xyz.rokkiitt.sector.objects.guild.gitems.GuildItemsManager;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.packets.guild.commands.PacketGuildCreate;
import xyz.rokkiitt.sector.utils.Time;
import xyz.rokkiitt.sector.utils.Util;

public class GuildCreateCommand extends ServerCommand {
    public GuildCreateCommand() {
        super("zaloz", "zaklada gildie", "/zaloz [tag] [nazwa]", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(Player p, String[] args) {
        if (!Settings.ENABLE_GUILD) {
            return Util.sendMessage(p, Settings.getMessage("guildsoffline"));
        }
        if (Config.isSpawn(p.getLocation())) {
            return Util.sendMessage(p, Settings.getMessage("commandwithoutspawn"));
        }
        if (args.length < 2) {
            return sendCorrectUsage(p);
        }
        User u = UserManager.getUser(p.getName());
        if (u != null) {
            if (!u.getTag().equalsIgnoreCase("NIEPOSIADA")) {
                return Util.sendMessage(p, Settings.getMessage("hasguild"));
            }
            String tag = args[0];
            String name = args[1];
            if (tag.length() < Settings.TAG_LENGHT_MIN || tag.length() > Settings.TAG_LENGHT_MAX) {
                return Util.sendMessage(p, Settings.getMessage("commandcreatetag")
                        .replace("{TAGMIN}", Integer.toString(Settings.TAG_LENGHT_MIN)).replace("{TAGMAX}", Integer.toString(Settings.TAG_LENGHT_MAX)));
            }
            if (name.length() < Settings.NAME_LENGHT_MIN || name.length() > Settings.NAME_LENGHT_MAX) {
                return Util.sendMessage(p, Settings.getMessage("commandcreatename")
                        .replace("{NAMEMIN}", Integer.toString(Settings.NAME_LENGHT_MIN)).replace("{NAMEMAX}", Integer.toString(Settings.NAME_LENGHT_MAX)));
            }
            if (!tag.matches("^[a-zA-Z0-9]*$")) {
                return Util.sendMessage(p, Settings.getMessage("guildregex"));
            }
            if (!name.matches("^[a-zA-Z0-9]*$")) {
                return Util.sendMessage(p, Settings.getMessage("guildregex"));
            }
            if (!GuildManager.canCreateGuildByGuild(p.getLocation())) {
                return Util.sendMessage(p, Settings.getMessage("commandcreateguild"));
            }
            if (!p.hasPermission("payments.free")) {
                for (GuildItems xx : GuildItemsManager.getItems()) {
                    Item item = xx.getWhat().clone();
                    item.setCount(xx.getAmount());
                    if (item.getId() != 0 && item.getCount() > 0 &&
                            !p.getInventory().contains(item)) {
                        p.addWindow(new GuildItemsInventory());
                        return false;
                    }
                }
            }
            List<String> m = new ArrayList<>();
            m.add(p.getName().toLowerCase());
            Util.sendMessage(p, Settings.getMessage("createbc")
                    .replace("{TAG}", tag)
                    .replace("{NAME}", name)
                    .replace("{PLAYER}", p.getName())
            );
            Guild g = GuildManager.createGuild(tag, name, p.getName(), p.getLocation().getFloorX(), p.getLocation().getFloorZ(), Settings.GUILD_SIZE_START,
                    JsonStream.serialize(m), 0, "brak", System.currentTimeMillis(),
                    System.currentTimeMillis() + Time.DAY.getTime(2),
                    System.currentTimeMillis() + Time.DAY.getTime(2), System.currentTimeMillis(), "red", "&c", 3, 0, "brak");
            Location loc = g.getCuboid().getCenter();
            loc.add(0.5D, 2.0D, 0.5D);
            p.teleport(loc);
            Util.changeNametag(p);
            return false;
        }
        p.kick("&9 not properly loaded data");
        return false;
    }

    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
