package xyz.rokkiitt.sector.commands.server;

import cn.nukkit.*;
import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.objects.wymiana.TradeHandler;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class TradeCommand extends ServerCommand
{
    public TradeCommand() {
        super("wymiana", "wymiana", "/wymiana [nickname]", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
//        if (!Config.isSpawn(p.getLocation())) {
//            return Util.sendMessage((CommandSender)p, Settings.getMessage("commandonlyspawn"));
//        }
        if (args.length != 1) {
            return this.sendCorrectUsage((CommandSender)p);
        }
        final Player p2 = Util.matchPlayer(args[0]);
        if (p2 == null) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("playersector"));
        }
        final User uu = UserManager.getUser(p2.getName());
        if (uu == null) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("playersector"));
        }
        if (p2 == p) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("tradesame"));
        }
        final User u = UserManager.getUser(p.getName());
        if (u == null) {
            p.close(Util.fixColor("&cnull data - TradeCommand"));
            return false;
        }
        if (u.isTrading()) {
            return Util.sendMessage((CommandSender)p, Settings.getMessage("trading"));
        }
        if (u.getTradeInvites().contains(p2.getName()) && !uu.isTrading()) {
            u.setTrading(true);
            uu.setTrading(true);
            u.getTradeInvites().remove(p2.getName());
            TradeHandler.create(p, p2, u, uu);
            return false;
        }
        if (!uu.getTradeInvites().contains(p.getName())) {
            uu.getTradeInvites().add(p.getName());
            Util.sendMessage((CommandSender)p2, Settings.getMessage("tradereceive").replace("{PLAYER}", u.getNickname()));
            return Util.sendMessage((CommandSender)p, Settings.getMessage("tradesent").replace("{PLAYER}", uu.getNickname()));
        }
        return Util.sendMessage((CommandSender)p, Settings.getMessage("tradealready"));
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
