package xyz.rokkiitt.sector.utils;

import cn.nukkit.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.PItemsGUI;
import xyz.rokkiitt.sector.objects.user.User;
import cn.nukkit.item.*;
import cn.nukkit.command.*;

public class DepositUtil
{
    public static boolean reduce(final Player p, final User u) {
        final int egapple = Util.getItemTypeCount(p, 466);
        final int gapple = Util.getItemTypeCount(p, 322);
        final int pearl = Util.getItemTypeCount(p, 368);
        final int primed = Util.getItemTypeCountByNBT(p, "rzucanetnt");
        final int arrow = Util.getItemTypeCount(p, 262);
        final int snowball = Util.getItemTypeCount(p, 332);
        final int gappleExcess = (gapple > Settings.LIMIT_GAPPLE) ? (gapple - Settings.LIMIT_GAPPLE) : 0;
        final int egappleExcess = (egapple > Settings.LIMIT_EGAPPLE) ? (egapple - Settings.LIMIT_EGAPPLE) : 0;
        final int pearlExcess = (pearl > Settings.LIMIT_PEARLS) ? (pearl - Settings.LIMIT_PEARLS) : 0;
        final int arrowExcess = (arrow > Settings.LIMIT_ARROWS) ? (arrow - Settings.LIMIT_ARROWS) : 0;
        final int snowballExcess = (snowball > Settings.LIMIT_SNOWBALLS) ? (snowball - Settings.LIMIT_SNOWBALLS) : 0;
        final int primedExcess = (primed > Settings.LIMIT_PRIMEDTNT) ? (primed - Settings.LIMIT_PRIMEDTNT) : 0;
        Util.removeItemById(p, Item.get(322, Integer.valueOf(0), gappleExcess));
        Util.removeItemById(p, Item.get(466, Integer.valueOf(0), egappleExcess));
        Util.removeItemById(p, Item.get(368, Integer.valueOf(0), pearlExcess));
        Util.removeItemById(p, Item.get(262, Integer.valueOf(0), arrowExcess));
        Util.removeItemById(p, Item.get(332, Integer.valueOf(0), snowballExcess));
        final Item pri = PItemsGUI.rzucane.clone();
        pri.setCount(primedExcess);
        Util.removeItemByNBTwithId(p, "rzucanetnt", pri);
        if (egappleExcess > 0 || gappleExcess > 0 || pearlExcess > 0 || snowballExcess > 0 || arrowExcess > 0 || primedExcess > 0) {
            Util.sendMessage((CommandSender)p, Settings.getMessage("depositreduce"));
            u.setEgapple(u.getEgapple() + egappleExcess);
            u.setGapple(u.getGapple() + gappleExcess);
            u.setPearls(u.getPearls()+ pearlExcess);
            u.setArrows(u.getArrows() + arrowExcess);
            u.setSnowballs(u.getSnowballs() + snowballExcess);
            u.setPrimedtnt(u.getPrimedtnt() + primedExcess);
        }
        return true;
    }
    
    public static void sniezki(final Player p, final User u) {
        if (u.getSnowballs() <= 0) {
            return;
        }
        int koxy = 0;
        final int i = Util.getItemTypeCount(p, 332);
        if (i >= Settings.LIMIT_SNOWBALLS) {
            return;
        }
        if (u.getSnowballs() <= Settings.LIMIT_SNOWBALLS) {
            koxy = u.getSnowballs();
            u.setSnowballs(u.getSnowballs() - koxy);
            final Item eg = Item.get(332);
            eg.setCount(koxy);
            Util.giveItem(p, eg);
            return;
        }
        if (i < Settings.LIMIT_SNOWBALLS) {
            final int ref = i - Settings.LIMIT_SNOWBALLS;
            koxy = ref * -1;
            u.setSnowballs(u.getSnowballs() - koxy);
            final Item eg2 = Item.get(332);
            eg2.setCount(koxy);
            Util.giveItem(p, eg2);
        }
    }
    
