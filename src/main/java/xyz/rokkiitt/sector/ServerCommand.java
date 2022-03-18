package xyz.rokkiitt.sector;

import cn.nukkit.command.*;
import cn.nukkit.*;
import xyz.rokkiitt.sector.utils.Util;

public abstract class ServerCommand extends Command
{
    private static CommandMap cmdMap;
    private final String name;
    private final String usage;
    private final String desc;
    
    protected ServerCommand(final String name, final String desc, final String usage, final String perm, final String... aliases) {
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
            return this.onConsoleCommand(sender, args);
        }
        return this.onCommand((Player)sender, args);
    }
    
    private static void registerCommand(final Command cmd, final String fallback) {
        ServerCommand.cmdMap.register(fallback, cmd);
    }
    
    public static void registerCommand(final Command cmd) {
        for (final String sss : cmd.getAliases()) {
            registerCommand(cmd, sss);
        }
        registerCommand(cmd, cmd.getName());
    }
    
    public boolean sendCorrectUsage(final CommandSender p) {
        return Util.sendMessage(p, Settings.getMessage("correctusage").replace("{USAGE}", this.usageMessage));
    }
    
    public abstract boolean onCommand(final Player p0, final String[] p1);
    
    public abstract boolean onConsoleCommand(final CommandSender p0, final String[] p1);
    
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
        ServerCommand.cmdMap = Main.getPlugin().getServer().getCommandMap();
    }
}
