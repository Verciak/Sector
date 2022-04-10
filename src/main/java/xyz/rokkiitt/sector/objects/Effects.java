package xyz.rokkiitt.sector.objects;

import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.objects.inventory.FakeSlotChangeEvent;
import xyz.rokkiitt.sector.objects.inventory.inventories.DoubleChestFakeInventory;
import xyz.rokkiitt.sector.utils.ItemBuilder;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.item.*;
import cn.nukkit.potion.*;

public class Effects extends DoubleChestFakeInventory
{
    public Effects() {
        super(null, Util.fixColor("&6Efekty"));
        this.sendGui();
    }
    
    @Override
    protected void onSlotChange(final FakeSlotChangeEvent e) {
        e.setCancelled(true);
        final int slot = e.getAction().getSlot();
        final String id = Util.getNBTTagValue(e.getAction().getSourceItem(), "effectId");
        if (slot != 4) {
            if (!id.isEmpty()) {
                final int cost = Integer.parseInt(Util.getNBTTagValue(e.getAction().getSourceItem(), "effectCost"));
                final int level = Integer.valueOf(Util.getNBTTagValue(e.getAction().getSourceItem(), "effectLevel")) - 1;
                final int dur = Integer.valueOf(Util.getNBTTagValue(e.getAction().getSourceItem(), "effectDuration")) * 20;
                final int efid = Integer.valueOf(id);
                if (efid == 5) {
                    if (level == 0 && !Settings.ENABLE_STRENGHT1) {
                        return;
                    }
                    if (level >= 1 && !Settings.ENABLE_STRENGHT2) {
                        return;
                    }
                }
                if (efid == 1) {
                    if (level == 0 && !Settings.ENABLE_SPEED1) {
                        return;
                    }
                    if (level >= 1 && !Settings.ENABLE_SPEED2) {
                        return;
                    }
                }
                if (!e.getPlayer().getInventory().contains(Item.get(133, Integer.valueOf(0), cost))) {
                    return;
                }
                e.getPlayer().addEffect(Effect.getEffect(efid).setAmplifier(level).setDuration(dur));
                Util.removeItemById(e.getPlayer(), Item.get(133, Integer.valueOf(0), cost));
            }
            return;
        }
        if (!e.getPlayer().getInventory().contains(Item.get(133, Integer.valueOf(0), 4))) {
            return;
        }
        Util.removeItemById(e.getPlayer(), Item.get(133, Integer.valueOf(0), 4));
        e.getPlayer().removeAllEffects();
        final Effect ef = Effect.getEffect(16);
        ef.setVisible(false);
        ef.setAmbient(true);
        ef.setAmplifier(1);
        ef.setDuration(Integer.MAX_VALUE);
        e.getPlayer().addEffect(ef);
    }
    
    private void sendGui() {
        this.clearAll();
        this.fill();
        this.setServerGui();
        this.clear(18);
        this.clear(27);
        this.clear(26);
        this.clear(35);
        this.setItem(19, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(257).setTitle("&r&6Haste I").setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex8 blokow szmaragdu" }).build(), "effectId", "3"), "effectLevel", "1"), "effectDuration", "180"), "effectCost", "8"));
        this.setItem(28, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(278).setTitle("&r&6Haste II").setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex16 blokow szmaragdu" }).build(), "effectId", "3"), "effectLevel", "2"), "effectDuration", "180"), "effectCost", "16"));
        this.setItem(21, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(267).setTitle("&r&6Sila I" + (Settings.ENABLE_STRENGHT1 ? "" : "&r&7(&cOFF&7)")).setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex8 blokow szmaragdu" }).build(), "effectId", "5"), "effectLevel", "1"), "effectDuration", "180"), "effectCost", "8"));
        this.setItem(30, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(276).setTitle("&r&6Sila II" + (Settings.ENABLE_STRENGHT2 ? "" : "&r&7(&cOFF&7)")).setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex16 blokow szmaragdu" }).build(), "effectId", "5"), "effectLevel", "2"), "effectDuration", "180"), "effectCost", "16"));
        this.setItem(23, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(353).setTitle("&r&6Szybkosc I" + (Settings.ENABLE_SPEED1 ? "" : "&r&7(&cOFF&7)\u270b")).setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex8 blokow szmaragdu" }).build(), "effectId", "1"), "effectLevel", "1"), "effectDuration", "180"), "effectCost", "8"));
        this.setItem(32, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(353).setTitle("&r&6Szybkosc II" + (Settings.ENABLE_SPEED2 ? "" : "&r&7(&cOFF&7)\u270b")).setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex16 blokow szmaragdu" }).build(), "effectId", "1"), "effectLevel", "2"), "effectDuration", "180"), "effectCost", "16"));
        this.setItem(25, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(309).setTitle("&r&6Wysokie skakanie I\u270b").setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex8 blokow szmaragdu" }).build(), "effectId", "8"), "effectLevel", "1"), "effectDuration", "180"), "effectCost", "8"));
        this.setItem(34, Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(Util.addNBTTagWithValue(new ItemBuilder(309).setTitle("&r&6Wysokie skakanie II\u270b").setLore(new String[] { "&r&f Czas: &e3 min", "&r&f Koszt: &ex16 blokow szmaragdu" }).build(), "effectId", "8"), "effectLevel", "2"), "effectDuration", "180"), "effectCost", "16"));
        this.setItem(4, new ItemBuilder(325, 1, 1).setTitle("&r&6Wyczysc efekty\u270b").setLore(new String[] { "&r&f Koszt: &ex4 blokow szmaragdu" }).build());
    }
}
