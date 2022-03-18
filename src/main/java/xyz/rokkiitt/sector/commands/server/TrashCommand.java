package xyz.rokkiitt.sector.commands.server;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.*;
import cn.nukkit.inventory.*;
import cn.nukkit.command.*;

public class TrashCommand extends ServerCommand
{
    public TrashCommand() {
        super("kosz", "kosz", "/kosz", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        p.addWindow((Inventory)new DoubleChestFakeInventory(p, Util.fixColor("&3Kosz")) {
            @Override
            protected void onSlotChange(final FakeSlotChangeEvent e) {
            }
        });
        return false;
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
