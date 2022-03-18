package xyz.rokkiitt.sector.objects.modify;

import xyz.rokkiitt.sector.objects.drop.DropManager;
import xyz.rokkiitt.sector.objects.cobblex.CobblexManager;
import xyz.rokkiitt.sector.objects.kits.KitManager;
import xyz.rokkiitt.sector.objects.meteorite.MeteoriteManager;
import xyz.rokkiitt.sector.objects.pandora.PandoraManager;

public class ModifyManager {
    public static void load() {
        DropManager.load();
        PandoraManager.load();
        CobblexManager.load();
        MeteoriteManager.load();
        KitManager.load("player");
        KitManager.load("vip");
        KitManager.load("svip");
        KitManager.load("sponsor");
    }
}
