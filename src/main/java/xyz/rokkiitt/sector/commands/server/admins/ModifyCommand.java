package xyz.rokkiitt.sector.commands.server.admins;

import xyz.rokkiitt.sector.ServerCommand;
import xyz.rokkiitt.sector.objects.Perms;
import xyz.rokkiitt.sector.objects.modify.gui.*;
import cn.nukkit.*;
import cn.nukkit.command.*;

public class ModifyCommand extends ServerCommand
{
    public ModifyCommand() {
        super("modyfikuj", "modyfikowanie zestawow", "/modyfikuj [drop|pandora|cx|meteoryt|gracz|vip|svip|sponsor|itemy]", Perms.CMD_MODIFY.getPermission());
    }
    
    @Override
    public boolean onCommand(final Player p, final String[] args) {
        if (args.length > 0) {
            final String lowerCase = args[0].toLowerCase();
            switch (lowerCase) {
                case "drop": {
                    new modifyDrop(p);
                    break;
                }
//                case "itemy": {
//                    new modifyItems(p);
//                    break;
//                }
                case "gracz": {
                    new modifyPlayer(p);
                    break;
                }
                case "vip": {
                    new modifyVip(p);
                    break;
                }
                case "svip": {
                    new modifySvip(p);
                    break;
                }
                case "sponsor": {
                    new modifySponsor(p);
                    break;
                }
                case "pandora": {
                    new modifyPandora(p);
                    break;
                }
                case "cx": {
                    new modifyCobblex(p);
                    break;
                }
                case "meteoryt": {
                    new modifyMeteor(p);
                    break;
                }
                default: {
                    return this.sendCorrectUsage(p);
                }
            }
            return false;
        }
        return this.sendCorrectUsage(p);
    }
    
    @Override
    public boolean onConsoleCommand(final CommandSender p, final String[] args) {
        return false;
    }
}