    public static void strzaly(final Player p, final User u) {
        if (u.getArrows()  <= 0) {
            return;
        }
        int koxy = 0;
        final int i = Util.getItemTypeCount(p, 262);
        if (i >= Settings.LIMIT_ARROWS) {
            return;
        }
        if (u.getArrows()  <= Settings.LIMIT_ARROWS) {
            koxy = u.getArrows() ;
            u.setArrows(u.getArrows() - koxy);
            final Item eg = Item.get(262);
            eg.setCount(koxy);
            Util.giveItem(p, eg);
            return;
        }
        if (i < Settings.LIMIT_ARROWS) {
            final int ref = i - Settings.LIMIT_ARROWS;
            koxy = ref * -1;
            u.setArrows(u.getArrows() - koxy);
            final Item eg2 = Item.get(262);
            eg2.setCount(koxy);
            Util.giveItem(p, eg2);
        }
    }
    
    public static void rzucane(final Player p, final User u) {
        if (u.getPrimedtnt() <= 0) {
            return;
        }
        int rzucane = 0;
        final int iiii = Util.getItemTypeCountByNBT(p, "rzucanetnt");
        if (iiii >= Settings.LIMIT_PRIMEDTNT) {
            return;
        }
        if (u.getPrimedtnt() <= Settings.LIMIT_PRIMEDTNT) {
            rzucane = u.getPrimedtnt();
            u.setPrimedtnt(u.getPrimedtnt() - rzucane);
            final Item rzucak = PItemsGUI.rzucane;
            rzucak.setCount(rzucane);
            Util.giveItem(p, rzucak);
            return;
        }
        if (iiii < Settings.LIMIT_PRIMEDTNT) {
            final int ref = iiii - Settings.LIMIT_PRIMEDTNT;
            rzucane = ref * -1;
            u.setPrimedtnt(u.getPrimedtnt() - rzucane);
            final Item rzucak2 = PItemsGUI.rzucane;
            rzucak2.setCount(rzucane);
            Util.giveItem(p, rzucak2);
        }
    }
    
    public static void perly(final Player p, final User u) {
        if (u.getPearls() == 0) {
            return;
        }
        int koxy = 0;
        final int i = Util.getItemTypeCount(p, 368);
        if (i >= Settings.LIMIT_PEARLS) {
            return;
        }
        if (u.getPearls() <= Settings.LIMIT_PEARLS) {
            koxy = u.getPearls();
            u.setPearls(u.getPearls() - koxy);
            final Item eg = Item.get(368);
            eg.setCount(koxy);
            Util.giveItem(p, eg);
            return;
        }
        if (i < Settings.LIMIT_PEARLS) {
            final int ref = i - Settings.LIMIT_PEARLS;
            koxy = ref * -1;
            u.setPearls(u.getPearls() - koxy);
            final Item eg2 = Item.get(368);
            eg2.setCount(koxy);
            Util.giveItem(p, eg2);
        }
    }
    
    public static void refy(final Player p, final User u) {
        if (u.getGapple() <= 0) {
            return;
        }
        int koxy = 0;
        final int i = Util.getItemTypeCount(p, 322);
        if (i >= Settings.LIMIT_GAPPLE) {
            return;
        }
        if (u.getGapple() <= Settings.LIMIT_GAPPLE) {
            koxy = u.getGapple();
            u.setGapple(u.getGapple() - koxy);
            final Item eg = Item.get(322);
            eg.setCount(koxy);
            Util.giveItem(p, eg);
            return;
        }
        if (i < Settings.LIMIT_GAPPLE) {
            final int ref = i - Settings.LIMIT_GAPPLE;
            koxy = ref * -1;
            u.setGapple(u.getGapple() - koxy);
            final Item eg2 = Item.get(322);
            eg2.setCount(koxy);
            Util.giveItem(p, eg2);
        }
    }
    
    public static void koxy(final Player p, final User u) {
        if (u.getEgapple() <= 0) {
            return;
        }
        int koxy = 0;
        final int i = Util.getItemTypeCount(p, 466);
        if (i >= Settings.LIMIT_EGAPPLE) {
            return;
        }
        if (u.getEgapple() <= Settings.LIMIT_EGAPPLE) {
            koxy = u.getEgapple();
            u.setEgapple(u.getEgapple() - koxy);
            final Item eg = Item.get(466);
            eg.setCount(koxy);
            Util.giveItem(p, eg);
            return;
        }
        if (i < Settings.LIMIT_EGAPPLE) {
            final int ref = i - Settings.LIMIT_EGAPPLE;
            koxy = ref * -1;
            u.setEgapple(u.getEgapple() - koxy);
            final Item eg2 = Item.get(466);
            eg2.setCount(koxy);
            Util.giveItem(p, eg2);
        }
    }
}
