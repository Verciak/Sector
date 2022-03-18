package xyz.rokkiitt.sector.commands.server;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Effects;
import cn.nukkit.*;
import cn.nukkit.inventory.*;
import cn.nukkit.command.*;

public class EffectsCommand extends ServerCommand
{
    public EffectsCommand() {
        super("efekty", "efekty", "/efekty", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        p.addWindow((Inventory)new Effects());
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
