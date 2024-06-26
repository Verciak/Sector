package xyz.rokkiitt.sector.objects.modify;

import xyz.rokkiitt.sector.objects.drop.DropManager;
import xyz.rokkiitt.sector.objects.cobblex.CobblexManager;
import xyz.rokkiitt.sector.objects.kits.KitManager;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;
import xyz.rokkiitt.sector.objects.premiumcase.CaseManager;
import xyz.rokkiitt.sector.objects.premiumcase.PremiumCaseManager;

public class ModifyManager {
    public static void load() {
        DropManager.load();
        PandoraManager.load();
        CobblexManager.load();
        MeteoriteManager.load();
        PremiumCaseManager.load();
        KitManager.load("player");
        KitManager.load("vip");
        KitManager.load("svip");
        KitManager.load("sponsor");
        CaseManager.onLoad();
    }
}
