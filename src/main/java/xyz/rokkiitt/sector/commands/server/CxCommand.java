package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.command.*;

public class CxCommand extends ServerCommand
{
    public CxCommand() {
        super("cx", "cx", "/cx", "");
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        int eme = Util.getItemTypeCount(p, 4);
        int block = 576;
        final int wynik;
        eme = (wynik = eme / block);
        final int wynik2;
        block = (wynik2 = block * eme);
        if (wynik2 != 0) {
            Util.removeItemById(p, Item.get(4, Integer.valueOf(0), wynik2));
            final Item cx = PItemsGUI.cx;
            cx.setCount(wynik);
            Util.giveItem(p, cx);
            return Util.sendMessage(p, Settings.getMessage("commandcxsucces").replace("{COUNT}", "" + wynik));
        }
        return Util.sendMessage(p, Settings.getMessage("commandcxfail"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
