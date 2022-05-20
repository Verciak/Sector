package xyz.rokkiitt.sector.commands;

import cn.nukkit.*;
import cn.nukkit.command.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import org.apache.commons.lang3.*;
import cn.nukkit.item.*;

public class DescriptionCommand extends ServerCommand
{
    public DescriptionCommand() {
        super("opis", "opis", "/opis [opis]", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length <= 0) {
            return this.sendCorrectUsage(p);
        }
        if (args.length < 2 || args.length > 16) {
            return Util.sendMessage(p, Settings.getMessage("commandopislenght"));
        }
        final Item item = p.getInventory().getItemInHand();
        if (item == null) {
            return Util.sendMessage(p, Settings.getMessage("commandopisfail"));
        }
        if (!Util.hasNBTTag(item, "safe")) {
            return Util.sendMessage(p, Settings.getMessage("commandopisfail"));
        }
        if (Util.getNBTTagValue(item, "owner").equalsIgnoreCase(p.getName())) {
            final String desc = StringUtils.join(args, " ");
            final Item clone = item.clone();
            final String[] lores = clone.getLore();
            if (lores.length >= 8) {
                lores[1] = Util.fixColor("&r &9Opis: &7" + desc);
            }
            clone.setLore(lores);
            p.getInventory().setItemInHand(clone);
            return Util.sendMessage(p, Settings.getMessage("commandopissucces").replace("{DESC}", desc));
        }
        return Util.sendMessage(p, Settings.getMessage("commandopisfail"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
