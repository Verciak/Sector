package xyz.rokkiitt.sector;

import cn.nukkit.command.*;
import cn.nukkit.*;
import org.apache.commons.lang3.StringUtils;
import xyz.rokkiitt.sector.utils.Util;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public abstract class SectorCommand extends Command
{
    private static CommandMap cmdMap;
    private final String name;
    private final String usage;
    private final String desc;
    public static Map<String, SectorCommand> commands;
    
    protected SectorCommand(final String name, final String desc, final String usage, final String perm, final String... aliases) {
        super(name, desc, usage, aliases);
        this.name = name;
        this.setPermission(perm);
        this.usage = usage;
        this.desc = desc;
    }
    
    public boolean execute(final CommandSender sender, final String label, final String[] args) {
        if (!this.testPermission(sender)) {
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Util.fixColor("&3Komenda sektorowa nie moze zostac uzyta z konsoli!"));
            return false;
        }
        this.onCommand((Player)sender, args);
        String cmd = StringUtils.join(args, " ");
//        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/957107354380406784/zN2FnI_Mo6XItWf-urM1SB5t0ASZzQGh_S0j7aqafozrl6ar_qbpYeVascPBUZ2wqK6L");
//        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
//        embedObject.setAuthor("LOGI KOMEND", "", "http://cravatar.eu/avatar/"+ sender.getName() +"/64.png");
//        embedObject.setColor(new Color(0x00FF00));
//        embedObject.setDescription("Gracz **" + sender.getName() + "** uzyl komendy: **/"+ getName() +" " + cmd + "**");
//        embedObject.setTitle("");
//        webhook.addEmbed(embedObject);
//        try {
//            webhook.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return true;
    }
    
    public static SectorCommand getCommand(final String s) {
        if (SectorCommand.commands.containsKey(s)) {
            return SectorCommand.commands.get(s);
        }
        return null;
    }
    
    private static void registerCommand(final SectorCommand cmd, final String fallback) {
        SectorCommand.commands.put(cmd.getName().toLowerCase(), cmd);
        SectorCommand.cmdMap.register(fallback, (Command)cmd);
    }
    
    public static void registerCommand(final SectorCommand cmd) {
        for (final String sss : cmd.getAliases()) {
            registerCommand(cmd, sss);
        }
        registerCommand(cmd, cmd.getName());
    }
    
    public boolean sendCorrectUsage(final Player p) {
        return Util.sendMessage((CommandSender)p, Settings.getMessage("correctusage").replace("{USAGE}", this.usageMessage));
    }

    public abstract boolean onCommand(final Player p0, final String[] p1);
    
    public String getDescription() {
        return this.desc;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUsage() {
        return this.usage;
    }
    
    static {
        SectorCommand.cmdMap = Main.getPlugin().getServer().getCommandMap();
        SectorCommand.commands = new HashMap<>();
    }
}
