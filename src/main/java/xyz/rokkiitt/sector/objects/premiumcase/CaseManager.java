package xyz.rokkiitt.sector.objects.premiumcase;

import bimopower.musiccontroller.api.MusicControllerApi;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.NukkitRunnable;
import com.google.common.collect.Maps;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.utils.PolishItemNames;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CaseManager {
    public static List<PremiumCase> drop = new ArrayList<>();

    public static void onLoad(){
        drop.clear();
        for (PremiumCase premiumCase : PremiumCaseManager.getItems()){
            drop.add(premiumCase);
        }
    }

    private static ConcurrentHashMap<Player, CaseInv> cases = new ConcurrentHashMap<>();

    public static boolean isInCase(Player p) {
        return cases.containsKey(p);
    }

    public static CaseInv getCase(Player p) {
        return cases.get(p);
    }

    public static void addCase(final Player p, final CaseInv inv) {
        if (isInCase(p))
            return;
        cases.put(p, inv);
        new NukkitRunnable() {
            public void run() {
                if (!CaseManager.isInCase(p)) {
                    cancel();
                    return;
                }
                int x = RandomUtil.getRandInt(0, CaseManager.drop.size() - 1);
                PremiumCase premiumCasee = CaseManager.drop.get(x);

                if (inv.getRool() >= inv.getRoolMax()) {
                    Item win = inv.getInv().getItem(13);
                    PremiumCase premiumCase = PremiumCaseManager.get(win.getId());
                    final int a = (premiumCase.getMinAmount() == premiumCase.getMaxAmount()) ? premiumCase.getMinAmount() : RandomUtil.getRandInt(premiumCase.getMinAmount(), premiumCase.getMaxAmount());
                    win.setCount(a);
                    Util.giveItem(p, win);
                    cancel();
                    CaseManager.removeCase(p);
                    p.getInventory().onClose(p);
                    return;
                }
                inv.setRool(inv.getRool() + 1);
                if (!inv.getInv().getTitle().equalsIgnoreCase(Util.fixColor("&f&oOtwieranie &8..."))) {
                    new PremiumCaseGUI(p);
                }
                PlaySoundPacket pk = new PlaySoundPacket();
                pk.name = Sound.BLOCK_CLICK.getSound();
                pk.volume = 1;
                pk.pitch = 1;
                pk.x = (int) p.x;
                pk.y = (int) p.y;
                pk.z = (int) p.z;
                p.dataPacket(pk);

//                p.playSound(p.getLocation(), Sound.CLICK, 10.0F, 10.0F);
                Item i = premiumCasee.getWhat();
                Item i1 = inv.getInv().getItem(10);
                Item i2 = inv.getInv().getItem(11);
                Item i3 = inv.getInv().getItem(12);
                Item i4 = inv.getInv().getItem(13);
                Item i5 = inv.getInv().getItem(14);
                Item i6 = inv.getInv().getItem(15);
                Item i7 = inv.getInv().getItem(16);
                Item i8 = inv.getInv().getItem(17);
                inv.getInv().setItem(9, i1);
                inv.getInv().setItem(10, i2);
                inv.getInv().setItem(11, i3);
                inv.getInv().setItem(12, i4);
                inv.getInv().setItem(13, i5);
                inv.getInv().setItem(14, i6);
                inv.getInv().setItem(15, i7);
                inv.getInv().setItem(16, i8);
                inv.getInv().setItem(17, i);
            }
        }.runTaskTimer((Plugin) Main.getPlugin(), 5, 5);
    }

    public static void removeCase(Player p) {
        if (!isInCase(p))
            return;
        cases.remove(p);
    }


    public static ConcurrentHashMap<Player, CaseInv> getCases() {
        return cases;
    }

}
