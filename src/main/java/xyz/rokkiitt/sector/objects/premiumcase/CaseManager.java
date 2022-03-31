package xyz.rokkiitt.sector.objects.premiumcase;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.NukkitRunnable;
import com.google.common.collect.Maps;
import xyz.rokkiitt.sector.Main;
import xyz.rokkiitt.sector.utils.RandomUtil;
import xyz.rokkiitt.sector.utils.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CaseManager {
    public static Map<Integer, PremiumCase> drop = new ConcurrentHashMap<>();
    static {
        int i = 0;
        for (PremiumCase premiumCase : PremiumCaseManager.getItems()){
            drop.put(i, premiumCase);
            i++;
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
        (new NukkitRunnable() {
            public void run() {
                if (!CaseManager.isInCase(p)) {
                    cancel();
                    return;
                }
                if (p.getInventory().getItemInHand().getId() != Item.CHEST && !Util.hasNBTTag(p.getInventory().getItemInHand(), "premiumcase")) {
                    cancel();
                    return;
                }
                if (inv.getRool() >= inv.getRoolMax()) {
                    Item win = inv.getInv().getItem(13);
//                    if (RandomUtil.getChance(100.0D - PremiumCaseGUI.drops.get())) {
//                        inv.setRool(inv.getRool() - 1);
//                        return;
//                    }
                    cancel();
                    Item item = p.getInventory().getItemInHand();
                    item.setCount(item.getCount() - 1);
                    p.getInventory().setItemInHand(item);
//                    Bukkit.broadcastMessage(ChatUtil.fixColor(CasePlugin.getPlugin().getConfig().getString("place.wylosowano-item")).replace("{NICK}", p.getName()).replace("{ILOSC}", String.valueOf(win.getAmount())).replace("{ITEM}", PolishItemNames.getPolishName(win.getType(), win.getData().getData())));
                    Util.giveItem(p, win);
                    CaseManager.removeCase(p);
                    (new NukkitRunnable() {
                        public void run() {
                            p.getInventory().onClose(p);
                        }
                    }).runTaskLater((Plugin) Main.getPlugin(), 40);
                    return;
                }
                inv.setRool(inv.getRool() + 1);
                new PremiumCaseGUI(p);
//                p.playSound(p.getLocation(), Sound.CLICK, 10.0F, 10.0F);

                    Item i = CaseManager.drop.get(RandomUtil.getRandInt(0, CaseManager.drop.size() - 1)).getWhat();
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
        }).runTaskTimer((Plugin)Main.getPlugin(), 5, 5);
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
