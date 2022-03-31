package xyz.rokkiitt.sector.commands.sector;

import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.SectorCommand;
import xyz.rokkiitt.sector.objects.IncognitoInventory;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.user.User;
import xyz.rokkiitt.sector.objects.user.UserManager;
import xyz.rokkiitt.sector.packets.commands.PacketIncognitoCommand;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;
import com.jsoniter.*;
import cn.nukkit.*;

public class IncognitoCommand extends SectorCommand
{
    public IncognitoCommand() {
        super("incognito", "/incognito", "/incognito [sprawdz/list] <nickname> ", "", new String[0]);
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length > 0) {
            if (p.hasPermission(Perms.INCOGNITOSEE.getPermission())) {
                final String lowerCase = args[0].toLowerCase();
                switch (lowerCase) {
                    case "list": {
                        if (args.length < 2) {
                            return this.sendCorrectUsage(p);
                        }
                        final PacketIncognitoCommand pa = new PacketIncognitoCommand();
                        pa.sender = p.getName();
                        pa.incognitoNickname = "";
                        pa.isCheck = false;
                        pa.isList = true;
                        pa.receiver = args[1].toLowerCase();
                        return false;
                    }
                    case "sprawdz": {
                        if (args.length < 2) {
                            return this.sendCorrectUsage(p);
                        }
                        final PacketIncognitoCommand pa = new PacketIncognitoCommand();
                        pa.sender = p.getName();
                        pa.incognitoNickname = "";
                        pa.isCheck = true;
                        pa.isList = false;
                        pa.receiver = args[1].toLowerCase();
                        return false;
                    }
                    default: {
                        return this.sendCorrectUsage(p);
                    }
                }
            }
            else {
                final User u = UserManager.getUser(p.getName());
                if (u != null) {
                    new IncognitoInventory(p, u);
                }
                else {
                    p.kick("not properly loaded user data - IncognitoCommand");
                }
            }
        }
        else {
            final User u = UserManager.getUser(p.getName());
            if (u != null) {
                new IncognitoInventory(p, u);
            }
            else {
                p.kick("not properly loaded user data - IncognitoCommand");
            }
        }
        return false;
    }
    
    public boolean onCallback(final String s) {
        final PacketIncognitoCommand pa = JsonIterator.deserialize(s, PacketIncognitoCommand.class);
            final Player p = Server.getInstance().getPlayerExact(pa.sender.toLowerCase());
            if (p != null) {
                Util.sendMessage((CommandSender)p, pa.reason);
                if (!pa.incognitoNickname.isEmpty()) {
                    final User u = UserManager.getUser(p.getName());
                    if (u != null) {
                        u.setIncognito(pa.incognitoNickname);
                        if (u.isIncognito()) {
                            Util.setIncognito(u, p);
                        }
                        Main.incognitos.forEach(c -> c.callRefresh(p));
                    }
                    else {
                        p.kick("null callback - Incognito");
                    }
                }
            }
        return false;
    }
}
